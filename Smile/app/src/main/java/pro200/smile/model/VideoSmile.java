package pro200.smile.model;

import java.net.URI;
import java.util.Date;

public class VideoSmile extends Smile {
    private URI filePath;

    public VideoSmile(Date timestamp, URI filePath) {
        this.timestamp = timestamp;
        this.filePath = filePath;
    }

    public URI getImage() {
        return filePath;
    }

    public void setImage(URI filePath) {
        this.filePath = filePath;
    }
}
