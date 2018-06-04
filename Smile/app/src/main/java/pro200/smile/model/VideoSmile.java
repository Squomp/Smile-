package pro200.smile.model;

import android.util.Log;

import java.net.URI;
import java.util.Date;

public class VideoSmile extends Smile {
    private String filePath;

    public VideoSmile(Date timestamp, String filePath) {
        this.timestamp = timestamp;
        this.filePath = filePath;
        Log.d("CREATION", "200");
    }

    public String getFilePath() {
        return filePath;
    }

    public void setImage(String filePath) {
        this.filePath = filePath;
    }
}
