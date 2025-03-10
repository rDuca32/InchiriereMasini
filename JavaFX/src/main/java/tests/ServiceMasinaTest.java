package tests;

import domain.Masina;
import org.junit.jupiter.api.Test;
import repository.MemoryRepository;
import repository.RepositoryException;
import service.ServiceMasina;

import static org.junit.jupiter.api.Assertions.*;

class ServiceMasinaTest {

    @Test
    void testAddMasina() throws RepositoryException {
        MemoryRepository<Masina> repo = new MemoryRepository<>();
        ServiceMasina service = new ServiceMasina(repo);

        service.addMasina(1, "Dacia", "Logan");

        Masina masina = repo.find(1);
        assertNotNull(masina);
        assertEquals("Dacia", masina.getMarca());
        assertEquals("Logan", masina.getModel());
    }

    @Test
    void testDeleteMasina() throws RepositoryException {
        MemoryRepository<Masina> repo = new MemoryRepository<>();
        ServiceMasina service = new ServiceMasina(repo);

        service.addMasina(1, "Dacia", "Logan");
        service.deleteMasina(1);

        assertNull(repo.find(1));
    }

    @Test
    void testUpdateMasina() throws RepositoryException {
        MemoryRepository<Masina> repo = new MemoryRepository<>();
        ServiceMasina service = new ServiceMasina(repo);

        service.addMasina(1, "Dacia", "Logan");
        service.updateMasina(1, "Renault", "Clio");

        Masina masina = repo.find(1);
        assertNotNull(masina);
        assertEquals("Renault", masina.getMarca());
        assertEquals("Clio", masina.getModel());
    }
}
