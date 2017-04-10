package net.chokethe.killerdealer;

import android.content.Context;
import android.database.Cursor;

import net.chokethe.killerdealer.db.BlindsContract;
import net.chokethe.killerdealer.db.KillerDealerDbHelper;
import net.chokethe.killerdealer.utils.PreferencesUtils;
import net.chokethe.killerdealer.utils.TimeUtils;

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

    private Cursor blindsCursor;
    private int blindPos;

    private long riseTimeLeft;
    private long rebuyTimePref;
    private long rebuyTimeLeft;

    private KillerDealerDbHelper mKillerDealerDbHelper;

    public SessionHolder(Context context, long now) {
        mKillerDealerDbHelper = new KillerDealerDbHelper(context);

        status = PreferencesUtils.getStatus(context);
        lastPlayTime = PreferencesUtils.getLastPlayTime(context);

        blindsCursor = mKillerDealerDbHelper.getAllBlinds();
        blindPos = PreferencesUtils.getBlindPos(context);
        if (blindPos >= blindsCursor.getCount()) {
            blindPos = PreferencesUtils.DEFAULT_BLINDS_POS;
        }
        blindsCursor.moveToPosition(blindPos);

        riseTimeLeft = getRiseTimeLeftBySTatus(context, now);

        rebuyTimePref = PreferencesUtils.getRebuyTime(context);
        rebuyTimeLeft = getRebuyTimeLeftByStatus(context, now);
    }

    private long getRiseTimeLeftBySTatus(Context context, long now) {
        long pausedRiseTime = PreferencesUtils.getPausedRiseTime(context, getRiseTimePref());
        long totalElapsedMillis = TimeUtils.getTotalElapsedMillis(lastPlayTime, pausedRiseTime, getRiseTimePref(), now);
        return getTimeLeftByStatus(totalElapsedMillis, getRiseTimePref(), pausedRiseTime);
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

    public void save(Context context, long now) {
        PreferencesUtils.setStatus(context, status);
        PreferencesUtils.setBlindPos(context, blindsCursor.getPosition());
        if (isPlaying()) {
            PreferencesUtils.setLastPlayTime(context, now);
        }
        if (!isStopped()) {
            PreferencesUtils.setPausedRiseTime(context, riseTimeLeft);
            PreferencesUtils.setPausedRebuyTime(context, rebuyTimeLeft);
        }
        if (blindsCursor != null && !blindsCursor.isClosed()) {
            blindsCursor.close();
        }
        mKillerDealerDbHelper.close();
    }

    void reset() {
        setStopped();
        blindsCursor.moveToPosition(PreferencesUtils.DEFAULT_BLINDS_POS);
        resetRiseTimeLeft();
        resetRebuyTimeLeft();
    }

    void resetRiseTimeLeft() {
        setRiseTimeLeft(getRiseTimePref());
    }

    void resetRebuyTimeLeft() {
        setRebuyTimeLeft(rebuyTimePref);
    }

    boolean isStopped() {
        return status.equals(Status.DEAD);
    }

    private void setStopped() {
        status = Status.DEAD;
    }

    boolean isPaused() {
        return status.equals(Status.IDLE);
    }

    void setPaused() {
        status = Status.IDLE;
    }

    public boolean isPlaying() {
        return status.equals(Status.ALIVE);
    }

    void setPlaying() {
        status = Status.ALIVE;
    }

    public int getSmallBlind() {
        return blindsCursor.getInt(blindsCursor.getColumnIndex(BlindsContract.BlindsEntry.COLUMN_SMALL_BLIND));
    }

    public int getBigBlind() {
        return blindsCursor.getInt(blindsCursor.getColumnIndex(BlindsContract.BlindsEntry.COLUMN_BIG_BLIND));
    }

    long getRiseTimePref() {
        int minutes = blindsCursor.getInt(blindsCursor.getColumnIndex(BlindsContract.BlindsEntry.COLUMN_RISE_TIME));
        return TimeUtils.getMinsInMillis(minutes);
    }

    public long getRiseTimeLeft() {
        return riseTimeLeft;
    }

    void setRiseTimeLeft(long riseTimeLeft) {
        this.riseTimeLeft = riseTimeLeft;
    }

    public long getRebuyTimeLeft() {
        return rebuyTimeLeft;
    }

    void setRebuyTimeLeft(long rebuyTimeLeft) {
        this.rebuyTimeLeft = rebuyTimeLeft;
    }

    public void setNextBlindPos() {
        if (!blindsCursor.moveToNext()) {
            blindsCursor.moveToLast();
        }
    }

    void setPrevBlindPos() {
        if (!blindsCursor.moveToPrevious()) {
            blindsCursor.moveToFirst();
        }

    }
}
