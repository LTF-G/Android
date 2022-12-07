package com.example.khulazy.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.khulazy.R;
import com.example.khulazy.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private View view;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        textView = view.findViewById(R.id.user_name);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        String usernames = sharedPreferences.getString("userid", "no name");
        textView.setText(usernames + "님의 기본 정보");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}