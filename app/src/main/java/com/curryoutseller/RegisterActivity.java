package com.curryoutseller;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.curryoutseller.appsharedpreference.AppGlobalConstants;
import com.curryoutseller.appsharedpreference.AppSharedPreference;
import com.curryoutseller.appsharedpreference.CommonUtilities;
import com.curryoutseller.model.CityModel;
import com.curryoutseller.model.CuisinesDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    //    EditText et_phNumber,et_address1,et_address2;
//    ImageView img_chek_terms,img_unchek_terms,img_chek_delivery,img_unchek_delivery, imgNumberClean,imgAddress1Clean,imgAddress2Clean;
//    LinearLayout ln_next;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    TextView txt_uploadImg, txt_cuisines, txtNonveg, txtVeg,txtFood,txtTerms;
    EditText etRestaurantName, etCity, etOwnerName, etEmail, et_phNumber, et_address1, et_address2;
    ImageView img_upload, back_icon, img_chek_terms, img_unchek_terms, img_chek_delivery, img_unchek_delivery, imgNumberClean, imgAddress1Clean, imgAddress2Clean,
            img_uncheck_veg, img_uncheck_nonVeg, img_check_nonVeg, img_check_veg,cancel_icon;
    String restau, city, owner, email, phnumber, address, foodtype, cuisines, image;
    String add = "+91", added = "";
    LinearLayout ln_save;
    ArrayList<CuisinesDataModel> alist;
    String strArrlst, strImage = "", getImg = "", foodt = "",delivery="",terms="",cityid="";
    Context context;
    ArrayList<Integer> strArrlst1;
    ArrayList<CuisinesDataModel> getCuisineList;
    ListView dialogLIst;
    Bitmap gallery;
    CheckBox checkboxVeg, checkboxNonveg,checkboxdelivery,checkboxterms;
    SparseBooleanArray sparseBooleanArray;
    String strListPosition;
    ScrollView scrollView;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Spinner spCityRes;

    ArrayList<CityModel> cityList;
    ArrayAdapter<CityModel> cityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        strArrlst1 = new ArrayList<String>();
//        for (int i = 0; i < 15; i++) {
//            strArrlst1.add("Number: " + i);
//        }


        init();
        listener();
        callgetAllCities();
    }

    private void init() {

//        et_phNumber=(EditText)findViewById(R.id.et_phNumber);
//        et_address1=(EditText)findViewById(R.id.et_address1);
//        et_address2=(EditText)findViewById(R.id.et_address2);
//        img_chek_terms=(ImageView)findViewById(R.id.img_chek_terms);
//        img_unchek_terms=(ImageView)findViewById(R.id.img_unchek_terms);
//        img_chek_delivery=(ImageView)findViewById(R.id.img_chek_delivery);
//        img_unchek_delivery=(ImageView)findViewById(R.id.img_unchek_delivery);
//        imgNumberClean=(ImageView)findViewById(R.id.imgNumberClean);
//        imgAddress1Clean=(ImageView)findViewById(R.id.imgAddress1Clean);
//        imgAddress2Clean=(ImageView)findViewById(R.id.imgAddress2Clean);
//
//        ln_next=(LinearLayout)findViewById(R.id.ln_next);

        etRestaurantName = (EditText) findViewById(R.id.etRestaurantName);
        //etCity = (EditText) findViewById(R.id.etCity);
        spCityRes = findViewById(R.id.spCityRes);
        etOwnerName = (EditText) findViewById(R.id.etOwnerName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        et_phNumber = (EditText) findViewById(R.id.et_phNumber);
        et_address1 = (EditText) findViewById(R.id.et_address1);
        et_address2 = (EditText) findViewById(R.id.et_address2);
        txt_uploadImg = (TextView) findViewById(R.id.txt_uploadImg);
        txt_cuisines = (TextView) findViewById(R.id.txt_cuisines);
        back_icon = (ImageView) findViewById(R.id.back_icon);
        img_upload = (ImageView) findViewById(R.id.img_upload);
        img_chek_terms = (ImageView) findViewById(R.id.img_chek_terms);
        img_unchek_terms = (ImageView) findViewById(R.id.img_unchek_terms);
        img_chek_delivery = (ImageView) findViewById(R.id.img_chek_delivery);
        img_unchek_delivery = (ImageView) findViewById(R.id.img_unchek_delivery);
        imgNumberClean = (ImageView) findViewById(R.id.imgNumberClean);
        imgAddress1Clean = (ImageView) findViewById(R.id.imgAddress1Clean);
        imgAddress2Clean = (ImageView) findViewById(R.id.imgAddress2Clean);
//        img_uncheck_veg = (ImageView) findViewById(R.id.img_uncheck_veg);
//        img_uncheck_nonVeg = (ImageView) findViewById(R.id.img_uncheck_nonVeg);
//        img_check_veg = (ImageView) findViewById(R.id.img_check_veg);
//        img_check_nonVeg = (ImageView) findViewById(R.id.img_check_nonVeg);
        checkboxVeg = (CheckBox) findViewById(R.id.checkboxveg);
        checkboxNonveg = (CheckBox) findViewById(R.id.checkboxNonveg);
        checkboxdelivery = (CheckBox) findViewById(R.id.checkboxdelivery);
        checkboxterms = (CheckBox) findViewById(R.id.checkboxterms);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        txtVeg = (TextView) findViewById(R.id.txtVeg);
        txtNonveg = (TextView) findViewById(R.id.txtNonveg);
        txtFood = findViewById(R.id.txtFood);
        txtTerms = findViewById(R.id.txtTerms);
        ln_save = (LinearLayout) findViewById(R.id.ln_save);
        strArrlst1=new ArrayList<>();
        cancel_icon = findViewById(R.id.cancel_icon);
        getCuisineList = new ArrayList<>();
        cityList = new ArrayList<>();
    }

    private void listener() {

        cancel_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_upload.setImageDrawable(null);
                img_upload.setVisibility(View.GONE);
            }
        });

