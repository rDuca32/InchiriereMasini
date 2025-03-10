package com.example.javafx;

import domain.Inchiriere;
import domain.Masina;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import repository.RepositoryException;
import repository.SQLInchiriereRepository;
import repository.SQLMasinaRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HelloController {
    private SQLMasinaRepository repoMasina;
    private SQLInchiriereRepository repoInchiriere;

    @FXML
    private ListView<Masina> listMasina;
    @FXML
    private ListView<Inchiriere> listInchiriere;

    @FXML
    private TextField idMasina, marcaMasina, modelMasina;

    @FXML
    private TextField idInchiriere, idMasinaInchiriere, dataInceput, dataSfarsit;

    @FXML
    public void initialize() {
        listMasina.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        listInchiriere.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        System.out.println("Controller initialized successfully!");
    }

    public void setRepositories(SQLMasinaRepository repoMasina, SQLInchiriereRepository repoInchiriere) {
        this.repoMasina = repoMasina;
        this.repoInchiriere = repoInchiriere;

        loadMasini();
        loadInchirieri();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadInchirieri() {
        listInchiriere.getItems().clear();
        listInchiriere.getItems().addAll(repoInchiriere.getAll());
    }

    private void loadMasini() {
        listMasina.getItems().clear();
        listMasina.getItems().addAll(repoMasina.getAll());
    }

    @FXML
    private void onButtonAddMasina() {
        int id = Integer.parseInt(idMasina.getText());
        String marca = marcaMasina.getText();
        String model = modelMasina.getText();

        try{
            repoMasina.add(new Masina(id, marca, model));
            loadMasini();
        } catch (Exception e) {
            showError("Eroare la adaugare", e.getMessage());
        }
    }

    @FXML
    private void onButtonRemoveMasina(){
        int id = Integer.parseInt(idMasina.getText());
        try{
            repoMasina.remove(new Masina(id, "", ""));
            loadMasini();
        } catch (Exception e) {
            showError("Eroare la stergere", e.getMessage());
        }
    }

    @FXML void onButtonUpdateMasina(){
        int id = Integer.parseInt(idMasina.getText());
        String marca = marcaMasina.getText();
        String model = modelMasina.getText();
        try{
            repoMasina.update(new Masina(id, marca, model));
            loadMasini();
        } catch (Exception e) {
            showError("Eroare la actualizare", e.getMessage());
        }
    }

    private Date parseDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Data este invalida: " + dateStr, e);
        }
    }

    @FXML
    private void onButtonAddInchiriere() {
        int id = Integer.parseInt(idInchiriere.getText());
        int idMasina = Integer.parseInt(idMasinaInchiriere.getText());
        String dataInceputStr = dataInceput.getText();
        String dataSfarsitStr = dataSfarsit.getText();

        try{
            addInchiriere(id, repoMasina.find(idMasina), dataInceputStr, dataSfarsitStr);
            loadInchirieri();
        } catch (Exception e) {
            showError("Eroare la adaugare", e.getMessage());
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

    @FXML
    private void onButtonRemoveInchiriere() {
        int id = Integer.parseInt(idInchiriere.getText());
        try {
            repoInchiriere.remove(new Inchiriere(id, null, null, null));
            loadInchirieri();
        } catch (Exception e) {
            showError("Eroare la stergere", e.getMessage());
        }
    }

    public void updateInchiriere(int id, Masina masina, String dataInceputStr, String dataSfarsitStr) throws RepositoryException {
        Inchiriere inchiriere = repoInchiriere.find(id);
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

    @FXML
    private void onButtonUpdateInchiriere() {
        int id = Integer.parseInt(idInchiriere.getText());
        int idMasina = Integer.parseInt(idMasinaInchiriere.getText());
        String dataInceputStr = dataInceput.getText();
        String dataSfarsitStr = dataSfarsit.getText();

        try {
            updateInchiriere(id, repoMasina.find(idMasina), dataInceputStr, dataSfarsitStr);
            loadInchirieri();
        } catch (Exception e) {
            showError("Eroare la actualizare", e.getMessage());
        }
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

    @FXML
    private void onButtonShowMostRentedCarsDialogue(){
        List<Map.Entry<Masina, Long>> mostRentedCars = getCeleMaiDesInchiriateMasini();
        StringBuilder message = new StringBuilder();
        for (Map.Entry<Masina, Long> entry : mostRentedCars) {
            message.append(entry.getKey()).append(" - ").append(entry.getValue()).append(" inchirieri\n");
        }

        showInfo("Cele mai des inchiriate masini", message.toString());
    }

    @FXML
    private void onButtonShowNumberOfRentalsPerMonthDialogue(){
        List<Map.Entry<String, Long>> rentalsPerMonth = getNumarInchirieriPeLuna();
        StringBuilder message = new StringBuilder();
        for (Map.Entry<String, Long> entry : rentalsPerMonth) {
            message.append(entry.getKey()).append(" - ").append(entry.getValue()).append(" inchirieri\n");
        }

        showInfo("Numarul de inchirieri pe luna", message.toString());
    }

    @FXML
    private void onButtonShowCarsRentedForTheLongestTimeDialogue(){
        List<Map.Entry<Masina, Long>> carsRentedForTheLongestTime = getMasiniInchiriateCelMaiMultTimp();
        StringBuilder message = new StringBuilder();
        for (Map.Entry<Masina, Long> entry : carsRentedForTheLongestTime) {
            message.append(entry.getKey()).append(" - ").append(entry.getValue()).append(" zile\n");
        }

        showInfo("Masinile inchiriate cel mai mult timp", message.toString());
    }
}
