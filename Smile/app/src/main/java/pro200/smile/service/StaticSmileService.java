package pro200.smile.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Date;

import pro200.smile.R;
import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;

public class StaticSmileService implements SmileService {

    private Context context;

    public StaticSmileService(Context context) {
        this.context = context;
    }

    @Override
    public SmileList getUserSmiles(String id) {
        SmileList smileList = new SmileList();
        smileList.addSmile(new Smile(new Date(), BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_home_black_24dp)));
        smileList.addSmile(new Smile(new Date(), BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_profile_black_24dp)));
        smileList.addSmile(new Smile(new Date(), BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_smile_black_24dp)));
        return smileList;
    }

    @Override
    public SmileList getRandomSmiles(int count) {
        SmileList smileList = new SmileList();
        smileList.addSmile(new Smile(new Date(), BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_home_black_24dp)));
        smileList.addSmile(new Smile(new Date(), BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_profile_black_24dp)));
        smileList.addSmile(new Smile(new Date(), BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_smile_black_24dp)));
        return smileList;
    }

    @Override
    public void loginOrCreate(String id) {
        /* do nothing */
    }

    @Override
    public void addSmile(String id, Bitmap smile) {
        /* do nothing */
    }

    @Override
    public void changePreference(String id, String newPreference) {

    }
}
