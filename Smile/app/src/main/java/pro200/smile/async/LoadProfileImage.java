package pro200.smile.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {

    ImageView profileImageView;

    public LoadProfileImage(ImageView profileImageView) {
        this.profileImageView = profileImageView;
    }

    protected Bitmap doInBackground(String... uri) {
        String url = uri[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            profileImageView.setImageBitmap(result);
        }
    }
}
