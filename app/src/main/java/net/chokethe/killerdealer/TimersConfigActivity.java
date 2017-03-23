package net.chokethe.killerdealer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;

public class TimersConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timers_config);

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
