package com.example.javafx;

import domain.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.*;
import service.ServiceInchiriere;
import service.ServiceMasina;
import ui.UI;
import utils.Settings;

import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {

    public static MemoryRepository<Masina> repoMasina;
    public static MemoryRepository<Inchiriere> repoInchiriere;
    public static ServiceMasina serviceMasina;
    public static ServiceInchiriere serviceInchiriere;

    @Override
    public void start(Stage stage) throws IOException, RepositoryException {
        SQLMasinaRepository repoMasina = new SQLMasinaRepository();
        ArrayList<Masina> masini = (ArrayList<Masina>) repoMasina.getAll();
        SQLInchiriereRepository repoInchiriere = new SQLInchiriereRepository(masini);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);

        HelloController controller = fxmlLoader.getController();
        controller.initialize();
        controller.setRepositories(repoMasina, repoInchiriere);

        stage.setTitle("Inchirieri Masini");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {

        Settings settings = Settings.getInstance();
        String executionMode = settings.getProperty("ExecutionMode");
        String repositoryType = settings.getProperty("Repository");
        String masiniFile = settings.getProperty("MasiniFile");
        String inchirieriFile = settings.getProperty("InchirieriFile");

        if ("db".equalsIgnoreCase(repositoryType)){
            repoMasina = new SQLMasinaRepository();
            ArrayList<Masina> masini = (ArrayList<Masina>) repoMasina.getAll();
            repoInchiriere = new SQLInchiriereRepository(masini);
        } else if ("text".equalsIgnoreCase(repositoryType)){
            EntityConverter<Masina> masinaConverter = new MasinaConverter();
            repoMasina = new TextFileRepository<>(masiniFile, masinaConverter);

            ServiceMasina serviceMasina = new ServiceMasina(repoMasina);
            EntityConverter<Inchiriere> inchiriereConverter = new InchiriereConverter(serviceMasina);
            repoInchiriere = new TextFileRepository<>(inchirieriFile, inchiriereConverter);
        } else if ("binary".equalsIgnoreCase(repositoryType)){
            repoMasina = new BinaryFileRepository<>(masiniFile);
            repoInchiriere = new BinaryFileRepository<>(inchirieriFile);
        } else {
            repoMasina = new MemoryRepository<>();
            repoInchiriere = new MemoryRepository<>();
        }

        serviceMasina = new ServiceMasina(repoMasina);
        serviceInchiriere = new ServiceInchiriere(repoInchiriere);

        if ("gui".equalsIgnoreCase(executionMode)) {
            launch();
        } else {
            UI ui = new UI(serviceMasina, serviceInchiriere);
            ui.showMenu();
        }
    }
}
