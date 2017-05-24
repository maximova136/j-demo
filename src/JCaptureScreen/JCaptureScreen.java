package JCaptureScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vmaksimo on 22.05.2017.
 */
public class JCaptureScreen {
    public void captureFullScreen() {
        try {
            Robot robot = new Robot();

            Toolkit myToolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = myToolkit.getScreenSize();

            Rectangle screen = new Rectangle(screenSize);

            BufferedImage screenFullImage = robot.createScreenCapture(screen);
            ImageIO.write(screenFullImage, "jpg", new File("screen.jpg"));

        } catch (AWTException | IOException ex) {
            Logger.getLogger(JCaptureScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
