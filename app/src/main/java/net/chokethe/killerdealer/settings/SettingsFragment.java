package net.chokethe.killerdealer.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import net.chokethe.killerdealer.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.prefs_main);
    }

}