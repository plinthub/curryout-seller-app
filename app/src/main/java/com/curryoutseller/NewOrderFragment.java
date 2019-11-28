package com.curryoutseller;

import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.curryoutseller.adapter.OrderDisplayAdapter;
import com.curryoutseller.adapter.OrderItemAdapter;
import com.curryoutseller.appsharedpreference.AppGlobalConstants;
import com.curryoutseller.appsharedpreference.AppSharedPreference;
import com.curryoutseller.appsharedpreference.CommonUtilities;
import com.curryoutseller.model.OrderDisplayModel;
import com.curryoutseller.model.OrderItemModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewOrderFragment extends Fragment {

    TextView txt_OrderId,txtDispTime;
    LinearLayout ln_accept,ln_cancel;
    ImageView back_icon;
    String OrderId="",Duration="";

    RecyclerView recyclerViewNewOrder;
    OrderItemAdapter adapter;
    ArrayList<OrderItemModel> listOrder;
    OrderItemModel itemModel;

    public NewOrderFragment(){}
    public NewOrderFragment(String oid,String duration){
        OrderId = oid;
        Duration = duration;
        Log.e("OID",oid);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.new_orders_layout,container,false);

        init(view);
        listener(view);
        setData();
        callgetOrderDetailsById(OrderId);
        return view;


    }

    public void setData(){
        txt_OrderId.setText(OrderId);
        txtDispTime.setText(Duration);
    }
    private void init(View view) {
        txt_OrderId = view.findViewById(R.id.txt_OrderId);
        txtDispTime = view.findViewById(R.id.txtDispTime);
        ln_accept = view.findViewById(R.id.ln_accept);
        ln_cancel = view.findViewById(R.id.ln_cancel);
        back_icon = view.findViewById(R.id.back_icon);
        listOrder = new ArrayList<>();
        recyclerViewNewOrder = view.findViewById(R.id.recyViewNewOrders);
        recyclerViewNewOrder.setHasFixedSize(true);
        recyclerViewNewOrder.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void listener(View view) {

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)getContext();
                activity.getSupportFragmentManager().popBackStack();
            }
        });

        ln_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callsellerAcceptCancelOrder(OrderId,"1");
            }
        });

        ln_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity)getContext();
                activity.getSupportFragmentManager().popBackStack();
                callsellerAcceptCancelOrder(OrderId,"2");
            }
        });
    }


    public void callgetOrderDetailsById(final String orderId){
        if (!CommonUtilities.isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        final CustomProgressDialogue pd= new CustomProgressDialogue(getActivity());
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "order/getOrderDetailsById";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                parseResponseOrderById(response);
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

                        parseResponseOrderById(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap();
                map.put("orderId",orderId);
                Log.e("Test Dta",OrderId);
                return map;
            }

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
    public void parseResponseOrderById(String response){
        Log.e("Seller List Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");
            if(data==true) {
                JSONObject jobj = job.getJSONObject("data");
                //for (int i = 0; i < jarr.length(); i++) {
                    //JSONObject jobj = jarr.getJSONObject(i);
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
                        itemModel = new OrderItemModel(product_name,ordered_item_quantity,ordered_item_price);
                        listOrder.add(itemModel);
                    }
                    adapter = new OrderItemAdapter(getActivity(),listOrder);
                    recyclerViewNewOrder.setAdapter(adapter);
                }

            //}

        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public void callsellerAcceptCancelOrder(final String OID,final String status){
        if (!CommonUtilities.isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        final CustomProgressDialogue pd= new CustomProgressDialogue(getActivity());
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "order/sellerAcceptCancelOrder";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                parseResponseAccept(response);
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

                        parseResponseAccept(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap();
                map.put("status",status);
                map.put("orderId",OID);
                Log.e("Test Dta",OID+": "+status);
                return map;
            }

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

    public void parseResponseAccept(String response) {
        Log.e("COSeller Accept Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");
            String message="";
            if(data==true){
               message = job.getString("message");
               if(message.contains("Order Status Updated Successfully")){
                   Toast.makeText(getActivity(), "Order Status Updated Successfully.", Toast.LENGTH_SHORT).show();
                   AppCompatActivity activity = (AppCompatActivity)getContext();
                   activity.getSupportFragmentManager().popBackStack();
               }else{

               }


            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }





}
