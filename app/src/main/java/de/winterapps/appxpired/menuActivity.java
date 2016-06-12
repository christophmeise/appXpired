package de.winterapps.appxpired;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import de.winterapps.appxpired.BarcodeScanner.scanActivity;
import de.winterapps.appxpired.CRUD.AddFiles.AddDialogFragment;
import de.winterapps.appxpired.CRUD.AddFiles.addActivity;
import de.winterapps.appxpired.CRUD.ShowFiles.showActivity;
import de.winterapps.appxpired.Settings.settingsActivity;

public class menuActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initializeLayout();
        /*tttt*/
    }

    private void initializeLayout() {
        final Button buttonAdd = (Button) findViewById(R.id.menuAddButton);
        buttonAdd.setOnClickListener(new ButtonOnClickListener(menuActivity.this, addActivity.class));

        final Button buttonShow = (Button) findViewById(R.id.menuShowButton);
        buttonShow.setOnClickListener(new ButtonOnClickListener(menuActivity.this, showActivity.class));

        final Button buttonScan = (Button) findViewById(R.id.menuScanButton);
        buttonScan.setOnClickListener(new ButtonOnClickListener(menuActivity.this, scanActivity.class));

        final Button buttonSettings = (Button) findViewById(R.id.menuSettingsButton);
        buttonSettings.setOnClickListener(new ButtonOnClickListener(menuActivity.this, settingsActivity.class));
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
