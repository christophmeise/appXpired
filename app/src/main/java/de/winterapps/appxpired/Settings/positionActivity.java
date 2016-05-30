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

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;

/**
 * Created by D062332 on 12.05.2016.
 */
public class positionActivity extends Activity{
    localDatabase database = new localDatabase(this);
    Activity that = this;
    String [] positionArray;
    ArrayAdapter<String> positionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_position);

        positionArray = getPositionArray();
        positionAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, positionArray);

        final ListView positionList = (ListView) findViewById(R.id.positionList);
        positionList.setAdapter(positionAdapter);
        final Button buttonAdd = (Button) findViewById(R.id.positionAddButton);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(addToDatabase()){
                    positionArray = getPositionArray();
                    positionAdapter = new ArrayAdapter<String>(that,android.R.layout.simple_list_item_1, positionArray);
                    positionList.setAdapter(positionAdapter);
                    Toast.makeText(positionActivity.this,"Position succesfully added to the database", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(positionActivity.this,"Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public boolean addToDatabase(){
        JSONObject position = new JSONObject();
        EditText name = (EditText) findViewById(R.id.positionName);
        try {
            position.put("name", name.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return database.addPosition(position);
    }

    public String [] getPositionArray(){
        JSONArray positions = database.getPositions();
        String [] positionsArray = new String[positions.length()];
        if (positions != null) {
            for (int i=0;i<positionsArray.length;i++){
                try {
                    JSONObject o = (JSONObject) positions.get(i);
                    positionsArray[i] = o.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return positionsArray;
    }
}
