package Notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.class_room_professor_identify_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationHelper {

    private static final String CHANNEL_ID = "alarm_channel";
    private static final int NOTIFICATION_ID = 1001;

    // 외부에서 호출하는 메소드
    public static void notifyIfPermitted(Context context) {
        // Android 13 이상이면 권한 확인 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없으면 사용자에게 알려만 주고 종료
                Toast.makeText(context, "알림 권한이 필요합니다. 설정에서 권한을 허용해주세요.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // 권한이 있거나 API 32 이하면 알림 표시
        showNotification(context, "알림", "즐겨찾기 교수님의 수업 알람입니다!");
    }

    public static void showNotification(Context context, String title, String message) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 채널 생성 (API 26 이상)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "수업 알림 채널",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        manager.notify(1001, builder.build());


    }
    // NotificationHelper.java 내부에 추가할 코드
    public static void saveAndNotify(Context context, String professorName, String room) {
        // 알림 메시지 생성
        String message = professorName + " 교수님이 " + room + " 강의실에 입장하셨습니다.";

        // 알림 생성 및 띄우기
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "alarm_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "수업 알림 채널", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("알림")
                .setContentText(message)
                .setAutoCancel(true);

        manager.notify(1001, builder.build());

        // 알림 데이터 저장 (SharedPreferences 활용 예시)
        SharedPreferences prefs = context.getSharedPreferences("notifications", Context.MODE_PRIVATE);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(prefs.getString("data", "[]"));
            JSONObject newObj = new JSONObject();
            newObj.put("professorName", professorName);
            newObj.put("room", room);
            newObj.put("timestamp", System.currentTimeMillis());

            jsonArray.put(newObj);
            prefs.edit().putString("data", jsonArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
