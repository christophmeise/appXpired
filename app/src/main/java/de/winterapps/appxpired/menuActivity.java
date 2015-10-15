package de.winterapps.appxpired;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class menuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

///////////////Initialize all buttons in menu
        final Button buttonAdd = (Button) findViewById(R.id.menuAddButton);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(menuActivity.this, addActivity.class);
                startActivity(intent);
            }
        });

        final Button buttonShow = (Button) findViewById(R.id.menuShowButton);
        buttonShow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(menuActivity.this, showActivity.class);
                startActivity(intent);
            }
        });

        final Button buttonScan = (Button) findViewById(R.id.menuScanButton);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(menuActivity.this, scanActivity.class);
                startActivity(intent);
            }
        });

        final Button buttonSettings = (Button) findViewById(R.id.menuSettingsButton);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(menuActivity.this, settingsActivity.class);
                startActivity(intent);
            }
        });
///////////////

    }
}
