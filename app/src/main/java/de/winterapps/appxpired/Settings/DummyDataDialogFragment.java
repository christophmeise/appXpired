package de.winterapps.appxpired.Settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import de.winterapps.appxpired.BarcodeScanner.scanActivity;
import de.winterapps.appxpired.CRUD.AddFiles.AddDialogFragment;
import de.winterapps.appxpired.CRUD.ShowFiles.showActivity;
import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;

/**
 * Created by Shark919 on 08.06.2016.
 */
public class DummyDataDialogFragment extends DialogFragment {
    DialogFragment self = this;
    ProgressDialog progress = null;


    public static DummyDataDialogFragment newInstance(int num) {
        DummyDataDialogFragment f = new DummyDataDialogFragment();

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
        Builder.setMessage(R.string.dummy_dialog_message)
                .setPositiveButton(R.string.dummy_dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface Dialog, int ID) {
                        localDatabase database = new localDatabase(self.getContext());
                        JSONObject category = new JSONObject();
                        JSONObject position = new JSONObject();
                        try {
                            category.put("name", "Meat");
                            database.addCategory(category);
                            category.put("name", "Noodles");
                            database.addCategory(category);
                            category.put("name", "Fluids");
                            database.addCategory(category);
                            category.put("name", "Vegetables");
                            database.addCategory(category);
                            category.put("name", "Fruits");
                            database.addCategory(category);
                            category.put("name", "Sweets");
                            database.addCategory(category);
                            position.put("name", "Refridgerator");
                            database.addPosition(position);
                            position.put("name", "Shelf");
                            database.addPosition(position);
                            position.put("name", "Basement");
                            database.addPosition(position);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        categoryActivity.updateScreen(database, self.getContext());
                    }
                })
                .setNegativeButton(R.string.dummy_dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface Dialog, int ID) {

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
