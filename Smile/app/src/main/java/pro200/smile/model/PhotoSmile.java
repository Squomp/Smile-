package pro200.smile.model;

import android.graphics.Bitmap;

import java.util.Date;

public class PhotoSmile extends Smile{

    private Bitmap image;

    public PhotoSmile(Date timestamp, Bitmap image) {
        this.timestamp = timestamp;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
