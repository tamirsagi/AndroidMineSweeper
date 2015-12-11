package com.minesweeper.UI.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.minesweeper.BL.DB.DbManager;
import com.minesweeper.BL.DB.PlayerRecord;
import com.minesweeper.UI.Activities.GameActivity;
import com.minesweeper.UI.Activities.R;

/**
 * This class represents a Dialog which pops up when user score should be saved in DB
 * @author  Tamir Sagi
 *
 */
public class DetailsDialog extends DialogFragment {
    private static final String TAG = "Details Dialog";
    private static Bundle data;

    public static DetailsDialog newInstance(Bundle savedInstanceState) {
        DetailsDialog  dialog = new DetailsDialog();
        data = savedInstanceState;
        dialog.setArguments(savedInstanceState);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder custom_dialog = new AlertDialog.Builder(getActivity());
        custom_dialog.setTitle(R.string.enterDetailsRecord);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View alertDialogView = inflater.inflate(R.layout.prompts, null);
        custom_dialog.setView(alertDialogView);
        final EditText name = (EditText) alertDialogView.findViewById(R.id.et_prompt_player_Name_edit);
        final TextView confirmationMessage = (TextView) alertDialogView.findViewById(R.id.tv_prompt_confirmation_message);
        final Button bth_close = (Button)alertDialogView.findViewById(R.id.bth_prompt_close);
        bth_close.setEnabled(false);
        confirmationMessage.setVisibility(View.GONE);
        final Button save = (Button) alertDialogView.findViewById(R.id.bth_prompt_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationMessage.setVisibility(View.VISIBLE);
                if (name.getText() == null || name.getText().toString().isEmpty()) {
                    confirmationMessage.setText(R.string.err_name_empty_or_null);
                    confirmationMessage.setTextColor(Color.RED);
                }
                else if(name.getText().length() > DbManager.MAX_PLAYER_NAME_LEENGTH){
                    confirmationMessage.setText(R.string.err_name_too_long);
                    confirmationMessage.setTextColor(Color.RED);
                }
                else {
                    PlayerRecord record = new PlayerRecord();
                    record.setFullName(name.getText().toString());
                    record.setRoundTime(data.getString(GameActivity.KEY_ROUND_TIME));
                    record.setCity(data.getString(GameActivity.KEY_LOCATION_CITY));
                    record.setCountry(data.getString(GameActivity.KEY_LOCATION_COUNTRY));
                    //get strings
                    String latitude = data.getString(GameActivity.KEY_LOCATION_LATITUDE);
                    String longitude = data.getString(GameActivity.KEY_LOCATION_LONGITUDE);
                    //parse to double
                    record.setLatitude(Double.parseDouble(latitude));
                    record.setLongitude(Double.parseDouble(longitude));
                    record.setDate(data.getString(GameActivity.KEY_DATE));
                    String table = data.getString(GameActivity.KEY_TABLE);

                    if(DbManager.getInstance(alertDialogView.getContext()).addPlayerRecord(table,record)) {
                        confirmationMessage.setText(R.string.info_saved);
                        confirmationMessage.setTextColor(Color.GREEN);
                        save.setEnabled(false);
                        bth_close.setEnabled(true);
                    }
                }
            }
        });

        bth_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusable(false);
                getDialog().dismiss();
            }
        });
        return custom_dialog.create();
    }

    public static void showDialog(FragmentManager fm,Bundle details) {
        DialogFragment newFragment = DetailsDialog.newInstance(details);
        newFragment.show(fm, TAG);
    }

}
