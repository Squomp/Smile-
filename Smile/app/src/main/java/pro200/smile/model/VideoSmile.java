package pro200.smile.model;

import java.net.URI;
import java.util.Date;

public class VideoSmile extends Smile {
    private android.net.Uri filePath;

    public VideoSmile(Date timestamp, android.net.Uri filePath) {
        this.timestamp = timestamp;
        this.filePath = filePath;
    }

    public android.net.Uri getFilePath() {
        return filePath;
    }

    public void setImage(android.net.Uri filePath) {
        this.filePath = filePath;
    }
}
