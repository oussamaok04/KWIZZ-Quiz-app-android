package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, pass, passConfirmation;
    Button btnRegister;
    TextView toLogin;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.txtEmail);
        name = findViewById(R.id.txtName);
        pass = findViewById(R.id.txtPassword);
        passConfirmation = findViewById(R.id.txtConfirm);
        btnRegister = findViewById(R.id.btnregister);
        toLogin = findViewById(R.id.txtToLogin);


        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") || name.getText().toString().equals("") || pass.getText().toString().equals("") || passConfirmation.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "A Field is Empty or Not Valid ", Toast.LENGTH_SHORT).show();
                } else {
                    if (!pass.getText().toString().equals(passConfirmation.getText().toString())){
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    } else{
                        signUp(email.getText().toString(), pass.getText().toString());
                    }
                }
            }
        });

    }

    public void signUp(String email,String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, ParamActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something is Wrong ! Check the LogCat" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
//    mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//        @Override
//        public void onComplete(@NonNull Task<AuthResult> task) {
//            if (task.isSuccessful()) {
//                // Sign in success, update UI with the signed-in user's information
//                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
//                Log.d("Info :", "User: " + name.getText() +", Password : " + pass.getText(), task.getException());
//                FirebaseUser user = mAuth.getCurrentUser();
//                updateUI(user);
//            }
//        }
//    })

//    if (task.isSuccessful()){
//        Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
//        Log.d("Info :", "User: " + name.getText() +", Password : " + pass.getText(), task.getException());
//    }
}