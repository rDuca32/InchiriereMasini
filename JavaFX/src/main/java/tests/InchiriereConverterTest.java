package tests;

import domain.Inchiriere;
import domain.InchiriereConverter;
import domain.Masina;
import org.junit.jupiter.api.Test;
import repository.MemoryRepository;
import repository.RepositoryException;
import service.ServiceMasina;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class InchiriereConverterTest {

    @Test
    void testToString() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        Date dataInceput = new Date();
        Date dataSfarsit = new Date();
        Inchiriere inchiriere = new Inchiriere(1, masina, dataInceput, dataSfarsit);

        MemoryRepository<Masina> masinaRepo = new MemoryRepository<>();
        masinaRepo.add(masina);
        ServiceMasina serviceMasina = new ServiceMasina(masinaRepo);

        InchiriereConverter converter = new InchiriereConverter(serviceMasina);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String expected = "1,1," + dateFormat.format(dataInceput) + "," + dateFormat.format(dataSfarsit);
        assertEquals(expected, converter.toString(inchiriere));
    }

    @Test
    void testFromString() throws RepositoryException {
        String input = "1,1,2024-11-01,2024-11-10";

        MemoryRepository<Masina> masinaRepo = new MemoryRepository<>();
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepo.add(masina);
        ServiceMasina serviceMasina = new ServiceMasina(masinaRepo);

        InchiriereConverter converter = new InchiriereConverter(serviceMasina);
        Inchiriere inchiriere = converter.fromString(input);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            assertEquals(1, inchiriere.getId());
            assertEquals(masina, inchiriere.getMasina());
            assertEquals(dateFormat.parse("2024-11-01"), inchiriere.getDataInceput());
            assertEquals(dateFormat.parse("2024-11-10"), inchiriere.getDataSfarsit());
        } catch (Exception e) {
            fail("Parsing failed");
        }
    }
}
