package repository;

import domain.Inchiriere;
import domain.Masina;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SQLInchiriereRepository extends MemoryRepository<Inchiriere> implements AutoCloseable {
    private static final String JDBC_URL = "jdbc:sqlite:C:/Users/rauld/Documents/GitHub/a4-rDuca32/JavaFX/InchirieriMasini.db";
    private Connection connection = null;
    private ArrayList<Masina> masini;

    public SQLInchiriereRepository(ArrayList<Masina> masini) throws RepositoryException {
        this.masini = masini;
        openConnection();
        createSchema();
        loadData();

        if (collection.isEmpty())
            initialInchirieri();
    }

    private void loadData() {
        try {
            try (PreparedStatement statement = connection.prepareStatement("select * from inchirieri");
                 ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    int id_inchiriere = rs.getInt("id_inchiriere");
                    int id_masina = rs.getInt("id_masina");
                    String dataInceputString = rs.getString("dataInceput");
                    String dataSfarsitString = rs.getString("dataSfarsit");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dataInceput = new Date(sdf.parse(dataInceputString).getTime());
                    Date dataSfarsit = new Date(sdf.parse(dataSfarsitString).getTime());
                    Inchiriere inchiriere = new Inchiriere(id_inchiriere, masini.get(id_masina), dataInceput, dataSfarsit);
                    collection.add(inchiriere);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createSchema() {
        try {
            try (final Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("create table if not exists inchirieri(id_inchiriere int primary key, id_masina int, dataInceput date, dataSfarsit date, foreign key (id_masina) references masini(id) on delete cascade);");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }
    }

    private void openConnection() {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(JDBC_URL);
            if (connection == null || connection.isClosed())
                connection = ds.getConnection();
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("PRAGMA foreign_keys = ON;");
//            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la conectarea cu baza de date", e);
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    @Override
    public void add(Inchiriere elem) throws RepositoryException {
        super.add(elem);

        try {
            try (PreparedStatement statement = connection.prepareStatement("insert into inchirieri values (?,?,?,?);")) {
                statement.setInt(1, elem.getId());
                statement.setInt(2, masini.indexOf(elem.getMasina()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                statement.setString(3, sdf.format(elem.getDataInceput()));
                statement.setString(4, sdf.format(elem.getDataSfarsit()));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la salvarea in baza de date", e);
        }
    }

    @Override
    public void remove(Inchiriere elem) throws RepositoryException {
        super.remove(elem);

        try {
            try (PreparedStatement statement = connection.prepareStatement("delete from inchirieri where id_inchiriere = ?;")) {
                statement.setInt(1, elem.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la stergerea din baza de date", e);
        }
    }

    @Override
    public void update(Inchiriere elem) throws RepositoryException {
        super.update(elem);

        try {
            try (PreparedStatement statement = connection.prepareStatement("update inchirieri set id_masina = ?, dataInceput = ?, dataSfarsit = ? where id_inchiriere = ?;")) {
                statement.setInt(1, masini.indexOf(elem.getMasina()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                statement.setString(2, sdf.format(elem.getDataInceput()));
                statement.setString(3, sdf.format(elem.getDataSfarsit()));
                statement.setInt(4, elem.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la actualizarea in baza de date", e);
        }
    }

    @Override
    public Collection<Inchiriere> getAll() {
        List<Inchiriere> resultInchirieri = new ArrayList<>();
        String s = "select * from inchirieri";
        try (PreparedStatement statement = connection.prepareStatement(s);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int id_inchiriere = rs.getInt("id_inchiriere");
                int id_masina = rs.getInt("id_masina");
                String dataInceputString = rs.getString("dataInceput");
                String dataSfarsitString = rs.getString("dataSfarsit");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dataInceput = new Date(sdf.parse(dataInceputString).getTime());
                Date dataSfarsit = new Date(sdf.parse(dataSfarsitString).getTime());
                Inchiriere inchiriere = new Inchiriere(id_inchiriere, masini.get(id_masina), dataInceput, dataSfarsit);
                resultInchirieri.add(inchiriere);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return resultInchirieri;
    }

    public void initialInchirieri() throws RepositoryException {
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            int id = i + 1;
            int masinaIndex = random.nextInt(masini.size() - 1);
            Masina masina = masini.get(masinaIndex);

            long currentTime = System.currentTimeMillis();

            long randomOffsetStart = random.nextInt(365) * 24L * 60L * 60L * 1000L;
            long randomOffsetEnd = randomOffsetStart + (1 + random.nextInt(10)) * 24L * 60L * 60L * 1000L;

            Date dataInceput = new Date(currentTime + randomOffsetStart);
            Date dataSfarsit = new Date(currentTime + randomOffsetEnd);

            if (dataSfarsit.before(dataInceput)) {
                dataSfarsit = new Date(dataInceput.getTime() + (1 + random.nextInt(10)) * 24L * 60L * 60L * 1000L);
            }

            Inchiriere inchiriere = new Inchiriere(id, masina, dataInceput, dataSfarsit);

            try {
                try (PreparedStatement statement = connection.prepareStatement("insert into inchirieri values (?,?,?,?);")) {
                    statement.setInt(1, inchiriere.getId());
                    statement.setInt(2, masini.indexOf(inchiriere.getMasina()));
                    statement.setString(3, dataInceput.toString());
                    statement.setString(4, dataSfarsit.toString());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RepositoryException("Eroare la salvarea in baza de date", e);
            }
        }
    }

}
