package com.example.myappfacebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "Error: " ;
    private GoogleApiClient googleApiClient;

    private SignInButton signInButton;

    public static final int SIGN_IN_CODE = 777;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private ProgressBar progressBar;

    private EditText Txt_User, Txt_Pass, Txt_Tel;
    private TextView Txt_sexe;
    private TextView Txt_newPassw;
    private Button Btn_Reg, Btn_Login;
    private RadioButton Btn_home, Btn_dona;
    private ProgressDialog progressDialog;
    private RadioGroup radioGroup;
    private DatabaseReference mRootRef;
    private Client dades;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_2);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Txt_User = (EditText)findViewById(R.id.email);
        Txt_Pass = (EditText)findViewById(R.id.passwd);
        Btn_Reg = (Button)findViewById(R.id.btn_register);
        Btn_Reg.setOnClickListener(this);
        Btn_Login = (Button)findViewById(R.id.btn_login);
        Btn_Login.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        Txt_newPassw = (TextView)findViewById(R.id.txt_newPassw);
        Txt_newPassw.setOnClickListener(this);
        //Fem la part del Registre
        Txt_Tel=(EditText)findViewById(R.id.telefon);
        Txt_Tel.setVisibility(View.INVISIBLE);
        Txt_sexe=(TextView)findViewById(R.id.txt_sexe);
        Txt_sexe.setVisibility(View.INVISIBLE);
        Btn_home=(RadioButton)findViewById(R.id.rBtnHome);
        Btn_home.setVisibility(View.INVISIBLE);
        Btn_dona=(RadioButton)findViewById(R.id.rBtnDona);
        Btn_dona.setVisibility(View.INVISIBLE);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        //Emmagatzemar dades
        mRootRef = FirebaseDatabase.getInstance().getReference();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goMainScreen();
                }
            }
        };
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            firebaseAuthWithGoogle(result.getSignInAccount());
        } else {
            toast=Toast.makeText(getApplicationContext(),"No s'ha pogut iniciar sesió", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
            toast.show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        progressBar.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.GONE);

        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);
                signInButton.setVisibility(View.VISIBLE);

                if (!task.isSuccessful()) {
                    toast=Toast.makeText(getApplicationContext(),R.string.not_firebase_auth, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
                    toast.show();
                }
            }
        });
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("telefon", Txt_Tel.getText().toString());
        startActivity(intent);
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
        String email = Txt_User.getText().toString().trim();
        String pass = Txt_Pass.getText().toString().trim();
        //Verifiquem si el email o password es buid i li possem un valor qualsevol
        if(email.matches("")){
            email="null";
        }if(pass.matches("")){
            pass="null";
        }
        //
        switch (v.getId()){
            case R.id.btn_login:
                verif_login(email,pass);
                break;
            case R.id.btn_register:
                registrantDades(email,pass);
                //registraUsuari(email,pass);
                break;
            case R.id.elimina:

                break;
            case R.id.actualitza:
                break;
            case R.id.txt_newPassw:
                enviaPasswd();
                break;
        }
    }

    private void enviaPasswd() {
        firebaseAuth.setLanguageCode("es");
        firebaseAuth.sendPasswordResetEmail(Txt_User.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    toast=Toast.makeText(getApplicationContext(),"Se ha enviat un correu per restablir la contrasenya", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
                    toast.show();
                }else {
                    toast=Toast.makeText(getApplicationContext(),"No s'ha pogut enviar el correu", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
                    toast.show();
                }
            }
        });
    }

    public void verif_login(final String email,String pass) {

        //Usuari Administrador
        if (email.equals("franciscoparisalbero@gmail.com") && pass.equals("q")) {//123456
            Intent intent = new Intent(getApplicationContext(),Administrador.class);
            startActivity(intent);
        }
        //emailOpasswdInc(email, pass);
        progressDialog.setMessage("Realitzant login...");
        progressDialog.show();
        //Se podría mejorar el FirebaseA-----
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            toast=Toast.makeText(getApplicationContext(),"Email y password correctes", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
                            toast.show();
                        }else {
                            toast=Toast.makeText(getApplicationContext(),"Email o password incorrectes", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
                            toast.show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }


    private void emailOpasswdInc(String email,String pass){
        if(TextUtils.isEmpty(email)){
            toast=Toast.makeText(getApplicationContext(),"Tens que ingressar un email", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
            toast.show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            toast=Toast.makeText(getApplicationContext(),"Tens que ingressar un password", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
            toast.show();
            return;
        }
    }

    private void registrantDades(String email,String pass){
        Txt_Tel.setVisibility(View.VISIBLE);
        Txt_sexe.setVisibility(View.VISIBLE);
        Btn_home.setVisibility(View.VISIBLE);
        Btn_dona.setVisibility(View.VISIBLE);
        String telefon = Txt_Tel.getText().toString();
        if(telefon.matches("") ){
            toast=Toast.makeText(getApplicationContext(),"Escriu el teu número de telèfon", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
            toast.show();
        }else{
            if(radioGroup.getCheckedRadioButtonId()==-1){
                toast=Toast.makeText(getApplicationContext(),"Escull el teu génere", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
                toast.show();
            }else {
                //En el cas que el teu telefon no sigui real...
                /*if (telefon == null || telefon.length() < 6 || telefon.length() > 13) {
                    Toast.makeText(getApplicationContext(),"Escriu el teu número de telèfon real", Toast.LENGTH_LONG).show();
                }*/
                registraUsuari(email,pass);
            }


        }
    }

    private void registraUsuari(final String email,String pass) {
        progressDialog.setMessage("Realitzant registre...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            toast=Toast.makeText(getApplicationContext(),"S'ha registrat el email", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
                            toast.show();
                            String genere;
                            if(Btn_home.isChecked()){
                                Toast.makeText(getApplicationContext(),"home", Toast.LENGTH_LONG).show();
                                genere =  "home";
                            }else{
                                Toast.makeText(getApplicationContext(),"Dona", Toast.LENGTH_LONG).show();
                                genere =  "dona";
                            }
                            //Emmagatzem les dades a la BBDD
                            emmagatzemarBBDD(email, Txt_Tel.getText().toString(), genere);
                        }else{
                            try{
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthWeakPasswordException weakPassword)
                            {
                                toast=Toast.makeText(getApplicationContext(),"Password massa debil", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
                                toast.show();
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail){
                                toast=Toast.makeText(getApplicationContext(),"Email mal format", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
                                toast.show();
                            }
                            catch (FirebaseAuthUserCollisionException existEmail){
                                toast=Toast.makeText(getApplicationContext(),"Email ja existent", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
                                toast.show();
                            }
                            catch (Exception e){
                                toast=Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 330);
                                toast.show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    private void emmagatzemarBBDD(String email, String telefon, String genere) {
        Map<String, Object> dadesUsuari = new HashMap<>();

        dades = new Client(email,telefon,genere);
        dades.setEmail(email);
        dades.setTelefon(telefon);
        dades.setSexe(genere);

        dadesUsuari.put("email", dades.getEmail());
        dadesUsuari.put("telefon", dades.getTelefon());
        dadesUsuari.put("sexe", dades.getSexe());

        mRootRef.child("Dades client").push().setValue(dadesUsuari);

    }
}