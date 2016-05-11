package de.winterapps.appxpired.Settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;

/**
 * Created by D062332 on 09.05.2016.
 */
public class templateActivity extends Activity{

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
    }

    public boolean addToDatabase(){
        localDatabase database = new localDatabase(this);
        JSONObject template = new JSONObject();
        EditText name = (EditText) findViewById(R.id.templateEditName);
        EditText amount  = (EditText) findViewById(R.id.templateAmountEdit);
        EditText addInf = (EditText) findViewById(R.id.templateDescEdit);
        EditText expDur = (EditText) findViewById(R.id.templateExpireDuration);
        try {
            template.put("name", name.getText().toString());
            template.put("amount", amount.getText().toString());
            template.put("additionalInformation", addInf.getText().toString());
            template.put("expireDuration", expDur.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return database.addTemplate(template);
    }
}
