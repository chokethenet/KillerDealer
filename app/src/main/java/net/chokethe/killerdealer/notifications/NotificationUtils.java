package net.chokethe.killerdealer.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import net.chokethe.killerdealer.holders.SessionHolder;

public class NotificationUtils {
    public static void scheduleNotifications(Context context, long now) {
        SessionHolder sessionHolder = new SessionHolder(context, now);
        if (sessionHolder.isPlaying()) {
            scheduleNotification(context, NotificationPublisher.RISE_NOTIFICATION_ACTION, now, sessionHolder.getRiseTimeLeft());
            scheduleNotification(context, NotificationPublisher.REBUY_NOTIFICATION_ACTION, now, sessionHolder.getRebuyTimeLeft());
        }
    }

    public static void scheduleNotification(Context context, String action, long now, long millisInFuture) {
        PendingIntent pendingIntent = getPendingIntent(context, action);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, now + millisInFuture, pendingIntent);
    }

    private static PendingIntent getPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void cancelScheduledNotifications(Context context) {
        // CANCEL ALARMS
        PendingIntent risePendingIntent = getPendingIntent(context, NotificationPublisher.RISE_NOTIFICATION_ACTION);
        PendingIntent rebuyPendingIntent = getPendingIntent(context, NotificationPublisher.REBUY_NOTIFICATION_ACTION);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(risePendingIntent);
        alarmManager.cancel(rebuyPendingIntent);

        // CANCEL NOTIFICATIONS
        NotificationPublisher.cancelNotifications(context);
    }
}
