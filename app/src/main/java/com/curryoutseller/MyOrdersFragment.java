package com.curryoutseller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.curryoutseller.adapter.OrderDisplayAdapter;
import com.curryoutseller.appsharedpreference.AppGlobalConstants;
import com.curryoutseller.appsharedpreference.AppSharedPreference;
import com.curryoutseller.appsharedpreference.CommonUtilities;
import com.curryoutseller.model.OrderDisplayModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyOrdersFragment extends Fragment {

    //LinearLayout ln;
    RecyclerView recyclerViewOrder;
    OrderDisplayAdapter adapter;
    ArrayList<OrderDisplayModel> listOrder;
    OrderDisplayModel orderDisplayModel;
    Date orderedDate;
    long check=0;
    String time="",show="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_orders_fragment, container, false);

        //MainActivity.navigation.setSelected(true);
        init(view);
        listener(view);
        callgetSellerOrdersList();
        return view;
    }

    private void init(View view) {

        //ln = (LinearLayout)view.findViewById(R.id.ln);
        listOrder = new ArrayList<>();
        recyclerViewOrder = view.findViewById(R.id.recyViewOrders);
        recyclerViewOrder.setHasFixedSize(true);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void listener(View view) {

//        ln.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                Fragment myFragment = new NewOrderFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
//            }
//        });
    }



    public void callgetSellerOrdersList(){
        if (!CommonUtilities.isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        final CustomProgressDialogue pd= new CustomProgressDialogue(getActivity());
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "order/getSellerOrdersList";
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                parseResponseOrderList(response);
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

                        parseResponseOrderList(response.toString());

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
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(getActivity()));
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);
    }

    public void parseResponseOrderList(String response) {
        Log.e("COSeller Order Response", "response " + response);
        try{
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");
            if(data==true) {
                JSONArray jarr = job.getJSONArray("data");
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jobj = jarr.getJSONObject(i);
                    String orderID = jobj.getString("orderID");
                    String customer_id = jobj.getString("customer_id");
                    String instruction_to_delivery_boy = jobj.getString("instruction_to_delivery_boy");
                    String instruction_for_order = jobj.getString("instruction_for_order");
                    String sub_total = jobj.getString("sub_total");
                    String delivery_fee = jobj.getString("delivery_fee");
                    String discount = jobj.getString("discount");
                    String total = jobj.getString("total");
                    String order_datetime = jobj.getString("order_datetime");
                    String order_accept_cancel = jobj.getString("order_accept_cancel");
                    String houseno = jobj.getString("houseno");
                    String street = jobj.getString("street");
                    String landmark = jobj.getString("landmark");
                    String city = jobj.getString("city");
                    String state = jobj.getString("state");
                    String country = jobj.getString("country");
                    String postalCode = jobj.getString("postalCode");
                    String payment_method = jobj.getString("payment_method");
                    String order_status = jobj.getString("order_status");
                    String mobile = jobj.getString("mobile");
                    String customer_name = jobj.getString("customer_name");
                    JSONArray oDetail = jobj.getJSONArray("order_item_details");
                    orderedDate = stringToDate(order_datetime);
                    for(int y=0;y<oDetail.length();y++){
                        JSONObject od = oDetail.getJSONObject(y);
                        String ordered_product_id = od.getString("ordered_product_id");
                        String ordered_item_quantity = od.getString("ordered_item_quantity");
                        String ordered_item_price = od.getString("ordered_item_price");
                        String product_name = od.getString("product_name");
                        String food_type = od.getString("food_type");
                        String category = od.getString("category");
                        String restraunt_name = od.getString("restraunt_name");
                        String restraunt_image = od.getString("restraunt_image");
                        String sub_category = od.getString("sub_category");

                    }
                    Date currentTime = Calendar.getInstance().getTime();
                    printDifference(orderedDate,currentTime);
                    String time = String.valueOf(check);
                    orderDisplayModel = new OrderDisplayModel(orderID,order_status,show);
                    listOrder.add(orderDisplayModel);
                }
                adapter = new OrderDisplayAdapter(getActivity(),listOrder);
                recyclerViewOrder.setAdapter(adapter);

            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Date stringToDate(String d){
        String dtStart = d;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dtStart);
            Log.e("Date",date+"");
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        Log.e("startDate : " , startDate+"");
        Log.e("endDate : ", endDate+"");
        Log.e("different : " , different+"");

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if (elapsedDays!=0){
            check = elapsedDays;
            show = check+" Days Ago";
        }
        else if(elapsedHours!=0){
            check = elapsedHours;
            show = check+" Hours Ago";
        }
        else
        {
            check = elapsedMinutes;
            show = check+" Minutes Ago";
        }
        Log.e(
                "%d days,%d hours,%d minutes,%d seconds%n",
                elapsedDays+" : "+elapsedHours+" : "+elapsedMinutes+" : "+ elapsedSeconds);
    }
}
