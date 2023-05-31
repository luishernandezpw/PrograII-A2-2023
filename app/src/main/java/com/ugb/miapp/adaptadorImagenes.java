package com.ugb.miapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class adaptadorImagenes extends BaseAdapter {
    Context context;
    ArrayList<usuarios> datos;
    LayoutInflater layoutInflater;
    usuarios user;

    public adaptadorImagenes(Context context, ArrayList<usuarios> datos) {
        this.context = context;
        this.datos = datos;
    }

    @Override
    public int getCount() {
        return datos.size();
    }
    @Override
    public Object getItem(int position) {
        return datos.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;//Long.parseLong( datosAmigosArrayList.get(position).getIdAmigo() );
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
        TextView tempVal = itemView.findViewById(R.id.lblTitulo);
        ImageView imgView = itemView.findViewById(R.id.imgPhoto);
        try{
            user = datos.get(position);
            tempVal.setText(user.getUserName());
            //Glide.with(context).load(user.getUrlPhotoFirestore()).into(imgView);
        }catch (Exception e){
            tempVal.setText(e.getMessage());
        }
        return itemView;
    }
}
