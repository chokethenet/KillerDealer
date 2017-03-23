package net.chokethe.killerdealer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;

import java.util.List;

public class TimersConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timers_config);

        // TODO:
        // 1. create timersConfigHolder to initialize and save the info of this activity on the fly in the preferences
        // 2. initialize the holder in onStart
        // 3. and split the times in hh, mm and ss to set the pickers
        // 4. save the holder in onStop, no need to save button, whatever you put is saved at exit

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
}
