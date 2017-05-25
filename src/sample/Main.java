package sample;

import com.jfoenix.controls.JFXTabPane;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
//    @FXML
//    private JFXTabPane jfxTabPane;
    @Override
    public void start(Stage primaryStage) throws Exception{
        // root Node - корневой узел
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        ScreenshotController sc = new ScreenshotController();
        sc.setPrimaryStage(primaryStage);
        loader.setController(sc);

        primaryStage.setTitle("Hello World");

        primaryStage.setIconified(false);

        // scene - сцена, stage - подмостки
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

    }

    public void setIconified(boolean isIconified){

    }

    public static void main(String[] args) {
        launch(args);
    }
}
