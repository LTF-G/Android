package com.example.khulazy.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khulazy.MainActivity;
import com.example.khulazy.R;

public class TimePickerActivity extends Fragment implements View.OnClickListener {

    private View view;
    private Button button;
    private TimePicker timePicker;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_timepicker, container, false);

        // 리스너 등록
        button = view.findViewById(R.id.okBtn);
        button.setOnClickListener(this);

        timePicker = (TimePicker)view.findViewById(R.id.time_picker);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okBtn:
                ((MainActivity)getActivity()).replaceFragment(2, timePicker.getHour(), timePicker.getMinute());
                break;
        }
    }

}