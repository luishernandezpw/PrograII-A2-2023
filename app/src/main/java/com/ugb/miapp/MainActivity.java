package com.ugb.miapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TabHost tbh;
    Button btn;
    TextView temp;
    Spinner spnDe, spnA;
    conversores miConversor = new conversores();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbh = findViewById(R.id.tbhConversores);
        tbh.setup();

        tbh.addTab(tbh.newTabSpec("Longitud").setContent(R.id.tbhLongitud).setIndicator("", getDrawable(R.drawable.longitud)));
        tbh.addTab(tbh.newTabSpec("Almacenamiento").setContent(R.id.tbhAlmcenamiento).setIndicator("ALMACENAMIENTO"));
        tbh.addTab(tbh.newTabSpec("Monedas").setContent(R.id.tbhMonedas).setIndicator("MONEDAS"));

        btn = findViewById(R.id.btnConvertir);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    int de = 0, a = 0;
                    temp = (TextView) findViewById(R.id.txtcantidad);
                    double cantidad = Double.parseDouble(temp.getText().toString());

                    int opcion = 0;
                    switch (tbh.getCurrentTab()) {
                        case R.id.tbhLongitud:
                            spnDe = findViewById(R.id.spnDeLongitud);
                            spnA = findViewById(R.id.spnALongitud);
                            opcion = 1;
                            break;
                        case R.id.tbhAlmcenamiento:
                            opcion = 3;
                            break;
                        case R.id.tbhMonedas:
                            spnDe = findViewById(R.id.spnDeMonedas);
                            spnA = findViewById(R.id.spnAMonedas);
                            opcion = 0;
                            break;
                    }
                    de = spnDe.getSelectedItemPosition();
                    a = spnA.getSelectedItemPosition();
                    temp = findViewById(R.id.lblrespuesta);
                    temp.setText("Respuesta: " + miConversor.convertir(opcion, de, a, cantidad));
            }
        });
    }
}
class conversores{
    double[][] valores = {
            {1, 7.84, 24.63, 36.51, 581.78, 8.75, 0.93, 130.54, 82.52, 0.82},//monedas
            {1, 100, 3.28084, 39.37008, 1.1963081929167, 1.093613, 0.001, 0.000621},//Longitud
            {},//Peso
            {1, 8388608, 1048576,1024, 0.0009765625, 0.000000095367431640625},//Almacenamiento
    };
    public double convertir(int opcion, int de, int a, double cantidad){
        return valores[opcion][a] / valores[opcion][de] * cantidad;
    }
}