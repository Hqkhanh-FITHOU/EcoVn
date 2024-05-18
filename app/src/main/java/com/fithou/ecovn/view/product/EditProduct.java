package com.fithou.ecovn.view.product;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.R;
import com.fithou.ecovn.view.component.MyProgressDialog;
import com.fithou.ecovn.model.CategoryModel;
import com.fithou.ecovn.model.product.ProductsModel;
import com.fithou.ecovn.view.shop.MyShopActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class EditProduct extends AppCompatActivity {
    private EditText edit_productId, edit_productName, edit_quantity, edit_cost, edit_des;
    private Spinner spin_edit_category, spin_edit_unit, spin_edit_container_type;
    private Button btn_choose_img, btn_edit_confirm, btn_edit_cancel;
    private ImageView img_edit_product;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Uri selectedImageUri;
    private String currentImgURL;
    private static final int IMAGE_PICKER_REQUEST = 1;
    private ProductsModel product;

    private List<String> categoryTitlesList;
    private ArrayList<CategoryModel> categoryModelList;

    private List<String> unitList;

    private List<String> containetTypeList;

    private String productID;
    private String categoryTemp;

    private boolean changeImg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        initViewComponent();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        categoryTitlesList = new ArrayList<>();
        categoryModelList = new ArrayList<>();
        loadContainerType();
        loadUnit();
        loadCategory();
        loadViewData();
        onClickChooseImage();
        onClickConfirmEdit();
        onClickCategory();
        onClickCancel();

        img_edit_product.setOnClickListener(view -> {
            StorageReference storageRef = storage.getReferenceFromUrl(currentImgURL);
            storageRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                        @Override
                        public void onSuccess(StorageMetadata storageMetadata) {
                            String name = storageMetadata.getName();
                            String type = storageMetadata.getContentType();
                            Toast.makeText(EditProduct.this, name+type, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void onClickChooseImage() {
        btn_choose_img.setOnClickListener(view -> {
            // Open image picker
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER_REQUEST);
        });
    }

    private void onClickConfirmEdit() {
        btn_edit_confirm.setOnClickListener(view -> {
            MyProgressDialog progressDialog = new MyProgressDialog(this);
            progressDialog.setTitle("");
            progressDialog.show();
            db = FirebaseFirestore.getInstance();

            String selectedProductType = spin_edit_category.getSelectedItem().toString();
            String productName = edit_productName.getText().toString().trim();
            double cost = Double.parseDouble(edit_cost.getText().toString().trim());
            String description = edit_des.getText().toString().trim();
            int quantity = Integer.parseInt(edit_quantity.getText().toString().trim());
            String unit = spin_edit_unit.getSelectedItem().toString();
            //String category = categorySpinner.getSelectedItem().toString();
            String containertype = spin_edit_container_type.getSelectedItem().toString();

            if (productName.isEmpty() || cost < 0 || description.isEmpty() || selectedProductType.isEmpty()) { /// validate input data
                Toast.makeText(EditProduct.this, "Xin nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if(changeImg){ ///cập nhật lại ảnh nếu đổi ảnh khác
                StorageReference storageRef = storage.getReference().child("product_images").child(selectedImageUri.getLastPathSegment());
                storageRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                db.collection("product").document(productID)
                                        .update(
                                                "name", productName,
                                                "quantity", quantity,
                                                "unit", unit,
                                                "cost", cost,
                                                "des", description,
                                                "container_type", containertype,
                                                "fk_category_id", getIdFromTitle(categoryTemp),
                                                "img", uri.toString()
                                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(EditProduct.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(EditProduct.this, MyShopActivity.class);
                                                setResult(Activity.RESULT_OK, intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(EditProduct.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                                Log.d("Edit Product", "Failure edit", e);
                                            }
                                        });
                            }
                        });
                    }
                });
            }else {
                db.collection("product").document(productID)
                        .update(
                                "name", productName,
                                "quantity", quantity,
                                "unit", unit,
                                "cost", cost,
                                "des", description,
                                "container_type", containertype,
                                "fk_category_id", getIdFromTitle(categoryTemp)
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditProduct.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(EditProduct.this, MyShopActivity.class);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProduct.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                Log.d("Edit Product", "Failure edit", e);
                            }
                        });
            }
        });
    }



    private void onClickCancel() {
        btn_edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Finish the activity
            }
        });
    }

    private void loadViewData() {
        MyProgressDialog progressDialog = new MyProgressDialog(this);
        progressDialog.setTitle("");
        progressDialog.show();
        Intent intent = getIntent();
        productID = intent.getStringExtra("productID");
        edit_productId.setText(productID);
        //Toast.makeText(this, productID, Toast.LENGTH_SHORT).show();
        db = FirebaseFirestore.getInstance();
        db.collection("product").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            edit_productName.setText(doc.getString("name"));
                            edit_quantity.setText(doc.get("quantity")+"");
                            edit_cost.setText(doc.get("cost")+"");
                            edit_des.setText(doc.getString("des"));
                            currentImgURL = doc.getString("img");
                            Glide.with(EditProduct.this).load(currentImgURL).placeholder(R.drawable.logo_login).into(img_edit_product);
                            spin_edit_category.setSelection(Integer.parseInt(doc.getString("fk_category_id"))-1);
                            categoryTemp = spin_edit_category.getSelectedItem().toString();
                            for(int i = 0; i < unitList.size(); ++i){
                                if(unitList.get(i).equalsIgnoreCase(doc.getString("unit"))){
                                    spin_edit_unit.setSelection(i);
                                }
                            }
                            for(int i = 0; i < containetTypeList.size(); ++i){
                                if(containetTypeList.get(i).equalsIgnoreCase(doc.getString("container_type"))){
                                    spin_edit_container_type.setSelection(i);
                                }
                            }
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        finish();
                        Toast.makeText(EditProduct.this, "Failure edit product info", Toast.LENGTH_SHORT).show();
                        Log.d("Edit Product", "Failure edit product info", e);
                    }
                });

    }

    private void initViewComponent() {
        edit_productId = findViewById(R.id.edit_productId);
        edit_productName = findViewById(R.id.edit_productName);
        edit_quantity = findViewById(R.id.edit_quantity);
        edit_cost = findViewById(R.id.edit_cost);
        edit_des = findViewById(R.id.edit_des);
        spin_edit_category = findViewById(R.id.spin_edit_category);
        spin_edit_unit = findViewById(R.id.spin_edit_unit);
        spin_edit_container_type = findViewById(R.id.spin_edit_container_type);
        btn_choose_img = findViewById(R.id.btn_choose_img);
        btn_edit_confirm = findViewById(R.id.btn_edit_confirm);
        btn_edit_cancel = findViewById(R.id.btn_edit_cancel);
        img_edit_product = findViewById(R.id.img_edit_product);
    }

    private void loadContainerType() {
        containetTypeList = new ArrayList<>();
        containetTypeList.add("Túi");
        containetTypeList.add("Bao");
        containetTypeList.add("Tuýp");
        containetTypeList.add("Lọ");
        containetTypeList.add("Vỉ");
        containetTypeList.add("Hộp");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, containetTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_edit_container_type.setAdapter(adapter);
    }

    private void loadUnit() {
        unitList = new ArrayList<>();
        unitList.add("g"); //gam
        unitList.add("kg"); //ki lô gam
        unitList.add("ml"); //mi li lít
        unitList.add("l"); //lít

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_edit_unit.setAdapter(adapter);
    }

    private void loadCategory() {
        db.collection("category")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            CategoryModel category = documentSnapshot.toObject(CategoryModel.class);
                            categoryTitlesList.add(category.getTitle());
                            categoryModelList.add(category);
                        }
                        // Notify adapter when data changes
//                        ((ArrayAdapter) categorySpinner.getAdapter()).notifyDataSetChanged();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProduct.this, android.R.layout.simple_spinner_item, categoryTitlesList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_edit_category.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProduct.this, "Failed to load product types", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String getIdFromTitle(String title) {
        for (CategoryModel categoryModel : categoryModelList) {
            if (categoryModel.getTitle().equals(title)) {
                return categoryModel.getId();
            }
        }
        return null; // Trả về null nếu không tìm thấy title tương ứng
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                img_edit_product.setImageURI(selectedImageUri);
                changeImg = true;
            }
        }
    }

    public void onClickCategory(){
        spin_edit_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryTemp = categoryTitlesList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}