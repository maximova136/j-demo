package sample;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.controlsfx.control.Notifications;

import javax.imageio.ImageIO;

/**
 * Created by vmaksimo on 10.06.2017.
 */
public class CaptureWindowController extends Stage {
    // Service
    final CaptureService captureService = new CaptureService();
    //    CaptureService captureService = new CaptureService();
    public Thread capturingThread;
    @FXML
    private StackPane stackPane;
    @FXML
    private Canvas mainCanvas;
    private GraphicsContext gc;
    private int xFrom = 0;
    private int yFrom = 0;
    private int xNow = 0;
    private int yNow = 0;
    private int UPPER_LEFT_X = 0;
    private int UPPER_LEFT_Y = 0;
    private int frameWidth;
    private int frameHeight;
    private int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    private int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();

    public CaptureWindowController() {
        System.out.println("CaptureWindow constructor");

        setX(0);
        setY(0);
        initStyle(StageStyle.TRANSPARENT);
        setAlwaysOnTop(true);
    }

    @FXML
    private void initialize() {
        System.out.println("CaptureWindow initialized");
        // Scene
        Scene scene = new Scene(stackPane, screenWidth, screenHeight, Color.TRANSPARENT);
        setScene(scene);
        addKeyHandlers();

        mainCanvas.setWidth(screenWidth);
        mainCanvas.setHeight(screenHeight);

        mainCanvas.setOnMousePressed(m -> {
            xFrom = (int) m.getSceneX();
            yFrom = (int) m.getScreenY();
        });

        mainCanvas.setOnMouseDragged(m -> {
            xNow = (int) m.getScreenX();
            yNow = (int) m.getScreenY();
            repaintCanvas();
        });

        gc = mainCanvas.getGraphicsContext2D();
    }

    // KeyHandlers for the scene
    private void addKeyHandlers() {
        getScene().setOnKeyReleased(key -> {
            if (key.getCode() == KeyCode.A && key.isControlDown())
                selectWholeScreen();

            if (key.getCode() == KeyCode.BACK_SPACE || key.getCode() == KeyCode.Q) {
                if (capturingThread != null) {
                    System.out.println("capturing thread not null, interrupting");
                    capturingThread.interrupt();
                    System.out.println(capturingThread.getState().toString());
                } else {
                    System.out.println("capturing thread null");
                }
//                System.out.println(captureService.getState());
//                captureService.reset();
                Main.stage.show();
                close();

            } else if (key.getCode() == KeyCode.ENTER || key.getCode() == KeyCode.SPACE) {
                createImage();
            }
        });
    }


    public void createImage() {
        System.out.println("create Image");
        // return if it's alive
        if ((capturingThread != null && capturingThread.isAlive()) || captureService.isRunning()) {
            System.out.println("return");
            return;
        }

        capturingThread = new Thread(() -> {
            boolean interrupted = false;
            // !interrupted?
            if (!Thread.interrupted()) {
                Platform.runLater(() -> {
                            // Clear the canvas
                            gc.clearRect(0, 0, getWidth(), getHeight());

                            BufferedImage image;
                            int[] rect = getRectangleBounds();
                            try {
                                image = new Robot().createScreenCapture(new Rectangle(rect[0], rect[1], rect[2], rect[3]));
                            } catch (AWTException ex) {
                                Logger.getLogger(getClass().getName()).log(Level.INFO, null, ex);
                                return;
                            } catch (IllegalArgumentException ex2) {
                                Logger.getLogger(getClass().getName()).log(Level.INFO, null, ex2);
                                return;
                            }
                            System.out.println("Starting Service");
                            captureService.startService(image);
                        }
                );
            } // !interrupted?
        });

        capturingThread.setDaemon(true);
        capturingThread.start();
    }


    protected void repaintCanvas() {
        getScene().setCursor(Cursor.CROSSHAIR);
        // smart calculation of where the mouse has been dragged
        frameWidth = (xNow > xFrom)
                ? xNow - xFrom  // RIGHT
                : xFrom - xNow; // LEFT

        frameHeight = (yNow > yFrom)
                ? yNow - yFrom  // DOWN
                : yFrom - yNow; // UP

        UPPER_LEFT_X = // --------> UPPER_LEFT_X
                (xNow > xFrom)
                        ? xFrom // RIGHT
                        : xNow; // LEFT

        UPPER_LEFT_Y = // --------> UPPER_LEFT_Y
                (yNow > yFrom)
                        ? yFrom // DOWN
                        : yNow; // UP

        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.rgb(20, 20, 20, 0.3)); // цвет для заливки. около светло-серого, прозрачный
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setFont(Font.font("", FontWeight.BOLD, 14));

