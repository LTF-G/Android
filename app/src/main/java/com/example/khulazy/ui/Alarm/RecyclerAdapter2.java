package com.example.khulazy.ui.Alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
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
import com.example.khulazy.ui.notifications.RecyclerAdapter;
import com.example.khulazy.ui.notifications.TimeActivity;
import com.example.khulazy.ui.notifications.onHTTPConnection;

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

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ViewHolder> {

    private ArrayList<TimeActivity> listbundle = new ArrayList<>();
    Context mContext;

    private SharedPreferences preferences;

    String TAG = "socket 응답";
    Handler handler = new Handler();


    public RecyclerAdapter2(ArrayList<TimeActivity> bundle) {
        this.listbundle = bundle;
    }

    @NonNull
    @Override
    // adapter와 연결하는 recyclerView에 추가할 item레이아웃과 item Data를 bind함
    public RecyclerAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext  = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alarm_list2, parent, false);
        RecyclerAdapter2.ViewHolder holder = new RecyclerAdapter2.ViewHolder(view);

        Switch toggle = view.findViewById(R.id.alarm_toggle);
        TextView starttime = view.findViewById(R.id.starttime);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    Toast.makeText(mContext, "알람을 설정했습니다.", Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(mContext, "알람을 해제했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter2.ViewHolder holder, int position) {
        TimeActivity list = listbundle.get(position);
        String StartTime = "";
        String EndTme = "";
        Log.d(TAG, "onBindViewHolder: " + list.getStartHour());

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

        Log.d(TAG, "onBindViewHolder: " + StartTime);
        holder.startTime.setText(StartTime);
    }

    @Override
    public int getItemCount() {
        return listbundle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView startTime;

        public ViewHolder(@NonNull View view) {
            super(view);
            startTime = view.findViewById(R.id.starttime);
        }
    }
}


