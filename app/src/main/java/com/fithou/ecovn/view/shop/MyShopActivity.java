package com.fithou.ecovn.view.shop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.R;
import com.fithou.ecovn.adapter.MyShopTabAdapter;
import com.fithou.ecovn.view.product.AddProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyShopActivity extends AppCompatActivity {

    private ViewPager2 my_shop_viewpager;
    private TabLayout my_shop_tablayout;
    private Toolbar my_shop_toolbar;
    private ImageView my_shop_img_certificate_business, my_shop_img, btn_back_my_shop;
    private TextView my_shop_name, phone_number;
    private Button btn_follow;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private MyShopTabAdapter tabAdapter;

    public static String SHOP_ID = "";
    FirebaseFirestore db;
    Fragment fragment = null;

    private ActivityResultLauncher<Intent> launcher  = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    tabAdapter.reloadFragmentView(1);
                }
            });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store);

        initViewComponent();
        my_shop_toolbar.setTitle("");
        setSupportActionBar(my_shop_toolbar);


        Intent intent = getIntent();
        SHOP_ID = intent.getStringExtra("shop_id");

        tabAdapter = new MyShopTabAdapter(this, SHOP_ID);
        my_shop_viewpager.setAdapter(tabAdapter);

        new TabLayoutMediator(my_shop_tablayout, my_shop_viewpager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Giới thiệu");
                    break;
                case 1:
                    tab.setText("Sản phẩm");
                    break;
            }
        }).attach();

        loadViewData(intent.getStringExtra("shop_id"));
        clickBackButton();

        setAppbarAnimation();
    }



    private void setAppbarAnimation() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_login);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(MyShopActivity.this,R.color.white ));
                collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(MyShopActivity.this,R.color.Sauvignon_red));
            }
        });
    }

    private void loadViewData(String shop_id) {
        db = FirebaseFirestore.getInstance();

        db.collection("shop").document(shop_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            //my_shop_name.setText((String) doc.get("name"));
                            //phone_number.setText((String) doc.get("phone_number"));

//                            Glide.with(MyShopActivity.this)
//                                    .load(doc.get("img_shop") == "" ? R.mipmap.ic_launcher : doc.get("img_shop"))
//                                    .into(my_shop_img);
                        }
                    }
                });
    }

    private void clickBackButton() {
        btn_back_my_shop.setOnClickListener(view -> {
            finish();
        });
    }

    private void initViewComponent() {
        my_shop_toolbar = findViewById(R.id.my_shop_toolbar);
        my_shop_viewpager = findViewById(R.id.my_shop_viewpager);
        my_shop_tablayout = findViewById(R.id.my_shop_tablayout);
        my_shop_toolbar = findViewById(R.id.my_shop_toolbar);
        my_shop_img_certificate_business = findViewById(R.id.my_shop_img_certificate_business);
        //my_shop_img = findViewById(R.id.my_shop_img);
        //my_shop_name = findViewById(R.id.my_shop_name);
        phone_number = findViewById(R.id.phone_number);
        btn_back_my_shop = findViewById(R.id.btn_back_my_shop);
        collapsingToolbarLayout = findViewById(R.id.my_shop_collapsingtoobarlayout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_shop_appbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.change_info_shop: {
                Toast.makeText(this, "Change shop info", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.add_product: {
//                Toast.makeText(this, "Add product", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyShopActivity.this, AddProduct.class);
                launcher.launch(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}