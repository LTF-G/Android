package com.example.khulazy.ui.dashboard;

import static android.icu.number.NumberRangeFormatter.RangeIdentityFallback.RANGE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.khulazy.MainActivity;
import com.example.khulazy.R;
import com.example.khulazy.databinding.FragmentDashboardBinding;
import com.example.khulazy.ui.dashboard.DashboardViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DashboardFragment extends Fragment {
    private View view, root;
    private TextView username;
    private String accesstoken;
    private String refreshtoken;
    private String TAG = "dashboard";



    private FragmentDashboardBinding binding;

    // variable for our bar chart
    BarChart barChart;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // array list for storing entries.
    ArrayList barEntriesArrayList = new ArrayList<>();;

    LineChart lineChart;

    // pie chart
    PieChart pieChart;
    int[] colorArray = new int[]{Color.LTGRAY, Color.GRAY};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        // initializing variable for bar chart.
        barChart = root.findViewById(R.id.actual_sleep_time);

        lineChart = root.findViewById(R.id.tossturn);
        // calling method to get bar entries.

        // creating a new bar data set.
        barDataSet = new BarDataSet(barEntriesArrayList, "Geeks for Geeks");

        // creating a new bar data and
        // passing our bar data set.
        barData = new BarData(barDataSet);

        // below line is to set data
        // to our bar chart.
        barChart.setData(barData);

        // adding color to our bar data set.
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // setting text color.
        barDataSet.setValueTextColor(Color.DKGRAY);

        barChart.animateY(1500, Easing.EaseOutQuad);
        // setting text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setDrawLabels(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barData.setValueTextSize(20);
        barChart.getXAxis().setAxisLineColor(Color.YELLOW);
        barChart.getAxisLeft().setAxisLineColor(Color.YELLOW);
        barDataSet.setColor(Color.parseColor("#FFAB40"));


        pieChart = root.findViewById(R.id.achievement_rate);

        pieChart.animateY(1500, Easing.EaseOutQuad);
        PieDataSet pieDataSet = new PieDataSet(data1(7, 1), "수면 목표 달성률");
        pieDataSet.setColors(colorArray);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setDrawEntryLabels(true);
        pieChart.setUsePercentValues(true);
        pieData.setValueTextSize(20);
        pieData.setValueTextColor(Color.DKGRAY);
        pieChart.setCenterTextSize(20);
        pieChart.setEntryLabelTextSize(16);
        pieChart.setHoleRadius(60);
        pieChart.setHoleColor(Color.parseColor("#EEF0F5"));
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        lineChart.setExtraBottomOffset(15f); // 간격
        lineChart.getDescription().setEnabled(false);
        setLineChartData(true);
        lineChart.animateY(1800, Easing.EaseOutQuad);
        // XAxis (아래쪽) - 선 유무, 사이즈, 색상, 축 위치 설정
        XAxis xAxis = lineChart.getXAxis();
        lineChart.getLegend().setTextColor(Color.DKGRAY);
        lineChart.getLegend().setTextSize(12);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // x축 데이터 표시 위치
        xAxis.setGranularity(1f);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(Color.rgb(118, 118, 118));
        xAxis.setSpaceMin(0.1f); // Chart 맨 왼쪽 간격 띄우기
        xAxis.setSpaceMax(0.1f); // Chart 맨 오른쪽 간격 띄우기

        // YAxis(Right) (왼쪽) - 선 유무, 데이터 최솟값/최댓값, 색상
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setTextSize(14f);
        yAxisLeft.setTextColor(Color.rgb(163, 163, 163));
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setAxisLineWidth(2);
        yAxisLeft.setAxisMinimum(0f); // 최솟값
        yAxisLeft.setAxisMaximum((float) 10); // 최댓값
        yAxisLeft.setGranularity((float) 10);

        YAxis yAxis = lineChart.getAxisRight();
        yAxis.setDrawLabels(false); // label 삭제
        yAxis.setTextColor(Color.rgb(163, 163, 163));
        yAxis.setDrawAxisLine(false);
        yAxis.setAxisLineWidth(2);
        yAxis.setAxisMinimum(0f); // 최솟값
        yAxis.setAxisMaximum((float) 10); // 최댓값
        yAxis.setGranularity((float) 10);

        username = root.findViewById(R.id.user_name);

        try {
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getContext());
            String usernames = sharedPreferences.getString("userid", "no name");
            accesstoken = sharedPreferences.getString("accessToken", "no token");
            refreshtoken = sharedPreferences.getString("refreshToken", "no token");
            username.setText(usernames + "님의");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String page = "https://43.201.130.48:8484/sleep";
                    URL url = new URL(page);

                    ignoreSsl();
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    final StringBuilder sb = new StringBuilder();
                    if (conn != null) {

                        Log.d(TAG, "accesstoken: " + accesstoken);
                        Log.d(TAG, "refreshtoken: " + refreshtoken);

                        conn.setConnectTimeout(10000);
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setRequestProperty("authorization", "Bearer " + accesstoken);
                        conn.setRequestProperty("refresh", "Bearer " + refreshtoken);
                        conn.setDoInput(true);
                        conn.connect();

                        Log.d(TAG, "response: " + conn.getResponseCode());

                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(
                                    conn.getInputStream(), "utf-8"
                            ));
                            String line;
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                            // 버퍼리더 종료
                            br.close();
                            // 응답 Json 타입일 경우
                            JSONObject jsonObject = new JSONObject(sb.toString());

                            JSONArray statistic = jsonObject.getJSONArray("sleepstats");
//                            Log.d("test", statistic.toString());

                            if (statistic.length() > 0) {
                                for (int i = 0; i < statistic.length(); i++) {
                                    JSONObject obj = statistic.getJSONObject(i);
                                    final int index = i;
                                    final int j = (Integer) obj.getInt("actual_sleep")/3600000;
                                   getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            getBarEntries(index, j);
                                        }
                                    });
                                }

                                JSONObject obj = statistic.getJSONObject(0);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int value1 = 1;
                                        try {
                                            value1 = (Integer) obj.getInt("actual_sleep")/3600000;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        PieDataSet pieDataSet = new PieDataSet(data1(8, value1), "수면 목표 달성률");

                                        float percent = (float) (value1 / 8.0) * 100;
                                        pieChart.setCenterText(percent + "%");
                                        pieDataSet.setColors(colorArray);
                                        pieDataSet.setColors(Color.parseColor("#FFAB40"), Color.DKGRAY);
                                        PieData pieData = new PieData(pieDataSet);
                                        pieChart.setData(pieData);
                                        pieChart.notifyDataSetChanged();
                                    }
                                });
                            }



                        } else {
                        }


                    }

                } catch (Exception e) {
                    Log.i("tag", "error :" + e);
                }
            }

        });
        th.start();



        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static void ignoreSsl() throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    private static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class miTM implements TrustManager, X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
    }

    private void getBarEntries(int i,float j) {
        // creating a new array list


        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.

        float temp = (float) (Math.round(j*10)/10.0);

        barEntriesArrayList.add(new BarEntry(i,j));
    //    barEntriesArrayList.add(new BarEntry(x, a));

        barDataSet = new BarDataSet(barEntriesArrayList, "Geeks for Geeks");
        barDataSet.setValueFormatter(new MyValueFormatter());

        // creating a new bar data and
        // passing our bar data set.
        barData = new BarData(barDataSet);
        barData.setValueTextSize(13);
        barData.setValueTextColor(Color.DKGRAY);
        barDataSet.setColor(Color.parseColor("#FFAB40"));

        // below line is to set data
        // to our bar chart.
        barChart.setData(barData);

    }

    private ArrayList<PieEntry> data1(int value1, int value2) {
        ArrayList<PieEntry> datavalue = new ArrayList<>();

        datavalue.add(new PieEntry(value1));
        datavalue.add(new PieEntry(value2));

        return datavalue;
    }

    private void setLineChartData(boolean exists) {
        ArrayList<Entry> entry1 = new ArrayList<>();
        for(int i = 0;i < 7;i++) {
            int val = (int) (Math.random() * 10);
            entry1.add(new Entry(i + 1, val));
        }

        LineDataSet set1;
        set1 = new LineDataSet(entry1, "뒤척임 수");
        set1.setCircleColor(Color.parseColor("#FFAB40"));
        set1.setLineWidth(2);
        set1.setColor(Color.DKGRAY);
        set1.setDrawValues(false);
        set1.setCircleHoleColor(Color.parseColor("#FFAB40"));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        lineChart.setData(data); // LineData 전달
        lineChart.invalidate();
    }
}

