//package com.example.myappfacebook;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//public class Usuari extends AppCompatActivity implements View.OnClickListener {
//    private Button btn_del, btn_upd;
//    boolean isSignUpMade = false;
//    FirebaseUser firebaseUser = null;
//    private FirebaseAuth mAuth;
//    private TextView nameTextView, emailTextView;
//    private EditText Txt_Pass;
//    private GoogleApiClient googleApiClient;
//
//    private FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener firebaseAuthListener;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_usuari);
//
//        btn_del=(Button)findViewById(R.id.btn_delete);
//        btn_upd=(Button)findViewById(R.id.btn_Update);
//        mAuth = FirebaseAuth.getInstance();
//        nameTextView = (TextView) findViewById(R.id.textUsuari);
//        emailTextView = (TextView) findViewById(R.id.email_Usuari);
//        Txt_Pass = (EditText)findViewById(R.id.password);
//
//        btn_del.setOnClickListener(this);
//        btn_upd.setOnClickListener(this);
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    //setUserData(user);
//                    nameTextView.setText(user.getDisplayName());
//                    emailTextView.setText(user.getEmail());
//                } else {
//                    goLogInScreen();
//                }
//            }
//        };
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_delete:
//                onBackPressed();
//                break;
//            case R.id.btn_Update:
//                isEmailAlreadyUsed();
//                break;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (!isSignUpMade) {
//            if (firebaseUser != null)
//                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(),"Compte d'usuari eliminada correctament", Toast.LENGTH_LONG).show();
//                            //Log.d(TAG, "User account deleted.");
//                        }
//                    }
//                });
//        }
//        super.onBackPressed();
//    }
//
//    private void isEmailAlreadyUsed() {
//
//        String email = Txt_User.getText().toString().trim();
//        String pass = Txt_Pass.getText().toString().trim();
//
/////// here I am gonna create dummy user at **Firebase** with the Entered email Id (Email to check against)
//
//        mAuth.createUserWithEmailAndPassword(mEmailView.getText().toString(), "dummypass")
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
//
//
//                        if (task.isSuccessful()) {
//                            /////user do not exit so good to initialize firebase user.
//                            firebaseUser = task.getResult().getUser();
//                        } else {
//                            if(task.getException().getMessage().equals("The email address is already in use by another account.")) {
//                                Log.d(TAG, "This email ID already used by someone else");
//                            }
//                        }
//                    }
//                });
//    }
//
//}