package sample;

import javafx.application.Platform;
import static java.lang.Thread.sleep;

/**
 * Created by Vicanz on 06.06.2017.
 */
public class ChildrenThread implements Runnable {

    private final  ScreenshotController sc;

    public ChildrenThread(ScreenshotController sc) {
        this.sc = sc;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(1000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
//                        sc.captureFullScreen(true);
                        sc.reloadImageView();
                    }
                });
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
