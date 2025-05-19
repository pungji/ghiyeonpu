package com.example.class_room_professor_identify_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import java.util.Calendar;

public class AlertActivity extends AppCompatActivity {

    private CheckBox cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday;
    private Button saveButton;

    private final String PREF_NAME = "AlarmPreferences";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001
                );
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_config);

        // 체크박스 연결
        cbMonday = findViewById(R.id.checkbox_monday);
        cbTuesday = findViewById(R.id.checkbox_tuesday);
        cbWednesday = findViewById(R.id.checkbox_wednesday);
        cbThursday = findViewById(R.id.checkbox_thursday);
        cbFriday = findViewById(R.id.checkbox_friday);

        saveButton = findViewById(R.id.saveButton);

        // 저장된 값 불러오기
        loadPreferences();

        // 저장 버튼 클릭 시
        saveButton.setOnClickListener(v -> {
            savePreferences();
            Toast.makeText(this, "설정이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        });

        Button btnHome = findViewById(R.id.homeButton);//홈 화면 이동
        btnHome.setOnClickListener( v -> {
            Intent intent = new Intent(AlertActivity.this, MainActivity.class);
            startActivity(intent);
        });



    }

    private void savePreferences() { // 설정 값 저장
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("monday_disabled", cbMonday.isChecked());
        editor.putBoolean("tuesday_disabled", cbTuesday.isChecked());
        editor.putBoolean("wednesday_disabled", cbWednesday.isChecked());
        editor.putBoolean("thursday_disabled", cbThursday.isChecked());
        editor.putBoolean("friday_disabled", cbFriday.isChecked());
        
        editor.apply();
    }

    private void loadPreferences() { // 설정 값 불러오기
        SharedPreferences prefs = getSharedPreferences("AlarmPreferences", MODE_PRIVATE);

        cbMonday.setChecked(prefs.getBoolean("monday_disabled", false));
        cbTuesday.setChecked(prefs.getBoolean("tuesday_disabled", false));
        cbWednesday.setChecked(prefs.getBoolean("wednesday_disabled", false));
        cbThursday.setChecked(prefs.getBoolean("thursday_disabled", false));
        cbFriday.setChecked(prefs.getBoolean("friday_disabled", false));
    }
    private void setDailyAlarm() { //알람
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7); // 오전 7시
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

}
