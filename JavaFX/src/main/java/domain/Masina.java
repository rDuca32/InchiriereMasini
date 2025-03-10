package domain;

import java.io.Serializable;
import java.util.Objects;

public class Masina extends Entitate implements Serializable {
    private String marca;
    private String model;

    public Masina(int id, String marca, String model) {
        super(id);
        this.marca = marca;
        this.model = model;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " Masina{" +
                "marca='" + marca + '\'' +
                ", model='" + model + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Masina masina = (Masina) o;
        return Objects.equals(marca, masina.marca) && Objects.equals(model, masina.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marca, model);
    }
}
