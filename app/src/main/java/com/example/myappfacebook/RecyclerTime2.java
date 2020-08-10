/*
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

public class RecyclerTime2 extends AppCompatActivity {
    private TextView textView;
    private CalendarView calendari;
    private RecyclerView rvhorari;
    GridLayoutManager glm;
    private HoraAdapter adapter;
    ArrayList<Hora> data;
    ArrayList<String> timers;
    Long date;
    private DatabaseReference mRootRef;
    private String correu;
    private ArrayList<String> dadesClient = new ArrayList<>();
    private ArrayList<String> consultaHorari = new ArrayList<>();
    String telefonClient;
    ArrayList<String> llistaDef;
    String diaReserva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_time);
        //Titol de l'activitat
        getSupportActionBar().setTitle("I ara l'hora que vols anar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Informació del activity anterior
        textView=(TextView)findViewById(R.id.text_inf_dia);
        Intent intent = getIntent();
        diaReserva=intent.getStringExtra("data");
        String nomProductes=intent.getStringExtra("nomProductes");
        String preuProductes=intent.getStringExtra("preuProductes");
        String tempsProductes=intent.getStringExtra("tempsProductes");
        String correu=intent.getStringExtra("correu");
        ArrayList <String> taulaProductes = new ArrayList<>();
taulaProductes.add(nomProductes);
        taulaProductes.add(preuProductes);
        taulaProductes.add(tempsProductes);
        taulaProductes.add(correu);


        taulaProductes.add("celles");
        taulaProductes.add("30");
        taulaProductes.add("45");
        taulaProductes.add("franciscoparisalbero@gmail.com");



        //textView.setText(getResources().getString(R.string.dataMarc) + diaReserva + " " + nomProductes + " " +
        //        preuProductes + " " + tempsProductes+ " " + correu);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        //dadesClientAct("fparisalbero@gmail.com");
        consultaHoresMarcades("3/7/2020", taulaProductes);
        //consultaHoresMarcades("3/7/2020", taulaProductes);


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


    public ArrayList<Hora> dataSet() {
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
                //data.add(new Hora(getResources().getString(R.string.horari) + inici + ":" + quarterHours[j]));
                data.add(new Hora(inici + ":" + quarterHours[j]));
            }
            inici++;
        }
        return data;
    }

    private void dadesClientAct(String email){
        Query query = mRootRef.child("Dades client").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    mRootRef.child("Dades client").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Client client=snapshot.getValue(Client.class);
                            String correu = client.getEmail();
                            String sexe = client.getSexe();
                            String telefon = client.getTelefon();
                            dadesClient.add(correu);
                            dadesClient.add(sexe);
                            dadesClient.add(telefon);
                            textView.setText("Dades del client: " + dadesClient + dadesClient.size());
                            //seguentActivity(dadesClient);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                dadesClient.removeAll(dadesClient);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void seguentActivity(ArrayList dades, ArrayList dadesProductes) {
        adapter = new HoraAdapter(this, dataSet(), "3/7/2020", dades, dadesProductes);
        rvhorari.setAdapter(adapter);
    }

    private ArrayList<String> stringstime() {
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
                //data.add(new Hora(getResources().getString(R.string.horari) + inici + ":" + quarterHours[j]));
                timers.add((inici + ":" + quarterHours[j]));
            }
            inici++;
        }
        return timers;
    }

    private ArrayList<String> horarisMarcats(ArrayList<String> horaris, ArrayList dadesProductes){
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
while (divisio>0){
                            llistaDef.add(timers.get(j+count));
                            count++;
                            divisio--;
                        }

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
        //31-84-171 lineas cambiadas

        //llistaDef.clear();
        textView.setText("Pasem els horaris reservats: "+llistaDef + " i el temps " + dadesProductes.get(2));
        return llistaDef;
    }


    private void consultaHoresMarcades(String data,final ArrayList dadesProductes){
        Query query = mRootRef.child("usuari").orderByChild("data").equalTo(data);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    mRootRef.child("usuari").child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Usuari usuari=snapshot.getValue(Usuari.class);
                            String horari = usuari.getHorari();
                            String temps = usuari.getTemps();
                            consultaHorari.add(horari);
                            consultaHorari.add(temps);
                            textView.setText("Dades del horari dels clients: " + consultaHorari + consultaHorari.size());
                            horarisMarcats(consultaHorari, dadesProductes);
                            seguentActivity(llistaDef, dadesProductes);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                consultaHorari.removeAll(consultaHorari);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

}
*/
