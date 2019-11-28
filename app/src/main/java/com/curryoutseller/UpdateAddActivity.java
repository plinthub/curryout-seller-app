package com.curryoutseller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.curryoutseller.appsharedpreference.AppGlobalConstants;
import com.curryoutseller.appsharedpreference.AppSharedPreference;
import com.curryoutseller.appsharedpreference.CommonUtilities;
import com.curryoutseller.model.CityModel;
import com.curryoutseller.model.CountryModel;
import com.curryoutseller.model.StateModel;
import com.curryoutseller.model.UserAddressModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateAddActivity extends AppCompatActivity {

    EditText edplotNumber, edStreet, edLandmark, edCity, edState, edPostal, edCountry;
    String plot, street, landmark, city, state, postal, country,add_id="";
    LinearLayout ln_next;
    Spinner spCountry, spState, spCity;

    ArrayList<CountryModel> countryList;
    ArrayAdapter<CountryModel> countryAdapter;

    ArrayList<StateModel> stateList;
    ArrayAdapter<StateModel> stateAdapter;

    ArrayList<CityModel> cityList;
    ArrayAdapter<CityModel> cityAdapter;

    String countryid="",stateid="",cityid="",stateName="";
    UserAddressModel uam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_update);

        init();

        uam = (UserAddressModel) getIntent().getSerializableExtra("AddressID");
        Log.e("UpdateActivity",uam.getUadd_id()+" : "+uam.getStreet());
        add_id = uam.getUadd_id();
        setData();
        listener();
        callgetAllCountries();
    }

    private void init() {

        edplotNumber = findViewById(R.id.edplotNumberUp);
        edStreet = findViewById(R.id.edStreetUp);
        edLandmark = findViewById(R.id.edLandmarkUp);
        edPostal = findViewById(R.id.edPostalUp);
        spCountry = findViewById(R.id.spCountryUp);
        spState = findViewById(R.id.spStateUp);
        spCity = findViewById(R.id.spCityUp);
        ln_next = (LinearLayout) findViewById(R.id.ln_update_address);
        countryList = new ArrayList<>();
        stateList = new ArrayList<>();
    }

    public void setData(){
        edplotNumber.setText(uam.getHouseno());
        edStreet.setText(uam.getStreet());
        edLandmark.setText(uam.getLandmark());
        edPostal.setText(uam.getPostalCode());

        //spCountry.setSelection(getIndex(spCountry, uam.getCountry()));
        //spState.setSelection(getIndex(spState, uam.getState()));
        //spCity.setSelection(getIndex(spCity, uam.getCity()));
    }
    private void listener() {
        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryModel cm = countryList.get(position);
                Log.e("TestCountry", cm.getName() + " " + cm.getCountriesID());
                countryid = cm.getCountriesID();
                callgetAllState(countryid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(uam.getState()!=null){
                    spState.setSelection(getIndex(spState, uam.getState()));
                }

                    StateModel sm = stateList.get(position);
                    Log.e("TestState", sm.getName() + " " + sm.getStatesID());
                    //spState.setSelection(position);
                    stateid = sm.getStatesID();
                    callgetAllCity(stateid);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CityModel citym = cityList.get(position);
                Log.e("TestCity", citym.getName() + " " + citym.getCitiesID());
                cityid = citym.getCitiesID();
                Log.e("CityId",cityid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ln_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                plot = edplotNumber.getText().toString();
                street = edStreet.getText().toString();
                landmark = edLandmark.getText().toString();
                // city = edCity.getText().toString();
                // state = edState.getText().toString();
                postal = edPostal.getText().toString();
                //country = edCountry.getText().toString();

                if (plot.isEmpty()) {
                    edplotNumber.setError("Please enter plot number");
                } else if (street.isEmpty()) {
                    edStreet.setError("Please enter street name");
                } else if (landmark.isEmpty()) {
                    edLandmark.setError("Please enter landmark");
                }
//                else if(city.isEmpty()){
//                    edCity.setError("Please enter city name");
//                }
//                else if(state.isEmpty()){
//                    edState.setError("Please enter state name");
//                }
                else if (postal.isEmpty()) {
                    edPostal.setError("Please enter postal code");
                }
//                else if(country.isEmpty()){
//                    edCountry.setError("Please enter country name");
                //}
                else {
                    Log.e("New Data Test",add_id+" : "+plot+" : "+street+" : "+landmark+" : "+cityid+" : "+stateid+" : "+postal+" : "+countryid);
                    callAddAddressapi(plot, street, landmark, cityid, stateid, postal, countryid);
                }
            }
        });

    }

    public void callAddAddressapi(final String plot, final String street, final String landmark, final String city, final String state, final String postal, final String country) {
        if (!CommonUtilities.isOnline(UpdateAddActivity.this)) {
            Toast.makeText(UpdateAddActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd = new CustomProgressDialogue(UpdateAddActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "auth/updateAddress";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("addressId",add_id);
                map.put("houseno", plot);
                map.put("street", street);
                map.put("landmark", landmark);
                map.put("city", city);
                map.put("state", state);
                map.put("postalCode", postal);
                map.put("country", country);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateAddActivity.this));
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(UpdateAddActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse(String response) {
        Log.e("CO Address Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message = job.getString("message");
            if (data == true) {
                if (message.equalsIgnoreCase("User Address updated Successfully.")) {
                    Toast.makeText(UpdateAddActivity.this, "User Address Updated Successfully.", Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                //Toast.makeText(UpdateAddActivity.this, "Please Enter Proper Data", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void callgetAllCountries() {
        if (!CommonUtilities.isOnline(UpdateAddActivity.this)) {
            Toast.makeText(UpdateAddActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd = new CustomProgressDialogue(UpdateAddActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "common/getAllCountries";
        String testurl = "";

        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponseCountry(response);
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

                        parseResponseCountry(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateAddActivity.this));
                //header.put("X-Auth-Token", "g848w8wswgco4ock88g8o4sgg0cgk8ko0ows8s4c");
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(UpdateAddActivity.this).addToRequestQueue(sr);
    }

    public void parseResponseCountry(String response) {
        Log.e("CO Country Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            if (data == true) {
                JSONArray jarr = job.getJSONArray("data");
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject ob = jarr.getJSONObject(i);
                    String countriesID = ob.getString("countriesID");
                    String code = ob.getString("code");
                    String name = ob.getString("name");

                    CountryModel cm = new CountryModel(countriesID, code, name);
                    countryList.add(cm);
                }

                countryAdapter = new ArrayAdapter(UpdateAddActivity.this, R.layout.spinner_display, R.id.txtSpin, countryList);
                spCountry.setAdapter(countryAdapter);
            }

//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void callgetAllState(final String country_id) {
        if (!CommonUtilities.isOnline(UpdateAddActivity.this)) {
            Toast.makeText(UpdateAddActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd = new CustomProgressDialogue(UpdateAddActivity.this);
        pd.setCancelable(false);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "common/getCountryStates";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponseState(response);
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

                        parseResponseState(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("countryId", country_id);
                return map;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateAddActivity.this));
                //header.put("X-Auth-Token", "g848w8wswgco4ock88g8o4sgg0cgk8ko0ows8s4c");
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(UpdateAddActivity.this).addToRequestQueue(sr);
    }

    public void parseResponseState(String response) {
        Log.e("CO State Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            if (data == true) {
                JSONArray jarr = job.getJSONArray("data");

                String name="",statesID="",country_id="";
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject ob = jarr.getJSONObject(i);
                    statesID = ob.getString("statesID");
                    name = ob.getString("name");
                    country_id = ob.getString("country_id");


                    StateModel sm = new StateModel(statesID, name, country_id);
                    //stateList = new ArrayList<>();
                    stateList.add(sm);
                }

                stateAdapter = new ArrayAdapter(UpdateAddActivity.this, R.layout.spinner_display, R.id.txtSpin, stateList);
                spState.setAdapter(stateAdapter);
                //stateAdapter.notifyDataSetChanged();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callgetAllCity(final String state_id){
        if (!CommonUtilities.isOnline(UpdateAddActivity.this)) {
            Toast.makeText(UpdateAddActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd= new CustomProgressDialogue(UpdateAddActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "common/getStatesAllCities";
        String testurl = "";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponseCity(response);
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

                        parseResponseCity(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("stateId",state_id);
                return map;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateAddActivity.this));
                //header.put("X-Auth-Token", "g848w8wswgco4ock88g8o4sgg0cgk8ko0ows8s4c");
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        sr.setShouldCache(false);
        VolleySingleton.getInstance(UpdateAddActivity.this).addToRequestQueue(sr);
    }

    public void parseResponseCity(String response) {
        Log.e("CO City Response", "response " + response);
        try{
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            if(data==true){
                JSONArray jarr = job.getJSONArray("data");
                for(int i=0;i<jarr.length();i++){
                    JSONObject ob = jarr.getJSONObject(i);
                    String citiesID = ob.getString("citiesID");
                    String name = ob.getString("name");
                    String state_id = ob.getString("state_id");

                    CityModel citym = new CityModel(citiesID,name,state_id);
                    cityList = new ArrayList<>();
                    cityList.add(citym);
                }

                cityAdapter = new ArrayAdapter(UpdateAddActivity.this,R.layout.spinner_display,R.id.txtSpin,cityList);
                spCity.setAdapter(cityAdapter);
                //cityAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                Log.e("GetIndex---",i+"");
                return i;
            }
        }
        return 0;
    }


}
