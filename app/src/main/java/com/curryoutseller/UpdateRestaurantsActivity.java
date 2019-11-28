package com.curryoutseller;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

public class UpdateRestaurantsActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    TextView txt_uploadImg, txt_cuisines, txtNonveg, txtVeg, txtFood;
    EditText etRestaurantName, etCity, etOwnerName, etEmail, et_phNumber, et_address1, et_address2;
    ImageView img_upload, back_icon, img_chek_terms, img_unchek_terms, img_chek_delivery, img_unchek_delivery, imgNumberClean, imgAddress1Clean, imgAddress2Clean,
            img_uncheck_veg, img_uncheck_nonVeg, img_check_nonVeg, img_check_veg,cancel_icon;
    String restau, city, owner, email, phnumber, address, image;
    String add = "+91", added = "";
    LinearLayout ln_update;
    ArrayList<CuisinesDataModel> alist;
    ArrayList<CuisinesDataModel> alistUp;
    String strArrlst = "", strArr2st = "", strArr2Ids = "", getImg = "", foodt = "", delivery = "", terms = "1",cityid="";
    Context context;
    ArrayList<Integer> strArrlst1;
    ArrayList<Integer> strArrlst2;
    ArrayList<CuisinesDataModel> getCuisineList;
    ArrayList<CuisinesDataModel> dispList;
    ListView dialogLIst;
    Bitmap gallery, testBim;
    CheckBox checkboxVeg, checkboxNonveg, checkboxdelivery, checkboxterms;
    SparseBooleanArray sparseBooleanArray;
    String strListPosition;
    String resId, showImg = "";
    AQuery aquery;
    CuisinesDataModel dialogCuisine;
    ScrollView scrollView;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String new_Cus="";
    Spinner spCityRes;

    ArrayList<CityModel> cityList;
    ArrayAdapter<CityModel> cityAdapter;
    CityModel citym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_restaurant);
        init();
        listener();
        resId = getIntent().getStringExtra("ResID");
        Log.e("RESID in Update", resId);
        callgetSellerRestrauntById(resId);
        callgetAllCities();
    }

    private void init() {
        etRestaurantName = (EditText) findViewById(R.id.etRestaurantNameUp);
        //etCity = (EditText) findViewById(R.id.etCityUp);
        spCityRes = findViewById(R.id.spCityResUp);
        etOwnerName = (EditText) findViewById(R.id.etOwnerNameUp);
        etEmail = (EditText) findViewById(R.id.etEmailUp);
        et_phNumber = (EditText) findViewById(R.id.et_phNumberUp);
        et_address1 = (EditText) findViewById(R.id.et_address1Up);
        et_address2 = (EditText) findViewById(R.id.et_address2);
        txt_uploadImg = (TextView) findViewById(R.id.txt_uploadImg);
        txt_cuisines = (TextView) findViewById(R.id.txt_cuisinesUp);
        back_icon = (ImageView) findViewById(R.id.back_icon);
        img_upload = (ImageView) findViewById(R.id.img_uploadUp);
        img_chek_terms = (ImageView) findViewById(R.id.img_chek_terms);
        img_unchek_terms = (ImageView) findViewById(R.id.img_unchek_terms);
        img_chek_delivery = (ImageView) findViewById(R.id.img_chek_delivery);
        img_unchek_delivery = (ImageView) findViewById(R.id.img_unchek_delivery);
        imgNumberClean = (ImageView) findViewById(R.id.imgNumberClean);
        imgAddress1Clean = (ImageView) findViewById(R.id.imgAddress1Clean);
        imgAddress2Clean = (ImageView) findViewById(R.id.imgAddress2Clean);
        checkboxVeg = (CheckBox) findViewById(R.id.checkboxvegUp);
        checkboxNonveg = (CheckBox) findViewById(R.id.checkboxNonvegUp);
        checkboxdelivery = (CheckBox) findViewById(R.id.checkboxdeliveryUp);
        checkboxterms = (CheckBox) findViewById(R.id.checkboxtermsUp);
        txtVeg = (TextView) findViewById(R.id.txtVegUp);
        txtNonveg = (TextView) findViewById(R.id.txtNonvegUp);
        ln_update = (LinearLayout) findViewById(R.id.ln_update);
        scrollView = (ScrollView) findViewById(R.id.scrollViewUp);
        txtFood = findViewById(R.id.txtFoodUp);
        strArrlst1 = new ArrayList<>();
        strArrlst2 = new ArrayList<>();
        getCuisineList = new ArrayList<>();
        dispList = new ArrayList<>();
        cityList = new ArrayList<>();
        aquery = new AQuery(UpdateRestaurantsActivity.this);
        cancel_icon = findViewById(R.id.cancel_icon);
        alistUp = new ArrayList<>();
        checkboxterms.setChecked(true);
        if (checkboxterms.isChecked() == false) {
            terms = "0";
            checkboxterms.setError("");
        } else {
            terms = "1";
        }

        boolean testVeg = checkboxVeg.isChecked();
        if(testVeg==true){
            foodt = txtVeg.getText().toString();
        }
        else{
            foodt="";
        }

        boolean testNonVeg = checkboxNonveg.isChecked();
        if(testNonVeg==true){
            foodt = txtNonveg.getText().toString();
        }
        else{
            foodt="";
        }



    }

    private void listener() {

        cancel_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_upload.setImageDrawable(null);
                img_upload.setVisibility(View.GONE);
            }
        });

        et_phNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    //imgNumberClean.setVisibility(View.VISIBLE);
                } else {
                    //imgNumberClean.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                phnumber = et_phNumber.getText().toString();
                added = add + phnumber;
               // Log.e("Added", added);
            }
        });
        spCityRes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citym = cityList.get(position);
               // Log.e("TestCity", citym.getName() + " " + citym.getCitiesID());
                cityid = citym.getCitiesID();
              //  Log.e("CityId",cityid);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        checkboxVeg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked == true) {
