package com.example.flyme.asynctaskdemo;

import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private Button btn_begin;
    private Button btn_stop;
    private ProgressBar progressBar;
    private TextView textView;
    private static final String TAG = "INFORMATION";

    private AutoTask myTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.init(TAG);
        btn_begin = (Button) findViewById(R.id.btn_async);
        btn_stop  = (Button) findViewById(R.id.btn_stop);
        progressBar = (ProgressBar) findViewById(R.id.pb_aysnc);
        textView = (TextView) findViewById(R.id.tv_result);
        btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //每次需要new一个实例，新建的任务只能执行一次，否则会出现异常
                myTask = new AutoTask();
                myTask.execute();
                btn_begin.setEnabled(false);
                btn_stop.setEnabled(true);
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTask.cancel(true);
                myTask.onCancelled();
            }
        });

    }
    private class AutoTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {
            //1.executed at once after execute() method executed.
            super.onPreExecute();
            Logger.d("onPreExecute() called");
            textView.setText("Prepair");
        }

        @Override
        protected String doInBackground(String... params) {
            //2.executed after onPreExecute(),to do something wasting time.receive params and return result.
            Logger.d("doInBackground() called");
            //volley 同步请求
            RequestFuture<String> future = RequestFuture.newFuture();
            StringRequest request =  new StringRequest("http://www.baidu.com",future,future);
            RequestQueue queue = AsynctaskApplication.getQueue();
            queue.add(request);
            try {
                String result = future.get();
                for(int i = 0;i<=20;i++){
                    publishProgress(i*5);
                    Thread.sleep(500);
                }
                return result;
            } catch (InterruptedException e) {
                e.printStackTrace();
                Logger.d(e.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return "";

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            //3.executed after publishProgress(),current information will update on UI components.
            Logger.d("onProgressUpdate() called");
            progressBar.setProgress(values[0]);
            textView.setText("loading..."+values[0]+"%");
        }

        @Override
        protected void onPostExecute(String result) {
            //4.while doInbackground() done , result will post to this method and will show on UI components.
            Logger.d("onPostExecute() called");
            textView.setText(result);
            btn_begin.setEnabled(true);
            btn_stop.setEnabled(false);
        }


        @Override
        protected void onCancelled() {
            Logger.d("onCancelled() called");
            textView.setText("cancelled");
            progressBar.setProgress(0);
            btn_stop.setEnabled(false);
            btn_begin.setEnabled(true);
        }
    }
}
