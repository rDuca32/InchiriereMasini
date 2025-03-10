package tests;

import domain.Inchiriere;
import domain.Masina;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class InchiriereTest {

    @Test
    void testInchiriereConstructor() {
        Masina masina = new Masina(1, "Dacia", "Logan");
        Date dataInceput = new Date();
        Date dataSfarsit = new Date();

        Inchiriere inchiriere = new Inchiriere(1, masina, dataInceput, dataSfarsit);

        assertEquals(1, inchiriere.getId());
        assertEquals(masina, inchiriere.getMasina());
        assertEquals(dataInceput, inchiriere.getDataInceput());
        assertEquals(dataSfarsit, inchiriere.getDataSfarsit());
    }

    @Test
    void testToString() {
        Masina masina = new Masina(1, "Dacia", "Logan");
        Date dataInceput = new Date();
        Date dataSfarsit = new Date();

        Inchiriere inchiriere = new Inchiriere(1, masina, dataInceput, dataSfarsit);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String expectedString = "ID: 1 Inchiriere{masina=ID: 1 Masina{marca='Dacia', model='Logan'}, dataInceput=" +
                dateFormat.format(dataInceput) + ", dataSfarsit=" + dateFormat.format(dataSfarsit) + "}";

        assertEquals(expectedString, inchiriere.toString());
    }

    @Test
    void testEquals() {
        Masina masina = new Masina(1, "Dacia", "Logan");
        Date dataInceput = new Date();
        Date dataSfarsit = new Date();

        Inchiriere inchiriere1 = new Inchiriere(1, masina, dataInceput, dataSfarsit);
        Inchiriere inchiriere2 = new Inchiriere(1, masina, dataInceput, dataSfarsit);

        assertEquals(inchiriere1, inchiriere2);
    }

    @Test
    void testHashCode() {
        Masina masina = new Masina(1, "Dacia", "Logan");
        Date dataInceput = new Date();
        Date dataSfarsit = new Date();

        Inchiriere inchiriere1 = new Inchiriere(1, masina, dataInceput, dataSfarsit);
        Inchiriere inchiriere2 = new Inchiriere(1, masina, dataInceput, dataSfarsit);

        assertEquals(inchiriere1.hashCode(), inchiriere2.hashCode());
    }

    @Test
    void testSettersAndGetters() {
        Masina masina = new Masina(1, "Dacia", "Logan");
        Date dataInceput = new Date();
        Date dataSfarsit = new Date();

        Inchiriere inchiriere = new Inchiriere(1, masina, dataInceput, dataSfarsit);

        Date newDataInceput = new Date();
        Date newDataSfarsit = new Date();
        inchiriere.setDataInceput(newDataInceput);
        inchiriere.setDataSfarsit(newDataSfarsit);

        assertEquals(newDataInceput, inchiriere.getDataInceput());
        assertEquals(newDataSfarsit, inchiriere.getDataSfarsit());
    }
}
