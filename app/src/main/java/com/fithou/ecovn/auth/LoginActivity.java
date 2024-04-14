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
import android.widget.TextView;
import android.widget.Toast;
import com.fithou.ecovn.model.authModels;
import com.fithou.ecovn.MainActivity;
import com.fithou.ecovn.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity {

    Button btn_create_new;
    TextView tv_forget_pass;

    Button btn_login;
    FirebaseAuth auth;
    authModels user = new authModels();
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    EditText edtEmail, edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đăng nhập");
        progressDialog.setMessage("Đang thực hiện !");

        btn_login = findViewById(R.id.btn_login);
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if(email.isEmpty()){
                    edtEmail.setError("Hãy nhập email hợp lệ");

                }else if(password.isEmpty()){
                    edtPassword.setError("Hãy nhập mật khẩu hợp lệ");
                }else{
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
//                                user.setId(auth.getCurrentUser().getUid());
//                                user.setName(auth.getCurrentUser().getDisplayName());
//                                user.setEmail(auth.getCurrentUser().getEmail());
//                                user.setPassword(password);
                                firestore.collection("users")
                                        .document(auth.getCurrentUser().getUid())
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    DocumentSnapshot doc = task.getResult();
                                                    user.setId((String) doc.get("id"));
                                                    user.setName((String)doc.get("name"));
                                                    user.setEmail((String)doc.get("email"));
                                                    user.setPassword((String)doc.get("password"));
                                                    user.setDate_of_birth((String) doc.get("date_of_birth"));
                                                    user.setImage((String) doc.get("image"));
                                                    user.setShop(doc.getBoolean("is_shop"));
                                                    user.setPhone((String) doc.get("phone"));
                                                    user.setGender(doc.getBoolean("gender"));
                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                    intent.putExtra("user", user);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });

                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });




        btn_create_new = findViewById(R.id.btn_new_register);
        tv_forget_pass = findViewById(R.id.tvForgetPass);


        btn_create_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        tv_forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(i);
            }
        });
    }


}