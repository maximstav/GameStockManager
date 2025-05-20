package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

// to run this app you need to run mvn javafx:run
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/mainFrame.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("view/style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Orders Management App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}