package com.example.khulazy.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khulazy.MainActivity;
import com.example.khulazy.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<TimeActivity> listbundle = new ArrayList<>();
    Context mContext;

    private SharedPreferences preferences;

    String response; //서버 응답
    String TAG = "socket 응답";
    Handler handler = new Handler();
    onHTTPConnection httpConnection = new onHTTPConnection();

    class SocketThread extends Thread{
        String server; // 서버 IP
        String data; // 전송 데이터
        String rpi; // 라즈베리 파이 ip
        int port;
        String accessToken;
        String refreshToken;

        public SocketThread(String server, String data, String accessToken, String refreshToken){
            this.server = server;
            this.data = data;
            this.rpi = "192.168.107.161";
            this.port = 8888;
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        @Override
        public void run() {
            try {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                rpi = httpConnection.GETFunction(this.server, this.accessToken, this.refreshToken);
                Socket socket = new Socket(rpi, port); // 소켓 열어주기

                String OutData = data;
                byte[] data = OutData.getBytes();
                OutputStream output = socket.getOutputStream();
                output.write(data);
                Log.d(TAG, "데이터 보냄" + data);

                while(true) {
                    ObjectInputStream instream = new ObjectInputStream(socket.getInputStream()); // 소켓의 입력 스트림 참조
                    response = (String) instream.readObject(); // 응답 가져오기
                    Log.d(TAG, "response: " + response);

                    response = "stop";
                    if (response == "stop") {
                        break;
                    }
                }

                Log.d(TAG, "소켓 닫음");
                socket.close(); // 소켓 해제

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public RecyclerAdapter(ArrayList<TimeActivity> bundle) {
        this.listbundle = bundle;
    }

    @NonNull
    @Override
    // adapter와 연결하는 recyclerView에 추가할 item레이아웃과 item Data를 bind함
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext  = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alarm_list, parent, false);
        ViewHolder holder = new ViewHolder(view);

        Switch toggle = view.findViewById(R.id.alarm_toggle);
        TextView starttime = view.findViewById(R.id.starttime);
        TextView endtime = view.findViewById(R.id.endtime);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    Toast.makeText(mContext, "알람을 설정했습니다.", Toast.LENGTH_SHORT).show();

                    preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    String refreshToken = preferences.getString("refreshToken", "no token");
                    String accessToken = preferences.getString("accessToken", "no token");

                    String data = starttime.getText() + "/" + endtime.getText();
                    SocketThread thread = new SocketThread("https://43.201.130.48:8484/connection", data, accessToken, refreshToken);
                    thread.start();
                }

                else {
                    Toast.makeText(mContext, "알람을 해제했습니다.", Toast.LENGTH_SHORT).show();
                    SocketThread thread = new SocketThread("https://43.201.130.48:8484/connection", "cancel", "", "");
                    thread.start();
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeActivity list = listbundle.get(position);
        String StartTime = "";
        String EndTme = "";

        if (list.getStartHour() <= 9) {
            StartTime +=  "0" + list.getStartHour();
        }
        else {
            StartTime += list.getStartHour();
        }
        if (list.getStartMinute() <= 9) {
            StartTime +=  ":0" + list.getStartMinute();
        }
        else {
            StartTime +=  ":" + list.getStartMinute();
        }

        if (list.getEndHour() <= 9) {
            EndTme +=  "0" + list.getEndHour();
        }
        else {
            EndTme += list.getEndHour();
        }

        if (list.getEndMinute() <= 9) {
            EndTme +=  ":0" + list.getEndMinute();
        }

        else {
            EndTme +=  ":" + list.getEndMinute();
        }

        holder.startTime.setText(StartTime);
        holder.endTime.setText(EndTme);
    }

    @Override
    public int getItemCount() {
        return listbundle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView startTime;
        TextView endTime;

        public ViewHolder(@NonNull View view) {
            super(view);
            startTime = view.findViewById(R.id.starttime);
            endTime = view.findViewById(R.id.endtime);
        }
    }
}

