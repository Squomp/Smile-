package pro200.smile.service;

import android.graphics.Bitmap;

import java.net.URI;

import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;

public interface SmileService {

    SmileList GetUserSmiles(String id);
    SmileList GetRandomSmiles(int count);
    void LoginOrCreate(String id);
    void AddSmile(String id, Bitmap smile, URI videoFile);
    void changePreference(String id, String newPreference);
}
