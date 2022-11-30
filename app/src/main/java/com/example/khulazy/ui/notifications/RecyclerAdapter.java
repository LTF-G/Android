package com.example.khulazy.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khulazy.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

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
