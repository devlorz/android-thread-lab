package com.leeway.labthread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvCounter;
    int counter;

    Thread thread;
    Handler handler;

    HandlerThread backgroundHandlerThread;
    Handler backgroundHandler;
    Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = 0;
        tvCounter = (TextView) findViewById(R.id.tvCounter);

        // thread method 1: thread
        /*
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Run in background
                for (int i = 0; i < 100; i++) {
                    counter++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // UI Thread aka Main Thread
                            tvCounter.setText(counter + "");
                        }
                    });

                }
            }
        });
        thread.start();
        */

        // thread method 2 : thread with handler
        /*
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // run in main thread
                tvCounter.setText(msg.arg1 + "");
            }
        };
        // background thread
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Run in background
                for (int i = 0; i < 100; i++) {
                    counter++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        return;
                    }

                    // create message to send to looper via handler
                    Message msg = new Message();
                    msg.arg1 = counter;
                    handler.sendMessage(msg);
                }
            }
        });
        thread.start();
        */

        // thread method 3: handler only
        /*
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                counter++;
                tvCounter.setText(counter + "");
                if (counter < 100)
                    sendEmptyMessageDelayed(0, 1000);
            }
        };
        handler.sendEmptyMessageDelayed(0, 1000);
        */

        // Thread method 4
        backgroundHandlerThread = new HandlerThread("BackgroundHandlerThread");
        backgroundHandlerThread.start();

        backgroundHandler = new Handler(backgroundHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // background
                Message msgMain = new Message();
                msgMain.arg1 = msg.arg1 + 1;
                mainHandler.sendMessage(msgMain);
            }
        };

        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // main thread
                tvCounter.setText(msg.arg1 + "");
                if (msg.arg1 < 100) {
                    Message msgBack = new Message();
                    msgBack.arg1 = msg.arg1;
                    backgroundHandler.sendMessageDelayed(msgBack, 1000);
                }
            }
        };

        Message msgBack = new Message();
        msgBack.arg1 = 0;
        backgroundHandler.sendMessageDelayed(msgBack, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //thread.interrupt();
        backgroundHandlerThread.quit();
    }
}
