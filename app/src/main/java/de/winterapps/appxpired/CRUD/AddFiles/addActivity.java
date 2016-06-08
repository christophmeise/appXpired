package de.winterapps.appxpired.CRUD.AddFiles;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.winterapps.appxpired.ButtonOnClickListener;
import de.winterapps.appxpired.CRUD.responseClass;
import de.winterapps.appxpired.R;
import de.winterapps.appxpired.localDatabase;
import de.winterapps.appxpired.memberVariables;
import de.winterapps.appxpired.menuActivity;

/**
 * Created by Christoph on 12.10.2015.
 */
public class addActivity extends AppCompatActivity {

    final static Calendar myCalendar = Calendar.getInstance();
    static EditText dateEdit;
    Button addButton;
    Button templateButton;
    EditText editName;
    EditText editAmount;
    EditText additionalInformation;
    responseClass stringRequest;
    Activity self = this;
    memberVariables members = memberVariables.sharedInstance;
    String backendid;
    Spinner oPositionSpinner;
    Spinner oUnitsSpinner;
    Spinner oCategorySpinner;
    ArrayAdapter<CharSequence> unitSpinnerAdapter;
    ArrayAdapter<String> categorySpinnerAdapter;
    ArrayAdapter<String> positionSpinnerAdapter;
    localDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        this.findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE); //turn off loading indicator
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        initializeLayout();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(addActivity.this, menuActivity.class);
        startActivity(intent);
    }

    private void initializeLayout() {
        loadLayoutElements();
        populateSpinners();
        addListeners();
        loadFromMembers();
    }

    private void loadFromMembers() {
        editName.setText(((memberVariables) ((Activity) this).getApplication()).getName());
        editAmount.setText(((memberVariables) ((Activity) this).getApplication()).getSize());
        editName.setText(((memberVariables) ((Activity) this).getApplication()).getName());
        oCategorySpinner.setSelection(categorySpinnerAdapter.getPosition(((memberVariables) ((Activity) this).getApplication()).getCategory()));
        //oPositionSpinner.setSelection(positionSpinnerAdapter.getPosition(((memberVariables) ((Activity) this).getApplication()).getPosition()));
        //TODO: alex in backend einfügen
        oUnitsSpinner.setSelection(unitSpinnerAdapter.getPosition(((memberVariables) ((Activity) this).getApplication()).getUnit()));
        additionalInformation.setText(((memberVariables) ((Activity) this).getApplication()).getAdditional());
        int duration;
        if (((memberVariables) ((Activity) this).getApplication()).getDuration() != null) {
            duration = Integer.parseInt(((memberVariables) ((Activity) this).getApplication()).getDuration());
            duration = Integer.parseInt(((memberVariables) ((Activity) this).getApplication()).getDuration());
            long millis = System.currentTimeMillis() % 1000;
            long durationInMillis = duration * 24 * 60 * 60 * 1000;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date expireDate = new Date();
            expireDate.setTime(durationInMillis);
            String expireDateFormatted = sdf.format(expireDate);
            dateEdit.setText(expireDateFormatted);
        }
    }

    private void addListeners() {
        final DatePickerDialog.OnDateSetListener date = new OnDateSetListener(self);

        dateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(addActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        View.OnClickListener addListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();

                final String user = members.getUsername();
                //String pass = members.getPassword();
                final String token = members.getToken();
                final String Wherevalues = "";
                backendRequestAdd(user, "", token, Wherevalues);
            }
        };

        addButton.setOnClickListener(addListener);
        templateButton.setOnClickListener(new ButtonOnClickListener(addActivity.this, useTemplateActivity.class));
    }

    private void populateSpinners() {
        database = new localDatabase(this);
        populateCategorySpinner();
        populateUnitsSpinner();
        populatePositionsSpinner();
    }

    private void populatePositionsSpinner() {
        JSONArray positions = database.getPositions();
        ArrayList positionsSpinnerFormat = new ArrayList();
        JSONObject positionElement;
        for (int i = 0; i < positions.length(); i++){
            try {
                positionElement = (JSONObject) positions.get(i);
                positionsSpinnerFormat.add(positionElement.get("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        positionSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, positionsSpinnerFormat);
        oPositionSpinner.setAdapter(positionSpinnerAdapter);
    }

    private void populateUnitsSpinner() {
        unitSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_spinner_item);
        unitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        oUnitsSpinner.setAdapter(unitSpinnerAdapter);
    }

    private void populateCategorySpinner() {
        JSONArray categories = database.getCategories();
        ArrayList categoriesSpinnerFormat = new ArrayList();
        JSONObject categoryElement;
        for (int i = 0; i < categories.length(); i++){
            try {
                categoryElement = (JSONObject) categories.get(i);
                categoriesSpinnerFormat.add(categoryElement.get("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        categorySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoriesSpinnerFormat);
        oCategorySpinner.setAdapter(categorySpinnerAdapter);

    }

    private void loadLayoutElements() {
        addButton = (Button) findViewById(R.id.addAddButton);
        templateButton = (Button) findViewById(R.id.addTemplateButton);
        editName = (EditText) findViewById(R.id.editName);
        editAmount = (EditText) findViewById(R.id.addAmountEdit);
        oPositionSpinner = (Spinner) findViewById(R.id.addPositionSpinner);
        oUnitsSpinner = (Spinner) findViewById(R.id.addAmountSpinner);
        oCategorySpinner = (Spinner) findViewById(R.id.addCategorySpinner);
        dateEdit = (EditText)findViewById(R.id.addDateEdit);
        additionalInformation = (EditText) findViewById(R.id.addDescEdit);
    }

    private void showPopup(){
        AddDialogFragment popup = new AddDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = AddDialogFragment.newInstance(1);
        newFragment.show(ft, "dialog");
    }

    public static void updateLabel(Context context) {
        dateEdit = (EditText)((Activity) context).findViewById(R.id.addDateEdit);

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMAN);

        dateEdit.setText(sdf.format(myCalendar.getTime()));
    }

    public void addToDatabase(String backendid){
        String name = editName.getText().toString();
        String amount = editAmount.getText().toString();
        localDatabase database = new localDatabase(this);
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        long expireDateMillis = 0;
        try {
            Date expireDate = format.parse(String.valueOf(dateEdit.getText()));
            expireDateMillis = expireDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject food = new JSONObject();
        try {
            food.put("name", name);
            food.put("expire_date", expireDateMillis);
            food.put("backendId", Integer.parseInt(backendid));
            food.put("category_id", database.getCategory(oCategorySpinner.getSelectedItem().toString()));
            food.put("position_id", database.getPosition(oPositionSpinner.getSelectedItem().toString()));
            food.put("amount", amount);
            food.put("unit", oUnitsSpinner.getSelectedItem().toString());
            //food.put("entry_date", )
            // add more values
        } catch (JSONException e) {
            e.printStackTrace();
        }
        database.addFood(food);
        showNotification("Added "+name+" to database", "");
        String dateString = dateEdit.getText().toString();
        dateString = dateString.replaceAll("/",".");
        Date dt = null;
        try {
            dt = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, Locale.GERMANY).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long insertedTimeInMillis = dt.getTime();
        if (insertedTimeInMillis < new Date().getTime()){
            showNotification("Expired!", name+" is expired on "+dt.toString());
        }
    }

    private void showNotification(String title, String text) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_newthicker)
                        .setContentTitle(title)
                        .setContentText(text);
        // Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private boolean backendRequestAdd(final String user, final String pass, final String token, final String Wherevalues){

        // Instantiate the RequestQueue.
        com.android.volley.RequestQueue queue;
        queue = Volley.newRequestQueue(self);
        String url ="http://www.appxpired.winterapps.de/api/api.php";
        final Boolean[] RequestResponse = {false};

        // Request a string response from the provided URL.
        stringRequest = new responseClass(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Map<String,String> headers = null;

                        String[] credentials = new String[1];
                        try {
                            Log.d("loginAc", addActivity.this.stringRequest.toString());
                            headers = addActivity.this.stringRequest.headers;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        credentials[0] = headers.get("LastId");
                        backendid = credentials[0];
                        addToDatabase(backendid);
                        Log.d("Hier bekomme ich me", backendid);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }

                }) {

            @Override
            public HashMap<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String>  params = new HashMap<String, String>();

                params.put("Appxpired-Username", user);
                if(pass != null) {
                    params.put("Appxpired-Token", token);
                    // TODO
                   // params.put("Appxpired-Password", pass);
                }
                else{
                    params.put("Appxpired-Token", token);
                }
                params.put("Appxpired-Table", "groceries");
                String userid = members.userid;
                String name = editName.getText().toString();
                params.put("Appxpired-Setvalues", "name,"+name+";household.id,7;createuser.id,"+userid);

                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        return RequestResponse[0];
    }
}
