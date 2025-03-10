package tests;

import domain.Masina;
import domain.MasinaConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MasinaConverterTest {

    @Test
    void testToString() {
        Masina masina = new Masina(1, "Dacia", "Logan");
        MasinaConverter converter = new MasinaConverter();

        String expected = "1,Dacia,Logan";
        assertEquals(expected, converter.toString(masina));
    }

    @Test
    void testFromString() {
        String input = "1,Dacia,Logan";
        MasinaConverter converter = new MasinaConverter();
        Masina masina = converter.fromString(input);

        assertEquals(1, masina.getId());
        assertEquals("Dacia", masina.getMarca());
        assertEquals("Logan", masina.getModel());
    }
}
