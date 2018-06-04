package pro200.smile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.Profile;

import java.util.Calendar;

import pro200.smile.service.LiveSmileService;
import pro200.smile.service.NotificationReceiver;
import pro200.smile.service.SmileService;
import pro200.smile.service.StaticSmileService;

public class MainActivity extends AppCompatActivity {

    private Profile profile = Profile.getCurrentProfile();
    private SmileService service;
    private PendingIntent pendingIntent;
    private BottomNavigationView mBottomNav;
    private int mSelectedItemInt;

    private AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                   AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        }
    };

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

        //Initialize views
        Profile.fetchProfileForCurrentAccessToken();
        mBottomNav = findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        service = new LiveSmileService(getApplicationContext());
        if(Profile.getCurrentProfile() != null) {
            service.LoginOrCreate(Profile.getCurrentProfile().getId());
        }

        startNotification();

        MenuItem selectedItem = mBottomNav.getMenu().getItem(0);
        selectFragment(selectedItem);
    }

    private void startNotification() {
        boolean alarmUp = (PendingIntent.getBroadcast(MainActivity.this, 100,
                new Intent(MainActivity.this, NotificationReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmUp) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Intent myIntent = new Intent(MainActivity.this, NotificationReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 100, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        }
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        String tag = "";

        // update selected item
        mSelectedItemInt = item.getItemId();

        // init corresponding fragment
        switch (mSelectedItemInt) {
            case R.id.navigation_home:
                tag = "home";
                frag = getSupportFragmentManager().findFragmentByTag(tag);
                if (frag == null) {
                    frag = HomeFragment.newInstance();
                }
                break;
            case R.id.navigation_smile:
                tag = "smile";
                frag = getSupportFragmentManager().findFragmentByTag(tag);
                if (frag == null) {
                    frag = SmileFragment.newInstance();
                }
                break;
            case R.id.navigation_profile:
                tag = "profile";
                frag = getSupportFragmentManager().findFragmentByTag(tag);
                if (frag == null) {
                    frag = ProfileFragment.newInstance();
                }
                break;
        }
        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, tag);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("MainActivity", "Calling onSaveInstanceState");
    }
}