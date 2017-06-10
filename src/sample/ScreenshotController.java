package sample;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private Canvas canvas;
    @FXML
    public void onMouseClicked(){
        System.out.println("on mouse clicked");
        captureFullScreen(chooseButton.isSelected());
//        reloadImageView();
    }
    private Thread thread;
    private ArrayList<Node> children;

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

        ;
        //===========================================
        //=======Catalogue===========================
        //===========================================

        children = new ArrayList<>();
        contentGallery();
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

    private void contentGallery(){
        children.clear();
        System.out.println(children);
        int counterDBSize = db.getSizeDB();
        ArrayList<String> list = new ArrayList<>(db.showDB());
        for (int i = 0; i < counterDBSize;i++) {
            String public_id = cloudHost.getPreviewImageUrl(list.get(i)); //прямая ссылка на миниатюру

            Image image = new Image(public_id);
            ImageView labelImageView = new ImageView();
            labelImageView.setImage(image);
            Label label = new Label();
            label.setPrefSize(250, 200);
            label.setGraphic(labelImageView);
            children.add(label);
        }
        System.out.println(children);
    }

    private void reloadContentGallery(){
        System.out.println("reload content Gallery");
        Platform.runLater(() ->{
            masonryPane.getChildren().addAll(children);
        });

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
        db.writeDB(urlid);
        db.showDB();
    }

    @FXML
    public void removeDB(){
        Scanner in = new Scanner(System.in);
        System.out.println("введи тестовые данные");
        int number = in.nextInt();
        db.removeDB(number);//изменить на удаление через urlid
        contentGallery();
//        reloadContentGallery();
    }
}