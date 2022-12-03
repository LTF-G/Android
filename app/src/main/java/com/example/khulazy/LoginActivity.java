package com.example.khulazy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class LoginActivity extends AppCompatActivity {

    private EditText login_id, login_password;
    private Button login_button, join_button;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        login_id = findViewById(R.id.login_id);
        login_password = findViewById(R.id.login_password);

        join_button = findViewById(R.id.join_button);
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserId = login_id.getText().toString();
                String UserPwd = login_password.getText().toString();

                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String page = "https://43.201.130.48:8484/auth/login";
                            URL url = new URL(page);

                            Map<String,Object> params = new LinkedHashMap<>();
                            params.put("userid", UserId);
                            params.put("password", UserPwd);

                            StringBuilder postData = new StringBuilder();
                            for(Map.Entry<String,Object> param : params.entrySet()) {
                                if(postData.length() != 0) postData.append('&');
                                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                                postData.append('=');
                                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                            }
                            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                            ignoreSsl();
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            final StringBuilder sb = new StringBuilder();
                            if (conn != null) {
                                conn.setConnectTimeout(10000);
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                                conn.setRequestProperty("Accept", "application/json");
                                conn.setDoOutput(true);
                                conn.setDoInput(true);
                                conn.getOutputStream().write(postDataBytes);

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
                                    Log.i("tag", "확인 jsonArray : " + jsonObject.getString("message"));

                                    String datas = jsonObject.getString("data");
                                    JSONObject tokens = new JSONObject(datas);
                                    String accessToken = tokens.getString("accessToken");
                                    String refreshToken = tokens.getString("refreshToken");

                                    Log.d("tag", "accessToken: " + accessToken);
                                    Log.d("tag", "refreshToken: " + refreshToken);

                                    editor.putString("refreshToken", refreshToken);
                                    editor.putString("accessToken", accessToken);
                                    editor.putString("userid", UserId);
                                    editor.apply();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), String.format("%s님 환영합니다.", UserId), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                                }
                                else {
                                }
                            }

                        } catch (Exception e) {
                            Log.i("tag", "error :" + e);
                        }
                    }
                });
                th.start();
            }
        });
    }

    public static void ignoreSsl () throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    private static void trustAllHttpsCertificates () throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new RegisterActivity.miTM();
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
}
