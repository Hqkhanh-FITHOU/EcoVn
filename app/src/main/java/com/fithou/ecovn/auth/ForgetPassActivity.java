package com.fithou.ecovn.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fithou.ecovn.R;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForgetPassActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    EditText edtEmail;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Create your account");
        progressDialog.setMessage("Please wait !");

        onResetPassword();
    }

    public boolean isEmailExists(String email) {
        Boolean isValidEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(email);
        return isValidEmail;
    }


    public void onResetPassword(){
        UserInfo userInfo = FirebaseAuth.getInstance().getCurrentUser();
        btnSave = findViewById(R.id.btn_save);
        edtEmail = findViewById(R.id.edtEmailRegisterForget);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                if(email.isEmpty()){
                    edtEmail.setError("Enter your email");
                }else if(!isEmailExists(email)){
                    edtEmail.setError("Your email not exists, please check again your email !");
                }else {
                    progressDialog.show();
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPassActivity.this, "Please check your email!", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(ForgetPassActivity.this, LoginActivity.class));
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPassActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }
}