package de.winterapps.appxpired.CRUD.ShowFiles;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import de.winterapps.appxpired.R;

/**
 * Created by Shark919 on 11.05.2016.
 */
public class CategorySpinner extends LayoutObject {

    public static void buildLayout(Context that) {
        showActivity.oCategorySpinner = (Spinner)((Activity) that).findViewById(R.id.categorySpinner1);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(that,
                R.array.categroy_array_show, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        showActivity.oCategorySpinner.setAdapter(categoryAdapter);
    }
}
