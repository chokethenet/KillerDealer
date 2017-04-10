package net.chokethe.killerdealer.config.blinds;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.chokethe.killerdealer.R;
import net.chokethe.killerdealer.config.ConfigActivity;
import net.chokethe.killerdealer.db.BlindsContract;
import net.chokethe.killerdealer.db.KillerDealerDbHelper;
import net.chokethe.killerdealer.utils.TimeUtils;

public class BlindsAdapter extends RecyclerView.Adapter<BlindsAdapter.BlindViewHolder> {
    private Context mContext;
    private KillerDealerDbHelper mKillerDealerDbHelper;
    private Cursor mCursor;

    public BlindsAdapter(Context context, KillerDealerDbHelper db) {
        mContext = context;
        mKillerDealerDbHelper = db;
        mCursor = mKillerDealerDbHelper.getAllBlinds();
    }

    @Override
    public BlindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.blind_layout, parent, false);
        return new BlindViewHolder(this, view);
    }

    @Override
    public void onBindViewHolder(BlindViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        long id = mCursor.getLong(mCursor.getColumnIndex(BlindsContract.BlindsEntry._ID));
        int smallBlind = mCursor.getInt(mCursor.getColumnIndex(BlindsContract.BlindsEntry.COLUMN_SMALL_BLIND));
        int bigBlind = mCursor.getInt(mCursor.getColumnIndex(BlindsContract.BlindsEntry.COLUMN_BIG_BLIND));
        int riseTime = mCursor.getInt(mCursor.getColumnIndex(BlindsContract.BlindsEntry.COLUMN_RISE_TIME));

        holder.itemView.setTag(position);
        holder.id = id;
        ConfigActivity.setBlindTextWithAdaptableSize(holder.mSmallBlind, smallBlind, true);
        ConfigActivity.setBlindTextWithAdaptableSize(holder.mBigBlind, bigBlind, true);
        holder.mRiseTime.setText(TimeUtils.getTwoDigitsTime(riseTime));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void close() {
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
    }

    public void swapCursor() {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = mKillerDealerDbHelper.getAllBlinds();
        this.notifyDataSetChanged();
    }

    public class BlindViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public long id;
        TextView mSmallBlind;
        TextView mBigBlind;
        TextView mRiseTime;
        BlindsAdapter mAdapter;

        BlindViewHolder(BlindsAdapter adapter, View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mSmallBlind = (TextView) itemView.findViewById(R.id.blind_tv_blind_small);
            mBigBlind = (TextView) itemView.findViewById(R.id.blind_tv_blind_big);
            mRiseTime = (TextView) itemView.findViewById(R.id.blind_tv_rise_time);
            mAdapter = adapter;
        }

        @Override
        public void onClick(View v) {
            BlindDialogHelper.showUpdate(mContext, mAdapter, mKillerDealerDbHelper, this);
        }
    }
}
