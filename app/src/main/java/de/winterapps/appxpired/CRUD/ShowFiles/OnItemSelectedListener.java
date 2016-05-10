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

    JSONArray foodEntries;
    Context that;
    int mode;
    Context self;

    public OnItemSelectedListener(JSONArray array, Context context, int i) {
        foodEntries = array;
        that = context;
        mode = i;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        showActivity show = new showActivity();
        JSONArray foodEntriesFiltered = ((showActivity) that).filterEntries(foodEntries, adapterView.getItemAtPosition(i).toString(), memberVariables.FILTERMODES[mode].toString());
        customAdapter adapter = new customAdapter(foodEntriesFiltered, that);
        //handle listview and assign adapter
        ListView lView = (ListView)((Activity) that).findViewById(R.id.listView);
        lView.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
