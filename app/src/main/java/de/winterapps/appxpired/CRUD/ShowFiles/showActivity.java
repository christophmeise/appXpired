package de.winterapps.appxpired.CRUD.ShowFiles;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;

/**
 * Created by D062400 on 15.10.2015.
 */
public class showActivity extends Activity {

    public static final String ALL = "All";
    static SearchView oSearch;
    static Spinner oCategorySpinner;
    static Spinner oPositionSpinner;
    static JSONArray foodEntries;
    static JSONArray foodEntriesFiltered = null;
    Context self = this;
    JSONObject food = null;
    String foodCategory = null;
    String foodPosition = null;
    String foodName = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_show);
        // initial data
        synchronizeItems("");
        // build Layout, set Listeners
        initializeLayout();
    }

    private void initializeLayout() {
        SearchBar.buildLayout(self);
        PositionSpinner.buildLayout(self);
        CategorySpinner.buildLayout(self);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        oCategorySpinner.setOnItemSelectedListener(new OnItemSelectedListener(self, FilterModes.CATEGORY));
        oPositionSpinner.setOnItemSelectedListener(new OnItemSelectedListener(self, FilterModes.POSITION));
    }

    public void synchronizeItems(String query) {
        loadDatabaseEntries();

        if(query.isEmpty() == true){
                if (oCategorySpinner == null || (oCategorySpinner != null && oCategorySpinner.getSelectedItem().equals(ALL))){
                    //initial load || All selected
                    foodEntriesFiltered = foodEntries;
                } else{
                    filterEntries(foodEntries, query, FilterModes.CATEGORY);
                }
        } else{
                filterEntries(foodEntries, query, (String) FilterModes.SEARCH);
        }

        buildList(null); //load initial list
    }

    public void buildList(JSONArray entries) {
        //  instantiate custom adapter
        customAdapter adapter = null;
        if(entries != null){ // if filter is applied
            adapter = new customAdapter(foodEntriesFiltered, this);
        } else{ // show full list
            adapter = new customAdapter(foodEntries, this);
        }

        //  handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.listView);
        lView.setAdapter(adapter);
    }

    private void loadDatabaseEntries() {
        localDatabase database = new localDatabase(this);
        foodEntries = database.getFood();
    }

    // mode -> category / position / search
    public void filterEntries(JSONArray foodEntries, String query, String mode) {

        foodEntriesFiltered = copy(foodEntries);
        for (int i = foodEntriesFiltered.length() - 1; i >= 0; i--){
            // loads JSONObject food, String foodPosition, String foodCategory, String foodName
            // from foodEntries

            getFoodObjectsFromArray(i);
            query = query.toLowerCase();

            if (mode == FilterModes.SEARCH) {
                filterWithSearch(query, i);
            }

            if (mode == FilterModes.CATEGORY){
                if (oCategorySpinner != null && (!foodCategory.equalsIgnoreCase(query) && !oCategorySpinner.getSelectedItem().equals(ALL))){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        foodEntriesFiltered.remove(i);
                    }
                }
            }

            if (mode == FilterModes.POSITION){
                if (oPositionSpinner != null && !foodCategory.equalsIgnoreCase(query) && !oPositionSpinner.getSelectedItem().equals(ALL)){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        foodEntriesFiltered.remove(i);
                    }
                }
            }
        }
    }

    private JSONArray copy (JSONArray original){
        JSONArray copy=new JSONArray();
        for (int i = 0; i < original.length(); i++){
            try {
                copy.put(i , original.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return copy;
    }

    private void filterWithSearch(String query, int i) {
        try {
            String searchSubstring = null;
            if (foodName.length() >= query.length()) {
                searchSubstring = foodName.substring(0, query.length());
                searchSubstring = searchSubstring.toLowerCase();
            } else {
                searchSubstring = "";
            }
            if (!searchSubstring.equals(query)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    foodEntriesFiltered.remove(i);
                }
            }
        } catch (Error e) {
            Log.e("String parse error: ", e.getLocalizedMessage());
        }

        Positions pos = new Positions();
        foodPosition = pos.parseValue(Integer.parseInt(foodPosition.toString()));
        if (!foodPosition.equalsIgnoreCase(query) && !oPositionSpinner.getSelectedItem().equals(ALL)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                foodEntriesFiltered.remove(i);
            }
        }
    }

    private void getFoodObjectsFromArray(int i) {
        try {
            food = (JSONObject) (foodEntries.get(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            foodCategory = food.get("categoryId").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            foodPosition = food.get("positionId").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            foodName = food.get("name").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
