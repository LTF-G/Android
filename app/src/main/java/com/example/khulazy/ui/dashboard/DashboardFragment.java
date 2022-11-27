package com.example.khulazy.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.khulazy.R;
import com.example.khulazy.databinding.FragmentDashboardBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
//    // variable for our bar chart
//    BarChart barChart;
//
//    // variable for our bar data.
//    BarData barData;
//
//    // variable for our bar data set.
//    BarDataSet barDataSet;
//
//    // array list for storing entries.
//    ArrayList barEntriesArrayList;
//
//    // pie chart
//    PieChart pieChart;
//    int[] colorArray = new int[] {Color.LTGRAY, Color.GRAY};
//
//    private FragmentDashboardBinding binding;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        DashboardViewModel dashboardViewModel =
//                new ViewModelProvider(this).get(DashboardViewModel.class);
//
//        binding = FragmentDashboardBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        // initializing variable for bar chart.
//        barChart = root.findViewById(R.id.actual_sleep_time);
//
//        // calling method to get bar entries.
//        getBarEntries();
//
//        // creating a new bar data set.
//        barDataSet = new BarDataSet(barEntriesArrayList, "Geeks for Geeks");
//
//        // creating a new bar data and
//        // passing our bar data set.
//        barData = new BarData(barDataSet);
//
//        // below line is to set data
//        // to our bar chart.
//        barChart.setData(barData);
//
//        // adding color to our bar data set.
//        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//
//        // setting text color.
//        barDataSet.setValueTextColor(Color.BLACK);
//
//        // setting text size
//        barDataSet.setValueTextSize(16f);
//        barChart.getDescription().setEnabled(false);
//        barChart.getAxisRight().setDrawGridLines(false);
//        barChart.getAxisLeft().setDrawGridLines(false);
//        barChart.getXAxis().setDrawGridLines(false);
//        barChart.getLegend().setEnabled(false);
//        barChart.getAxisRight().setDrawLabels(false);
//        barChart.getXAxis().setDrawLabels(false);
//        barChart.getAxisLeft().setDrawLabels(false);
//        barChart.getAxisRight().setDrawAxisLine(false);
//        barChart.getXAxis().setDrawAxisLine(false);
//        barChart.getAxisLeft().setDrawAxisLine(false);
//
//
//
//        pieChart = root.findViewById(R.id.achievement_rate);
//
//        PieDataSet pieDataSet = new PieDataSet(data1(),"수면 목표 달성률");
//        pieDataSet.setColors(colorArray);
//        PieData pieData = new PieData(pieDataSet);
//        pieChart.setDrawEntryLabels(true);
//        pieChart.setUsePercentValues(true);
//        pieData.setValueTextSize(0);
//        pieChart.setHoleRadius(60);
//        pieChart.setHoleColor(Color.parseColor("#EEF0F5"));
//        pieChart.setData(pieData);
//        pieChart.invalidate();
//        pieChart.getDescription().setEnabled(false);
//        pieChart.getLegend().setEnabled(false);
//        pieChart.setCenterText("12.5%");
//
//        return root;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//
//    private void getBarEntries() {
//        // creating a new array list
//        barEntriesArrayList = new ArrayList<>();
//
//        // adding new entry to our array list with bar
//        // entry and passing x and y axis value to it.
//        barEntriesArrayList.add(new BarEntry(1f, 4));
//        barEntriesArrayList.add(new BarEntry(2f, 3));
//        barEntriesArrayList.add(new BarEntry(3f, 1));
//        barEntriesArrayList.add(new BarEntry(4f, 2));
//        barEntriesArrayList.add(new BarEntry(5f, 4));
//        barEntriesArrayList.add(new BarEntry(6f, 1));
//    }
//
//    private ArrayList<PieEntry> data1() {
//        ArrayList<PieEntry> datavalue = new ArrayList<>();
//
//        datavalue.add(new PieEntry(7));
//        datavalue.add(new PieEntry(1));
//
//
//        return datavalue;
//    }
//}
