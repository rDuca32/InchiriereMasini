package tests;

import domain.Masina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MasinaTest {

    private Masina masina;

    @BeforeEach
    public void setUp() {
        masina = new Masina(1, "Dacia", "Logan");
    }

    @Test
    public void testMasinaConstructor() {
        assertEquals(1, masina.getId());
        assertEquals("Dacia", masina.getMarca());
        assertEquals("Logan", masina.getModel());
    }

    @Test
    public void testSettersAndGetters() {
        masina.setMarca("Renault");
        masina.setModel("Clio");
        assertEquals("Renault", masina.getMarca());
        assertEquals("Clio", masina.getModel());
    }

    @Test
    public void testToString() {
        String expected = "ID: 1 Masina{marca='Dacia', model='Logan'}";
        assertEquals(expected, masina.toString());
    }

    @Test
    public void testEquals() {
        Masina masina1 = new Masina(1, "Dacia", "Logan");
        Masina masina2 = new Masina(1, "Dacia", "Logan");
        assertEquals(masina1, masina2);
    }

    @Test
    public void testHashCode() {
        Masina masina1 = new Masina(1, "Dacia", "Logan");
        Masina masina2 = new Masina(1, "Dacia", "Logan");
        assertEquals(masina1.hashCode(), masina2.hashCode());
    }
}
