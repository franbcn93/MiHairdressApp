package com.example.myappfacebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerTime extends AppCompatActivity {
    //private TextView textView;
    private RecyclerView rvhorari;
    GridLayoutManager glm;
    private HoraAdapter adapter;
    ArrayList<Hora> data;
    ArrayList<String> timers;
    DatabaseReference mRootRef;
    private ArrayList<String> consultaHorari = new ArrayList<>();
    ArrayList<String> llistaDef;
    String diaReserva;
    ArrayList <String> taulaProductes = new ArrayList<>();
    UsuariRerva usuariRerva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_time);
        //Titol de l'activitat
        getSupportActionBar().setTitle("I ara l'hora que vols reservar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Informació del activity anterior
        //textView=(TextView)findViewById(R.id.text_inf_dia);
        Intent intent = getIntent();
        diaReserva=intent.getStringExtra("data");
        String nomProductes=intent.getStringExtra("nomProductes");
        String preuProductes=intent.getStringExtra("preuProductes");
        String tempsProductes=intent.getStringExtra("tempsProductes");
        String correu=intent.getStringExtra("correu");

        taulaProductes.add(nomProductes);
        taulaProductes.add(preuProductes);
        taulaProductes.add(tempsProductes);
        taulaProductes.add(correu);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        //Fa la consulta amb el dia.Si n'hi han reserves les marca, tant si les ha marcat un client com si ha sigut l'administador
        consulta(diaReserva);
        consultaAdmin(diaReserva);
        // recyclerview
        rvhorari = (RecyclerView) findViewById(R.id.rv_horari);
        glm = new GridLayoutManager(this, 1);
        rvhorari.setLayoutManager(glm);
    }

    //En teoria debería ser MainActivity2, peropara que no pete..
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
        return true;
    }


    private ArrayList<Hora> dataSet() {
        int inici=9;
        int horesTreball =8;
        String[] quarterHours = {"00","15","30","45"};
        data = new ArrayList<>();
        for (int i = 0; i <horesTreball; i++) {
            //L'horari es de 9 a 13 i de 15 a 19
            if(inici==13){
                data.add(new Hora(getResources().getString(R.string.descans)));
                inici = 15;
            }
            for (int j = 0; j < 4; j++) {
                data.add(new Hora(inici + ":" + quarterHours[j]));
            }
            inici++;
        }
        return data;
    }

    public void seguentActivity(ArrayList dades) {
        adapter = new HoraAdapter(this, dataSet(), diaReserva, dades, taulaProductes);
        rvhorari.setAdapter(adapter);
    }

    public ArrayList<String> stringstime() {
        int inici=9;
        int horesTreball =8;
        String[] quarterHours = {"00","15","30","45"};
        timers = new ArrayList<>();
        for (int i = 0; i <horesTreball; i++) {
            //L'horari es de 9 a 13 i de 15 a 19
            if(inici==13){
                timers.add((getResources().getString(R.string.descans)));
                inici = 15;
            }
            for (int j = 0; j < 4; j++) {
                timers.add((inici + ":" + quarterHours[j]));
            }
            inici++;
        }
        return timers;
    }

    public ArrayList<String> horarisMarcats(ArrayList<String> horaris){
        int quartsHora = 15;
        llistaDef = new ArrayList<>();
        stringstime();

        for (int i = 0; i < horaris.size(); i++) {
            llistaDef.add(horaris.get(i));
            i++;
            if(Integer.parseInt(horaris.get(i))>quartsHora){
                int divisio=0;
                int count =1;
                divisio = Integer.parseInt(horaris.get(i))/quartsHora;
                for (int j = 0; j < timers.size(); j++) {
                    if((horaris.get(i-1)).equals(timers.get(j))){
                        if(divisio==2){
                            llistaDef.add(timers.get(j+1));
                        }if(divisio==3){
                            llistaDef.add(timers.get(j+1));
                            llistaDef.add(timers.get(j+2));
                        }if(divisio==4){
                            llistaDef.add(timers.get(j+1));
                            llistaDef.add(timers.get(j+2));
                            llistaDef.add(timers.get(j+3));
                        }if(divisio==5){
                            llistaDef.add(timers.get(j+1));
                            llistaDef.add(timers.get(j+2));
                            llistaDef.add(timers.get(j+3));
                            llistaDef.add(timers.get(j+4));
                        }
                    }
                }
            }
        }
        //textView.setText("Pasem els horaris reservats: "+llistaDef);
        return llistaDef;
    }


    protected void consulta(String consulta_ET) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("usuari").orderByChild("data").equalTo(consulta_ET);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mRootRef.child("usuari").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                usuariRerva=snapshot.getValue(UsuariRerva.class);
                                String horari = usuariRerva.getHorari();
                                String temps = usuariRerva.getTemps();
                                consultaHorari.add(horari);
                                consultaHorari.add(temps);
                                //textView.setText("Dades del horari dels clients: " + consultaHorari + consultaHorari.size());
                                horarisMarcats(consultaHorari);
                                //textView.setText("Dades del horari dels clients: " + consultaHorari + consultaHorari.size() +" " + llistaDef.size() +" " + llistaDef.get(0));
                                //seguentActivity(llistaDef);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
                else {
                    //textView.setText("No hi han reserves encara");
                    horarisMarcats(consultaHorari);
                    //seguentActivity(llistaDef);
                }
                //consultaHorari.removeAll(consultaHorari);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    protected void consultaAdmin(String consulta_ET) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("admin").orderByChild("data").equalTo(consulta_ET);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mRootRef.child("admin").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                usuariRerva=snapshot.getValue(UsuariRerva.class);
                                String horari = usuariRerva.getHorari();
                                //String temps = usuariRerva.getTemps();
                                consultaHorari.add(horari);
                                consultaHorari.add("15");
                                //textView.setText("Dades del horari dels clients: " + consultaHorari + consultaHorari.size());
                                horarisMarcats(consultaHorari);
                                //textView.setText("Dades del horari dels clients: " + consultaHorari + consultaHorari.size() +" " + llistaDef.size() +" " + llistaDef.get(0));
                                seguentActivity(llistaDef);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
                else {
                    //textView.setText("No hi han reserves encara");
                    horarisMarcats(consultaHorari);
                    seguentActivity(llistaDef);
                }
                consultaHorari.removeAll(consultaHorari);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}