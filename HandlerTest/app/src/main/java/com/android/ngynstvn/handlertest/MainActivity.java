package com.android.ngynstvn.handlertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView progressValue;
    private TextView progressMax;

    private Thread thread;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tb_activity_main);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.pb_activity_main);
        progressValue = (TextView) findViewById(R.id.tv_progress_value);
        progressMax = (TextView) findViewById(R.id.tv_progress_max_value);

        progressMax.setText(String.valueOf(progressBar.getMax()));

        thread = new Thread(new MyThread());
        thread.start();

        // Instantiate Handler here. Because you instantiate it here in MainActivity, it is now
        // with the MessageQueue of the Main (UI) thread

        /**
         *
         * Once the message has been created from teh Thread that wants to send a task over
         * to the Handler to perform the task by handling the message, the following is done:
         *
         */

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // The code here is going to run on this thread, the Main(UI) thread!
                Log.v("(MainActivity)", "Thread in handleMessage(): " + Thread.currentThread().getName());
                progressBar.setProgress(msg.arg1);
                progressValue.setText(String.valueOf(msg.arg1));
            }
        };
    }

    class MyThread implements Runnable {

        // This method runs in the background

        @Override
        public void run() {

            /**
             *
             * Each time this loop is iterated, I want to tell the view (must be in
             * UI thread, to update the value of the progress. How would I do that?
             * Use a handler that will handle the task in the UI thread.
             * In order to do that, one must first create a Message object. From there, the Handler will send that message
             * to the thread it was instantiated in and perform the certain desired task.
             * In this case, the Handler object is going to send the message to the UI thread
             *
             * Note: The Handler object does both sending message AND handling the message
             *
             */

            for(int i = 0; i <= 100; i++) {

                Log.v("(MainActivity)", "Thread in run(): " + Thread.currentThread().getName());

                // Start creating the Message object here
                // NOTE: If I created this line below outside the for loop, it will crash the application
                // because the message object can only sends one message. Another Message needs to send
                // another message and so on. The Message object cannot be constantly reassigned like
                // that for delivery of the message.
                //
                Message message = Message.obtain();

                // I want to send the value of i over to the UI thread. I need to assign the message
                // argument to i. Then let the handler send it out
                message.arg1 = i;
                handler.sendMessage(message);

                // Let the Thread sleep for 100 milliseconds otherwise the progress bar will finish
                // instantly

                try {
                    Thread.sleep(50);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
