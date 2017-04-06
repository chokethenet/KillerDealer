package net.chokethe.killerdealer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import net.chokethe.killerdealer.ConfigActivity;
import net.chokethe.killerdealer.R;
import net.chokethe.killerdealer.db.BlindsContract;

public class BlindsAdapter extends RecyclerView.Adapter<BlindsAdapter.BlindViewHolder> {
    private Context mContext;
    private Cursor mCursor;
//    private List<Integer> mBlinds;

    public BlindsAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

//    public List<Integer> getBlinds() {
//        return mBlinds;
//    }

    @Override
    public BlindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.blind_layout, parent, false);
        return new BlindViewHolder(view);
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
        holder.mRiseTime.setText(String.valueOf(riseTime));
////        holder.mBlindEditTextListener.updatePosition(position);
//        ConfigActivity.setBlindTextWithAdaptableSize(holder.mBlind, blind, true);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class BlindViewHolder extends RecyclerView.ViewHolder {
        long id;
        TextView mSmallBlind;
        TextView mBigBlind;
        TextView mRiseTime;
//        BlindEditTextListener mBlindEditTextListener;

        BlindViewHolder(View itemView) {
            super(itemView);
            mSmallBlind = (TextView) itemView.findViewById(R.id.blind_tv_blind_small);
            mBigBlind = (TextView) itemView.findViewById(R.id.blind_tv_blind_big);
            mRiseTime = (TextView) itemView.findViewById(R.id.blind_tv_rise_time);
//            BlindEditTextListener blindEditTextListener = new BlindEditTextListener(mBlind);
//            mBlindEditTextListener = blindEditTextListener;
//            mBlind.addTextChangedListener(blindEditTextListener);
        }
    }

//    private class BlindEditTextListener implements TextWatcher {
//        private int position;
//        private EditText mBlind;
//
//        public BlindEditTextListener(EditText blind) {
//            mBlind = blind;
//        }
//
//        void updatePosition(int position) {
//            this.position = position;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//            String input = charSequence.toString();
//            if (!"".equals(input)) {
//                mBlinds.set(position, Integer.valueOf(input));
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//            ConfigActivity.adaptBlindSize(mBlind);
//            ConfigActivity.updateResultUI(mContext);
//        }
//    }
}
