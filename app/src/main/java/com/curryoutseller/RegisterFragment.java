package com.curryoutseller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.curryoutseller.appsharedpreference.AppGlobalConstants;
import com.curryoutseller.appsharedpreference.AppSharedPreference;
import com.curryoutseller.appsharedpreference.CommonUtilities;
import com.curryoutseller.model.CuisinesDataModel;

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

public class RegisterFragment  extends Fragment {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    TextView txt_uploadImg, txt_cuisines;
    EditText etRestaurantName, etCity, etOwnerName, etEmail, et_phNumber,et_address1,et_address2;
    ImageView img_upload, back_icon, img_chek_terms,img_unchek_terms,img_chek_delivery,img_unchek_delivery, imgNumberClean,imgAddress1Clean,imgAddress2Clean;
    String restau,city,owner,email,phnumber,address, foodtype,cuisines,image;
    String add="+91",added="";
    LinearLayout ln_save;
    ArrayList<CuisinesDataModel> alist;
    String strArrlst;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.register_fragment, container, false);

        final ArrayList<String> strArrlst = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            strArrlst.add("Number: " + i);
        }

        init(view);
        listener(view);


        return view;

    }

    private void init(View view) {

        etRestaurantName=(EditText)view.findViewById(R.id.etRestaurantName);
        etCity=(EditText)view.findViewById(R.id.etCity);
        etOwnerName=(EditText)view.findViewById(R.id.etOwnerName);
        etEmail=(EditText)view.findViewById(R.id.etEmail);
        et_phNumber=(EditText)view.findViewById(R.id.et_phNumber);
        et_address1=(EditText)view.findViewById(R.id.et_address1);
        et_address2=(EditText)view.findViewById(R.id.et_address2);
        txt_uploadImg=(TextView)view.findViewById(R.id.txt_uploadImg);
        txt_cuisines=(TextView)view.findViewById(R.id.txt_cuisines);
        back_icon=(ImageView)view.findViewById(R.id.back_icon);
        img_upload=(ImageView)view.findViewById(R.id.img_upload);
        img_chek_terms=(ImageView)view.findViewById(R.id.img_chek_terms);
        img_unchek_terms=(ImageView)view.findViewById(R.id.img_unchek_terms);
        img_chek_delivery=(ImageView)view.findViewById(R.id.img_chek_delivery);
        img_unchek_delivery=(ImageView)view.findViewById(R.id.img_unchek_delivery);
        imgNumberClean=(ImageView)view.findViewById(R.id.imgNumberClean);
        imgAddress1Clean=(ImageView)view.findViewById(R.id.imgAddress1Clean);
        imgAddress2Clean=(ImageView)view.findViewById(R.id.imgAddress2Clean);

        ln_save=(LinearLayout)view.findViewById(R.id.ln_save);
    }

    private void listener(final View view) {


        txt_cuisines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                final Dialog dialog = new Dialog(context, R.style.FullScreenDialogStyle);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.select_cuisines_layout);
                dialog.show();

                ListView dialogLIst = (ListView) dialog.findViewById(R.id.listView);


//                final AdapterListC adapListC = new AdapterListC(context, strArrlst);
//                dialogLIst.setAdapter(adapListC);
//
//                final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice, strArrlst);
//                alertdialog_Listview.setAdapter(adapter);

                ImageView cancel_icon=(ImageView)dialog.findViewById(R.id.cancel_icon);

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
            }catch (Exception e){
                    e.printStackTrace();
                }
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
                phnumber = et_phNumber.getText().toString();
                added = add+phnumber;
            }
        });


        ln_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                restau = etRestaurantName.getText().toString();
                city = etCity.getText().toString();
                owner = etOwnerName.getText().toString();
                email = etEmail.getText().toString();
                phnumber=et_phNumber.getText().toString();
                address=et_address1.getText().toString();





                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new RestaurantsListFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

//                    callRegRestaurant(restau,city,owner,email,phnumber,address);

            }
        });

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)getContext();
                activity.getSupportFragmentManager().popBackStack();

            }
        });


        et_phNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() > 0){
                    imgNumberClean.setVisibility(View.VISIBLE);
                }else{
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
                if(charSequence.length() > 0){
                    imgAddress1Clean.setVisibility(View.VISIBLE);
                }else{
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
                if(charSequence.length() > 0){
                    imgAddress2Clean.setVisibility(View.VISIBLE);
                }else{
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


        img_unchek_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_unchek_delivery.setVisibility(View.GONE);
                img_chek_delivery.setVisibility(View.VISIBLE);
            }
        });

        img_chek_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_unchek_delivery.setVisibility(View.VISIBLE);
                img_chek_delivery.setVisibility(View.GONE);
            }
        });

        img_unchek_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_unchek_terms.setVisibility(View.GONE);
                img_chek_terms.setVisibility(View.VISIBLE);
            }
        });

        img_chek_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_unchek_terms.setVisibility(View.VISIBLE);
                img_chek_terms.setVisibility(View.GONE);
            }
        });


    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(getActivity());
                String userChoosenTask;
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
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
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
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
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
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
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        img_upload.setImageBitmap(bm);
        img_upload.setVisibility(View.VISIBLE);
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
    }

    public void callRegRestaurant(final String restau, final String city, final String owner, final String email, final String phnumber, final String address) {

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
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "auth/addRestraunt";
        String testurl = "";

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
                map.put("name", restau);
                map.put("city", city);
                map.put("owner_name", owner);
                map.put("phone", phnumber);
                map.put("email", email);
                map.put("address", address);
                map.put("food_type", foodtype);
                map.put("cuisines", cuisines);
                map.put("image", image);
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);
    }

    public void parseResponse(String response) {
        Log.e("CO Restaurant Response", "response " + response);
        try{
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message = job.getString("message");
            if (data == true) {
                if(message.equals("Restaurant Added Successfully")){
                    Toast.makeText(getActivity(), "Restaurant Added Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } }else {
                Toast.makeText(getActivity(), "Please Enter Proper Data", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
