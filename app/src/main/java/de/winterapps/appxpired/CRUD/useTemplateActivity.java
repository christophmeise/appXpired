package de.winterapps.appxpired.CRUD;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import de.winterapps.appxpired.R;

/**
 * Created by D062400 on 30.11.2015.
 */
public class useTemplateActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        this.setContentView(R.layout.activity_template);
    }
}
