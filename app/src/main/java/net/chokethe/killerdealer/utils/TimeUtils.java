package net.chokethe.killerdealer.utils;

public class TimeUtils {
    public static int HOURS_IN_MILLIS = 3600000;
    public static int MINS_IN_MILLIS = 60000;
    public static int SECS_IN_MILLIS = 1000;

    private TimeUtils() {
    }

    public static String getPrettyTime(long millis) {
        long hours = millis / HOURS_IN_MILLIS;
        long minutes = (millis / MINS_IN_MILLIS) % 60;
        long seconds = (millis / SECS_IN_MILLIS) % 60;
        return (hours == 0 ? "" : hours + ":")
                + (String.valueOf(minutes).length() == 1 ? "0" + minutes : minutes) + ":"
                + (String.valueOf(seconds).length() == 1 ? "0" + seconds : seconds);
    }

    public static long getTotalElapsedMillis(long lastPlayTime, long timeLeft, long timePref, long now) {
        if (lastPlayTime == 0) {
            return 0;
        } else {
            long pausedElapsedMillis = timePref - timeLeft;
            long playingStartedMillis = lastPlayTime - pausedElapsedMillis;
            long totalElapsedMillis = now - playingStartedMillis;
            return totalElapsedMillis;
        }
    }
}
