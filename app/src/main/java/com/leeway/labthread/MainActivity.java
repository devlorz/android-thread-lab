package com.leeway.labthread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvCounter;
    int counter;

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counter = 0;
        tvCounter = (TextView) findViewById(R.id.tvCounter);

        // thread method 1: thread
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        thread.interrupt();
    }
}
