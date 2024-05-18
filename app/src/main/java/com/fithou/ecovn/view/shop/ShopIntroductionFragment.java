package com.fithou.ecovn.view.shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fithou.ecovn.R;
import com.fithou.ecovn.model.product.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ShopIntroductionFragment extends Fragment {
    TextView shop_phone, shop_joining_date, shop_products_count, shop_description;
    String shopID;

    FirebaseFirestore db;
    public ShopIntroductionFragment(String shopID) {
        this.shopID = shopID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_introduction, container, false);
        shop_phone = view.findViewById(R.id.shop_phone);
        shop_joining_date = view.findViewById(R.id.shop_joining_date);
        shop_products_count = view.findViewById(R.id.shop_products_count);
        shop_description = view.findViewById(R.id.shop_description);
        loadViewData(shopID);
        return view;
    }

    private void loadViewData(String s_id) {
        db = FirebaseFirestore.getInstance();

        db.collection("shop").document(s_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            String dateTimeString = (String) doc.get("joining_date");
                            if (dateTimeString != null && !dateTimeString.isEmpty()){
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date dateTime = null;
                                try {
                                    dateTime = dateFormat.parse(dateTimeString);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                shop_joining_date.setText(dateFormat.format(dateTime));
                            }
                            shop_phone.setText((String) doc.get("phone_number"));
                            shop_description.setText((String) doc.get("des"));
                        }
                    }
                });

        db.collection("product").whereEqualTo("fk_shop_id", s_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            shop_products_count.setText(count+"");
                        } else {
                            Log.d("shopintro", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}