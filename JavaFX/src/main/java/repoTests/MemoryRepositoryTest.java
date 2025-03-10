package repoTests;

import domain.Masina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.DuplicateIDException;
import repository.MemoryRepository;
import repository.RepositoryException;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryRepositoryTest {
    private MemoryRepository<Masina> masinaRepository;

    @BeforeEach
    public void setUp() {
        masinaRepository = new MemoryRepository<>();
    }

    @Test
    public void testAddMasina() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepository.add(masina);
        assertEquals(1, masinaRepository.size());
        assertNotNull(masinaRepository.find(1));
    }

    @Test
    public void testFindMasina() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepository.add(masina);
        Masina foundMasina = masinaRepository.find(1);
        assertNotNull(foundMasina);
        assertEquals("Dacia", foundMasina.getMarca());
        assertEquals("Logan", foundMasina.getModel());
    }

    @Test
    public void testUpdateMasina() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepository.add(masina);
        masina.setModel("Sandero");
        masinaRepository.update(masina);
        Masina updatedMasina = masinaRepository.find(1);
        assertNotNull(updatedMasina);
        assertEquals("Sandero", updatedMasina.getModel());
    }

    @Test
    public void testRemoveMasina() throws RepositoryException {
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepository.add(masina);
        masinaRepository.remove(masina);
        assertEquals(0, masinaRepository.size());
        assertNull(masinaRepository.find(1));
    }

    @Test
    public void testAddDuplicateMasina() {
        Masina masina1 = new Masina(1, "Dacia", "Logan");
        Masina masina2 = new Masina(1, "Dacia", "Spring");
        try {
            masinaRepository.add(masina1);
            masinaRepository.add(masina2);
            fail("Expected DuplicateIDException");
        } catch (RepositoryException e) {
            assertTrue(e instanceof DuplicateIDException);
        }
    }
}

