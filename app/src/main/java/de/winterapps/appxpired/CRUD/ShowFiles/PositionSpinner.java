package de.winterapps.appxpired.CRUD.ShowFiles;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;

/**
 * Created by Shark919 on 11.05.2016.
 */
public class PositionSpinner extends LayoutObject {

    public static void buildLayout(Context that) {
        showActivity.oPositionSpinner = (Spinner) ((showActivity) that).findViewById(R.id.positionSpinner1);
        localDatabase database = new localDatabase(that);
        JSONArray positions = database.getPositions();
        ArrayList positionsSpinnerFormat = new ArrayList();
        JSONObject positionElement;
        positionsSpinnerFormat.add("All");
        for (int i = 0; i < positions.length(); i++){
            try {
                positionElement = (JSONObject) positions.get(i);
                positionsSpinnerFormat.add(positionElement.get("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        showActivity.oPositionSpinner.setAdapter(new ArrayAdapter<String>(that, android.R.layout.simple_spinner_dropdown_item, positionsSpinnerFormat));
    }
}
