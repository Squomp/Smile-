package pro200.smile.service;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URI;

import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;

public interface SmileService {

    SmileList getUserSmiles(String id);
    SmileList getRandomSmiles(int count);
    void loginOrCreate(String id);
    void addSmile(String id, Bitmap smile);
    void changePreference(String id, String newPreference);
}
