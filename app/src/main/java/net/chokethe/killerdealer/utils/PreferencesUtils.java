package net.chokethe.killerdealer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import net.chokethe.killerdealer.R;
import net.chokethe.killerdealer.SessionHolder;

public class PreferencesUtils {
    private static final long DEFAULT_REBUY_TIME = 10800000;
    private static final int DEFAULT_STATUS = SessionHolder.Status.DEAD.getId();
    private static final long DEFAULT_LAST_PLAY_TIME = 0;
    public static final int DEFAULT_BLINDS_POS = 0;

    public static final boolean DEFAULT_RISE_RESET = false;
    public static final boolean DEFAULT_SCREEN_ON = false;
    public static final boolean DEFAULT_VIBRATE_ON = true;
    public static final boolean DEFAULT_SOUND_ON = true;
    public static final boolean DEFAULT_TOAST_ON = true;

    private PreferencesUtils() {
    }

    private static SharedPreferences sharedPreferences;
    private static Editor editor;

    // Static objects
    private static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sharedPreferences;
    }

    private static Editor getSharedPreferencesEditor(Context context) {
        if (editor == null) {
            editor = getSharedPreferences(context).edit();
        }
        return editor;
    }

    // Helper methods
    private static int getIntValue(Context context, int id, int def) {
        return getSharedPreferences(context).getInt(context.getString(id), def);
    }

    private static void setIntValue(Context context, int id, int value) {
        getSharedPreferencesEditor(context).putInt(context.getString(id), value);
        getSharedPreferencesEditor(context).commit();
    }

    private static long getLongValue(Context context, int id, long def) {
        return getSharedPreferences(context).getLong(context.getString(id), def);
    }

    private static void setLongValue(Context context, int id, long value) {
        getSharedPreferencesEditor(context).putLong(context.getString(id), value);
        getSharedPreferencesEditor(context).commit();
    }

    private static boolean getBoolValue(Context context, int id, boolean def) {
        return getSharedPreferences(context).getBoolean(context.getString(id), def);
    }

    // Prefs methods
    public static SessionHolder.Status getStatus(Context context) {
        return SessionHolder.Status.getById(getIntValue(context, R.string.status_pref, DEFAULT_STATUS));
    }

    public static void setStatus(Context context, SessionHolder.Status status) {
        setIntValue(context, R.string.status_pref, status.getId());
    }

    public static long getLastPlayTime(Context context) {
        return getLongValue(context, R.string.last_play_time_pref, DEFAULT_LAST_PLAY_TIME);
    }

    public static void setLastPlayTime(Context context, long lastPlayTime) {
        setLongValue(context, R.string.last_play_time_pref, lastPlayTime);
    }

    public static long getRebuyTime(Context context) {
        return getLongValue(context, R.string.rebuy_timer_pref, DEFAULT_REBUY_TIME);
    }

    public static void setRebuyTime(Context context, long rebuyTime) {
        setLongValue(context, R.string.rebuy_timer_pref, rebuyTime);
    }

    public static long getPausedRiseTime(Context context, long defaultRiseTime) {
        return getLongValue(context, R.string.pause_rise_time_pref, defaultRiseTime);
    }

    public static void setPausedRiseTime(Context context, long pausedRiseTime) {
        setLongValue(context, R.string.pause_rise_time_pref, pausedRiseTime);
    }

    public static long getPausedRebuyTime(Context context) {
        return getLongValue(context, R.string.pause_rebuy_time_pref, DEFAULT_REBUY_TIME);
    }

    public static void setPausedRebuyTime(Context context, long pausedRebuyTime) {
        setLongValue(context, R.string.pause_rebuy_time_pref, pausedRebuyTime);
    }

    public static int getBlindPos(Context context) {
        return getIntValue(context, R.string.blind_pos_pref, DEFAULT_BLINDS_POS);
    }

    public static void setBlindPos(Context context, int pos) {
        setIntValue(context, R.string.blind_pos_pref, pos);
    }

    public static boolean isRiseReset(Context context) {
        return getBoolValue(context, R.string.blinds_reset_key, DEFAULT_RISE_RESET);
    }

    public static boolean isScreenLocked(Context context) {
        return getBoolValue(context, R.string.general_screen_key, DEFAULT_SCREEN_ON);
    }

    public static boolean isVibrateOn(Context context) {
        return getBoolValue(context, R.string.notif_vibrate_key, DEFAULT_VIBRATE_ON);
    }

    public static boolean isSoundOn(Context context) {
        return getBoolValue(context, R.string.notif_sound_key, DEFAULT_SOUND_ON);
    }

    public static boolean isToastOn(Context context) {
        return getBoolValue(context, R.string.notif_toast_key, DEFAULT_TOAST_ON);
    }
}
