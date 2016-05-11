package de.winterapps.appxpired.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import de.winterapps.appxpired.R;

/**
 * Created by D062400 on 15.10.2015.
 */
public class settingsActivity extends Activity{

    String currentUsername;
    String currentHousehold;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);

        final Button buttonTemplate = (Button) findViewById(R.id.buttonTemplate);
        buttonTemplate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(settingsActivity.this, templateActivity.class);
                startActivity(intent);
            }
        });

        final Button buttonCategory = (Button) findViewById(R.id.buttonCategorie);
        buttonCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(settingsActivity.this, categoryActivity.class);
                startActivity(intent);
            }
        });
        /*memberVariables members = memberVariables.sharedInstance;
        currentHousehold = members.getHousehold();
        currentUsername = members.getUsername();
        getLoginData();*/
    }

    /*public void getLoginData() {
        TextView userTextView = (TextView) findViewById(R.id.settingsTextViewUser);
        TextView householdTextView = (TextView) findViewById(R.id.settingsTextViewHousehold);
      //  Log.d("username:",currentUsername);
        userTextView.setText(currentUsername);
        householdTextView.setText(currentHousehold);
    }*/
}
