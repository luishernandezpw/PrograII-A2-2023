package com.ugb.miapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class lista_usuarios extends Activity {
    ListView ltsUsuarios;
    DatabaseReference databaseReference;
    JSONArray datosJSONArray = new JSONArray();
    JSONObject datosJSONObject;
    String miToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);
        ltsUsuarios = findViewById(R.id.ltsUsuarios);
        mostrarListadoUsuarios();
        mostrarChats();
    }
    void mostrarListadoUsuarios(){
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if( !task.isSuccessful() ){
                return;
            }
            miToken = task.getResult();
            if( miToken!=null && miToken.length()>0 ) {
                databaseReference.orderByChild("token").equalTo(miToken).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if (snapshot.getChildrenCount() <= 0) {
                                mostrarMsgToast("No hay Usuario registrados...");
                                registrarUsuarios();
                            }
                        } catch (Exception ex) {
                            mostrarMsgToast(ex.getMessage());
                            registrarUsuarios();
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                mostrarMsgToast("No pupde obtener el identificar de tu telefono, intentalo mas tarde.");
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    ArrayList<usuarios> stringArrayList =new ArrayList<usuarios>();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        usuarios user = dataSnapshot.getValue(usuarios.class);
                        stringArrayList.add(user);

                        datosJSONObject = new JSONObject();
                        datosJSONObject.put("user", user.getUserName());
                        datosJSONObject.put("to", user.getToken());
                        datosJSONObject.put("from", miToken);
                        datosJSONObject.put("urlPhoto", user.getUrlPhoto());
                        datosJSONObject.put("urlPhotoFirestore", user.getUrlPhoto());
                        datosJSONArray.put(datosJSONObject);
                    }
                    adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), stringArrayList);
                    ltsUsuarios.setAdapter(adaptadorImagenes);
                }catch (Exception e){
                    mostrarMsgToast(e.getMessage());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    void mostrarChats(){
        ltsUsuarios.setOnItemClickListener((parent, view, position, id) -> {
            try{
                Bundle bundle = new Bundle();
                bundle.putString("user", datosJSONArray.getJSONObject(position).getString("user") );
                bundle.putString("to", datosJSONArray.getJSONObject(position).getString("to") );
                bundle.putString("from", datosJSONArray.getJSONObject(position).getString("from") );
                bundle.putString("urlPhoto", datosJSONArray.getJSONObject(position).getString("urlPhoto") );
                bundle.putString("urlPhotoFirestore", datosJSONArray.getJSONObject(position).getString("urlPhotoFirestore") );

                Intent intent = new Intent(getApplicationContext(), chats.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }catch (Exception ex){
                mostrarMsgToast(ex.getMessage());
            }
        });
    }
    void registrarUsuarios(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    private void mostrarMsgToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
