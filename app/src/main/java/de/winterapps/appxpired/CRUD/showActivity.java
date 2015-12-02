package de.winterapps.appxpired.CRUD;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;

/**
 * Created by D062400 on 15.10.2015.
 */
public class showActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_show);
        synchronizeItems();

    }

    public void synchronizeItems() {
        localDatabase database = new localDatabase(this);
        JSONArray foodEntries = database.getFood();

        //instantiate custom adapter
        customAdapter adapter = new customAdapter(foodEntries, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.listView);
        lView.setAdapter(adapter);
    }
}
