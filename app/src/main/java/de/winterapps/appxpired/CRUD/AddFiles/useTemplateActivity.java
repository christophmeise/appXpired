package de.winterapps.appxpired.CRUD.AddFiles;

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

import de.winterapps.appxpired.CRUD.ShowFiles.*;
import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;

/**
 * Created by D062400 on 30.11.2015.
 */
public class useTemplateActivity extends Activity{

    localDatabase database = new localDatabase(this);
    ArrayAdapter<String> templateAdapter;
    String[] templateArray;
    Activity that = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_usetemplate);

        templateArray = getTemplateArray();
        templateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, templateArray);

        customAdapter adapter = null;
        adapter = new customAdapter(templateArray, that);

        final ListView templateList = (ListView) findViewById(R.id.templateList);
        //customAdapter templateAdapter = new customAdapter(categories,this);
        templateList.setAdapter(adapter);
    }

    public String [] getTemplateArray(){
        JSONArray templates = database.getTemplates();
        String [] templatesArray = new String[templates.length()];
        if (templates != null) {
            for (int i=0;i<templatesArray.length;i++){
                try {
                    JSONObject o = (JSONObject) templates.get(i);
                    templatesArray[i] = o.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return templatesArray;
    }
}

