package sample;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import javax.imageio.ImageIO;
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
    @FXML
    private JFXToggleButton chooseButton;
    private static GraphicsContext gc;

    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane anchorPaneCanvas;
    @FXML
    private ScrollPane scrollPaneCanvas;

    public void updatePic() {
        Platform.runLater(() -> {
            gc.clearRect(0, 0, canvasWidth, canvasHeight);
            Image image = new Image("file:screenshot.png");
            gc.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
            canvasWidth = (int) image.getWidth();
            canvasHeight = (int) image.getHeight();
        });
        primaryStage.setIconified(false);
    }

    @FXML
    public void onMouseClicked() {
        System.out.println("on mouse clicked");
        Main.captureWindowController.prepareForCapture();
    }

    private Thread thread;
    private ArrayList<Node> children;


    public static int screenWidth = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public static int screenHeight = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public static int canvasWidth;
    public static int canvasHeight;

    public void setCanvasSize(int width, int height) {
        canvasWidth = width;
        canvasHeight = height;
        Platform.runLater(() -> {
            scrollPaneCanvas.setContent(canvas);
        });
    }

    public void initialize() {
        thread = new Thread(new ChildrenThread(this));
        System.out.println("initialize");
        imageView.setMouseTransparent(true);

        canvas.setHeight(screenHeight);
        canvas.setWidth(screenWidth);

        canvasHeight = 0;
        canvasWidth = 0;

        gc = canvas.getGraphicsContext2D();

        scrollPaneCanvas.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPaneCanvas.setPannable(true);
        scrollPaneCanvas.setContent(canvas);
//        scrollPaneCanvas.setContent(null);

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

        ;
        //===========================================
        //=======Catalogue===========================
        //===========================================

        children = new ArrayList<>();
        contentGallery();
        reloadContentGallery();

//        masonryPane.setStyle("-fx-border-color: black");


        scrollPane.setStyle("-fx-font-size: 20;");
        scrollPane.getMainHeader().setVisible(false);
        scrollPane.getCondensedHeader().setVisible(false);

        previewImageUrl = null;

        imagePreview.setImage(null);
        spinner.setVisible(false);

        vBox.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.3)); //чтобы не ползал разделитель экранов
    }

    static private int counter = 0;

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
        } catch (java.awt.AWTException ex) {
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

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    JFXSpinner spinner;

    @FXML
    JFXButton uploadButton;

    // true - busy, false - clickable
    private void setOnUploading(boolean isBusy) {
        uploadButton.setDisable(isBusy);
        spinner.setVisible(isBusy);
    }

    public void onUploadClicked() {
        if (canvas.getScene() != null) {
            System.out.println("on mouse clicked upload");
            try {
                WritableImage wim = new WritableImage(canvasWidth, canvasHeight);
                canvas.snapshot(null, wim);
                File file = new File("screenshot.png");
                ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                // TODO in separate thread
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Platform.runLater(() -> {
                                setOnUploading(true);
                            });
                            cloudHost.upload(file);
                            System.out.println(cloudHost.getLastImageUrl());
                            System.out.println(cloudHost.getLastPublicId());
                            System.out.println(cloudHost.getPreviewImageUrl(cloudHost.getLastPublicId()));
                            writeToDB(cloudHost.getLastPublicId());
                            Platform.runLater(() -> {
                                setOnUploading(false);
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalArgumentException iae) {
                // TODO change Notifications to JFXSnackbar
                // Image dimensions must be positive (w,h > 0)
                System.out.println("No image to upload");
                Notifications.create().title("Notification").text("No image to upload").showError();
            }
        } else {
            System.out.println("oops, canvas is empty");
            return;
        }
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
    @FXML
    SplitPane splitPane;
    @FXML
    VBox vBox;
    @FXML
    SVGPath svg1,svg2,svg3;

    private void contentGallery(){
        children.clear();
//        System.out.println(children);
        int counterDBSize = db.getSizeDB();
        ArrayList<String> list = new ArrayList<>(db.showDB());
        for (int i = 0; i < counterDBSize;i++) {
            String previewImgUrl = cloudHost.getPreviewImageUrl(list.get(i)); //прямая ссылка на миниатюру для галереи
            String previewUrlToImageView = cloudHost.getPreviewMiddleImageUrl(list.get(i)); //ссылка для превью миниатюры
            Image image = new Image(previewImgUrl);
            ImageView labelImageView = new ImageView();
            labelImageView.setImage(image);
            Label label = new Label();
//            label.maxHeight(200);
//            label.maxWidth(250);
//            label.setStyle("-fx-background-color: black");
////            label.setPrefSize(250, 200);
//            label.setStyle("-fx-border-color: black");
            label.setGraphic(labelImageView);
            label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    contentImagePreview(previewUrlToImageView);
                }
            });

            children.add(label);
        }
        System.out.println(children);
    }

    private void reloadContentGallery(){
//        System.out.println("reload content Gallery");
        Platform.runLater(() ->{
            masonryPane.getChildren().clear();
            masonryPane.getChildren().addAll(children);
        });
    }

    String previewImageUrl;

    private void contentImagePreview(String previewImgUrl){
        Image image = new Image(previewImgUrl);
        Platform.runLater(() -> {
            imagePreview.setImage(image);
            previewImageUrl = previewImgUrl;
        });
    }
    /////////////// DB ////////////////

    @FXML
    public void writeToDB(String public_id){
        db.writeDB(public_id);
        db.showDB();
        contentGallery();
        reloadContentGallery();
    }

    @FXML
    public void removeImage(){
        db.removeDB(CloudHost.getPublicID(previewImageUrl));
        cloudHost.deleteImage(CloudHost.getPublicID(previewImageUrl)); //удаление с сервера
        contentGallery();
        reloadContentGallery();
        Platform.runLater(() -> {
            imagePreview.setImage(null);
            previewImageUrl = null;
        });
    }
}