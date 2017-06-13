package sample;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.Notifications;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


import static sample.Main.db;


public class ScreenshotController {
    private static CloudHost cloudHost = new CloudHost();

    @FXML
    private JFXButton clickButton;
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
            imageOld = image;
            gc.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
            canvasWidth = (int) image.getWidth();
            canvasHeight = (int) image.getHeight();
//            canvas.widthProperty(). // doesn't work
        });
        primaryStage.setIconified(false);
    }

    @FXML
    public void onMouseClicked() {
        System.out.println("on mouse clicked");
        Main.captureWindowController.prepareForCapture(chooseButton.isSelected());
    }

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

        System.out.println("initialize");

        canvas.setHeight(screenHeight);
        canvas.setWidth(screenWidth);

        // TODO change if image without screenshot should not be saved and uploaded
        canvasHeight = screenHeight;
        canvasWidth = screenWidth;

        gc = canvas.getGraphicsContext2D();
        final Pen penTool = new Pen(gc);
        final Eraser eraserTool = new Eraser(gc);
        penTool.setColor(colorPicker.getValue());
        gc.setLineJoin(StrokeLineJoin.ROUND);
        gc.setLineCap(StrokeLineCap.ROUND);


        scrollPaneCanvas.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
//        scrollPaneCanvas.setPannable(true);
        scrollPaneCanvas.setContent(canvas);
//        scrollPaneCanvas.setContent(null);

        //==== editor =====
        penLabel = new Label("pen");
        penLabel.setGraphic(new ImageView(new Image("file:brush18dp.png")));
        penLabel.setId("pen");

        eraserLabel = new Label("eraser");
        eraserLabel.setGraphic(new ImageView(new Image("file:eraser18dp.png")));
        eraserLabel.setId("eraser");
        chooseToolBox.getItems().addAll(penLabel, eraserLabel);

        chooseToolBox.valueProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {
                System.out.println("new value:" + newValue.getId());
                if (newValue.getId().equalsIgnoreCase("pen")) {
                    currentTool = penTool;
                    currentTool.setColor(colorPicker.getValue());
                } else if (newValue.getId().equalsIgnoreCase("eraser")) {
                    eraserTool.setImage(imageOld);
                    currentTool = eraserTool;
                }
                currentTool.setSize(sizeSlider.getValue());
            }
        });

        Platform.runLater(() -> {
            chooseToolBox.setValue(chooseToolBox.getItems().get(0));
        });

        gcCircle = sizeCircleCanvas.getGraphicsContext2D();
        gcCircle.setFill(Color.WHITE);
        // start value is 10, white color
        gcCircle.fillOval(10, 10, 10, 10); // TODO it working

        sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                gcCircle.clearRect(0, 0, 30, 30);
                gcCircle.setFill(colorPicker.getValue());
                gcCircle.fillOval(15 - newValue.doubleValue() / 2, 15 - newValue.doubleValue() / 2, newValue.doubleValue(), newValue.doubleValue());

                currentTool.setSize(newValue.doubleValue());
            }
        });

        colorPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                gcCircle.clearRect(0, 0, 30, 30);
                gcCircle.setFill(newValue);
                gcCircle.fillOval(15 - sizeSlider.getValue() / 2, 15 - sizeSlider.getValue() / 2, sizeSlider.getValue(), sizeSlider.getValue());

                currentTool.setColor(newValue);
            }
        });

        Platform.runLater(() -> {
            colorPicker.setValue(Color.DEEPPINK);
        });

        canvas.setOnMouseDragged(e -> {
            if (currentTool != null) {
                currentTool.onDrag(e);
            }
        });
        canvas.setOnMousePressed(e -> {
            if (currentTool != null) {
                currentTool.onPress(e);
            }
        });
        canvas.setOnMouseReleased(e -> {
            if (currentTool != null) {
                currentTool.onRelease(e);
            }
        });

        ////       ____
        ////      /----\
        ////      |----|
        ////      |----|
        ////      |----|                  ,
        ////      |----|        this is XYN
        ////     /   |  \
        ////    |    |   |
        ////     \___|__/
        ;
        //===========================================
        //=======Catalogue===========================
        //===========================================

        children = new ArrayList<>();
        contentGallery();
        reloadContentGallery();

