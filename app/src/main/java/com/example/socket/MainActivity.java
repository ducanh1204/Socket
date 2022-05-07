package com.example.socket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private ServerSocket serverSocket;
    public static final int SERVER_PORT = 2222;
    private LinearLayout msgList;
    private Handler handler;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Server-Side Endpoint");

        handler = new Handler();
        msgList = findViewById(R.id.msgList);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startCreateSocket();
            }
        };
        long delay = 100L;
        Timer timer = new Timer("Timers");
        timer.schedule(timerTask, 0, delay);
    }

    private void startCreateSocket() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            if (serverSocket != null) {
                try {
                    socket = serverSocket.accept();
//                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
//                        String str = br.lines().collect(Collectors.joining());
                    //showMessage(str, Color.BLUE, false);
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                tv.setText(str);
//                            }
//                        });
                    InputStream stream = socket.getInputStream();
                    int allSize = stream.available();
                    byte[] data = new byte[stream.available()];
                    stream.read(data);
                    String str = new String(data, StandardCharsets.UTF_8);
                    showMessage(str, Color.BLUE, false);

                    Log.e("RESULT", allSize + "");

                } catch (IOException e) {

                } finally {
                    Log.e("RESULT", "sssssss");
                    try {
                        socket.close();
                        serverSocket.close();
                    } catch (IOException ioException) {
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TextView textView(String message, int color, Boolean value) {
        if (null == message || message.trim().isEmpty()) {
            message = "<Empty Message>";
        }
        TextView tv = new TextView(this);
        tv.setTextColor(color);
//        tv.setText(message + " [" + getTime() + "]");
        tv.setText(message);
        tv.setTextSize(20);
        tv.setPadding(0, 5, 0, 0);
        if (value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            }
        }
        return tv;
    }

    public void showMessage(final String message, final int color, final Boolean value) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                msgList.addView(textView(message, color, value));
            }
        });
    }

    private String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }
}
