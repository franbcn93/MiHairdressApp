package com.example.myappfacebook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private String TAG = "Error: " ;
    private String novaContr;
    private ImageView photoImageView;
    private TextView nameTextView, textInformatiu;
    private TextView emailTextView;
    private TextView idTextView;
    private GoogleApiClient googleApiClient;
    private Button Btn_Elimina, Btn_Actualitza, Btn_Confirmar, Btn_Productes;
    private EditText canvi_Contr, old_Pass;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Escogeix opció:");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoImageView = (ImageView) findViewById(R.id.photoImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        idTextView = (TextView) findViewById(R.id.idTextView);
        idTextView.setVisibility(View.INVISIBLE); //d'aquesta manera ocultem la contrasenya encriptada
        old_Pass=(EditText)findViewById(R.id.old_Password);
        old_Pass.setVisibility(View.INVISIBLE);
        canvi_Contr=(EditText) findViewById(R.id.canvi_Passw);
        canvi_Contr.setVisibility(View.INVISIBLE);
        textInformatiu=(TextView)findViewById(R.id.textPulsaBtn);
        textInformatiu.setAlpha(0.0f);

        Btn_Elimina=(Button)findViewById(R.id.elimina);
        Btn_Actualitza=(Button)findViewById(R.id.actualitza);
        Btn_Confirmar=(Button)findViewById(R.id.confirma);
        Btn_Confirmar.setVisibility(View.INVISIBLE);
        Btn_Productes=(Button)findViewById(R.id.btn_Productes);

        Btn_Elimina.setOnClickListener(this);
        Btn_Actualitza.setOnClickListener(this);
        Btn_Confirmar.setOnClickListener(this);
        Btn_Productes.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //setUserData(user);
                    nameTextView.setText(user.getDisplayName());
                    emailTextView.setText(user.getEmail());
                    idTextView.setText(user.getUid());
                    Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(photoImageView);
                } else {
                    goLogInScreen();
                }
            }
        };
    }

    private void setUserData(FirebaseUser user) {
        nameTextView.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());
        idTextView.setText(user.getUid());
        Glide.with(this).load(user.getPhotoUrl()).into(photoImageView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logOut(View view) {
        firebaseAuth.signOut();
    }

    public void revoke(View view) {
        firebaseAuth.signOut();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.elimina:
                canvi_Contr.setVisibility(View.INVISIBLE);
                visibilitat();
                Btn_Confirmar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        eliminaCompte();
                    }
                });
                break;
            case R.id.actualitza:
                canvi_Contr.setVisibility(View.VISIBLE);
                visibilitat();
                Btn_Confirmar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        novaContr = canvi_Contr.getText().toString();
                        actualizarPassw(novaContr);
                    }
                });
                break;
            case R.id.btn_Productes:
                Intent intent = new Intent(this, ProductesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }


    private void visibilitat(){
        old_Pass.setVisibility(View.VISIBLE);
        old_Pass.requestFocus(); //Asegurar que editText tiene focus

        textInformatiu.setAlpha(1.0f);
        Btn_Confirmar.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(old_Pass, InputMethodManager.SHOW_IMPLICIT);
    }

    public void actualizarPassw(final String newPassw){
        progressDialog.setMessage("Realitzant el canvi de contrasenya...");
        progressDialog.show();

        String email = emailTextView.getText().toString().trim();
        String pass = old_Pass.getText().toString();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, pass);

        user.reauthenticate(credential)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassw).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"Password Actualitzat", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),"Error, password no actualitzat", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(),"Error autentificació", Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }
            });

    }

    private void eliminaCompte(){
        progressDialog.setMessage("Eliminant usuari...");
        progressDialog.show();

        String email = emailTextView.getText().toString().trim();
        String pass = old_Pass.getText().toString();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, pass);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Usuari eliminat", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Error al eliminar l'usuari", Toast.LENGTH_LONG).show();
                }
            }
        });
        progressDialog.dismiss();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        firebaseAuth.signOut();
        return true;
    }
}