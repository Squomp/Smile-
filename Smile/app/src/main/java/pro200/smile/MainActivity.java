package pro200.smile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Calendar;

import pro200.smile.service.NotificationReceiver;
import pro200.smile.service.SmileService;
import pro200.smile.service.StaticSmileService;

public class MainActivity extends AppCompatActivity {

    private SmileService service;
    private PendingIntent pendingIntent;
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
        service = new StaticSmileService(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        startNotification();
        MenuItem selectedItem = mBottomNav.getMenu().getItem(0);
        selectFragment(selectedItem);
    }

    private void startNotification() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 26);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM,Calendar.PM);

        Intent myIntent = new Intent(MainActivity.this, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 100, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
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