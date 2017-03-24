package net.chokethe.killerdealer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.chokethe.killerdealer.holders.SessionHolder;
import net.chokethe.killerdealer.utils.TimeUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static Toast mToast;

    private TextView mSmallBlindTextView;
    private TextView mBigBlindTextView;
    private TextView mRiseTimerTextView;
    private TextView mRebuyTimerTextView;
    private ImageView mPlayPauseView;

    private CountDownTimer mRiseTimer;
    private CountDownTimer mRebuyTimer;

    private SessionHolder mSessionHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSmallBlindTextView = (TextView) findViewById(R.id.tv_blind_small);
        mBigBlindTextView = (TextView) findViewById(R.id.tv_blind_big);
        mRiseTimerTextView = (TextView) findViewById(R.id.tv_rise_timer);
        mRebuyTimerTextView = (TextView) findViewById(R.id.tv_rebuy_timer);
        mPlayPauseView = (ImageView) findViewById(R.id.iv_play_pause);

        findViewById(R.id.iv_blind_small).setOnClickListener(this);
        findViewById(R.id.iv_blind_big).setOnClickListener(this);
        mRiseTimerTextView.setOnClickListener(this);
        mRebuyTimerTextView.setOnClickListener(this);
        findViewById(R.id.iv_reload).setOnClickListener(this);
        mPlayPauseView.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSessionHolder = new SessionHolder(this);
        updateUI();
        handleTimers();
        // TODO: if pending notif on blinds or rebuys, cancel
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelTimers();
        mSessionHolder.save(this);
        // TODO: set next blind notif to call notifyRiseBlinds(); recursively for mSessionHolder.getRiseTimePref()
        // TODO: set next rebuy notif to call notifyRebuyEnd();
    }

    // UI
    private void updateUI() {
        updateBlindsUI();
        updateTimersUI();
        updatePlayPauseUI();
    }

    private void updateBlindsUI() {
        mSmallBlindTextView.setText(String.valueOf(mSessionHolder.getSmallBlind()));
        mBigBlindTextView.setText(String.valueOf(mSessionHolder.getBigBlind()));
    }

    private void updateTimersUI() {
        updateTimerUI(mRiseTimerTextView, mSessionHolder.getRiseTimeLeft());
        updateTimerUI(mRebuyTimerTextView, mSessionHolder.getRebuyTimeLeft());
    }

    private static void updateTimerUI(TextView mRiseTimerTextView, long remainingMillis) {
        mRiseTimerTextView.setText(TimeUtils.getPrettyTime(remainingMillis));
    }

    private void updatePlayPauseUI() {
        mPlayPauseView.setImageResource(!mSessionHolder.isPlaying() ? R.drawable.ic_play : R.drawable.ic_pause);
    }

    // CONTROLS
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_blind_small:
            case R.id.iv_blind_big:
                configOnClick(BlindsConfigActivity.class);
                break;
            case R.id.tv_rise_timer:
            case R.id.tv_rebuy_timer:
                configOnClick(TimersConfigActivity.class);
                break;
            case R.id.iv_reload:
                reloadOnClick();
                break;
            case R.id.iv_play_pause:
                playPauseOnClick();
                break;
        }
    }

    private void configOnClick(Class activityClass) {
        Intent configActivityIntent = new Intent(this, activityClass);
        startActivity(configActivityIntent);
    }

    private void reloadOnClick() {
        showToast(this, getString(R.string.reload_toast));
        cancelTimers();
        mSessionHolder.reset();
        updateUI();
    }

    private void playPauseOnClick() {
        if (!mSessionHolder.isPlaying()) {
            mSessionHolder.setPlaying();
        } else {
            mSessionHolder.setPaused();
        }
        handleTimers();
    }

    private void handleTimers() {
        if (mSessionHolder.isPlaying()) {
            showToast(this, getString(R.string.play_toast));
            updatePlayPauseUI();
            mRiseTimer = createAndStartRiseTimer(mSessionHolder.getRiseTimeLeft());
            mRebuyTimer = createAndStartRebuyTimer();
        } else if (mSessionHolder.isPaused()) {
            showToast(this, getString(R.string.pause_toast));
            updatePlayPauseUI();
            cancelTimers();
        }
    }

    private CountDownTimer createAndStartRiseTimer(long millisInFuture) {
        return new CountDownTimer(millisInFuture, 100) {
            @Override
            public void onTick(long remainingMillis) {
                mSessionHolder.setRiseTimeLeft(remainingMillis);
                updateTimerUI(mRiseTimerTextView, remainingMillis);
            }

            @Override
            public void onFinish() {
                mSessionHolder.resetRiseTimeLeft();
                mRiseTimer = createAndStartRiseTimer(mSessionHolder.getRiseTimePref());
                mSessionHolder.setNextBlindPos();
                updateBlindsUI();
                notifyRiseBlinds();
            }
        }.start();
    }

    private CountDownTimer createAndStartRebuyTimer() {
        return new CountDownTimer(mSessionHolder.getRebuyTimeLeft(), 100) {
            @Override
            public void onTick(long remainingMillis) {
                mSessionHolder.setRebuyTimeLeft(remainingMillis);
                updateTimerUI(mRebuyTimerTextView, remainingMillis);
            }

            @Override
            public void onFinish() {
                cancelTimerIfNotNull(mRebuyTimer);
                notifyRebuyEnd();
            }
        }.start();
    }

    private void cancelTimers() {
        cancelTimerIfNotNull(mRiseTimer);
        cancelTimerIfNotNull(mRebuyTimer);
    }

    private void cancelTimerIfNotNull(CountDownTimer mTimer) {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private void notifyRiseBlinds() {
        // TODO: vibrate
        // TODO: sound
        showToast(this, "Rise done!");
    }

    private void notifyRebuyEnd() {
        // TODO: vibrate
        // TODO: sound
        showToast(this, "Rebuy ended!");
    }

    public static void showToast(Context context, String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
