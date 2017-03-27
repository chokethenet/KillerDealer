package net.chokethe.killerdealer.utils;

public class TimeUtils {
    private static int HOURS_IN_MILLIS = 3600000;
    private static int MINS_IN_MILLIS = 60000;
    private static int SECS_IN_MILLIS = 1000;

    public static int MIN_TIME = 0;
    public static int MAX_TIME = 59;

    private TimeUtils() {
    }

    public static String getPrettyTime(long millis) {
        long hours = getHours(millis);
        long minutes = getMins(millis);
        long seconds = getSecs(millis);
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
            return now - playingStartedMillis;
        }
    }

    public static int getHours(long millis) {
        return (int) (millis / HOURS_IN_MILLIS);
    }

    public static long getHoursInMillis(int hours) {
        return hours * HOURS_IN_MILLIS;
    }

    public static int getMins(long millis) {
        return (int) ((millis / MINS_IN_MILLIS) % 60);
    }

    public static long getMinsInMillis(int minutes) {
        return minutes * MINS_IN_MILLIS;
    }

    public static int getSecs(long millis) {
        return (int) ((millis / SECS_IN_MILLIS) % 60);
    }

    public static long getSecsInMillis(int seconds) {
        return seconds * SECS_IN_MILLIS;
    }

}
