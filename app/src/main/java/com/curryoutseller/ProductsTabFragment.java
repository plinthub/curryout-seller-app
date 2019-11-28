package com.curryoutseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
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
import com.androidquery.AQuery;
import com.curryoutseller.appsharedpreference.AppGlobalConstants;
import com.curryoutseller.appsharedpreference.AppSharedPreference;
import com.curryoutseller.appsharedpreference.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductsTabFragment extends Fragment {

//    private static NonScrollExpandableListView expandableListView;
//    private static ExpandableListAdapter adapter;


    LinearLayout ln_addProduct;
    NonScrollExpandableListView expandableListView;
    MyExpandableListAdapter adapter;
    ArrayList<ProductsSubCategory> subList;
    ProductsSubCategory productsSubCategory;
    ArrayList<ProductsCategory> productsCategories = null;
    ArrayList<ProductsSubCategory> asiaSubCategory;
    String imageRes="";
    AQuery aQuery;
    String rid;

    public ProductsTabFragment() {
    }

    public ProductsTabFragment(String rid) {
        try {
            this.rid = rid;
            Log.e("ProductTabF", rid);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.my_product_tab_fragment, container, false);


        expandableListView = (NonScrollExpandableListView) view.findViewById(R.id.simple_expandable_listview);
        productsCategories = new ArrayList<>();
        subList = new ArrayList<>();
        productsCategories = populateContinentData(productsCategories);
        adapter = new MyExpandableListAdapter(getActivity(), productsCategories);

        expandableListView.setAdapter(adapter);
        expandableListView.setFocusable(false);


//
//        // Setting group indicator null for custom indicator
//        expandableListView.setGroupIndicator(null);
        init(view);
        listener(view);
//        setItems();
        callgetSellerRestrauntProducts(rid);




        return view;
    }




    private ArrayList<ProductsCategory> populateContinentData(ArrayList<ProductsCategory> productsCategories) {

//        ProductsCategory asiaProductsCategory = new ProductsCategory(1, "Fast Food", null);
//        ArrayList<ProductsSubCategory> asiaSubCategory = new ArrayList<>();
//        asiaSubCategory.add(new ProductsSubCategory("Indori Food", "Jalebi","15", 1, R.drawable.fastfood_img));
//        asiaSubCategory.add(new ProductsSubCategory("Indori Food", "Poha","20", 2, R.drawable.fastfood_img));
//        asiaSubCategory.add(new ProductsSubCategory("Indori Food", "Kachori","15", 3, R.drawable.fastfood_img));
//        asiaSubCategory.add(new ProductsSubCategory("Indori Food", "Samosa","10", 4, R.drawable.fastfood_img));
//        asiaProductsCategory.setProductsSubCategory(asiaSubCategory);
//        productsCategories.add(asiaProductsCategory);

//        ProductsCategory asiaProductsCategory2 = new ProductsCategory(1, "Chicken", null);
//        ArrayList<ProductsSubCategory> asiaSubCategory2 = new ArrayList<>();
//        asiaSubCategory2.add(new ProductsSubCategory("Indori Food", "Chicken","55", 1, R.drawable.chicken));
//        asiaSubCategory2.add(new ProductsSubCategory("Indori Food", "Egg","20", 2, R.drawable.chicken));
//        asiaProductsCategory2.setProductsSubCategory(asiaSubCategory2);
//        productsCategories.add(asiaProductsCategory2);

        return productsCategories;
    }


    private void init(View view) {
    aQuery = new AQuery(getActivity());
    asiaSubCategory = new ArrayList<>();
    ln_addProduct=(LinearLayout)view.findViewById(R.id.ln_addProduct);

    }

    private void listener(View view) {

        ln_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AddProductActivity.class);
                in.putExtra("ResIdPro",rid);
                startActivity(in);
            }
        });


//
//// This listener will show toast on group click
        expandableListView.setOnGroupClickListener(new NonScrollExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView listview, View view,
                                        int group_pos, long id) {

//                Toast.makeText(getActivity(),
//                        "You clicked : " + adapter.getGroup(group_pos),
//                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
//
//        // This listener will expand one group at one time
//        // You can remove this listener for expanding all groups
        expandableListView
                .setOnGroupExpandListener(new NonScrollExpandableListView.OnGroupExpandListener() {

                    // Default position
                    int previousGroup = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {


                        if (groupPosition != previousGroup)

                            // Collapse the expanded group
                            expandableListView.collapseGroup(previousGroup);
                        previousGroup = groupPosition;
                    }

                });
//
//        // This listener will show toast on child click
        expandableListView.setOnChildClickListener(new NonScrollExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView listview, View view,
                                        int groupPos, int childPos, long id) {
//                Toast.makeText(
//                        SupportActivity.this,
//                        "You clicked : " + adapter.getChild(groupPos, childPos),
//                        Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(SupportActivity.this, FreAskedQuestions.class));
                return false;
            }
        });

    }
