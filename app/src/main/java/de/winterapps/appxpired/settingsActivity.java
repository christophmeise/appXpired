package de.winterapps.appxpired;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        final Button buttonAdd = (Button) findViewById(R.id.buttonTemplate);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(settingsActivity.this, templateActivity.class);
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
