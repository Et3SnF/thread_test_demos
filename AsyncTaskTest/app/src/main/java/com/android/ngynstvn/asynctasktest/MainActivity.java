package com.android.ngynstvn.asynctasktest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private ProgressBar progressBar;
    private TextView progressValue;
    private static int progressCounter = 0;
    private MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.btn_start_button);
        progressBar = (ProgressBar) findViewById(R.id.pb_test_progress);
        progressValue = (TextView) findViewById(R.id.tv_progress_value);

        Log.v("(MainActivity)", "Current thread in onCreate(): " + Thread.currentThread().getName());

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("(MainActivity)", "Current thread in onClick(): " + Thread.currentThread().getName());
                myTask = new MyTask();
                myTask.execute();
            }
        });
    }

    class MyTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            Log.v("(MainActivity)", "Current thread in onPreExecute(): " + Thread.currentThread().getName());
            progressBar.setVisibility(View.VISIBLE);
            progressValue.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.v("(MainActivity)", "Current thread in doInBackground(): " + Thread.currentThread().getName());

            int maxValue = 125000;

            for(progressCounter = 0; progressCounter <= maxValue; progressCounter++) {
                publishProgress((int) (((float) progressCounter / maxValue) * 100));
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
            progressValue.setText(String.valueOf(progress[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.v("(MainActivity)", "Current thread in onPostExecute(): " + Thread.currentThread().getName());
            progressBar.setVisibility(View.GONE);
            progressValue.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            Log.v("(MainActivity)", "Current thread in onCancelled(): " + Thread.currentThread().getName());
            progressBar.setVisibility(View.GONE);
            progressValue.setVisibility(View.GONE);
            progressCounter = 0;
        }
    }
}
