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

        tbh.addTab(tbh.newTabSpec("Longitud").setContent(R.id.tbhArea).setIndicator("AREA"));
        tbh.addTab(tbh.newTabSpec("Almacenamiento").setContent(R.id.tbhAguaPotable).setIndicator("POTABLE"));
        tbh.addTab(tbh.newTabSpec("Monedas").setContent(R.id.tbhCajero).setIndicator("CAJA"));

        btn = findViewById(R.id.btnConvertir);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int de = 0, a = 0;
                    temp = (TextView) findViewById(R.id.txtcantidad);
                    double cantidad = Double.parseDouble(temp.getText().toString());

                    int opcion = 0;
                    switch (tbh.getCurrentTab()) {
                        case 0://Area
                            spnDe = findViewById(R.id.spnDeArea);
                            spnA = findViewById(R.id.spnArea);
                            de = spnDe.getSelectedItemPosition();
                            a = spnA.getSelectedItemPosition();
                            temp = findViewById(R.id.lblrespuesta);
                            temp.setText("Respuesta: " + miConversor.convertir(0, de, a, cantidad));
                            break;
                        case 1://Potable

                            break;
                        case 2://Cajero

                            break;
                    }
                }catch (NumberFormatException e){
                    temp = findViewById(R.id.lblrespuesta);
                    temp.setText("Por favor ingrese una cantidad a convertir");
                }catch (Exception e){
                    temp = findViewById(R.id.lblrespuesta);
                    temp.setText("ERROR: "+ e.getMessage().toString());
                }
            }
        });
    }
}
class conversores{
    double[][] valores = {
            {1, 10.7639, 1.4308,1.19599,0.0015903307888, 0.0001431, 0.00009999828, 0.00024309781864560005}//AREA
    };
    public double convertir(int opcion, int de, int a, double cantidad){
        return valores[opcion][a] / valores[opcion][de] * cantidad;
    }
}