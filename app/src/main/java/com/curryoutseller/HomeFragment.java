package com.curryoutseller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class HomeFragment extends Fragment {

    LinearLayout ln1,ln2,ln3,ln4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_fragment, container, false);

        init(view);
        listerner(view);

        return view;
    }

    private void init(View view) {

        ln1=(LinearLayout)view.findViewById(R.id.ln1);
        ln2=(LinearLayout)view.findViewById(R.id.ln2);
        ln3=(LinearLayout)view.findViewById(R.id.ln3);
        ln4=(LinearLayout)view.findViewById(R.id.ln4);
    }

    private void listerner(View view) {

         ln1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 MyOrdersFragment myOrdersFragment= new MyOrdersFragment();
                 getActivity().getSupportFragmentManager().beginTransaction()
                         .replace(R.id.fragment_container, myOrdersFragment, "findThisFragment")
                         .addToBackStack(null)
                         .commit();
             }
         });


         ln2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 MyOrdersFragment myOrdersFragment= new MyOrdersFragment();
                 getActivity().getSupportFragmentManager().beginTransaction()
                         .replace(R.id.fragment_container, myOrdersFragment, "findThisFragment")
                         .addToBackStack(null)
                         .commit();
             }
         });

        ln3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrdersFragment myOrdersFragment= new MyOrdersFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, myOrdersFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        ln4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrdersFragment myOrdersFragment= new MyOrdersFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, myOrdersFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
