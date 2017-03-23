package net.chokethe.killerdealer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.chokethe.killerdealer.holders.BlindsConfigHolder;

import java.util.List;

public class BlindsConfigActivity extends AppCompatActivity {

    private BlindsConfigHolder mBlindsConfigHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blinds_config);

        // TODO: init the views
        // 3. set the blinds and the first generated blinds (this holder must have separated lists)
        // 4. save the holder in onStop, no need to save button, whatever you put is saved at exit
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBlindsConfigHolder = new BlindsConfigHolder(this);
        updateUI();
    }

    private void updateUI() {
        List<Integer> blindsList = mBlindsConfigHolder.getBlindsListPref();
        List<Integer> generatedBlindsList = mBlindsConfigHolder.getGeneratedBlindsListPref();
        // TODO: update the blind inputs and the generated list
    }

    @Override
    protected void onStop() {
        super.onStop();
        // TODO: get values from inputs and generate the list
        mBlindsConfigHolder.setBlindsListPref(null);
        mBlindsConfigHolder.save(this);
    }
}
