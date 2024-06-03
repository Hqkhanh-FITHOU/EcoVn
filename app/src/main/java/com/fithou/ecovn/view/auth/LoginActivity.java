package com.fithou.ecovn.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fithou.ecovn.model.user.authModels;
import com.fithou.ecovn.view.MainActivity;
import com.fithou.ecovn.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static int LOGIN_OK = 12;
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
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if(email.isEmpty()){
                    edtEmail.setError("Hãy nhập email hợp lệ");

                }else if(password.isEmpty()){
                    edtPassword.setError("Hãy nhập mật khẩu hợp lệ");
                }else{
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            user.setId(auth.getCurrentUser().getUid());

                            firestore.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()){
                                    DocumentSnapshot doc = task1.getResult();
                                    user.setName(doc.getString("name"));
                                    user.setEmail(doc.getString("email"));
                                    user.setPassword(doc.getString("password"));
                                    user.setAddress(doc.getString("address"));
                                    user.setGender(doc.getBoolean("gender"));
                                    user.setPhone(doc.getString("phone"));
                                    user.setImage(doc.getString("image"));
                                    user.setShop(doc.getBoolean("shop"));
                                    MainActivity.CURRENT_USER = user;
                                    checkCartExist();
                                    progressDialog.dismiss();

                                    setResult(LOGIN_OK);
                                    finish();
                                }
                            });

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn_create_new = findViewById(R.id.btn_new_register);
        tv_forget_pass = findViewById(R.id.tvForgetPass);


        btn_create_new.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        tv_forget_pass.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, ForgetPassActivity.class);
            startActivity(i);
        });
    }

    private void checkCartExist() {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("cart").whereEqualTo("user_id",MainActivity.CURRENT_USER.getId()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()){
                        createCartOnFireStore();
                    }
                });
    }

    private void createCartOnFireStore() {
        firestore = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", MainActivity.CURRENT_USER.getId());
        data.put("cart_id","");
        data.put("product", new ArrayList<>());
        data.put("total", "");
        firestore.collection("cart").add(data).addOnSuccessListener(documentReference -> firestore.collection("cart").document(documentReference.getId()).update("cart_id", documentReference.getId()).addOnSuccessListener(unused -> {
            Log.d("MainActivity", "shop created");
        }));
    }


}