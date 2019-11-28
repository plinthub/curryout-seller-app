package com.curryoutseller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyProductsActivity extends AppCompatActivity {

    ImageView back_icon;
    TextView txt_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        init();
        listener();


        MyProductsDataModel[] myProductsDataModels= new MyProductsDataModel[] {

                new MyProductsDataModel("Happy Meal", "Fast Food", "INR 120", R.drawable.chicken),
                new MyProductsDataModel("Happy Meal", "Fast Food", "INR 120", R.drawable.chicken),
                new MyProductsDataModel("Happy Meal", "Fast Food", "INR 120", R.drawable.chicken),
                new MyProductsDataModel("Happy Meal", "Fast Food", "INR 120", R.drawable.chicken),
                new MyProductsDataModel("Happy Meal", "Fast Food", "INR 120", R.drawable.chicken),
                new MyProductsDataModel("Happy Meal", "Fast Food", "INR 120", R.drawable.chicken),
                new MyProductsDataModel("Happy Meal", "Fast Food", "INR 120", R.drawable.chicken),
                new MyProductsDataModel("Happy Meal", "Fast Food", "INR 120", R.drawable.chicken),
                new MyProductsDataModel("Happy Meal", "Fast Food", "INR 120", R.drawable.chicken),
                new MyProductsDataModel("Happy Meal", "Fast Food", "INR 120", R.drawable.chicken),
                new MyProductsDataModel("Happy Meal", "Fast Food", "INR 120", R.drawable.chicken),

        };

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyView);
        MyProductsListAdapter adapter = new MyProductsListAdapter(myProductsDataModels);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        back_icon=(ImageView)findViewById(R.id.back_icon);
        txt_add=(TextView)findViewById(R.id.txt_add);

    }

    private void listener() {
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProductsActivity.this,AddProductActivity.class));
            }
        });

    }

}
