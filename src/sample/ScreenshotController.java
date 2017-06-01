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
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.canvas.*;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import com.sun.glass.ui.Robot;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
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
        System.out.println("initialize");
        //imageView = new ImageView();//new Image("https://pp.userapi.com/c630529/v630529928/52669/3BsceoPMCHM.jpg"));
        imageView.setMouseTransparent(true);
        GraphicsContext g = canvas.getGraphicsContext2D();

        // Get screen dimensions and set the canvas accordingly
//        Dimension screenSize = getScreenSize();
//        double screenWidth = screenSize.getWidth();
//        double screenHeight = screenSize.getHeight();
        canvas.setHeight(screenHeight/1.5);
        canvas.setWidth(screenWidth/1.5);

        canvas.setStyle("-fx-background-color: rgba(0, 255, 255, 100);");  //Set the background to be translucent

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


        //===========================================
        //=======Catalogue===========================
        //===========================================

        contentGalery();

        scrollY.setMin(0);
//        scrollY.setMax(100);
        scrollY.setValue(0);

        tilePane.setPadding(new Insets(10, 10, 10, 10));
        tilePane.setVgap(15);
        tilePane.setHgap(20);
        tilePane.setStyle("-fx-background-color: 000000;");
        tilePane.setAlignment(Pos.TOP_CENTER);

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

            reloadImageView();
            primaryStage.setIconified(false);

//            reloadImageView();

        } catch (AWTException ex ){
            Logger.getLogger(ScreenshotController.class.getName()).log(Level.ALL, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadImageView() {

        System.out.println("reload image view");
        Image image = SwingFXUtils.toFXImage(screenCapture, null);
        imageView.setImage(image);
    }

    private static Stage primaryStage;
    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }


    //===========================================
    //=======Catalogue===========================
    //===========================================

    @FXML
    private ScrollBar scrollY;
    @FXML
    private TilePane tilePane;
    @FXML
    private ImageView imageView00, imageView01, imageView02, imageView03, imageView10, imageView11, imageView12,
            imageView13, imageView20, imageView21, imageView22, imageView23;
    @FXML
    private void scrollMenu(){
        scrollY.setMax(primaryStage.getHeight()/0.7);
//        scrollY.setMax();
        System.out.println(primaryStage.getHeight());
        scrollY.valueProperty().addListener(event->{
            tilePane.setTranslateY(-scrollY.getValue());
        });
    }

    private void contentGalery(){
        Image image = new Image("http://333v.ru/uploads/0a/0aa6cf3843b1cffc6c570812b8b304aa.jpg");
        imageView00.setImage(image);
        imageView01.setImage(image);
        imageView02.setImage(image);
        imageView03.setImage(image);
        imageView10.setImage(image);
        imageView11.setImage(image);
        imageView12.setImage(image);
        imageView13.setImage(image);
        imageView20.setImage(image);
        imageView21.setImage(image);
        imageView22.setImage(image);
        imageView23.setImage(image);
    }
}
