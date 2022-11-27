package com.example.khulazy;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.khulazy.ui.dashboard.DashboardFragment;
import com.example.khulazy.ui.home.HomeFragment;
import com.example.khulazy.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private String TAG = "main";

    // 프래그먼트 변수
    Fragment home;
    Fragment dashboard;
    Fragment noti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 프래그먼트 생성
        home = new HomeFragment();
        dashboard = new DashboardFragment();
        noti = new NotificationsFragment();

        // 초기 플래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, dashboard).commit();


        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.nav_view);

        // 리스너 등록

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i(TAG, "바텀 네비게이션 클릭");

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
}