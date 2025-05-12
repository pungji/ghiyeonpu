package com.example.class_room_professor_identify_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

//알림 기록 보기
public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_history);

        Button btnHome = findViewById(R.id.homeButton);
        btnHome.setOnClickListener(v -> {

            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
        });


    }
}