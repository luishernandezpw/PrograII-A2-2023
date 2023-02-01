package com.ugb.miapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView temp;
    RadioGroup rgp;
    Spinner spn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btnCalcular);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = (TextView) findViewById(R.id.txtNum1);
                double num1 = Double.parseDouble(temp.getText().toString());

                temp = (TextView) findViewById(R.id.txtNum2);
                double num2 = Double.parseDouble(temp.getText().toString());

                double resp = 0;
                String msg = "Operacion invalida";
                /*rgp = (RadioGroup)findViewById(R.id.rgpOpciones);
                switch (rgp.getCheckedRadioButtonId()){
                    case R.id.optSuma:
                        resp = num1 + num2;
                        msg = "La suma es: "+ resp;
                        break;
                    case R.id.optResta:
                        resp = num1 - num2;
                        msg = "La resta es: "+ resp;
                        break;
                    case R.id.optMultiplicacion:
                        resp = num1 * num2;
                        msg = "La multiplicacion es: "+ resp;
                        break;
                    case R.id.optDivision:
                        resp = num1 / num2;
                        msg = "La division es: "+ resp;
                        break;
                    case R.id.optExponente:
                        resp = Math.pow(num1, num2);
                        msg = "Exponente: "+ resp;
                        break;
                }*/
                spn = (Spinner)findViewById(R.id.spnOpciones);
                switch (spn.getSelectedItemPosition()){
                    case 0: //Suma
                        resp = num1 + num2;
                        msg = "La suma es: "+ resp;
                        break;
                    case 1://Resta
                        resp = num1 - num2;
                        msg = "La resta es: "+ resp;
                        break;
                    case 2://Multiplicacion
                        resp = num1 * num2;
                        msg = "La multiplicacion es: "+ resp;
                        break;
                    case 3://division
                        resp = num1 / num2;
                        msg = "La division es: "+ resp;
                        break;
                    case 4://exponente
                        resp = Math.pow(num1,num2);
                        msg = "La exponenciacion es: "+ resp;
                        break;
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}