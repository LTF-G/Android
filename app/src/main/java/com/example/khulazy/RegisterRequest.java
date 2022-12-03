package com.example.khulazy;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static private String URL = "https://43.201.130.48:8484/auth/register";
    private Map<String, String> map;
    //private Map<String, String>parameters;

    public RegisterRequest(String userid, String password, String nickname,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userid", userid);
        map.put("password", password);
        map.put("nickname", nickname);

        Log.d("register", "request 보냄");
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        Log.d("register", "getParams userid: " + map.get("userid"));
        Log.d("register", "getParams password: " + map.get("password"));
        Log.d("register", "getParams nickname: " + map.get("nickname"));
        return map;
    }
}

