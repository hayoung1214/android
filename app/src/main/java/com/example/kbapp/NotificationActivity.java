//package com.example.kbapp;
//
//import android.app.Activity;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.os.Bundle;
//import android.widget.TextView;
//
//public class NotificationActivity extends Activity{
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sms_display);
//        CharSequence msg="MainActivity에서 전달 받은 값";
//        int id=0;
//
//        Bundle extras = getIntent().getExtras();
//        if(extras ==null){
//            msg="error";
//
//        }
//        else{
//            id=extras.getInt("noti_Id");
//
//        }
//
//        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        nm.cancel(id);
//    }
//}
