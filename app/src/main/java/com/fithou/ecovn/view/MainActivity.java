package com.fithou.ecovn.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.fithou.ecovn.R;
import com.fithou.ecovn.model.authModels;
import com.fithou.ecovn.adapter.ViewPagerAdapter;
import com.fithou.ecovn.view.dashboard.AccountMenuFragment;
import com.fithou.ecovn.view.dashboard.CartMenuFagment;
import com.fithou.ecovn.view.dashboard.HomeMenuFragment;
import com.fithou.ecovn.view.dashboard.NotificationMenuFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.fithou.ecovn.R.id;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
//    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    public BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    FirebaseFirestore db;
    public static authModels CURRENT_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setUserInputEnabled(false);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        bottomNavigationView = findViewById(R.id.bottomNav);
        frameLayout = findViewById(R.id.frameLayout);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                viewPager2.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                switch (item.getItemId()){
                    case id.bottom_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeMenuFragment()).commit();
                        return true;
                    case id.bottom_cart:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CartMenuFagment()).commit();
                        return true;
                    case id.bottom_notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new NotificationMenuFragment()).commit();
                        return true;
                    case id.bottom_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AccountMenuFragment()).commit();
                        return true;
                }
                return false;
            }
        });
    }

    private void checkCartExist() {
        db = FirebaseFirestore.getInstance();
        db.collection("cart").whereEqualTo("user_id",CURRENT_USER.getId()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            //Toast.makeText(MainActivity.this, "Cart is not exist", Toast.LENGTH_SHORT).show();
                            createCartOnFireStore();
                        }else{
                            //Toast.makeText(MainActivity.this, "Cart is exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createCartOnFireStore() {
        db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", CURRENT_USER.getId());
        data.put("cart_id","");
        data.put("product", new ArrayList<>());
        data.put("total", "");
        db.collection("cart").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                db.collection("cart").document(documentReference.getId()).update("cart_id", documentReference.getId()).addOnSuccessListener(unused -> {
                    //Toast.makeText(MainActivity.this, "Cart is exist", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void checkShop(authModels user){
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            user.setShop(doc.getBoolean("shop"));
                        }
                    }
                });
    }

    public void setBottomNavigationSelectedItem(int position){
        switch (position) {
            case 0: {
                bottomNavigationView.getMenu().findItem(id.bottom_home).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeMenuFragment()).commit();
                break;
            }
            case 1: {
                bottomNavigationView.getMenu().findItem(id.bottom_cart).setChecked(true);
                break;
            }
            case 2: {
                bottomNavigationView.getMenu().findItem(id.bottom_notification).setChecked(true);
                break;
            }
            case 3: {
                bottomNavigationView.getMenu().findItem(id.bottom_account).setChecked(true);
                break;
            }
        }
        bottomNavigationView.setSelectedItemId(position);

        Log.w("Selected", "Navigation: "+position);
    }
}