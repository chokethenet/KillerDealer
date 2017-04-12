package net.chokethe.killerdealer.notifications;


import android.content.Context;
import android.os.PowerManager;

public abstract class WakeLockHelper {
    private static final String APP_TAG = "KillerDealer";

    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context ctx) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, APP_TAG);
        wakeLock.acquire();
    }

    public static void release() {
        if (wakeLock != null) wakeLock.release();
        wakeLock = null;
    }
}