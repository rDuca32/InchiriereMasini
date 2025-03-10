package ui;

import domain.Inchiriere;
import domain.Masina;
import repository.RepositoryException;
import service.ServiceInchiriere;
import service.ServiceMasina;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UI {
    private Scanner scanner;
    private ServiceInchiriere serviceInchiriere;
    private ServiceMasina serviceMasina;

    public UI(ServiceMasina serviceMasina, ServiceInchiriere serviceInchiriere) {
        this.serviceInchiriere = serviceInchiriere;
        this.serviceMasina = serviceMasina;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() throws RepositoryException {
        int option;
        do {
            System.out.println("\n~~~ Meniu ~~~\n");
            System.out.println("1. Adauga o masina");
            System.out.println("2. Adauga inchiriere");
            System.out.println("3. Afiseaza toate masinile");
            System.out.println("4. Afiseaza toate inchirierile");
            System.out.println("5. Sterge o masina");
            System.out.println("6. Sterge inchiriere");
            System.out.println("7. Actualizare masina");
            System.out.println("8. Actualizare inchiriere");
            System.out.println("9. Gasire masina");
            System.out.println("10. Gasire inchiriere");
            System.out.println("11. Sterge toate masinile");
            System.out.println("12. Sterge toate inchirierile");
            System.out.println("\n0. Iesire\n");
            System.out.print("Alege o optiune: ");
            try{
                option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1:
                        addMasina();
                        break;
                    case 2:
                        addInchiriere();
                        break;
                    case 3:
                        showMasini();
                        break;
                        case 4:
                            showInchirieri();
                            break;
                            case 5:
                                deleteMasina();
                                break;
                                case 6:
                                    deleteInchiriere();
                                    break;
                                    case 7:
                                        updateMasina();
                                        break;
                                        case 8:
                                            updateInchiriere();
                                            break;
                                            case 9:
                                                findMasina();
                                                break;
                                                case 10:
                                                    findInchiriere();
                                                    break;
                                                    case 11:
                                                        serviceMasina.deleteAllMasini();
                                                        break;
                                                        case 12:
                                                            serviceInchiriere.deleteAllInchirieri();
                                                            break;
                    case 0:
                        break;
                    default:
                        System.out.println("Optiune invalida");

                }
            } catch (InputMismatchException e) {
                System.out.println("Introduceti un numar valid");
                scanner.nextLine();
                option = -1;
            }
        } while (option != 0);
    }

    private void addMasina() {
        try {
            System.out.print("Introduceti ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Introduceti Marca: ");
            String marca = scanner.nextLine();
            System.out.print("Introduceti Modelul: ");
            String model = scanner.nextLine();

            serviceMasina.addMasina(id, marca, model);
            System.out.println("Masina a fost adaugata");
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteMasina() {
        try {
            System.out.print("Introduceti ID-ul masinii de sters: ");
            int id = scanner.nextInt();
            serviceMasina.deleteMasina(id);
            System.out.println("Masina a fost stearsa");
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateMasina() {
        try {
            System.out.print("Introduceti ID-ul masinii de actualizat: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Introduceti noua Marca: ");
            String marca = scanner.nextLine();
            System.out.print("Introduceti noul Model: ");
            String model = scanner.nextLine();

            serviceMasina.updateMasina(id, marca, model);
            System.out.println("Masina a fost actualizata");
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findMasina() {
        System.out.print("Introduceti ID-ul masinii cautate: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Masina masina = serviceMasina.findMasina(id);
        if (masina != null) {
            System.out.println(masina);
        } else {
            System.out.println("Masina cu ID-ul " + id + " nu a fost gasita.");
        }
    }

    private void showMasini(){
        serviceMasina.showMasini();
    }

    private void showInchirieri(){
        serviceInchiriere.showInchirieri();
    }

    private void addInchiriere() {
        try {
            System.out.println("ID inchiriere: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("ID masina: ");
            int masinaId = scanner.nextInt();
            scanner.nextLine();

            Masina masina = serviceMasina.findMasina(masinaId);
            if (masina == null) {
                System.out.println("Masina cu ID-ul " + masinaId + " nu a fost gasita.");
                return;
            }

            System.out.println("Data de inceput (yyyy-mm-dd): ");
            String dataInceputStr = scanner.nextLine();
            System.out.println("Data de sfarsit (yyyy-mm-dd): ");
            String dataSfarsitStr = scanner.nextLine();


            serviceInchiriere.addInchiriere(id, masina, dataInceputStr, dataSfarsitStr);
            System.out.println("Inchirierea a fost adaugata cu succes.");
        } catch (RepositoryException e) {
            System.out.println("Eroare la adaugarea inchirierii: " + e.getMessage());
        }
    }

    private void deleteInchiriere() {
        try {
            System.out.print("Introduceti ID-ul inchirierii de sters: ");
            int id = scanner.nextInt();
            serviceInchiriere.deleteInchiriere(id);
            System.out.println("Inchirierea a fost stearsa");
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateInchiriere() {
        try {
            System.out.print("Introduceti ID-ul inchirierii de actualizat: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Introduceti ID-ul masinii asociate: ");
            int masinaId = scanner.nextInt();
            scanner.nextLine();

            Masina masina = serviceMasina.findMasina(masinaId);
            if (masina == null) {
                System.out.println("Masina cu ID-ul " + masinaId + " nu a fost gasita.");
                return;
            }

            System.out.print("Introduceti noua data de inceput (yyyy-mm-dd): ");
            String dataInceputStr = scanner.nextLine();

            System.out.print("Introduceti noua data de sfarsit (yyyy-mm-dd): ");
            String dataSfarsitStr = scanner.nextLine();

            serviceInchiriere.updateInchiriere(id, masina, dataInceputStr, dataSfarsitStr);
            System.out.println("Inchirierea a fost actualizata");
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findInchiriere() {
        System.out.print("Introduceti ID-ul inchirierii cautate: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Inchiriere inchiriere = serviceInchiriere.findInchiriere(id);
        if (inchiriere != null) {
            System.out.println(inchiriere);
        } else {
            System.out.println("Inchirierea nu a fost gasita");
        }
    }
}



