package pro200.smile;

import android.annotation.SuppressLint;
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

import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;
import pro200.smile.service.LiveSmileService;


public class HomeFragment extends Fragment {

    private final int NUMBER_OF_PICTURES = 5;
    private ImageButton slideshowButton;
    private VideoView slideshowVideoView;
    private ImageView slideshowImageView;
    private GestureDetector gs;
    private List<Smile> imagesAndVideoList;
    private int currentImageIndex = -1;
    private LiveSmileService ls;
    private final int NUMBER_OF_IMAGES_RETRIEVED = 2;


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
//                                currentImageIndex = 0;
                                SmileList retrievedList = ls.getRandomSmiles(NUMBER_OF_IMAGES_RETRIEVED);
                                for (int i = 0; i < retrievedList.getSmiles().size(); i++) {
                                    imagesAndVideoList.add(retrievedList.getSmiles().get(i));
                                }
                            }
                            slideshowImageView.setVisibility(View.VISIBLE);
                            slideshowImageView.setImageBitmap(((Smile) imagesAndVideoList.get(currentImageIndex)).getImage());

//                            if (imagesAndVideoList.get(currentImageIndex) instanceof Smile) {
//                                slideshowVideoView.setVisibility(View.GONE);
//                            }
//                            else {
//                                slideshowImageView.setVisibility(View.GONE);
//                                slideshowVideoView.setVisibility(View.VISIBLE);
//                                Log.d("FILEPATH", (Uri.parse(((Smile)imagesAndVideoList.get(currentImageIndex)).getFilePath())).toString());
//                                slideshowVideoView.setVideoURI(Uri.parse(((Smile)imagesAndVideoList.get(currentImageIndex)).getFilePath()));
//                            }

                        } else {
                            currentImageIndex--;
                            if (currentImageIndex == -1) {
                                currentImageIndex = 0;
                            } else if(currentImageIndex == -2) {
                                currentImageIndex = 0;
                                slideshowImageView.setImageBitmap(((Smile) imagesAndVideoList.get(currentImageIndex)).getImage());
                            }else{
                                    slideshowImageView.setImageBitmap(((Smile) imagesAndVideoList.get(currentImageIndex)).getImage());
                            }


//                            if (imagesAndVideoList.get(currentImageIndex) instanceof Smile) {
//                                slideshowVideoView.setVisibility(View.GONE);
//                                slideshowImageView.setVisibility(View.VISIBLE);

//                            } else {
//                                slideshowImageView.setVisibility(View.GONE);
//                                slideshowVideoView.setVisibility(View.VISIBLE);
//
//                                slideshowVideoView.setVideoURI(Uri.parse(((VideoSmile) imagesAndVideoList.get(currentImageIndex)).getFilePath()));
//                            }
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
        ls = new LiveSmileService(this.getContext());
        gs = new GestureDetector(getActivity(), new ClickConfirmed());
        populateImageButton();

        return v;
    }

    private void populateImageButton() {
        SmileList retrievedList = ls.getRandomSmiles(NUMBER_OF_IMAGES_RETRIEVED);
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
