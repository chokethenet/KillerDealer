package net.chokethe.killerdealer;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import net.chokethe.killerdealer.adapters.BlindsAdapter;
import net.chokethe.killerdealer.db.KillerDealerDbHelper;
import net.chokethe.killerdealer.holders.BlindsConfigHolder;

public class ConfigActivity extends AppCompatActivity {

    private TextView mBlindResult;

    private BlindsConfigHolder mBlindsConfigHolder;
    private BlindsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private KillerDealerDbHelper mKillerDealerDbHelper;
    private Cursor mBlindsCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

//        final FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
//        fabButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Collections.sort(mAdapter.getBlinds());
////                mAdapter.getBlinds().add(0);
//                mAdapter.notifyDataSetChanged();
//
//                int lastPosition = mAdapter.getItemCount() - 1;
//                mRecyclerView.smoothScrollToPosition(lastPosition);
//                View current = getCurrentFocus();
//                if (current != null) {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(current.getWindowToken(), 0);
//                    current.clearFocus();
//                }
//                CommonUtils.showToast(ConfigActivity.this, getString(R.string.blind_added));
//            }
//        });

        mKillerDealerDbHelper = new KillerDealerDbHelper(this);
        mBlindsCursor = mKillerDealerDbHelper.getAllBlinds();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBlindsConfigHolder = new BlindsConfigHolder(this);
        updateUI();
    }

    private void updateUI() {
        mAdapter = new BlindsAdapter(this, mBlindsCursor);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_blinds);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)); // FIXME: for future builds with small-big blinds and rise time individually
        mRecyclerView.setAdapter(mAdapter);

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                Integer blind = (Integer) viewHolder.itemView.getTag();
////                mAdapter.getBlinds().remove(blind);
////                if (mAdapter.getBlinds().isEmpty()) {
////                    mAdapter.getBlinds().add(0);
////                }
//                mAdapter.notifyDataSetChanged();
//                CommonUtils.showToast(ConfigActivity.this, getString(R.string.blind_deleted));
//            }
//        }).attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mBlindsConfigHolder.setBlindsListPref(mAdapter.getBlinds());
        mBlindsConfigHolder.save(this);
    }

    public static void setBlindTextWithAdaptableSize(TextView textView, int value, boolean isConfig) {
        setBlindAdaptableSize(textView, value, isConfig);
        textView.setText(String.valueOf(value));
    }

//    public static void adaptBlindSize(TextView textView) {
//        setBlindAdaptableSize(textView, Integer.valueOf(String.valueOf(textView.getText())));
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
        if (mBlindsCursor != null && !mBlindsCursor.isClosed()) {
            mBlindsCursor.close();
        }
        mKillerDealerDbHelper.close();
    }
}
