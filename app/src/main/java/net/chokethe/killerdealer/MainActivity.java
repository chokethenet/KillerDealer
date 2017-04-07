package net.chokethe.killerdealer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import net.chokethe.killerdealer.about.AboutActivity;
import net.chokethe.killerdealer.config.ConfigActivity;
import net.chokethe.killerdealer.notifications.NotificationUtils;
import net.chokethe.killerdealer.settings.SettingsActivity;
import net.chokethe.killerdealer.settings.SettingsHolder;
import net.chokethe.killerdealer.utils.CommonUtils;
import net.chokethe.killerdealer.utils.TimeUtils;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView mSmallBlindTextView;
    private TextView mBigBlindTextView;
    private TextView mRiseTimerTextView;
    private TextView mRebuyTimerTextView;
    private ImageView mPlayPauseView;

    private CountDownTimer mRiseTimer;
    private CountDownTimer mRebuyTimer;

    private SessionHolder mSessionHolder;
    private SettingsHolder mSettingsHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        mSmallBlindTextView = (TextView) findViewById(R.id.tv_blind_small);
        mBigBlindTextView = (TextView) findViewById(R.id.tv_blind_big);
        mRiseTimerTextView = (TextView) findViewById(R.id.tv_rise_timer);
        mRebuyTimerTextView = (TextView) findViewById(R.id.tv_rebuy_timer);
        mPlayPauseView = (ImageView) findViewById(R.id.iv_play_pause);

        findViewById(R.id.main_iv_blind_prev).setOnClickListener(this);
        findViewById(R.id.main_iv_blind_next).setOnClickListener(this);
        mSmallBlindTextView.setOnClickListener(this);
        mBigBlindTextView.setOnClickListener(this);
        mRiseTimerTextView.setOnClickListener(this);
        mRebuyTimerTextView.setOnClickListener(this);
        findViewById(R.id.main_iv_rise_reload).setOnClickListener(this);
        findViewById(R.id.main_iv_rebuy_reload).setOnClickListener(this);
        findViewById(R.id.iv_reload).setOnClickListener(this);
        mPlayPauseView.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lockScreen();
        NotificationUtils.cancelScheduledNotifications(this);
        mSessionHolder = new SessionHolder(this, System.currentTimeMillis());
        updateUI();
        handleTimers();
    }

    private void lockScreen() {
        mSettingsHolder = new SettingsHolder(this);
        if (mSettingsHolder.isScreenLocked()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelTimers();
        long now = System.currentTimeMillis();
        mSessionHolder.save(this, now);
        NotificationUtils.scheduleNotifications(this, now);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            case R.id.action_about:
                Intent startAboutActivity = new Intent(this, AboutActivity.class);
                startActivity(startAboutActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mSettingsHolder.updateHolder(this, sharedPreferences, key);
    }

    // UI
    private void updateUI() {
        updateBlindsUI();
        updateTimersUI();
        updatePlayPauseUI();
    }

    private void updateBlindsUI() {
        ConfigActivity.setBlindTextWithAdaptableSize(mSmallBlindTextView, mSessionHolder.getSmallBlind(), false);
        ConfigActivity.setBlindTextWithAdaptableSize(mBigBlindTextView, mSessionHolder.getBigBlind(), false);
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
            case R.id.main_iv_blind_prev:
                setPrevBlindOnClick();
                break;
            case R.id.main_iv_blind_next:
                setNextBlindOnClick();
                break;
            case R.id.tv_blind_small:
            case R.id.tv_blind_big:
            case R.id.tv_rise_timer:
            case R.id.tv_rebuy_timer:
                configOnClick(ConfigActivity.class);
                break;
            case R.id.main_iv_rise_reload:
                reloadRiseOnClick();
                break;
            case R.id.main_iv_rebuy_reload:
                reloadRebuyOnClick();
                break;
            case R.id.iv_reload:
                reloadOnClick();
                break;
            case R.id.iv_play_pause:
                playPauseOnClick();
                break;
        }
    }

    private void setPrevBlindOnClick() {
        mSessionHolder.setPrevBlindPos();
        updateBlindsAndTimers();
    }

    private void setNextBlindOnClick() {
        mSessionHolder.setNextBlindPos();
        updateBlindsAndTimers();
    }

    private void updateBlindsAndTimers() {
        updateBlindsUI();
        if (mSettingsHolder.isRiseReset()) {
            mSessionHolder.resetRiseTimeLeft();
            reloadTimers();
        }
    }

    private void configOnClick(Class activityClass) {
        Intent configActivityIntent = new Intent(this, activityClass);
        startActivity(configActivityIntent);
    }

    private void reloadRiseOnClick() {
        mSessionHolder.resetRiseTimeLeft();
        reloadTimers();
    }

    private void reloadRebuyOnClick() {
        mSessionHolder.resetRebuyTimeLeft();
        reloadTimers();
    }

    private void reloadTimers() {
        cancelTimers();
        handleTimers();
        updateTimersUI();
    }

    private void reloadOnClick() {
        CommonUtils.showToast(this, getString(R.string.reload_toast));
        cancelTimers();
        mSessionHolder.reset();
        updateUI();
    }

    private void playPauseOnClick() {
        if (!mSessionHolder.isPlaying()) {
            mSessionHolder.setPlaying();
            CommonUtils.showToast(this, getString(R.string.play_toast));
        } else {
            mSessionHolder.setPaused();
            CommonUtils.showToast(this, getString(R.string.pause_toast));
        }
        handleTimers();
    }

    private void handleTimers() {
        if (mSessionHolder.isPlaying()) {
            updatePlayPauseUI();
            mRiseTimer = createAndStartRiseTimer(mSessionHolder.getRiseTimeLeft());
            mRebuyTimer = createAndStartRebuyTimer();
        } else if (mSessionHolder.isPaused()) {
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
                alertRiseBlinds();
            }
        }.start();
    }

    private CountDownTimer createAndStartRebuyTimer() {
        if (mSessionHolder.getRebuyTimeLeft() > 0) {
            return new CountDownTimer(mSessionHolder.getRebuyTimeLeft(), 100) {
                @Override
                public void onTick(long remainingMillis) {
                    mSessionHolder.setRebuyTimeLeft(remainingMillis);
                    updateTimerUI(mRebuyTimerTextView, remainingMillis);
                }

                @Override
                public void onFinish() {
                    cancelTimerIfNotNull(mRebuyTimer);
                    alertRebuyEnd();
                }
            }.start();
        } else {
            return null;
        }
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

    private void alertRiseBlinds() {
        customAlert(getString(R.string.rise_done_toast));
    }

    private void alertRebuyEnd() {
        customAlert(getString(R.string.rebuy_end_toast));
    }

    private void customAlert(String message) {
        new AsyncTask<Context, Void, Void>() {

            @Override
            protected Void doInBackground(Context... context) {
                if (mSettingsHolder.isVibrateOn()) {
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    if (am.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        if (vibrator.hasVibrator()) {
                            vibrator.vibrate(new long[]{0, 300, 300, 300}, -1);
                        }
                    }
                }

                if (mSettingsHolder.isSoundOn()) {
                    Uri notificationRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                    try {
                        mediaPlayer.setDataSource(context[0], notificationRingtoneUri);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        while (mediaPlayer.isPlaying()) {
                        }
                        mediaPlayer.release();
                    } catch (Exception e) {
                        return null;
                    }
                }

                return null;
            }
        }.execute(this);

        if (mSettingsHolder.isToastOn()) {
            CommonUtils.showToast(this, message);
        }
    }
}
