package com.example.khulazy.ui.notifications;

import android.content.Context;
import android.os.StrictMode;
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
import java.util.logging.Handler;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    String response; //서버 응답
    String TAG = "socket 응답";
//    Handler handler = new Handler();
    onHTTPConnection httpConnection = new onHTTPConnection();

    class SocketThread extends Thread{
        String host; // 서버 IP
        String data; // 전송 데이터
        String rpi; // 라즈베리 파이 ip

        public SocketThread(String host, String data){
            this.host = host;
            this.data = data;
            this.rpi = "127.0.0.1";
        }

        @Override
        public void run() {
            try{
                int port = 5555; //포트 번호는 서버측과 똑같이

                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                rpi = httpConnection.GETFunction(this.host);
                Socket socket = new Socket(rpi, port); // 소켓 열어주기

                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream()); //소켓의 출력 스트림 참조
                outstream.writeObject(data); // 출력 스트림에 데이터 넣기
                outstream.flush(); // 출력

                String OutData = "AT+START\n";
                byte[] data = OutData.getBytes();
                OutputStream output = socket.getOutputStream();
                output.write(data);

                Log.d(TAG, "데이터 보냄");

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(mContext.getApplicationContext(), "소켓 연결됨", Toast.LENGTH_LONG).show();
//                    }
//                });

//                while (true) {
//                    byte[] buffer = new byte[1024];
//                    InputStream input = socket.getInputStream();
//                    int bytes = input.read(buffer);
//
//                    Log.d(TAG, "byte = " + bytes);
//                }

                while(true) {
                    ObjectInputStream instream = new ObjectInputStream(socket.getInputStream()); // 소켓의 입력 스트림 참조
                    response = (String) instream.readObject(); // 응답 가져오기
                    Log.d(TAG, "response: " + response);

                    if (response == "stop") {
                        break;
                    }
                }
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText((MainActivity)getActivity(), "서버 응답 : " + response, Toast.LENGTH_LONG).show();
//                    }
//                });

                socket.close(); // 소켓 해제

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private ArrayList<TimeActivity> listbundle = new ArrayList<>();
    Context mContext;


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
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    Toast.makeText(mContext, "알람을 설정했습니다.", Toast.LENGTH_SHORT).show();
                    SocketThread thread = new SocketThread("https://43.201.130.48:8484/connection", "set");
                    thread.start();
                }
                else {
                    Toast.makeText(mContext, "알람을 해제했습니다.", Toast.LENGTH_SHORT).show();
                    SocketThread thread = new SocketThread("https://43.201.130.48:8484/connection", "cancel");
                    thread.start();
                }
            }
        });

        return holder;
    }

    @Override
    // recyclerView 자체와 item 데이터셋을 서로 연결해주는 과정
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeActivity list = listbundle.get(position);
        holder.time.setText(list.getHour() + ":" + list.getMinute());
    }

    @Override
    // 데이터셋의 데이터 개수이다.
    public int getItemCount() {
        return listbundle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView time;

        public ViewHolder(@NonNull View view) {
            super(view);
            time = view.findViewById(R.id.time);
        }
    }
}

