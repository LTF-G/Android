package com.example.khulazy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private String TAG = "main";
    String serverip = "43.201.130.48";

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
}

class ClientThread extends Thread{
    private String serveradd;
    private Handler mHandler;

    public ClientThread(String str, Handler handler){
        serveradd=str;
        mHandler=handler;
    }

    @Override
    public void run() {
        Log.d("testing","ClientThread run");

        Socket sock=null;
        try{
            sock=new Socket(serveradd,9000);
            println(">> 서버와 연결 성공!");
            SendThread sendThread=new SendThread(this, sock.getOutputStream());
            RecvThread recvThread=new RecvThread(this,sock.getInputStream());
            sendThread.start();
            recvThread.start();
            sendThread.join();
            recvThread.join();
        }catch (Exception e){
            e.printStackTrace();
            println(e.getMessage());
        }finally {
            try{
                if(sock!=null){
                    sock.close();
                    println(">> 서버와 연결 종료!");
                }
            }catch (IOException e){
                e.printStackTrace();
                println(e.getMessage());
            }
        }
    }
    public void println(String str){
        Message msg=Message.obtain();
        msg.what=1;
        msg.obj=str+"\n";
        mHandler.sendMessage(msg);
    }
}
class SendThread extends Thread{
    private ClientThread clientThread;
    private OutputStream outputStream;
    public static Handler mHandler;

    public SendThread(ClientThread client,OutputStream output){
        clientThread=client;
        outputStream=output;
    }

    @Override
    public void run() {
        Log.d("testing","SendThread run");

        Looper.prepare();
        mHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        // 송신
                        try{
                            String s=(String) msg.obj;
                            outputStream.write(s.getBytes());
                            clientThread.println("[보낸 데이터]"+s);
                        }catch (IOException e){
                            e.printStackTrace();
                            clientThread.println(e.getMessage());
                        }
                        break;
                    case 2:
                        // 스레드 종료
                        getLooper().quit();
                        break;
                }
            }
        };
        Looper.loop();
    }
}
class RecvThread extends Thread{
    private ClientThread clientThread;
    private InputStream inputStream;

    public RecvThread(ClientThread client, InputStream input){
        clientThread=client;
        inputStream = input;
    }

    @Override
    public void run() {
        Log.d("testing","RevThread run");
        byte[] buf=new byte[1024];
        while(true){
            try{
                int nbytes=inputStream.read(buf);
                if(nbytes>0){
                    String s=new String(buf,0,nbytes);
                    clientThread.println("[받은 데이터]"+s);
                }
                else{
                    clientThread.println(">> 서버가 연결 끊음!");
                    if(SendThread.mHandler!=null){
                        Message msg=Message.obtain();
                        msg.what=2;
                        SendThread.mHandler.sendMessage(msg);
                    }
                    break;
                }
            }catch (IOException e){
                clientThread.println(e.getMessage());
            }
        }
    }
}