//                    if(foodt.contains("Veg")){
//                        foodt.replaceAll("Veg","");
//                        Log.e("FoodContains",foodt);
//                    }
//                    foodt += txtVeg.getText().toString() + ",";
//                    Log.e("checkboxVeg", foodt);
//                } else {
////                    foodt = "";
////                    Log.e("Veg", foodt);
//                    if(checkboxNonveg.isChecked()){
//                        Log.e("checkboxVeg Else", foodt);
//                        //Toast.makeText(UpdateRestaurantsActivity.this,"VegElse"+foodt,Toast.LENGTH_LONG).show();
//                     }else{foodt="";}
//                }
//
//                //Toast.makeText(getApplicationContext(), "Veg", Toast.LENGTH_LONG).show();
           }
       });

        checkboxNonveg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                if (isChecked == true) {
//                    if(foodt.contains("Non-Veg")){
//                        foodt.replaceAll("Non-Veg","");
//                        Log.e("FoodContains",foodt);
//                    }
//                    foodt += txtNonveg.getText().toString()+",";
//                    Log.e("checkboxNonveg", foodt);
//                } else {
////                    foodt = "";
////                    Log.e("Veg", foodt);
//                    if(checkboxVeg.isChecked()){
//                        Log.e("checkboxNonveg - Else", foodt);
//                        //Toast.makeText(UpdateRestaurantsActivity.this,"Non-VegElse"+foodt,Toast.LENGTH_LONG).show();
//                    }else{foodt="";}
//                }
//
//
            }
        });

        checkboxdelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true)
                    delivery = "1";
                else
                    delivery = "0";

                //Toast.makeText(RegisterActivity.this,delivery,Toast.LENGTH_LONG).show();
            }
        });

        checkboxterms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    terms = "1";
                    //checkboxterms.setError("");
                } else
                    terms = "0";
            }
        });
        txt_cuisines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txt_cuisines.equals(null)) {
                    final Dialog dialog = new Dialog(UpdateRestaurantsActivity.this, R.style.FullScreenDialogStyle);
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
                            dialog.dismiss();
                            txt_cuisines.setText(strArrlst);

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
                } else {
                    Log.e("Testtxt", "Cuisine");
                    String txt_cus = txt_cuisines.getText().toString();
                    Log.e("Testtxt", txt_cus+" ="+strArr2st);
                    String cus_name[] = txt_cus.split(",");
                    Log.e("strArr2st first", "dffrdg"+strArr2st);

                    for (int x = 0; x <cus_name.length; x++) {
                        strArr2st += cus_name[x] + ",";
                        Log.e("strArr2stXArray", strArr2st);
                        Log.e("DialogCus", dialogCuisine.getCuisineID() + ":" + dialogCuisine.getName());
                    }

                    //-------------------------------Testing-------------------
                    final Dialog dialog = new Dialog(UpdateRestaurantsActivity.this, R.style.FullScreenDialogStyle);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.select_cuisines_layout);
                    dialog.show();

                    dialogLIst = (ListView) dialog.findViewById(R.id.listView);
                    callGetAllCuisinesUpdate(strArr2st);
                    //Log.e("StringListOutside",strArr2st);
                    alist = new ArrayList<>();

                    ImageView cancel_icon = (ImageView) dialog.findViewById(R.id.cancel_icon);
                    LinearLayout ln_addSaveCuisine = dialog.findViewById(R.id.ln_addSaveCuisine);

                    ln_addSaveCuisine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                            txt_cuisines.setText(strArr2st);
                            //new_Cus = strArr2st;
                            alistUp = new ArrayList<>();
                            Log.e("StringList",strArr2st);
                            alistUp.add(new CuisinesDataModel(strArr2st));
                            //getCuisineList = alistUp;
                            Log.e("AList Up Inside",alistUp.toString());
                            //callGetAllCuisinesUpdate(strArr2st);


                        }
                    });
                    Log.e("AList Up",alist.toString());
                    Log.e("AList",alist.toString());
                   // callGetAllCuisinesUpdate(strArr2st);
                    Log.e("StringListOutside",strArr2st);
                    cancel_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //txt_cuisines.setText(strArr2st);
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
            }


        });

        txt_uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ln_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                restau = etRestaurantName.getText().toString();
                //city = etCity.getText().toString();
                owner = etOwnerName.getText().toString();
                email = etEmail.getText().toString();
                phnumber = et_phNumber.getText().toString();
                address = et_address1.getText().toString();
               // Log.e("Img_Upload", img_upload.isShown() + "");
                gallery = ((BitmapDrawable) img_upload.getDrawable()).getBitmap();

                String cus = txt_cuisines.getText().toString();

                if (restau.isEmpty()) {
                    etRestaurantName.setError("Please Enter Restaurant Name");
                }
