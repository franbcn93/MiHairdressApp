package com.example.myappfacebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Administrador extends AppCompatActivity {
    private CalendarView CalAdmin;
    private TextView textAdmin;
    private DatabaseReference mRootRef;
    private String diaReserva;
    private RecyclerView rv_time;
    private GridLayoutManager glm;
    private HoraAdapter_Admin adapter;
    private ArrayList<Hora> day;
    private Bundle mBundle = new Bundle();
    private ArrayList<String> consultaHorari = new ArrayList<>();
    private ArrayList<String> llistaDef;
    private ArrayList<String> timers;
    private UsuariRerva usuariRerva;
    //private RecyclerTime2 RTime;
    private static final String TAG = "Error: " ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);
        CalAdmin = (CalendarView)findViewById(R.id.calendarAdmin);
        textAdmin = (TextView)findViewById(R.id.txt_inf_admin);

        getSupportActionBar().setTitle("Marca les teves reserves");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        CalAdmin.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view,int year,int month,int dayOfMonth) {
                month = month +1;
                diaReserva = dayOfMonth + "/" + month  + "/" + year;
                textAdmin.setText(getResources().getString(R.string.dataMarc) + diaReserva);
                mBundle.putString("data",diaReserva);
                consultaAdmin(diaReserva);
                consulta(diaReserva);
            }
        });
        rv_time = (RecyclerView) findViewById(R.id.rv_horari);
        glm = new GridLayoutManager(this, 1);
        rv_time.setLayoutManager(glm);
    }

    private ArrayList<Hora> dataSet2() {
        int inici=9;
        int horesTreball =8;
        String[] quarterHours = {"00","15","30","45"};
        day = new ArrayList<>();
        for (int i = 0; i <horesTreball; i++) {
            //L'horari es de 9 a 13 i de 15 a 19
            if(inici==13){
                day.add(new Hora("DESCANS"));
                inici = 15;
            }
            for (int j = 0; j < 4; j++) {
                day.add(new Hora( + inici + ":" + quarterHours[j] + " P.M."));
            }
            inici++;
        }
        //Afegim el botó per acceptar les hores marcades pel administrador
        day.add(new Hora("ACCEPTAR LES TEVES RESERVES"));
        return day;
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
                                horarisMarcats(consultaHorari);
                                //textAdmin.setText("Dades del horari dels clients: " + consultaHorari + consultaHorari.size() +" " + llistaDef.size() +" " + llistaDef.get(0));
                                seguentActivity(llistaDef);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
                else {
                    //textAdmin.setText("No hi han reserves encara3");
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
                                //textAdmin.setText("Dades del horari dels clients: " + consultaHorari + consultaHorari.size() +" " + llistaDef.size() +" " + llistaDef.get(0));
                                //seguentActivity(llistaDef);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
                else {
                    //textAdmin.setText("No hi han reserves encara2");
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

    public ArrayList<String> horarisMarcats(ArrayList<String> horaris){
        int quartsHora = 15;
        llistaDef = new ArrayList<>();
        stringstime();

        for (int i = 0; i < horaris.size(); i++) {
            llistaDef.add(horaris.get(i));
            i++;
            if(Integer.parseInt(horaris.get(i))>quartsHora){
                int divisio=0;
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
        return llistaDef;
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

    public void seguentActivity(ArrayList dades) {
        adapter = new HoraAdapter_Admin(this, dataSet2(), diaReserva, dades);
        rv_time.setAdapter(adapter);
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        return true;
    }
}