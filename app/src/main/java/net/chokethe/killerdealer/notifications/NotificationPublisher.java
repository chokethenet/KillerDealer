package net.chokethe.killerdealer.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import net.chokethe.killerdealer.MainActivity;
import net.chokethe.killerdealer.R;

public class NotificationPublisher extends BroadcastReceiver {

    private static final int RISE_NOTIFICATION_ID = 13;
    private static final int REBUY_NOTIFICATION_ID = 14;

    public static final String RISE_NOTIFICATION_ACTION = "rise-notification-tag";
    public static final String REBUY_NOTIFICATION_ACTION = "rebuy-notification-tag";
    public static final String MILLIS_IN_FUTURE = "millis-in-future";

    private static final int OPEN_PENDING_INTENT_ID = 0;
//    private static final int STOP_PENDING_INTENT_ID = 0;
//    private static final int OK_PENDING_INTENT_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId;
        String text;
        if (RISE_NOTIFICATION_ACTION.equals(intent.getAction())) {
            notificationId = RISE_NOTIFICATION_ID;
            long millisInFuture = intent.getLongExtra(MILLIS_IN_FUTURE, 0);
            if (millisInFuture != 0) {
                NotificationUtils.scheduleNotification(context, RISE_NOTIFICATION_ACTION, millisInFuture, millisInFuture);
            }
            text = context.getString(R.string.rise_done_toast);
        } else {
            notificationId = REBUY_NOTIFICATION_ID;
            text = context.getString(R.string.rebuy_end_toast);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(contentIntent(context))
//                .addAction(stopAction(context))
//                .addAction(okAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, OPEN_PENDING_INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

//    private static NotificationCompat.Action stopAction(Context context) {
//        return new NotificationCompat.Action(R.drawable.ic_stop, null,
//                getNotificationPendingIntent(context, NotificationIntentService.ACTION_STOP_NOTIFICATION, STOP_PENDING_INTENT_ID));
//    }

//    private static NotificationCompat.Action okAction(Context context) {
//        return new NotificationCompat.Action(R.drawable.ic_done, null,
//                getNotificationPendingIntent(context, NotificationIntentService.ACTION_OK_NOTIFICATION, OK_PENDING_INTENT_ID));
//    }

//    private static PendingIntent getNotificationPendingIntent(Context context, String action, int id) {
//        Intent okIntent = new Intent(context, NotificationIntentService.class);
//        okIntent.setAction(action);
//        return PendingIntent.getService(context, id, okIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }

    public static void cancelNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(RISE_NOTIFICATION_ID);
        notificationManager.cancel(REBUY_NOTIFICATION_ID);
    }
}