package com.ugb.miapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class adaptadorImagenes extends BaseAdapter{
    Context context;
    ArrayList<amigos> datosAmigosArrayList;
    amigos misAmigos;
    LayoutInflater layoutInflater;

    public adaptadorImagenes(Context context, ArrayList<amigos> datosAmigosArrayList){
        this.context = context;
        this.datosAmigosArrayList = datosAmigosArrayList;
    }
    @Override
    public int getCount() {
        return datosAmigosArrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return datosAmigosArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_imagenes, viewGroup, false);
        try {
            misAmigos = datosAmigosArrayList.get(i);

            TextView tempVal = itemView.findViewById(R.id.lblTitulo);
            tempVal.setText(misAmigos.getNombre());

            tempVal = itemView.findViewById(R.id.lblTelefono);
            tempVal.setText(misAmigos.getTelefono());

            tempVal = itemView.findViewById(R.id.lblemail);
            tempVal.setText(misAmigos.getEmail());

            ImageView imgView = itemView.findViewById(R.id.imgFoto);
            Bitmap bitmap = BitmapFactory.decodeFile(misAmigos.getUrlImg());
            imgView.setImageBitmap(bitmap);
        }catch (Exception ex){
            Toast.makeText(context, "Error al mostrar la foto en el ListView: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}
