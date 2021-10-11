package com.example.lab4nonuithreadprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button startButton;
    private volatile boolean stopThread = false;
    public TextView text;
    public int num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.button);
        text = findViewById(R.id.textView);
        num = 0;
    }

    public void mockFileDownloader(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Downloading...");
            }
        });

        for(int downloadProgress = 0; downloadProgress <= 100; downloadProgress = downloadProgress+10){
            num = downloadProgress;
            if(stopThread){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText("Start");
                        text.setText("");
                    }
                });
                //return;
            }

            if(!stopThread){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText("Download Progress: "+num+"%");
                    }
                });
            }



            //Log.d(TAG, "Download Progress: " + downloadProgress + "%");
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Start");
                text.setText("");

            }
        });



    }

    class ExampleRunnable implements Runnable{
        @Override
        public void run(){
            mockFileDownloader();
        }
    }

    public void startDownload(View view){
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view){
        stopThread = true;
    }
}