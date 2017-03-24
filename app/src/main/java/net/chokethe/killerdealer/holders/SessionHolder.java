package net.chokethe.killerdealer.holders;

import android.content.Context;

import net.chokethe.killerdealer.utils.PreferencesUtils;
import net.chokethe.killerdealer.utils.TimeUtils;

import java.util.List;

public class SessionHolder {

    public enum Status {
        DEAD(0), IDLE(1), ALIVE(2);

        private int id;

        Status(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Status getById(int id) {
            for (Status e : values()) {
                if (e.id == id) return e;
            }
            return null;
        }
    }

    private Status status;
    private long lastPlayTime;

    private List<Integer> blindsListPref;
    private int blindPos;

    private long riseTimePref;
    private long riseTimeLeft;
    private long rebuyTimePref;
    private long rebuyTimeLeft;

    public SessionHolder(Context context) {
        long now = System.currentTimeMillis();

        status = PreferencesUtils.getStatus(context);
        lastPlayTime = PreferencesUtils.getLastPlayTime(context);

        riseTimePref = PreferencesUtils.getRiseTime(context);
        rebuyTimePref = PreferencesUtils.getRebuyTime(context);
        riseTimeLeft = getRiseTimeLeftBySTatus(context, now);
        rebuyTimeLeft = getRebuyTimeLeftByStatus(context, now);

        blindsListPref = PreferencesUtils.getFullBlindsList(context);
        blindPos = getBlindPosByStatus(context, now);
    }

    private long getRiseTimeLeftBySTatus(Context context, long now) {
        long pausedRiseTime = PreferencesUtils.getPausedRiseTime(context);
        long totalElapsedMillis = TimeUtils.getTotalElapsedMillis(lastPlayTime, pausedRiseTime, riseTimePref, now);
        return getTimeLeftByStatus(totalElapsedMillis, riseTimePref, pausedRiseTime);
    }

    private long getRebuyTimeLeftByStatus(Context context, long now) {
        long pausedRebuyTime = PreferencesUtils.getPausedRebuyTime(context);
        long totalElapsedMillis = TimeUtils.getTotalElapsedMillis(lastPlayTime, pausedRebuyTime, rebuyTimePref, now);
        if (isPlaying() && totalElapsedMillis >= rebuyTimePref) {
            return 0;
        } else {
            return getTimeLeftByStatus(totalElapsedMillis, rebuyTimePref, pausedRebuyTime);
        }
    }

    private long getTimeLeftByStatus(long totalElapsedMillis, long timePref, long timeLeft) {
        long millisLeft;
        if (isPlaying()) {
            long lastPeriodMillis = totalElapsedMillis % timePref;
            millisLeft = timePref - lastPeriodMillis;
        } else if (isPaused()) {
            millisLeft = timeLeft;
        } else {
            millisLeft = timePref;
        }
        return millisLeft;
    }

    private int getBlindPosByStatus(Context context, long now) {
        int calculatedPos = PreferencesUtils.getBlindPos(context);
        if (isPlaying()) {
            long elapsedSteps = TimeUtils.getTotalElapsedMillis(lastPlayTime, riseTimeLeft, riseTimePref, now) / riseTimePref;
            calculatedPos += elapsedSteps;
            if (calculatedPos + 1 >= blindsListPref.size()) {
                calculatedPos = 0;
            }
        }
        return calculatedPos;
    }

    public void save(Context context) {
        PreferencesUtils.setStatus(context, status);
        PreferencesUtils.setBlindPos(context, blindPos);
        if (isPlaying()) {
            PreferencesUtils.setLastPlayTime(context, System.currentTimeMillis());
        }
        if (!isStopped()) {
            PreferencesUtils.setPausedRiseTime(context, riseTimeLeft);
            PreferencesUtils.setPausedRebuyTime(context, rebuyTimeLeft);
        }
    }

    public void reset() {
        setStopped();
        blindPos = 0;
        resetRiseTimeLeft();
        resetRebuyTimeLeft();
    }

    public void resetRiseTimeLeft() {
        setRiseTimeLeft(riseTimePref);
    }

    public void resetRebuyTimeLeft() {
        setRebuyTimeLeft(rebuyTimePref);
    }

    public boolean isStopped() {
        return status.equals(Status.DEAD);
    }

    public void setStopped() {
        status = Status.DEAD;
    }

    public boolean isPaused() {
        return status.equals(Status.IDLE);
    }

    public void setPaused() {
        status = Status.IDLE;
    }

    public boolean isPlaying() {
        return status.equals(Status.ALIVE);
    }

    public void setPlaying() {
        status = Status.ALIVE;
    }

    public int getSmallBlind() {
        return blindsListPref.get(blindPos).intValue();
    }

    public int getBigBlind() {
        return blindsListPref.get(blindPos + 1).intValue();
    }

    public long getRiseTimePref() {
        return riseTimePref;
    }

    public long getRiseTimeLeft() {
        return riseTimeLeft;
    }

    public void setRiseTimeLeft(long riseTimeLeft) {
        this.riseTimeLeft = riseTimeLeft;
    }

    public long getRebuyTimeLeft() {
        return rebuyTimeLeft;
    }

    public void setRebuyTimeLeft(long rebuyTimeLeft) {
        this.rebuyTimeLeft = rebuyTimeLeft;
    }

    public void setNextBlindPos() {
        blindPos++;
        if (blindPos + 1 >= blindsListPref.size()) {
            blindPos = 0;
        }
    }
}
