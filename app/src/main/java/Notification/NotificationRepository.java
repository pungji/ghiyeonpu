package Notification; //알림데이터 저장

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {
    private static final String PREFS_NAME = "notifications";
    private SharedPreferences prefs;

    public NotificationRepository(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void addNotification(NotificationItem item) {
        JSONArray jsonArray = getNotificationsArray();
        JSONObject obj = new JSONObject();
        try {
            obj.put("message", item.message);
            obj.put("timestamp", item.timestamp);
            jsonArray.put(obj);
            prefs.edit().putString("data", jsonArray.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<NotificationItem> getNotifications() {
        List<NotificationItem> items = new ArrayList<>();
        JSONArray jsonArray = getNotificationsArray();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                items.add(new NotificationItem(
                        obj.getString("message"),
                        obj.getLong("timestamp")
                ));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    private JSONArray getNotificationsArray() {
        String data = prefs.getString("data", "[]");
        try {
            return new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
