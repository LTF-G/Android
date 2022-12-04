package com.example.khulazy.ui.notifications;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class onHTTPConnection extends Thread {
    String EXCEPTION_ERROR = "url 오류 발생";
    String result;

    @Override
    public void run() {
        super.run();
    }

    public String GETFunction(String mUrl, String authorization, String refreshToken) {
        try {
            URL url = new URL(mUrl);
            ignoreSsl();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            final StringBuilder sb = new StringBuilder();

            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("authorization", "Bearer " + authorization);
                conn.setRequestProperty("refresh", "Bearer " + refreshToken);
                conn.setDoInput(true);
                conn.connect();
            }

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
                result = jsonObject.getString("ip");

                Log.d("rpi ip", "GETFunction: " + result);
                return result;
            }
            else {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("ERROR", EXCEPTION_ERROR);
        return null;
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
}
