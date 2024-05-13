package org.example;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class UtilizatorController implements ITripObserver {
    public TableView<ExcursieDTO> tabelMain;

    public TableColumn<ExcursieDTO, String> coloanaMainObiectiv;
    public TableColumn<ExcursieDTO, String> coloanaMainFirma;
    public TableColumn<ExcursieDTO, LocalTime> coloanaMainOra;
    public TableColumn<ExcursieDTO, Double> coloanaMainPret;
    public TableColumn<ExcursieDTO, Integer> coloanaMainLocuri;

    public ComboBox<LocalTime> comboDeLa;
    public ComboBox<LocalTime> comboPanaLa;

    public TextField fieldObiectiv;
    public Button butonCauta;
    public TableView<ExcursieDTO> tabelSecundar;

    public TableColumn<ExcursieDTO, String> coloanaSecundarObiectiv;
    public TableColumn<ExcursieDTO, String> coloanaSecundarFirme;
    public TableColumn<ExcursieDTO, LocalTime> coloanaSecundarOra;
    public TableColumn<ExcursieDTO, Double> coloanaSecundarPret;
    public TableColumn<ExcursieDTO, Integer> coloanaSecundarLocuri;

    public TextField fieldNume;
    public TextField fieldTelefon;

    public Spinner<Integer> spinnerBilete;

    public GridPane gridRezerva;
    public GridPane gridInitial;
    public GridPane gridMain;

    public TextField usernameLogin;
    public PasswordField passwordLogin;

    public Button loginButton;
    public GridPane gridRegister;

    private String obiectivCautat;
    private LocalTime deLaCautat;
    private LocalTime panaLaCautat;
    private ITripServices server;

    private Utilizator utilizator;
    private Stage stage;

    ObservableList<ExcursieDTO> modelTabel = FXCollections.observableArrayList();
    ObservableList<ExcursieDTO> modelTabelSecundar = FXCollections.observableArrayList();

    public void setServer(ITripServices server, Stage stage) {
        this.utilizator = null;
        this.stage = stage;
        this.server = server;
        gridRegister.setVisible(false);
        gridInitial.setVisible(true);
        gridMain.setVisible(false);
        stage.setOnCloseRequest(event -> {
            logout();
        });
    }
    public void loadWindow(Utilizator client){
//        this.serviceApplication = serviceApplication;
        gridInitial.setVisible(false);
        this.utilizator = client;
        loadComboTime();
        loadTabelMain();
        loadTabelSecundar();
        gridRezerva.setVisible(false);
        gridMain.setVisible(true);
    }


    private void loadTabelMain(){
        coloanaMainObiectiv.setCellValueFactory(new PropertyValueFactory<>("obiectivTuristic"));
        coloanaMainFirma.setCellValueFactory(new PropertyValueFactory<>("firmaTransport"));
        coloanaMainOra.setCellValueFactory(new PropertyValueFactory<>("oraPlecarii"));
        coloanaMainPret.setCellValueFactory(new PropertyValueFactory<>("pret"));
        coloanaMainLocuri.setCellValueFactory(new PropertyValueFactory<>("numarLocuriDisponibile"));

//        coloanaMain2.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(serviceApp.loginUser(cellDataFeatures.toString()).get().getName()));
        tabelMain.setItems(modelTabel);
        this.reloadTabelMain();
        tabelMain.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(ExcursieDTO excursieDTO, boolean empty) {
                super.updateItem(excursieDTO, empty);
                if (excursieDTO == null || empty) {
                    setStyle("");
                } else {
                    // Set condition for which rows should be red
                    if (excursieDTO.getNumarLocuriDisponibile() ==0) {
                        setStyle("-fx-background-color: #fccf2b;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

    }

    private void loadTabelSecundar(){
        coloanaSecundarObiectiv.setCellValueFactory(new PropertyValueFactory<>("obiectivTuristic"));
        coloanaSecundarFirme.setCellValueFactory(new PropertyValueFactory<>("firmaTransport"));
        coloanaSecundarOra.setCellValueFactory(new PropertyValueFactory<>("oraPlecarii"));
        coloanaSecundarPret.setCellValueFactory(new PropertyValueFactory<>("pret"));
        coloanaSecundarLocuri.setCellValueFactory(new PropertyValueFactory<>("numarLocuriDisponibile"));

        tabelSecundar.setItems(modelTabelSecundar);
        tabelSecundar.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                gridRezerva.setVisible(true);
                loadSpinnerBilete();
            }
            else{
                gridRezerva.setVisible(false);
            }
        });
        tabelSecundar.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(ExcursieDTO excursieDTO, boolean empty) {
                super.updateItem(excursieDTO, empty);
                if (excursieDTO == null || empty) {
                    setStyle("");
                } else {
                    // Set condition for which rows should be red
                    if (excursieDTO.getNumarLocuriDisponibile() ==0) {
                        setStyle("-fx-background-color: #fccf2b;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
//        this.reloadTabelSecundar();
    }

    private void reloadTabelMain(){
        List<ExcursieDTO> lista = null;
        try {
            lista = server.getAllExcursii();
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
        this.modelTabel.setAll(lista);
//        tabelMain.getSelectionModel().clearSelection();
    }


//    private void reloadTabelSecundar(){
//        var lista = serviceApplication.getAllExcursii();
//        this.modelTabelSecundar.setAll(lista);
//    }

    private void loadComboTime(){
        this.comboDeLa.setItems(FXCollections.observableArrayList(LocalTime.of(4, 0),LocalTime.of(6, 0) ,LocalTime.of(8, 0), LocalTime.of(10, 0), LocalTime.of(12, 0), LocalTime.of(14, 0), LocalTime.of(16, 0), LocalTime.of(18, 0), LocalTime.of(20, 0), LocalTime.of(22, 0)));
        this.comboPanaLa.setItems(FXCollections.observableArrayList(LocalTime.of(4, 0),LocalTime.of(6, 0) ,LocalTime.of(8, 0), LocalTime.of(10, 0), LocalTime.of(12, 0), LocalTime.of(14, 0), LocalTime.of(16, 0), LocalTime.of(18, 0), LocalTime.of(20, 0), LocalTime.of(22, 0)));
    }

    public void handleCauta(ActionEvent actionEvent) {
        LocalTime deLa = comboDeLa.getValue();
        LocalTime panaLa = comboPanaLa.getValue();
        String obiectiv = fieldObiectiv.getText();
        if(deLa == null || panaLa == null || obiectiv.isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Date invalide", "Introduceti date valide");
            return;
        }
        if(deLa.isAfter(panaLa)){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Date invalide", "Introduceti date valide");
            return;
        }
        this.obiectivCautat = obiectiv;
        this.deLaCautat = deLa;
        this.panaLaCautat = panaLa;
        reloadTabelSecundar();
    }

    private void reloadTabelSecundar(){
        if(obiectivCautat == null || deLaCautat == null || panaLaCautat == null){
            return;
        }
        try {
            this.modelTabelSecundar.setAll(server.getExcursiiByFilter(obiectivCautat, deLaCautat, panaLaCautat));
        }
        catch (RuntimeException e){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Date invalide", e.getMessage());
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleRezerva(ActionEvent actionEvent) {
        var numeClient = fieldNume.getText();
        var telefonClient = fieldTelefon.getText();
        var numarBilete = spinnerBilete.getValue();
        var excursie = tabelSecundar.getSelectionModel().getSelectedItem();
        if(numeClient.isEmpty() || telefonClient.isEmpty() || excursie == null || numarBilete == null || numarBilete <= 0){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Date invalide", "Introduceti date valide");
            return;
        }
        try {
            server.rezervaBilete(excursie.getId(), numeClient, telefonClient, numarBilete);
        }
        catch (RuntimeException e){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Date invalide", e.getMessage());
            return;
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
        handleCancel(actionEvent);
        reloadTabelMain();
        reloadTabelSecundar();
    }

    public void handleCancel(ActionEvent actionEvent) {
        gridRezerva.setVisible(false);
        tabelSecundar.getSelectionModel().clearSelection();
        fieldNume.clear();
        fieldTelefon.clear();
        spinnerBilete.getValueFactory().setValue(1);
    }
    public void loadSpinnerBilete() {
        var excursie = tabelSecundar.getSelectionModel().getSelectedItem();
        if(excursie == null){
            return;
        }
        var locuriDisponibile = excursie.getNumarLocuriDisponibile();
        if(locuriDisponibile == 0){
            spinnerBilete.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
            return;
        }
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, locuriDisponibile, 1);
        spinnerBilete.setValueFactory(valueFactory);
    }

    public void handlerRegisterRequest(ActionEvent actionEvent) {
    }

    public void handleLogin(ActionEvent actionEvent) {
        String username = usernameLogin.getText();
        String password = passwordLogin.getText();
        System.out.println("1utilizatorCotroller///handleRegisterRequest///username: " + username + " password: " + password);
        usernameLogin.clear();
        passwordLogin.clear();
        if (username.length() <= 4 || password.length() <= 4) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Date invalide", "Introduceti date valide");
            return;
        }
        Optional<Utilizator> clientOpt = Optional.empty();
        try {
            System.out.println("2utilizatorCotroller///handleRegisterRequest///username: " + username + " password: " + password);
            clientOpt = server.loginUser(username, password, this);
            System.out.println("3utilizatorCotroller///handleRegisterRequest///username: " + username + " password: " + password);

        }
        catch (Exception e){
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Eroare", e.getMessage());
            return;
        }
        if (clientOpt.isEmpty()) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Nu exista userul", "Mai incearca");
            return;
        }
        var utilizator = clientOpt.get();
        loadWindow(utilizator);
    }

    public void handlerSave(ActionEvent actionEvent) {

    }

    public void handlerCancel(ActionEvent actionEvent) {

    }

    @Override
    public void reservationUpdate() {
        Platform.runLater(() -> {
            reloadTabelMain();
            reloadTabelSecundar();
        });
    }

    public void logout(){
        if (utilizator == null) {
            return;
        }
        try {
            server.logOut(utilizator.getUsername());
        } catch (AppException e) {
            throw new RuntimeException(e);
        }
    }
}
