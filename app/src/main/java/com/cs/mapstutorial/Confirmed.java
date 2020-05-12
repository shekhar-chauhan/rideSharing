package com.cs.mapstutorial;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Confirmed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed);

        Bundle bundle;
        bundle = getIntent().getExtras();

        String addressC = bundle.getString("addressC");
        String addressD = bundle.getString("addressD");

        EditText currentPostalAddress = (EditText)findViewById(R.id.currentPostalAddress);
        EditText destinationPostalAddress = (EditText)findViewById(R.id.destinationPostalAddress);

        currentPostalAddress.setText(addressC);
        destinationPostalAddress.setText(addressD);
    }
}