//
//    @Override
//    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//        ProductsSubCategory c = adapter.getChild(groupPosition, childPosition);
//        Toast.makeText(getActivity(), "Your clicked " + c.getName() + " of Group " + adapter.getGroup(groupPosition).getName(), Toast.LENGTH_SHORT).show();
//        return false;
//    }
//
//    @Override
//    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//        Toast.makeText(getActivity(), "Your clicked Group " + adapter.getGroup(groupPosition).getName(), Toast.LENGTH_SHORT).show();
//        return false;
//    }
//
//    @Override
//    public void onGroupCollapse(int groupPosition) {
//        Toast.makeText(getActivity(), "Group collapsed is : " + adapter.getGroup(groupPosition).getName(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onGroupExpand(int groupPosition) {
//        Toast.makeText(getActivity(), "Group Expanded is : " + adapter.getGroup(groupPosition).getName(), Toast.LENGTH_SHORT).show();
//    }

    public void callgetSellerRestrauntProducts(final String restaurantID){
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

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "product/getSellerRestrauntProducts";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                parseResponseProduct(response);
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

                        parseResponseProduct(response.toString());

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
                    //map.put("restrauntId", "1");
                    map.put("restrauntId", restaurantID);
                    //Log.e("MapRes", restaurantID);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                //header.put("X-Auth-Token", "s40w8so0044wo4gw88woks4kcksksckcwcskkc44");
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

    public void parseResponseProduct(String response) {
        Log.e("COSellerProductResponse", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");

            if(data==true) {
                JSONObject jobj = job.getJSONObject("data");
                JSONArray jarr = jobj.getJSONArray("products");

                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject productsobj = jarr.getJSONObject(i);
                    String category_id = productsobj.getString("category_id");
                    String category_name = productsobj.getString("category_name");
                    ProductsCategory asiaProductsCategory = new ProductsCategory(category_id, category_name, null);
                    ArrayList<ProductsSubCategory> asiaSubCategory = new ArrayList<>();
                    JSONArray productsarr = productsobj.getJSONArray("products");

                    for (int j = 0; j < productsarr.length(); j++) {
                        JSONObject productObj = productsarr.getJSONObject(j);
                        String productID = productObj.getString("productID");
                        String restaurant_id = productObj.getString("restaurant_id");
                        String name = productObj.getString("name");
                        String category = productObj.getString("category");
                        String sub_Category = productObj.getString("sub_Category");
                        String food_type = productObj.getString("food_type");
                        String price = productObj.getString("price");
                        String description = productObj.getString("description");
                        String quantity = productObj.getString("quantity");
                        String ingrediants = productObj.getString("ingrediants");
                        String image = productObj.getString("image");
                        //Logic to be applied for cuisine_name
                        //JSONArray jsonArray = jobj.optJSONArray("available_days");
                        String url = "";
                        try{
                        JSONArray jsonArray = productObj.getJSONArray("available_days");

                        for (int x = 0; x < jsonArray.length(); x++) {
                            url += jsonArray.optString(x)+",";
                            Log.e("CN", "url => " + url);
                        }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String available_start_time = productObj.getString("available_start_time");
                        String available_end_time = productObj.getString("available_end_time");
                        String createdAt = productObj.getString("createdAt");
                        String status = productObj.getString("status");
                        String category_name1 = productObj.getString("category_name");
                        String sub_category_name = productObj.getString("sub_category_name");

                        asiaSubCategory.add(new ProductsSubCategory(name, name ,price, productID,image,restaurant_id,category,sub_Category,food_type,description,quantity,ingrediants,available_start_time,available_end_time,createdAt,status,category_name1,sub_category_name,url));
                        //asiaSubCategory.add(new ProductsSubCategory(name, name ,price, productID,image));
                        Log.e("asiaSubCategory",new ProductsSubCategory(name, name,price, productID,image)+"");
                    }
                    asiaProductsCategory.setProductsSubCategory(asiaSubCategory);
                    productsCategories.add(asiaProductsCategory);


                }
                adapter = new MyExpandableListAdapter(getActivity(), productsCategories);
                expandableListView.setAdapter(adapter);
                expandableListView.expandGroup(0);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    boolean allowRefresh;
    @Override
    public void onResume() {
        super.onResume();
        //callgetSellerRestrauntProducts(rid);
        if (allowRefresh)
        {
            allowRefresh = false;
            callgetSellerRestrauntProducts(rid);
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        }
    }





}
