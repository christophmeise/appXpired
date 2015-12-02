package de.winterapps.appxpired.CRUD;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;

/**
 * Created by D062400 on 01.12.2015.
 */
public class showDetailedActivity extends Activity{

    TextView name;
    int index;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        android.os.Debug.waitForDebugger();
        super.onCreate(savedInstanceState, persistentState);
        this.setContentView(R.layout.activity_detaileditem);
        Intent intent = getIntent();
        index = intent.getIntExtra("index",0);


        name = (TextView) findViewById(R.id.detailedName);

        loadInitial();
    }

    private void loadInitial(){
        localDatabase database = new localDatabase(this);
        JSONObject food = database.getFood(index);


    }
}
