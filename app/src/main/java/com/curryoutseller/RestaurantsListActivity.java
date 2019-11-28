package com.curryoutseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.curryoutseller.adapter.RestaurantListAdapter;
import com.curryoutseller.appsharedpreference.AppGlobalConstants;
import com.curryoutseller.appsharedpreference.AppSharedPreference;
import com.curryoutseller.appsharedpreference.CommonUtilities;
import com.curryoutseller.model.RestaurantDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantsListActivity extends AppCompatActivity {
    TextView txt_regAnotherRestaurant;

    ArrayList<RestaurantDataModel> alist;
    RestaurantDataModel restaurantDataModel;
    RecyclerView recyclerView;
    String imageRes="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants_list_fragment);

        init();
        listener();
        alist = new ArrayList<>();
        callGetAllRestaurants();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RestaurantsListActivity.this));
    }

    private void init() {
        txt_regAnotherRestaurant=(TextView)findViewById(R.id.txt_regAnotherRestaurant);

    }
    private void listener() {

        txt_regAnotherRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppCompatActivity activity = (AppCompatActivity)getContext();
//                Fragment myFragment = new RegisterFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                Intent in = new Intent(RestaurantsListActivity.this,RegisterActivity.class);
                startActivity(in);
            }
        });

    }

    public void callGetAllRestaurants(){
        if (!CommonUtilities.isOnline(RestaurantsListActivity.this)) {
            Toast.makeText(RestaurantsListActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(RestaurantsListActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(RestaurantsListActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();
        //Log.e("Name in Call", fullname);
        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "restraunt/getSellerAllRestraunts";
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                NetworkResponse response = error.networkResponse;

                Log.e("com.curryout", "error response " + response);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e("mls", "VolleyError TimeoutError error or NoConnectionError");
                } else if (error instanceof AuthFailureError) {                    //TODO
                    Log.e("mls", "VolleyError AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.e("mls", "VolleyError ServerError");
                } else if (error instanceof NetworkError) {
                    Log.e("mls", "VolleyError NetworkError");
                } else if (error instanceof ParseError) {
                    Log.e("mls", "VolleyError TParseError");
                }
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        Log.e("com.curryout", "error response " + res);

                        parseResponse(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                //header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(RestaurantsListActivity.this));
                header.put("X-Auth-Token", "cg0gg4w8kkkgcco484ssskkkg80oko8ko8wwk084");
                return header;
            }
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(RestaurantsListActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse(String response) {
        Log.e("CurryOut Parse Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");
            if(data==true){
                JSONArray jarr = job.getJSONArray("data");

                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jobj = jarr.getJSONObject(i);
                    String restaurantID = jobj.getString("restrauntID");
                    String user_id = jobj.getString("user_id");
                    String name = jobj.getString("name");
                    String city = jobj.getString("city");
                    String owner_name = jobj.getString("owner_name");
                    String phone = jobj.getString("phone");
                    String email = jobj.getString("email");
                    String address = jobj.getString("address");
                    imageRes = jobj.getString("image");
                    String food_type = jobj.getString("food_type");
                    String status = jobj.getString("status");

                    //JSONArray jsonArray = jobj.optJSONArray("cuisine_name");

                    //Logic to be applied for cuisine_name
                    JSONArray jsonArray = jobj.optJSONArray("cuisine_name");
                    String url="";
                    for(int j=0; j<jsonArray.length(); j++)
                    {
                        url += (String) jsonArray.getString(j)+" | ";
                        Log.d("CN", "url => " + url);
                    }
                    restaurantDataModel= new RestaurantDataModel(restaurantID,name, address,imageRes);
                    Log.e("RD", restaurantID);
                    Log.e("RD1", name);
                    Log.e("RD2", food_type);
                    Log.e("Image",imageRes);
                    Log.e("RD2", jsonArray.getString(1));
                    alist.add(restaurantDataModel);
                }}


            RestaurantListAdapter recyAdapter = new RestaurantListAdapter(alist,RestaurantsListActivity.this);
            recyclerView.setAdapter(recyAdapter);
            //recyAdapter.notifyDataSetChanged();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
