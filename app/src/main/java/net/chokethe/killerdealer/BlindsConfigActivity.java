package net.chokethe.killerdealer;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import net.chokethe.killerdealer.adapters.BlindsAdapter;
import net.chokethe.killerdealer.holders.BlindsConfigHolder;
import net.chokethe.killerdealer.utils.CommonUtils;

import java.util.Collections;

public class BlindsConfigActivity extends AppCompatActivity {

    private TextView mBlindResult;

    private BlindsConfigHolder mBlindsConfigHolder;
    private BlindsAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blinds_config);

        mBlindResult = (TextView) findViewById(R.id.tv_blind_result);
        final FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(mAdapter.getBlinds());
                mAdapter.getBlinds().add(0);
                mAdapter.notifyDataSetChanged();

                int lastPosition = mAdapter.getItemCount() - 1;
                mRecyclerView.smoothScrollToPosition(lastPosition);
                View current = getCurrentFocus();
                if (current != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(current.getWindowToken(), 0);
                    current.clearFocus();
                }
                CommonUtils.showToast(BlindsConfigActivity.this, getString(R.string.blind_added));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBlindsConfigHolder = new BlindsConfigHolder(this);
        updateUI();
    }

    private void updateUI() {
        mAdapter = new BlindsAdapter(this, mBlindsConfigHolder.getBlindsListPref());
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                updateResultUI(BlindsConfigActivity.this);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_blinds);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)); // FIXME: for future builds with small-big blinds and rise time individually
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Integer blind = (Integer) viewHolder.itemView.getTag();
                mAdapter.getBlinds().remove(blind);
                if (mAdapter.getBlinds().isEmpty()) {
                    mAdapter.getBlinds().add(0);
                }
                mAdapter.notifyDataSetChanged();
                CommonUtils.showToast(BlindsConfigActivity.this, getString(R.string.blind_deleted));
            }
        }).attachToRecyclerView(mRecyclerView);

        updateResultUI(this);
    }

    public static void updateResultUI(Context context) {
        BlindsConfigActivity mActivity = (BlindsConfigActivity) context;
        mActivity.mBlindResult.setText(BlindsConfigHolder.getResultBlinds(mActivity.mAdapter.getBlinds()));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBlindsConfigHolder.setBlindsListPref(mAdapter.getBlinds());
        mBlindsConfigHolder.save(this);
    }

    public static void setBlindTextWithAdaptableSize(TextView textView, int value) {
        setBlindAdaptableSize(textView, value);
        textView.setText(String.valueOf(value));
    }

    public static void adaptBlindSize(TextView textView) {
        setBlindAdaptableSize(textView, Integer.valueOf(String.valueOf(textView.getText())));
    }

    private static void setBlindAdaptableSize(TextView textView, int value) {
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
        textView.setTextSize(textSize);
    }
}
