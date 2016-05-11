package de.winterapps.appxpired.CRUD.ShowFiles;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.memberVariables;

/**
 * Created by D062400 on 10.05.2016.
 */
public class OnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    Context that;
    String mode;
    Context self;

    public OnItemSelectedListener(Context context, String mode) {
        that = context;
        this.mode = mode;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((showActivity) that).filterEntries(showActivity.foodEntries, adapterView.getItemAtPosition(i).toString(), mode);
        ((showActivity) that).buildList(showActivity.foodEntriesFiltered);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
