import ro.mpp2024.Domain.Client;
import ro.mpp2024.Domain.Excursie;
import ro.mpp2024.Service.Service;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
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
import java.util.Properties;

public class LoginMain extends Application
{

    public LoginMain() {
        // You can optionally initialize fields here if needed
    }



    public static void main(String[] args) {
    Properties props=new Properties();
    try {
        props.load(new FileReader("bd.config"));
    } catch (IOException e) {
        System.out.println("Cannot find bd.config "+e);
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
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        IClientRepository clientRepository = new ClientDBRepository(props);
        IExcursieRepository excursieRepository = new ExcursieDBRepository(props);
        IRezervareRepository rezervareRepository = new RezervareDBRepository(props);
        IUtilizatorRepository utilizatorRepository = new UtilizatorDBRepository(props);
        Service service = new Service(clientRepository, excursieRepository, rezervareRepository, utilizatorRepository);

        BorderPane root = new BorderPane();


        AnchorPane leftAnchorPane = new AnchorPane();
        leftAnchorPane.setPrefSize(200, 200);
        root.setLeft(leftAnchorPane);

        AnchorPane rightAnchorPane = new AnchorPane();
        rightAnchorPane.setPrefSize(200, 200);
        root.setRight(rightAnchorPane);

        AnchorPane centerAnchorPane = new AnchorPane();
        centerAnchorPane.setPrefSize(200, 200);
        root.setCenter(centerAnchorPane);

        Label usernameLabel = new Label("Username");
        usernameLabel.setLayoutX(14);
        usernameLabel.setLayoutY(153);
        usernameLabel.setFont(Font.font("Arial Narrow Bold", 16));
        centerAnchorPane.getChildren().add(usernameLabel);

        Label passwordLabel = new Label("Password");
        passwordLabel.setLayoutX(14);
        passwordLabel.setLayoutY(190);
        passwordLabel.setFont(Font.font("Arial Narrow Bold", 16));
        centerAnchorPane.getChildren().add(passwordLabel);

        TextField usernameField = new TextField();
        usernameField.setLayoutX(91);
        usernameField.setLayoutY(150);
        usernameField.setPrefSize(173, 26);
        centerAnchorPane.getChildren().add(usernameField);

        TextField passwordField = new TextField();
        passwordField.setLayoutX(91);
        passwordField.setLayoutY(186);
        passwordField.setPrefSize(173, 26);
        centerAnchorPane.getChildren().add(passwordField);

        Button loginButton = new Button("Login");
        loginButton.setLayoutX(16);
        loginButton.setLayoutY(295);
        loginButton.setPrefSize(231, 26);
        loginButton.setStyle("-fx-background-color: #fab025;");
        centerAnchorPane.getChildren().add(loginButton);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Form");
        primaryStage.show();

        loginButton.setOnAction(event ->{
            try{
                if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Please fill in all fields");
                    alert.showAndWait();
                }
                else
                if(service.validateUsername(usernameField.getText(), passwordField.getText())){
                    primaryStage.close();
                    new CautareFX().start(new Stage());
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid username or password");
                    alert.showAndWait();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        });
    }


}

