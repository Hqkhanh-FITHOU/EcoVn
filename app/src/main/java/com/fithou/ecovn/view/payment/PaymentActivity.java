package com.fithou.ecovn.view.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.PaymentAdapter;
import com.fithou.ecovn.model.payment.PaymentModel;
import com.fithou.ecovn.model.cart.ExtendProductModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {
    private Spinner paymentSpinner;

    private ArrayList<ExtendProductModel> extendProductModel;

    private PaymentModel paymentModel;

    RecyclerView paymentRecyclerView;

    PaymentAdapter paymentAdapter;

    private FirebaseFirestore firestore;
    private ImageView btn_back;
    private TextView total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        paymentSpinner = findViewById(R.id.payment_spinner);
        btn_back = findViewById(R.id.btn_back);
        total = findViewById(R.id.tv_total_price);
        Intent intent = getIntent();
        extendProductModel = new ArrayList<ExtendProductModel>();
        extendProductModel = getIntent().getParcelableArrayListExtra("extendProductModels");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(PaymentActivity.this,
                R.array.payment_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentSpinner.setAdapter(adapter);

        paymentAdapter = new PaymentAdapter(PaymentActivity.this,paymentModel, extendProductModel,total );
        paymentRecyclerView = findViewById(R.id.payment_list);

        LinearLayoutManager cartLayoutManager = new LinearLayoutManager(PaymentActivity.this, RecyclerView.VERTICAL, false);
        paymentRecyclerView.setLayoutManager(cartLayoutManager);
        paymentRecyclerView.setAdapter(paymentAdapter);
        paymentAdapter.setViewData(paymentModel,extendProductModel);
        paymentAdapter.notifyDataSetChanged();
        onClickBack();
    }


    private void onClickTypePayment(){

    }

    private void onClickBack(){
        btn_back.setOnClickListener(v -> {
            finish();
        });
    }
}