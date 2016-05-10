package de.winterapps.appxpired.CRUD.ShowFiles;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.winterapps.appxpired.CRUD.ShowFiles.Positions;
import de.winterapps.appxpired.CRUD.ShowFiles.customAdapter;
import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;

/**
 * Created by D062400 on 15.10.2015.
 */
public class showActivity extends Activity {

    SearchView oSearch;
    Context that = this;
    Spinner oCategorySpinner;
    Spinner oPositionSpinner;
    JSONArray foodEntries;
    JSONObject food = null;
    String foodCategory = null;
    String foodPosition = null;
    String foodName = null;

    String[] FILTERMODES = {"category", "position", "search"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_show);
        // initial data
        synchronizeItems("");
        // set Listeners etc.
        initializeLayout();
    }

    private void initializeLayout() {
        //Searchbar
        oSearch = (SearchView)findViewById(R.id.searchView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            oSearch.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            oSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {

                    synchronizeItems(query);
                    return true;
                }

            });
        }
        // Category spinner
        oCategorySpinner = (Spinner)findViewById(R.id.categorySpinner1);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.categroy_array_show, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oCategorySpinner.setAdapter(categoryAdapter);
        oCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    JSONArray foodEntriesFiltered = filterEntries(foodEntries, adapterView.getItemAtPosition(i).toString(), FILTERMODES[0].toString());
                    customAdapter adapter = new customAdapter(foodEntriesFiltered, that);
                    //handle listview and assign adapter
                    ListView lView = (ListView)findViewById(R.id.listView);
                    lView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });
        // Position spinner
        oPositionSpinner = (Spinner)findViewById(R.id.positionSpinner1);
        ArrayAdapter<CharSequence> positionAdapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array_show, android.R.layout.simple_spinner_item);
        positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oPositionSpinner.setAdapter(positionAdapter);
        oPositionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    JSONArray foodEntriesFiltered = filterEntries(foodEntries, adapterView.getItemAtPosition(i).toString(), FILTERMODES[0].toString());
                    customAdapter adapter = new customAdapter(foodEntriesFiltered, that);
                    //handle listview and assign adapter
                    ListView lView = (ListView)findViewById(R.id.listView);
                    lView.setAdapter(adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void synchronizeItems(String query) {
        localDatabase database = new localDatabase(this);
        foodEntries = database.getFood();
        JSONArray foodEntriesFiltered = null;

        if(query.isEmpty() == true){
                if (oCategorySpinner == null || (oCategorySpinner != null && oCategorySpinner.getSelectedItem().equals("All"))){
                    //initial load || All selected
                    foodEntriesFiltered = foodEntries;
                } else{
                    foodEntriesFiltered = filterEntries(foodEntries, query, (String) FILTERMODES[0]);
                }
        } else{
                foodEntriesFiltered = filterEntries(foodEntries, query, (String) FILTERMODES[2]);
        }

        //instantiate custom adapter
        customAdapter adapter = null;
        if(foodEntriesFiltered != null){
            adapter = new customAdapter(foodEntriesFiltered, this);
        } else{
            adapter = new customAdapter(foodEntries, this);
        }

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.listView);
        lView.setAdapter(adapter);
    }
    // mode -> category / position / search
    private JSONArray filterEntries(JSONArray foodEntries, String query, String mode) {
        for (int i = foodEntries.length() - 1; i >= 0; i--){
            // loads JSONObject food, String foodPosition, String foodCategory, String foodName
            // from foodEntries
            loadFoodData(i);
            query = query.toLowerCase();

            // search
            if (mode == FILTERMODES[2].toString()) {
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
                            foodEntries.remove(i);
                        }
                    }
                } catch (Error e) {
                    Log.e("String parse error: ", e.getLocalizedMessage());
                }
                Positions pos = new Positions();
                foodPosition = pos.parseValue(Integer.parseInt(foodPosition.toString()));
                if (!foodPosition.equalsIgnoreCase(query) && !oPositionSpinner.getSelectedItem().equals("All")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        foodEntries.remove(i);
                    }
                }
            }

            // category
            if (mode == FILTERMODES[0].toString()){
                if (!foodCategory.equalsIgnoreCase(query) && !oCategorySpinner.getSelectedItem().equals("All")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        foodEntries.remove(i);
                    }
                }
            }
            // position
            if (mode == FILTERMODES[1].toString()){
                if (!foodCategory.equalsIgnoreCase(query) && !oPositionSpinner.getSelectedItem().equals("All")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        foodEntries.remove(i);
                    }
                }
            }
        }
        return foodEntries;
    }

    private void loadFoodData(int i) {
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
