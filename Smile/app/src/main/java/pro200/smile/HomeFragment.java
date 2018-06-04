package pro200.smile;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import com.facebook.Profile;

import java.util.List;

import pro200.smile.model.PhotoSmile;
import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;
import pro200.smile.model.VideoSmile;
import pro200.smile.service.LiveSmileService;


public class HomeFragment extends Fragment {

    private final int NUMBER_OF_PICTURES = 5;
    private ImageButton slideshowButton;
    private VideoView slideshowVideoView;
    private ImageView slideshowImageView;
    private GestureDetector gs;
    private List<Smile> imagesAndVideoList;
    private int currentImageIndex = 0;


    public static Fragment newInstance() {
        Fragment frag = new HomeFragment();
        return frag;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addListenerOnButton() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int halfWidth = displayMetrics.widthPixels / 2;

        slideshowButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("LISTENER", "CLICKED");
                if (imagesAndVideoList.size() != 0) {
                    if (gs.onTouchEvent(event)) {
                        if (event.getX() >= halfWidth) {
                            currentImageIndex++;
                            if (currentImageIndex == imagesAndVideoList.size()) {
                                currentImageIndex = 0;
                            }
                            if (imagesAndVideoList.get(currentImageIndex) instanceof PhotoSmile) {
                                slideshowVideoView.setVisibility(View.GONE);
                                slideshowImageView.setVisibility(View.VISIBLE);

                                slideshowImageView.setImageBitmap(((PhotoSmile) imagesAndVideoList.get(currentImageIndex)).getImage());
                            } else {
                                slideshowImageView.setVisibility(View.GONE);
                                slideshowVideoView.setVisibility(View.VISIBLE);
                                Log.d("FILEPATH", (Uri.parse(((VideoSmile)imagesAndVideoList.get(currentImageIndex)).getFilePath())).toString());
                                slideshowVideoView.setVideoURI(Uri.parse(((VideoSmile)imagesAndVideoList.get(currentImageIndex)).getFilePath()));
                            }

                        } else {
                            currentImageIndex--;
                            if (currentImageIndex == -1) {
                                currentImageIndex = imagesAndVideoList.size() - 1;
                            }

                            if (imagesAndVideoList.get(currentImageIndex) instanceof PhotoSmile) {
                                slideshowVideoView.setVisibility(View.GONE);
                                slideshowImageView.setVisibility(View.VISIBLE);

                                slideshowImageView.setImageBitmap(((PhotoSmile) imagesAndVideoList.get(currentImageIndex)).getImage());
                            } else {
                                slideshowImageView.setVisibility(View.GONE);
                                slideshowVideoView.setVisibility(View.VISIBLE);

                                slideshowVideoView.setVideoURI(Uri.parse(((VideoSmile) imagesAndVideoList.get(currentImageIndex)).getFilePath()));
                            }
                        }

                    }

                }
                return true;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        gs = new GestureDetector(getActivity(), new ClickConfirmed());
        populateImageButton();

        return v;
    }

    private void populateImageButton() {
        LiveSmileService ls = new LiveSmileService(this.getContext());
        SmileList retrievedList = ls.GetUserSmiles(Profile.getCurrentProfile().getId());
        imagesAndVideoList = retrievedList.getSmiles();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize Views
        slideshowButton = view.findViewById(R.id.slideshowButton);
        slideshowImageView = view.findViewById(R.id.slideshowImageView);
        slideshowVideoView = view.findViewById(R.id.slideshowVideoView);
        addListenerOnButton();
        if (savedInstanceState == null) {
            Bundle args = getArguments();
        }
    }
}
