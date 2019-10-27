package com.example.vehicleassistance;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatDialogFragment;

public class FilterDialog extends AppCompatDialogFragment {
    private RadioGroup filter_radio_group;
    private FilterDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (FilterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ "must implement FilterDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.filter_dialog, null);

        filter_radio_group = view.findViewById(R.id.filter_radio_group);

        builder.setView(view)
                .setTitle("Select Filter")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id= filter_radio_group.getCheckedRadioButtonId();
                        View selectedRadioButton = filter_radio_group.findViewById(id);
                        int index = filter_radio_group.indexOfChild(selectedRadioButton);

                        RadioButton r = (RadioButton) filter_radio_group.getChildAt(index);
                        String filter = r.getText().toString();
                        listener.applyfilter(filter);
                    }
                });
        return builder.create();
    }

    public interface FilterDialogListener{
        void applyfilter(String Filter);
    }
}
