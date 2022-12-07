package com.example.khulazy.ui.Alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khulazy.MainActivity;
import com.example.khulazy.R;
import com.example.khulazy.ui.notifications.RecyclerAdapter;
import com.example.khulazy.ui.notifications.TimeActivity;

import java.util.ArrayList;

public class AlarmFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button button;

    private int starHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    RecyclerView recyclerView;
    RecyclerAdapter2 adapter;
    RecyclerView.LayoutManager layoutManager;

    private ArrayList<TimeActivity> listbundle = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            starHour = getArguments().getInt("startHour");
            startMinute = getArguments().getInt("startMinute");

            TimeActivity t = new TimeActivity(starHour, startMinute, 0, 0);
            listbundle.add(t);
            adapter.notifyDataSetChanged();
            setArguments(null);
        }
        view = inflater.inflate(R.layout.fragment_alarms, container, false);

        // 리스너 등록
        button = view.findViewById(R.id.button);
        button.setOnClickListener(this);

        // 리스트 뷰
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter2(listbundle);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                ((MainActivity)getActivity()).replaceFragment(4, 0, 0, 0, 0);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}