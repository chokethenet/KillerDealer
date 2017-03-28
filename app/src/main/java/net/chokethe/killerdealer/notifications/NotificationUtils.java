package net.chokethe.killerdealer.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import net.chokethe.killerdealer.holders.SessionHolder;

public class NotificationUtils {
    public static void scheduleNotifications(Context context, SessionHolder sessionHolder) {
        if (sessionHolder.isPlaying()) {
            scheduleNotification(context, NotificationPublisher.RISE_NOTIFICATION_ACTION, sessionHolder.getRiseTimeLeft(), sessionHolder.getRiseTimePref());
            scheduleNotification(context, NotificationPublisher.REBUY_NOTIFICATION_ACTION, sessionHolder.getRebuyTimeLeft(), 0);
        }
    }

    public static void scheduleNotification(Context context, String action, long millisInFuture, long extraMillis) {
        PendingIntent pendingIntent = getPendingIntent(context, action, extraMillis);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + millisInFuture, pendingIntent);
    }

    private static PendingIntent getPendingIntent(Context context, String action, long extraMillis) {
        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.setAction(action);
        intent.putExtra(NotificationPublisher.MILLIS_IN_FUTURE, extraMillis);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void cancelScheduledNotifications(Context context) {
        // CANCEL ALARMS
        PendingIntent risePendingIntent = getPendingIntent(context, NotificationPublisher.RISE_NOTIFICATION_ACTION, 0);
        PendingIntent rebuyPendingIntent = getPendingIntent(context, NotificationPublisher.REBUY_NOTIFICATION_ACTION, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(risePendingIntent);
        alarmManager.cancel(rebuyPendingIntent);

        // CANCEL NOTIFICATIONS
        NotificationPublisher.cancelNotifications(context);
    }
}
