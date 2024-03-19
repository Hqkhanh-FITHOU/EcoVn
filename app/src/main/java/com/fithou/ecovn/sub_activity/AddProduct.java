package com.fithou.ecovn.sub_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
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

import com.fithou.ecovn.R;
import com.fithou.ecovn.model.CategoryModel;
import com.fithou.ecovn.model.product.ProductsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AddProduct extends AppCompatActivity {
    private EditText productNameEditText, priceEditText, descriptionEditText;
    private Spinner categorySpinner;
    private Button btnAddProduct, btnCancel, btnAddImage;

    private ImageView imgProduct;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Uri selectedImageUri;
    private static final int IMAGE_PICKER_REQUEST = 1;
    private ProductsModel product;

    private List<String> categoryTitlesList;
    private ArrayList<CategoryModel> categoryModelList;

    private String categoryTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productNameEditText = findViewById(R.id.edt_productName);
        priceEditText = findViewById(R.id.edt_cost);
        descriptionEditText = findViewById(R.id.edt_des);
        categorySpinner = findViewById(R.id.spin_category);
        imgProduct = findViewById(R.id.imgProduct);
        btnAddProduct = findViewById(R.id.btn_add);
        btnCancel = findViewById(R.id.btn_cancel);
        btnAddImage = findViewById(R.id.btn_add_img);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        categoryTitlesList = new ArrayList<>();
        categoryModelList = new ArrayList<>();
        product = new ProductsModel();

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER_REQUEST);
            }
        });

        loadCategory();
        onClickCategory();

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedProductType = categorySpinner.getSelectedItem().toString();
                String productName = productNameEditText.getText().toString().trim();
                double price =Integer.parseInt(priceEditText.getText().toString().trim());
                String description = descriptionEditText.getText().toString().trim();

                if (productName.isEmpty() || price < 0 || description.isEmpty() || selectedProductType.isEmpty() || selectedImageUri == null) {
                    Toast.makeText(AddProduct.this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                StorageReference storageRef = storage.getReference().child("product_images").child(selectedImageUri.getLastPathSegment());
                storageRef.putFile(selectedImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        product.setName(productName);
                                        product.setCost(price);
                                        product.setImg(uri.toString());
                                        product.setDes(description);
                                        product.setFK_category_id(getIdFromTitle(categoryTemp));
                                        db.collection("product")
                                                .add(product)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        String product_id = documentReference.getId();
                                                        product.setProduct_id(product_id);
                                                        documentReference.update("product_id",product_id)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        finish();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(AddProduct.this, "Failed to update product_id", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AddProduct.this, "Failed to add product to Firestore", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Finish the activity
            }
        });
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddProduct.this, android.R.layout.simple_spinner_item, categoryTitlesList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categorySpinner.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProduct.this, "Failed to load product types", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClickCategory(){
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryTemp = categoryTitlesList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                imgProduct.setImageURI(selectedImageUri);
            }
        }
    }
}