package com.example.myappfacebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Reserva extends AppCompatActivity implements View.OnClickListener {

    private TextView reservat,reserveClients, textConsulta;
    DatabaseReference mRootRef;
    Button pujarDades, reserves, btnConsulta;
    String data, preu, temps;
    ArrayList<String> dadesClients = new ArrayList<>();
    ArrayList<String> citesClients = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText consultaDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);
        getSupportActionBar().setTitle("Pas final reserva:");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Mostrar reserves
        reserveClients=(TextView)findViewById(R.id.textTotesReserves);
        reserves=(Button) findViewById(R.id.btn_Reserves);
        reserves.setOnClickListener(this);
        //Guardar dades
        mRootRef = FirebaseDatabase.getInstance().getReference();
        reservat=(TextView)findViewById(R.id.textReservat);
        pujarDades=(Button)findViewById(R.id.btnDades);
        pujarDades.setOnClickListener(this);
        //Consulta dia
        textConsulta=(TextView)findViewById(R.id.textConsultaDia);
        consultaDia=(EditText)findViewById(R.id.editTextTextConsultaDia);
        btnConsulta=(Button)findViewById(R.id.bntConsultaDia);
        btnConsulta.setOnClickListener(this);



    }

    private void solicitudDades() {
        mRootRef.child("usuari").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    mRootRef.child("usuari").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Users users=snapshot.getValue(Users.class);
                            data = users.getData();
                            preu = users.getPreu();
                            temps = users.getTemps();
                            //Log.e("Data: ",data);
                            dadesClients.add(data);
                            dadesClients.add(preu);
                            dadesClients.add(temps);
                            reserveClients.setText("Data: "+ dadesClients);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private  void  consulta(String consulta_ET){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        Query query = databaseReference.child("usuari").orderByChild("data").equalTo(consulta_ET);//"18/6/2020"
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    mRootRef.child("usuari").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Users users=snapshot.getValue(Users.class);
                            data = users.getData();
                            dadesClients.add(data);
                            textConsulta.setText("Dates reserva: "+ dadesClients);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                dadesClients.removeAll(dadesClients);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    private void dadesUsuari(String dataReserva,String preuProductes,String tempsEmpleat) {
        Map<String, Object> dadesUsuari = new HashMap<>();
        dadesUsuari.put("data", dataReserva);
        dadesUsuari.put("preu",preuProductes);
        dadesUsuari.put("temps",tempsEmpleat);

        mRootRef.child("usuari").push().setValue(dadesUsuari);
    }

    /*public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), CalendariActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDades:
                Intent intent = getIntent();
                final String dataReserva=intent.getStringExtra("data");
                final String horaReserva= intent.getStringExtra("reserva1");
                final String tempsEmpleat=intent.getStringExtra("tempsEmpleat");
                final String preuProductes = intent.getStringExtra("preuProductes");
                String hora = intent.getStringExtra("resultat");
                //String preuProductes = intent.getStringExtra("preu");
                reservat.setText("DADES GUARDADES: La data de la reserva es: "+ dataReserva+", l'hora reservada es: " + horaReserva +
                        " el temps empleat es: " + tempsEmpleat + " minuts, i el preu total son: " + preuProductes + " €.");
                dadesUsuari(dataReserva,preuProductes,tempsEmpleat);

                break;
            case R.id.btn_Reserves:
                solicitudDades();
                //reserveClients.setText("Data: "+ dadesClients);
                //consulta();
                break;
            case R.id.bntConsultaDia:
                String consulta_ET = consultaDia.getText().toString();
                consulta(consulta_ET);

                break;
        }
    }
}