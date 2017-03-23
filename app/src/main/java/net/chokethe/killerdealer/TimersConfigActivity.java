package net.chokethe.killerdealer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;

import net.chokethe.killerdealer.holders.TimersConfigHolder;

public class TimersConfigActivity extends AppCompatActivity {

    private TimersConfigHolder mTimersConfigHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timers_config);

        // TODO: change the init for the pickers to get a global variable for each?
        int[] pickerIds = {
                R.id.np_rise_ss_config,
                R.id.np_rise_mm_config,
                R.id.np_rebuy_ss_config,
                R.id.np_rebuy_mm_config,
                R.id.np_rebuy_hh_config
        };
        NumberPicker picker;
        for (int pickerId : pickerIds) {
            picker = (NumberPicker) findViewById(pickerId);
            picker.setMinValue(0);
            picker.setMaxValue(59);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTimersConfigHolder = new TimersConfigHolder(this);
        updateUI();
    }

    private void updateUI() {
        long riseTime = mTimersConfigHolder.getRiseTimePref();
        long rebuyTime = mTimersConfigHolder.getRebuyTimePref();
        // TODO: split times in hh, mm and ss
        // TODO: update the pickers
    }

    @Override
    protected void onStop() {
        super.onStop();
        // TODO: get values from pickers and calculate both times
        mTimersConfigHolder.setRiseTimePref(0);
        mTimersConfigHolder.setRebuyTimePref(0);
        mTimersConfigHolder.save(this);
    }
}
