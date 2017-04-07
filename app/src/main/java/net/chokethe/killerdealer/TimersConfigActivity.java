package net.chokethe.killerdealer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;

import net.chokethe.killerdealer.utils.TimeUtils;

public class TimersConfigActivity extends AppCompatActivity {

    private TimersConfigHolder mTimersConfigHolder;
    private NumberPicker mNumberPickerRiseMM;
    private NumberPicker mNumberPickerRiseSS;
    private NumberPicker mNumberPickerRebuyHH;
    private NumberPicker mNumberPickerRebuyMM;
    private NumberPicker mNumberPickerRebuySS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timers_config);

        mNumberPickerRiseMM = (NumberPicker) findViewById(R.id.np_rise_mm_config);
        mNumberPickerRiseSS = (NumberPicker) findViewById(R.id.np_rise_ss_config);
        mNumberPickerRebuyHH = (NumberPicker) findViewById(R.id.np_rebuy_hh_config);
        mNumberPickerRebuyMM = (NumberPicker) findViewById(R.id.np_rebuy_mm_config);
        mNumberPickerRebuySS = (NumberPicker) findViewById(R.id.np_rebuy_ss_config);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTimersConfigHolder = new TimersConfigHolder(this);
        updateUI();
    }

    private void updateUI() {
        long riseTime = mTimersConfigHolder.getRiseTimePref();
        int riseMM = TimeUtils.getMins(riseTime);
        int riseSS = TimeUtils.getSecs(riseTime);
        long rebuyTime = mTimersConfigHolder.getRebuyTimePref();
        int rebuyHH = TimeUtils.getHours(rebuyTime);
        int rebuyMM = TimeUtils.getMins(rebuyTime);
        int rebuySS = TimeUtils.getSecs(rebuyTime);
        setTimeMinMaxAndValues(mNumberPickerRiseMM, riseMM);
        setTimeMinMaxAndValues(mNumberPickerRiseSS, riseSS);
        setTimeMinMaxAndValues(mNumberPickerRebuyHH, rebuyHH);
        setTimeMinMaxAndValues(mNumberPickerRebuyMM, rebuyMM);
        setTimeMinMaxAndValues(mNumberPickerRebuySS, rebuySS);
    }

    private void setTimeMinMaxAndValues(NumberPicker picker, int value) {
        picker.setMinValue(TimeUtils.MIN_TIME);
        picker.setMaxValue(TimeUtils.MAX_TIME);
        picker.setValue(value);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateValues();
        mTimersConfigHolder.save(this);
    }

    private void updateValues() {
        int riseMM = mNumberPickerRiseMM.getValue();
        int riseSS = mNumberPickerRiseSS.getValue();
        long riseTime = TimeUtils.getMinsInMillis(riseMM) + TimeUtils.getSecsInMillis(riseSS);
        int rebuyHH = mNumberPickerRebuyHH.getValue();
        int rebuyMM = mNumberPickerRebuyMM.getValue();
        int rebuySS = mNumberPickerRebuySS.getValue();
        long rebuyTime = TimeUtils.getHoursInMillis(rebuyHH) + TimeUtils.getMinsInMillis(rebuyMM) + TimeUtils.getSecsInMillis(rebuySS);
        mTimersConfigHolder.setRiseTimePref(riseTime);
        mTimersConfigHolder.setRebuyTimePref(rebuyTime);
    }
}
