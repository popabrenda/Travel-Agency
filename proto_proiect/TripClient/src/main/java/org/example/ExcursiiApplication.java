package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.protobuffprotocol.TripServicesRPCProxyProto;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ExcursiiApplication extends Application {
    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("views/utilizator-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        initialize(fxmlLoader, stage);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    private void initialize(FXMLLoader fxmlLoader, Stage stage){
        Properties propertiesClient = new Properties();
        try {
            propertiesClient.load(new FileReader("client.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Cannot find client.properties " + e);
        }
        var port = Integer.parseInt(propertiesClient.getProperty("port"));
        var host = propertiesClient.getProperty("host");
       // ITripServices server = new TripServicesRPCProxy(host, port);
        ITripServices server = new TripServicesRPCProxyProto(defaultServer, defaultPort);
        UtilizatorController mainController = fxmlLoader.getController();
        mainController.setServer(server, stage);
    }


    public static void main(String[] args) {
        launch();
    }
}