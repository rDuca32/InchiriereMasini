package service;

import domain.Masina;
import repository.MemoryRepository;
import repository.RepositoryException;

import java.util.Collection;
import java.util.Collections;

public class ServiceMasina {
    private MemoryRepository<Masina> repoMasina;

    public ServiceMasina(MemoryRepository<Masina> repository) {
        this.repoMasina = repository;
    }

    public void addMasina(int id, String marca, String model) throws RepositoryException {
        Masina masina = new Masina(id, marca, model);
        repoMasina.add(masina);
    }

    public void deleteMasina(int id) throws RepositoryException {
        Masina masina = findMasina(id);
        if (masina != null) {
            repoMasina.remove(masina);
        } else {
            throw new RepositoryException("Nu a fost gasit");
        }
    }

    public void updateMasina(int id, String marca, String model) throws RepositoryException {
        Masina masina = findMasina(id);
        if (masina != null) {
            masina.setMarca(marca);
            masina.setModel(model);
            repoMasina.update(masina);
        } else {
            throw new RepositoryException("Nu a fost gasit");
        }
    }

    public Masina findMasina(int id) {
        return repoMasina.find(id);
    }

    public void showMasini() {
        repoMasina.getAll().forEach(System.out::println);
    }

    public void deleteAllMasini() {
        repoMasina.getAll().forEach(masina -> {
            try {
                repoMasina.remove(masina);
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        });
    }

    public Collection<Masina> getMasini() {
        return Collections.unmodifiableCollection(repoMasina.getAll());
    }
}
