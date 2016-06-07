package de.winterapps.appxpired.Settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.winterapps.appxpired.CRUD.ShowFiles.CategorySpinner;
import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;

/**
 * Created by D062332 on 09.05.2016.
 */
public class templateActivity extends Activity{

    EditText name = (EditText) findViewById(R.id.templateEditName);
    EditText amount  = (EditText) findViewById(R.id.templateAmountEdit);
    EditText addInf = (EditText) findViewById(R.id.templateDescEdit);
    EditText expDur = (EditText) findViewById(R.id.templateExpireDuration);
    Spinner categorySpinner = (Spinner) findViewById(R.id.templateCategorySpinner);
    Spinner positionSpinner = (Spinner) findViewById(R.id.templatePositionSpinner);
    Spinner unitSpinner = (Spinner) findViewById(R.id.templateUnitSpinner);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_template);

        final Button buttonAdd = (Button) findViewById(R.id.templateAddButton);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(addToDatabase()){
                    Toast.makeText(templateActivity.this,"Template succesfully added to the database", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(templateActivity.this,"Error", Toast.LENGTH_LONG).show();
                }
            }
        });

        setSpinnerAdapters();
    }

    private void setSpinnerAdapters(){
        localDatabase database = new localDatabase(this);
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
        categorySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoriesSpinnerFormat));

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.units, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter2);

        JSONArray positions = database.getPositions();
        ArrayList positionsSpinnerFormat = new ArrayList();
        JSONObject positionElement;
        for (int i = 0; i < positions.length(); i++){
            try {
                positionElement = (JSONObject) positions.get(i);
                positionsSpinnerFormat.add(positionElement.get("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        positionSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, positionsSpinnerFormat));
    }

    public boolean addToDatabase(){
        localDatabase database = new localDatabase(this);
        JSONObject template = new JSONObject();
        try {
            template.put("name", name.getText().toString());
            template.put("amount", amount.getText().toString());
            template.put("additionalInformation", addInf.getText().toString());
            template.put("expireDuration", expDur.getText().toString());
            template.put("category_id", database.getCategory(categorySpinner.getSelectedItem().toString()));
            template.put("position_id", database.getPosition(positionSpinner.getSelectedItem().toString()));
            template.put("unit", unitSpinner.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return database.addTemplate(template);
    }
}
