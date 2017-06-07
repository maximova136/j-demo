package sample;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by vmaksimo on 07.06.2017.
 */
public class CloudHost {
    private Cloudinary cloudinary;

    CloudHost(){
        Map params = ObjectUtils.asMap(
                "cloud_name","dira4xawu",
                "api_key", "596983916447626",
                "api_secret", "H-23JcrsPFmkwy7yWHoH2R8DmD8"
        );
        cloudinary = new Cloudinary(params);
//        cloudinary =  new Cloudinary(
    }
    static Map lastResult = null;
    public void upload(File toUpload) throws IOException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    cloudHost.upload(fileImg);
                    lastResult = cloudinary.uploader().upload(toUpload, ObjectUtils.emptyMap());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.run();

    }
     public String getLastResultUrl(){
         return (String) lastResult.get("url");
     }
}
