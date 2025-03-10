package domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Inchiriere extends Entitate implements Serializable {
    private Masina masina;
    private Date dataInceput;
    private Date dataSfarsit;

    public Inchiriere(int id, Masina masina, Date dataInceput, Date dataSfarsit) {
        super(id);
        this.masina = masina;
        this.dataInceput = dataInceput;
        this.dataSfarsit = dataSfarsit;
    }

    public Masina getMasina() {
        return masina;
    }

    public void setMasina(Masina masina) {
        this.masina = masina;
    }

    public Date getDataInceput() {
        return dataInceput;
    }

    public void setDataInceput(Date dataInceput) {
        this.dataInceput = dataInceput;
    }

    public Date getDataSfarsit() {
        return dataSfarsit;
    }

    public void setDataSfarsit(Date dataSfarsit) {
        this.dataSfarsit = dataSfarsit;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return "ID: " + getId() + " Inchiriere{" +
                "masina=" + masina +
                ", dataInceput=" + dateFormat.format(dataInceput) +
                ", dataSfarsit=" + dateFormat.format(dataSfarsit) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inchiriere that = (Inchiriere) o;
        return Objects.equals(masina, that.masina) && Objects.equals(dataInceput, that.dataInceput) && Objects.equals(dataSfarsit, that.dataSfarsit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(masina, dataInceput, dataSfarsit);
    }
}
