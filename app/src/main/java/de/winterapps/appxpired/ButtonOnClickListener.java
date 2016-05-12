package de.winterapps.appxpired;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import de.winterapps.appxpired.CRUD.ShowFiles.showActivity;

/**
 * Created by D062400 on 12.05.2016.
 */
public class ButtonOnClickListener implements View.OnClickListener {

    Activity from;
    Class to;

    public ButtonOnClickListener(Activity from, Class to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(from, to);
        from.startActivity(intent);
    }
}
