package de.winterapps.appxpired;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import de.winterapps.appxpired.BarcodeScanner.scanActivity;
import de.winterapps.appxpired.CRUD.addActivity;
import de.winterapps.appxpired.CRUD.detailedActivity;
import de.winterapps.appxpired.CRUD.showActivity;
import de.winterapps.appxpired.CRUD.testActivity;

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
                Intent intent = new Intent(menuActivity.this, testActivity.class);
                startActivity(intent);
            }
        });
///////////////

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            View view_instance = findViewById(R.id.menuTableLayout);
            //View view_instance1 = (View)findViewById(R.id.addButtonBar);

            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) view_instance.getLayoutParams();
            //LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) view_instance1.getLayoutParams();

            //params1.setMargins(0,350,0,0);
            //view_instance1.setLayoutParams(params1);
            //params.setMargins(0,350,0,0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            params.addRule(RelativeLayout.ALIGN_BASELINE);
            //view_instance.setLayoutParams(params);
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
           View view_instance = findViewById(R.id.menuTableLayout);
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) view_instance.getLayoutParams();
            params.topMargin = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            view_instance.setLayoutParams(params);
           // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }

    }

}
