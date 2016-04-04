package de.winterapps.appxpired.CRUD;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;

/**
 * Created by D062400 on 03.12.2015.
 */
public class testActivity extends Activity{
    int index;
    TextView name;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add);
        Intent intent = getIntent();
        index = intent.getIntExtra("index",0);


        name = (TextView) findViewById(R.id.detailedName);

        loadInitial();
    }

    private void loadInitial(){
        localDatabase database = new localDatabase(this);
        //JSONObject food = database.getFood(1);

    }
}
