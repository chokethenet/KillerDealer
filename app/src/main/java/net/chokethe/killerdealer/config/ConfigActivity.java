package net.chokethe.killerdealer.config;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import net.chokethe.killerdealer.R;
import net.chokethe.killerdealer.config.blinds.BlindDialogHelper;
import net.chokethe.killerdealer.config.blinds.BlindsAdapter;
import net.chokethe.killerdealer.config.rebuy.RebuyDialogHelper;
import net.chokethe.killerdealer.config.rebuy.RebuyTimerConfigHolder;
import net.chokethe.killerdealer.db.KillerDealerDbHelper;
import net.chokethe.killerdealer.utils.CommonUtils;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    private BlindsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private KillerDealerDbHelper mKillerDealerDbHelper;
    private RebuyTimerConfigHolder mRebuyTimerConfigHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mRebuyTimerConfigHolder = new RebuyTimerConfigHolder(this);

        final FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.config_fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlindDialogHelper.showInsert(ConfigActivity.this, mRecyclerView, mKillerDealerDbHelper);
            }
        });

        TextView hours = (TextView) findViewById(R.id.config_tv_rebuy_hours);
        hours.setText(mRebuyTimerConfigHolder.getRebuyStringHours());
        hours.setOnClickListener(this);
        findViewById(R.id.config_tv_rebuy_hh).setOnClickListener(this);
        TextView minutes = (TextView) findViewById(R.id.config_tv_rebuy_minutes);
        minutes.setText(mRebuyTimerConfigHolder.getRebuyStringMinutes());
        minutes.setOnClickListener(this);
        findViewById(R.id.config_tv_rebuy_mm).setOnClickListener(this);

        mKillerDealerDbHelper = new KillerDealerDbHelper(this);
        mAdapter = new BlindsAdapter(this, mKillerDealerDbHelper);
        mRecyclerView = (RecyclerView) findViewById(R.id.config_rv_blinds);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                BlindsAdapter.BlindViewHolder blindViewHolder = (BlindsAdapter.BlindViewHolder) viewHolder;
                mKillerDealerDbHelper.deleteBlind(blindViewHolder.id);
                mAdapter.swapCursor();
                CommonUtils.showToast(ConfigActivity.this, getString(R.string.blind_deleted));
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.config_tv_rebuy_hours:
            case R.id.config_tv_rebuy_hh:
            case R.id.config_tv_rebuy_minutes:
            case R.id.config_tv_rebuy_mm:
                RebuyDialogHelper.show(this, mRebuyTimerConfigHolder);
                break;
        }
    }

    public static void setBlindTextWithAdaptableSize(TextView textView, int value, boolean isConfig) {
        setBlindAdaptableSize(textView, value, isConfig);
        textView.setText(String.valueOf(value));
    }

    public static void adaptBlindSize(TextView textView) {
        setBlindAdaptableSize(textView, Integer.valueOf(String.valueOf(textView.getText())), true);
    }

    private static void setBlindAdaptableSize(TextView textView, int value, boolean isConfig) {
        int textSize = 64;
        if (value > 9999999) {
            textSize = 20;
        } else if (value > 999999) {
            textSize = 22;
        } else if (value > 99999) {
            textSize = 26;
        } else if (value > 9999) {
            textSize = 30;
        } else if (value > 999) {
            textSize = 36;
        } else if (value > 99) {
            textSize = 48;
        }
        if (isConfig) {
            textSize = textSize / 2;
        }
        textView.setTextSize(textSize);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.close();
        mKillerDealerDbHelper.close();
    }
}
