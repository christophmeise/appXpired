package de.winterapps.appxpired.BarcodeScanner;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.DialogFragment;
        import android.support.v4.app.FragmentTransaction;
        import android.util.Log;

        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONException;
        import org.json.JSONObject;

        import de.winterapps.appxpired.CRUD.addActivity;
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
                    // ((memberVariables) ((Activity) mListener).getApplication()).setCode(mMessage.toString());
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
       // RequestQueue queue = Volley.newRequestQueue(((Activity) mListener).getApplication());
        com.android.volley.RequestQueue queue;
        queue = Volley.newRequestQueue(this.getContext());
        String url ="https://www.datakick.org/api/items/"+ean;
        //boolean hasResponse = false;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    String id;

                    @Override
                    public void onResponse(String response) {
                        System.out.println("hallooooooooooooo"+response);
                        final boolean hasResponse = true;
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String id = null;
                        String message = null;
                        try {
                            if (jsonObj != null) {
                                id = jsonObj.getString("name");
                            }
                            if (id != ""){
                                /*memberVariables mv = new memberVariables();
                                mv.setCode(id);*/
                                ((memberVariables) ((Activity) mListener).getApplication()).setCode(id);
                                String x = ((memberVariables) ((Activity) mListener).getApplication()).getCode();
                                System.out.println(x);
                                startIntent();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                },
        new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {
                System.out.println("teeeest");
                Log.v("teeeeeeeeeeeeeeeeeeeeeeeeest","test");
                System.out.println(error.toString());
                showPopup();
/*                if(hasResponse == false ){

                }*/

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void showPopup() {
        AddDialogFragment popup = new AddDialogFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = AddDialogFragment.newInstance(1);
        newFragment.show(ft, "dialog");
    }
}