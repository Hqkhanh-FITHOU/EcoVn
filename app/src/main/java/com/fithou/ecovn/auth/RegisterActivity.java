package com.fithou.ecovn.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fithou.ecovn.R;
import com.fithou.ecovn.model.authModels;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    Button btn_login_again;
    ImageView btn_register;

    EditText edtName, edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Create your account");
        progressDialog.setMessage("Please wait !");

        btn_login_again = findViewById(R.id.btn_login_again);
        btn_register = findViewById(R.id.btn_register);
        edtName = findViewById(R.id.edtNameRegister);
        edtEmail = findViewById(R.id.edtEmailRegister);
        edtPassword = findViewById(R.id.edtPasswordRegister);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if(name.isEmpty()){
                    edtName.setError("Enter your name");

                }else if(email.isEmpty()){
                    edtEmail.setError("Enter your email");
                }else if(password.isEmpty()){
                    edtPassword.setError("Enter your password");
                }else{
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String id = task.getResult().getUser().getUid();
                                authModels authModels = new authModels(id, name, email, password);

                                DocumentReference documentReference =   firestore.collection("users").document();

                                documentReference.set(authModels, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                           documentReference.set(authModels, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   progressDialog.dismiss();
                                                   Toast.makeText(RegisterActivity.this, "Register successfully !", Toast.LENGTH_SHORT).show();
                                                   startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                               }
                                           }).addOnFailureListener(new OnFailureListener() {
                                               @Override
                                               public void onFailure(@NonNull Exception e) {
                                                   progressDialog.dismiss();
                                                   Toast.makeText(RegisterActivity.this, "Register failure !", Toast.LENGTH_LONG).show();
                                               }
                                           });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Register failure !", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

        btn_login_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}