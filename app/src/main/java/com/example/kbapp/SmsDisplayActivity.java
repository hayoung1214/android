//sms 받아온거 보여주는 파란 화면부분 코드, 여기서 받아온거 보여주는 대신 모델 돌리고 결과 보여주기 부분 추가하면 됨
package com.example.kbapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SmsDisplayActivity extends AppCompatActivity {
    //객체 선언
    Button btnTitle, btnClose;
    TextView tvMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_display);

        //객체 초기화
        tvMsg = findViewById(R.id.tvMsg);
        btnTitle = findViewById(R.id.btnTitle);
        btnClose = findViewById(R.id.btnClose);

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

        //보낸 사람이 있으면
        if(sender != null) {
            btnTitle.setText("발신자 번호 : "+ sender );
            tvMsg.setText("[" + receivedDate + "]\n" + contents);
        }
    }
}