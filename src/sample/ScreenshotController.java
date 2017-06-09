package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static sample.Main.db;

//import com.sun.javafx.geom.Rectangle;
//import com.sun.javafx.tk.Toolkit;
//import com.sun.glass.ui.Robot;

public class ScreenshotController {

    @FXML
    private JFXButton clickButton;
    @FXML
    public ImageView imageView;

//    public void initImageView(){
//        System.out.println("init image view");
//        imageView = new ImageView();
//    }
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
    private Thread thread;
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
        thread = new Thread(new ChildrenThread(this));
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

        ////       ____
        ////      /----\
        ////      |----|
        ////      |----|
        ////      |----|                  ,
        ////      |----|        this is XYN
        ////     /   |  \
        ////    |    |   |
        ////     \___|__/

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

        ArrayList<Node> children = new ArrayList<>();
        contentGallery(children);
        masonryPane.getChildren().addAll(children);

        scrollPane.setStyle("-fx-font-size: 20;");
        scrollPane.getMainHeader().setVisible(false);
//        scrollPane.getMainHeader().setStyle("-fx-background-color: #DFB951;");
        scrollPane.getCondensedHeader().setVisible(false);
//        scrollPane.getBottomBar().getChildren().add(new javafx.scene.control.Label("Title"));
//        scrollPane.getMidBar().setVisible(false);
//        scrollPane.getBottomBar().setVisible(false);

        imagePreview.setImage(null);
    }

    static private int counter = 0;
    public static int screenWidth = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static int screenHeight = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public static BufferedImage screenCapture;

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
            primaryStage.setIconified(false);
            thread.start();
            reloadImageView();
        } catch (AWTException ex ){
            Logger.getLogger(ScreenshotController.class.getName()).log(Level.ALL, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadImageView() {
        System.out.println("========================");
        System.out.println("reload image view");
        Image image = SwingFXUtils.toFXImage(screenCapture, null);
        Platform.runLater(() -> {
            imageView.setImage(image);
        });
//        imageView.setImage(image);
    }

    private static Stage primaryStage;
    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }


    //===========================================
    //=======Catalogue===========================
    //===========================================

    @FXML
    JFXMasonryPane masonryPane;
    @FXML
    ImageView imagePreview;
    @FXML
    JFXScrollPane scrollPane;

    private void contentGallery(ArrayList children){

//        Image image = new Image("http://333v.ru/uploads/0a/0aa6cf3843b1cffc6c570812b8b304aa.jpg");
//        Image image2 = new Image("http://333v.ru/uploads/0a/0aa6cf3843b1cffc6c570812b8b304aa.jpg");
//        ImageView testImageView1 = new ImageView();
//        ImageView testImageView2 = new ImageView();
//        testImageView1.setImage(image);
//        testImageView1.setFitHeight(200);
//        testImageView1.setFitWidth(200);
//        testImageView2.setImage(image2);
//        testImageView2.setFitHeight(200);
//        testImageView2.setFitWidth(200);

        Random r = new Random();
        for (int i = 0; i < 30; i++) {
            javafx.scene.control.Label label = new javafx.scene.control.Label();
            label.setPrefSize(300, 200);
            javafx.scene.control.Label label2 = new javafx.scene.control.Label();
            label2.setPrefSize(300, 200);
//            label.setGraphic(testImageView1);
//            label2.setGraphic(testImageView2);
            label.setStyle("-fx-background-color:rgb(" + r.nextInt(200) + ","+ r.nextInt(200) + ","+ r.nextInt(200) + ");");
            children.add(label);
//        children.add(label2);
        }
    }

    private void contentImagePreview(){

    }

    @FXML
    public void writeToDB(){
        Scanner in = new Scanner(System.in);
        System.out.println("введи тестовые данные");
        String urlid = in.nextLine();
        String url = in.nextLine();
        db.writeDB(urlid, url);
        db.showDB();
    }

    @FXML
    public void removeDB(){
        Scanner in = new Scanner(System.in);
        System.out.println("введи тестовые данные");
        int number = in.nextInt();
        db.removeDB(number);
    }
}
