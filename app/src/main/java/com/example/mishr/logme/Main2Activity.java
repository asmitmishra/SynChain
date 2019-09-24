package com.example.mishr.logme;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Main2Activity extends AppCompatActivity {

    private EditText email,password;
    private Button login,signUp;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login = (Button) findViewById(R.id.btnLogin);
        signUp = (Button)findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){

                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void signup(){
        Intent intent = new Intent(Main2Activity.this,SignUp.class);
        startActivity(intent);
    }

    private void signIn(){
        String mail = email.getText().toString();
        String pass = password.getText().toString();
        if(TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)){
            Toast.makeText(Main2Activity.this, "Enter Email id and password...", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        if(mAuth.getCurrentUser().isEmailVerified()){
                            Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(Main2Activity.this, "Please complete your email verification", Toast.LENGTH_SHORT).show();
                        }

                        //Toast.makeText(Main2Activity.this, "Sign In Problem!!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Main2Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        email.setText("");
                        password.setText("");
                    }
                }
            });
        }

    }



}
