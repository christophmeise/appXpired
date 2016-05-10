package de.winterapps.appxpired.BarcodeScanner;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.DialogFragment;
        import android.support.v4.app.FragmentTransaction;

        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONException;
        import org.json.JSONObject;

        import de.winterapps.appxpired.CRUD.AddFiles.addActivity;
        import de.winterapps.appxpired.memberVariables;

public class MessageDialogFragment extends DialogFragment {
    public interface MessageDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    private String mTitle;
    private String mMessage;
    private MessageDialogListener mListener;
    public MessageDialogFragment self = this;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static MessageDialogFragment newInstance(String title, String message, MessageDialogListener listener) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMessage)
                .setTitle(mTitle);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mListener != null) {
                    mListener.onDialogPositiveClick(MessageDialogFragment.this);
                    requestAPI(mMessage.substring(11, 24));
                }
            }
        });

        return builder.create();
    }

    public void startIntent(){
        Intent intent = new Intent(getActivity(), addActivity.class);
        startActivity(intent);
    }

    public void requestAPI(String ean){
        // Instantiate the RequestQueue.
        com.android.volley.RequestQueue queue;
        queue = Volley.newRequestQueue(this.getContext());
        String url ="https://www.datakick.org/api/items/"+ean;
        ((memberVariables) ((Activity) mListener).getApplication()).setEAN(ean);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    String id;

                    @Override
                    public void onResponse(String response) {
                        final boolean hasResponse = true;
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String name = null;
                        String brand = null;
                        String size = null;
                        String message = null;
                            if (jsonObj != null) {
                                   try {
                                        if (jsonObj.has("name")){
                                            name = jsonObj.getString("name");
                                            ((memberVariables) ((Activity) mListener).getApplication()).setName(name);
                                        }
                                        if (jsonObj.has("brand_name")) {
                                            brand = jsonObj.getString("brand_name");
                                            ((memberVariables) ((Activity) mListener).getApplication()).setBrand(brand);
                                        }
                                        if (jsonObj.has("size")){
                                            size = jsonObj.getString("size");
                                            ((memberVariables) ((Activity) mListener).getApplication()).setSize(size);
                                        }
                                    } catch(JSONException e){

                                    }

                                startIntent();
                            }

                    }
                },
        new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                showPopup();  // confirmation to add food to (api-)database
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void showPopup() {
        BarAddDialogFragment popup = new BarAddDialogFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = BarAddDialogFragment.newInstance(1);
        newFragment.show(ft, "dialog");
    }
}