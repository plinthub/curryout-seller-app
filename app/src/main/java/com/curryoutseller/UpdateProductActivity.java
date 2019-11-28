package com.curryoutseller;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateProductActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ImageView img_uncheck_veg,img_check_veg, img_uncheck_nonVeg, img_check_nonVeg, img_upload,cancel_icon;
    TextView txt_uploadImg, txt_addAnother,txt_chkVeg,txt_chkNon;
    LinearLayout ln_updateProduct;
    ArrayList<SpCategoryModel> aSpList;
    SpCategoryModel spCategoryModel;
    ArrayAdapter<SpCategoryModel> spAdapter;
    private Spinner spCategory, spSubCategory;
    ArrayList<SpCategoryModel> alistSubCate;
    SpCategoryModel subCat;
    ArrayAdapter<SpCategoryModel> subCateAdap;
    CheckBox checkboxpureveg,checkboxnonvegpro,checkboxAllDays;
    EditText ed_dishName,ed_ingredients,ed_price,ed_description,ed_Qty,ed_start,ed_end;
    String dishName="",spCategoryData="",foodtype="",ingredients="",price="",description="",qty="",start="",end="",daysName="",resPro="",upProId="",spSubCategoryData="";
    Bitmap gallery;
    ProductsSubCategory psc;
    AQuery aQuery;
    String ad="",spCat="",subCatData="",allDays="";
    RecyclerView recyclerView;
    DaysListAdapter adapter;
    ScrollView scrollView;
    ArrayList<DaysDataModel> daysDataModels;
    DaysDataModel daysModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        init();
        listener();

        aQuery = new AQuery(UpdateProductActivity.this);
        psc = (ProductsSubCategory) getIntent().getSerializableExtra("PROID");
        Log.e("UpProId",psc.getName());
        Log.e("UpProIdqq",psc.getProductID());
        spCat = psc.getCategory();
        Log.e("SpCat",psc.getCategory());
        callGetSellerCategory(spCat);
        setData();

        //resPro = getIntent().getStringExtra("ResIdPro");
        //Log.e("ResPro",resPro);

