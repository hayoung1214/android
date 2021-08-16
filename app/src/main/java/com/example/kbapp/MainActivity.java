//어플 틀자마자 보이는 화면 부분 코드 권환 확인해주는 기능 담김
package com.example.kbapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.View;
import android.app.NotificationChannel;

public class MainActivity extends AppCompatActivity {
//    public TextView tv_outPut = findViewById(R.id.tv_outPut); ;
    Button btn_noti =null;
    private static final String TAG4 = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkDangerousPermissions();
        btn_noti=(Button) findViewById(R.id.btn_noti);
        btn_noti.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                NotificationActivity();
            }
        });
        //NotificationActivity();
//        tv_outPut.setText("");
    }


    public void NotificationActivity() {
        //Resources res = getResources();
        //알림(Notification)을 관리하는 관리자 객체를 운영체제(Context)로부터 소환하기
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this,NotificationActivity.class);
        notificationIntent.putExtra("not_Id",9999);

        PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

//        builder.setContentTitle("setContentTitle")
//                .setContentText("setContentText")
//                .setTicker("setTicker")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                //.setLargeIcon(BitmapFactory.decodeResource(res,R.mipmap.ic_launcher))
//                .setContentIntent(contentIntent)
//                .setAutoCancel(true)
//                .setWhen(System.currentTimeMillis())
//                .setDefaults(Notification.DEFAULT_ALL);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            String channelID="channel_01";
            String channelName="MyChannel01";
            //알림채널 객체 만들기
            NotificationChannel channel= null;
            channel = new NotificationChannel(channelID,channelName, NotificationManager.IMPORTANCE_HIGH);


            //알림매니저에게 채널 객체의 생성을 요청
            notificationManager.createNotificationChannel(channel);

            //알림건축가 객체 생성
            builder=new NotificationCompat.Builder(this, channelID);

            Log.d(TAG4, "Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP");
//            builder.setCategory(Notification.CATEGORY_MESSAGE)
//                    .setPriority(Notification.PRIORITY_HIGH);
//                    //.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        //건축가에게 원하는 알림의 설정작업
        builder.setSmallIcon(android.R.drawable.ic_menu_view);
        //상태바를 드래그하여 아래로 내리면 보이는
        //알림창(확장 상태바)의 설정
        builder.setContentTitle("Title");//알림창 제목
        builder.setContentText("Messages....");//알림창 내용
        builder.setCategory(Notification.CATEGORY_MESSAGE);
        builder.setPriority(Notification.PRIORITY_HIGH);
        //builder.setVisibility(Notification.VISIBILITY_PRIVATE);
        //알림창의 큰 이미지
        //Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.gametitle_09);
        //builder.setLargeIcon(bm);//매개변수가 Bitmap을 줘야한다.

        //건축가에게 알림 객체 생성하도록
        Notification notification=builder.build();

        //알림매니저에게 알림(Notify) 요청
        notificationManager.notify(1, notification);

        //알림 요청시에 사용한 번호를 알림제거 할 수 있음.
        //notificationManager.cancel(1);

//        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        nm.notify(1234,builder.build());
    }
//    public void set_tv_outPut(TextView tv_outPut){
//        this.tv_outPut = tv_outPut;
//    }
//    public TextView get_tv_outPut(){
//        return tv_outPut;
//    }

    //위험 권한 체크
    //manifest와 java에 둘 다 권한 허가받는 코드를 작성한다.
    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.RECEIVE_SMS
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}