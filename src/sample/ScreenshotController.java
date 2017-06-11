package sample;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static sample.Main.db;


public class ScreenshotController {
    private static CloudHost cloudHost = new CloudHost();


    @FXML
    private JFXButton clickButton;
    @FXML
    private ImageView imageView;
    public void initImageView(){
        System.out.println("init image view");
//        imageView = new ImageView();
    }
    @FXML
    private JFXToggleButton chooseButton;
    @FXML
    private Canvas some_canvas;

    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane anchorPaneCanvas;
    @FXML
    private ScrollPane scrollPaneCanvas;

    public void updatePic(){
//        if (canvas != null) {
        Platform.runLater(()->{
//            canvas.getGraphicsContext2D().drawImage(new Image(cloudHost.getImageUrl("v1nspq7zmp95beqvajtb")),0,0);

        });
//        }
    }

    @FXML
    public void onMouseClicked(){
        System.out.println("on mouse clicked");
//        captureFullScreen(chooseButton.isSelected());
        Main.captureWindowController.prepareForCapture();

//        reloadImageView();
    }
    private Thread thread;
    @FXML
    public void onMouseClickedCanvas(){
        System.out.println("on mouse clicked some_canvas");
        GraphicsContext g = some_canvas.getGraphicsContext2D();
        Image backImage = new Image("http://animal-store.ru/img/2015/050219/2042896");
        double widthImage  = backImage.getWidth();
        double heightImage = backImage.getHeight();
        g.drawImage(backImage, 0, 0, widthImage, heightImage); // arg: X, Y, Width, Height
    }

    public void initialize() {
        thread = new Thread(new ChildrenThread(this));
        System.out.println("initialize");
        imageView.setMouseTransparent(true);
        GraphicsContext g = some_canvas.getGraphicsContext2D();

        canvas.setHeight(500);
        canvas.setWidth(200);
        // Get screen dimensions and set the canvas accordingly
//        Dimension screenSize = getScreenSize();
//        double screenWidth = screenSize.getWidth();
//        double screenHeight = screenSize.getHeight();
        some_canvas.setHeight(screenHeight/1.5);
        some_canvas.setWidth(screenWidth/1.5);

        ////       ____
        ////      /----\
        ////      |----|
        ////      |----|
        ////      |----|                  ,
        ////      |----|        this is XYN
        ////     /   |  \
        ////    |    |   |
        ////     \___|__/

//        some_canvas.setOnMouseDragged(e -> {
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
//        some_canvas.setOnMouseClicked(e -> {
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

        imagePreview.setImage(null);

        spinner.setVisible(false);
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
            java.awt.Robot robot = new java.awt.Robot();
            robot.delay(500);
            screenCapture = robot.createScreenCapture(new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(screenCapture, "png", new File("screen_" + Integer.toString(counter) + ".png"));
            counter++;
            primaryStage.setIconified(false);
            thread.start();
            reloadImageView();
        } catch (java.awt.AWTException ex ){
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

    }

    private static Stage primaryStage;

    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }
    public static Stage getPrimaryStage(){
        return primaryStage;
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



    @FXML
    JFXSpinner spinner;

    @FXML
    JFXButton uploadButton;
    static boolean isUploadEnabled = false;

    public void onUploadClicked() {
        if (imageView.getImage() != null) { // TODO change on canvas
            if (isUploadEnabled) { // Cancel uploading ???
                System.out.println("on mouse clicked upload cancel");

                uploadButton.setCancelButton(false);
                uploadButton.setText("Upload");
                spinner.setVisible(false);
                isUploadEnabled = false;
            } else { // Uploading
                System.out.println("on mouse clicked upload");
                uploadButton.setCancelButton(true);
                uploadButton.setText("Cancel");
                isUploadEnabled = true;
                spinner.setVisible(true);

                // TODO add uploading from canvas
                // TODO in separate thread
                File fileImg = new File("screen_" + Integer.toString(counter) + ".png");
                BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
                try {
                    ImageIO.write(bImage, "png", fileImg);
                    cloudHost.upload(fileImg);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    uploadButton.setCancelButton(false);
                    uploadButton.setText("Upload");
                    spinner.setVisible(false);
                    isUploadEnabled = false;
                }

                System.out.println(cloudHost.getLastImageUrl());
                System.out.println(cloudHost.getLastPublicId());
                System.out.println(cloudHost.getPreviewImageUrl(cloudHost.getLastPublicId()));
            }
        } else {
            System.out.println("oops, canvas is empty");
            return;
        }
    }

/////////////// DB ////////////////
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
