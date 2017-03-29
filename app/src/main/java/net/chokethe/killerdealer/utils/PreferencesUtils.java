package net.chokethe.killerdealer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import net.chokethe.killerdealer.R;
import net.chokethe.killerdealer.holders.SessionHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PreferencesUtils {
    private static final Integer[] DEFAULT_BLINDS_LIST = {10, 25, 50, 75};
    private static final long DEFAULT_RISE_TIME = 1200000;
    private static final long DEFAULT_REBUY_TIME = 10800000;
    private static final int DEFAULT_STATUS = SessionHolder.Status.DEAD.getId();
    private static final long DEFAULT_LAST_PLAY_TIME = 0;
    private static final int DEFAULT_BLINDS_POS = 0;
    private static final int DEFAULT_MULTIPLIER = 10;
    private static final int MAX_BLIND = 99999999;

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

    public static List<Integer> getFullBlindsList(Context context) {
        List<Integer> blindsList = getRawBlindsList(context);
        blindsList.addAll(getGeneratedNextBlinds(blindsList));
        return blindsList;
    }

    public static List<Integer> getRawBlindsList(Context context) {
        List<Integer> blindsList = new ArrayList<>();
        int blind;
        for (int i = 0; i < 20; i++) {
            blind = getSharedPreferences(context)
                    .getInt(context.getString(R.string.blind_pref) + i, 0);
            if (blind != 0) {
                blindsList.add(blind);
            }
        }
        generateDefaultBlinds(blindsList);
        return blindsList;
    }

    public static void generateDefaultBlinds(List<Integer> blindsList) {
        if (blindsList.isEmpty()) {
            blindsList.addAll(new ArrayList<>(Arrays.asList(DEFAULT_BLINDS_LIST)));
        }
    }

    public static List<Integer> getGeneratedNextBlinds(List<Integer> blindsList) {
        List<Integer> generatedBlindsList = new ArrayList<>();
        int pos = 0;

        int blindsListSize = blindsList.size();
        int generator = DEFAULT_MULTIPLIER;
        while (blindsList.get(0) * generator < blindsList.get(blindsListSize - 1)) {
            generator = generator * DEFAULT_MULTIPLIER;
        }
        if (blindsList.get(0) * generator == blindsList.get(blindsListSize - 1)) {
            pos++;
        }

        int missing = 100 - blindsListSize;
        for (; pos < missing; pos++) {
            int generatedBlind;
            if (pos < blindsListSize) {
                generatedBlind = blindsList.get(pos) * generator;
            } else {
                generatedBlind = generatedBlindsList.get(pos - blindsListSize) * generator;
            }
            if (generatedBlind > MAX_BLIND) {
                break;
            }
            generatedBlindsList.add(generatedBlind);
        }
        return generatedBlindsList;
    }

    public static void setBlindsList(Context context, List<Integer> blindsList) {
        Collections.sort(blindsList);
        int blindsSize = blindsList.size();
        for (int i = 0; i < blindsSize; i++) {
            getSharedPreferencesEditor(context)
                    .putInt(context.getString(R.string.blind_pref) + i, blindsList.get(i));
        }
        for (int i = blindsSize; i < 20; i++) {
            getSharedPreferencesEditor(context)
                    .putInt(context.getString(R.string.blind_pref) + i, 0);
        }
        getSharedPreferencesEditor(context).commit();
    }

    public static long getRiseTime(Context context) {
        return getLongValue(context, R.string.rise_timer_pref, DEFAULT_RISE_TIME);
    }

    public static void setRiseTime(Context context, long riseTime) {
        setLongValue(context, R.string.rise_timer_pref, riseTime);
    }

    public static long getRebuyTime(Context context) {
        return getLongValue(context, R.string.rebuy_timer_pref, DEFAULT_REBUY_TIME);
    }

    public static void setRebuyTime(Context context, long rebuyTime) {
        setLongValue(context, R.string.rebuy_timer_pref, rebuyTime);
    }

    public static long getPausedRiseTime(Context context) {
        return getLongValue(context, R.string.pause_rise_time_pref, DEFAULT_RISE_TIME);
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
}
