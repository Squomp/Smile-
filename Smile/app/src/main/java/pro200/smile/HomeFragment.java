package pro200.smile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;
import pro200.smile.service.LiveSmileService;


public class HomeFragment extends Fragment {
    private ImageButton imageButton;
    private GestureDetector gs;
    private final int NUMBER_OF_PICTURES = 5;

    private List<Smile> images;
    private int currentImageIndex = 0;

    public static Fragment newInstance() {
        Fragment frag = new HomeFragment();
        return frag;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addListenerOnButton() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int halfWidth = displayMetrics.widthPixels/2;

//        imageButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(images.size() != 0) {
//
//
//                    if (gs.onTouchEvent(event)) {
//                        if (event.getX() >= halfWidth) {
//                            currentImageIndex++;
//                            if (currentImageIndex == images.size()) {
//                                currentImageIndex = 0;
//                            }
//                            imageButton.setImageBitmap(images.get(currentImageIndex).getImage());
//                        } else {
//                            currentImageIndex--;
//                            if (currentImageIndex == -1) {
//                                currentImageIndex = images.size() - 1;
//                            }
//                            imageButton.setImageBitmap(images.get(currentImageIndex).getImage());
//                        }
//
//                    }
//
//                }
//                return true;
//            }
//        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        gs = new GestureDetector(getActivity(), new ClickConfirmed());
        populateImageButton();
        imageButton = (ImageButton)v.findViewById(R.id.imageButton1);
        addListenerOnButton();
        return v;
    }

    private void populateImageButton() {
        LiveSmileService ls =  new LiveSmileService(this.getContext());
        ls.LoginOrCreate("YEET");
        SmileList retrievedList = ls.GetRandomSmiles(7);
        images = retrievedList.getSmiles();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            Bundle args = getArguments();
        }
    }
}
