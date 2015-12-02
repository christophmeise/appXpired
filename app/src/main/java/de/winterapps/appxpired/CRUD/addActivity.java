package de.winterapps.appxpired.CRUD;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;
import de.winterapps.appxpired.memberVariables;

/**
 * Created by Christoph on 12.10.2015.
 */
public class addActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText dateEdit;
    Button addButton;
    EditText editName;
    Spinner spinner;
    Spinner spinner2;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           /* View view_instance = (View)findViewById(R.id.addRelativeLayout);
            View view_instance1 = (View)findViewById(R.id.addButtonBar);

            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) view_instance.getLayoutParams();
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) view_instance1.getLayoutParams();

            params1.setMargins(0,350,0,0);
            view_instance1.setLayoutParams(params1);

            params.topMargin = 350;
            params.setMargins(0,350,0,0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            params.addRule(RelativeLayout.ALIGN_BASELINE);*/
            //view_instance.setLayoutParams(params);
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
           /* View view_instance = (View)findViewById(R.id.addButtonBar);
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) view_instance.getLayoutParams();
            params.topMargin = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            view_instance.setLayoutParams(params);*/
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        this.findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE); //turn off loading indicator
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        Button addButton = (Button) findViewById(R.id.addAddButton);
        editName = (EditText) findViewById(R.id.editName);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        Spinner spinner5 = (Spinner) findViewById(R.id.spinner5);

        editName.setText(((memberVariables) ((Activity) this).getApplication()).getName());

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.units, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.categroy_array, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter5);

        dateEdit = (EditText)findViewById(R.id.addDateEdit);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        dateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(addActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addButton.setOnClickListener(addListener);
    }

    View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showPopup();
            addToDatabase();
        }
    };

    private void showPopup(){
        AddDialogFragment popup = new AddDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = AddDialogFragment.newInstance(1);
        newFragment.show(ft, "dialog");
    }

    private void updateLabel() {

        dateEdit = (EditText)findViewById(R.id.addDateEdit);

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMAN);

        dateEdit.setText(sdf.format(myCalendar.getTime()));
    }

    public void addToDatabase(){
        String name = editName.getText().toString();
        localDatabase database = new localDatabase(this);
        JSONObject food = new JSONObject();
        try {
            food.put("name", name);
            //food.put("entry_date", )
            // add more values
        } catch (JSONException e) {
            e.printStackTrace();
        }
        database.addFood(food);
    }
}
