package net.chokethe.killerdealer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class BlindsConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blinds_config);

        // TODO:
        // 1. create blindsConfigHolder to initialize and save the info of this activity on the fly in the preferences
        // 2. initialize the holder in onStart
        // 3. set the blinds and the first generated blinds (this holder must have separated lists)
        // 4. save the holder in onStop, no need to save button, whatever you put is saved at exit
    }
}
