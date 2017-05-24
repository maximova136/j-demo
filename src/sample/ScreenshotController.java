package sample;

import JCaptureScreen.JCaptureScreen;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;


import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScreenshotController {
    @FXML
    private JFXButton clickButton;
    @FXML
    private ImageView imageView;

    public ScreenshotController() {
    }

    @FXML
    public void onAction(){
        captureFullScreen();
    }

    static private int counter = 0;
    public void captureFullScreen() {
//        try {
            WritableImage snapshot ;
//            Robot robot = new Robot();

//            Toolkit myToolkit = Toolkit.getDefaultToolkit();
//            Dimension screenSize = myToolkit.getScreenSize();

//            Rectangle screen = new Rectangle(screenSize);
//
//            BufferedImage screenFullImage = robot.createScreenCapture(screen);
//            ImageIO.write(screenFullImage, "jpg", new File("D:\\" + "screen" + Integer.toString(counter) + ".jpg"));
//            imageView.setImage(screenFullImage);

//        } catch (IOException ex) {
//            Logger.getLogger(JCaptureScreen.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
