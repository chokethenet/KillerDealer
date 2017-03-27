package net.chokethe.killerdealer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import net.chokethe.killerdealer.BlindsConfigActivity;
import net.chokethe.killerdealer.R;

import java.util.List;

public class BlindsAdapter extends RecyclerView.Adapter<BlindsAdapter.BlindViewHolder> {
    private Context mContext;
    private List<Integer> mBlinds;

    public BlindsAdapter(Context context, List<Integer> blinds) {
        mContext = context;
        mBlinds = blinds;
    }

    public List<Integer> getBlinds() {
        return mBlinds;
    }

    @Override
    public BlindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.blind_layout, parent, false);
        return new BlindViewHolder(view, new BlindEditTextListener());
    }

    @Override
    public void onBindViewHolder(BlindViewHolder holder, int position) {
        int blind = mBlinds.get(position);
        holder.itemView.setTag(blind);
        holder.mBlindEditTextListener.updatePosition(position);
        holder.mBlind.setText(String.valueOf(blind));
    }

    @Override
    public int getItemCount() {
        return mBlinds.size();
    }

    class BlindViewHolder extends RecyclerView.ViewHolder {
        EditText mBlind;
        BlindEditTextListener mBlindEditTextListener;

        BlindViewHolder(View itemView, BlindEditTextListener blindEditTextListener) {
            super(itemView);
            mBlind = (EditText) itemView.findViewById(R.id.et_blind);
            mBlindEditTextListener = blindEditTextListener;
            mBlind.addTextChangedListener(blindEditTextListener);
        }
    }

    private class BlindEditTextListener implements TextWatcher {
        private int position;

        void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            String input = charSequence.toString();
            if (!"".equals(input)) {
                mBlinds.set(position, Integer.valueOf(input));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            BlindsConfigActivity.updateResultUI(mContext);
        }
    }
}
