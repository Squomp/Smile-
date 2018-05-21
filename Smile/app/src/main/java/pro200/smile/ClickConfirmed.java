package pro200.smile;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class ClickConfirmed extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }
}
