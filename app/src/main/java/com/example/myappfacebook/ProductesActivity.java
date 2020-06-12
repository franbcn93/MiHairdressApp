package com.example.myappfacebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class ProductesActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView producteText;
    private TextView producteText1;
    private TextView producteText2;
    private TextView producteText3;
    private TextView producteText4;
    private TextView producteText5;
    private TextView preuText1;
    private TextView preuText2;
    private TextView preuText3;
    private TextView preuText4;
    private TextView preuText5;
    private TextView totalText;
    private  DatabaseReference mDatabase;
    private String depilacio;
    private  Integer preuTotal;
    private CheckBox chB1,chB2,chB3,chB4,chB5;
    private Integer pDep= 32, pMsg=32,pTC=24, pTYC=30, pU=14;
    private Button btnBack;

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
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        totalText = (TextView)findViewById(R.id.textTotal);
        preuTotal = 0;
        btnBack = (Button)findViewById(R.id.idBack);
        btnBack.setOnClickListener(this);

        producteText1 = (TextView)findViewById(R.id.textProducte1);
        producteText2 = (TextView)findViewById(R.id.textProducte2);
        producteText3 = (TextView)findViewById(R.id.textProducte3);
        producteText4 = (TextView)findViewById(R.id.textProducte4);
        producteText5 = (TextView)findViewById(R.id.textProducte5);

        preuText1 = (TextView)findViewById(R.id.textPreu1);
        preuText2 = (TextView)findViewById(R.id.textPreu2);
        preuText3 = (TextView)findViewById(R.id.textPreu3);
        preuText4 = (TextView)findViewById(R.id.textPreu4);
        preuText5 = (TextView)findViewById(R.id.textPreu5);

        chB1 = (CheckBox)findViewById(R.id.checkBox1);
        chB1.setOnClickListener(this);
        chB2 = (CheckBox)findViewById(R.id.checkBox2);
        chB2.setOnClickListener(this);
        chB3 = (CheckBox)findViewById(R.id.checkBox3);
        chB3.setOnClickListener(this);
        chB4 = (CheckBox)findViewById(R.id.checkBox4);
        chB4.setOnClickListener(this);
        chB5 = (CheckBox)findViewById(R.id.checkBox5);
        chB5.setOnClickListener(this);

        //totalText.setText("El preu total dels productes escollits son: " + preuTotal + " €.");

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

                    //1 producte:
                    producteText1.setText(dataSnapshot.child("depilació").getKey().toString());
                    preuText1.setText(dataSnapshot.child("depilació").getValue().toString()+ " €");

                    //2 producte:
                    producteText2.setText(dataSnapshot.child("massatge").getKey().toString());
                    preuText2.setText(dataSnapshot.child("massatge").getValue().toString()+ " €");
                    //3 producte:
                    producteText3.setText(dataSnapshot.child("tall de cabell").getKey().toString());
                    preuText3.setText(dataSnapshot.child("tall de cabell").getValue().toString()+ " €");
                    //4 producte:
                    producteText4.setText(dataSnapshot.child("tenyir el cabell").getKey().toString());
                    preuText4.setText(dataSnapshot.child("tenyir el cabell").getValue().toString()+ " €");
                    //5 producte:
                    producteText5.setText(dataSnapshot.child("ungles").getKey().toString());
                    preuText5.setText(dataSnapshot.child("ungles").getValue().toString()+ " €");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkBox1:
                if(chB1.isChecked()){
                    preuTotal+=pDep;
                }else{
                    preuTotal-=pDep;
                }
                break;
            case R.id.checkBox2:
                if(chB2.isChecked()){
                    preuTotal+=pMsg;
                }else{
                    preuTotal-=pMsg;
                }
                break;
            case R.id.checkBox3:
                if(chB3.isChecked()){
                    preuTotal+=pTC;
                }else{
                    preuTotal-=pTC;
                }
                break;
            case R.id.checkBox4:
                if(chB4.isChecked()){
                    preuTotal+=pTYC;
                }else{
                    preuTotal-=pTYC;
                }
                break;
            case R.id.checkBox5:
                if(chB5.isChecked()){
                    preuTotal+=pU;
                }else{
                    preuTotal-=pU;
                }
                break;
            case R.id.idBack:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
        totalText.setText("El preu total dels productes escollits son: " + preuTotal + " €.");
    }

    /*public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
*/
}