package de.winterapps.appxpired.Settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.winterapps.appxpired.CRUD.ShowFiles.customAdapter;
import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;

/**
 * Created by D062332 on 11.05.2016.
 */
public class categoryActivity extends Activity{

    localDatabase database = new localDatabase(this);
    ArrayAdapter<String> categoryAdapter;
    String [] categoryArray;
    Activity that = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_category);

        categoryArray = getCategoryArray();
        categoryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, categoryArray);

        final ListView categoryList = (ListView) findViewById(R.id.categoryList);
        //customAdapter categoryAdapter = new customAdapter(categories,this);
        categoryList.setAdapter(categoryAdapter);
        final Button buttonAdd = (Button) findViewById(R.id.categoryAddButton);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(addToDatabase()){
                    categoryArray = getCategoryArray();
                    categoryAdapter = new ArrayAdapter<String>(that,android.R.layout.simple_list_item_1, categoryArray);
                    categoryList.setAdapter(categoryAdapter);
                    Toast.makeText(categoryActivity.this,"Category succesfully added to the database", Toast.LENGTH_SHORT).show();
                    //categoryAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(categoryActivity.this,"Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public boolean addToDatabase(){
        JSONObject category = new JSONObject();
        EditText name = (EditText) findViewById(R.id.categoryName);
        try {
            category.put("name", name.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return database.addCategory(category);
    }

    public String [] getCategoryArray(){
        JSONArray categories = database.getCategories();
        String [] categoriesArray = new String[categories.length()];
        if (categories != null) {
            for (int i=0;i<categoriesArray.length;i++){
                try {
                    JSONObject o = (JSONObject) categories.get(i);
                    categoriesArray[i] = o.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return categoriesArray;
    }
}
