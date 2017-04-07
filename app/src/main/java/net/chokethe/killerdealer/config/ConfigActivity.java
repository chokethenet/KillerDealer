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
import net.chokethe.killerdealer.db.KillerDealerDbHelper;
import net.chokethe.killerdealer.utils.CommonUtils;

public class ConfigActivity extends AppCompatActivity {

    private BlindsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private KillerDealerDbHelper mKillerDealerDbHelper;

    // TODO: missing rebuy timer layout and dialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        final FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlindDialogHelper.showInsert(ConfigActivity.this, mRecyclerView, mKillerDealerDbHelper);
            }
        });

        mKillerDealerDbHelper = new KillerDealerDbHelper(this);
        mAdapter = new BlindsAdapter(this, mKillerDealerDbHelper);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_blinds);
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

    public static void setBlindTextWithAdaptableSize(TextView textView, int value, boolean isConfig) {
        setBlindAdaptableSize(textView, value, isConfig);
        textView.setText(String.valueOf(value));
    }

    // TODO: use this when typing in dialogs
//    public static void adaptBlindSize(TextView textView) {
//        setBlindAdaptableSize(textView, Integer.valueOf(String.valueOf(textView.getText())), true);
//    }

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