        gc.setStroke(Color.LIGHTPINK); // цвет пунктирчика
        gc.setLineDashes(0); // который не пунктирчик
        gc.setLineWidth(1);

        // нутро прямоугольника
        gc.clearRect(UPPER_LEFT_X, UPPER_LEFT_Y, frameWidth, frameHeight);
        // 1 = ширина линии обводки
        gc.strokeRect(UPPER_LEFT_X - 1, UPPER_LEFT_Y - 1, frameWidth + 2, frameHeight + 2);

        gc.setFill(Color.rgb(0, 0, 0, 0.5)); // полупрозрачный черный

        // действия для таблички с надписью
        int sizeLabelWidth = 70;
        int sizeLabelHeight = 20;
        gc.fillRect(UPPER_LEFT_X, UPPER_LEFT_Y < sizeLabelHeight ? UPPER_LEFT_Y : UPPER_LEFT_Y - sizeLabelHeight, sizeLabelWidth, sizeLabelHeight);
        gc.strokeRect(UPPER_LEFT_X - 1, UPPER_LEFT_Y - 2 < sizeLabelHeight ? UPPER_LEFT_Y - 2 : UPPER_LEFT_Y - sizeLabelHeight - 2, sizeLabelWidth + 2, sizeLabelHeight + 2);

        gc.setFill(Color.WHITE);
        gc.fillText(frameWidth + "," + frameHeight, UPPER_LEFT_X + 5, UPPER_LEFT_Y < sizeLabelHeight ? UPPER_LEFT_Y + sizeLabelHeight / 2 + sizeLabelHeight / 4 : UPPER_LEFT_Y - sizeLabelHeight / 2 + sizeLabelHeight / 4);
    }


    private void selectWholeScreen() {
        xFrom = 0;
        yFrom = 0;
        xNow = (int) getWidth();
        yNow = (int) getHeight();
        repaintCanvas();
    }

    static boolean isHideEnabled = true;
    // prepare window for user
    public void prepareForCapture(boolean hideEnable) {
        xFrom = 0;
        yFrom = 0;
        xNow = 0;
        yNow = 0;
        UPPER_LEFT_X = 0;
        UPPER_LEFT_Y = 0;
        repaintCanvas();
        show();
        isHideEnabled = hideEnable;
//        ScreenshotController.getPrimaryStage().close();
        if (isHideEnabled) {
            Main.stage.hide();
        }
    }


    public int[] getRectangleBounds() {
        // array which contains UPPER_LEFT point of the rectangle and its W&H
        return new int[]{UPPER_LEFT_X, UPPER_LEFT_Y, frameWidth, frameHeight};
    }

    // service to capture the drawn image
    public class CaptureService extends Service<Boolean> {
        String filePath;
        BufferedImage image;

        public CaptureService() {
            setOnSucceeded(s -> done());
//            setOnCancelled(c -> done());
//            setOnFailed(f -> done());
        }

        public void startService(BufferedImage img) {
            System.out.println("started service");

            if (!isRunning()) {
                System.out.println("write image");
                this.image = img;

                File file = new File("screenshot.png");
                filePath = file.getAbsolutePath();
                System.out.println("reset service");
                reset();
                System.out.println("start service");
                start();
            }
        }

        private void done() {
            System.out.println("done service: " + getValue());
            close();
            Main.stage.show();

            if (getValue())
                System.out.println("Successfull Capturing, image is saved at:" + filePath);
            else {
                System.out.println("Error, failed to capture screen");
                Notifications.create().title("Error").text("Failed to capture the Screen!").showError();
            }
        }

        @Override
        protected void cancelled() {
            super.cancelled();
            System.out.println("cancelled service");
            Main.stage.show();
            close();
            System.out.println("Error, failed to capture screen");
            Notifications.create().title("Error").text("Failed to capture the Screen!").showError();
        }

        @Override
        protected void failed() {
            super.failed();
            System.out.println("failed service");
            Main.stage.show();
            close();
            System.out.println("Error, failed to capture screen");
            Notifications.create().title("Error").text("Failed to capture the Screen!").showError();
//            reset();
        }

        @Override
        protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    System.out.println("try to write to file");
                    boolean write = false;
                    try {
                        write = ImageIO.write(image, "png", new File(filePath));
                    } catch (IOException ex) {
                        return write;
                    }
                    return write;
                }
            };
        }
    }
}
