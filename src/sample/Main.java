package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Hello World");

        primaryStage.setIconified(false);
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
