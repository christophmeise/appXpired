package de.winterapps.appxpired.BarcodeScanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import de.winterapps.appxpired.CRUD.addActivity;
import de.winterapps.appxpired.R;

/**
 * Created by D062400 on 19.11.2015.
 */
public class AddDialogFragment extends DialogFragment {

    static AddDialogFragment newInstance(int num) {
        AddDialogFragment f = new AddDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for Convenient Dialog Construction
        AlertDialog.Builder Builder = new AlertDialog.Builder(getActivity());
        Builder.setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.dialog_add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface Dialog, int ID) {
                        Intent intent = new Intent(getActivity(), uploadItem.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface Dialog, int ID) {
                        // User canceled the Dialog
                    }
                });
        // Create the AlertDialog Object and return it
        return Builder . create ();
    }
}

