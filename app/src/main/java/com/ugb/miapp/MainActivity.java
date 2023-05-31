package com.ugb.miapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {
    ImageView imgPhoto;
    Intent takePhotoIntent;
    String urlCompleteImg;
    String urlCompleteImgFirestore;
    TextView tempVal;
    Button btnGuardar;
    DatabaseReference databaseReference;
    String miToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            obtenerToken();
            tomarFoto();
        }catch (Exception e){
            mostrarMsgToast(e.getMessage());
        }
        btnGuardar = findViewById(R.id.btnGuardarUsuario);
        btnGuardar.setOnClickListener(v -> {
            uploadPhotoFirestore();
        });
    }
    void uploadPhotoFirestore(){
        mostrarMsgToast("Subiendo la foto te confirmaremos cuando este listo");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        Uri file = Uri.fromFile(new File(urlCompleteImg));
        final StorageReference reference = storageReference.child("photos/"+file.getLastPathSegment());

        final UploadTask uploadTask = reference.putFile(file);
        uploadTask.addOnFailureListener(e -> {
            mostrarMsgToast("Fallo el subir la foto al servidor: "+ e.getMessage());
        });
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            mostrarMsgToast("Listo la foto se subio correctamente al servidor");
            Task<Uri> downloadUri = uploadTask.continueWithTask(task -> reference.getDownloadUrl()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    urlCompleteImgFirestore = task.getResult().toString();
                    guardarUsuario();
                } else{
                    mostrarMsgToast("La foto se subio con exito pero no pude obtener su enlace");
                }
            });
        });
    }
    private void guardarUsuario(){
        try{
            databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
            tempVal = findViewById(R.id.txtNombreUsuario);
            String nombre = tempVal.getText().toString(),
                    key = databaseReference.push().getKey();
            if( miToken=="" || miToken==null ){
                obtenerToken();
            }
            if( miToken!=null && miToken!="" ){
                usuarios user = new usuarios(nombre, "luishernandez@ugb.edu.sv", urlCompleteImg, urlCompleteImgFirestore, miToken);
                if(key!=null){
                    databaseReference.child(key).setValue(user).addOnSuccessListener(aVoid -> {
                        mostrarMsgToast("Usuario registrado con exito");
                        mostrarListaUsuarios();
                    });
                } else {
                    mostrarMsgToast("NO se inserto el usuario en la base de datos de firebase");
                }
            } else{
                mostrarMsgToast("NO pude obtener el identificar de tu telefono, por favor intentalo mas tarde.");
            }
        }catch (Exception ex){
            mostrarMsgToast(ex.getMessage());
        }
    }
    void tomarFoto() {
        imgPhoto = findViewById(R.id.imgPhoto);
        imgPhoto.setOnClickListener(v->{
            takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
                //guardando la imagen
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (Exception ex) {
                }
                if (photoFile != null) {
                    try {
                        Uri photoURI = FileProvider.getUriForFile(MainActivity.this, "com.ugb.miapp.fileprovider", photoFile);
                        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePhotoIntent, 1);
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "Error Toma Foto: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                Bitmap imageBitmap = BitmapFactory.decodeFile(urlCompleteImg);
                imgPhoto.setImageBitmap(imageBitmap);
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "imagen_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if( storageDir.exists()==false ){
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        urlCompleteImg = image.getAbsolutePath();
        return image;
    }
    private void mostrarMsgToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void obtenerToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if( !task.isSuccessful() ){
                return;
            }
            miToken = task.getResult();
        });
    }
    void mostrarListaUsuarios(){
        Intent intent = new Intent(getApplicationContext(), lista_usuarios.class);
        startActivity(intent);
    }
}
