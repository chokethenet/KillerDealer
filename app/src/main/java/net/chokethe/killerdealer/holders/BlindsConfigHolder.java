package net.chokethe.killerdealer.holders;

import android.content.Context;

import net.chokethe.killerdealer.utils.PreferencesUtils;

import java.util.List;

public class BlindsConfigHolder {

    private List<Integer> blindsListPref;
    private List<Integer> generatedBlindsListPref;

    public BlindsConfigHolder(Context context) {
        blindsListPref = PreferencesUtils.getRawBlindsList(context);
        generatedBlindsListPref = PreferencesUtils.getGeneratedNextBlinds(blindsListPref);
    }

    public void save(Context context) {
        // TODO: validar que la lista está ordenada
        PreferencesUtils.setBlindsList(context, blindsListPref);
    }

    public List<Integer> getBlindsListPref() {
        return blindsListPref;
    }

    public void setBlindsListPref(List<Integer> blindsListPref) {
        this.blindsListPref = blindsListPref;
    }

    public List<Integer> getGeneratedBlindsListPref() {
        return generatedBlindsListPref;
    }
}
