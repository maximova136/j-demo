package sample;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import javafx.application.Platform;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by vmaksimo on 07.06.2017.
 */
public class CloudHost {
    // size for gallery to fit in
    private int WIDTH_SMALL = 250;
    private int HEIGHT_SMALL = 200;

    private int WIDTH_MIDDLE = 400;
    private int HEIGHT_MIDDLE = 300;
    private String CROP_TYPE = "limit";

    private Cloudinary cloudinary;

    // do not change
    CloudHost() {
        Map params = ObjectUtils.asMap(
                "cloud_name", "dira4xawu",
                "api_key", "596983916447626",
                "api_secret", "H-23JcrsPFmkwy7yWHoH2R8DmD8"
        );
        cloudinary = new Cloudinary(params);

    }

    static Map lastResult = null;
    String last_public_id = null;

    public void upload(File toUpload) throws IOException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lastResult = cloudinary.uploader().upload(toUpload, ObjectUtils.emptyMap());
                    last_public_id = (String) lastResult.get("public_id");
                } catch (IOException e) {
                    Platform.runLater(()->{
                        Notifications.create().title("Error").text("Network is unreachable").showError();
                    });
                    e.printStackTrace();
                }
            }
        });
        thread.run();

    }

    public String getLastPublicId() {
        return last_public_id;
    }

    String urlHead = "http://res.cloudinary.com/dira4xawu/image/upload";

    public String getLastImageUrl() {
        return urlHead + "/q_100/" + last_public_id;
    }

    public String getImageUrl(String public_id) {
        return urlHead + "/q_100/" + public_id;
    }

    public String getPreviewImageUrl(String public_id) {
        return urlHead + "/c_" + CROP_TYPE + ",w_" + WIDTH_SMALL + ",h_" + HEIGHT_SMALL + "/" + public_id;
    }

    public String getPreviewMiddleImageUrl (String public_id){
        return urlHead + "/c_" + CROP_TYPE + ",w_" + WIDTH_MIDDLE + ",h_" + HEIGHT_MIDDLE + "/" + public_id;
    }

    public String getLastPreviewImageUrl() {
        return urlHead + "/c_" + CROP_TYPE + ",w_" + WIDTH_SMALL + ",h_" + HEIGHT_SMALL + "/" + last_public_id;
    }

    public static String getPublicID(String url) {
        return url.substring(url.lastIndexOf('/')+1);
    }

    // TODO test
    public void deleteImage(String public_id) {
        try {
//            cloudinary.uploader().destroy(public_id, ObjectUtils.emptyMap());
            cloudinary.api().deleteResources(Arrays.asList(public_id), ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
            // TODO add "can not delete image" message
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
