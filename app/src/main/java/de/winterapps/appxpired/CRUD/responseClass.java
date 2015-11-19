package de.winterapps.appxpired.CRUD;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by D062400 on 21.10.2015.
 */
public class responseClass extends StringRequest {
Map<String,String> headers;

    public responseClass(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }


    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        this.headers = response.headers;
        return super.parseNetworkResponse(response);
    }
}
