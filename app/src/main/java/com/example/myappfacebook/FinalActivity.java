package com.example.myappfacebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.net.InternetDomainName;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FinalActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txt_final;
    private Button btn_cancelar, btn_acceptar;
    private DatabaseReference mRootRef;
    ArrayList<String> dadesClient = new ArrayList<>();
    private String res_correu,res_temps, resultat_horari, res_productes, res_preu,resultat_dia;
    private Usuari client;
    String missatge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        //Li posem un titol i establim que podem tornat enrera
        getSupportActionBar().setTitle(getResources().getString(R.string.info_final));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        resultat_dia = intent.getStringExtra("resultat_dia");
        res_temps = intent.getStringExtra("res_temps");
        resultat_horari = intent.getStringExtra("resultat_horari");
        res_productes = intent.getStringExtra("res_productes");
        res_preu = intent.getStringExtra("res_preu");
        res_correu = intent.getStringExtra("res_correu");

        //dadesClientAct(res_correu);

        txt_final=(TextView)findViewById(R.id.text_final);
        txt_final.setText("-Dia escollit: " + resultat_dia + "\n-L'hora seleccionada: " + resultat_horari +
                " P.M. \n-Temp empleat: " + res_temps + " minuts .\n-Els productes contractats son: " +
                res_productes + ". \n-Preu productes: " + res_preu + " €, \n-El teu correu es: " +
                res_correu + "\n\n Si tot es correcte pulsa acceptar i envia " + "un correu per tenir " +
                "constancia la perruqueria. Gracies");
        missatge = "Reserva escollida:\n -Dia: "+ resultat_dia + "\n -Hora: " + resultat_horari + " P.M." +
                "\n -Temp empleat: " + res_temps + " minuts\n -Productes: " + res_productes + ". \n-Preu: "
                + res_preu + " €\n Gracies";
        //txt_telf=(TextView)findViewById(R.id.text_telef);
        btn_cancelar=(Button)findViewById(R.id.btn_cancelar);
        btn_acceptar=(Button)findViewById(R.id.btn_acceptar);
        btn_cancelar.setOnClickListener(this);
        btn_acceptar.setOnClickListener(this);
        //Enviar un missatge de texto
        if(ActivityCompat.checkSelfPermission(
                FinalActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(
                FinalActivity.this,Manifest
                        .permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(FinalActivity.this,new String[]
                    { Manifest.permission.SEND_SMS,},1000);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), RecyclerTime.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //Torna a l'inici de l'aplicació
            case R.id.btn_cancelar:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_acceptar:
                sendEmail(res_correu);
                emmagatzemarBBDDClient(resultat_dia, res_correu, resultat_horari, res_preu, res_temps);
                //envia un missatge amb les dades
                //enviarMensaje("647410533",missatge);//"647410533"
                //enviarMensaje(dadesClient.get(2),missatge);
                dadesClient.removeAll(dadesClient);
                //txt_telf.setText("Si ja has enviat el correu electònic i has rebut un missatge amb el " +
                        //"recordatori de la cita, ja pots finalitzar la sesió. Gràcies");
                break;
        }
    }

    protected void sendEmail(String res_correu) {
        //Aqui aniria el correu de la perruqueria
        String[] TO = {"franciscoparisalbero@gmail.com"};
        String[] CC = {res_correu};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reserva cita");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "-L'hora seleccionada: " + resultat_horari + " P.M. \n-Temp empleat: " + res_temps +
                " minuts .\n-Els productes contractats son: " + res_productes + ". \n-Preu productes: " + res_preu +
                " €, \n-El teu correu es: " + res_correu + "\n\n Si tot es correcte pulsa acceptar i envia " +
                "un correu per tenir constancia la perruqueria. Gracies");

        try {
            startActivity(Intent.createChooser(emailIntent, "Confirma les teves dades i envia per Gmail"));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FinalActivity.this, "El email, no s\'ha pogut enviar.", Toast.LENGTH_SHORT).show();
        }
    }

    private void enviarMensaje (String numero, String mensaje){
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numero,null,mensaje,null,null);
            Toast toast=Toast.makeText(this, "Missatge enviat", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Missatge no enviat, dades incorrectes.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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
                            //txt_telf.setText("Dades del client: " + dadesClient + dadesClient.size() +
                                    //". El teu telèfon es el " + dadesClient.get(2));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                //dadesClient.removeAll(dadesClient);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void emmagatzemarBBDDClient(String data, String email, String horari, String preu, String temps) {
        Map<String, Object> dadesUsuari = new HashMap<>();

        client = new Usuari(data,email,horari,preu,temps);
        client.setData(data);
        client.setEmail(email);
        client.setHorari(horari);
        client.setPreu(preu);
        client.setTemps(temps);

        dadesUsuari.put("data", client.getData());
        dadesUsuari.put("email", client.getEmail());
        dadesUsuari.put("horari", client.getHorari());
        dadesUsuari.put("preu", client.getPreu());
        dadesUsuari.put("temps", client.getTemps());

        mRootRef.child("usuari").push().setValue(dadesUsuari);
    }
}