import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import ro.mpp2024.Domain.Client;
import ro.mpp2024.Service.Service;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ro.mpp2024.Domain.Excursie;
import ro.mpp2024.Repository.ClientDBRepository;
import ro.mpp2024.Repository.ExcursieDBRepository;
import ro.mpp2024.Repository.Interface.IClientRepository;
import ro.mpp2024.Repository.Interface.IExcursieRepository;
import ro.mpp2024.Repository.Interface.IRezervareRepository;
import ro.mpp2024.Repository.Interface.IUtilizatorRepository;
import ro.mpp2024.Repository.RezervareDBRepository;
import ro.mpp2024.Repository.UtilizatorDBRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class CautareFX extends Application {
    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        IClientRepository clientRepository = new ClientDBRepository(props);
        IExcursieRepository excursieRepository = new ExcursieDBRepository(props);
        IRezervareRepository rezervareRepository = new RezervareDBRepository(props);
        IUtilizatorRepository utilizatorRepository = new UtilizatorDBRepository(props);
        Service service = new Service(clientRepository, excursieRepository, rezervareRepository, utilizatorRepository);

        // Create instance of LoginMain and pass service to its constructor
        LoginMain loginMain = new LoginMain();

        // Launch JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        IClientRepository clientRepository = new ClientDBRepository(props);
        IExcursieRepository excursieRepository = new ExcursieDBRepository(props);
        IRezervareRepository rezervareRepository = new RezervareDBRepository(props);
        IUtilizatorRepository utilizatorRepository = new UtilizatorDBRepository(props);
        Service service = new Service(clientRepository, excursieRepository, rezervareRepository, utilizatorRepository);

        HBox root = new HBox();

        // Left Pane
        Pane leftPane = new Pane();
        leftPane.setPrefSize(477, 542);


        ///ID EXCURSIE REZERVARE LABEL
        Label idExcursieLabel1 = new Label("Id Excursie");
        idExcursieLabel1.setLayoutX(40);
        idExcursieLabel1.setLayoutY(350);
        idExcursieLabel1.setPrefSize(96, 25);
        idExcursieLabel1.setStyle("-fx-background-color: #f5c856;");
        idExcursieLabel1.setFont(Font.font("Arial Narrow Bold", 14));

        ///NUME CLIENT REZERVARE LABEL
        Label numeClientLabel1 = new Label("Nume Client");
        numeClientLabel1.setLayoutX(40);
        numeClientLabel1.setLayoutY(381);
        numeClientLabel1.setPrefSize(96, 25);
        numeClientLabel1.setStyle("-fx-background-color: #f5c856;");
        numeClientLabel1.setFont(Font.font("Arial Narrow Bold", 14));

        ///NUMAR TELEFON REZERVARE LABEL
        Label numarTelefonLabel = new Label("Numar Telefon");
        numarTelefonLabel.setLayoutX(40);
        numarTelefonLabel.setLayoutY(412);
        numarTelefonLabel.setPrefSize(96, 25);
        numarTelefonLabel.setStyle("-fx-background-color: #f5c856;");
        numarTelefonLabel.setFont(Font.font("Arial Narrow Bold", 14));

        ///NUMAR BILETE REZERVARE LABEL
        Label numarBileteLabel = new Label("Numar Bilete");
        numarBileteLabel.setLayoutX(40);
        numarBileteLabel.setLayoutY(444);
        numarBileteLabel.setPrefSize(96, 25);
        numarBileteLabel.setStyle("-fx-background-color: #f5c856;");
        numarBileteLabel.setFont(Font.font("Arial Narrow Bold", 14));

        ///OBIECTIV CAUTARE LABEL
        Label obiectivLabel2 = new Label("Obiectiv");
        obiectivLabel2.setLayoutX(40);
        obiectivLabel2.setLayoutY(169);
        obiectivLabel2.setPrefSize(96, 25);
        obiectivLabel2.setStyle("-fx-background-color: #f5c856;");
        obiectivLabel2.setFont(Font.font("Arial Narrow Bold", 14));

        ///ORA PLECARE CAUTARE LABEL
        Label oraPlecareLabel2 = new Label("De la ora:");
        oraPlecareLabel2.setLayoutX(40);
        oraPlecareLabel2.setLayoutY(204);
        oraPlecareLabel2.setPrefSize(96, 25);
        oraPlecareLabel2.setStyle("-fx-background-color: #f5c856;");
        oraPlecareLabel2.setFont(Font.font("Arial Narrow Bold", 14));

        ///ORA SOSIRE CAUTARE LABEL
        Label oraSosireLabel2 = new Label("Pana la ora:");
        oraSosireLabel2.setLayoutX(40);
        oraSosireLabel2.setLayoutY(236);
        oraSosireLabel2.setPrefSize(96, 25);
        oraSosireLabel2.setStyle("-fx-background-color: #f5c856;");
        oraSosireLabel2.setFont(Font.font("Arial Narrow Bold", 14));

        ///TEXT FIELD ORA PLECARE
        TextField oraPlecareTextField1 = new TextField();
        oraPlecareTextField1.setLayoutX(152);
        oraPlecareTextField1.setLayoutY(204);
        oraPlecareTextField1.setPrefSize(165, 25);

        ///TEXT FIELD OBIECTIV
        TextField obiectivTextField1 = new TextField();
        obiectivTextField1.setLayoutX(152);
        obiectivTextField1.setLayoutY(169);
        obiectivTextField1.setPrefSize(165, 25);

        ///TEXT FIELD ORA SOSIRE
        TextField oraSosireTextField2 = new TextField();
        oraSosireTextField2.setLayoutX(152);
        oraSosireTextField2.setLayoutY(236);
        oraSosireTextField2.setPrefSize(165, 25);

        ///TEXT FIELD NUMAR TELEFON
        TextField numarTelefonTextField = new TextField();
        numarTelefonTextField.setLayoutX(152);
        numarTelefonTextField.setLayoutY(412);
        numarTelefonTextField.setPrefSize(165, 25);

        ///TEXT FIELD NUMAR BILETE
        TextField numarBileteTextField = new TextField();
        numarBileteTextField.setLayoutX(152);
        numarBileteTextField.setLayoutY(444);
        numarBileteTextField.setPrefSize(165, 25);

        ///TEXT FIELD ID EXCURSIE
        TextField idExcursieTextField3 = new TextField();
        idExcursieTextField3.setLayoutX(152);
        idExcursieTextField3.setLayoutY(350);
        idExcursieTextField3.setPrefSize(165, 25);

        //TEXT FIELD NUME CLIENT
        TextField numeClientTextField = new TextField();
        numeClientTextField.setLayoutX(152);
        numeClientTextField.setLayoutY(381);
        numeClientTextField.setPrefSize(165, 25);


        Button cautareButton = new Button("Cautare");
        cautareButton.setLayoutX(53);
        cautareButton.setLayoutY(276);
        cautareButton.setPrefSize(71, 25);
        cautareButton.setStyle("-fx-background-color: #f5c856;");
        cautareButton.setFont(Font.font("Arial Narrow Bold", 14));


        Button rezervaButton = new Button("Rezerva");
        rezervaButton.setLayoutX(53);
        rezervaButton.setLayoutY(480);
        rezervaButton.setPrefSize(71, 25);
        rezervaButton.setStyle("-fx-background-color: #f5c856;");
        rezervaButton.setFont(Font.font("Arial Narrow Bold", 14));

        Button logoutButton = new Button("Logout");
        logoutButton.setLayoutX(341);
        logoutButton.setLayoutY(490);
        logoutButton.setPrefSize(62, 32);
        logoutButton.setStyle("-fx-background-color: #e8ae1c;");
        logoutButton.setFont(Font.font("Arial Narrow Bold", 14));
        logoutButton.setOnAction(event -> {
            primaryStage.close();
            new LoginMain().start(new Stage());

        });

//        Button showAllButton = new Button("Show All");
//        showAllButton.setLayoutX(logoutButton.getLayoutX() + 100); // Adjust X position to appear near the Logout button
//        showAllButton.setLayoutY(logoutButton.getLayoutY());
//        showAllButton.setPrefSize(71, 25);
//        showAllButton.setStyle("-fx-background-color: #f5c856;");
//        showAllButton.setFont(Font.font("Arial Narrow Bold", 14));
//        showAllButton.setOnAction(event -> {
//            List<Excursie> excursieList = (List<Excursie>) excursieRepository.findAll();
//            ObservableList<String> item = FXCollections.observableArrayList();
//            for (Excursie excursie : excursieList) {
//                item.add(excursie.toString());
//            }
//            ListView listView2 = new ListView();
//            listView2.setLayoutX(19);
//            listView2.setLayoutY(45);
//            listView2.setPrefSize(262, 474);
//            listView2.setItems(item);
//            leftPane.getChildren().add(listView2);
//        });


        ListView listView1 = new ListView();
        listView1.setLayoutX(34);
        listView1.setLayoutY(14);
        listView1.setPrefSize(349, 138);
        //create a list with all the clients
        List<Client> clientList = (List<Client>) clientRepository.findAll();
        ObservableList<Client> clientObservableList = FXCollections.observableArrayList(clientList);
        listView1.setItems(clientObservableList);

        leftPane.getChildren().addAll(
                idExcursieLabel1, numeClientLabel1, numarTelefonLabel, numarBileteLabel,
                obiectivLabel2, oraPlecareLabel2, oraSosireLabel2,
                oraPlecareTextField1, obiectivTextField1, oraSosireTextField2,
                numarTelefonTextField, numarBileteTextField, idExcursieTextField3,
                cautareButton, rezervaButton, listView1, logoutButton, numeClientTextField
        );

        // Right Pane
        Pane rightPane = new Pane();
        rightPane.setPrefSize(359, 542);

        Label listaActualizataLabel = new Label("Lista Actualizata");
        listaActualizataLabel.setLayoutX(86);
        listaActualizataLabel.setLayoutY(14);
        listaActualizataLabel.setPrefSize(126, 26);
        listaActualizataLabel.setStyle("-fx-background-color: #f5c856;");

        ListView listView2 = new ListView();
        listView2.setLayoutX(19);
        listView2.setLayoutY(45);
        listView2.setPrefSize(262, 474);

        //create a list with all the excursions
        List<Excursie> excursieList = (List<Excursie>) excursieRepository.findAll();
        ObservableList<Excursie> excursieObservableList = FXCollections.observableArrayList(excursieList);
        listView2.setItems(excursieObservableList);

        rightPane.getChildren().addAll(listaActualizataLabel, listView2);

        cautareButton.setOnAction(event -> {
            String obiectiv = obiectivTextField1.getText();
            String oraPlecare = oraPlecareTextField1.getText();
            String oraSosire = oraSosireTextField2.getText();
            if (obiectiv.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please fill in all fields");
                alert.showAndWait();
            } else if (Integer.parseInt(oraPlecareTextField1.getText()) < 0 || Integer.parseInt(oraSosireTextField2.getText()) < 0 || Integer.parseInt(oraPlecareTextField1.getText()) > 24 || Integer.parseInt(oraSosireTextField2.getText()) > 24) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Incorect data");
                alert.showAndWait();
            } else {
                List<Excursie> filterExcursii = (List<Excursie>) service.filterExcursii(obiectiv, Integer.parseInt(oraPlecareTextField1.getText()), Integer.parseInt(oraSosireTextField2.getText()));
                ObservableList<String> item = FXCollections.observableArrayList();
                for (Excursie excursie : filterExcursii) {
                    item.add(excursie.toString());
                }
                listView2.setItems(item);
                if (filterExcursii.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("No excursions found");
                    alert.showAndWait();

                    for (Excursie excursie : excursieRepository.findAll()) {
                        item.add(excursie.toString());
                    }
                }
                listView2.setItems(item);
            }

        });

        rezervaButton.setOnAction(event -> {
            String numeClient = numeClientTextField.getText();
            String numarTelefon = numarTelefonTextField.getText();
            int idExcursie = Integer.parseInt(idExcursieTextField3.getText());
            int numarBilete = Integer.parseInt(numarBileteTextField.getText());
//            System.out.println("DATE PRIMITE DE LA JAVA FX:");
//            System.out.println("numeClient"+numeClient);
//            System.out.println("numarTelefon"+numarTelefon);
//            System.out.println("idExcursie"+idExcursie);
//            System.out.println("numarBilete"+numarBilete);

            if (numeClient.isEmpty() || numarTelefon.isEmpty() || idExcursieTextField3.getText().isEmpty() || numarBileteTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please fill in all fields");
                alert.showAndWait();
            } else {
                List<Client> clientList1 = (List<Client>) clientRepository.findAll();
                Client client = null;
                for (Client c : clientList1) {
                    if (c.getNume().equals(numeClient) && c.getNrTelefon().equals(numarTelefon)) {
                        client = c;
                    }
                }
                if (client == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Client not found");
                    alert.showAndWait();
                } else {
                    try {
                        Excursie excursie = excursieRepository.findOne(idExcursie);
                        if (excursie.getLocuriDisponibile() < numarBilete) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Not enough available seats");
                            alert.showAndWait();
                        }
                        else if (excursie == null)
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Excursie not found");
                            alert.showAndWait();
                        }
                        else service.rezervare(client, excursie, numarBilete);

                    } catch (RuntimeException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(e.getMessage());
                        alert.showAndWait();
                    }
                }
            }
            //update the list with the excursions
            List<Excursie> excursieList1 = (List<Excursie>) excursieRepository.findAll();
            ObservableList<String> item = FXCollections.observableArrayList();
            for (Excursie excursie : excursieList1) {
                item.add(excursie.toString());
            }
            listView2.setItems(item);
        });



        // Add left and right panes to the HBox
        root.getChildren().addAll(leftPane, rightPane);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, 715, 541);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Agentie Turism");
        primaryStage.show();
    }
}


