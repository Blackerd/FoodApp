package com.example.foodorder.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorder.R;
import com.example.foodorder.adapter.AdminFoodListAdapter;
import com.example.foodorder.domain.Foods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class ProductsManagerActivity extends AppCompatActivity {
    private RecyclerView productsView;
    private AdminFoodListAdapter adapter;
    private ArrayList<Foods> foods;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ChipNavigationBar bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_manager);

        // Khởi tạo các view và biến
        initViews();
        initVariables();

        // Cài đặt RecyclerView
        setupRecyclerView();

        // Tải danh sách sản phẩm từ Firebase
        loadProducts();

        // Thiết lập nút điều hướng và thêm sản phẩm
        setupNavigationButtons();
    }

    // Khởi tạo các view và biến
    private void initViews() {
        productsView = findViewById(R.id.productsView);
        bottomNavigationView = findViewById(R.id.bottomMenuAmin);
    }

    // Khởi tạo các biến cần thiết
    private void initVariables() {
        foods = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Foods");
    }

    // Cài đặt RecyclerView và adapter
    private void setupRecyclerView() {
        productsView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminFoodListAdapter(this, foods, new AdminFoodListAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Foods food) {
                showDialog(food);
            }

            @Override
            public void onDeleteClick(Foods food) {
                deleteProduct(food);
            }
        });
        productsView.setAdapter(adapter);
    }

    // Thiết lập nút điều hướng và nút thêm sản phẩm
    private void setupNavigationButtons() {
        ImageView addProductsBtn = findViewById(R.id.addProductsBtn);
        addProductsBtn.setOnClickListener(v -> showDialog(null));

        // Thiết lập bottom navigation
        setupBottomNavigation();
    }

    // Thiết lập bottom navigation
    private void setupBottomNavigation() {
        bottomNavigationView.setItemSelected(R.id.manage_products, true);
        bottomNavigationView.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                startActivity(new Intent(ProductsManagerActivity.this, AdminHomeActivity.class));
            } else if (id == R.id.manage_orders) {
                startActivity(new Intent(ProductsManagerActivity.this, OrderManagerActivity.class));
            } else if (id == R.id.manage_users) {
                startActivity(new Intent(ProductsManagerActivity.this, UsersManagerActivity.class));
            }
        });
    }

    // Tải danh sách sản phẩm từ Firebase
    private void loadProducts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foods.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Foods food = dataSnapshot.getValue(Foods.class);
                    if (food != null) {
                        foods.add(food);
                    }
                }
                adapter.notifyDataSetChanged();
                Log.d("FirebaseData", "Number of items: " + foods.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read data", error.toException());
            }
        });
    }

    // Xóa sản phẩm khỏi Firebase
    private void deleteProduct(Foods food) {
        databaseReference.child(String.valueOf(food.getId())).removeValue((error, ref) -> {
            if (error == null) {
                Toast.makeText(ProductsManagerActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProductsManagerActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hiển thị hộp thoại thêm/sửa sản phẩm
    private void showDialog(Foods food) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        dialogBuilder.setView(dialogView);

        // Ánh xạ các view
        EditText[] editTexts = getEditTexts(dialogView);
        Switch productBestFoodSwitch = dialogView.findViewById(R.id.productBestFoodSwitch);

        // Điền dữ liệu nếu đang sửa sản phẩm
        if (food != null) {
            fillProductData(editTexts, productBestFoodSwitch, food);
        }

        // Xử lý sự kiện khi người dùng chọn ô để thêm hoặc sửa ảnh
        editTexts[5].setOnClickListener(v -> openFileChooser());

        AlertDialog alertDialog = dialogBuilder.create();

        dialogView.findViewById(R.id.saveButton).setOnClickListener(v -> {
            saveProductData(food, editTexts, productBestFoodSwitch);
            alertDialog.dismiss();
        });

        dialogView.findViewById(R.id.cancelButton).setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }

    // Lấy các EditText từ dialog
    private EditText[] getEditTexts(View dialogView) {
        return new EditText[]{
                dialogView.findViewById(R.id.productTitleEditText),
                dialogView.findViewById(R.id.productDescriptionEditText),
                dialogView.findViewById(R.id.productPriceEditText),
                dialogView.findViewById(R.id.productStarEditText),
                dialogView.findViewById(R.id.productTimeEditText),
                dialogView.findViewById(R.id.productImagePathEditText),
                dialogView.findViewById(R.id.productCategoryIdEditText),
                dialogView.findViewById(R.id.productLocationIdEditText),
                dialogView.findViewById(R.id.productPriceIdEditText),
                dialogView.findViewById(R.id.productTimeIdEditText)
        };
    }

    // Điền dữ liệu vào các EditText nếu đang sửa sản phẩm
    private void fillProductData(EditText[] editTexts, Switch productBestFoodSwitch, Foods food) {
        editTexts[0].setText(food.getTitle());
        editTexts[1].setText(food.getDescription());
        editTexts[2].setText(String.valueOf(food.getPrice()));
        editTexts[3].setText(String.valueOf(food.getStar()));
        editTexts[4].setText(String.valueOf(food.getTimeValue()));
        editTexts[5].setText(food.getImagePath());
        editTexts[6].setText(String.valueOf(food.getCategoryId()));
        editTexts[7].setText(String.valueOf(food.getLocationId()));
        editTexts[8].setText(String.valueOf(food.getPriceId()));
        editTexts[9].setText(String.valueOf(food.getTimeId()));
        // Đặt trạng thái của Switch theo giá trị bestFood của sản phẩm
        productBestFoodSwitch.setChecked(food.isBestFood());
    }

    // Lưu dữ liệu sản phẩm vào Firebase
    private void saveProductData(Foods food, EditText[] editTexts, Switch productBestFoodSwitch) {
        String title = editTexts[0].getText().toString();
        String description = editTexts[1].getText().toString();
        double price = Double.parseDouble(editTexts[2].getText().toString());
        double star = Double.parseDouble(editTexts[3].getText().toString());
        int time = Integer.parseInt(editTexts[4].getText().toString());
        String imagePath = editTexts[5].getText().toString();
        int categoryId = Integer.parseInt(editTexts[6].getText().toString());
        int locationId = Integer.parseInt(editTexts[7].getText().toString());
        int priceId = Integer.parseInt(editTexts[8].getText().toString());
        int timeId = Integer.parseInt(editTexts[9].getText().toString());
        boolean bestFood = productBestFoodSwitch.isChecked();

        // Nếu food == null thì đang thêm sản phẩm mới, ngược lại đang sửa sản phẩm
        if (food == null) {
            addNewProduct(title, description, price, star, time, imagePath, categoryId, locationId, priceId, timeId, bestFood);
        } else {
            updateProduct(String.valueOf(food.getId()), title, description, price, star, time, imagePath, categoryId, locationId, priceId, timeId, bestFood);
        }
    }

    // Thêm sản phẩm mới vào Firebase
    private void addNewProduct(String title, String description, double price, double star, int time, String imagePath, int categoryId, int locationId, int priceId, int timeId, boolean bestFood) {
        String id = databaseReference.push().getKey();
        Foods newFood = new Foods();
        newFood.setId(Integer.parseInt(id));
        newFood.setTitle(title);
        newFood.setDescription(description);
        newFood.setPrice(price);
        newFood.setStar(star);
        newFood.setTimeValue(time);
        newFood.setImagePath(imagePath);
        newFood.setCategoryId(categoryId);
        newFood.setLocationId(locationId);
        newFood.setPriceId(priceId);
        newFood.setTimeId(timeId);
        newFood.setBestFood(bestFood);

        if (id != null) {
            databaseReference.child(id).setValue(newFood)
                    .addOnSuccessListener(aVoid -> Toast.makeText(ProductsManagerActivity.this, "Product added", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(ProductsManagerActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show());
        }
    }

    // Cập nhật sản phẩm hiện có trong Firebase
    private void updateProduct(String id, String title, String description, double price, double star, int time, String imagePath, int categoryId, int locationId, int priceId, int timeId, boolean bestFood) {
        databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Foods currentFood = dataSnapshot.getValue(Foods.class);

                if (currentFood != null) {
                    currentFood.setTitle(title);
                    currentFood.setDescription(description);
                    currentFood.setPrice(price);
                    currentFood.setStar(star);
                    currentFood.setTimeValue(time);
                    currentFood.setImagePath(imagePath);
                    currentFood.setCategoryId(categoryId);
                    currentFood.setLocationId(locationId);
                    currentFood.setPriceId(priceId);
                    currentFood.setTimeId(timeId);
                    currentFood.setBestFood(bestFood);

                    databaseReference.child(id).setValue(currentFood)
                            .addOnSuccessListener(aVoid -> Toast.makeText(ProductsManagerActivity.this, "Product updated", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(ProductsManagerActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductsManagerActivity.this, "Failed to retrieve product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Mở hộp thoại chọn tệp
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Xử lý kết quả trả về từ hộp thoại chọn tệp
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Lấy đường dẫn của tệp ảnh từ Intent
            Uri imageUri = data.getData();

            // Tiếp tục xử lý với đường dẫn ảnh được chọn
            handleImageSelection(imageUri);
        }
    }

    // Xử lý đường dẫn ảnh được chọn
    private void handleImageSelection(Uri imageUri) {
        if (imageUri != null) {
            // Lấy đường dẫn tuyệt đối của ảnh từ imageUri
            String imagePath = getAbsolutePath(imageUri);

            // Đưa đường dẫn tuyệt đối của ảnh vào ô nhập liệu
            EditText productImagePathEditText = findViewById(R.id.productImagePathEditText);
            productImagePathEditText.setText(imagePath);

            // Hiển thị ảnh đã chọn lên ImageView bằng Glide
            ImageView imageView = findViewById(R.id.imageFood);
            Glide.with(this).load(imageUri).into(imageView);
        }
    }

    // Lấy đường dẫn tuyệt đối của ảnh từ URI
    private String getAbsolutePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }
}
