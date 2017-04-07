package net.chokethe.killerdealer;

import android.content.Context;

import net.chokethe.killerdealer.utils.PreferencesUtils;
import net.chokethe.killerdealer.utils.TimeUtils;

public class RebuyTimerConfigHolder {

    private long rebuyTimePref;

    public RebuyTimerConfigHolder(Context context) {
        rebuyTimePref = PreferencesUtils.getRebuyTime(context);
    }

    public void save(Context context) {
        PreferencesUtils.setRebuyTime(context, rebuyTimePref);
    }

    public long getRebuyTimePref() {
        return rebuyTimePref;
    }

    public void setRebuyTimePref(long rebuyTimePref) {
        this.rebuyTimePref = rebuyTimePref;
    }

    public String getRebuyHours() {
        return TimeUtils.getStringHours(rebuyTimePref);
    }

    public String getRebuyMinutes() {
        return TimeUtils.getStringMins(rebuyTimePref);
    }
}
