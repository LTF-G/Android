package com.example.khulazy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import com.example.khulazy.ui.dashboard.DashboardFragment;
import com.example.khulazy.ui.home.HomeFragment;
import com.example.khulazy.ui.notifications.NotificationsFragment;
import com.example.khulazy.ui.notifications.TimePickerActivity;
import com.example.khulazy.ui.notifications.onHTTPConnection;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private String TAG = "main";
    private String serverip = "43.201.130.48";
    private String authorization = "";
    private String refreshToken = "";

    // 프래그먼트 변수
    Fragment home;
    Fragment dashboard;
    Fragment noti;
    Fragment timepicker;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        // 프래그먼트 생성
        home = new HomeFragment();
        dashboard = new DashboardFragment();
        noti = new NotificationsFragment();
        timepicker = new TimePickerActivity();

        // 초기 플래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, home).commit();

        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.nav_view);

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://" + serverip + ":8484/connection");
                    ignoreSsl();
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    final StringBuilder sb = new StringBuilder();

                    if (conn != null) {
                        authorization = preferences.getString("accessToken", "no token");
                        refreshToken = preferences.getString("refreshToken", "no token");

                        conn.setConnectTimeout(10000);
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setRequestProperty("authorization", "Bearer " + authorization);
                        conn.setRequestProperty("refresh", "Bearer " + refreshToken);
                        conn.setDoInput(true);
                        conn.connect();
                    }

                    Log.d("rpi ip", "rpi server: " + conn.getResponseCode());

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(
                                conn.getInputStream(), "utf-8"
                        ));
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        br.close();

                        JSONObject jsonObject = new JSONObject(sb.toString());
                        String result = jsonObject.getString("ip");

                        Log.d("rpi ip", "GETFunction: " + result);
                        editor.putString("rpi", result);
                        editor.apply();
                    }
                    else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();

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

    public void replaceFragment(int index, int starHour, int startMinute, int endHour, int endMinute) {
        switch(index) {
            case 1:
                Log.d(TAG, "replaceFragment: 화면 전환 1");
                getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, timepicker).commit();
                break;
            case 2:
                Log.d(TAG, "replaceFragment: 화면 전환 2");

                Bundle bundle = new Bundle();
                bundle.putInt("starHour", starHour);
                bundle.putInt("startMinute", startMinute);

                bundle.putInt("endHour", endHour);
                bundle.putInt("endMinute", endMinute);

                noti.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, noti).commit();
                break;
        }
    }

    public static void ignoreSsl() throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    private static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class miTM implements TrustManager, X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
    }
}