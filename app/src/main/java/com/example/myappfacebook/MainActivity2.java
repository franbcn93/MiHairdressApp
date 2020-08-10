package com.example.myappfacebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    TextView solucio, hora_act;
    private CalendarView calendari;
    private Bundle mBundle = new Bundle();
    private Button btn_calendari2;
    String dataReserva, correu;
    ProducteAdapter2 PA = new ProducteAdapter2();
    Integer preuProductes = 0;
    String nomProductes = "";
    Integer tempsProductes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //Li posem un titol i establim que podem tornat enrera
        getSupportActionBar().setTitle(getResources().getString(R.string.activityCalendari));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //variables
        hora_act=(TextView)findViewById(R.id.textHoraActual);
        solucio=(TextView)findViewById(R.id.textDate);
        //txtInf=(TextView)findViewById(R.id.textinformacio);
        calendari=(CalendarView)findViewById(R.id.calendarView);
        btn_calendari2=(Button)findViewById(R.id.btnReservaDay);
        btn_calendari2.setOnClickListener(this);

        //Data del dia d'avui
        Date currentTime = Calendar.getInstance().getTime();
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        hora_act.setText(getResources().getString(R.string.dataAvui) + currentDateTimeString);

        //Tenim que escollir el dia
        calendari.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view,int year,int month,int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                month = month +1;
                dataReserva = dayOfMonth + "/" + month  + "/" + year;
                solucio.setText(getResources().getString(R.string.dataMarc) + dataReserva);
                mBundle.putString("data",dayOfMonth + "/" + month  + "/" + year);
            }
        });

        //Informacio dels productes/correu
        ArrayList<String> productes = (ArrayList<String>) getIntent().getSerializableExtra("Extra");

        Intent intent = getIntent();
        correu = intent.getStringExtra("correu");

        for (int i = 0; i < productes.size(); i++) {
            if(productes.get(i).equals("0")){
                preuProductes+=Integer.valueOf(PA.preus[0]);
                nomProductes+= PA.productes[0] + " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[0]);
            }else if(productes.get(i).equals("1")){
                preuProductes+=Integer.valueOf(PA.preus[1]);
                nomProductes+= PA.productes[1]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[1]);
            }else if(productes.get(i).equals("2")){
                preuProductes+=Integer.valueOf(PA.preus[2]);
                nomProductes+= PA.productes[2]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[2]);
            }else if(productes.get(i).equals("3")){
                preuProductes+=Integer.valueOf(PA.preus[3]);
                nomProductes+= PA.productes[3]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[3]);
            }else if(productes.get(i).equals("4")){
                preuProductes+=Integer.valueOf(PA.preus[4]);
                nomProductes+= PA.productes[4]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[4]);
            }else if(productes.get(i).equals("5")){
                preuProductes+=Integer.valueOf(PA.preus[5]);
                nomProductes+= PA.productes[5]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[5]);
            }else if(productes.get(i).equals("6")){
                preuProductes+=Integer.valueOf(PA.preus[6]);
                nomProductes+= PA.productes[6]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[6]);
            }else if(productes.get(i).equals("7")){
                preuProductes+=Integer.valueOf(PA.preus[7]);
                nomProductes+= PA.productes[7]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[7]);
            }else if(productes.get(i).equals("8")){
                preuProductes+=Integer.valueOf(PA.preus[8]);
                nomProductes+= PA.productes[8]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[8]);
            }else if(productes.get(i).equals("9")){
                preuProductes+=Integer.valueOf(PA.preus[9]);
                nomProductes+= PA.productes[9]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[9]);
            }else if(productes.get(i).equals("10")){
                preuProductes+=Integer.valueOf(PA.preus[10]);
                nomProductes+= PA.productes[10]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[10]);
            }else if(productes.get(i).equals("11")){
                preuProductes+=Integer.valueOf(PA.preus[11]);
                nomProductes+= PA.productes[11]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[11]);
            }else if(productes.get(i).equals("12")){
                preuProductes+=Integer.valueOf(PA.preus[12]);
                nomProductes+= PA.productes[12]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[12]);
            }else if(productes.get(i).equals("13")){
                preuProductes+=Integer.valueOf(PA.preus[13]);
                nomProductes+= PA.productes[13]+ " ";
                tempsProductes+=Integer.valueOf(PA.tempsEmpleat[13]);
            }
        }
        //txtInf.setText( "Productes: " + nomProductes +". Preu: " + preuProductes +
                //". Temps: "+ tempsProductes + " " + correu);
        mBundle.putString("nomProductes", nomProductes);
        mBundle.putString("preuProductes",String.valueOf(preuProductes));
        mBundle.putString("tempsProductes",String.valueOf(tempsProductes));
        mBundle.putString("correu", correu);
        //Fi de l'informacio dels productes/correu
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), ProducteAdapter2.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnReservaDay:
                //Si no hem marcat encara el dia i es null...Sino tot correcte, pasa a la següent activitat
                if(dataReserva==null){
                    Toast toast=Toast.makeText(this, getResources().getString(R.string.error_dia), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
                    toast.show();
                } else{
                    Intent i = new Intent(this, RecyclerTime.class);
                    i.putExtras(mBundle);
                    startActivity(i);
                }
        }
    }
}