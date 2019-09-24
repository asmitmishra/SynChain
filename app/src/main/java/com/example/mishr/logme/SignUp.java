package com.example.mishr.logme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUp extends AppCompatActivity {

    private EditText email2, password1;
    private Button signUp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email2 = (EditText)findViewById(R.id.mail);
        password1 = (EditText)findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        signUp = (Button)findViewById(R.id.btnSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String mail1 = email2.getText().toString().trim();
                String pass = password1.getText().toString().trim();
                if(TextUtils.isEmpty(mail1) || TextUtils.isEmpty(pass)){
                    Toast.makeText(SignUp.this, "Empty!!", Toast.LENGTH_SHORT).show();
                }else {

                    mAuth.createUserWithEmailAndPassword(mail1, pass)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        mAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(SignUp.this, "Registered Successfully. Please verify your email", Toast.LENGTH_LONG).show();
                                                            email2.setText("");
                                                            password1.setText("");
                                                        }else{
                                                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                        startActivity(new Intent(SignUp.this, Main2Activity.class));

                                        Toast.makeText(SignUp.this, "createUserWithEmail:success", Toast.LENGTH_SHORT).show();


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        ;


                                    }
                                }
                            });
                }

            }
        });


    }
}
