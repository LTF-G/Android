package com.example.khulazy.ui.notifications;

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

public class onHTTPConnection {
    String EXCEPTION_ERROR = "url 오류 발생";

    public String GETFunction(String mUrl) {
        try {
            URL url = new URL(mUrl);
            ignoreSsl();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImFzZCIsImlhdCI6MTY2OTQ5MDg5MCwiZXhwIjoxNjY5NDk4MDkwfQ.TjmeuMwEcnY7str3L2DyEt3QTD6ZSKTK5mBa5Eqxc7Q");
            conn.setRequestProperty("refresh", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2Njk0OTA4OTAsImV4cCI6MTY3MDcwMDQ5MH0.KTc1GuJZAyYB3lvtZZ4W8XLjnEZh97YL8bNcDSCoxZw");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            InputStream is = conn.getInputStream();
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String result;
            while ((result = br.readLine()) != null) {
                sb.append(result);
            }
            br.close();

            result = sb.toString();

            JSONArray jsonResponse = new JSONArray(result);
            JSONObject jsonObject = jsonResponse.getJSONObject(0);
            result = jsonObject.getString("ip");

            return result;

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
