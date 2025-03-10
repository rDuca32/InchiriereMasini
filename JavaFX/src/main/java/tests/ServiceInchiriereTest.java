package tests;

import domain.Inchiriere;
import domain.Masina;
import org.junit.jupiter.api.Test;
import repository.MemoryRepository;
import repository.RepositoryException;
import service.ServiceInchiriere;

import static org.junit.jupiter.api.Assertions.*;

class ServiceInchiriereTest {

    @Test
    void testAddInchiriere() throws RepositoryException {
        MemoryRepository<Inchiriere> repo = new MemoryRepository<>();
        MemoryRepository<Masina> masinaRepo = new MemoryRepository<>();
        ServiceInchiriere service = new ServiceInchiriere(repo);
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepo.add(masina);

        service.addInchiriere(1, masina, "2024-11-01", "2024-11-10");

        Inchiriere inchiriere = repo.find(1);
        assertNotNull(inchiriere);
        assertEquals(masina, inchiriere.getMasina());
    }

    @Test
    void testDeleteInchiriere() throws RepositoryException {
        MemoryRepository<Inchiriere> repo = new MemoryRepository<>();
        MemoryRepository<Masina> masinaRepo = new MemoryRepository<>();
        ServiceInchiriere service = new ServiceInchiriere(repo);
        Masina masina = new Masina(1, "Dacia", "Logan");
        masinaRepo.add(masina);

        service.addInchiriere(1, masina, "2024-11-01", "2024-11-10");
        service.deleteInchiriere(1);

        assertNull(repo.find(1));
    }
}
