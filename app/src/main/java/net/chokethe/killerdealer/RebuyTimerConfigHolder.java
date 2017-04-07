package net.chokethe.killerdealer;

import android.content.Context;

import net.chokethe.killerdealer.utils.PreferencesUtils;
import net.chokethe.killerdealer.utils.TimeUtils;

public class RebuyTimerConfigHolder {

    private long rebuyTimePref;

    public RebuyTimerConfigHolder(Context context) {
        rebuyTimePref = PreferencesUtils.getRebuyTime(context);
    }

    public void saveRebuyTimePref(Context context, int hours, int minutes) {
        this.rebuyTimePref = TimeUtils.getMinsInMillis(minutes) + TimeUtils.getHoursInMillis(hours);
        PreferencesUtils.setRebuyTime(context, rebuyTimePref);
    }

    public String getRebuyStringHours() {
        return TimeUtils.getStringHours(rebuyTimePref);
    }

    public int getRebuyHours() {
        return TimeUtils.getHours(rebuyTimePref);
    }

    public String getRebuyStringMinutes() {
        return TimeUtils.getStringMins(rebuyTimePref);
    }
    public int getRebuyMinutes() {
        return TimeUtils.getMins(rebuyTimePref);
    }
}
