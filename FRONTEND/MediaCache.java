package application;

import javafx.scene.media.Media;
import java.io.File;

public class MediaCache {
    public static final Media BACKGROUND_MEDIA;
    static {
        String absolutePath = "C:\\Users\\Josh\\Downloads\\bg.mp4";
        File videoFile = new File(absolutePath);
        if (videoFile.exists()) {
            BACKGROUND_MEDIA = new Media(videoFile.toURI().toString());
        } else {
            BACKGROUND_MEDIA = null;
            System.err.println("Background video file not found: " + videoFile.getAbsolutePath());
        }
    }
}
