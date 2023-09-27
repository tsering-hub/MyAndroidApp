package com.example.myfirstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapplication.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtFieldUsername, txtFieldName, txtFieldEmail, txtFieldPassword;
    private TextView txtSignIn;
    private Button btnRegister;
    private FirebaseAuth auth;

    private DatabaseReference mRootRef;

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtFieldUsername = findViewById(R.id.txtFieldUsername);
        txtFieldName = findViewById(R.id.txtFieldName);
        txtFieldEmail = findViewById(R.id.txtFieldEmail);
        txtFieldPassword = findViewById(R.id.txtFieldPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtSignIn = findViewById(R.id.txtSignIn);
        auth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        pd = new ProgressDialog(this);

        initOnClickListener();
    }

    private void initOnClickListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isEmptyUsername = Utils.validateTextField(txtFieldUsername, "Username");
                boolean isEmptyName = Utils.validateTextField(txtFieldName, "Name");
                boolean isEmptyEmail = Utils.validateTextField(txtFieldEmail, "Email");
                boolean isEmptyPassword = Utils.validateTextFieldPassword(txtFieldPassword, "Password");

                if (isEmptyUsername && isEmptyName && isEmptyEmail && isEmptyPassword) {
                    String txt_username = txtFieldUsername.getText().toString();
                    String txt_name = txtFieldName.getText().toString();
                    String txt_email = txtFieldEmail.getText().toString();
                    String txt_password = txtFieldPassword.getText().toString();
                    registerUser(txt_username, txt_name, txt_email, txt_password);
                }


            }
        });
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void registerUser(String _username, String _name, String _email, String _password) {
        pd.setMessage("Please Wait!");
        pd.show();
        auth.createUserWithEmailAndPassword(_email, _password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("username", _username);
                map.put("name", _name);
                map.put("email", _email);
                map.put("password", _password);

                mRootRef.child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();

            }
        })


        ;
    }
}