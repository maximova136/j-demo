package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.geom.Dimension2D;
//import com.sun.javafx.geom.Rectangle;
//import com.sun.javafx.tk.Toolkit;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
//import com.sun.glass.ui.Robot;
import javafx.stage.Stage;
import sun.security.util.SecurityConstants;

import javax.imageio.ImageIO;
import javax.tools.Tool;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sample.Main;

public class ScreenshotController {
    @FXML
    private JFXButton clickButton;
    @FXML
    private ImageView imageView;
    @FXML
    private JFXToggleButton chooseButton;

    public ScreenshotController() {
    }

    @FXML
    public void onAction(){
        captureFullScreen();
//        Node source = (Node) actionEvent.getSource();
//        Window theStage = source.getScene().getWindow().
    }

    static private int counter = 0;
    public static int screenWidth = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static int screenHeight = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public void captureFullScreen() {
//        try {
//        WritableImage snapshot = Stage.getScene().snapshot(null);
//        java.awt.Robot robot = new java.awt.Robot();
//            Robot robot = Application.GetApplication().createRobot();
//            Pixels picture = robot.getScreenCapture(0, 0, screenWidth, screenHeight);

//            Rectangle screen = new Rectangle(screenSize);
//
//            BufferedImage screenFullImage = robot.createScreenCapture(screen);
//            ImageIO.write(screenFullImage, "jpg", new File("D:\\" + "screen" + Integer.toString(counter) + ".jpg"));
//            imageView.setImage(screenFullImage);

//        } catch (IOException ex) {
//            Logger.getLogger(JCaptureScreen.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        Toolkit tk;
        try {
            if (chooseButton.isSelected())
                primaryStage.setIconified(true);
            Robot robot = new Robot();
            robot.delay(500);
            BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(screenCapture, "png", new File("screen_" + Integer.toString(counter) + ".png"));
            counter++;
            primaryStage.setIconified(false);
            Image image = SwingFXUtils.toFXImage(screenCapture, null);
            imageView.setImage(image);

        } catch (AWTException ex ){
            Logger.getLogger(ScreenshotController.class.getName()).log(Level.ALL, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private Main mainApp;
//    public void setMainApp(Main mainApp){
//        this.mainApp = mainApp;
//    }
    private static Stage primaryStage;
    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }
}
