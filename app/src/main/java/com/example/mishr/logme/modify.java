package com.example.mishr.logme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class modify extends AppCompatActivity {

    EditText id1;
    TextView textView;
    Button update;
    DatabaseReference databasreference;
    FirebaseStorage mstorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        id1 = (EditText)findViewById(R.id.edtid);
        update = (Button)findViewById(R.id.btnUpdate);
        textView = (TextView)findViewById(R.id.textView2);
        databasreference = FirebaseDatabase.getInstance().getReference("VIT").child("Students");
        mstorage = FirebaseStorage.getInstance();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String idEntered = id1.getText().toString();
                Query modifyStudent = databasreference.orderByChild("stdid").equalTo(idEntered);

                modifyStudent.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        final Student studentfound1 = dataSnapshot.getValue(Student.class);
                        StorageReference fileref = mstorage.getReferenceFromUrl(studentfound1.getLink());
                        Toast.makeText(modify.this, "Got link...now delete", Toast.LENGTH_SHORT).show();
                        fileref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                databasreference.child(id1.getText().toString()).removeValue();
                                Toast.makeText(modify.this, "Upload the new file to successully update the data", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(modify.this, admission.class));
                                DatabaseReference dtr = databasreference.child(studentfound1.getKey1());
                                dtr.removeValue();
                                id1.setText(null);
                            }
                        });
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //databasreference.child(id1.getText().toString()).removeValue();





            }
        });

    }


}
