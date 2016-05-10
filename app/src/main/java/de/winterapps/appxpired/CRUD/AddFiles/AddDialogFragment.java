package de.winterapps.appxpired.CRUD.AddFiles;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import de.winterapps.appxpired.BarcodeScanner.scanActivity;
import de.winterapps.appxpired.R;

/**
 * Created by D062400 on 25.11.2015.
 */
public class AddDialogFragment extends DialogFragment {

    DialogFragment self = this;
    ProgressDialog progress = null;


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
        Builder.setMessage(R.string.add_dialog_message)
                .setPositiveButton(R.string.add_dialog_adding, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface Dialog, int ID) {

                    }
                })
                .setNegativeButton(R.string.add_dialog_scanning, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface Dialog, int ID) {

                      //  self.getActivity().findViewById(R.id.avloadingIndicatorView).buildLayer();
                        self.getActivity().findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);
                      //  self.getActivity().findViewById(R.id.avloadingIndicatorView).animate();


                        final Thread thread = new Thread() {
                            @Override
                            public void run() {

                                Intent i = new Intent(self.getActivity(), scanActivity.class);
                                startActivity(i);
                                self.getActivity().overridePendingTransition(R.anim.mainfadein, R.anim.mainfadeout);
                            };
                        };
                            thread.start();
                    }
                });
        // Create the AlertDialog Object and return it
        return Builder . create ();
    }

    public void dismissLoading(){
        progress.dismiss();
    }

    void startAnim(){
        this.getActivity().findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);
    }

    void stopAnim(){
        this.getActivity().findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE);
    }

}
