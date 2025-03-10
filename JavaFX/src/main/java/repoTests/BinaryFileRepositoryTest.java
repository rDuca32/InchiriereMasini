package repoTests;

import domain.Masina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.BinaryFileRepository;
import repository.RepositoryException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryFileRepositoryTest {
    private static final String TEMP_FILE = "tempMasina.bin";
    private BinaryFileRepository<Masina> masinaRepository;

    @BeforeEach
    public void setUp() {
        File file = new File(TEMP_FILE);
        if (file.exists()) {
            file.delete();
        }
        masinaRepository = new BinaryFileRepository<>(TEMP_FILE);
    }

    @Test
    public void testAddMasinaToBinaryFile() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepository.add(masina);
        assertEquals(1, masinaRepository.size());
    }

    @Test
    public void testFindMasinaFromBinaryFile() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepository.add(masina);
        masinaRepository = new BinaryFileRepository<>(TEMP_FILE);
        Masina foundMasina = masinaRepository.find(1);
        assertNotNull(foundMasina);
        assertEquals("Dacia", foundMasina.getMarca());
        assertEquals("Logan", foundMasina.getModel());
    }

    @Test
    public void testRemoveMasinaFromBinaryFile() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepository.add(masina);
        masinaRepository.remove(masina);
        assertEquals(0, masinaRepository.size());
    }

    @Test
    public void testUpdateMasinaInBinaryFile() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepository.add(masina);
        masina.setModel("Spring");
        masinaRepository.update(masina);
        Masina updatedMasina = masinaRepository.find(1);
        assertNotNull(updatedMasina);
        assertEquals("Spring", updatedMasina.getModel());
    }
}
