package com.example.damien.trainingrecord;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Homepage extends AppCompatActivity {
    ImageView imgAddTraining;
    ImageView imgViewTraining;

    public static final int REQUEST_CODE1 = 1;
    public static final int REQUEST_CODE2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        imgAddTraining = findViewById(R.id.imgAddTraining);
        imgViewTraining = findViewById(R.id.imgViewTraining);

        imgAddTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Homepage.this, AddTraining.class);
                startActivityForResult(i, REQUEST_CODE1);
            }
        });

        imgViewTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Homepage.this, ViewTraining.class);
                startActivityForResult(i, REQUEST_CODE2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE1){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "Add Training successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void btnLogout_onClicked(View view) {
        Intent i = new Intent(Homepage.this, Login.class);
        startActivity(i);
    }
}
