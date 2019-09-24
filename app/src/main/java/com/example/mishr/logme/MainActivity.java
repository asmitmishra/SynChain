package com.example.mishr.logme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.Admission:
                Intent admission = new Intent(MainActivity.this,admission.class);
                startActivity(admission);
                return true;

            case R.id.Transfer:
                Intent transfer = new Intent(MainActivity.this, transfer.class);
                startActivity(transfer);
                return true;

            case R.id.Modify:
                Intent modify = new Intent(MainActivity.this, modify.class);
                startActivity(modify);
                return true;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                this.finish();
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
                return true;



            case R.id.add_resource:
                Intent res = new Intent(MainActivity.this, add_resources.class);
                startActivity(res);
                return true;

            case R.id.getResource:
                Intent getres = new Intent(MainActivity.this, getResource.class);
                startActivity(getres);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
