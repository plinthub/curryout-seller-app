package com.curryoutseller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.curryoutseller.model.CuisinesDataModel;

import java.util.ArrayList;

public class AdapterListC extends BaseAdapter {

    Context context_;
    ArrayList<CuisinesDataModel> alist;
    ArrayList<String> aName;
    private LayoutInflater mInflater;
    //SparseBooleanArray mSparseBooleanArray;

    public AdapterListC(Context context_, ArrayList<CuisinesDataModel> alist) {
        this.context_ = context_;
        this.alist = alist;
        //mSparseBooleanArray = new SparseBooleanArray();

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
//        Viewolder holder = null;
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.select_cuisines_listitem, null);
//            holder = new Viewolder();
//            ImageView imgBox = (ImageView)convertView.findViewById(R.id.chkBox);
//            holder.txtName = (TextView) convertView.findViewById(R.id.txt_cuisines);
//
//            convertView.setTag(holder);
//        } else {
//            holder = (Viewolder) convertView.getTag();
//        }
//        holder.txtName.setText(alist.get(position).toString());
//
//        final Viewolder finalHolder = holder;
//        final String s = null;
//        String fs=null;
//        holder.imgBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String st = finalHolder.txtName.getText().toString();
//                Toast.makeText(context_,st,Toast.LENGTH_LONG).show();
//
//            }
//        });

        final CuisinesDataModel cdm = alist.get(position);
        mInflater = (LayoutInflater) context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myv = mInflater.inflate(R.layout.select_cuisines_listitem,null);
        TextView txtName = (TextView) myv.findViewById(R.id.txt_cuisines);
        final CheckBox chkBox = myv.findViewById(R.id.chkBox);
//        final ImageView chkBox = (ImageView) myv.findViewById(R.id.chkBoxUnchek);
//        final ImageView chk = (ImageView) myv.findViewById(R.id.chkBoxChek);
        txtName.setText(cdm.getName());
        //final String[] st = {""};
        final ArrayList<String> st = new ArrayList<String>();
        chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                if(isChecked == true){
//                //st[0] +=cdm.getName();
//                st.add(cdm.getName());
//                String data="";
//                for(int i=0;i<st.size();i++){
//                    data+=st.get(i);
//                    Log.e("Data",data);
//                }
//                getData(st);

                int getPosition = (Integer) buttonView.getTag();
                alist.get(getPosition);
               // alist.


                Toast.makeText(context_,cdm.getName(),Toast.LENGTH_LONG).show();
                //Toast.makeText(context_,data,Toast.LENGTH_LONG).show();

            }
        });


//        chkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chkBox.setVisibility(View.GONE);
//                chk.setVisibility(View.VISIBLE);
//                Toast.makeText(context_,cdm.getName(),Toast.LENGTH_LONG).show();
//            }
//        });
        return myv;
    }


    class Viewolder {}

    public void getData(ArrayList<String> getN){
        aName = getN;
        Log.e("ListData",aName.toString());
    }

    public ArrayList<String> sendData(){
        Log.e("ListData1",aName.toString());
        return aName;
    }


}
