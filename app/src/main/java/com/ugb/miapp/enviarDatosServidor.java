package com.ugb.miapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class enviarDatosServidor extends AsyncTask<String, String, String> {
    Context context;
    String resp;
    HttpURLConnection urlConnection;

    public enviarDatosServidor(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        String jsonResponse = null;
        String jsonDatos = strings[0];
        BufferedReader bufferedReader;

        try{
            URL url = new URL(utilidades.url_mto);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            Writer write = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            write.write(jsonDatos);
            write.close();

            InputStream inputStream = urlConnection.getInputStream();
            if(inputStream==null){
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            resp = bufferedReader.toString();

            String line;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line= bufferedReader.readLine())!=null ){
                stringBuffer.append(line+"\n");
            }
            if(stringBuffer.length()==0){
                return null;
            }
            jsonResponse = stringBuffer.toString();
            return jsonResponse;
        }catch (Exception ex){
            Log.d("ENVIANDO", "Error al emviar datos al server: "+ ex.getLocalizedMessage());
            //Toast.makeText(context, "Error al enviar Datos al servidor: "+ ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
