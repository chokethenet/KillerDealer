package net.chokethe.killerdealer.config;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import net.chokethe.killerdealer.R;
import net.chokethe.killerdealer.RebuyTimerConfigHolder;

public class RebuyDialogHelper {

    private RebuyDialogHelper() {
    }

    static void show(final Context context, final RebuyTimerConfigHolder rebuyTimerConfigHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View blindDialogView = inflater.inflate(R.layout.rebuy_dialog, null);

        NumberPicker hours = (NumberPicker) blindDialogView.findViewById(R.id.rebuy_np_rebuy_hours);
        hours.setMinValue(0);
        hours.setMaxValue(23);
        hours.setValue(rebuyTimerConfigHolder.getRebuyHours());
        NumberPicker minutes = (NumberPicker) blindDialogView.findViewById(R.id.rebuy_np_rebuy_minutes);
        minutes.setMinValue(0);
        minutes.setMaxValue(59);
        minutes.setValue(rebuyTimerConfigHolder.getRebuyMinutes());

        builder.setView(blindDialogView)
                .setTitle(R.string.dialog_update_title)
                .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dInterface, int id) {
                        final Dialog d = (Dialog) dInterface;

                        NumberPicker rebuyHours = (NumberPicker) d.findViewById(R.id.rebuy_np_rebuy_hours);
                        NumberPicker rebuyMinutes = (NumberPicker) d.findViewById(R.id.rebuy_np_rebuy_minutes);

                        rebuyTimerConfigHolder.saveRebuyTimePref(context, rebuyHours.getValue(), rebuyMinutes.getValue());

                        Activity configActivity = ((Activity)context);
                        TextView rebuyHoursTextView = (TextView) configActivity.findViewById(R.id.config_tv_rebuy_hours);
                        TextView rebuyMinutesTextView = (TextView) configActivity.findViewById(R.id.config_tv_rebuy_minutes);
                        rebuyHoursTextView.setText(rebuyTimerConfigHolder.getRebuyStringHours());
                        rebuyMinutesTextView.setText(rebuyTimerConfigHolder.getRebuyStringMinutes());

//                        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//                        activityManager.
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null);

        builder.show();
    }
}
