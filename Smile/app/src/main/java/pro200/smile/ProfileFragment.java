package pro200.smile;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.facebook.Profile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static java.security.AccessController.getContext;


public class ProfileFragment extends Fragment {

    private View mContent;
    private ImageView profileImageView;

    public static Fragment newInstance() {
        Fragment frag = new ProfileFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            Bundle args = getArguments();

            // initialize views
            mContent = view.findViewById(R.id.profile_content);
            profileImageView = view.findViewById(R.id.imageView);
            Profile profile = Profile.getCurrentProfile();
            new LoadProfileImage(profileImageView).execute(profile.getProfilePictureUri(300, 300).toString());
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
