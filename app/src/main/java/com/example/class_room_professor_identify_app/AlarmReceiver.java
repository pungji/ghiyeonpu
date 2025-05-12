package com.example.class_room_professor_identify_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String key = null;

        switch (dayOfWeek) {
            case Calendar.MONDAY: key = "monday_disabled"; break;
            case Calendar.TUESDAY: key = "tuesday_disabled"; break;
            case Calendar.WEDNESDAY: key = "wednesday_disabled"; break;
            case Calendar.THURSDAY: key = "thursday_disabled"; break;
            case Calendar.FRIDAY: key = "friday_disabled"; break;
        }

        SharedPreferences prefs = context.getSharedPreferences("AlarmPreferences", Context.MODE_PRIVATE);
        if (key != null && prefs.getBoolean(key, false)) {
            return; // 꺼진 요일이면 알림 무시
        }

        NotificationHelper.showNotification(context, "알림", "즐겨찾기 교수 수업 알람입니다!");
    }
}

