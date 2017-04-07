package net.chokethe.killerdealer.config;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import net.chokethe.killerdealer.R;
import net.chokethe.killerdealer.db.KillerDealerDbHelper;
import net.chokethe.killerdealer.utils.CommonUtils;

public class BlindDialogHelper {

    private BlindDialogHelper() {
    }

    static void showInsert(final Context context, final RecyclerView recyclerView, final KillerDealerDbHelper db) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View blindDialogView = inflateBlindDialogView(null, context);

        builder.setView(blindDialogView)
                .setTitle(R.string.dialog_add_title)
                .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dInterface, int id) {
                        final Dialog d = (Dialog) dInterface;

                        EditText smallBlindEditText = (EditText) d.findViewById(R.id.blind_dialog_tv_blind_small);
                        EditText bigBlindEditText = (EditText) d.findViewById(R.id.blind_dialog_tv_blind_big);
                        NumberPicker riseTimeNumberPicker = (NumberPicker) d.findViewById(R.id.blind_dialog_tv_rise_time);

                        // TODO: set listeners to blinds to adapt size
//            BlindEditTextListener blindEditTextListener = new BlindEditTextListener(mBlind);
//            mBlind.addTextChangedListener(blindEditTextListener);

                        int smallBlind = Integer.parseInt(smallBlindEditText.getText().toString());
                        int bigBlind = Integer.parseInt(bigBlindEditText.getText().toString());
                        int riseTime = riseTimeNumberPicker.getValue();

                        db.insertBlind(smallBlind, bigBlind, riseTime);
                        BlindsAdapter adapter = (BlindsAdapter) recyclerView.getAdapter();
                        adapter.swapCursor();
                        recyclerView.smoothScrollToPosition(0);
                        CommonUtils.showToast(context, context.getString(R.string.blind_added));
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null);

        builder.show();
    }

    static void showUpdate(final Context context, final BlindsAdapter adapter, final KillerDealerDbHelper db, final BlindsAdapter.BlindViewHolder blindViewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View blindDialogView = inflateBlindDialogView(blindViewHolder, context);

        builder.setView(blindDialogView)
                .setTitle(R.string.dialog_update_title)
                .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dInterface, int id) {
                        final Dialog d = (Dialog) dInterface;

                        EditText smallBlindEditText = (EditText) d.findViewById(R.id.blind_dialog_tv_blind_small);
                        EditText bigBlindEditText = (EditText) d.findViewById(R.id.blind_dialog_tv_blind_big);
                        NumberPicker riseTimeNumberPicker = (NumberPicker) d.findViewById(R.id.blind_dialog_tv_rise_time);

                        // TODO: set listeners to blinds to adapt size
//            BlindEditTextListener blindEditTextListener = new BlindEditTextListener(mBlind);
//            mBlind.addTextChangedListener(blindEditTextListener);

                        int smallBlind = Integer.parseInt(smallBlindEditText.getText().toString());
                        int bigBlind = Integer.parseInt(bigBlindEditText.getText().toString());
                        int riseTime = riseTimeNumberPicker.getValue();

                        db.updateBlind(blindViewHolder.id, smallBlind, bigBlind, riseTime);
                        adapter.swapCursor();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null);

        builder.show();
    }

    private static View inflateBlindDialogView(BlindsAdapter.BlindViewHolder blindViewHolder, Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View blindDialogView = inflater.inflate(R.layout.blind_dialog, null);
        EditText smallBlindEditText = (EditText) blindDialogView.findViewById(R.id.blind_dialog_tv_blind_small);
        EditText bigBlindEditText = (EditText) blindDialogView.findViewById(R.id.blind_dialog_tv_blind_big);
        NumberPicker riseTimeNumberPicker = (NumberPicker) blindDialogView.findViewById(R.id.blind_dialog_tv_rise_time);
        riseTimeNumberPicker.setMinValue(0);
        riseTimeNumberPicker.setMaxValue(99);
        if (blindViewHolder != null) {
            smallBlindEditText.setText(blindViewHolder.mSmallBlind.getText());
            bigBlindEditText.setText(blindViewHolder.mBigBlind.getText());
            riseTimeNumberPicker.setValue(Integer.parseInt(blindViewHolder.mRiseTime.getText().toString()));
        }
        return blindDialogView;
    }

    //    private class BlindEditTextListener implements TextWatcher {
//        private EditText mBlind;
//
//        public BlindEditTextListener(EditText blind) {
//            mBlind = blind;
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
//                mBlinds.set(position, Integer.valueOf(input)); ???
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//            ConfigActivity.adaptBlindSize(mBlind);
//        }
//    }
}
