package com.example.myappfacebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ProducteAdapter2 extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener, View.OnClickListener {

    MyRecyclerViewAdapter adapter;
    private ArrayList<String> dadesProds = new ArrayList<>();
    private ArrayList<String> products = new ArrayList<>();
    private ArrayList<String> price = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    Button buttonAccept, btnCancel;
    String[] productes = {"alissat","celles","depilació","extensions","manicura","massatge","parafina",
            "pedicura","permanent","pestanyes","tall de cabell","tenyir el cabell","tractament de peus","ungles"};
    String [] preus ={"28","10","32","40","14","32","14","22","33","10","24","30","30","10"};
    String [] tempsEmpleat ={"30","15","30","45","30","30","15","45","60","15","30","30","45","15"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producte_adapter2);
        getSupportActionBar().setTitle("Escull l'article:");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonAccept =(Button)findViewById(R.id.btnAccept);
        buttonAccept.setOnClickListener(this);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        //Afegim a l'array tots el productes
        for (int i = 0; i <productes.length; i++) {
            products.add((productes[i]));
            price.add(preus[i]);
            time.add(tempsEmpleat[i]);
        }
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, products, price, time);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        //Quan clickem en el article, el possem a dalt de tota l'activitat
        Toast toast=Toast.makeText(this, "Contractat " + adapter.getItem(position) + ". Nº " + position, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        //Afegim a l'array el element afegit
        dadesProds.add(String.valueOf(position));
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //Intent intent = new Intent(getApplicationContext(), HorariActivity.class);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        String correu = intent.getStringExtra("correu");

        switch (v.getId()){
            //Si acceptem, anem a un altra activitat
            case R.id.btnAccept:
                Intent i = new Intent(this,MainActivity2.class);
                i.putExtra("Extra", dadesProds);
                i.putExtra("correu", correu);
                //Tens que escollir algun article, de lo contrari, no et deixa continura perque es buit l'objecte
                if(dadesProds.isEmpty()){
                    Toast toast=Toast.makeText(this, "Escull al menys un article", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }else{
                    startActivity(i);
                }
                break;
            //Si cancelem, tornem a la mateixa activitat i cancelem l'array dadesProds
            case R.id.btnCancel:
                dadesProds.clear();
                //Intent i2 = new Intent(this,MainActivity.class);
                Intent i2 = new Intent(this,ProducteAdapter2.class);
                i2.putExtra("Extra", dadesProds);
                i2.putExtra("correu", correu);
                startActivity(i2);
                break;
        }
    }
}