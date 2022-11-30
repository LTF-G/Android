package com.example.khulazy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import com.example.khulazy.ui.dashboard.DashboardFragment;
import com.example.khulazy.ui.home.HomeFragment;
import com.example.khulazy.ui.notifications.NotificationsFragment;
import com.example.khulazy.ui.notifications.TimePickerActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private String TAG = "main";
    String serverip = "43.201.130.48";

    // 프래그먼트 변수
    Fragment home;
    Fragment dashboard;
    Fragment noti;
    Fragment timepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 프래그먼트 생성
        home = new HomeFragment();
        dashboard = new DashboardFragment();
        noti = new NotificationsFragment();
        timepicker = new TimePickerActivity();

        // 초기 플래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, home).commit();


        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.nav_view);

        // 리스너 등록

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, home).commit();
                        return true;
                    case R.id.navigation_dashboard: ;
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, dashboard).commit();
                        return true;
                    case R.id.navigation_notifications:
                        getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, noti).commit();
                        return true;
                }
                return true;
            }
        });


    }

    private Handler myHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Log.d("socket 수신 결과", (String)msg.obj);
                    break;
            }
        }
    };

    public void socketNetwork() {
        ClientThread clientThread=new ClientThread(serverip, myHandler);
        clientThread.start();
    }

    public void replaceFragment(int index, int hour, int minute) {
        switch(index) {
            case 1:
                Log.d(TAG, "replaceFragment: 화면 전환 1");
                getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, timepicker).commit();
                break;
            case 2:
                Log.d(TAG, "replaceFragment: 화면 전환 2");

                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                noti.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, noti).commit();
                break;
        }
    }
}