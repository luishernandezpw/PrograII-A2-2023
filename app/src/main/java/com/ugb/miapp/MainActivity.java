package com.ugb.miapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btnCalcular);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = (TextView) findViewById(R.id.txtNum1);
                Double num1 = Double.parseDouble(temp.getText().toString());

                temp = (TextView) findViewById(R.id.txtNum2);
                double num2 = Double.parseDouble(temp.getText().toString());

                Double resp = num1 + num2;

                Toast.makeText(MainActivity.this, "La suma es: "+ resp, Toast.LENGTH_LONG).show();
            }
        });
    }
}