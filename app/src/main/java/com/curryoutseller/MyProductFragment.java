package com.curryoutseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;

public class MyProductFragment extends Fragment {


    String rid;
    TextView txt_ResName,txt_ResCuisine;
    ImageView img_ResImg,cancel_icon,edit_icon;
    AQuery aQuery;
    ViewPager viewPager;
    public MyProductFragment() {
    }

    public MyProductFragment(String rid) {
        try {
            this.rid = rid;
            Log.e("RID in PF", rid);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_product_fragment, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        aQuery = new AQuery(getActivity());
        img_ResImg = view.findViewById(R.id.img_ResImg);
        txt_ResName = view.findViewById(R.id.txt_ResName);
        txt_ResCuisine = view.findViewById(R.id.txt_ResCuisine);
        cancel_icon = view.findViewById(R.id.cancel_icon);
        edit_icon = view.findViewById(R.id.edit_icon);
        callgetSellerRestrauntById(rid);
        listener(view);
        return view;
    }

    private void listener(View view) {
        cancel_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish();
                AppCompatActivity activity = (AppCompatActivity)getContext();
                activity.getSupportFragmentManager().popBackStack();
            }
        });


        edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),UpdateRestaurantsActivity.class);
                in.putExtra("ResID",rid);
                startActivity(in);
            }
        });
    }
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new ProductsTabFragment(rid), "Products");
        adapter.addFragment(new AboutTabFragment(), "About");
        //adapter.addFragment(new ReviewTabFragment(), "Review");
        viewPager.setAdapter(adapter);
    }



    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            //return super.getItemPosition(object);
            return POSITION_NONE;
        }
    }


    public void callgetSellerRestrauntById(final String restaurantID){
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

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "restraunt/getSellerRestrauntById";
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
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap();
                map.put("restrauntId",restaurantID);
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

    public void parseResponse(String response) {
        Log.e("COSeller Parse Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean)job.get("status");
            if(data==true){
                JSONObject jobj = job.getJSONObject("data");

                //for (int i = 0; i < jarr.length(); i++) {
                    //JSONObject jobj = jarr.getJSONObject(i);
                    String restaurantID = jobj.getString("restrauntID");
                    String user_id = jobj.getString("user_id");
                    String name = jobj.getString("name");
                    String city = jobj.getString("city");
                    String owner_name = jobj.getString("owner_name");
                    String phone = jobj.getString("phone");
                    String email = jobj.getString("email");
                    String address = jobj.getString("address");
                    String imageRes = jobj.getString("image");
                    String food_type = jobj.getString("food_type");
                    String provide_delivery = jobj.getString("provide_delivery");
                    String status = jobj.getString("status");

                JSONArray jsonArray = jobj.getJSONArray("cuisine");
                String url="";
                for(int j=0; j<jsonArray.length(); j++)
                {
                    JSONObject o = jsonArray.getJSONObject(j);
                    String cuisine_id = o.getString("cuisine_id");
                    url += (String) o.getString("cuisine_name")+" | ";
                    Log.e("CusNname", "url => " + url);
                }
                String st = removeLastChar(url);


                    //Logic to be applied for cuisine_name
//                    JSONArray jsonArray = jobj.optJSONArray("cuisine_name");
//                    String url="";
//                    for(int j=0; j<jsonArray.length(); j++)
//                    {
//                        url += (String) jsonArray.getString(j)+" | ";
//                        Log.d("CN", "url => " + url);
//                    }


                    //aQuery.id(img_ResImg).image(imageRes,true,true,0,0,null,AQuery.FADE_IN);
                Glide.with(getActivity()).load(Uri.parse(imageRes))
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_ResImg);
                    txt_ResName.setText(name);
                    txt_ResCuisine.setText(st);
                }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 2);
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        callgetSellerRestrauntById(rid);
//    }


    boolean allowRefresh;
    @Override
    public void onResume() {
        super.onResume();
        callgetSellerRestrauntById(rid);
        viewPager.getAdapter().notifyDataSetChanged();
        if (allowRefresh)
        {
            allowRefresh = false;
            callgetSellerRestrauntById(rid);
            viewPager.getAdapter().notifyDataSetChanged();
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        }
    }

}