//                else if (city.isEmpty()) {
//                    etCity.setError("Please Enter City");
//                }
                else if (owner.isEmpty()) {
                    etOwnerName.setError("Please Enter Owner Name");
                } else if (phnumber.isEmpty()) {
                    et_phNumber.setError("Please Enter Phone Number");

                } else if (email.isEmpty()) {
                    etEmail.setError("Please Enter Email");
                } else if (!email.matches(emailPattern)) {
                    etEmail.setError("Please Enter Email in form of abc@test.com");
                } else if (address.isEmpty()) {
                    et_address1.setError("Please Enter Address");
                }
//                else if (foodt.isEmpty()) {
//                    //checkboxVeg.setError("");
//                    //checkboxNonveg.setError("");
//                    focusOnView();
//                    Toast.makeText(UpdateRestaurantsActivity.this, "Please Select Food Type", Toast.LENGTH_LONG).show();
//                }
//                else if(checkboxVeg.isChecked() || checkboxNonveg.isChecked()){
//                    focusOnView();
//                    Toast.makeText(UpdateRestaurantsActivity.this, "Please Select Food Type", Toast.LENGTH_LONG).show();
//                }
                else if (cus.isEmpty()) {
                    txt_cuisines.setError("Please Select Cuisines");
                }
//                else if (!cus.isEmpty()){
//                    txt_cuisines.setError("");
//                }
                else if (gallery == null) {
                    //txt_uploadImg.setError("Please Select Gallery");
                    Toast.makeText(UpdateRestaurantsActivity.this, "Please Select Image", Toast.LENGTH_LONG).show();

                }
                else if (checkboxVeg.isChecked() == false && checkboxNonveg.isChecked() == false){
                    Toast.makeText(UpdateRestaurantsActivity.this, "Please Select Food Type", Toast.LENGTH_LONG).show();
                }

                else if (terms.isEmpty()) {
                    //checkboxterms.setError("");
                    focusOnViewTerms();
                    Toast.makeText(UpdateRestaurantsActivity.this, "Please Select Terms and Condition", Toast.LENGTH_LONG).show();
                } else if (img_upload.isShown()) {
                   // Log.e("Data:--", resId + " : " + restau + " : " + cityid + " : " + owner + " : " + added + " : " + email + " : " + address + " : " + foodt + " : " + delivery);


                    if(checkboxVeg.isChecked()==true){
                        //foodt = txtVeg.getText().toString();
                        foodt += txtVeg.getText().toString()+",";
                    }
                    if(checkboxNonveg.isChecked()==true){
                        foodt += txtNonveg.getText().toString();
                    }



                    callAddUpdateRestaurantJSON(resId, restau, cityid, owner, added, email, address, foodt, delivery);
                } else {
                    Toast.makeText(UpdateRestaurantsActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
//                Intent in = new Intent(RegisterActivity.this,RestaurantsListActivity.class);
//                startActivity(in);


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
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRestaurantsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(UpdateRestaurantsActivity.this);
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
                bm = MediaStore.Images.Media.getBitmap(UpdateRestaurantsActivity.this.getContentResolver(), data.getData());
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
       // Log.e("Thumbnail", thumbnail.toString());
        //Log.e("Destination", destination.toString());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void callgetSellerRestrauntById(final String restaurantID) {
        if (!CommonUtilities.isOnline(UpdateRestaurantsActivity.this)) {
            Toast.makeText(UpdateRestaurantsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(UpdateRestaurantsActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd = new CustomProgressDialogue(UpdateRestaurantsActivity.this);
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
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("restrauntId", restaurantID);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateRestaurantsActivity.this));
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
        VolleySingleton.getInstance(UpdateRestaurantsActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse(String response) {
        Log.e("COSeller Parse Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            if (data == true) {
                JSONObject jobj = job.getJSONObject("data");

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
                String approve_status = jobj.getString("approve_status");
                String city_name = jobj.getString("city_name");

                //JSONArray jsonArray = jobj.optJSONArray("cuisine_name");

                //Logic to be applied for cuisine_name
//                JSONArray jsonArray = jobj.optJSONArray("cuisine_name");
//                String url="";
//                for(int j=0; j<jsonArray.length(); j++)
//                {
//                    url += (String) jsonArray.getString(j)+",";
//                    Log.d("CN", "url => " + url);
//                }

                JSONArray jsonArray = jobj.getJSONArray("cuisine");
                String url = "", cuisine_id, cuisine_name;
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject o = jsonArray.getJSONObject(j);
                    cuisine_id = o.getString("cuisine_id");
                    cuisine_name = o.getString("cuisine_name");
                    url += cuisine_name + ",";
                    strArr2Ids += cuisine_id + ",";
                   // Log.e("CusNname", "url => " + url);
                    alist = new ArrayList<>();
                    dialogCuisine = new CuisinesDataModel(cuisine_id, cuisine_name);
                    dispList.add(dialogCuisine);
                }

                spCityRes.setSelection(getIndex(spCityRes, city_name));

                etRestaurantName.setText(name);
                //etCity.setText(city_name);

                etOwnerName.setText(owner_name);
                String ph = phone.substring(3);
                et_phNumber.setText(ph);
                etEmail.setText(email);
                et_address1.setText(address);
                String st = removeLastChar(url);
                txt_cuisines.setText(st);

                if(imageRes!=null){
                    img_upload.setVisibility(View.VISIBLE);
                    //aquery.id(img_upload).image(imageRes);
                    Glide.with(UpdateRestaurantsActivity.this).asBitmap()
                            .load(imageRes)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_upload);
                }
                //aquery.id(img_upload).image(imageRes);


//
//                Glide.with(UpdateRestaurantsActivity.this).load(imageRes)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_upload);

                //Picasso.with(UpdateRestaurantsActivity.this).load(imageRes).into(img_upload);
                testBim = BitmapFactory.decodeFile(imageRes);
                if (showImg.equals("null")) {
                  //  Log.e("TestBimIf", "If");
                } else {
                    showImg = convertBase64(testBim);
                }

                if (provide_delivery.equals("1")) {
                    checkboxdelivery.setChecked(true);
                    delivery = "1";
                }



                String ft[] = food_type.split(",");
               // Log.e("FT", ft[0]);

                for (int i = 0; i < ft.length; i++) {
                  //  Log.e("First-->" + i, ft[i]);
                    if(ft[i].equalsIgnoreCase("Veg"))
                    {
                        checkboxVeg.setChecked(true);

                    }
                    else  if(ft[i].equalsIgnoreCase("Non-Veg"))
                    {
                        checkboxNonveg.setChecked(true);

                    }
                   // Log.e("ConditionVeg", ft[0].equalsIgnoreCase("Veg") + "");
                   // Log.e("ConditionNon-Veg", ft[0].equalsIgnoreCase("Non-Veg") + "");
//                    if (ft.length == 0) {
//                        Log.e("Length Zero", ft.length + "");
//
//                        if (ft[0].equalsIgnoreCase("Veg") || ft[1].equalsIgnoreCase("Veg")) {
//                            checkboxVeg.setChecked(true);
//                            //foodt += "Veg";
//                        }
//                        if (ft[0].equalsIgnoreCase("Non-Veg") || ft[1].equalsIgnoreCase("Non-Veg")) {
//                            checkboxNonveg.setChecked(true);
//                            //foodt += "Non-Veg";
//                        }
//                    }
//                    if (ft.length > 0) {
//                        Log.e("Length Greater", ft.length + "");
//
//                        if (ft[0].equalsIgnoreCase("Veg") || ft[1].equalsIgnoreCase("Veg")) {
//                            checkboxVeg.setChecked(true);
//                            //foodt += "Veg";
//                        }
//                        if (ft[0].equalsIgnoreCase("Non-Veg") || ft[1].equalsIgnoreCase("Non-Veg")) {
//                            checkboxNonveg.setChecked(true);
//                            //foodt += "Non-Veg";
//                        }
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    public void callGetAllCuisines() {
        if (!CommonUtilities.isOnline(UpdateRestaurantsActivity.this)) {
            Toast.makeText(UpdateRestaurantsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd = new CustomProgressDialogue(UpdateRestaurantsActivity.this);
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
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateRestaurantsActivity.this));
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
        VolleySingleton.getInstance(UpdateRestaurantsActivity.this).addToRequestQueue(sr);
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

                ArrayAdapter<CuisinesDataModel> adap = new ArrayAdapter<>(UpdateRestaurantsActivity.this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, alist);
                dialogLIst.setAdapter(adap);

                dialogLIst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        CuisinesDataModel ss = alist.get(position);
//                        String s = ss.getName();
//                        Toast.makeText(RegisterActivity.this, s+"", Toast.LENGTH_SHORT).show();
                        ;
                        Log.e("strListPosition", strListPosition + "");
                        sparseBooleanArray = dialogLIst.getCheckedItemPositions();

                        String ValueHolder = "";

                        int i = 0;

                        while (i < sparseBooleanArray.size()) {

                            if (sparseBooleanArray.valueAt(i)) {

                                ValueHolder += alist.get(sparseBooleanArray.keyAt(i)) + ",";
                                try {
                                    strArrlst1.add(position);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            i++;
                        }

                        CuisinesDataModel sd = alist.get(position);
                        Log.e("ArrL", sd.cuisineID + "");
                        //CuisinesDataModel sd = alist.get(x);
                        getCuisineList.add(sd);


                        ValueHolder = ValueHolder.replaceAll("(,)*$", "");

                        //Toast.makeText(RegisterActivity.this, "ListView Selected Values = " + ValueHolder, Toast.LENGTH_LONG).show();
                        strArrlst = ValueHolder;
                    }
                });
                if (strArrlst1 != null) {
                    for (int a = 0; a < strArrlst1.size(); a++) {
                        Log.e("StrList", String.valueOf(strArrlst1.get(0)));
                        try {
                            dialogLIst.setItemChecked(strArrlst1.get(a), true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callGetAllCuisinesUpdate(final String cus_name) {

        if (!CommonUtilities.isOnline(UpdateRestaurantsActivity.this)) {
            Toast.makeText(UpdateRestaurantsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("In Api",cus_name);
        final CustomProgressDialogue pd = new CustomProgressDialogue(UpdateRestaurantsActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "common/getAllCuisines";
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseResponseCuisineUpdate(response, cus_name);
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

                        parseResponseCuisineUpdate(response.toString(), cus_name);

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
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateRestaurantsActivity.this));
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
        VolleySingleton.getInstance(UpdateRestaurantsActivity.this).addToRequestQueue(sr);
    }

    public void parseResponseCuisineUpdate(String response, String cus_name) {
        Log.e("CurryOut Cuisi Response", "response " + response);
        try {
            Log.e("TRY", "test");
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            Log.e("status", "test");

            if (data == true) {
                JSONArray jarr = job.getJSONArray("data");
                Log.e("status", "true");
                String name = "", cuisine_id = "";
                CuisinesDataModel cdm = null;

                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jobj = jarr.getJSONObject(i);
                    cuisine_id = jobj.getString("cuisineID");
                    name = jobj.getString("name");
                    String parent_id = jobj.getString("parent_id");
                    String status = jobj.getString("status");
                    String image = jobj.getString("image");
                    String restraunt_count = jobj.getString("restraunt_count");
                    //cus_name.contains(name);
                    Log.e("CID", cuisine_id);
                    Log.e("CNAME", name);
                    Log.e("dfd", "" + cus_name.contains(name));
                    // if(cus_name.contains(name)) {
                    cdm = new CuisinesDataModel(cuisine_id, name);
                    //Toast.makeText(RegisterActivity.this, cdm.toString(), Toast.LENGTH_LONG).show();
                    //alist = new ArrayList<>();
                    alist.add(cdm);
                    Log.e("UP_CUS", alist.size() + "");
                    //   }
                    //Test
//                    String st[] = cus_name.split(",");
//                    for (int r = 0; r < st.length; r++) {
//                        Log.e("Cus_Name", st[r]);
//                    }


                    if (cus_name.contains(name)) {
                        strArrlst2.add(i);
                        Log.e("IF_CUS", name);
                        Log.e("I IN CUS", i + "");
                        Log.e("IF_STR", strArrlst2.size() + "");
                    }


                }
                Log.e("Cus_NameRes", cus_name);
                //Log.e("Cus_NameRes", name);
                String s[] = cus_name.split(" ");
//                    if (name.equals(cus_name)){
//                        Log.e("TestCus_Name","If");
//                        CuisinesDataModel cdm1 = new CuisinesDataModel(cuisine_id, name);
//                        alist.add(cdm1);
//                    }
//                    else{
//                        Log.e("TestCus_Name","Else");
//                    }

                ArrayAdapter<CuisinesDataModel> adap = new ArrayAdapter<>(UpdateRestaurantsActivity.this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, alist);
                dialogLIst.setAdapter(adap);

                dialogLIst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (dialogLIst.isItemChecked(position)) {
                            strArrlst2.clear();
                            strArr2st="";
                        }
                        Log.e("strListPositionfff", strListPosition + "");
                        sparseBooleanArray = dialogLIst.getCheckedItemPositions();

                        String ValueHolder = "";

                        int i = 0;

                        while (i < sparseBooleanArray.size()) {

                            if (sparseBooleanArray.valueAt(i)) {

                                ValueHolder += alist.get(sparseBooleanArray.keyAt(i)) + ",";
                                try {
                                    strArrlst2.add(position);
                                    Log.e("Pos", position + "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            i++;
                        }

                        CuisinesDataModel sd = alist.get(position);
                        Log.e("ArrLhhhh", sd.getName() + "");
                        getCuisineList.add(sd);
                        Log.e("CETCui", getCuisineList.toString());
                        ValueHolder = ValueHolder.replaceAll("(,)*$", "");

                        //Toast.makeText(RegisterActivity.this, "ListView Selected Values = " + ValueHolder, Toast.LENGTH_LONG).show();
                        strArr2st = ValueHolder;
                        Log.e("ValueHolder fdfdfdg",""+strArrlst2);

                        Log.e("ValueHolder", strArr2st);



                    }
                });
                if (strArrlst2 != null) {
                    for (int a = 0; a < strArrlst2.size(); a++) {
                        Log.e("StrList", String.valueOf(strArrlst2.get(0)));
                        try {
                            dialogLIst.setItemChecked(strArrlst2.get(a), true);
                            //alistUp

                            CuisinesDataModel cd = alist.get(a);
                            Log.e("CDCheck", cd.getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callAddUpdateRestaurantJSON(final String id, final String restau, final String city, final String owner, final String phnumber, final String email, final String address, final String fdtype, final String delivery) {
        if (!CommonUtilities.isOnline(UpdateRestaurantsActivity.this)) {
            Toast.makeText(UpdateRestaurantsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd = new CustomProgressDialogue(UpdateRestaurantsActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "restraunt/addRestraunt";
        String testurl = "";

        JSONObject map = new JSONObject();
        String mRequestBody = "";
        try {
         //   Log.e("MapResId", resId);
           // Log.e("MapResId1", id);
            map.put("restrauntId", resId);
            map.put("name", restau);
            map.put("city", city);
            map.put("owner_name", owner);
            map.put("phone", phnumber);
            map.put("email", email);
            map.put("address", address);
            map.put("food_type", fdtype);
            map.put("is_provide_delivery", delivery);
            map.put("image", convertBase64(gallery));

            CuisinesDataModel cdm = new CuisinesDataModel();
            JSONArray jarr = new JSONArray();
            JSONObject ob = null;
            if (strArrlst2.size() == 0) {
            } else {
                int x = strArrlst2.size();
                Log.e("X", x + "");
                int y = getCuisineList.size();
                Log.e("Y", y + "");
                //CuisinesDataModel cdm = new CuisinesDataModel();
                //JSONArray jarr = new JSONArray();
                //JSONObject ob = null;
                for (int i = 0; i < getCuisineList.size(); i++) {
                    cdm = getCuisineList.get(i);
                    ob = new JSONObject();
                    ob.put("cuisine_id", cdm.getCuisineID());
                    //ob.put("cuisine_id","9");
                    Log.e("cuisine_id", cdm.getCuisineID());
                    jarr.put(ob);
                    map.put("cuisines", jarr);


                }


            }
            if (strArr2Ids != null) {
                String cus_id[] = strArr2Ids.split(",");
                for (int x = 0; x < cus_id.length; x++) {
                    //CuisinesDataModel cdm = new CuisinesDataModel();
                    //JSONArray jarr = new JSONArray();
                    //JSONObject ob = null;

                    cdm = dispList.get(x);
                    ob = new JSONObject();
                    ob.put("cuisine_id", cdm.getCuisineID());
                    //ob.put("cuisine_id","9");
                    Log.e("cuisine_id", cdm.getCuisineID());
                    jarr.put(ob);
                    map.put("cuisines", jarr);
                }
            } else {
            }
//                CuisinesDataModel cdm = new CuisinesDataModel();
//                JSONArray jarr = new JSONArray();
//                JSONObject ob = null;

//                for (int i = 0; i < getCuisineList.size(); i++) {
//                    cdm = getCuisineList.get(i);
//                    ob = new JSONObject();
//                    ob.put("cuisine_id", cdm.getCuisineID());
//                    //ob.put("cuisine_id","9");
//                    Log.e("cuisine_id", cdm.getCuisineID());
//                    jarr.put(ob);
//                    map.put("cuisines", jarr);
//                }


            JSONArray items = jarr;
            Log.e("ITEMSArray", items + "");

            Log.e("Map", jarr + "");
            mRequestBody = map.toString();

            Log.e("RegisterRes Data",
                    restau + city + owner + phnumber + email + address + fdtype
                            + delivery);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Request Body", mRequestBody);
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
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateRestaurantsActivity.this));
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            // Adding request to request queue
            @Override
            public byte[] getBody() {
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
        VolleySingleton.getInstance(UpdateRestaurantsActivity.this).addToRequestQueue(jsonObjReq);
    }

    public void parseResponseAdd(String response) {
        Log.e("CO AddRes Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message = job.getString("message");
            if (data == true) {
                if (message.equalsIgnoreCase("Restraunt Updated Successfully")) {
                    Toast.makeText(UpdateRestaurantsActivity.this, "Restraunt Updated Successfully.", Toast.LENGTH_SHORT).show();

                    Intent in = new Intent(UpdateRestaurantsActivity.this, MainActivity.class);
                    in.putExtra("FromUpdate", "1");
                    startActivity(in);
                    finish();

                }
            }
            if (data == false) {
                if (message.equalsIgnoreCase("The Restraunt Name field is required.<br>\n")) {
                    Toast.makeText(UpdateRestaurantsActivity.this, "The Restraunt Name field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The City field is required.<br>\n")) {
                    Toast.makeText(UpdateRestaurantsActivity.this, "The City field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Owner Name field is required.<br>\n")) {
                    Toast.makeText(UpdateRestaurantsActivity.this, "The Owner Name field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Phone field is required.<br>\n")) {
                    Toast.makeText(UpdateRestaurantsActivity.this, "The Phone field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Phone field must be exactly 13 characters in length.<br>\n")) {
                    Toast.makeText(UpdateRestaurantsActivity.this, "The Phone field must be exactly 13 characters in length.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Email field is required.<br>\n")) {
                    Toast.makeText(UpdateRestaurantsActivity.this, "The Email field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Email field must contain a valid email address.<br>\n")) {
                    Toast.makeText(UpdateRestaurantsActivity.this, "The Email field must contain a valid email address.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Address field is required.<br>\n")) {
                    Toast.makeText(UpdateRestaurantsActivity.this, "The Address field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Food Type field is required.<br>\n")) {
                    Toast.makeText(UpdateRestaurantsActivity.this, "The Food Type field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("Restraunt cuisines are required.")) {
                    Toast.makeText(UpdateRestaurantsActivity.this, "Restraunt cuisines are required.", Toast.LENGTH_SHORT).show();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void focusOnView() {
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

    private void focusOnViewTerms() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anim = ObjectAnimator.ofInt(scrollView, "scrollY", checkboxterms.getBottom());
                anim.setDuration(1000);
                anim.start();
// scrollView.scrollTo(0, ln_priceDetails.getBottom());
            }
        });
    }
    public void callgetAllCities(){
        if (!CommonUtilities.isOnline(UpdateRestaurantsActivity.this)) {
            Toast.makeText(UpdateRestaurantsActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        final CustomProgressDialogue pd= new CustomProgressDialogue(UpdateRestaurantsActivity.this);
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
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateRestaurantsActivity.this));
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
        VolleySingleton.getInstance(UpdateRestaurantsActivity.this).addToRequestQueue(sr);
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

                cityAdapter = new ArrayAdapter(UpdateRestaurantsActivity.this,R.layout.spinner_display,R.id.txtSpin,cityList);
                spCityRes.setAdapter(cityAdapter);
                //cityAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                Log.e("GetIndex---",i+"");
                return i;
            }
        }
        return 0;
    }


}
