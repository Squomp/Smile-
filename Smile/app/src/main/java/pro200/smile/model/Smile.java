package pro200.smile.model;

import android.graphics.Bitmap;

import java.util.Date;

public abstract class Smile {

    protected Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
