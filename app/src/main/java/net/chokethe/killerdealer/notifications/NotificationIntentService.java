//package net.chokethe.killerdealer.notifications;
//
//import android.app.IntentService;
//import android.content.Context;
//import android.content.Intent;
//
//import net.chokethe.killerdealer.holders.SessionHolder;
//import net.chokethe.killerdealer.utils.PreferencesUtils;
//
//public class NotificationIntentService extends IntentService {
//
//    public static final String ACTION_STOP_NOTIFICATION = "action-dismiss-notification";
//    public static final String ACTION_OK_NOTIFICATION = "action-confirm-notification";
//
//    public NotificationIntentService() {
//        super("NotificationIntentService");
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        Context context = getApplicationContext();
//        String action = intent.getAction();
//        if (ACTION_STOP_NOTIFICATION.equals(action)) {
//            PreferencesUtils.setStatus(context, SessionHolder.Status.DEAD);
//            PreferencesUtils.setBlindPos(context, 0);
//
//        }
//        NotificationUtils.cancelScheduledNotifications(context);
//    }
//}