//        masonryPane.setStyle("-fx-border-color: black");

        scrollPane.setStyle("-fx-font-size: 20;");
        Image image = new Image("file:background.jpg");
        BackgroundImage myBI = new BackgroundImage(image,
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        scrollPane.getMainHeader().setBackground(new Background(myBI));
        scrollPane.getCondensedHeader().setBackground(new Background(myBI));

        previewImageUrl = null;

        buttonDel.setGraphic(new ImageView(new Image("file:delete.png")));
        buttonCopy.setGraphic(new ImageView(new Image("file:content_copy.png")));
        buttonUrl.setGraphic(new ImageView(new Image("file:http.png")));
        uploadButton.setGraphic(new ImageView(new Image("file:cloud_upload.png")));
        clickButton.setGraphic(new ImageView(new Image("file:photo_camera.png")));
        copyButton.setGraphic(new ImageView(new Image("file:content_copy.png", 30.0, 30.0, true, false)));

        buttonDel.setPadding(new Insets(50, 30, 70, 30));
        buttonCopy.setPadding(new Insets(50, 30, 70, 30));
        buttonUrl.setPadding(new Insets(50, 30, 70, 30));

        imagePreview.setImage(null);
        spinner.setVisible(false);

        vBox.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.3)); //чтобы не ползал разделитель экранов
    }

    private static Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    JFXButton copyButton;

    @FXML
    public void onCopyClicked() {
        WritableImage wim = new WritableImage(canvasWidth, canvasHeight);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        canvas.snapshot(params, wim);
        copyImageToClipboard(wim);
    }

    @FXML
    JFXSpinner spinner;
    @FXML
    JFXButton uploadButton;

    static Label penLabel;
    static Label eraserLabel;

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
                SnapshotParameters params = new SnapshotParameters();
                params.setFill(Color.TRANSPARENT);
                canvas.snapshot(params, wim);
//                canvas.snapshot(null, wim);
                File file = new File("screenshot.png");
                file.createNewFile();
                ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Platform.runLater(() -> {
                                setOnUploading(true);
                            });
                            cloudHost.upload(file);
                            System.out.println(cloudHost.getLastImageUrl());

                            copyTextToClipboard(cloudHost.getLastImageUrl());

                            writeToDB(cloudHost.getLastPublicId());
                            Platform.runLater(() -> {
                                setOnUploading(false);
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException eia) {
                            eia.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
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

    //=====================
    //======editor========
    //====================
    @FXML
    JFXColorPicker colorPicker;
    @FXML
    JFXSlider sizeSlider;
    @FXML
    Canvas sizeCircleCanvas;
    GraphicsContext gcCircle;
    @FXML
    JFXComboBox<Label> chooseToolBox;
    static Image imageOld;
    private Tool currentTool = null;

    public void copyTextToClipboard(String text) {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Platform.runLater(() -> {
            Main.clipboard.setContent(content);
        });
        // TODO notification about copied link
    }

    public void copyImageToClipboard(WritableImage image) {
        ClipboardContent content = new ClipboardContent();
        content.putImage(image);
        Platform.runLater(() -> {
            Main.clipboard.setContent(content);
        });
        // TODO notification about copied link
    }

    public void copyImageToClipboard(Image image) {
        PixelReader pixelReader = image.getPixelReader();
        WritableImage wi = new WritableImage(pixelReader, (int) image.getWidth(), (int) image.getHeight());
        ClipboardContent content = new ClipboardContent();
        content.putImage(wi);
        Platform.runLater(() -> {
            Main.clipboard.setContent(content);
        });
        // TODO notification about copied link
    }

    public void copyImageToClipboard(String url) {
        copyImageToClipboard(new Image(url));
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
    JFXButton buttonDel, buttonCopy, buttonUrl;

    private void contentGallery() {
        children.clear();
//        System.out.println(children);
        int counterDBSize = db.getSizeDB();
        ArrayList<String> list = new ArrayList<>(db.showDB());
        new Thread() {
            @Override
            public void run() {
                try {
                    Platform.runLater(() -> {
//                        setOnUploading(true);
                    });
                    for (int i = 0; i < counterDBSize; i++) {
                        String previewImgUrl = cloudHost.getPreviewImageUrl(list.get(i)); //прямая ссылка на миниатюру для галереи
                        String previewUrlToImageView = cloudHost.getPreviewMiddleImageUrl(list.get(i)); //ссылка для превью миниатюры
                        Image image = new Image(previewImgUrl);
                        ImageView labelImageView = new ImageView();
                        labelImageView.setImage(image);
                        Label label = new Label();
                        label.setPrefSize(250, 200);
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

                    Platform.runLater(() -> {
//                        setOnUploading(false);
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    private void reloadContentGallery() {
//        System.out.println("reload content Gallery");
        Platform.runLater(() -> {
            masonryPane.getChildren().clear();
            masonryPane.getChildren().addAll(children);
        });
    }

    String previewImageUrl;

    private void contentImagePreview(String previewImgUrl) {
        Image image = new Image(previewImgUrl);
        Platform.runLater(() -> {
            imagePreview.setImage(image);
            previewImageUrl = previewImgUrl;
        });
    }
    /////////////// DB ////////////////

    @FXML
    public void writeToDB(String public_id) {
        db.writeDB(public_id);
        db.showDB();
        contentGallery();
        reloadContentGallery();
    }

    @FXML
    public void removeImage() {
        db.removeDB(CloudHost.getPublicID(previewImageUrl));
        cloudHost.deleteImage(CloudHost.getPublicID(previewImageUrl)); //удаление с сервера
        contentGallery();
        reloadContentGallery();
        Platform.runLater(() -> {
            imagePreview.setImage(null);
            previewImageUrl = null;
        });
    }

    @FXML
    public void buttonCopy() {
        copyImageToClipboard(new Image(cloudHost.getImageUrl((CloudHost.getPublicID(previewImageUrl)))));
    }

    @FXML
    public void buttonURL() {
        copyTextToClipboard(cloudHost.getImageUrl((CloudHost.getPublicID(previewImageUrl))));
    }

}