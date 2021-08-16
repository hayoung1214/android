//sms 받아온거 보여주는 파란 화면부분 코드, 여기서 받아온거 보여주는 대신 모델 돌리고 결과 보여주기 부분 추가하면 됨
package com.example.kbapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class SmsDisplayActivity extends AppCompatActivity {
    //객체 선언
    Button btnTitle, btnClose;
    TextView tvMsg;
    public static TextView tv_outPut ;

    private static final String TAG3 = "SmsDisplayActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_display);

        //객체 초기화
        tvMsg = findViewById(R.id.tvMsg);
        btnTitle = findViewById(R.id.btnTitle);
        btnClose = findViewById(R.id.btnClose);
        tv_outPut = findViewById(R.id.tv_outPut);

        //btnClose 기능 추가 : 창 닫기
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //onCreate에 있으면 새로 받은 문자가 갱신이 안된다.
        //인텐트 받기
        Intent displayIntent = getIntent();
        processIntent(displayIntent);
    }

    //새 문자를 받을때(이미 창이 만들어져 있어서 onCreate가 작동을 안할 때, 새 Intent를 받을 때) 작동
    //매개 변수에는 자동으로 갱신되는 인텐트가 들어간다.
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    //인텐트를 받아서 내용을 TextView에 출력하는 메서드
    private void processIntent(Intent displayIntent) {
        String sender = displayIntent.getStringExtra("sender");
        String receivedDate = displayIntent.getStringExtra("receivedDate");
        String contents = displayIntent.getStringExtra("contents");
        String s = displayIntent.getStringExtra("s");

        //보낸 사람이 있으면
        if(sender != null) {
            btnTitle.setText("발신자 번호 : "+ sender );
            tvMsg.setText("[" + receivedDate + "]\n" + contents);

        }
    }

    public static class NetworkTask extends AsyncTask<Void, Void, String> {
        private String url;
        private String values;

        public NetworkTask(String url, String values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String result_message="";
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
//            tv_outPut.setText(s); //flask 에서 모델 결과 받아온 거 보여주는 부분


            try {
                JSONObject jsonObj = new JSONObject(s);
                result_message = jsonObj.getString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.d(TAG3, "onPostExecute: tv_outPut: " + result_message);
            tv_outPut.setText(result_message);


        }
    }
}