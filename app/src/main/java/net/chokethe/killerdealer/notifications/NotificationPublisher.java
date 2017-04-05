package net.chokethe.killerdealer.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import net.chokethe.killerdealer.MainActivity;
import net.chokethe.killerdealer.R;
import net.chokethe.killerdealer.holders.SessionHolder;
import net.chokethe.killerdealer.holders.SettingsHolder;

public class NotificationPublisher extends BroadcastReceiver {

    private static final int RISE_NOTIFICATION_ID = 13;
    private static final int REBUY_NOTIFICATION_ID = 14;

    public static final String RISE_NOTIFICATION_ACTION = "rise-notification-tag";
    public static final String REBUY_NOTIFICATION_ACTION = "rebuy-notification-tag";

    private static final int OPEN_PENDING_INTENT_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId;
        StringBuilder text = new StringBuilder();
        if (RISE_NOTIFICATION_ACTION.equals(intent.getAction())) {
            long now = System.currentTimeMillis();
            SessionHolder sessionHolder = new SessionHolder(context, now);
            sessionHolder.setNextBlindPos();
            sessionHolder.save(context, now);
            NotificationUtils.scheduleNotification(context, RISE_NOTIFICATION_ACTION, now, sessionHolder.getRiseTimeLeft());

            notificationId = RISE_NOTIFICATION_ID;
            text.append(context.getString(R.string.rise_done_toast));
            text.append(sessionHolder.getSmallBlind());
            text.append(" - ");
            text.append(sessionHolder.getBigBlind());
        } else {
            notificationId = REBUY_NOTIFICATION_ID;
            text.append(context.getString(R.string.rebuy_end_toast));
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setDefaults(getDefaults(context))
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private int getDefaults(Context context) {
        SettingsHolder mSettingsHolder = new SettingsHolder(context);
        if (mSettingsHolder.isVibrateOn() && mSettingsHolder.isSoundOn()) {
            return Notification.DEFAULT_ALL;
        } else if (!mSettingsHolder.isVibrateOn() && mSettingsHolder.isSoundOn()) {
            return Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;
        } else if (mSettingsHolder.isVibrateOn() && !mSettingsHolder.isSoundOn()) {
            return Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;
        } else {
            return Notification.DEFAULT_LIGHTS;
        }
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, OPEN_PENDING_INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void cancelNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(RISE_NOTIFICATION_ID);
        notificationManager.cancel(REBUY_NOTIFICATION_ID);
    }
}