package net.chokethe.killerdealer.utils;

public class TimeUtils {
    private static int HOURS_IN_MILLIS = 3600000;
    private static int MINS_IN_MILLIS = 60000;
    private static int SECS_IN_MILLIS = 1000;

    private TimeUtils() {
    }

    public static String getPrettyTime(long millis) {
        int hours = getHours(millis);
        int minutes = getMins(millis);
        int seconds = getSecs(millis);
        return (hours == 0 ? "" : hours + ":")
                + getTwoDigitsTime(minutes) + ":"
                + getTwoDigitsTime(seconds);
    }

    public static String getTwoDigitsTime(int time) {
        return String.valueOf(time).length() == 1 ? "0" + time : String.valueOf(time);
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

    public static String getStringHours(long millis) {
        return String.valueOf(getHours(millis));
    }

    public static String getStringMins(long millis) {
        int minutes = getMins(millis);
        return (String.valueOf(minutes).length() == 1 ? "0" + minutes : String.valueOf(minutes));
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

    private static int getSecs(long millis) {
        return (int) ((millis / SECS_IN_MILLIS) % 60);
    }

}
