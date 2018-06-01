package pro200.smile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Profile;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import pro200.smile.async.LoadProfileImage;
import pro200.smile.async.LoadUserImages;
import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;
import pro200.smile.service.LiveSmileService;
import pro200.smile.service.SmileService;


public class ProfileFragment extends Fragment {

    private View mContent;
    private ImageView profileImageView;
    private TextView profileTextView;
    private LiveSmileService service;
    private SmileList smiles;
    private Profile profile = Profile.getCurrentProfile();

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

        //Initialize Views
        mContent = view.findViewById(R.id.profile_content);
        profileImageView = view.findViewById(R.id.imageView);
        profileTextView = view.findViewById(R.id.profileNameTextView);
        service = new LiveSmileService(this.getContext());

        if (savedInstanceState == null) {
            Bundle args = getArguments();

            runAsyncTasks();
            profileTextView.setText(profile.getName());
        } else {
            //profileImageView.setImageBitmap((Bitmap)savedInstanceState.getParcelable("ProfileBitmap"));
        }

        LinearLayout layout = view.findViewById(R.id.userSmiles);

    }

    private void runAsyncTasks() {
        LoadProfileImage profileImageLoader = new LoadProfileImage(profileImageView);
        profileImageLoader.execute(profile.getProfilePictureUri(300, 300).toString());

        LoadUserImages userImageLoader = new LoadUserImages(this.getActivity(), service);
        userImageLoader.execute();
    }
}
