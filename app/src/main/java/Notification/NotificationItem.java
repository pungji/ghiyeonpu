package Notification; //알림데이터 관리

public class NotificationItem {
    public String message;
    public long timestamp;

    public NotificationItem(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

}

