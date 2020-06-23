package com.example.myappfacebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class EmptyExample extends AppCompatActivity {
    TextView resultat;
    Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_example);
        String res = getIntent().getStringExtra("novaDada");
        String res2 = getIntent().getStringExtra("tempsEmpleat");
        resultat=(TextView)findViewById(R.id.textResult);
        resultat.setText(res+ " , ");


    }
}