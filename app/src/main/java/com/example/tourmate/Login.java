package com.example.tourmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText emailEd,passwordEd;
    private TextView forgotPass,signup;
    private Button login_btn;
    private ProgressBar pBar_L;
    private ProgressBar pBar_F;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        emailEd = findViewById(R.id.email_login_ed);
        passwordEd = findViewById(R.id.pass_login_ed);
        forgotPass = findViewById(R.id.forgot_pass);
        signup = findViewById(R.id.signup_login);
        login_btn = findViewById(R.id.login);
        pBar_L =findViewById(R.id.pBar_L);
        pBar_F = findViewById(R.id.pBar_F);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(Login.this,Home.class));
                }
            }
        };

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPassword();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Registration.class));
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    @Override
    protected void onStart() {
        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
    }

    public void Login(){
        String email = emailEd.getText().toString().trim();
        String pass = passwordEd.getText().toString().trim();

        if(email.isEmpty()){
            emailEd.setError("Required Field !");
            return;
        }
        if(pass.isEmpty()){
            passwordEd.setError("Required Field !");
            return;
        }
        login_btn.setEnabled(false);
        pBar_L.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Home.class));

                        }else{
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            login_btn.setEnabled(true);
                            pBar_L.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    public  void ForgotPassword(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(Login.this);
        LayoutInflater inflater = LayoutInflater.from(Login.this);
        View myView = inflater.inflate(R.layout.forgot_dialog,null);
        final AlertDialog dialog = myDialog.create();
        dialog.setView(myView);

        final EditText emailEd = myView.findViewById(R.id.email_forgotPass);
        final Button submit = myView.findViewById(R.id.submit_forgot);
        Button cancel = myView.findViewById(R.id.cancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEd.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    emailEd.setError("Field Required");
                    return;
                }
                submit.setEnabled(false);
                pBar_F.setVisibility(View.VISIBLE);

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(Login.this, "Check your email id plase.", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    submit.setEnabled(true);
                                    pBar_F.setVisibility(View.INVISIBLE);
                                }
                            }
                        });


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

}
