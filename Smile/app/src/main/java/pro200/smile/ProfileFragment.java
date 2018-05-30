package pro200.smile;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.facebook.Profile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;
import pro200.smile.service.LiveSmileService;

import static java.security.AccessController.getContext;


public class ProfileFragment extends Fragment {

    private View mContent;
    private ImageView profileImageView;
    private LiveSmileService service;
    private SmileList smiles;

    public static Fragment newInstance() {
        Fragment frag = new ProfileFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        service = new LiveSmileService(this.getContext());
        service.LoginOrCreate("boi");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContent = view.findViewById(R.id.profile_content);
        profileImageView = view.findViewById(R.id.imageView);
        if (savedInstanceState == null) {
            Bundle args = getArguments();

            Profile profile = Profile.getCurrentProfile();
            new LoadProfileImage(profileImageView).execute(profile.getProfilePictureUri(300, 300).toString());
        }
        else {
            profileImageView.setImageBitmap((Bitmap)savedInstanceState.getParcelable("ProfileBitmap"));
        }

        smiles = service.GetUserSmiles("boi");
        LinearLayout layout = view.findViewById(R.id.userSmiles);
        for (Smile s : smiles.getSmiles()) {
            ImageView img = new ImageView(this.getContext());
            img.setImageBitmap(s.getImage());
            layout.addView(img);
        }
    }
}


class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {

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

    protected void onPostExecute(Bitmap result) {

        if (result != null) {
            profileImageView.setImageBitmap(result);
        }
    }
}
