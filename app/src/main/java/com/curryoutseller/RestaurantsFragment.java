package com.curryoutseller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class RestaurantsFragment extends Fragment {

    TextView txt_regYourRestaurant;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.restaurants_fragment, container, false);

        init(view);
        listener(view);

        return view;
    }

    private void init(View view) {
        txt_regYourRestaurant=(TextView)view.findViewById(R.id.txt_regYourRestaurant);

    }

    private void listener(View view) {

        txt_regYourRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppCompatActivity activity = (AppCompatActivity)getContext();
//                Fragment myFragment = new RegisterFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

                startActivity(new Intent(getActivity(),RegisterActivity.class));



            }
        });
    }
}
