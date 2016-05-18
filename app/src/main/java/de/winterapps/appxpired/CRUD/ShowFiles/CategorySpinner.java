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
public class CategorySpinner extends LayoutObject {

    public static void buildLayout(Context that) {
        showActivity.oCategorySpinner = (Spinner) ((showActivity) that).findViewById(R.id.categorySpinner1);
        localDatabase database = new localDatabase(that);
        JSONArray categories = database.getCategories();
        ArrayList categoriesSpinnerFormat = new ArrayList();
        JSONObject categoryElement;
        for (int i = 0; i < categories.length(); i++){
            try {
                categoryElement = (JSONObject) categories.get(i);
                categoriesSpinnerFormat.add(categoryElement.get("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        showActivity.oCategorySpinner.setAdapter(new ArrayAdapter<String>(that, android.R.layout.simple_spinner_dropdown_item, categoriesSpinnerFormat));
    }
}
