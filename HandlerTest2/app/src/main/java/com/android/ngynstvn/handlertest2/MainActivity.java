package com.android.ngynstvn.handlertest2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    /**
     *
     * Lesson on using Handlers to do UI thread to background thread communication!
     * Previous tutorial did it from background to UI thread
     *
     */

    private Button sendMessageBtn;
    private MyThread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendMessageBtn = (Button) findViewById(R.id.btn_send_message);

        myThread = new MyThread();
        myThread.start();

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("(MainActivity)", "Thread in onClick(): " + Thread.currentThread().getName());

                // To send a message to the background thread, do the following:

                myThread.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // This thread is being run in the Thread that the Handler was assigned to!
                        // In this case, it is from MyThread
                        Log.v("(MainActivity)", "Thread in onClick()'s run(): " + Thread.currentThread().getName());
                    }
                });
            }
        });
    }

    class MyThread extends Thread {

        /**
         *
         * NOTE: The class MyThread is on the Main Thread! When the run() method is executed,
         * it is executed in the Background Thread! Instantiate the handler inside run() to
         * ensure it belongs to the background thread
         *
         */

        // Ensure that this class has its own MessageQueue for communication. We are switching
        // the order of threads here and this is the thread that will have a queue and handle
        // the messages

        Handler handler;

        @Override
        public void run() {
            Log.v("(MainActivity)", "Thread in run(): " + Thread.currentThread().getName());
            // Make this MyThread capable of accepting multiple messages in terms of a MessageQueue

            // Prepare a Looper for this class. The looper is initialized to this background thread
            // Call loop() method after prepare()

            Looper.prepare();

            // Instantiate Handler here to ensure it runs in the background thread. NOT OUTSIDE run()

            handler = new Handler();

            Looper.loop(); // This runs the MessageQueue in this thread
        }
    }
}
