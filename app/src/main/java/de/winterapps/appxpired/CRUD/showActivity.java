package de.winterapps.appxpired.CRUD;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

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
        localDatabase x = new localDatabase(this);
        x.getFood();
    }
}
