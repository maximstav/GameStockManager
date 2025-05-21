package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

/**
 * <h1>Warehouse Orders Management System</h1>
 * <p>
 * This JavaFX application allows administrators and clients to manage products, place orders, and
 * generate bills. The system uses a layered architecture with:
 * <ul>
 *     <li>Presentation layer (JavaFX controllers and GUI)</li>
 *     <li>Business logic layer (services)</li>
 *     <li>Data access layer (DAOs using reflection)</li>
 *     <li>MySQL database integration</li>
 * </ul>
 * <p>
 * Admins can manage clients/products; clients can place orders. Every order results in an auto-generated bill.
 *
 * to run this app you need to run mvn javafx:run
 *
 * @author Staver Maxim, 30223
 * @version 1.0
 */
// to run this app you need to run mvn javafx:run
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/mainFrame.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("view/style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Game Orders Management App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}