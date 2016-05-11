package de.winterapps.appxpired.CRUD.ShowFiles;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import de.winterapps.appxpired.R;

/**
 * Created by Shark919 on 11.05.2016.
 */
public class PositionSpinner extends LayoutObject {

    public static void buildLayout(Context that) {
        showActivity.oPositionSpinner = (Spinner)((Activity) that).findViewById(R.id.positionSpinner1);
        ArrayAdapter<CharSequence> positionAdapter = ArrayAdapter.createFromResource(that,
                R.array.planets_array_show, android.R.layout.simple_spinner_item);
        positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        showActivity.oPositionSpinner.setAdapter(positionAdapter);
    }
}
