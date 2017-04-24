package net.chokethe.killerdealer.config.blinds;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import net.chokethe.killerdealer.R;
import net.chokethe.killerdealer.config.ConfigActivity;
import net.chokethe.killerdealer.db.KillerDealerDbHelper;
import net.chokethe.killerdealer.utils.CommonUtils;

public class BlindDialogHelper {

    private BlindDialogHelper() {
    }

    private enum BlindDialogAction {
        INSERT, UPDATE
    }

    public static void showInsert(final Context context, final RecyclerView recyclerView, final KillerDealerDbHelper db) {
        BlindsAdapter adapter = (BlindsAdapter) recyclerView.getAdapter();
        show(context, db, BlindDialogAction.INSERT, R.string.dialog_add_title, adapter, recyclerView, null);
    }

    static void showUpdate(final Context context, final BlindsAdapter adapter, final KillerDealerDbHelper db, final BlindsAdapter.BlindViewHolder blindViewHolder) {
        show(context, db, BlindDialogAction.UPDATE, R.string.dialog_update_title, adapter, null, blindViewHolder);
    }

    private static void show(final Context context, final KillerDealerDbHelper db,
                             final BlindDialogAction action, int title, final BlindsAdapter adapter,
                             // INSERT
                             final RecyclerView recyclerView,
                             // UPDATE
                             final BlindsAdapter.BlindViewHolder blindViewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View blindDialogView = inflateBlindDialogView(blindViewHolder, context);
        setTextWatchers(context.getResources(), blindDialogView);

        builder.setView(blindDialogView)
                .setTitle(title)
                .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dInterface, int id) {
                        final Dialog d = (Dialog) dInterface;

                        EditText smallBlindEditText = (EditText) d.findViewById(R.id.blind_dialog_et_blind_small);
                        EditText bigBlindEditText = (EditText) d.findViewById(R.id.blind_dialog_et_blind_big);
                        NumberPicker riseTimeNumberPicker = (NumberPicker) d.findViewById(R.id.blind_dialog_np_rise_minutes);
                        int smallBlind = Integer.parseInt(smallBlindEditText.getText().toString());
                        int bigBlind = Integer.parseInt(bigBlindEditText.getText().toString());
                        int riseTime = riseTimeNumberPicker.getValue();

                        if (smallBlind > bigBlind) {
                            int tempBlind = smallBlind;
                            smallBlind = bigBlind;
                            bigBlind = tempBlind;
                        }
                        if (action.equals(BlindDialogAction.INSERT)) {
                            db.insertBlind(smallBlind, bigBlind, riseTime);
                        } else if (action.equals(BlindDialogAction.UPDATE)) {
                            db.updateBlind(blindViewHolder.id, smallBlind, bigBlind, riseTime);
                        }
                        adapter.swapCursor();
                        if (action.equals(BlindDialogAction.INSERT)) {
                            recyclerView.smoothScrollToPosition(0);
                            CommonUtils.showToast(context, context.getString(R.string.blind_added));
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null);

        builder.show();
    }

    private static View inflateBlindDialogView(BlindsAdapter.BlindViewHolder blindViewHolder, Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View blindDialogView = inflater.inflate(R.layout.blind_dialog, null);
        NumberPicker riseTimeNumberPicker = (NumberPicker) blindDialogView.findViewById(R.id.blind_dialog_np_rise_minutes);
        riseTimeNumberPicker.setMinValue(1);
        riseTimeNumberPicker.setMaxValue(99);
        if (blindViewHolder != null) {
            riseTimeNumberPicker.setValue(Integer.parseInt(blindViewHolder.mRiseTime.getText().toString()));

            EditText smallBlindEditText = (EditText) blindDialogView.findViewById(R.id.blind_dialog_et_blind_small);
            EditText bigBlindEditText = (EditText) blindDialogView.findViewById(R.id.blind_dialog_et_blind_big);
            smallBlindEditText.setText(blindViewHolder.mSmallBlind.getText());
            bigBlindEditText.setText(blindViewHolder.mBigBlind.getText());
        }
        return blindDialogView;
    }

    private static void setTextWatchers(Resources res, View blindDialogView) {
        EditText smallBlindEditText = (EditText) blindDialogView.findViewById(R.id.blind_dialog_et_blind_small);
        EditText bigBlindEditText = (EditText) blindDialogView.findViewById(R.id.blind_dialog_et_blind_big);

        smallBlindEditText.addTextChangedListener(new BlindEditTextListener(res, smallBlindEditText));
        bigBlindEditText.addTextChangedListener(new BlindEditTextListener(res, bigBlindEditText));
    }

    private static class BlindEditTextListener implements TextWatcher {
        private Resources mRes;
        private EditText mBlind;

        BlindEditTextListener(Resources res, EditText blind) {
            mRes = res;
            mBlind = blind;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            ConfigActivity.adaptBlindSize(mRes, mBlind);
        }
    }
}
