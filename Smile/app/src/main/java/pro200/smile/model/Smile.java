package pro200.smile.model;

import android.graphics.Bitmap;

import java.util.Date;

public class Smile {

    private Date timestamp;
    private Bitmap image;

    public Smile(Date timestamp, Bitmap image) {
        this.timestamp = timestamp;
        this.image = image;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
