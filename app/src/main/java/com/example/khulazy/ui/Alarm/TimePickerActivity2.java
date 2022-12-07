package com.example.khulazy.ui.Alarm;

import android.os.Bundle;
import android.util.Log;
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

public class TimePickerActivity2 extends Fragment implements View.OnClickListener {

    private View view;
    private Button button;
    private TimePicker timePicker1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_timepicker2, container, false);

        // 리스너 등록
        button = view.findViewById(R.id.okBtn);
        button.setOnClickListener(this);

        timePicker1 = (TimePicker)view.findViewById(R.id.time_picker1);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okBtn:
                ((MainActivity)getActivity()).replaceFragment(3, timePicker1.getHour(), timePicker1.getMinute(), 0, 0);
                break;
        }
    }
}