//        txtVeg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Veg", Toast.LENGTH_LONG).show();
//            }
//        });

//        ln_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(RegisterActivity.this, AddProductActivity.class));
//            }
//        });
//
        et_phNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() > 0){
                    //imgNumberClean.setVisibility(View.VISIBLE);
                }else{
                    //imgNumberClean.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                phnumber = et_phNumber.getText().toString();
                added = add + phnumber;
                Log.e("Added",added);
            }
        });
//
//        imgNumberClean.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                et_phNumber.setText("");
//            }
//        });
//
//
//        et_address1.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if(charSequence.length() > 0){
//                    imgAddress1Clean.setVisibility(View.VISIBLE);
//                }else{
//                    imgAddress1Clean.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        imgAddress1Clean.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                et_address1.setText("");
//            }
//        });
//
//        et_address2.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                if(charSequence.length() > 0){
//                    imgAddress2Clean.setVisibility(View.VISIBLE);
//                }else{
//                    imgAddress2Clean.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        imgAddress2Clean.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                et_address2.setText("");
//            }
//        });
//
//
//        img_unchek_delivery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                img_unchek_delivery.setVisibility(View.GONE);
//                img_chek_delivery.setVisibility(View.VISIBLE);
//            }
//        });
//
//        img_chek_delivery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                img_unchek_delivery.setVisibility(View.VISIBLE);
//                img_chek_delivery.setVisibility(View.GONE);
//            }
//        });
//
//        img_unchek_terms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                img_unchek_terms.setVisibility(View.GONE);
//                img_chek_terms.setVisibility(View.VISIBLE);
//            }
//        });
//
//        img_chek_terms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                img_unchek_terms.setVisibility(View.VISIBLE);
//                img_chek_terms.setVisibility(View.GONE);
//            }
//        });

