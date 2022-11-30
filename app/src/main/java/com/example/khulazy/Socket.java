package com.example.khulazy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

class ClientThread extends Thread{
    private String serveradd;
    private Handler mHandler;

    public ClientThread(String str, Handler handler){
        serveradd=str;
        mHandler=handler;
    }

    @Override
    public void run() {
        Log.d("testing","ClientThread run");

        Socket sock=null;
        try{
            sock=new Socket(serveradd,8484);
            println(">> 서버와 연결 성공!");
            SendThread sendThread=new SendThread(this, sock.getOutputStream());
            RecvThread recvThread=new RecvThread(this,sock.getInputStream());
            sendThread.start();
            recvThread.start();
            sendThread.join();
            recvThread.join();
        }catch (Exception e){
            e.printStackTrace();
            println(e.getMessage());
        }finally {
            try{
                if(sock!=null){
                    sock.close();
                    println(">> 서버와 연결 종료!");
                }
            }catch (IOException e){
                e.printStackTrace();
                println(e.getMessage());
            }
        }
    }
    public void println(String str){
        Message msg=Message.obtain();
        msg.what=1;
        msg.obj=str+"\n";
        mHandler.sendMessage(msg);
    }
}
class SendThread extends Thread{
    private ClientThread clientThread;
    private OutputStream outputStream;
    public static Handler mHandler;

    public SendThread(ClientThread client,OutputStream output){
        clientThread=client;
        outputStream=output;
    }

    @Override
    public void run() {
        Log.d("testing","SendThread run");

        Looper.prepare();
        mHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        // 송신
                        try{
                            String s=(String) msg.obj;
                            outputStream.write(s.getBytes());
                            clientThread.println("[보낸 데이터]"+s);
                        }catch (IOException e){
                            e.printStackTrace();
                            clientThread.println(e.getMessage());
                        }
                        break;
                    case 2:
                        // 스레드 종료
                        getLooper().quit();
                        break;
                }
            }
        };
        Looper.loop();
    }
}
class RecvThread extends Thread{
    private ClientThread clientThread;
    private InputStream inputStream;

    public RecvThread(ClientThread client, InputStream input){
        clientThread=client;
        inputStream = input;
    }

    @Override
    public void run() {
        Log.d("testing","RevThread run");
        byte[] buf=new byte[1024];
        while(true){
            try{
                int nbytes=inputStream.read(buf);
                if(nbytes>0){
                    String s=new String(buf,0,nbytes);
                    clientThread.println("[받은 데이터]"+s);
                }
                else{
                    clientThread.println(">> 서버가 연결 끊음!");
                    if(SendThread.mHandler!=null){
                        Message msg=Message.obtain();
                        msg.what=2;
                        SendThread.mHandler.sendMessage(msg);
                    }
                    break;
                }
            }catch (IOException e){
                clientThread.println(e.getMessage());
            }
        }
    }
}
