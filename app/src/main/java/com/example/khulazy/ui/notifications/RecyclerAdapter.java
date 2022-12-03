package com.example.khulazy.ui.notifications;

import android.content.Context;
import android.os.Handler;
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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<TimeActivity> listbundle = new ArrayList<>();
    Context mContext;

    String response; //서버 응답
    String TAG = "socket 응답";
    Handler handler = new Handler();

    class SocketThread extends Thread{
        String host; // 서버 IP
        String data; // 전송 데이터

        public SocketThread(String host, String data){
            this.host = host;
            this.data = data;
        }

        @Override
        public void run() {
            try{
                int port = 5555; //포트 번호는 서버측과 똑같이
                Socket socket = new Socket(host, port); // 소켓 열어주기
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream()); //소켓의 출력 스트림 참조
                outstream.writeObject(data); // 출력 스트림에 데이터 넣기
                outstream.flush(); // 출력

                ObjectInputStream instream = new ObjectInputStream(socket.getInputStream()); // 소켓의 입력 스트림 참조
                response = (String) instream.readObject(); // 응답 가져오기
                Log.d(TAG, "response: " + response);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext.getApplicationContext(), "서버 응답 : " + response, Toast.LENGTH_LONG).show();
                    }
                });

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
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    Toast.makeText(mContext, "알람을 설정했습니다.", Toast.LENGTH_SHORT).show();
                    String data = view.findViewById(R.id.starttime) + "/" + view.findViewById(R.id.endtime).toString();
                    Log.d(TAG, "data: " + data);
                    SocketThread thread = new SocketThread("hostip", data);
                    thread.start();
                }
                else {
                    Toast.makeText(mContext, "알람을 해제했습니다.", Toast.LENGTH_SHORT).show();
                    SocketThread thread = new SocketThread("hostip", "canceled");
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
    // 데이터셋의 데이터 개수이다.
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
