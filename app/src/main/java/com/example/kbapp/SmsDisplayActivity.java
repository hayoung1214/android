//sms 받아온거 보여주는 파란 화면부분 코드, 여기서 받아온거 보여주는 대신 모델 돌리고 결과 보여주기 부분 추가하면 됨
package com.example.kbapp;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.PendingIntent;

import org.json.JSONException;
import org.json.JSONObject;

public class SmsDisplayActivity extends AppCompatActivity {
    //객체 선언
    Button btnTitle, btnClose;
    TextView tvMsg;
    public static TextView tv_outPut ;
    public static PendingIntent pendingIntent;
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
        pendingIntent = PendingIntent.getActivity(this, 0, displayIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d(TAG3, "onCreate pendingIntent : "+pendingIntent);

    }

    public static void NotificationActivity(Context ctx,String str, String sender) {

        //Resources res = getResources();
        //알림(Notification)을 관리하는 관리자 객체를 운영체제(Context)로부터 소환하기
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        //Intent notificationIntent = new Intent(this,NotificationActivity.class);
        //notificationIntent.putExtra("not_Id",9999);

        //PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        String channelID = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelID = "channel_01";
            String channelName = "MyChannel01";
            //알림채널 객체 만들기
            NotificationChannel channel = null;
            channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);


            //알림매니저에게 채널 객체의 생성을 요청
            notificationManager.createNotificationChannel(channel);

            //알림건축가 객체 생성

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, channelID);

        Log.d(TAG3, "Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP");
        //건축가에게 원하는 알림의 설정작업
        builder.setSmallIcon(android.R.drawable.ic_menu_view);
        //상태바를 드래그하여 아래로 내리면 보이는
        //알림창(확장 상태바)의 설정
        builder.setTicker("setTicker");
        builder.setContentTitle("KaptureBee");//알림창 제목
        //builder.setContentText(str+"의심. 보이스피싱을 주의하세요!");//알림창 내용
        builder.setCategory(Notification.CATEGORY_MESSAGE);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);
        Log.d(TAG3, "NotificationActivity pendingIntent : " + pendingIntent);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setSummaryText(sender+" 로부터 수신된 메세지" );

        //bigTextStyle.setBigContentTitle("KaptureBee");

        bigTextStyle.bigText(str+"이 의심됩니다. 보이스 피싱을 주의하세요!");
        builder.setStyle(bigTextStyle);
        //builder.setVisibility(Notification.VISIBILITY_PRIVATE);
        //알림창의 큰 이미지
        Bitmap bm= BitmapFactory.decodeResource(ctx.getResources(),R.drawable.bee);
        builder.setLargeIcon(bm);//매개변수가 Bitmap을 줘야한다.

        //건축가에게 알림 객체 생성하도록
        Notification notification = builder.build();

        //알림매니저에게 알림(Notify) 요청
        notificationManager.notify(1, notification);

        //알림 요청시에 사용한 번호를 알림제거 할 수 있음.
        //notificationManager.cancel(1);

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
        String result_message = displayIntent.getStringExtra("result_message");

        //보낸 사람이 있으면
        if(sender != null) {
            btnTitle.setText("발신자 번호 : "+ sender );
            tvMsg.setText("[" + receivedDate + "]\n" + contents);
            tv_outPut.setText("검출 결과 : " + "로딩중입니다...");
            if(result_message !=null){
                tv_outPut.setText("검출 결과 : " + result_message);
            }
        }


    }


}