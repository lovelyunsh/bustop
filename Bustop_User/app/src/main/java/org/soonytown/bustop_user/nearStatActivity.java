package org.soonytown.bustop_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class nearStatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_station);

    }

    public void onclick_station1(View view) {
        Intent intent = getIntent();
        Double latitude = intent.getDoubleExtra("latitude",0);
        Double longitude = intent.getDoubleExtra("longitude",0);
        Toast.makeText(nearStatActivity.this, "lat : "+latitude+"long : "+longitude, Toast.LENGTH_LONG).show();

    }
}
