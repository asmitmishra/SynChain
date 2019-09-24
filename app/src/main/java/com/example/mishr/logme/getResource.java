package com.example.mishr.logme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class getResource extends AppCompatActivity {

    EditText resourceid,reslink,resourcenamefetched;
    DatabaseReference databaseReference;
    Button getresource,qrgenerate;
    ImageView qrshow;
    FloatingActionButton btn;
    QRGEncoder qrgEncoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_resource);

        resourceid = (EditText)findViewById(R.id.edtresourceid);
        qrshow = (ImageView) findViewById(R.id.imageViewqr);

        btn = (FloatingActionButton)findViewById(R.id.actionbtn);
        getresource = (Button)findViewById(R.id.btngetresource);
        reslink = (EditText)findViewById(R.id.resLink);
        resourcenamefetched = (EditText)findViewById(R.id.gotresname);
        qrgenerate = (Button)findViewById(R.id.btnqrgenerator);
        final OutputStream[] outputStream = new OutputStream[1];

        databaseReference = FirebaseDatabase.getInstance().getReference("VIT").child("Resources");


        getresource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResource();
            }
        });

        qrgenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generate();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qrshow.getDrawable() == null || TextUtils.isEmpty(reslink.getText().toString())){
                    Toast.makeText(getResource.this, "Generate the QR code first!", Toast.LENGTH_SHORT).show();
                }else{
                    BitmapDrawable drawable = (BitmapDrawable) qrshow.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    File filepath = Environment.getExternalStorageDirectory();
                    File dir = new File(filepath.getAbsoluteFile() + "/Demo/");
                    dir.mkdir();
                    File file = new File(dir, System.currentTimeMillis() + ".jpg");
                    try {
                        outputStream[0] = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream[0]);
                   // Toast.makeText(getResource.this, "Save successful", Toast.LENGTH_SHORT).show();
                    try {
                        outputStream[0].flush();
                        outputStream[0].close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String path1 = file.getPath();

                    Uri path = FileProvider.getUriForFile(getResource.this, "com.example.mishr.logme", new File(path1));

                    Intent shareintent = new Intent();
                    shareintent.setAction(Intent.ACTION_SEND);
                    shareintent.putExtra(Intent.EXTRA_TEXT, "This is the image i m sharing.");
                    shareintent.putExtra(Intent.EXTRA_STREAM,path);
                    shareintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareintent.setType("image/*");
                    startActivity(Intent.createChooser(shareintent, "Share using:"));
                }
            }
        });


    }

    private void getResource(){

        final String residEntered = resourceid.getText().toString();
        Query queryResource = databaseReference.orderByChild("resourceid").equalTo(residEntered);

        queryResource.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Resources resourcefound = dataSnapshot.getValue(Resources.class);
                reslink.setText(resourcefound.getResourceLink());
                resourcenamefetched.setText("Resource Name: " + resourcefound.getResourceName());
                resourcenamefetched.setVisibility(View.VISIBLE);
                Toast.makeText(getResource.this, "Fetched the Resource..Kindly Generate the QR code...", Toast.LENGTH_SHORT).show();
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
    }


    private void generate(){
        if(TextUtils.isEmpty(reslink.getText().toString().trim())){
            Toast.makeText(getResource.this, "No data!!", Toast.LENGTH_SHORT).show();
        }
        else {

            String inputVal = reslink.getText().toString().trim();
            if (inputVal!= null) {
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3 / 4;
                qrgEncoder = new QRGEncoder(inputVal, null, QRGContents.Type.TEXT, smallerDimension);
                try {
                    Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                    qrshow.setImageBitmap(bitmap);


                } catch (WriterException e) {
                    Toast.makeText(getResource.this, "error occured during code generation..", Toast.LENGTH_SHORT).show();

                }

            } else {

            }
        }

    }

}
