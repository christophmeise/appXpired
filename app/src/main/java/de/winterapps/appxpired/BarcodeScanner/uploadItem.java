package de.winterapps.appxpired.BarcodeScanner;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.winterapps.appxpired.CRUD.addActivity;
import de.winterapps.appxpired.CRUD.showActivity;
import de.winterapps.appxpired.R;
import de.winterapps.appxpired.memberVariables;
import de.winterapps.appxpired.settingsActivity;

/**
 * Created by D062400 on 19.11.2015.
 */
public class uploadItem extends Activity {

    Activity self = this;
    String EAN;
    String Name;
    String Brand;
    String Amount;
    String Unit;

    EditText nameEdit;
    Spinner unitSpinner;
    EditText brandEdit;
    EditText amountEdit;
    Button buttonUpload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_upload);

        nameEdit = (EditText) findViewById(R.id.uploadNameEdit);
        unitSpinner = (Spinner) findViewById(R.id.uploadUnitSpinner);
        brandEdit = (EditText) findViewById(R.id.uploadBrandEdit);
        amountEdit = (EditText) findViewById(R.id.uploadAmountEdit);
        buttonUpload = (Button) findViewById(R.id.uploadUploadButton);

        buttonUpload.setOnClickListener(upload);
        EAN = ((memberVariables) ((Activity) self).getApplication()).getEAN();

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(self,
                R.array.units, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        unitSpinner.setAdapter(adapter2);
    }

    View.OnClickListener upload = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            {
                Name = nameEdit.getText().toString();
                Brand = brandEdit.getText().toString();
                Amount = amountEdit.getText().toString();
                Unit = unitSpinner.getSelectedItem().toString();

                putAPI();
            }
        }
    };

    public void putAPI(){
        // Instantiate the RequestQueue.
        // RequestQueue queue = Volley.newRequestQueue(((Activity) mListener).getApplication());
        com.android.volley.RequestQueue queue;
        queue = Volley.newRequestQueue(self);
        //String url ="https://www.datakick.org/api/items/"+EAN+"?brand_name="+Brand+"&name="+Name+"&size="+Amount+Unit; //4008458803774?brand_name=OberSelters&name=Nassauer Land Medium&size=1l
        String url ="https://www.datakick.org/api/items/"+EAN;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        ((memberVariables) ((Activity) self).getApplication()).setCode(Name+" ("+Brand+")");
                        ((memberVariables) ((Activity) self).getApplication()).setAmount(Amount);
                        ((memberVariables) ((Activity) self).getApplication()).setUnit(Unit);
                        showNotification();
                        String x = ((memberVariables) ((Activity) self).getApplication()).getCode();
                        Intent intent = new Intent(self, addActivity.class);
                        startActivity(intent);
                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent intent = new Intent(self, settingsActivity.class);
                        startActivity(intent);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("brand_name", Brand);
                params.put("name", Name);
                params.put("size", Amount+Unit);

                return params;
            }
        }
                ;


// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void showNotification(){
        String name =  ((memberVariables) ((Activity) self).getApplication()).getCode();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Upload erfolgreich!")
                        .setContentText(name + " erfolgreich hinzugefügt");
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        mBuilder.setLargeIcon(bm);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, showActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(showActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        int mId = 1;
        mNotificationManager.notify(mId, mBuilder.build());
        }
    }
}
