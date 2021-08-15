package com.example.kbapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.os.AsyncTask;
import android.content.ContentValues;



import java.text.SimpleDateFormat;
import java.util.Date;

//패키지 우클릭 - New - Other -  Broadcast Receiver
public class SmsReceiver extends BroadcastReceiver {


    private static final String TAG = "MyReceiver";

    private static final String urls = "http://10.0.2.2:5000/api/v1/message/detect";

    //연-월-일 시:분:초 형태로 출력하게끔 정하는 메서드
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //문자가 오면 반드시 작동하는 메서드
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: 호출됨");

        //intent의 내용을 bundle에 넣는다.
        Bundle bundle = intent.getExtras();

        //sms 메세지가 한 개가 아니므로 배열로 만든다.
        SmsMessage[] messages = parseSmsMessage(bundle);

        //메세지 내용이 있을 경우 작동
        if(messages != null && messages.length > 0) {
            Log.d(TAG, "onReceive: SMS를 수신하였습니다");

            //보낸 사람
            String sender = messages[0].getOriginatingAddress();
            StringBuffer sender_format = new StringBuffer(sender);
            sender_format.insert(3,'-');
            sender_format.insert(8,'-');
            sender=sender_format.toString();

            Log.d(TAG, "onReceive: sender:" + sender);

            //받은 날짜
            Date receivedDate = new Date(messages[0].getTimestampMillis());
            Log.d(TAG, "onReceive: receivedDate: " + receivedDate);

            //내용
            String contents = messages[0].getMessageBody();
            Log.d(TAG, "onReceive: contents: " + contents);

            // AsyncTask를 통해 HttpURLConnection 수행.
            NetworkTask networkTask = new NetworkTask(urls, contents);
            networkTask.execute();

            //SmsDisplayActivity 화면에 띄우기
            //Flag : 속성을 부여하는 키워드
            //예시 : M화면 → A화면 → SMS메시지화면 → B화면
            //NEW_TASK : 새 화면을 띄우겠다 (SMS메시지화면)
            //CLEAR_TOP : SMS메시지 위의 화면들을 없앰 (B화면 이하 화면들)
            //SINGLE_TOP : 기존의 SMS메시지 화면이 있으면 그걸 사용하라는 뜻
            Intent displayIntent = new Intent(context, SmsDisplayActivity.class);
            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);

            //값을 추가로 보냄
            displayIntent.putExtra("sender", sender);
            displayIntent.putExtra("receivedDate", dateFormat.format(receivedDate));
            displayIntent.putExtra("contents", contents);
            context.startActivity(displayIntent);




        }
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle) {
        //pdus에 메세지가 담겨있다.
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];

        for (int i = 0; i < objs.length; i++) {
            //M버젼 (API 23 마시멜로우)이상일 때와 아닐때의 메세지 저장 형식 지정
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
            }
        }
        return messages;
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {
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

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
//            tv_outPut.setText(s); //flask 에서 모델 결과 받아온 거 보여주는 부분
            Log.d(TAG, "onPostExecute: tv_outPut: " + s);

        }
    }

}