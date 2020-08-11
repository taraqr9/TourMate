package com.example.tourmate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.views.ExpenseMain;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    private TextView expTracker,mCapture,weather,map;

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private long backPressedTime;
    private Button logout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout);
        expTracker = findViewById(R.id.expTracker);
        mCapture = findViewById(R.id.mCapture);
        weather = findViewById(R.id.weather);
        map = findViewById(R.id.map);

        expTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ExpenseMain.class));
                Toast.makeText(Home.this, "Clicked on Expence Tracker Button", Toast.LENGTH_SHORT).show();
            }
        });
        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MomentCapture.class));
                finish();
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Weather.class));
                finish();
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Map.class));
                finish();
            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }


    @Override
    public void onBackPressed() {
        exitAppDialog();

    }

    public  void exitAppDialog(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(Home.this);
        LayoutInflater inflater = LayoutInflater.from(Home.this);
        View myView = inflater.inflate(R.layout.exit_dialog,null);
        final AlertDialog dialog = myDialog.create();
        dialog.setView(myView);

        final Button yes = myView.findViewById(R.id.yes_exit);
        Button no = myView.findViewById(R.id.no_exit);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Exit app--------------------------------------------------------------------
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void logout(){
        mAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }



}