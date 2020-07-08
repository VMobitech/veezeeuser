package com.versatilemobitech.VeeZee.WebUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.versatilemobitech.VeeZee.BaseApplication;
import com.versatilemobitech.VeeZee.interfaces.IParseListener;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class ServerResponse {
    private int requestCode;
    private Context mContext;
    private RequestQueue requestQueue;

    public void serviceRequestStringBuilder(String url) {
        String tag_json_obj = "json_obj_req";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        BaseApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void serviceRequestStringBuilder(Context mContext, String methodType, final String url, StringBuilder params,
                                            final IParseListener iParseListener, final int requestCode) {
        this.requestCode = requestCode;
        int method = 0;
        if (methodType.equalsIgnoreCase("POST")) {
            method = Request.Method.POST;
        } else if (methodType.equalsIgnoreCase("GET")) {
            method = Request.Method.GET;
        }

        Log.d("the url request is", url + params);

        StringRequest stringRequest = new StringRequest(method, url + params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response is", response);
                        iParseListener.SuccessResponse(response, requestCode);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            String body = "";
                            String statusCode = String.valueOf(error.networkResponse.statusCode);
                            if (error.networkResponse.data != null) {
                                try {
                                    body = new String(error.networkResponse.data, "UTF-8");
                                    Log.e("the error is", body.toString());
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                            iParseListener.ErrorResponse(body, requestCode);
                        }
                    }
                }) {
        };
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void serviceRequestJSonObject(final Context mContext, String methodType, final String url, final JSONObject params,
                                         final Map<String, String> headers, final IParseListener iParseListener, final int requestCode) {
        this.requestCode = requestCode;
        int method = 0;
        if (methodType.equalsIgnoreCase("POST")) {
            method = Request.Method.POST;
        } else if (methodType.equalsIgnoreCase("GET")) {
            method = Request.Method.GET;
        }

        Log.e("hbdvcdgd","params "+params.toString());
        Log.e("hbdvcdgd","header "+headers.toString());

        //Utility.showLog("Params is", ""+params);
        Log.d(TAG, "serviceRequestJSonObject: Params...." + params);
        Log.d(TAG, "serviceRequestJSonObject: Params...." + headers);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response is", response.toString());
                        iParseListener.SuccessResponse("" + response, requestCode);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    String body = "";
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    if (error.networkResponse.data != null) {
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            Log.e("the error is", body.toString());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    iParseListener.ErrorResponse(body, requestCode);

                    /*Handling Timeout Error*/
                    if (error.getClass().equals(TimeoutError.class)) {
                        Toast.makeText(mContext, "Server was disconnected, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void serviceRequest(Context mContext, final String url, final Map<String, String> params,
                               final IParseListener iParseListener, final int requestCode) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response is", response);
                        iParseListener.SuccessResponse(response, requestCode);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error1) {
                        iParseListener.ErrorResponse(error1.toString(), requestCode);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Log.e("service is", url + params + "");
                return params;
            }
        };
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void multipartRequest(Context context, String url, final HashMap<String, String> params,
                                 final Map<String, VolleyMultipartRequest.DataPart> files,
                                 final IParseListener iParseListener, final int requestCode) {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("response is", response.statusCode + "");
                        iParseListener.SuccessResponse(response.statusCode + "", requestCode);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iParseListener.ErrorResponse(error.toString() + "", requestCode);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                return files;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(multipartRequest);
    }
}
