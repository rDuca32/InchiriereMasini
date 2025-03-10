package repository;

import com.github.javafaker.Faker;
import domain.Masina;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class SQLMasinaRepository extends MemoryRepository<Masina> implements AutoCloseable {
    private static final String JDBC_URL = "jdbc:sqlite:C:/Users/rauld/Documents/GitHub/a4-rDuca32/JavaFX/InchirieriMasini.db";
    private Connection connection = null;

    public SQLMasinaRepository() {
        openConnection();
        createSchema();
        loadData();

        if (collection.isEmpty())
            initialValues();
    }

    private void createSchema() {
        try{
            try(final Statement stmt = connection.createStatement()){
                stmt.executeUpdate("create table if not exists masini (id int primary key, marca varchar(50), model varchar(50))");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Eroare la crearea tabelei masini" + e.getMessage());
        }
    }

    private void openConnection() {
        try{
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(JDBC_URL);
            if (connection == null || connection.isClosed()) {
                connection = ds.getConnection();
            }
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("PRAGMA foreign_keys = ON;");
//            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la conectarea cu baze de date", e);
        }
    }

    private void loadData() {
        try {
            try (PreparedStatement statement = connection.prepareStatement("select * from masini");
                 ResultSet rs = statement.executeQuery();) {
                while (rs.next()) {
                    Masina masina = new Masina(rs.getInt(("id")), rs.getString("marca"), rs.getString("model"));
                    collection.add(masina);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Masina elem) throws RepositoryException{
        super.add(elem);

        try{
            try(PreparedStatement statement = connection.prepareStatement("insert into masini values (?, ?, ?)")){
                statement.setInt(1, elem.getId());
                statement.setString(2, elem.getMarca());
                statement.setString(3, elem.getModel());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la adaugarea masinii in baza de date", e);
        }
    }

    @Override
    public void remove(Masina elem) throws RepositoryException {
        super.remove(elem);

//        try {
//            try (PreparedStatement statement = connection.prepareStatement("delete from inchirieri where id_masina = ?;")) {
//                statement.setInt(1, elem.getId());
//                statement.executeUpdate();
//            }
//        } catch (SQLException e) {
//            throw new RepositoryException("Eroare la stergerea din baza de date", e);
//        }

        try (PreparedStatement stmt = connection.prepareStatement("delete from masini where id = ?")) {
            stmt.setInt(1, elem.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la stergerea masinii din baza de date", e);
        }
    }

    @Override
    public void update(Masina elem) throws RepositoryException{
        super.update(elem);
        try{
            try(PreparedStatement statement = connection.prepareStatement("update masini set marca = ?, model = ? where id = ?")){
                statement.setString(1, elem.getMarca());
                statement.setString(2, elem.getModel());
                statement.setInt(3, elem.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Eroare la actualizarea masinii in baza de date", e);
        }
    }

    @Override
    public void close() throws Exception{
        try{
            if (connection!=null){
                connection.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Masina> getAll() {
        List<Masina> resultMasini = new ArrayList<>();
        String s = "select * from masini";
        try (PreparedStatement getAllStatement = connection.prepareStatement(s);
             ResultSet rs = getAllStatement.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String marca = rs.getString("marca");
                String model = rs.getString("model");
                Masina masina = new Masina(id, marca, model);
                resultMasini.add(masina);
            }
            return resultMasini;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la citirea masinilor din baza de date", e);
        }
    }

    private void initialValues() {
        List<Masina> masinaList = new ArrayList<>();

        String[] marci = {"Toyota", "Ford", "BMW", "Mercedes-Benz", "Audi", "Honda", "Hyundai", "Volkswagen", "Chevrolet", "Kia"};
        String[][] modele = {
                {"Corolla", "Camry", "RAV4", "Prius", "Highlander"},
                {"Focus", "Mustang", "Explorer", "Fiesta", "F-150"},
                {"X5", "3 Series", "5 Series", "X3", "Z4"},
                {"C-Class", "E-Class", "S-Class", "GLA", "GLE"},
                {"A3", "A4", "A6", "Q5", "Q7"},
                {"Civic", "Accord", "CR-V", "HR-V", "Pilot"},
                {"Elantra", "Sonata", "Tucson", "Santa Fe", "Kona"},
                {"Golf", "Passat", "Tiguan", "Polo", "ID.4"},
                {"Spark", "Malibu", "Equinox", "Traverse", "Tahoe"},
                {"Sportage", "Sorento", "Rio", "Soul", "Seltos"}
        };
        Random random = new Random();

        for (int i = 0;  i < 100; i++) {
            int id = i + 1;
            int marcaIndex = random.nextInt(marci.length);
            String marca = marci[marcaIndex];
            String model = modele[marcaIndex][random.nextInt(modele[marcaIndex].length)];

            Masina masina = new Masina(id, marca, model);
            masinaList.add(masina);
        }

        try (PreparedStatement statement = connection.prepareStatement("insert into masini values (?, ?, ?)")) {
            for (Masina masina : masinaList) {
                statement.setInt(1, masina.getId());
                statement.setString(2, masina.getMarca());
                statement.setString(3, masina.getModel());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



//private void initialValues() {
//    List<Masina> masinaList = new ArrayList<>();
//    Faker faker = new Faker();
//
//    for (int i = 0; i < 100; i++) {
//        int id = i + 1;
//
//        String marca = faker.company().name();
//
//        String model = faker.name().title();
//
//        Masina masina = new Masina(id, marca, model);
//        masinaList.add(masina);
//    }
//
//    try (PreparedStatement statement = connection.prepareStatement("insert into masini values (?, ?, ?)")) {
//        for (Masina masina : masinaList) {
//            statement.setInt(1, masina.getId());
//            statement.setString(2, masina.getMarca());
//            statement.setString(3, masina.getModel());
//            statement.executeUpdate();
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//}



//        masinaList.add(new Masina(1, "Dacia", "Logan"));
//        masinaList.add(new Masina(2, "Dacia", "Sandero"));
//        masinaList.add(new Masina(3, "Renault", "Clio"));
//        masinaList.add(new Masina(4, "Renault", "Megane"));
//        masinaList.add(new Masina(5, "Volkswagen", "Golf"));
//        masinaList.add(new Masina(6, "Volkswagen", "Passat"));
//        masinaList.add(new Masina(7, "Ford", "Focus"));
//        masinaList.add(new Masina(8, "Ford", "Mondeo"));
//        masinaList.add(new Masina(9, "Opel", "Astra"));
//        masinaList.add(new Masina(10, "Opel", "Insignia"));
//
//        try (PreparedStatement statement = connection.prepareStatement("insert into masini values (?, ?, ?)")) {
//            for (Masina masina : masinaList) {
//                statement.setInt(1, masina.getId());
//                statement.setString(2, masina.getMarca());
//                statement.setString(3, masina.getModel());
//                statement.executeUpdate();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }