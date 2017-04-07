package net.chokethe.killerdealer.config;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import net.chokethe.killerdealer.R;

public class RebuyDialogHelper {

    private RebuyDialogHelper() {
    }

    static void show(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View blindDialogView = inflater.inflate(R.layout.rebuy_dialog, null);

        NumberPicker hours = (NumberPicker) blindDialogView.findViewById(R.id.config_tv_rebuy_hours);
        hours.setMinValue(0);
        hours.setMaxValue(23);
        hours.setValue(2);
        NumberPicker minutes = (NumberPicker) blindDialogView.findViewById(R.id.config_tv_rebuy_minutes);
        minutes.setMinValue(0);
        minutes.setMaxValue(59);
        minutes.setValue(0);

        builder.setView(blindDialogView)
                .setTitle(R.string.dialog_update_title)
                .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dInterface, int id) {
                        final Dialog d = (Dialog) dInterface;

//                        EditText smallBlindEditText = (EditText) d.findViewById(R.id.blind_dialog_tv_blind_small);
//                        EditText bigBlindEditText = (EditText) d.findViewById(R.id.blind_dialog_tv_blind_big);
//                        NumberPicker riseTimeNumberPicker = (NumberPicker) d.findViewById(R.id.blind_dialog_tv_rise_time);

                        // TODO: set listeners to blinds to adapt size
//            BlindEditTextListener blindEditTextListener = new BlindEditTextListener(mBlind);
//            mBlind.addTextChangedListener(blindEditTextListener);

//                        int smallBlind = Integer.parseInt(smallBlindEditText.getText().toString());
//                        int bigBlind = Integer.parseInt(bigBlindEditText.getText().toString());
//                        int riseTime = riseTimeNumberPicker.getValue();

                        // TODO: set preferences
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null);

        builder.show();
    }
}