//        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
//        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCategory.setAdapter(spAdapter);



        daysDataModels= new ArrayList<DaysDataModel>();
        daysDataModels.add(new DaysDataModel("Monday"));
        daysDataModels.add(new DaysDataModel("Tuesday"));
        daysDataModels.add(new DaysDataModel("Wednesday"));
        daysDataModels.add(new DaysDataModel("Thursday"));
        daysDataModels.add(new DaysDataModel("Friday"));
        daysDataModels.add(new DaysDataModel("Saturday"));
        daysDataModels.add(new DaysDataModel("Sunday"));

        recyclerView = (RecyclerView) findViewById(R.id.recyView);
        adapter = new DaysListAdapter(daysDataModels);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    public void setData(){
        ed_dishName.setText(psc.getName());
        try{
        String food_type = psc.getFood_type();
        if(food_type.contains(",")) {
            String ft[] = food_type.split(",");
            for(int d = 0;d<ft.length;d++) {
                if(ft.length==0){}
                if(ft.length>0){
                Log.e("FT", ft[0]);
                if (ft[0].equals("Pure Veg")) {
                    checkboxpureveg.setChecked(true);
                }
                if (ft[0].equals("Non-Veg")) {
                    checkboxnonvegpro.setChecked(true);
                }
            }
            }
        }
        else{
            String food_type1 = psc.getFood_type();
            Log.e("UpdatePFT--",food_type1);
            if (food_type1.equals("Pure Veg")) {
                checkboxpureveg.setChecked(true);
            }
            if (food_type1.equals("Non-Veg")) {
                checkboxnonvegpro.setChecked(true);
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        ed_ingredients.setText(psc.getIngrediants());
        ed_price.setText(psc.getPrice());
        ed_description.setText(psc.getDescriptio());
        //aQuery.id(img_upload).image(psc.getImgView());
        if(psc.getImgView()!=null){
            img_upload.setVisibility(View.VISIBLE);
            Glide.with(UpdateProductActivity.this).asBitmap().load(psc.getImgView())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(img_upload);
        }
        ed_Qty.setText(psc.getQuantity());
        ed_start.setText(psc.getAvailable_start_time());
        ed_end.setText(psc.getAvailable_end_time());


        subCatData = psc.getSub_Category();
        Log.e("SubCat",psc.getSub_Category());
        Log.e("SubCatName",psc.getSub_category_name());
        try{
            ad = psc.getAvailable_days();
            Log.e("AD",ad);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void init() {
        ed_dishName = findViewById(R.id.ed_dishNameUp);
        checkboxpureveg = findViewById(R.id.checkboxpurevegUp);
        checkboxnonvegpro = findViewById(R.id.checkboxnonvegproUp);
        txt_chkVeg = findViewById(R.id.txt_chkVeg);
        txt_chkNon = findViewById(R.id.txt_chkNon);
        ed_ingredients = findViewById(R.id.ed_ingredientsUp);
        ed_price = findViewById(R.id.ed_priceUp);
        ed_description = findViewById(R.id.ed_descriptionUp);
        ed_Qty = findViewById(R.id.ed_QtyUp);
        ed_start = findViewById(R.id.ed_startUp);
        ed_end = findViewById(R.id.ed_endUp);

        img_check_veg=(ImageView)findViewById(R.id.img_check_veg);
        img_uncheck_veg=(ImageView)findViewById(R.id.img_uncheck_veg);
        img_check_nonVeg=(ImageView)findViewById(R.id.img_check_nonVeg);
        img_uncheck_nonVeg=(ImageView)findViewById(R.id.img_uncheck_nonVeg);
        img_upload=(ImageView)findViewById(R.id.img_uploadProduct);
        txt_uploadImg=(TextView)findViewById(R.id.txt_uploadImgUp);
        //txt_addAnother=(TextView)findViewById(R.id.txt_addAnother);
        ln_updateProduct = (LinearLayout)findViewById(R.id.ln_updateProduct);
        spCategory=(Spinner)findViewById(R.id.spCategoryUp);
        spSubCategory=(Spinner)findViewById(R.id.spSubCategoryUp);
        scrollView = findViewById(R.id.product_scrollUp);
        //checkboxAllDays = findViewById(R.id.checkboxAllDaysUp);
        aSpList = new ArrayList();
        daysModel = new DaysDataModel();
        cancel_icon = findViewById(R.id.cancel_icon);


    }

    private void listener() {

        cancel_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_upload.setImageDrawable(null);
                img_upload.setVisibility(View.GONE);
            }
        });

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    SpCategoryModel sp = aSpList.get(position);
                    spCategoryData = sp.getCuisineID();
                    Log.e("Category Data", spCategoryData);
                    callgetAllSubCategory(sp.getCuisineID());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpCategoryModel sp = alistSubCate.get(position);
                spSubCategoryData = sp.getCuisineID();
                Log.e("Category Data",spSubCategoryData);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        checkboxpureveg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    foodtype += txt_chkVeg.getText().toString();
                    Log.e("FTV",foodtype);
                }
                else{
                    foodtype="";
                    Log.e("FTV",foodtype);
                }
            }
        });

        checkboxnonvegpro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    foodtype += txt_chkNon.getText().toString();
                    Log.e("FTN",foodtype);
                }
                else{
                    foodtype="";
                    Log.e("FTN",foodtype);
                }
            }
        });

        ed_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(UpdateProductActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ed_start.setText( "" + selectedHour + ":" + selectedMinute);
                        ed_start.setError(null);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        ed_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(UpdateProductActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ed_end.setText( "" + selectedHour + ":" + selectedMinute);
                        ed_end.setError(null);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

//        checkboxAllDays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if(checkboxAllDays.isChecked()==true){
//
//                    if(daysModel.getDaysName()!=null){
//                        String s = daysModel.getDaysName();
//                        Log.e("AllDaysInner",s);
//                    }
//                    if(daysModel.getSelected()==true && daysName!=null){
//                        DaysDataModel dm = daysModel.getPosition();
//                        String testD = dm.getDaysName();
//                        Toast.makeText(UpdateProductActivity.this, testD, Toast.LENGTH_SHORT).show();
//                        dm.setSelected(false);
//
//                        //Toast.makeText(UpdateProductActivity.this, testD, Toast.LENGTH_SHORT).show();
//                        //daysDataModels= new ArrayList<DaysDataModel>();
//                        daysName="";
//                        //adapter.selectAll();
//                        //DaysListAdapter adap = new DaysListAdapter();
//
//                    }
//
//                    //Toast.makeText(AddProductActivity.this, "Checked", Toast.LENGTH_SHORT).show();
//
//                }
//                else if(checkboxAllDays.isChecked()==false){
//                     adapter.unSelectAll();
//                    //Toast.makeText(AddProductActivity.this, "Not Checked", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        ln_updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dishName = ed_dishName.getText().toString();
                ingredients = ed_ingredients.getText().toString();
                price = ed_price.getText().toString();
                description = ed_description.getText().toString();
                qty = ed_Qty.getText().toString();
                start = ed_start.getText().toString();
                end = ed_end.getText().toString();
                gallery = ((BitmapDrawable) img_upload.getDrawable()).getBitmap();

//                if(dishName.isEmpty()) {
////                    ed_dishName.setError("Please Enter  Name");
////                }else if(ingredients.isEmpty()){
////                    ed_ingredients.setError("Please Enter Email");
////                }else if(price.isEmpty()){
////                    ed_price.setError("Please Enter Phone Number");
////                }else if(description.isEmpty()){
////                    ed_description.setError("Please Enter Address");
////                }else if(qty.isEmpty()){
////                    ed_Qty.setError("Please Enter Address");
////                }else if(start.isEmpty()){
////                    ed_start.setError("Please Enter Address");
////                }else if(end.isEmpty()){
////                    ed_end.setError("Please Enter Address");
                //}
                if (dishName.isEmpty()) {
                    ed_dishName.setError("Please Enter Dish Name");
                }
                else if(foodtype.isEmpty())
                {
                    focusOnView();
                    Toast.makeText(UpdateProductActivity.this,"Please Select Food Type",Toast.LENGTH_LONG).show();
                }

                else if(ingredients.isEmpty()){
                    ed_ingredients.setError("Please Enter Ingredients");
                }else if(price.isEmpty()){
                    ed_price.setError("Please Enter Price");
                }else if(description.isEmpty()){
                    ed_description.setError("Please Enter Description");
                }else if(gallery==null){
                    Toast.makeText(UpdateProductActivity.this,"Please Select Image",Toast.LENGTH_LONG).show();
                }else if(qty.isEmpty()){
                    ed_Qty.setError("Please Enter Quantity");
                }else if(start.isEmpty()){
                    ed_start.setError("Please Enter Start Time");
                }else if(end.isEmpty()){
                    ed_end.setError("Please Enter End Time");
                }
                else if(daysName.isEmpty()){
                    Toast.makeText(UpdateProductActivity.this,"Please Select Atleast a Single Day",Toast.LENGTH_LONG).show();
                }
                else if(img_upload.isShown()){
                    Log.e("Data:--",dishName+" : "+spCategoryData+" : "+spSubCategoryData+" : "+foodtype+" : "+ingredients+" : "+price+" : "+description+" : "+qty+" : "+start+" : "+end+" : "+daysName);
                callAddProductJSON(dishName,spCategoryData,spSubCategoryData,foodtype,ingredients,price,description,qty,start,end,daysName);
                //startActivity(new Intent(AddProductActivity.this, MyProductsActivity.class));
            }
            else{}
            }
        });

        txt_uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    public class DaysDataModel {
        private String daysName;
        private boolean isSelected;
        private DaysDataModel position;

        public DaysDataModel() {
        }
        public DaysDataModel(String daysName) {
            this.daysName = daysName;
        }

        public String getDaysName() {
            return daysName;
        }

        public void setDaysName(String daysName) {
            this.daysName = daysName;
        }

        public boolean getSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public DaysDataModel getPosition() {
            return position;
        }

        public void setPosition(DaysDataModel position) {
            this.position = position;
        }
    }

    public class DaysListAdapter extends RecyclerView.Adapter<DaysListAdapter.ViewHolder> {

        private ArrayList<DaysDataModel> listData;
        boolean isSelectedAll;

        public DaysListAdapter(ArrayList<DaysDataModel> listData) {
            this.listData = listData;
        }

        @NonNull
        @Override
        public DaysListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.product_days_list_layout, parent, false);
            DaysListAdapter.ViewHolder viewHolder = new DaysListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final DaysListAdapter.ViewHolder holder, int position) {
            try {
                final DaysDataModel daysListData = listData.get(position);
                holder.txtDaysName.setText(listData.get(position).getDaysName());
                holder.checkBox_dayName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            daysName += daysListData.getDaysName() + ",";
                            Log.e("DaysName", daysName);

                        }
                    }
                });

                if (!isSelectedAll){
                    holder.checkBox_dayName.setChecked(false);
                }
                else{  holder.checkBox_dayName.setChecked(true);}


                Log.e("Position",position+"");
                Log.e("ListDataPosi",daysListData.getDaysName());
                if (ad.contains(",")) {
                    String ft[] = ad.split(",");

                    //Log.e("FTOut", ft[0]);
                    //Log.e("FTOut", ft[1]);
                    Log.e("LengthAd", ft.length + "");
                    for (int x = 0; x < ft.length; x++) {
                        Log.e("FT=" + x, ft[x]);
                        Log.e("Condition", ""+(ft[x].equals("Monday") || ft[x].equals("Tuesday") || ft[x].equals("Wednesday") || ft[x].equals("Thursday") || ft[x].equals("Friday") || ft[x].equals("Saturday") || ft[x].equals("Sunday")));
                        if (ft[x].equals("Monday") || ft[x].equals("Tuesday") || ft[x].equals("Wednesday") || ft[x].equals("Thursday") || ft[x].equals("Friday") || ft[x].equals("Saturday") || ft[x].equals("Sunday")) {
                            if(listData.get(position).getDaysName().contains(ft[x])) {
                                holder.checkBox_dayName.setChecked(true);
                                Log.e("Inner","ggg");

                                if(holder.checkBox_dayName.isChecked()==true){
                                    listData.get(position).setSelected(true);
                                    String d = daysListData.getDaysName();
                                    daysModel.setDaysName(d);
                                    Log.e("checkBoxDayName",d);
//                                    DaysDataModel pos = listData.get(position);
//                                    daysModel.setPosition(pos);
//                                    daysModel.setSelected(true);
//                                    daysListData.setDaysName(daysName);
//                                    Log.e("TestPosition",listData.get(position).getSelected()+"");
                                }
                            }

//                            ArrayList<DaysDataModel> daysDataModels= new ArrayList<DaysDataModel>();
//                            boolean r = daysDataModels.add(new DaysDataModel(ft[x]));
//                            holder.checkBox_dayName.setSelected(r);


                        }}}

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            //            public ImageView img_checked, img_Unchecked;
            public CheckBox checkBox_dayName;
            public TextView txtDaysName;

            public ViewHolder(View itemView) {
                super(itemView);
                this.txtDaysName = (TextView) itemView.findViewById(R.id.txt_dayName);
//                img_checked=(ImageView)itemView.findViewById(R.id.img_chek);
//                img_Unchecked=(ImageView)itemView.findViewById(R.id.img_uncheck);
                this.checkBox_dayName = itemView.findViewById(R.id.checkbox_dayName);

                try{

            }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        public void selectAll(){
            Log.e("onClickSelectAll","yes");
            isSelectedAll=true;
            notifyDataSetChanged();
        }

        public void unSelectAll(){
            isSelectedAll=false;
            notifyDataSetChanged();
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(UpdateProductActivity.this);
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
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
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
        //getImg = destination.getPath();
        Log.e("Thumbnail", thumbnail.toString());
        Log.e("Destination", destination.toString());
    }


    public class SpCategoryModel {

        private String cuisineID;
        private String name;

        public SpCategoryModel(String cuisineID) {
            this.cuisineID = cuisineID;
        }

        public SpCategoryModel(String cuisineID, String name) {
            this.cuisineID = cuisineID;
            this.name = name;
        }

        public String getCuisineID() {
            return cuisineID;
        }

        public void setCuisineID(String cuisineID) {
            this.cuisineID = cuisineID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        @Override
        public String toString() {
            return name;
        }
    }

    public void callGetSellerCategory(final String category) {
        if (!CommonUtilities.isOnline(UpdateProductActivity.this)) {
            Toast.makeText(UpdateProductActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(UpdateProductActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(UpdateProductActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        //Log.e("Name in Call", fullname);
        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "common/getAllCuisines";
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                parseResponse(response,category);
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

                        parseResponse(response.toString(),category);

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
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateProductActivity.this));
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
        VolleySingleton.getInstance(UpdateProductActivity.this).addToRequestQueue(sr);
    }

    public void parseResponse(String response,String category) {
        Log.e("CO Seller Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);

            boolean data = (boolean)job.get("status");
            if(data==true) {
                JSONArray jarr = job.getJSONArray("data");
                spCategoryModel = new SpCategoryModel("", "Select Category");
                String cuisineID = "", name = "";

                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jobj = jarr.getJSONObject(i);
                    cuisineID = jobj.getString("cuisineID");
                    name = jobj.getString("name");
                if (category.equals(cuisineID)) {
                    SpCategoryModel spCategoryModel = new SpCategoryModel(cuisineID, name);
                    Log.e("CIDff", cuisineID);
                    Log.e("NameCusff", name);
                    aSpList.add(spCategoryModel);
                    spAdapter = new ArrayAdapter(UpdateProductActivity.this, R.layout.spinner_display, R.id.txtSpin, aSpList);
                    spCategory.setAdapter(spAdapter);
                    //spAdapter.notifyDataSetChanged();
                }

            }

                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jobj = jarr.getJSONObject(i);
                    cuisineID = jobj.getString("cuisineID");
                    name = jobj.getString("name");

                    String status = jobj.getString("status");
                    Log.e("Test1", cuisineID.contains(category) + "");
                    Log.e("Test2", cuisineID.equals(category) + "");
                    Log.e("Test3", category.contains(cuisineID) + "");
                    Log.e("Test4", category.equals(cuisineID) + "");
                    Log.e("Cat", category);

                    spCategoryModel = new SpCategoryModel(cuisineID, name);
                    Log.e("CID", cuisineID);
                    Log.e("NameCus", name);
                    aSpList.add(spCategoryModel);
                }


                spAdapter = new ArrayAdapter(UpdateProductActivity.this, R.layout.spinner_display, R.id.txtSpin, aSpList);
                spCategory.setAdapter(spAdapter);
                spAdapter.notifyDataSetChanged();
            }
                //spAdapter= new ArrayAdapter<SpCategoryModel>(AddProductActivity.this, android.R.layout.simple_spinner_item, aSpList);
//                spAdapter = new ArrayAdapter(UpdateProductActivity.this, R.layout.spinner_display, R.id.txtSpin, aSpList);
//                spCategory.setAdapter(spAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String convertBase64(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            return encoded;
        }catch (Exception e){
            e.printStackTrace();;
        }
        return null;
    }

    public void callAddProductJSON(final String dishName, final String spCategoryData, final String subData,final String foodtype, final String ingredients, final String price, final String description, final String qty,final String start,final String end,final String daysName){
        if (!CommonUtilities.isOnline(UpdateProductActivity.this)) {
            Toast.makeText(UpdateProductActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(UpdateProductActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(UpdateProductActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "product/addProduct";
        String testurl = "";

        JSONObject map = new JSONObject();
        String mRequestBody = "";
        try {
            map.put("productId", psc.getProductID());
            map.put("restrauntId", psc.getRestaurant_id());
            map.put("name", dishName);
            map.put("category", spCategoryData);
            map.put("sub_category", subData);
            map.put("food_type", foodtype);
            map.put("price", price);
            map.put("description", description);
            map.put("quantity_available", qty);

            map.put("ingrediants", ingredients);


            String arr[] = daysName.split(",");
            JSONArray care_type = new JSONArray();
            for(int i=0; i < arr.length; i++) {
                care_type.put(arr[i]);   // create array and add items into that
            }

            map.put("available_days", care_type); //array
            map.put("available_start_time", start);
            map.put("available_end_time", end);
            map.put("image", convertBase64(gallery));



//            int x = strArrlst1.size();
//            Log.e("X",x+"");
//            int y = getCuisineList.size();
//            Log.e("Y",y+"");
//            CuisinesDataModel cdm = new CuisinesDataModel();
//            JSONArray jarr = new JSONArray();
//            JSONObject ob=null;
//
//            for(int i=0;i<getCuisineList.size();i++){
//                cdm = getCuisineList.get(i);
//                ob = new JSONObject();
//                ob.put("cuisine_id",cdm.getCuisineID());
//                //ob.put("cuisine_id","9");
//                Log.e("cuisine_id",cdm.getCuisineID());
//                jarr.put(ob);
//                map.put("cuisines",jarr);
//            }
//
//            JSONArray items = jarr;
//            Log.e("ITEMSArray",items+"");
//
//            Log.e("Map",jarr+"");
            mRequestBody = map.toString();

            Log.e("Product Data",psc.getRestaurant_id()+psc.getProductID()+
                    dishName + spCategoryData + subData +foodtype + ingredients + price + description + start
                            + end + daysName);

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
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateProductActivity.this));
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
        VolleySingleton.getInstance(UpdateProductActivity.this).addToRequestQueue(jsonObjReq);
    }

    public void parseResponseAdd(String response){
        Log.e("CO Update Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");
            String message = job.getString("message");
            if (data == true) {
                if (message.equalsIgnoreCase("Product Updated Successfully")) {
                    Toast.makeText(UpdateProductActivity.this, "Product Updated Successfully.", Toast.LENGTH_SHORT).show();
                    //Intent in = new Intent(UpdateProductActivity.this,RestaurantsListFragment.class);
                    //startActivity(in);
                    finish();
                }
            }
            if(data==false){
                if (message.equalsIgnoreCase("The Restraunt Name field is required.<br>\n")) {
                    //Toast.makeText(RegisterActivity.this, "The Restraunt Name field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The City field is required.<br>\n")) {
                    //Toast.makeText(RegisterActivity.this, "The City field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Owner Name field is required.<br>\n")) {
                    //Toast.makeText(RegisterActivity.this, "The Owner Name field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Phone field is required.<br>\n")) {
                    //Toast.makeText(RegisterActivity.this, "The Phone field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Email field is required.<br>\n")) {
                    //Toast.makeText(RegisterActivity.this, "The Email field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Address field is required.<br>\n")) {
                    //Toast.makeText(RegisterActivity.this, "The Address field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("The Food Type field is required.<br>\n")) {
                    //Toast.makeText(RegisterActivity.this, "The Food Type field is required.", Toast.LENGTH_SHORT).show();
                }
                if (message.equalsIgnoreCase("Restraunt cuisines are required.")) {
                    //Toast.makeText(RegisterActivity.this, "Restraunt cuisines are required.", Toast.LENGTH_SHORT).show();
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void callgetAllSubCategory(final String cuisineId){
        if (!CommonUtilities.isOnline(UpdateProductActivity.this)) {
            Toast.makeText(UpdateProductActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

//        final ProgressDialog pd = new ProgressDialog(UpdateProductActivity.this);
//        pd.setTitle("Please Wait");
//        pd.setCancelable(false);
//        pd.show();

        final CustomProgressDialogue pd= new CustomProgressDialogue(UpdateProductActivity.this);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.show();

        String url = AppGlobalConstants.WEBSERVICE_BASE_URL + "common/getAllSubCategory";
        String testurl = "";
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pd.dismiss();
                parseResponseSub(response);
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
                        parseResponseSub(response.toString());

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
                map.put("cuisineId",cuisineId);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/x-www-form-urlencoded");
                header.put("X-Auth-Token", AppSharedPreference.loadTokenPreference(UpdateProductActivity.this));
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
        VolleySingleton.getInstance(UpdateProductActivity.this).addToRequestQueue(sr);
    }

    public void parseResponseSub(String response){
        Log.e("CO AddProduct Response", "response " + response);
        try {
            JSONObject job = new JSONObject(response);
            boolean data = (boolean) job.get("status");

            if (data == true) {
                JSONArray arr = job.getJSONArray("data");
                Log.e("ArrLen",arr.length()+"");
                if(arr.length()==0){

                   String s =job.getString("message");
                    Log.e("Mgs",s);
                   if(s.equals("Data Not Found.")){
                       Log.e("MgsEquals",s);
                       subCat= new SpCategoryModel("", "Select Sub-Category");
                       alistSubCate = new ArrayList<>();
                       alistSubCate.add(subCat);
                       subCateAdap = new ArrayAdapter(UpdateProductActivity.this,R.layout.spinner_display,R.id.txtSpin,alistSubCate);
                       spSubCategory.setAdapter(subCateAdap);
                       subCateAdap.notifyDataSetChanged();
                   }
                }else{
                for(int i=0;i<arr.length();i++){
                    JSONObject obj = arr.getJSONObject(i);
                    String cuisineID = obj.getString("cuisineID");
                    String cuisine_name = obj.getString("cuisine_name");
                    String parent_id = obj.getString("parent_id");
                    String status = obj.getString("status");
                    String category_name = obj.getString("category_name");
                    Log.e("CategoryName",category_name);


                        subCat = new SpCategoryModel(cuisineID, cuisine_name);

                    Log.e("CID", cuisineID);
                    Log.e("NameCus", cuisine_name);
                    alistSubCate = new ArrayList<>();
                    alistSubCate.add(subCat);

                }
                    subCateAdap = new ArrayAdapter(UpdateProductActivity.this,R.layout.spinner_display,R.id.txtSpin,alistSubCate);
                    spSubCategory.setAdapter(subCateAdap);
                    subCateAdap.notifyDataSetChanged();
                }

            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void focusOnView(){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anim = ObjectAnimator.ofInt(scrollView, "scrollY", txt_chkVeg.getBottom());
                anim.setDuration(1000);
                anim.start();
// scrollView.scrollTo(0, ln_priceDetails.getBottom());
            }
        });
    }

}
