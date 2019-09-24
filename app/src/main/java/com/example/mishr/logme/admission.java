package com.example.mishr.logme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class admission extends AppCompatActivity {


    EditText name, id,resname1,flag1;
    Button btn;
    DatabaseReference databasreference,databaseReference1;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission);
        final boolean flag = true;
        name = (EditText)findViewById(R.id.searchname);
        id = (EditText)findViewById(R.id.searchid);
        btn = (Button)findViewById(R.id.btnSearch);
        flag1 = (EditText)findViewById(R.id.flag);
        resname1 = (EditText)findViewById(R.id.stdresource);
        databaseReference1 = FirebaseDatabase.getInstance().getReference("VIT");
        databasreference = databaseReference1.child("Students");
        storageReference = FirebaseStorage.getInstance().getReference();





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String idEntered = id.getText().toString();
                Query checkStudent = databaseReference1.child("Students").orderByChild("stdid").equalTo(idEntered);
                checkStudent.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if(dataSnapshot.exists()) {
//                            selectStudentResource();
                            Toast.makeText(admission.this, "Registration number id already taken, please enter another number", Toast.LENGTH_SHORT).show();


                        }



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

                    selectStudentResource();





            }
        });

    }

    private void selectStudentResource() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select pdf file"), 2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uploadPDFFile(data.getData());
        }
    }

    private void uploadPDFFile(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("StudentBioData/" +System.currentTimeMillis()+ ".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();
                        String key = databasreference.push().getKey();
                        Student student = new Student(name.getText().toString(), id.getText().toString(), url.toString(),resname1.getText().toString(), key);
                        databasreference.child(key).setValue(student);
                        Toast.makeText(admission.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded" + (int)progress + "%");
            }
        });
    }
}
