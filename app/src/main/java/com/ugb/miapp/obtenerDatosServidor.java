package com.ugb.miapp;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class obtenerDatosServidor extends AsyncTask<String, String, String> {
    HttpURLConnection httpURLConnection;
    @Override
    protected String doInBackground(String... voids) {
        StringBuilder result = new StringBuilder();
        try{
            //conexion con el servidor
            URL url = new URL(utilidades.url_consulta);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line= reader.readLine())!=null){
                result.append(line);
            }
        }catch (Exception e){
            //mensaje de error
        }finally {
            httpURLConnection.disconnect();
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
