package com.example.myappfacebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class ProductesActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView producteText;
    private TextView producteText1, producteText2, producteText3, producteText4, producteText5,
            producteText6, producteText7, producteText8, producteText9, producteText10, producteText11,
            producteText12,producteText13,producteText14;
    private TextView totalText, totalTemps;
    private  DatabaseReference mDatabase;
    private String depilacio;
    private  Integer preuTotal, tempsTotal;
    private CheckBox chB1,chB2,chB3,chB4,chB5,chB6,chB7,chB8,chB9,chB10,chB11,chB12,chB13,chB14;
    private Integer pA=28, pC=10, pDep= 32, pEx=40, pM=14, pMsg=32,pPF=14, pPC=22, pP=33, pPes=10,
            pTC=24, pTYC=30, pTP=24,pU=14;
    private Integer tA=50, tC=10, tDep= 40, tEx=70, tM=20, tMsg=60,tPF=20, tPC=30, tP=50, tPes=10,
            tTC=30, tTYC=70, tTP=30,tU=20;
    private Button btnBack, btn_contractar;
    private ProgressDialog progressDialog;

    public String getDepilacio() {
        return depilacio;
    }

    public void setDepilacio(String depilacio) {
        this.depilacio = depilacio;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productes);
        getSupportActionBar().setTitle("Taula de productes:");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        totalText = (TextView)findViewById(R.id.textTotal);
        totalTemps = (TextView)findViewById(R.id.textTempsTotal);
        progressDialog=new ProgressDialog(this);
        preuTotal = 0;
        tempsTotal = 0;

        producteText1 = (TextView)findViewById(R.id.textProducte1);
        producteText2 = (TextView)findViewById(R.id.textProducte2);
        producteText3 = (TextView)findViewById(R.id.textProducte3);
        producteText4 = (TextView)findViewById(R.id.textProducte4);
        producteText5 = (TextView)findViewById(R.id.textProducte5);
        producteText6 = (TextView)findViewById(R.id.textProducte6);
        producteText7 = (TextView)findViewById(R.id.textProducte7);
        producteText8 = (TextView)findViewById(R.id.textProducte8);
        producteText9 = (TextView)findViewById(R.id.textProducte9);
        producteText10 = (TextView)findViewById(R.id.textProducte10);
        producteText11 = (TextView)findViewById(R.id.textProducte11);
        producteText12 = (TextView)findViewById(R.id.textProducte12);
        producteText13 = (TextView)findViewById(R.id.textProducte13);
        producteText14 = (TextView)findViewById(R.id.textProducte14);


        chB1 = (CheckBox)findViewById(R.id.chB1);
        chB1.setOnClickListener(this);
        chB2 = (CheckBox)findViewById(R.id.chB2);
        chB2.setOnClickListener(this);
        chB3 = (CheckBox)findViewById(R.id.chB3);
        chB3.setOnClickListener(this);
        chB4 = (CheckBox)findViewById(R.id.chB4);
        chB4.setOnClickListener(this);
        chB5 = (CheckBox)findViewById(R.id.chB5);
        chB5.setOnClickListener(this);
        chB6 = (CheckBox)findViewById(R.id.chB6);
        chB6.setOnClickListener(this);
        chB7 = (CheckBox)findViewById(R.id.chB7);
        chB7.setOnClickListener(this);
        chB8 = (CheckBox)findViewById(R.id.chB8);
        chB8.setOnClickListener(this);
        chB9 = (CheckBox)findViewById(R.id.chB9);
        chB9.setOnClickListener(this);
        chB10 = (CheckBox)findViewById(R.id.chB10);
        chB10.setOnClickListener(this);
        chB11 = (CheckBox)findViewById(R.id.chB11);
        chB11.setOnClickListener(this);
        chB12 = (CheckBox)findViewById(R.id.chB12);
        chB12.setOnClickListener(this);
        chB13 = (CheckBox)findViewById(R.id.chB13);
        chB13.setOnClickListener(this);
        chB14 = (CheckBox)findViewById(R.id.chB14);
        chB14.setOnClickListener(this);
        //Botó que serveix per contrartar el servei marcat
        btn_contractar = (Button)findViewById(R.id.bnt_ok);
        btn_contractar.setOnClickListener(this);

        //totalText.setText("El preu total dels productes escollits son: " + preuTotal + " €.");
        //ProgressDialog per esperar mentre es connecta amb la BBDD
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Connectant");
        progress.setMessage("Espera mentres es connecta amb la base de dades...");
        progress.show();

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progress.cancel();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 1000);
        //
        mDatabase = FirebaseDatabase.getInstance().getReference("productes/dona");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    /*String name = dataSnapshot.getValue().toString();
                    producteText.setText(name);
                    //ProductesActivity user = dataSnapshot.getChildren().getClass(ProductesActivity.class);
                    producteText2.setText(dataSnapshot.child("tall de cabell").getKey());

                    String products = name.replace(",", "\n");
                    producteText3.setText(products);*/
                    String s = repeat(8);
                    String s2 = repeat(29);
                    String s3 = repeat(25);
                    String s4 = repeat(31);
                    String s5 = repeat(26);
                    String s6 = repeat(22);
                    String s7 = repeat(28);
                    String s8 = repeat(16);
                    String s9 = repeat(9);
                    String s10 = repeat(24);

                    //1 producte:
                    producteText1.setText(s + dataSnapshot.child("alissat").getKey().toString() + s2 +
                            dataSnapshot.child("alissat/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("alissat/temps").getValue().toString()+ " minuts");
                    //2 producte:
                    producteText2.setText(s + dataSnapshot.child("celles").getKey().toString()+ s7 +
                            "  "+ dataSnapshot.child("celles/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("celles/temps").getValue().toString()+ " minuts");
                    //3 producte:
                    producteText3.setText(s + dataSnapshot.child("depilació").getKey().toString() + s3 +
                            dataSnapshot.child("depilació/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("depilació/temps").getValue().toString()+ " minuts");
                    //4 producte:
                    producteText4.setText(s + dataSnapshot.child("extensions").getKey().toString() + s6 +
                            dataSnapshot.child("extensions/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("extensions/temps").getValue().toString()+ " minuts");
                    //5 producte:
                    producteText5.setText(s + dataSnapshot.child("manicura").getKey().toString() + s3 +
                            dataSnapshot.child("manicura/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("manicura/temps").getValue().toString()+ " minuts");
                    //6 producte:
                    producteText6.setText(s + dataSnapshot.child("massatge").getKey().toString() + s3 +
                            dataSnapshot.child("massatge/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("massatge/temps").getValue().toString()+ " minuts");
                    //7 producte:
                    producteText7.setText(s + dataSnapshot.child("parafina").getKey().toString()+ s2 +
                            dataSnapshot.child("parafina/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("parafina/temps").getValue().toString()+ " minuts");
                    //8 producte:
                    producteText8.setText(s + dataSnapshot.child("pedicura").getKey().toString() + s2 +
                            dataSnapshot.child("pedicura/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("pedicura/temps").getValue().toString()+ " minuts");
                    //9 producte:
                    producteText9.setText(s + dataSnapshot.child("permanent").getKey().toString() + s10 +
                            dataSnapshot.child("permanent/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("permanent/temps").getValue().toString()+ " minuts");
                    //10 producte:
                    producteText10.setText(s + dataSnapshot.child("pestanyes").getKey().toString() + s10 +
                            dataSnapshot.child("pestanyes/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("pestanyes/temps").getValue().toString()+ " minuts");
                    //11 producte:
                    producteText11.setText(s + dataSnapshot.child("tall de cabell").getKey().toString() + s6 +
                            dataSnapshot.child("tall de cabell/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("tall de cabell/temps").getValue().toString()+ " minuts");
                    //12 producte:
                    producteText12.setText(s + dataSnapshot.child("tenyir el cabell").getKey().toString()+ s8 +
                            "  "+ dataSnapshot.child("tenyir el cabell/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("tenyir el cabell/temps").getValue().toString()+ " minuts");
                    //13 producte:
                    producteText13.setText(s + dataSnapshot.child("tractament de peus").getKey().toString() + s9 +
                            dataSnapshot.child("tractament de peus/preu").getValue().toString()+ " €" + s5 +
                            dataSnapshot.child("tractament de peus/temps").getValue().toString()+ " minuts");
                    //14 producte:
                    producteText14.setText(s + dataSnapshot.child("ungles").getKey().toString() + s4 +
                            dataSnapshot.child("ungles/preu").getValue().toString()+ " €" + s3 +
                            dataSnapshot.child("ungles/temps").getValue().toString()+ " minuts");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        progressDialog.dismiss();
    }

    private String repeat(int count){
        char[] chars = new char[count];
        Arrays.fill(chars,' ');
        String s = new String(chars);
        return s;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chB1:
                if(chB1.isChecked()){
                    preuTotal+=pA;
                    tempsTotal+=tA;
                }else{
                    preuTotal-=pA;
                    tempsTotal-=tA;
                }
                break;
            case R.id.chB2:
                if(chB2.isChecked()){
                    preuTotal+=pC;
                    tempsTotal+=tC;
                }else{
                    preuTotal-=pC;
                    tempsTotal-=tC;
                }
                break;
            case R.id.chB3:
                if(chB3.isChecked()){
                    preuTotal+=pDep;
                    tempsTotal+=tDep;
                }else{
                    preuTotal-=pDep;
                    tempsTotal-=tDep;
                }
                break;
            case R.id.chB4:
                if(chB4.isChecked()){
                    preuTotal+=pEx;
                    tempsTotal+=tEx;
                }else{
                    preuTotal-=pEx;
                    tempsTotal-=tEx;
                }
                break;
            case R.id.chB5:
                if(chB5.isChecked()){
                    preuTotal+=pM;
                    tempsTotal+=tM;
                }else{
                    preuTotal-=pM;
                    tempsTotal-=tM;
                }
                break;
            case R.id.chB6:
                if(chB6.isChecked()){
                    preuTotal+=pMsg;
                    tempsTotal+=tMsg;
                }else{
                    preuTotal-=pMsg;
                    tempsTotal-=tMsg;
                }
                break;
            case R.id.chB7:
                if(chB7.isChecked()){
                    preuTotal+=pPF;
                    tempsTotal+=tPF;
                }else{
                    preuTotal-=pPF;
                    tempsTotal-=tPF;
                }
                break;
            case R.id.chB8:
                if(chB8.isChecked()){
                    preuTotal+=pPC;
                    tempsTotal+=tPC;
                }else{
                    preuTotal-=pPC;
                    tempsTotal-=tPC;
                }
                break;
            case R.id.chB9:
                if(chB9.isChecked()){
                    preuTotal+=pP;
                    tempsTotal+=tP;
                }else{
                    preuTotal-=pP;
                    tempsTotal-=tP;
                }
                break;
            case R.id.chB10:
                if(chB10.isChecked()){
                    preuTotal+=pPes;
                    tempsTotal+=tPes;
                }else{
                    preuTotal-=pPes;
                    tempsTotal-=tPes;
                }
                break;
            case R.id.chB11:
                if(chB11.isChecked()){
                    preuTotal+=pTC;
                    tempsTotal+=tTC;
                }else{
                    preuTotal-=pTC;
                    tempsTotal-=tTC;
                }
                break;
            case R.id.chB12:
                if(chB12.isChecked()){
                    preuTotal+=pTYC;
                    tempsTotal+=tTYC;
                }else{
                    preuTotal-=pTYC;
                    tempsTotal-=tTYC;
                }
                break;
            case R.id.chB13:
                if(chB13.isChecked()){
                    preuTotal+=pTP;
                    tempsTotal+=tTP;
                }else{
                    preuTotal-=pTP;
                    tempsTotal-=tTP;
                }
                break;
            case R.id.chB14:
                if(chB14.isChecked()){
                    preuTotal+=pU;
                    tempsTotal+=tU;
                }else{
                    preuTotal-=pU;
                    tempsTotal-=tU;
                }
                break;
            case R.id.bnt_ok:
                Intent intent = new Intent(this, CalendariActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                Bundle mBundle = new Bundle();
                mBundle.putString("preu",preuTotal.toString());
                mBundle.putString("temps",tempsTotal.toString());
                intent.putExtras(mBundle);
                startActivity(intent);
                break;
        }
        totalText.setText("El preu total dels productes escollits son: " + preuTotal + " €.");
        totalTemps.setText("El temps estimat que trigaran sera de: " + tempsTotal + " minuts.");


    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}