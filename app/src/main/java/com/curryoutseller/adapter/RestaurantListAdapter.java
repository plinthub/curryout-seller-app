package com.curryoutseller.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.curryoutseller.MyProductFragment;
import com.curryoutseller.R;

import com.curryoutseller.RestaurantsListActivity;
import com.curryoutseller.RestaurantsListFragment;
import com.curryoutseller.model.RestaurantDataModel;

import java.util.ArrayList;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private ArrayList<RestaurantDataModel> listdata;
    RestaurantDataModel restauListData;
    Context context;
    public RestaurantListAdapter(ArrayList<RestaurantDataModel> listdata,Context context) {
        this.listdata = listdata;
        this.context = context;
    }



    @NonNull
    @Override
    public RestaurantListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.restaurant_list_item_layout, parent, false);
        RestaurantListAdapter.ViewHolder viewHolder = new RestaurantListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListAdapter.ViewHolder holder, int position) {
        restauListData = listdata.get(position);
        holder.txtRestName.setText(restauListData.getTxtRestaurantName());
        holder.txtRestAddress.setText(restauListData.getTxtRestaurantAddress());


        //holder.imageRes = restauListData.getImgRestaurantBG();

        Glide.with(context).load(Uri.parse(restauListData.getImgRestaurantBG()))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);

        final String rid = restauListData.getRestrauntID();
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                Fragment myFragment = new MyProductFragment();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView txtRestName;
        public TextView txtRestAddress;

        public ViewHolder(View itemView) {
            super(itemView);

            this.imageView = (ImageView) itemView.findViewById(R.id.img_Restaurant);
            this.txtRestName = (TextView) itemView.findViewById(R.id.txt_RestaurantsName);
            this.txtRestAddress = (TextView) itemView.findViewById(R.id.txt_Restaurantaddress);
        }
    }
}
