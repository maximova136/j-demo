package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
//import com.sun.javafx.geom.Rectangle;
//import com.sun.javafx.tk.Toolkit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import com.sun.glass.ui.Robot;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.canvas.Canvas;

public class ScreenshotController {

    @FXML
    private JFXButton clickButton;
    @FXML
    private ImageView imageView;
    public void initImageView(){
        System.out.println("init image view");

        imageView = new ImageView();
    }
    @FXML
    private JFXToggleButton chooseButton;
    @FXML
    private Canvas canvas;
    @FXML
    public void onMouseClicked(){
        System.out.println("on mouse clicked");
        captureFullScreen(chooseButton.isSelected()); //chooseButton.isSelected());
//        reloadImageView();
    }

    @FXML
    public void onMouseClickedCanvas(){
        System.out.println("on mouse clicked Canvas");
        GraphicsContext g = canvas.getGraphicsContext2D();
        Image backImage = new Image("http://animal-store.ru/img/2015/050219/2042896");
        double widthImage  = backImage.getWidth();
        double heightImage = backImage.getHeight();
        g.drawImage(backImage, 0, 0, widthImage, heightImage); // arg: X, Y, Width, Height
    }

    public void initialize() {
        System.out.println("initialize controller");
//        imageView = new ImageView();//new Image("https://pp.userapi.com/c630529/v630529928/52669/3BsceoPMCHM.jpg"));
        imageView.setMouseTransparent(true);
//        canvas = new Canvas();
//        GraphicsContext g = canvas.getGraphicsContext2D();

        // Get screen dimensions and set the canvas accordingly
//        Dimension screenSize = getScreenSize();
//        double screenWidth = screenSize.getWidth();
//        double screenHeight = screenSize.getHeight();
        canvas.setHeight(screenHeight/1.5);
        canvas.setWidth(screenWidth/1.5);

//        canvas.setStyle("-fx-background-color: rgba(0, 255, 255, 100);");  //Set the background to be translucent

//        canvas.setOnMouseDragged(e -> {
////            double size = Double.parseDouble(brushSize.getText());
////            double x = e.getX() - size / 2;
////            double y = e.getY() - size / 2;
////
////            if (eraser.isSelected()) {
////                g.clearRect(x, y, size, size);
////            } else {
////
////                g.setFill(colorPicker.getValue());
////                if (isBrushBrush) {
////                    g.fillOval(x, y, size, size);
////                } else {
////                    g.fillRect(x, y, size, size);
////                }
////            }
//        });
//
//        canvas.setOnMouseClicked(e -> {
////            double size = Double.parseDouble(brushSize.getText());
////            double x = e.getX() - size / 2;
////            double y = e.getY() - size / 2;
////
////            if (eraser.isSelected()) {
////                g.clearRect(x, y, size, size);
////            } else {
////                g.setFill(colorPicker.getValue());
////                if(isBrushBrush) {
////                    g.fillOval(x, y, size, size);
////                } else {
////                    g.fillRect(x, y, size, size);
////                }
////            }
//        });


    }

    static private int counter = 0;
    public static int screenWidth = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static int screenHeight = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private static BufferedImage screenCapture;

    public void captureFullScreen(boolean isHideEnabled) {
        try {
            System.out.println("capture full screen");

            if (isHideEnabled) {
                primaryStage.setIconified(true);
            }
            Robot robot = new Robot();
            robot.delay(500);
            screenCapture = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(screenCapture, "png", new File("screen_" + Integer.toString(counter) + ".png"));
            counter++;

//            reloadImageView();
//            Image image = SwingFXUtils.toFXImage(screenCapture, null);
            //            if (canvas == null) {
//                System.out.println("canvas null");
//                canvas = new Canvas(screenWidth/1.5, screenHeight/1.5);
//            }
//            GraphicsContext g = canvas.getGraphicsContext2D();
////            Image backImage = new Image("http://animal-store.ru/img/2015/050219/2042896");
////            double widthImage  = backImage.getWidth();
////            double heightImage = backImage.getHeight();
//            Image image = SwingFXUtils.toFXImage(screenCapture, null);
//            image = SwingFXUtils.toFXImage(screenCapture, null);
//            if (imageView.getImage() != image) {
//                imageView.setImage(image);
//
//            }
reloadImageView();
//            g.drawImage(backImage, 0, 0, widthImage, heightImage); // arg: X, Y, Width, Height
            primaryStage.setIconified(false);
//            g.drawImage(image, 0,0);
            reloadImageView();

        } catch (AWTException ex ){
            Logger.getLogger(ScreenshotController.class.getName()).log(Level.ALL, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadImageView() {

        System.out.println("reload image view");
        Image image = SwingFXUtils.toFXImage(screenCapture, null);
        if (imageView.getImage() != image) {
            System.out.println("here ");
            imageView.setImage(image);
        }
        System.out.println("out reload");
//        imageView.setImage(image);
   }

    private static Stage primaryStage;
    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

}
