package com.example.tourmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    private EditText fullNameEd,emailEd,passEd,phoneEd;
    private TextView loginTv;
    private Button signBtn;
    private RadioButton maleRadio,femaleRadio;
    private ProgressBar pBar;
    String gender = "";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("User");


        fullNameEd = findViewById(R.id.fullname_sign_ed);
        emailEd = findViewById(R.id.email_sign_ed);
        passEd = findViewById(R.id.pass_sign_ed);
        phoneEd = findViewById(R.id.phone_sign_ed);
        maleRadio = findViewById(R.id.male_radio);
        femaleRadio = findViewById(R.id.female_radio);
        signBtn = findViewById(R.id.signup);
        loginTv = findViewById(R.id.login_sign);
        pBar = findViewById(R.id.pBar_R);


        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,Login.class));
                finish();
            }
        });

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

    }
    public void SignUp(){

        final String fname = fullNameEd.getText().toString().trim();
        final String email = emailEd.getText().toString().trim();
        final String pass = passEd.getText().toString().trim();
        final String phone = phoneEd.getText().toString().trim();
        if(fname.isEmpty()){
            fullNameEd.setError("Required Field !");
            return;
        }
        if(email.isEmpty()){
            emailEd.setError("Required Field !");
            return;
        }
        if(pass.isEmpty()){
            passEd.setError("Required Field !");
            return;
        }
        if(pass.length()<6){
            passEd.setError("At least 6 digit Required !");
            return;
        }
        if(phone.isEmpty()){
            phoneEd.setError("Required Field !");
        }
        if(maleRadio.isChecked() || femaleRadio.isChecked()){
            if(maleRadio.isChecked()){
                gender = "Male";
            }
            if(femaleRadio.isChecked()){
                gender = "Female";
            }
        }else{
            Toast.makeText(Registration.this, "Please select your Gender", Toast.LENGTH_SHORT).show();
            return;
        }
        signBtn.setEnabled(false);
        pBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            UserInfo info = new UserInfo(fname,email,pass,phone,gender);

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(Registration.this,Login.class));
                                        Toast.makeText(Registration.this, "Sucessfully Registered", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }else{
                            Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            signBtn.setEnabled(true);
                            pBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

}