//        img_uncheck_veg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                img_check_veg.setVisibility(View.VISIBLE);
////                img_uncheck_veg.setVisibility(View.GONE);
////                foodt = txtVeg.getText().toString();
//                Toast.makeText(context, "veg", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        img_uncheck_nonVeg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //img_check_nonVeg.setVisibility(View.VISIBLE);
//                //img_uncheck_nonVeg.setVisibility(View.GONE);
//                //foodt = txtNonveg.getText().toString();
//                Toast.makeText(context, "ggg", Toast.LENGTH_SHORT).show();
//            }
//        });

        spCityRes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        checkboxVeg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    foodt += txtVeg.getText().toString()+",";
                    Log.e("Veg", foodt);
                    //checkboxVeg.setFocusable(false);
                }
                else{
                    foodt = "";
                    Log.e("Veg", foodt);
                    //checkboxVeg.setError("");
//                    checkboxVeg.setFocusable(true);
//                    checkboxVeg.setFocusableInTouchMode(true);
//                    checkboxVeg.requestFocus(View.FOCUS_UP);
                }

                //Toast.makeText(getApplicationContext(), "Veg", Toast.LENGTH_LONG).show();
            }
        });

        checkboxNonveg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true) {
                    foodt += txtNonveg.getText().toString();
                    Log.e("Non-Veg", foodt);
                    //checkboxNonveg.setError(null);
                }else{
                    foodt = "";
                    Log.e("Veg", foodt);

                }

                //Toast.makeText(getApplicationContext(), foodt, Toast.LENGTH_SHORT).show();
            }
        });

        checkboxdelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    delivery = "1";

                }
                else {
                    delivery = "0";
                }
                //Toast.makeText(RegisterActivity.this,delivery,Toast.LENGTH_LONG).show();
            }
        });

        checkboxterms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    terms = "1";
                    //checkboxterms.setError(null);
                }
                else
                    terms = "0";
                    //checkboxterms.setError("");
            }
        });
        txt_cuisines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(RegisterActivity.this, R.style.FullScreenDialogStyle);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.select_cuisines_layout);
                dialog.show();

                dialogLIst = (ListView) dialog.findViewById(R.id.listView);
                LinearLayout ln_addSaveCuisine = dialog.findViewById(R.id.ln_addSaveCuisine);
                callGetAllCuisines();
                ;
                alist = new ArrayList<>();

                ImageView cancel_icon = (ImageView) dialog.findViewById(R.id.cancel_icon);

                ln_addSaveCuisine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(strArrlst!=null) {
                            txt_cuisines.setText(strArrlst);
                            txt_cuisines.setError(null);
                            dialog.dismiss();
                        }
                        else{
                            txt_cuisines.setError("Cuisines Not Selected.");
                        }
                    }
                });

                cancel_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
//                            finish();

                            dialog.dismiss();
                        }
                        return true;
                    }
                });
            }


        });

        txt_uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        et_phNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                phnumber = et_phNumber.getText().toString();
//                added = add + phnumber;
//                Log.e("Added",added);
            }
        });


        ln_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                restau = etRestaurantName.getText().toString();
                //city = etCity.getText().toString();
                owner = etOwnerName.getText().toString();
                email = etEmail.getText().toString();
                phnumber = et_phNumber.getText().toString();
                address = et_address1.getText().toString();
                String cus = txt_cuisines.getText().toString();

                if(restau.isEmpty()) {
                    etRestaurantName.setError("Please Enter Restaurant Name");
                }
//                else if(city.isEmpty()){
//                    etCity.setError("Please Enter City");
//                }
                else if(owner.isEmpty()){
                    etOwnerName.setError("Please Enter Owner Name");
                }else if(phnumber.isEmpty()){
                        et_phNumber.setError("Please Enter Phone Number");

                }else if(email.isEmpty()){
                    etEmail.setError("Please Enter Email");
                }
                else if(!email.matches(emailPattern)){
                    etEmail.setError("Please Enter Email in form of abc@test.com");
                }
                else if(address.isEmpty()) {
                    et_address1.setError("Please Enter Address");
                }
                else if(foodt.isEmpty())
                {
                    //checkboxVeg.setError("");
                    //checkboxNonveg.setError("");
                    focusOnView();
                    Toast.makeText(RegisterActivity.this,"Please Select Food Type",Toast.LENGTH_LONG).show();
                }
//                else if(checkboxVeg.isChecked() || checkboxNonveg.isChecked()){
//                    //checkboxVeg.clearFocus();
//                    checkboxNonveg.
//                    checkboxVeg.setError(null);
//                    checkboxNonveg.setError(null);
//                }
                else if (cus.isEmpty()){
                    txt_cuisines.setError("Please Select Cuisines");
                }
//                else if (!cus.isEmpty()){
//                    txt_cuisines.setError("");
//                }
                else if(gallery==null ){
                    //txt_uploadImg.setError("Please Select Gallery");
                    Toast.makeText(RegisterActivity.this,"Please Select Image",Toast.LENGTH_LONG).show();

                }
