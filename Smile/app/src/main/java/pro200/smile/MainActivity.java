package pro200.smile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import pro200.smile.service.SmileService;
import pro200.smile.service.StaticSmileService;

public class MainActivity extends AppCompatActivity {

    //private SmileService service = new StaticSmileService(getApplicationContext());
    private BottomNavigationView mBottomNav;
    private int mSelectedItemInt;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectFragment(item);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MenuItem selectedItem = mBottomNav.getMenu().getItem(0);
        selectFragment(selectedItem);
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;

        // update selected item
        mSelectedItemInt = item.getItemId();

        // init corresponding fragment
        switch (mSelectedItemInt) {
            case R.id.navigation_home:
                frag = HomeFragment.newInstance();
                break;
            case R.id.navigation_smile:
                frag = SmileFragment.newInstance();
                break;
            case R.id.navigation_profile:
                frag = ProfileFragment.newInstance();
                break;
        }

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(0);
        if (mSelectedItemInt != homeItem.getItemId()) {
            // select home item
            mBottomNav.setSelectedItemId(homeItem.getItemId());
        } else {
            super.onBackPressed();
        }
    }
}