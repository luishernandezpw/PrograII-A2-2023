package com.ugb.miapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_amigos extends AppCompatActivity {
    Bundle parametros = new Bundle();
    BD db_agenda;
    ListView lts;
    Cursor cAMigos;
    FloatingActionButton btn;
    final ArrayList<amigos> alAmigos = new ArrayList<amigos>();
    final ArrayList<amigos> alAmigosCopy = new ArrayList<amigos>();
    amigos misAmigos;
    JSONArray datosJSON; //para los datos que vienen del servidor
    JSONObject jsonObject;
    ProgressDialog progreso; //para la barra de progreso...
    obtenerDatosServidor datosServidor;
    protected void onCreate(Bundle instance){
        super.onCreate(instance);
        setContentView(R.layout.lista_amigos);
        obtenerDatosAmigos();
        buscarAmigos();
        btn = findViewById(R.id.btnAgregarAmigos);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parametros.putString("accion", "nuevo");
                abrirAgregarAmigos(parametros);
            }
        });
    }
    public void abrirAgregarAmigos(Bundle parametros){
        Intent iAgregarAmigos = new Intent(lista_amigos.this, MainActivity.class);
        iAgregarAmigos.putExtras(parametros);
        startActivity(iAgregarAmigos);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        cAMigos.moveToPosition(info.position);
        menu.setHeaderTitle(cAMigos.getString(1)); //1=> Nombre del amigo...
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try{
            switch (item.getItemId()){
                case R.id.mnxAgregar:
                    parametros.putString("accion", "nuevo");
                    abrirAgregarAmigos(parametros);
                    return true;
                case R.id.mnxModificar:
                    String amigos[] = {
                        cAMigos.getString(0), //idAmigo
                        cAMigos.getString(0), //rev
                        cAMigos.getString(1), //nombre
                        cAMigos.getString(2), //direccion
                        cAMigos.getString(3), //telefono
                        cAMigos.getString(4), //email
                        cAMigos.getString(5), //foto->url
                    };
                    parametros.putString("accion", "modificar");
                    parametros.putStringArray("amigos", amigos);
                    abrirAgregarAmigos(parametros);
                    return true;
                case R.id.mnxEliminar:
                    eliminarDatosAmigos();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }catch (Exception e){
            return super.onContextItemSelected(item);
        }
    }
    void eliminarDatosAmigos(){
        try{
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(lista_amigos.this);
            confirmacion.setTitle("Esta seguro de eliminar a: ");
            confirmacion.setMessage(cAMigos.getString(1));
            confirmacion.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db_agenda.administrar_agenda(cAMigos.getString(0), "", "", "", "", "","eliminar");
                    obtenerDatosAmigos();
                    dialogInterface.dismiss();
                }
            });
            confirmacion.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            confirmacion.create().show();
        }catch (Exception e){
            Toast.makeText(this, "Error al eliminar: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void obtenerDatosAmigos(){
        try {

            db_agenda = new BD(lista_amigos.this, "", null, 1);
            cAMigos = db_agenda.consultar_agenda();
            if(cAMigos.moveToFirst()){
                /*final ArrayAdapter<String> adAmigos = new ArrayAdapter<String>(lista_amigos.this,
                        android.R.layout.simple_expandable_list_item_1, alAmigos);
                lts.setAdapter(adAmigos);*/
                datosJSON = new JSONArray();
                do{
                    //alAmigos.add(cAMigos.getString(1));//1 es el nombre del amigo, pues 0 es el idAmigo.
                    jsonObject = new JSONObject();
                    JSONObject jsonObjectValue = new JSONObject();

                    jsonObject.put("_id", cAMigos.getString(0));
                    jsonObject.put("_rev", cAMigos.getString(0));
                    jsonObject.put("nombre", cAMigos.getString(1));
                    jsonObject.put("direccion", cAMigos.getString(2));
                    jsonObject.put("telefono", cAMigos.getString(3));
                    jsonObject.put("email", cAMigos.getString(4));
                    jsonObject.put("urlFoto", cAMigos.getString(5));
                    jsonObjectValue.put("value", jsonObject);

                    datosJSON.put(jsonObjectValue);
                }while(cAMigos.moveToNext());
                mostrarDatosAmigos();
            }else{
                obtenerDatosAmigosServer();
                Toast.makeText(this, "Sincronizando con servidor", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "Error al obtener amigos: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    void obtenerDatosAmigosServer(){
        try {
            datosServidor = new obtenerDatosServidor();
            String data = datosServidor.execute().get();
            jsonObject = new JSONObject(data);
            datosJSON = jsonObject.getJSONArray("rows");
            mostrarDatosAmigos();
        }catch (Exception ex){
            Toast.makeText(this, "Error al obtener datos desde el servidor: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    void mostrarDatosAmigos(){
        try {
            if( datosJSON.length()>0 ){
                lts = findViewById(R.id.ltsAmigos);
                alAmigos.clear();
                alAmigosCopy.clear();

                JSONObject misDatosJSONObject;
                for (int i=0; i<datosJSON.length(); i++){
                    misDatosJSONObject = datosJSON.getJSONObject(i).getJSONObject("value");
                    misAmigos = new amigos(
                            misDatosJSONObject.getString("_id"),
                            misDatosJSONObject.getString("_rev"),
                            misDatosJSONObject.getString("nombre"),
                            misDatosJSONObject.getString("direccion"),
                            misDatosJSONObject.getString("telefono"),
                            misDatosJSONObject.getString("email"),
                            misDatosJSONObject.getString("urlFoto")
                    );
                    alAmigos.add(misAmigos);
                }
                adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alAmigos);
                lts.setAdapter(adImagenes);
                alAmigosCopy.addAll(alAmigos);
                //adAmigos.notifyDataSetChanged();
                registerForContextMenu(lts);
            }else{
                Toast.makeText(this, "NO hay datos que mostrar", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(this, "Error al mostrar datos amigos: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    void buscarAmigos(){
        TextView temp = findViewById(R.id.txtBuscarAmigos);
        temp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    alAmigos.clear();
                    String valor = temp.getText().toString().trim().toLowerCase();
                    if( valor.length()<=0 ){//es porque no esta escribiendo mostramos
                        // la lista completa de amigos
                        alAmigos.addAll(alAmigosCopy);
                    }else{ //si esta buscando amigos...
                        for(amigos amigo : alAmigosCopy){
                            String idAmigo = amigo.getIdAmigo();
                            String nombre = amigo.getNombre();
                            String direccion = amigo.getDireccion();
                            String telefono = amigo.getTelefono();
                            String email = amigo.getEmail();

                            if( nombre.toLowerCase().trim().contains(valor) ||
                                direccion.toLowerCase().trim().contains(valor) ||
                                telefono.toLowerCase().trim().contains(valor) ||
                                email.toLowerCase().trim().contains(valor)){
                                alAmigos.add(amigo);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alAmigos);
                        lts.setAdapter(adImagenes);
                    }
                }catch (Exception e){
                    Toast.makeText(lista_amigos.this, "Error al buscar amigos: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
