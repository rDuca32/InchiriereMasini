package repoTests;

import domain.Masina;
import domain.MasinaConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.RepositoryException;
import repository.TextFileRepository;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TextFileRepositoryTest {
    private static final String TEMP_FILE = "tempMasina.txt";
    private TextFileRepository<Masina> masinaRepository;

    @BeforeEach
    public void setUp() throws IOException {
        File file = new File(TEMP_FILE);
        if (file.exists()) {
            file.delete();
        }
        masinaRepository = new TextFileRepository<>(TEMP_FILE, new MasinaConverter());
    }

    @Test
    public void testAddMasinaToFile() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepository.add(masina);
        assertEquals(1, masinaRepository.size());
    }

    @Test
    public void testFindMasinaFromFile() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepository.add(masina);
        masinaRepository = new TextFileRepository<>(TEMP_FILE, new MasinaConverter());
        Masina foundMasina = masinaRepository.find(1);
        assertNotNull(foundMasina);
        assertEquals("Dacia", foundMasina.getMarca());
        assertEquals("Logan", foundMasina.getModel());
    }

    @Test
    public void testRemoveMasinaFromFile() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepository.add(masina);
        masinaRepository.remove(masina);
        assertEquals(0, masinaRepository.size());
    }

    @Test
    public void testUpdateMasinaInFile() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepository.add(masina);
        masina.setModel("Spring");
        masinaRepository.update(masina);
        Masina updatedMasina = masinaRepository.find(1);
        assertNotNull(updatedMasina);
        assertEquals("Spring", updatedMasina.getModel());
    }
}
