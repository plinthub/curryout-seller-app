package com.curryoutseller;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.androidquery.AQuery;
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

public class MyExpandableListAdapter implements ExpandableListAdapter {

    Context context= null;
    ArrayList<ProductsCategory> oiginalProductsCategoryList;
    ArrayList<ProductsCategory> productsCategoryList;
    AQuery aQuery;

    public MyExpandableListAdapter(Context context, ArrayList<ProductsCategory> data) {
        this.context = context;
        this.oiginalProductsCategoryList = new ArrayList<ProductsCategory>();
        this.oiginalProductsCategoryList.addAll(data);

        this.productsCategoryList = new ArrayList<ProductsCategory>();
        this.productsCategoryList.addAll(data);
        aQuery = new AQuery(context);
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {

        return productsCategoryList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return productsCategoryList.get(i).getProductsSubCategory().size();
    }

    @Override
    public ProductsCategory getGroup(int i) {
        return productsCategoryList.get(i);
    }

    @Override
    public ProductsSubCategory getChild(int groupPosition, int childPosition) {
        return productsCategoryList.get(groupPosition).getProductsSubCategory().get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }



    @Override
    public View getGroupView(int position, boolean isExpanded, View contentView, ViewGroup parent) {
        ProductsCategory productsCategory = productsCategoryList.get(position);
        if(contentView == null){
            contentView= LayoutInflater.from(context).inflate(R.layout.my_products_header_layout, parent, false);
        }
        TextView tvContinentName=(TextView)contentView.findViewById(R.id.header);
        tvContinentName.setText(productsCategory.getName());

        if (isExpanded) {
            tvContinentName.setTypeface(null, Typeface.BOLD);
            tvContinentName.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.icons8collapsearrow24, 0);
        } else {
            // If group is not expanded then change the text back into normal
            // and change the icon

            tvContinentName.setTypeface(null, Typeface.NORMAL);
            tvContinentName.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                    R.drawable.icons8expandarrow24, 0);
        }


        return contentView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View contentView, ViewGroup parent) {
        final ProductsSubCategory productsSubCategory = productsCategoryList.get(groupPosition).getProductsSubCategory().get(childPosition);
        if(contentView == null){
            contentView= LayoutInflater.from(context).inflate(R.layout.my_products_item_layout, parent, false);
        }



        TextView tvCountryName = (TextView) contentView.findViewById(R.id.txt_title);
        TextView tvSubName = (TextView) contentView.findViewById(R.id.txt_subTitle);
        TextView tvPrice = (TextView) contentView.findViewById(R.id.txt_price);
        final RoundRectCornerImageView imageView =(RoundRectCornerImageView)contentView.findViewById(R.id.imageView);
        final LinearLayout ln_addProduct = (LinearLayout)contentView.findViewById(R.id.ln_addProduct);
        //imageView.setImageResource(productsSubCategory.getFlag());
        final ToggleButton toggleButton1 = contentView.findViewById(R.id.toggleButton1);
        tvCountryName.setText(productsSubCategory.getName());
        tvSubName.setText(productsSubCategory.getSubName());
        tvPrice.setText(productsSubCategory.getPrice());
        //aQuery.id(imageView).image(productsSubCategory.getImgView());
        Glide.with(context).load(Uri.parse(productsSubCategory.getImgView()))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        final ImageView img_editProduct = contentView.findViewById(R.id.img_editProduct);


        img_editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context,UpdateProductActivity.class);
                //in.putExtra("PROID",productsSubCategory.getID());
                in.putExtra("PROID",productsSubCategory);
                context.startActivity(in);
                //Toast.makeText(context,"Edit Icon",Toast.LENGTH_LONG).show();
            }
        });

        String stat = productsSubCategory.status;
        if(stat.equals("1")){
            toggleButton1.setChecked(true);
        }else {
            toggleButton1.setChecked(false);
        }
        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String off = toggleButton1.getTextOff().toString();
//                Log.e("TBC",toggleButton1.isChecked()+"");
//                Log.e("TBS",toggleButton1.isSelected()+"");
//                Log.e("TBA",toggleButton1.isActivated()+"");
//                Log.e("TBID",productsSubCategory.status);
                String stat = productsSubCategory.status;
                Log.e("Condition",(toggleButton1.isChecked() && stat.equals("1"))+"");
                if((!toggleButton1.isChecked()) && (stat.equals("1") || stat.equals("0"))){
                    //Toast.makeText(context, "Product Disabled", Toast.LENGTH_SHORT).show();
                    callChangeProductStatus("0",productsSubCategory.getProductID());
//                    ln_addProduct.setEnabled(false);
//                    imageView.setEnabled(false);
//                    img_editProduct.setEnabled(false);

                }else{
                    //Toast.makeText(context, "ToggleButton"+productsSubCategory.getProductID(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, "Product Enabled", Toast.LENGTH_SHORT).show();
                    callChangeProductStatus("1",productsSubCategory.getProductID());
//                    ln_addProduct.setEnabled(true);
//                    imageView.setEnabled(true);
//                    img_editProduct.setEnabled(true);

            }
            }
        });

        return contentView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        if (productsCategoryList.size() == 0)
        return true;
        else
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return childId;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return groupId;
    }

    public void  callChangeProductStatus(final String status,final String proId){
        if (!CommonUtilities.isOnline(context)) {
            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd= new CustomProgressDialogue(context);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "product/changeProductStatus";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                parseResponseStatus(response);
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
                        parseResponseStatus(response.toString());

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }

            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap();
                map.put("productId",proId);
                map.put("status",status);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(context));
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
        VolleySingleton.getInstance(context).addToRequestQueue(sr);
    }

    public void parseResponseStatus(String response){
        Log.e("ProductStatus Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");

            if (data == true) {
                String msg = job.getString("message");
                if(msg.equals("Product Status Changed Successfully.")){
                    Toast.makeText(context, "Product Status Changed Successfully.", Toast.LENGTH_SHORT).show();
                }
            }
            else if(data == false){
                String msg = job.getString("message");
                if(msg.equals("Something went wrong! Please try again later.")){
                    Toast.makeText(context, "Something went wrong! Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }


            }catch (Exception e){
            e.printStackTrace();
        }

    }

}








