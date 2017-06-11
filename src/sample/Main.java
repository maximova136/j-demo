package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main extends Application implements NativeKeyListener {

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        if (e.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN) {
            Platform.runLater(() -> {
                captureWindowController.prepareForCapture();
                System.out.println("prepared for capture");
            });
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }

    public static DbHandler db;

    @Override
    public void init() {
        // Get the logger for "org.jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        db = new DbHandler();
        db.onCreateDB();
        db.createTable();

        // Change the level for all handlers attached to the default logger.
        Handler[] handlers = Logger.getLogger("").getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            handlers[i].setLevel(Level.OFF);
        }

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem while registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new Main());
    }

    private static ScreenshotController sc = new ScreenshotController();

    public static Stage stage;
    public static CaptureWindowController captureWindowController;

    @Override
    public void start(Stage primaryStage) { //throws Exception {
        try {
            stage = primaryStage;
            // root Node - корневой узел
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            Parent root = loader.load();

            // capture window controller
            FXMLLoader loaderCWC = new FXMLLoader(getClass().getResource("captureWindow.fxml"));
            loaderCWC.load();
            captureWindowController = loaderCWC.getController();

            captureWindowController.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.out.println("HIDING REQUEST");
                    sc.updatePic();
                }
            });

            // TODO remove if isn't needed
            primaryStage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.out.println("SHOWN primary stage");
                }
            });
            sc.setPrimaryStage(primaryStage);
            loader.setController(sc);

            primaryStage.setTitle("Screenshot Tool");

            primaryStage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    System.out.println("minimized: " + newValue.booleanValue());
                }
            });

            // scene - сцена, stage - подмостки
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void stop() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem while unregistering the native hook.");
            System.err.println(ex.getMessage());
        }
        System.out.println("Unregistered successfully");
        System.runFinalization();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
