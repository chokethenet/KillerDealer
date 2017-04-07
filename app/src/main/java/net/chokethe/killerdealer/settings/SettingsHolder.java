package net.chokethe.killerdealer.settings;

import android.content.Context;
import android.content.SharedPreferences;

import net.chokethe.killerdealer.R;
import net.chokethe.killerdealer.utils.PreferencesUtils;

public class SettingsHolder {

    private boolean isRiseReset;
    private boolean isScreenLocked;
    private boolean isVibrateOn;
    private boolean isSoundOn;
    private boolean isToastOn;

    public SettingsHolder(Context context) {
        isRiseReset = PreferencesUtils.isRiseReset(context);
        isScreenLocked = PreferencesUtils.isScreenLocked(context);
        isVibrateOn = PreferencesUtils.isVibrateOn(context);
        isSoundOn = PreferencesUtils.isSoundOn(context);
        isToastOn = PreferencesUtils.isToastOn(context);
    }

    public boolean isRiseReset() {
        return isRiseReset;
    }

    public boolean isScreenLocked() {
        return isScreenLocked;
    }

    public boolean isVibrateOn() {
        return isVibrateOn;
    }

    public boolean isSoundOn() {
        return isSoundOn;
    }

    public boolean isToastOn() {
        return isToastOn;
    }

    public void updateHolder(Context context, SharedPreferences sharedPreferences, String key) {
        if (key.equals(context.getString(R.string.blinds_reset_key))) {
            isRiseReset = sharedPreferences.getBoolean(key, PreferencesUtils.DEFAULT_RISE_RESET);
        } else if (key.equals(context.getString(R.string.general_screen_key))) {
            isScreenLocked = sharedPreferences.getBoolean(key, PreferencesUtils.DEFAULT_SCREEN_ON);
        } else if (key.equals(context.getString(R.string.notif_vibrate_key))) {
            isVibrateOn = sharedPreferences.getBoolean(key, PreferencesUtils.DEFAULT_VIBRATE_ON);
        } else if (key.equals(context.getString(R.string.notif_sound_key))) {
            isSoundOn = sharedPreferences.getBoolean(key, PreferencesUtils.DEFAULT_SOUND_ON);
        } else if (key.equals(context.getString(R.string.notif_toast_key))) {
            isToastOn = sharedPreferences.getBoolean(key, PreferencesUtils.DEFAULT_TOAST_ON);
        }
    }
}
