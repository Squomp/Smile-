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


public class HomeFragment extends Fragment {
    private ImageButton imageButton;
    private GestureDetector gs;
    private final int NUMBER_OF_PICTURES = 5;

    private int[] images = new int[]{R.drawable.happybeach, R.drawable.badboyz, R.drawable.smile1, R.drawable.smile2, R.drawable.smile3};
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

        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(gs.onTouchEvent(event)){
                    if(event.getX() >= halfWidth){
                        currentImageIndex++;
                        if(currentImageIndex == NUMBER_OF_PICTURES ){
                            currentImageIndex = 0;
                        }
                        imageButton.setImageResource(images[currentImageIndex]);
                    }else {
                        currentImageIndex--;
                        if(currentImageIndex == -1){
                            currentImageIndex = NUMBER_OF_PICTURES - 1;
                        }
                        imageButton.setImageResource(images[currentImageIndex]);
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
        imageButton = (ImageButton)v.findViewById(R.id.imageButton1);
        addListenerOnButton();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            Bundle args = getArguments();
        }
    }
}
