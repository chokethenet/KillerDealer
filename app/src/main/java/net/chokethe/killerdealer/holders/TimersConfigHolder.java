package net.chokethe.killerdealer.holders;

import android.content.Context;

import net.chokethe.killerdealer.utils.PreferencesUtils;

public class TimersConfigHolder {

    private long riseTimePref;
    private long rebuyTimePref;

    public TimersConfigHolder(Context context) {
        riseTimePref = PreferencesUtils.getRiseTime(context);
        rebuyTimePref = PreferencesUtils.getRebuyTime(context);
    }

    public void save(Context context) {
        PreferencesUtils.setRiseTime(context, riseTimePref);
        PreferencesUtils.setRebuyTime(context, rebuyTimePref);
    }

    public long getRiseTimePref() {
        return riseTimePref;
    }

    public void setRiseTimePref(long riseTimePref) {
        this.riseTimePref = riseTimePref;
    }

    public long getRebuyTimePref() {
        return rebuyTimePref;
    }

    public void setRebuyTimePref(long rebuyTimePref) {
        this.rebuyTimePref = rebuyTimePref;
    }
}
