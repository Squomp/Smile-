package pro200.smile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.SubMenuBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
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

    private LinearLayout profileLayout;
    private ImageView profileImageView;
    private ImageButton settingsButton;
    private TextView profileTextView;
    private LiveSmileService service;
    private SmileList smiles;
    private Profile profile = Profile.getCurrentProfile();
    private PopupMenu.OnMenuItemClickListener settingsPopupMenuItemListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.settings_public:
                    service.changePreference(profile.getId(), "public");
                    return true;
                case R.id.settings_friends_only:
                    service.changePreference(profile.getId(), "friends");
                    return true;
                case R.id.settings_private:
                    service.changePreference(profile.getId(), "private");
                    return true;
                default:
                    return false;
            }
        }
    };

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
        Log.d("ProfileFragment", "Calling onViewCreated");

        //Initialize Views
        settingsButton = view.findViewById(R.id.settingsButton);
        profileLayout = view.findViewById(R.id.profileLayout);
        profileImageView = view.findViewById(R.id.imageView);
        profileTextView = view.findViewById(R.id.profileNameTextView);
        service = new LiveSmileService(this.getContext());

        //Initialize listeners
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsPopup(v);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("ProfileFragment", "Calling onActivityCreated");

        if (savedInstanceState == null) {
            Bundle args = getArguments();

            runAsyncTasks();
            profileTextView.setText(profile.getName());
        } else {
            Log.d("ProfileFragment", "Instance Not null");
            profileImageView.setImageBitmap((Bitmap) savedInstanceState.getParcelable("ProfileBitmap"));
        }
    }

    private void runAsyncTasks() {
        LoadProfileImage profileImageLoader = new LoadProfileImage(profileImageView);
        profileImageLoader.execute(profile.getProfilePictureUri(300, 300).toString());

        LoadUserImages userImageLoader = new LoadUserImages(this.getActivity(), service, profileLayout);
        userImageLoader.execute();
    }

    public void showSettingsPopup(View v) {
        PopupMenu popup = new PopupMenu(this.getActivity(), v);
        popup.setOnMenuItemClickListener(settingsPopupMenuItemListener);
        popup.inflate(R.menu.settings);
        String privacySetting = service.getPreference(profile.getId());
        if (privacySetting.equals("public")) {
            popup.getMenu().getItem(0).setChecked(true);
        } else if (privacySetting.equals("friends")) {
            popup.getMenu().getItem(1).setChecked(true);
        } else if (privacySetting.equals("private")) {
            popup.getMenu().getItem(2).setChecked(true);
        }
        popup.show();
}


}
