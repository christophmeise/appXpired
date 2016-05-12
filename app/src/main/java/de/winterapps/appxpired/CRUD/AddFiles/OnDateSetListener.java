package de.winterapps.appxpired.CRUD.AddFiles;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by D062400 on 12.05.2016.
 */
public class OnDateSetListener implements DatePickerDialog.OnDateSetListener {

    Context self;

    public OnDateSetListener(Context context) {
        this.self = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // TODO Auto-generated method stub
        addActivity.myCalendar.set(Calendar.YEAR, year);
        addActivity.myCalendar.set(Calendar.MONTH, monthOfYear);
        addActivity.myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        addActivity.updateLabel(self);
    }
}
