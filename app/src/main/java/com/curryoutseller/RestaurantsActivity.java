package com.curryoutseller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class RestaurantsActivity extends AppCompatActivity {

    TextView txt_regYourRestaurant;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants_fragment);
        init();
        listener();
    }

    public void init(){
        txt_regYourRestaurant=(TextView)findViewById(R.id.txt_regYourRestaurant);
    }

    public void listener(){

        txt_regYourRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RestaurantsActivity.this,RegisterActivity.class));



            }
        });
    }
}

