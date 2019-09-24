package com.example.mishr.logme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class add_resources extends AppCompatActivity {

    EditText resname, idres;
    Button upload;

    StorageReference storageReference;
    DatabaseReference databaseReference,databaseReference1,databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resources);

        resname = (EditText)findViewById(R.id.edtres);
        idres = (EditText)findViewById(R.id.edtresId);

        upload = (Button)findViewById(R.id.btnAdd);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference1 = FirebaseDatabase.getInstance().getReference("VIT");
        databaseReference = databaseReference1.child("Resources");
        //databaseReference = databaseReference2.child("classNo");

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDFFile();
            }
        });


    }
    private void selectPDFFile(){
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select pdf file"),1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uploadPDFFile(data.getData());
        }
    }

    private void uploadPDFFile(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("uploads/" +System.currentTimeMillis()+ ".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();

                        Resources resources = new Resources(resname.getText().toString(), url.toString(),idres.getText().toString());
                        String key = databaseReference.push().getKey();
                        databaseReference.child(key).setValue(resources);
                        Toast.makeText(add_resources.this, "File Uploaded", Toast.LENGTH_SHORT).show();
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
