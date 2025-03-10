package service;

import domain.Inchiriere;
import domain.Masina;
import repository.MemoryRepository;
import repository.RepositoryException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class ServiceInchiriere {
    private MemoryRepository<Inchiriere> repoInchiriere;

    public ServiceInchiriere(MemoryRepository<Inchiriere> repoInchiriere) {
        this.repoInchiriere = repoInchiriere;
    }

    private Date parseDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Data este invalida: " + dateStr, e);
        }
    }

    public void addInchiriere(int id, Masina masina, String dataInceputStr, String dataSfarsitStr) throws RepositoryException {
        Date dataInceput = parseDate(dataInceputStr);
        Date dataSfarsit = parseDate(dataSfarsitStr);

        for (Inchiriere inchiriere : repoInchiriere.getAll()) {
            if (inchiriere.getMasina().equals(masina)) {
                Date inchiriereInceput = inchiriere.getDataInceput();
                Date inchiriereSfarsit = inchiriere.getDataSfarsit();

                if ((dataInceput.before(inchiriereSfarsit) && dataSfarsit.after(inchiriereInceput)) ||
                        (dataInceput.equals(inchiriereInceput) || dataSfarsit.equals(inchiriereSfarsit))) {
                    throw new RepositoryException("Masina este deja inchiriata in intervalul precizat.");
                }
            }
        }

            Inchiriere inchiriere = new Inchiriere(id, masina, dataInceput, dataSfarsit);
        repoInchiriere.add(inchiriere);
    }

    public void deleteInchiriere(int id) throws RepositoryException {
        Inchiriere inchiriere = findInchiriere(id);
        if (inchiriere != null) {
            repoInchiriere.remove(inchiriere);
        } else {
            throw new RepositoryException("Inchirierea cu ID-ul specificat nu a fost gasita.");
        }
    }

    public void updateInchiriere(int id, Masina masina, String dataInceputStr, String dataSfarsitStr) throws RepositoryException {
        Inchiriere inchiriere = findInchiriere(id);
        if (inchiriere != null) {
            Date dataInceput = parseDate(dataInceputStr);
            Date dataSfarsit = parseDate(dataSfarsitStr);

            for (Inchiriere i : repoInchiriere.getAll()) {
                if (i.getMasina().equals(masina) && i.getId() != id) {
                    Date inchiriereInceput = i.getDataInceput();
                    Date inchiriereSfarsit = i.getDataSfarsit();

                    if ((dataInceput.before(inchiriereSfarsit) && dataSfarsit.after(inchiriereInceput)) ||
                            (dataInceput.equals(inchiriereInceput) || dataSfarsit.equals(inchiriereSfarsit))) {
                        throw new RepositoryException("Masina este deja inchiriata in intervalul precizat.");
                    }
                }
            }

            inchiriere.setMasina(masina);
            inchiriere.setDataInceput(dataInceput);
            inchiriere.setDataSfarsit(dataSfarsit);
            repoInchiriere.update(inchiriere);
        } else {
            throw new RepositoryException("Inchirierea cu ID-ul specificat nu a fost gasita.");
        }
    }

    public Inchiriere findInchiriere(int id) {
        return repoInchiriere.find(id);
    }

    public void showInchirieri(){
        repoInchiriere.getAll().forEach(System.out::println);
    }

    public void deleteAllInchirieri() {
        repoInchiriere.getAll().forEach(inchiriere -> {
            try {
                repoInchiriere.remove(inchiriere);
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        });
    }

    public Collection<Inchiriere> getInchirieri() {
        return repoInchiriere.getAll();
    }

    public List<Map.Entry<Masina, Long>> getCeleMaiDesInchiriateMasini() {
        return repoInchiriere.getAll().stream().collect(Collectors.groupingBy(Inchiriere::getMasina, Collectors.counting())).entrySet().stream().sorted(Map.Entry.<Masina, Long>comparingByValue().reversed()).toList();
    }

    public List<Map.Entry<String, Long>> getNumarInchirieriPeLuna() {
        return repoInchiriere.getAll().stream().collect(Collectors.groupingBy(inchiriere -> new SimpleDateFormat("MMMM").format(inchiriere.getDataInceput()), Collectors.counting())).entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).toList();
    }


    public List<Map.Entry<Masina, Long>> getMasiniInchiriateCelMaiMultTimp() {
        return repoInchiriere.getAll().stream().collect(Collectors.groupingBy(Inchiriere::getMasina, Collectors.summingLong(inchiriere -> {long diff = inchiriere.getDataSfarsit().getTime() - inchiriere.getDataInceput().getTime();return diff / (1000 * 60 * 60 * 24);}))).entrySet().stream().sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())).collect(Collectors.toList());
    }
}