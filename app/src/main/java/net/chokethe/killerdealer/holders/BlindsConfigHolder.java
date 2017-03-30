package net.chokethe.killerdealer.holders;

import android.content.Context;

import net.chokethe.killerdealer.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlindsConfigHolder {

    private static final int MAX_BLINDS_SHOWN = 20;

    private List<Integer> blindsListPref;

    public BlindsConfigHolder(Context context) {
        blindsListPref = PreferencesUtils.getRawBlindsList(context);
    }

    public void save(Context context) {
        PreferencesUtils.setBlindsList(context, blindsListPref);
    }

    public List<Integer> getBlindsListPref() {
        return blindsListPref;
    }

    public void setBlindsListPref(List<Integer> blindsListPref) {
        this.blindsListPref = blindsListPref;
    }

    public static String getResultBlinds(List<Integer> blindsList) {
        StringBuilder result = new StringBuilder();
        List<Integer> copiedBlindsList = new ArrayList<>();
        copiedBlindsList.addAll(blindsList);
        Collections.sort(copiedBlindsList);
        while (!copiedBlindsList.isEmpty() && copiedBlindsList.get(0) == 0) {
            copiedBlindsList.remove(0);
        }
        PreferencesUtils.generateDefaultBlinds(copiedBlindsList);

        List<Integer> generatedBlindsListPref = PreferencesUtils.getGeneratedNextBlinds(copiedBlindsList);
        int generatedSize = generatedBlindsListPref.size();
        int blindsSize = copiedBlindsList.size();

        for (int i = 0; i < MAX_BLINDS_SHOWN; i++) {
            if (i < blindsSize) {
                result.append(String.valueOf(copiedBlindsList.get(i))).append(", ");
            } else if ((i - blindsSize) < generatedSize) {
                result.append(String.valueOf(generatedBlindsListPref.get(i - blindsSize))).append(", ");
            } else {
                break;
            }
        }
        result.append("...");
        return result.toString();
    }
}
