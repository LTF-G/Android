package com.example.khulazy.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khulazy.MainActivity;
import com.example.khulazy.R;
import com.example.khulazy.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button button;

    private int hour;
    private int minute;

    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private ArrayList<TimeActivity> listbundle = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            hour = getArguments().getInt("hour");
            minute = getArguments().getInt("minute");

            Toast.makeText(getActivity().getApplicationContext(), hour+" "+minute, Toast.LENGTH_SHORT).show();

            TimeActivity t = new TimeActivity(hour, minute);
            listbundle.add(t);
        }
        view = inflater.inflate(R.layout.fragment_notifications, container, false);

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
        adapter = new RecyclerAdapter(listbundle);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                ((MainActivity)getActivity()).replaceFragment(1, 0, 0);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}