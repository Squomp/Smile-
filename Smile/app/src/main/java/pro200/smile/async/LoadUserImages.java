package pro200.smile.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.facebook.Profile;

import java.lang.ref.WeakReference;

import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;
import pro200.smile.service.SmileService;

public class LoadUserImages extends AsyncTask<Void, Void, SmileList> {

    private WeakReference<Activity> weakActivity;
    private SmileService service;

    public LoadUserImages(Activity activity, SmileService service) {
        this.weakActivity = new WeakReference<>(activity);
        this.service = service;
    }

    @Override
    protected SmileList doInBackground(Void... voids) {
        SmileList smiles = service.GetUserSmiles(Profile.getCurrentProfile().getId());
        return smiles;
    }

    @Override
    protected void onPostExecute(SmileList result) {
        if (result != null) {
            Activity activity = weakActivity.get();
            if(activity == null || activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
            for (Smile s : result.getSmiles()) {
                ImageView img = new ImageView(activity.getApplicationContext());
//                img.setImageBitmap();
//                layout.addView(img);
            }
        }
    }
}
