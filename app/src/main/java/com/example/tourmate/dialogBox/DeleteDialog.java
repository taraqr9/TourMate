package com.example.tourmate.dialogBox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.tourmate.R;
import com.example.tourmate.data.Databases;
import com.example.tourmate.views.ExpenseMain;

public class DeleteDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Databases mDatabases = new Databases(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete transaction")
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = getArguments().getInt("position");
                        mDatabases.deleteEntry(position);
                        ((ExpenseMain)getContext()).setAdapter();
                        ((ExpenseMain)getContext()).updateBudgetDisplay();
                        Toast.makeText(getActivity(), "Entry deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}
