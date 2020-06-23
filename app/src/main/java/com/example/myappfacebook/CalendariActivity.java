package com.example.myappfacebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendariActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView temps, solucio;
    private CalendarView calendari;
    private RecyclerView rvhorari;
    GridLayoutManager glm;
    private HoraAdapter adapter;
    Long date;
    private Button reserva;
    private Bundle mBundle = new Bundle();
    private Hora hora;
    ArrayList<Hora> data;
    String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendari);
        //Li posem un titol i establim que podem tornat enrera
        getSupportActionBar().setTitle("Escogeix cita:");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //variables
        temps=(TextView)findViewById(R.id.textTemps);
        solucio=(TextView)findViewById(R.id.textDate);
        calendari=(CalendarView)findViewById(R.id.calendarView);
        date = calendari.getDate();
        //Establim una data per defecte
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = sdf.format(new Date());
        solucio.setText("La data marcada es: "+ currentDateandTime);
        mBundle.putString("data", currentDateandTime);
        //Si escollim un altra...
        calendari.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view,int year,int month,int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                solucio.setText("La data marcada es: "+ dayOfMonth + "/" +(cal.get(Calendar.MONTH)+1)  + "/" + year);

                mBundle.putString("data",dayOfMonth + "/" +(cal.get(Calendar.MONTH)+1)  + "/" + year);
            }
        });
        //Creem un intent per obtenir dades de la activity anterior
        Intent intent = getIntent();
        String tempsEmpleat=intent.getStringExtra("temps");
        String preuProductes = intent.getStringExtra("preu");

        mBundle.putString("tempsEmpleat", tempsEmpleat);
        mBundle.putString("preuProductes", preuProductes);
        //Donem resposta i establim la variable temps
        temps.setText("El preu dels productes son "+ preuProductes + " € i el temps empleat son " +
                tempsEmpleat + " minuts." + " i l'hora escollida es: " + res);

        rvhorari = (RecyclerView) findViewById(R.id.rv_horari);
        glm = new GridLayoutManager(this, 3);
        rvhorari.setLayoutManager(glm);
        adapter = new HoraAdapter(dataSet());
        adapter.setOnEntryClickListener(new HoraAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view,int position) {
                //Log.d("REsultat nou ",String.valueOf(position));
            }
        });
        rvhorari.setAdapter(adapter);
        reserva=(Button)findViewById(R.id.btnReserva);
        reserva.setOnClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ProductesActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private ArrayList<Hora> dataSet() {
        int inici=9;
        int horesTreball =8;
        data = new ArrayList<>();
        for (int i = 0; i <horesTreball; i++) {
            //L'horari es de 9 a 13 i de 15 a 19
            if(inici==13){
                inici = 15;
            }
            for (int j = 0; j < 6; j++) {
                data.add(new Hora("Horari " + inici + ":" + j + "0", "Contractar"));
            }
            inici++;
        }
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnReserva:
                Intent i = new Intent(this, Reserva.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                i.putExtras(mBundle);
                startActivity(i);
        }

    }
    public void posicio(String position){
        res = String.valueOf(position);
        mBundle.putString("reserva1", res);
        Log.d("a veure",res);
    }
}