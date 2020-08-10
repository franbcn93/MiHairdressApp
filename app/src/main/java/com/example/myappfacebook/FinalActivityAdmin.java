package com.example.myappfacebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FinalActivityAdmin extends AppCompatActivity {

    TextView txtFinAdmin;
    private DatabaseReference mRootRef;
    private Admin admin;
    private String resultat_dia, result_horari;
    ArrayList<String> mevesPosicions;
    String citesReservades = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_admin);
        txtFinAdmin=(TextView)findViewById(R.id.textEndAdmin);
        getSupportActionBar().setTitle("Reserves guardades correctament");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        Intent intent=getIntent();
        resultat_dia = intent.getStringExtra("resultat_dia");

        mevesPosicions = (ArrayList<String>) getIntent().getSerializableExtra("resultat_horaris");
        //L'ultim element no el guardem, ja que es a les 19:00 i es acceptar
        for (int i = 0; i < mevesPosicions.size()-1; i++) {
            SaveBBDDAdmin(resultat_dia, String.valueOf(mevesPosicions.get(i)));
            citesReservades += (String.valueOf(mevesPosicions.get(i) + " "));
        }
        txtFinAdmin.setText("Les teves cites reservades en el dia " + resultat_dia + " son: "
                + citesReservades + ". Gracies");
    }

    private void SaveBBDDAdmin(String data, String horari) {
        Map<String, Object> dadesAdmin = new HashMap<>();

        admin = new Admin(data, horari);
        admin.setData(data);
        admin.setHorari(horari);

        dadesAdmin.put("data", admin.getData());
        dadesAdmin.put("horari", admin.getHorari());

        mRootRef.child("admin").push().setValue(dadesAdmin);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), Administrador.class);
        startActivity(intent);
        return true;
    }
}