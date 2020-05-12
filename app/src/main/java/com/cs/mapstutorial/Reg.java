package com.cs.mapstutorial;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Reg extends AppCompatActivity {

    EditText editTextName, editTextPhNumber, editTextAadhar, editTextAddress;
    Button buttonReg;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        editTextName = findViewById(R.id.editTextName);
        editTextPhNumber = findViewById(R.id.editTextPhNumber);
        editTextAadhar = findViewById(R.id.editTextAadhar);
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonReg = findViewById(R.id.buttonReg);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUsers();
            }
        });
    }

    private void addUsers() {

        String name = editTextName.getText().toString().trim();
        String phnumber = editTextPhNumber.getText().toString().trim();
        String aadhar = editTextAadhar.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phnumber) && !TextUtils.isEmpty(aadhar) && !TextUtils.isEmpty(address)) {
            String id = databaseReference.push().getKey();
            UserInfo userInfo = new UserInfo(id, name, phnumber, aadhar, address);
            databaseReference.child(id).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getBaseContext(), "Registered", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Data Not Entered", Toast.LENGTH_SHORT).show();
        }
    }
}