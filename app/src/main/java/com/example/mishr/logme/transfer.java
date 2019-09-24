package com.example.mishr.logme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class transfer extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText stdid,gotLink,sstname;
    Button search,generate,reset;
    QRGEncoder qrgEncoder;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);


        stdid = (EditText)findViewById(R.id.searchid);
        sstname = (EditText)findViewById(R.id.studentname);
        search = (Button)findViewById(R.id.btnSearch);
        generate = (Button) findViewById(R.id.btnqr);
        gotLink = (EditText)findViewById(R.id.gotLink);
        reset = (Button) findViewById(R.id.btnReset);
        image = (ImageView)findViewById(R.id.imageView);
        databaseReference = FirebaseDatabase.getInstance().getReference("VIT").child("Students");



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchStudent();
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generate();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

    }

    private void searchStudent(){

        final String idEntered = stdid.getText().toString();
        final String nameEntered = sstname.getText().toString();
        Query queryStudent = databaseReference.orderByChild("stdid").equalTo(idEntered);

        queryStudent.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Student studentfound = dataSnapshot.getValue(Student.class);
                gotLink.setText(studentfound.getLink());
                sstname.setText("Student Name: " + studentfound.getStdname());
                sstname.setVisibility(View.VISIBLE);
                Toast.makeText(transfer.this, "found student's link!!", Toast.LENGTH_SHORT).show();
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
        if(TextUtils.isEmpty(gotLink.getText().toString().trim())){
            Toast.makeText(transfer.this, "No data!!", Toast.LENGTH_SHORT).show();
        }
        else {
            String inputVal = gotLink.getText().toString().trim();
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
                    image.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    Toast.makeText(transfer.this, "error occured during code generation..", Toast.LENGTH_SHORT).show();

                }
            } else {

            }
        }

    }

    private void reset(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
