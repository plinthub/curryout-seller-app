package com.curryoutseller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.curryoutseller.appsharedpreference.AppGlobalConstants;
import com.curryoutseller.appsharedpreference.AppSharedPreference;
import com.curryoutseller.appsharedpreference.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class RestaurantsListFragment extends Fragment {

    TextView txt_regAnotherRestaurant;

    ArrayList<RestaurantDataModel> alist;
    RestaurantDataModel restaurantDataModel;
    //RestaurantListAdapter recyAdapter;
    RecyclerView recyclerView;
    String imageRes="";
    String resId="",resStatus="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.restaurants_list_fragment, container, false);

        init(view);
        listener(view);
        alist = new ArrayList<>();

//        RestaurantDataModel restaurantDataModel[] = new RestaurantDataModel[] {
//
//                new RestaurantDataModel("Nachos Parlour", "Random Address, Unknown Street, Zipcode", R.drawable.wabisabi_rest_img),
//                new RestaurantDataModel("Nachos Parlour", "Random Address, Unknown Street, Zipcode", R.drawable.wabisabi_rest_img),
//                new RestaurantDataModel("Nachos Parlour", "Random Address, Unknown Street, Zipcode", R.drawable.wabisabi_rest_img),
//        };

        callGetAllRestaurants();
        //callGetAllRestaurants();


//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        RestaurantListAdapter recyAdapter = new RestaurantListAdapter(restaurantDataModel);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(recyAdapter);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }


    private void init(View view) {
        txt_regAnotherRestaurant=(TextView)view.findViewById(R.id.txt_regAnotherRestaurant);

    }

    private void listener(View view) {

        txt_regAnotherRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent in = new Intent(getActivity(),RegisterActivity.class);
                    startActivity(in);
//                AppCompatActivity activity = (AppCompatActivity)getContext();
//                Fragment myFragment = new RegisterFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });

    }


    public class RestaurantDataModel {

        private String restrauntID;
        private String txtRestaurantName;
        private String txtRestaurantAddress;
        private String imgRestaurantBG;
        private String status;

        public RestaurantDataModel(String restrauntID, String txtRestaurantName, String txtRestaurantAddress, String imgRestaurantBG) {
            this.restrauntID = restrauntID;
            this.txtRestaurantName = txtRestaurantName;
            this.txtRestaurantAddress = txtRestaurantAddress;
            this.imgRestaurantBG = imgRestaurantBG;
        }

        public RestaurantDataModel(String restrauntID, String txtRestaurantName, String txtRestaurantAddress, String imgRestaurantBG,String status) {
            this.restrauntID = restrauntID;
            this.txtRestaurantName = txtRestaurantName;
            this.txtRestaurantAddress = txtRestaurantAddress;
            this.imgRestaurantBG = imgRestaurantBG;
            this.status = status;
        }

        public String getRestrauntID() {
            return restrauntID;
        }

        public void setRestrauntID(String restrauntID) {
            this.restrauntID = restrauntID;
        }

        public String getTxtRestaurantName() {
            return txtRestaurantName;
        }

        public void setTxtRestaurantName(String txtRestaurantName) {
            this.txtRestaurantName = txtRestaurantName;
        }

        public String getTxtRestaurantAddress() {
            return txtRestaurantAddress;
        }

        public void setTxtRestaurantAddress(String txtRestaurantAddress) {
            this.txtRestaurantAddress = txtRestaurantAddress;
        }

        public String getImgRestaurantBG() {
            return imgRestaurantBG;
        }

        public void setImgRestaurantBG(String imgRestaurantBG) {
            this.imgRestaurantBG = imgRestaurantBG;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {
        private ArrayList<RestaurantDataModel> listdata;
        RestaurantDataModel restauListData;

        public RestaurantListAdapter(ArrayList<RestaurantDataModel> listdata) {
            this.listdata = listdata;
        }

        @NonNull
        @Override
        public RestaurantListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.restaurant_list_item_layout, parent, false);
            ViewHolder viewHolder= new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RestaurantListAdapter.ViewHolder holder, int position) {
            restauListData = listdata.get(position);
            holder.txtRestName.setText(restauListData.getTxtRestaurantName());
            holder.txtRestAddress.setText(restauListData.getTxtRestaurantAddress());


            imageRes = restauListData.getImgRestaurantBG();

            Glide.with(getActivity()).load(Uri.parse(restauListData.getImgRestaurantBG()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);

            final String rid = restauListData.getRestrauntID();
            Log.e("RID in RLF",rid);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new MyProductFragment(rid);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                }
            });

            final String stat = restauListData.getStatus();
            if(stat.equals("1")){
                holder.toggleButtonResStatus.setChecked(true);
            }else {
                holder.toggleButtonResStatus.setChecked(false);
            }
            holder.toggleButtonResStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((!holder.toggleButtonResStatus.isChecked()) && (stat.equals("1") || stat.equals("0"))) {
                        callSellerRestaurantsStatus(rid,"0");
                    }
                    else{
                        callSellerRestaurantsStatus(rid,"1");
                    }
                    //Toast.makeText(getActivity(), "Tb"+rid+" : "+restauListData.getStatus(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView txtRestName;
            public TextView txtRestAddress;
            public ToggleButton toggleButtonResStatus;
            public ViewHolder(View itemView) {
                super(itemView);

                this.imageView=(ImageView)itemView.findViewById(R.id.img_Restaurant);
                this.txtRestName=(TextView)itemView.findViewById(R.id.txt_RestaurantsName);
                this.txtRestAddress=(TextView)itemView.findViewById(R.id.txt_Restaurantaddress);
                this.toggleButtonResStatus = itemView.findViewById(R.id.toggleButtonResStatus);
            }
        }
    }


    public void callGetAllRestaurants(){
        if (!CommonUtilities.isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(getActivity());
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(getActivity());
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
                //header.put("X-Auth-Token", "g848w8wswgco4ock88g8o4sgg0cgk8ko0ows8s4c");
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
                    String provide_delivery = jobj.getString("provide_delivery");
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
                    restaurantDataModel= new RestaurantDataModel(restaurantID,name, address,imageRes,status);
                    Log.e("RD", restaurantID);
                    Log.e("RD1", name);
                    Log.e("RD2", food_type);
                    Log.e("Image",imageRes);
                    //Log.e("RD2", jsonArray.getString(1));
                    alist.add(restaurantDataModel);
                }
            }


            RestaurantListAdapter recyAdapter = new RestaurantListAdapter(alist);
            recyclerView.setAdapter(recyAdapter);
            recyAdapter.notifyDataSetChanged();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //callGetAllRestaurants();
    }




    public void callSellerRestaurantsStatus(final String resid,final String status){
        if (!CommonUtilities.isOnline(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }


        final CustomProgressDialogue pd= new CustomProgressDialogue(getActivity());
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "restraunt/changeRestrauntStatus";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                parseResponseResStatus(response);
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

                        parseResponseResStatus(response.toString());

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
                Map<String,String> map = new HashMap<>();
                map.put("restrauntId",resid);
                Log.e("Map",resid);
                map.put("status",status);
                Log.e("Map",status);
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

    public void parseResponseResStatus(String response) {
        Log.e("CurryOut Parse Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");
            if(data==true) {
                String msg = job.getString("message");
                if(msg.equals("Restraunt status changed Successfully.")){
                    Toast.makeText(getActivity(), "Restraunt Status Changed Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
            else if(data == false){
                String msg = job.getString("message");
                if(msg.equals("Something went wrong! Please try again later.")){
                    Toast.makeText(getActivity(), "Something went wrong! Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }





}