//                else if (gallery!=null){
//
//                }
                else if(terms.isEmpty()){
                    //checkboxterms.setError("");
                    //focusOnViewTerms();
                    Toast.makeText(RegisterActivity.this,"Please Select Terms and Condition",Toast.LENGTH_LONG).show();
                }
//                else if(delivery.isEmpty()){
//                    checkboxdelivery.setError("Please Select Delivery Services");
//                }
//                else if(!checkboxterms.isChecked()){
//                    checkboxterms.setError(null);
//                }
//
                else if(img_upload.isShown()){
                    callAddRestaurantJSON(restau,cityid,owner,added,email,address,foodt,delivery);
                    //Toast.makeText(RegisterActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
                else{
                    //callAddRestaurantJSON(restau,city,owner,added,email,address,foodt,delivery);
                    Toast.makeText(RegisterActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
                Log.e("Image",img_upload.isShown()+"");
//                Intent in = new Intent(RegisterActivity.this,RestaurantsListActivity.class);
//                startActivity(in);


//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                Fragment myFragment = new RestaurantsListFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

//                if(strImage == null || getImg == null){
//
//                }
                //callRegRestaurant(restau,city,owner,email,phnumber,address,getImg);
                //callRegRestaurantVolleyMultipart(restau, city, owner, email, phnumber, address, getImg);


            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppCompatActivity activity = (AppCompatActivity)getContext();
//                activity.getSupportFragmentManager().popBackStack();
                finish();
                ;

            }
        });


        et_phNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    imgNumberClean.setVisibility(View.VISIBLE);
                } else {
                    imgNumberClean.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgNumberClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_phNumber.setText("");
            }
        });


        et_address1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    imgAddress1Clean.setVisibility(View.VISIBLE);
                } else {
                    imgAddress1Clean.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgAddress1Clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_address1.setText("");
            }
        });

        et_address2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    imgAddress2Clean.setVisibility(View.VISIBLE);
                } else {
                    imgAddress2Clean.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgAddress2Clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_address2.setText("");
            }
        });


