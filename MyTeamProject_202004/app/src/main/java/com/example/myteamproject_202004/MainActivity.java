package com.example.myteamproject_202004;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import retrofit2.Call;
import retrofit2.http.GET;


}

public class MainActivity extends AppCompatActivity {

    private String queryURL = "http://spaceweather.rra.go.kr/api/kindex";
    public static final int LOAD_SUCCESS = 101;
    Button btnStart;
    TextView textView;
    String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button) findViewById(R.id.btnStart);
        textView = (TextView) findViewById(R.id.text1);
        textView.setText("값이 없습니다.");

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "자기장 조회",Toast.LENGTH_SHORT).show();
                getJsonData();

            }
        });
    }

    private final MyHandler myHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private final WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity mainActivity) {
            weakReference = new WeakReference<MainActivity>(mainActivity);

        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity = weakReference.get();
            if (mainActivity != null) {
                switch (msg.what) {
                    case LOAD_SUCCESS:
                        String jsonString = (String) msg.obj;
                        mainActivity.textView.setText(jsonString);
                        break;
                      int time[] = new String[100];
                    for(int i = 0; i<time.length(); i++){
                        JSONObject times = time.getJSONObject(i);

                        String time = times.getString("time" | "시간");
                        String kp = times.getString("kp");
                        String kk = times.getString("kk");
                    }
                }
            }
        }
    }
   public void getJsonData(){
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            String result;

            try{
                URL url = new URL(queryURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(3000);
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setUseCaches(false);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine())!= null) {
                    sb.append(line);
                }
                bufferedReader.close();
                httpURLConnection.disconnect();
                result = sb.toString().trim();
            }catch(Exception e){
                result = e.toString();
            }

            Message message = myHandler.obtainMessage(LOAD_SUCCESS, result);
            myHandler.sendMessage(message);
        }
    });

    thread.start();
    }
}

//
//"error":false,  // 오류 여부 true면 오류
//        "lastUpdate":"1588069020000", // 최종 업데이트 시간 (밀리초)
//        "errorCode":"NOERR", // String 오류발생 시 오류 코드
//        "kindex":{   // object 관측 값 목록
//        "time":"2020-04-28 07:21:38", // String 최종 수집 시각
//        "currentP":1,  // int 현재 kp
//        "currentK":1,  // int 현재 kk
//        "max24P":3,  // int 24시간내 최대 Kp
//        "max24K":3, //  int 24시간내 최대 Kk
//        "recent":[   //array 최근 측정값 목록
//        {
//        "time":"2020-04-28 09:00:00", // String 수집 시각
//        "kp":1,  // 측정된 Kp
//        "kk":1  // 측정된 Kk
//        },
//        {
//        "time":"2020-04-28 06:00:00",
//        "kp":1,
//        "kk":1
//        },
//        {
//        "time":"2020-04-28 03:00:00",
//        "kp":3,
//        "kk":2
//        },
//        {
//        "time":"2020-04-28 00:00:00",
//        "kp":2,
//        "kk":1
//        },
//        {
//        "time":"2020-04-27 21:00:00",
//        "kp":2,
//        "kk":2
//        },
//        {
//        "time":"2020-04-27 18:00:00",
//        "kp":2,
//        "kk":2
//        },
//        {
//        "time":"2020-04-27 15:00:00",
//        "kp":2,
//        "kk":3
//        },
//        {
//        "time":"2020-04-27 12:00:00",
//        "kp":1,
//        "kk":1
//        }
//        ]
//        }
//        }



