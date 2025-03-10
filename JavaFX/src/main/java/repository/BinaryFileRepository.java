package repository;

import domain.Entitate;

import java.io.*;
import java.util.ArrayList;

public class BinaryFileRepository<T extends Entitate> extends AbstractFileRepository<T> {

    public BinaryFileRepository(String fileName) {
        super(fileName);
        try {
            loadFile();
        } catch (RepositoryException e) {
            throw new RuntimeException("Nu s-a reusit incarcarea datelor din fisier.", e);
        }
    }

    @Override
    public void saveFile() throws RepositoryException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(collection);
        } catch (IOException e) {
            throw new RepositoryException("Eroare la salvarea in fisierul binar.", e);
        }
    }

    @Override
    public void loadFile() throws RepositoryException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            collection = (ArrayList<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Fisierul nu a fost gasit, initializare un fisier .bin gol.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryException("Eroare la citirea din fisierul binar.", e);
        }
    }
}
