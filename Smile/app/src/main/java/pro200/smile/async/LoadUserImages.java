package pro200.smile.async;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.Profile;

import java.lang.ref.WeakReference;

import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;
import pro200.smile.service.SmileService;

public class LoadUserImages extends AsyncTask<Void, Void, SmileList> {

    private WeakReference<Activity> weakActivity;
    private SmileService service;
    private LinearLayout layout;

    public LoadUserImages(Activity activity, SmileService service, LinearLayout layout) {
        this.weakActivity = new WeakReference<>(activity);
        this.service = service;
        this.layout = layout;
    }

    @Override
    protected SmileList doInBackground(Void... voids) {
        SmileList smiles = service.getUserSmiles(Profile.getCurrentProfile().getId());
        return smiles;
    }

    @Override
    protected void onPostExecute(SmileList result) {
        if (result != null) {
            Activity activity = weakActivity.get();
            if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
            for (Smile s : result.getSmiles()) {
                int dp = (int) activity.getApplicationContext().getResources().getDisplayMetrics().density;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(10 * dp, 10 * dp, 10 * dp, 10 * dp);
                lp.gravity = Gravity.CENTER;
                ImageView img = new ImageView(activity.getApplicationContext());
                img.setPadding(1 * dp,1 * dp,1 * dp,1 * dp);
                img.setBackgroundColor(Color.BLACK);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                img.setMaxHeight(350 * dp);
                img.setAdjustViewBounds(true);
                img.setImageBitmap(s.getImage());

                layout.addView(img, lp);
            }
        }
    }
}