//        img_unchek_delivery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                img_unchek_delivery.setVisibility(View.GONE);
//                img_chek_delivery.setVisibility(View.VISIBLE);
//            }
//        });
//
//        img_chek_delivery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                img_unchek_delivery.setVisibility(View.VISIBLE);
//                img_chek_delivery.setVisibility(View.GONE);
//            }
//        });
//
//        img_unchek_terms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                img_unchek_terms.setVisibility(View.GONE);
//                img_chek_terms.setVisibility(View.VISIBLE);
//            }
//        });
//
//        img_chek_terms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                img_unchek_terms.setVisibility(View.VISIBLE);
//                img_chek_terms.setVisibility(View.GONE);
//            }
//        });

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(RegisterActivity.this);
                String userChoosenTask;
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try{
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String userChoosenTask = null;
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }catch (Exception e){
        e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(RegisterActivity.this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        img_upload.setImageBitmap(bm);
        img_upload.setVisibility(View.VISIBLE);
        gallery = bm;

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        assert thumbnail != null;
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        img_upload.setImageBitmap(thumbnail);
        img_upload.setVisibility(View.VISIBLE);
        img_upload.requestFocus();
        gallery = thumbnail;
        getImg = destination.getPath();
        Log.e("Thumbnail", thumbnail.toString());
        Log.e("Destination", destination.toString());
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public String convertBase64(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            return encoded;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



//    public void callRegRestaurant(final String restau, final String city, final String owner, final String email, final String phnumber, final String address, final String img) {
//
//        if (!CommonUtilities.isOnline(RegisterActivity.this)) {
//            Toast.makeText(RegisterActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();
//
//        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "restraunt/addRestraunt";
//        String testurl = "";
//
//        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                pd.dismiss();
//                parseResponse(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                pd.dismiss();
//                NetworkResponse response = error.networkResponse;
//
//                Log.e("com.curryout", "error response " + response);
//
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Log.e("mls", "VolleyError TimeoutError error or NoConnectionError");
//                } else if (error instanceof AuthFailureError) {                    //TODO
//                    Log.e("mls", "VolleyError AuthFailureError");
//                } else if (error instanceof ServerError) {
//                    Log.e("mls", "VolleyError ServerError");
//                } else if (error instanceof NetworkError) {
//                    Log.e("mls", "VolleyError NetworkError");
//                } else if (error instanceof ParseError) {
//                    Log.e("mls", "VolleyError TParseError");
//                }
//                if (error instanceof ServerError && response != null) {
//                    try {
//                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                        // Now you can use any deserializer to make sense of data
//                        Log.e("com.curryout", "error response " + res);
//
//                        parseResponse(response.toString());
//
//                    } catch (UnsupportedEncodingException e1) {
//                        // Couldn't properly decode data to string
//                        e1.printStackTrace();
//                    }
//                }
//
//            }
//        }) {
//
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("name", "test");
//                map.put("city", "sfs");
//                map.put("owner_name", "Pichori");
//                map.put("phone", "+911234567890");
//                map.put("email", "abc@gm.com");
//                map.put("address", "hsjahj");
//                map.put("food_type", "Veg");
//                map.put("cuisines[0]", "1");
//                map.put("is_provide_delivery", "0");
//                map.put("cuisines[1]", "8");
//                map.put("cuisines[2]", "12");
//                map.put("image", img);
//
//                Log.e("RegisterRes Data",
//                        restau + city + owner + phnumber + email + address + foodtype
//                                + cuisines + img);
//
//
//                return map;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> header = new HashMap<>();
//                //header.put("Content-Type", "multipart/form-data");
//                header.put("Content-Type", "application/x-www-form-urlencoded");
//                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(RegisterActivity.this));
//                return header;
//            }
//
//            @Override
//            public Priority getPriority() {
//                return Priority.IMMEDIATE;
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//        };
//
//        sr.setRetryPolicy(new DefaultRetryPolicy(
//                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        ));
//        sr.setShouldCache(false);
//        VolleySingleton.getInstance(RegisterActivity.this).addToRequestQueue(sr);
//    }
//
//    public void parseResponse(String response) {
//        Log.e("CO Restaurant Response", "response " + response);
//        try {
//            JSONObject job = new JSONObject(response);
//            boolean data = (boolean) job.get("status");
//            String message = job.getString("message");
//            if (data == true) {
//                if (message.equals("Restaurant Added Successfully")) {
//                    Toast.makeText(RegisterActivity.this, "Restaurant Added Successfully", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
//                }
//            } else {
//                Toast.makeText(RegisterActivity.this, "Please Enter Proper Data", Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public void callGetAllCuisines() {
        if (!CommonUtilities.isOnline(RegisterActivity.this)) {
            Toast.makeText(RegisterActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(RegisterActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "common/getAllCuisines";
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponseCuisine(response);
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

                        parseResponseCuisine(response.toString());

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
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(RegisterActivity.this));
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
        VolleySingleton.getInstance(RegisterActivity.this).addToRequestQueue(sr);
    }

    public void parseResponseCuisine(String response) {
        Log.e("CurryOut Cuisi Response", "response " + response);
        try {
            Log.e("TRY", "test");
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            Log.e("status", "test");
            if (data == true) {
                JSONArray jarr = job.getJSONArray("data");
                Log.e("status", "true");
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jobj = jarr.getJSONObject(i);
                    String cuisine_id = jobj.getString("cuisineID");
                    String name = jobj.getString("name");
                    String parent_id = jobj.getString("parent_id");
                    String status = jobj.getString("status");
                    String image = jobj.getString("image");
                    String restraunt_count = jobj.getString("restraunt_count");

                    Log.e("CID", cuisine_id);
                    Log.e("CNAME", name);
                    CuisinesDataModel cdm = new CuisinesDataModel(cuisine_id, name);
                    //Toast.makeText(RegisterActivity.this, cdm.toString(), Toast.LENGTH_LONG).show();
                    alist.add(cdm);


                }
//                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_list_item_1, strArrlst1);
//                //alertdialog_Listview.setAdapter(adapter);
//                dialogLIst.setAdapter(adapter);
//
//                AdapterListC adapListC = new AdapterListC(alist);
//
//                dialogLIst.setAdapter(adapListC);
                ArrayAdapter<CuisinesDataModel> adap = new ArrayAdapter<>(RegisterActivity.this,android.R.layout.simple_list_item_multiple_choice,android.R.id.text1,alist);
                dialogLIst.setAdapter(adap);

                dialogLIst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        CuisinesDataModel ss = alist.get(position);
//                        String s = ss.getName();
//                        Toast.makeText(RegisterActivity.this, s+"", Toast.LENGTH_SHORT).show();
                         ;
                        Log.e("strListPosition",strListPosition+"");
                        sparseBooleanArray = dialogLIst.getCheckedItemPositions();

                        String ValueHolder = "" ;

                        int i = 0 ;

                        while (i < sparseBooleanArray.size()) {

                            if (sparseBooleanArray.valueAt(i)) {

                                ValueHolder += alist.get(sparseBooleanArray.keyAt(i)) + ",";
                               try{
                                strArrlst1.add(position);


                               }
                               catch (Exception e)
                               {
                                  e.printStackTrace();
                               }
                            }

                            i++ ;
                        }

                        CuisinesDataModel sd = alist.get(position);
                        Log.e("ArrL",sd.cuisineID+"");
                        //CuisinesDataModel sd = alist.get(x);
                        getCuisineList.add(sd);


                        ValueHolder = ValueHolder.replaceAll("(,)*$", "");

                        //Toast.makeText(RegisterActivity.this, "ListView Selected Values = " + ValueHolder, Toast.LENGTH_LONG).show();
                        strArrlst = ValueHolder;
                    }
                });
//                if(strArrlst!=null){
//                    String arr[]=strArrlst.split(",");
//
//                    int d=arr.length;
//
//                    for(int a=0;a<d;a++){
//                        Log.e("StrList",arr[a]);
//
//                        dialogLIst.setItemChecked(a,true);
//                        //dialogLIst.setItemChecked(strListPosition,true);
//
//                }}true
                if(strArrlst1!=null){
                    for(int a=0;a<strArrlst1.size();a++){
                        Log.e("StrList", String.valueOf(strArrlst1.get(0)));
                        try{
                        dialogLIst.setItemChecked(strArrlst1.get(a),true);}
                        catch (Exception e)
                        {e.printStackTrace();}
                    }}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public void callRegRestaurantVolleyMultipart(final String restau, final String city, final String owner, final String email, final String phnumber, final String address, final String img) {
//        if (!CommonUtilities.isOnline(RegisterActivity.this)) {
//            Toast.makeText(RegisterActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();
//
//        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "restraunt/addRestraunt";
//        String testurl = "";
//
//        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
//                new Response.Listener<NetworkResponse>() {
//                    @Override
//                    public void onResponse(NetworkResponse response) {
//                        try {
//                            //JSONObject obj = new JSONObject(new String(response.data));
//                            pd.dismiss();
//                            String obj = new String(response.data);
//                            Toast.makeText(getApplicationContext(), obj.toString(), Toast.LENGTH_SHORT).show();
//                            Log.e("Error", response.toString());
//                            Log.e("Error1", obj.toString());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pd.dismiss();
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                        Log.e("Error", error.getMessage());
//                    }
//                }) {
//
//            /*
//             * If you want to add more parameters with the image
//             * you can do it here
//             * here we have only one parameter with the image
//             * which is tags
//             * */
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("name", restau);
//                map.put("city", city);
//                map.put("owner_name", owner);
//                map.put("phone", phnumber);
//                map.put("email", email);
//                map.put("address", address);
//                map.put("food_type", "veg, non-veg");
//                map.put("is_provide_delivery", "0");
//                map.put("image", img);
//
//                JSONArray jarr = new JSONArray();
//                JSONObject ob=null;
//                for(int i=0;i<jarr.length();i++){
//                    //scfm = alist.get(i);
//                    ob = new JSONObject();
////                    ob.put("cuisine_id","12");
////                    jarr.put(ob);
////                    map.put("items",jarr);
//                }
//
//
//
//                map.put("cuisines","12");
//
//                Log.e("RegisterRes Data",
//                        restau + city + owner + phnumber + email + address + foodtype
//                                + cuisines + img);
//
//                Log.e("img",img);
//                return map;
//            }
//
//            /*
//             * Here we are passing image by renaming it with a unique name
//             * */
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                long imagename = System.currentTimeMillis();
//                //params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
//                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(gallery)));
//                Log.e("getByteData",convertBase64(gallery)+"");
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> header = new HashMap<>();
//                //header.put("Content-Type", "multipart/form-data");
//               //header.put("Content-Type", "application/x-www-form-urlencoded");
//                header.put("Content-Type", "application/json");
//                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(RegisterActivity.this));
//                return header;
//            }
//
//            @Override
//            public Priority getPriority() {
//                return Priority.IMMEDIATE;
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//        };
//
//        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
//                AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
//
//        );
//        volleyMultipartRequest.setShouldCache(false);
//        //adding the request to volley
//        //Volley.newRequestQueue(this).add(volleyMultipartRequest);
//        VolleySingleton.getInstance(RegisterActivity.this).addToRequestQueue(volleyMultipartRequest);
//    }

    public class AdapterListC extends BaseAdapter {

        Context context_;
        ArrayList<CuisinesDataModel> alist;
        ArrayList<String> aName;
        private LayoutInflater mInflater;

        public AdapterListC(ArrayList<CuisinesDataModel> alist) {
            this.alist = alist;
        }

        @Override
        public int getCount() {
            return alist.size();
        }

        @Override
        public Object getItem(int position) {
            return alist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final CuisinesDataModel cdm = alist.get(position);
            //mInflater = (LayoutInflater) context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mInflater = getLayoutInflater();
            View myv = mInflater.inflate(R.layout.select_cuisines_listitem, null);
            TextView txtName = (TextView) myv.findViewById(R.id.txt_cuisines);
            final CheckBox chkBox = myv.findViewById(R.id.chkBox);
            txtName.setText(cdm.getName());
            //final String[] st = {""};
            final ArrayList<String> st = new ArrayList<String>();
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true){
                //st[0] +=cdm.getName();
                st.add(cdm.getName());
                String data="";
                for(int i=0;i<st.size();i++){
                    data+=st.get(i);
                    Log.e("Data",data);
                }
                //Toast.makeText(RegisterActivity.this, cdm.getName(), Toast.LENGTH_LONG).show();
                Toast.makeText(RegisterActivity.this,data,Toast.LENGTH_LONG).show();

                }
                }
            });

            return myv;
        }

    }

    public void callAddRestaurantJSON(final String restau, final String city, final String owner, final String phnumber, final String email, final String address, final String fdtype,final String delivery){
            if (!CommonUtilities.isOnline(RegisterActivity.this)) {
                Toast.makeText(RegisterActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                return;
            }

//            final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
//            pd.setTitle("Please Wait");
//            pd.setCancelable(false);
//            pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(RegisterActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

            String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "restraunt/addRestraunt";
            String testurl = "";

            JSONObject map = new JSONObject();
            String mRequestBody = "";
            try {
                map.put("name", restau);
                map.put("city", city);
                map.put("owner_name", owner);
                map.put("phone", phnumber);
                map.put("email", email);
                map.put("address", address);
                map.put("food_type", fdtype);
                map.put("is_provide_delivery", delivery);
                map.put("image", convertBase64(gallery));



                int x = strArrlst1.size();
                Log.e("X",x+"");
                int y = getCuisineList.size();
                Log.e("Y",y+"");
                CuisinesDataModel cdm = new CuisinesDataModel();
                JSONArray jarr = new JSONArray();
                JSONObject ob=null;

                for(int i=0;i<getCuisineList.size();i++){
                    cdm = getCuisineList.get(i);
                    ob = new JSONObject();
                    ob.put("cuisine_id",cdm.getCuisineID());
                    //ob.put("cuisine_id","9");
                    Log.e("cuisine_id",cdm.getCuisineID());
                    jarr.put(ob);
                    map.put("cuisines",jarr);
                }

                JSONArray items = jarr;
                Log.e("ITEMSArray",items+"");

                Log.e("Map",jarr+"");
                mRequestBody = map.toString();

                Log.e("RegisterRes Data",
                        restau + city + owner + phnumber + email + address + fdtype
                                + delivery);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("Request Body",mRequestBody);
            final String finalMRequestBody = mRequestBody;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, map,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            pd.dismiss();
                            String res = response.toString();
                            parseResponseAdd(res);
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

                            parseResponseAdd(response.toString());

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
                    header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(RegisterActivity.this));
                    return header;
                }

                @Override
                public Priority getPriority() {
                    return Priority.IMMEDIATE;
                }

                // Adding request to request queue
                @Override
                public byte[] getBody()  {
                    try {
                        return finalMRequestBody == null ? null : finalMRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", finalMRequestBody, "utf-8");
                        return null;
                    }
                }

            };

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    AppGlobalConstants.WEBSERVICE_TIMEOUT_VALUE_IN_MILLIS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            jsonObjReq.setShouldCache(false);
            VolleySingleton.getInstance(RegisterActivity.this).addToRequestQueue(jsonObjReq);
        }

        public void parseResponseAdd(String response){
            Log.e("CO AddRes Response", "response " + response);
            try {
                JSONObject job = new JSONObject(response);
                boolean data = (boolean) job.get("status");
                String message = job.getString("message");
                if (data == true) {
                    if (message.equalsIgnoreCase("Restraunt Added Successfully")) {
                      Toast.makeText(RegisterActivity.this, "Restraunt Added Successfully.", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(RegisterActivity.this,MainActivity.class);
                        in.putExtra("FromAdd","2");
                        startActivity(in);
                        finish();
                    }
                }
                if(data==false){
                    if (message.equalsIgnoreCase("The Restraunt Name field is required.<br>\n")) {
                        Toast.makeText(RegisterActivity.this, "The Restraunt Name field is required.", Toast.LENGTH_SHORT).show();
                    }
                    if (message.equalsIgnoreCase("The City field is required.<br>\n")) {
                        Toast.makeText(RegisterActivity.this, "The City field is required.", Toast.LENGTH_SHORT).show();
                    }
                    if (message.equalsIgnoreCase("The Owner Name field is required.<br>\n")) {
                        Toast.makeText(RegisterActivity.this, "The Owner Name field is required.", Toast.LENGTH_SHORT).show();
                    }
                    if (message.equalsIgnoreCase("The Phone field is required.<br>\n")) {
                        Toast.makeText(RegisterActivity.this, "The Phone field is required.", Toast.LENGTH_SHORT).show();
                    }
                    if (message.equalsIgnoreCase("The Phone field must be exactly 13 characters in length.<br>\n")) {
                        Toast.makeText(RegisterActivity.this, "The Phone field must be exactly 13 characters in length.", Toast.LENGTH_SHORT).show();
                    }
                    if (message.equalsIgnoreCase("The Email field is required.<br>\n")) {
                        Toast.makeText(RegisterActivity.this, "The Email field is required.", Toast.LENGTH_SHORT).show();
                    }
                    if (message.equalsIgnoreCase("The Email field must contain a valid email address.<br>\n")) {
                        Toast.makeText(RegisterActivity.this, "The Email field must contain a valid email address.", Toast.LENGTH_SHORT).show();
                    }
                    if (message.equalsIgnoreCase("The Address field is required.<br>\n")) {
                        Toast.makeText(RegisterActivity.this, "The Address field is required.", Toast.LENGTH_SHORT).show();
                    }
                    if (message.equalsIgnoreCase("The Food Type field is required.<br>\n")) {
                        Toast.makeText(RegisterActivity.this, "The Food Type field is required.", Toast.LENGTH_SHORT).show();
                    }
                    if (message.equalsIgnoreCase("Restraunt cuisines are required.")) {
                        Toast.makeText(RegisterActivity.this, "Restraunt cuisines are required.", Toast.LENGTH_SHORT).show();
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }


//Testing

    private void focusOnView(){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anim = ObjectAnimator.ofInt(scrollView, "scrollY", txtFood.getBottom());
                anim.setDuration(1000);
                anim.start();
// scrollView.scrollTo(0, ln_priceDetails.getBottom());
            }
        });
    }

    private void focusOnViewTerms(){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anim = ObjectAnimator.ofInt(scrollView, "scrollY", txtTerms.getBottom());
                anim.setDuration(1000);
                anim.start();
// scrollView.scrollTo(0, ln_priceDetails.getBottom());
            }
        });
    }


    public void callgetAllCities(){
        if (!CommonUtilities.isOnline(RegisterActivity.this)) {
            Toast.makeText(RegisterActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd= new CustomProgressDialogue(RegisterActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "common/getAllCities";
        String testurl = "";

        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(RegisterActivity.this));
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
        VolleySingleton.getInstance(RegisterActivity.this).addToRequestQueue(sr);
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

                    cityList.add(citym);
                }

                cityAdapter = new ArrayAdapter(RegisterActivity.this,R.layout.spinner_display,R.id.txtSpin,cityList);
                spCityRes.setAdapter(cityAdapter);
                //cityAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}








