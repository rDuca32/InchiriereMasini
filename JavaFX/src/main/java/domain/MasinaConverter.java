package domain;

public class MasinaConverter extends EntityConverter<Masina> {
    @Override
    public String toString(Masina masina) {
        return masina.getId() + "," + masina.getMarca() + "," + masina.getModel();
    }

    @Override
    public Masina fromString(String string) {
        String[] parts = string.split(",");
        int id = Integer.parseInt(parts[0]);
        String marca = parts[1];
        String model = parts[2];
        return new Masina(id, marca, model);
    }
}
