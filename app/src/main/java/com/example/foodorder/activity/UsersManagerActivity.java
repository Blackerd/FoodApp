package com.example.foodorder.activity;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.R;
import com.example.foodorder.adapter.UserAdapter;
import com.example.foodorder.databinding.ActivityUsersManagerBinding;
import com.example.foodorder.domain.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class UsersManagerActivity extends AppCompatActivity implements UserAdapter.OnUserActionListener {
    private ActivityUsersManagerBinding binding;
    private List<Users> usersList;
    private UserAdapter userAdapter;
    private DatabaseReference databaseReference;
    private ChipNavigationBar bottomNavigationView;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo các biến cần thiết
        initVariables();
        // Thiết lập RecyclerView và adapter
        setupRecyclerView();
        // Lấy dữ liệu người dùng từ Firebase Realtime Database
        fetchUsers();
        // Thiết lập thanh điều hướng dưới cùng
        setupBottomNavigation();
        // Thiết lập sự kiện click cho nút thêm người dùng
        setupAddUserButton();
    }

    // Khởi tạo các biến cần thiết
    private void initVariables() {
        usersList = new ArrayList<>();
        // Tham chiếu đến node Users trong Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        // Khởi tạo adapter
        userAdapter = new UserAdapter(usersList, this);
    }

    // Thiết lập RecyclerView và adapter
    private void setupRecyclerView() {
        binding.userView.setLayoutManager(new LinearLayoutManager(this));
        binding.userView.setAdapter(userAdapter);
    }

    // Thiết lập thanh điều hướng dưới cùng
    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomMenuAmin);
        bottomNavigationView.setItemSelected(R.id.manage_users, true);

        bottomNavigationView.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                startActivity(new Intent(UsersManagerActivity.this, AdminHomeActivity.class));
            } else if (id == R.id.manage_orders) {
                startActivity(new Intent(UsersManagerActivity.this, OrderManagerActivity.class));
            } else if (id == R.id.manage_products) {
                startActivity(new Intent(UsersManagerActivity.this, ProductsManagerActivity.class));
            } else if (id == R.id.manage_users) {
            }
        });
    }

    // Lấy dữ liệu người dùng từ Firebase Realtime Database
    private void fetchUsers() {
        // Lắng nghe sự thay đổi dữ liệu trong node Users
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Xóa dữ liệu cũ
                usersList.clear();
                // Duyệt qua từng phần tử trong dataSnapshot để lấy dữ liệu
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    if (user != null) {
                        // Thêm người dùng vào danh sách
                        usersList.add(user);
                    }
                }
                // Cập nhật dữ liệu mới nhất lên RecyclerView
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UsersManagerActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Xử lý khi người dùng muốn xóa người dùng
    @Override
    public void onDeleteUser(Users user, int position) {
        // Xóa người dùng khỏi Firebase Realtime Database
        databaseReference.child(String.valueOf(user.getId())).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Kiểm tra nếu position hợp lệ trước khi xóa
                        if (position >= 0 && position < usersList.size()) {
                            usersList.remove(position);
                            userAdapter.notifyItemRemoved(position);
                            Toast.makeText(UsersManagerActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UsersManagerActivity.this, "Không thể xóa người dùng", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UsersManagerActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Xử lý khi người dùng muốn chỉnh sửa thông tin người dùng
    @Override
    public void onEditUser(Users user) {
        showUserDialog(user, false);
    }
    // Thiết lập sự kiện click cho nút thêm người dùng
    private void setupAddUserButton() {
        ImageView addUserBtn = findViewById(R.id.addUserBtn);
        addUserBtn.setOnClickListener(v -> showUserDialog(new Users(), true));
    }
    // Hiển thị dialog để thêm hoặc chỉnh sửa thông tin người dùng
    private void showUserDialog(Users user, boolean isAdding) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_edit_user);

        EditText userNameEditText = dialog.findViewById(R.id.userNameEdt);
        EditText userEmailEditText = dialog.findViewById(R.id.userEmailEdt);
        Spinner userRoleSpinner = dialog.findViewById(R.id.userRoleSpinner);
        EditText userPasswordEditText = dialog.findViewById(R.id.userPasswordEdt);
        Button saveButton = dialog.findViewById(R.id.saveButton);

        userNameEditText.setText(user.getName());
        userEmailEditText.setText(user.getEmail());

        // Thiết lập dữ liệu cho Spinner Vai trò (Admin hoặc User)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userRoleSpinner.setAdapter(adapter);
        userRoleSpinner.setSelection(user.isAdmin() ? 0 : 1); // Chọn Admin hoặc User dựa trên user.isAdmin()

        if (isAdding) {
            userPasswordEditText.setVisibility(View.VISIBLE); // Hiển thị ô mật khẩu
            userEmailEditText.setVisibility(View.VISIBLE); // Hiển thị ô email
        } else {
            userPasswordEditText.setVisibility(View.GONE); // Ẩn ô mật khẩu
            userEmailEditText.setVisibility(View.GONE); // Ẩn ô email
        }

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Xử lý khi người dùng nhấn nút "Lưu"
        saveButton.setOnClickListener(v -> {
            if (isAdding) {
                addUserInformation(dialog, userNameEditText, userEmailEditText, userRoleSpinner, userPasswordEditText);
            } else {
                updateUserInformation(dialog, user, userNameEditText, userEmailEditText, userRoleSpinner);
            }
        });

        dialog.show();
    }
    // Thêm thông tin người dùng vào Firebase
    private void addUserInformation(Dialog dialog, EditText userNameEditText, EditText userEmailEditText, Spinner userRoleSpinner, EditText userPasswordEditText) {
        auth = FirebaseAuth.getInstance();
        String name = userNameEditText.getText().toString().trim();
        String email = userEmailEditText.getText().toString().trim();
        String role = userRoleSpinner.getSelectedItem().toString(); // Lấy vai trò từ Spinner
        String password = userPasswordEditText.getText().toString().trim();

        // Kiểm tra các trường thông tin có rỗng không
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(UsersManagerActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra FirebaseAuth đã khởi tạo chưa
        if (FirebaseAuth.getInstance() == null) {
            Log.e(TAG, "FirebaseAuth instance is null");
            return;
        }

        // Tạo đối tượng người dùng mới
        Users newUser = new Users(name, email, role.equals("Admin"), password);



        // Đăng ký người dùng trong Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            // Lưu người dùng vào Firebase Realtime Database
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int newId = 1; // Gán mặc định là 1 nếu không có dữ liệu

                                    // Tìm id lớn nhất hiện có để tạo id mới
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Users existingUser = snapshot.getValue(Users.class);
                                        if (existingUser != null && existingUser.getId() >= newId) {
                                            newId = existingUser.getId() + 1;
                                        }
                                    }

                                    newUser.setId(newId); // Đặt id cho người dùng mới

                                    // Lưu người dùng mới vào Firebase Realtime Database
                                    databaseReference.child(String.valueOf(newId)).setValue(newUser)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    usersList.add(newUser);
                                                    userAdapter.notifyDataSetChanged();
                                                    dialog.dismiss();
                                                    Toast.makeText(UsersManagerActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(UsersManagerActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(UsersManagerActivity.this, "Lỗi khi tạo id người dùng mới", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Log.e(TAG, "Registration failed: " + task.getException().getMessage());
                        Toast.makeText(UsersManagerActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    // Cập nhật thông tin người dùng trong Firebase
    private void updateUserInformation(Dialog dialog, Users user, EditText userNameEditText, EditText userEmailEditText, Spinner userRoleSpinner) {
        String newUserName = userNameEditText.getText().toString();
        String newUserEmail = userEmailEditText.getText().toString();
        String newRole = userRoleSpinner.getSelectedItem().toString(); // Lấy vai trò từ Spinner

        DatabaseReference userRef = databaseReference.child(String.valueOf(user.getId()));

        // Cập nhật thông tin trong Firebase Realtime Database
        userRef.child("name").setValue(newUserName);
        userRef.child("email").setValue(newUserEmail);
        userRef.child("admin").setValue(newRole.equals("Admin")).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Cập nhật thông tin trong Firebase Authentication
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    firebaseUser.updateEmail(newUserEmail)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    // Cập nhật thành công
                                    updateUserList(user, newUserName, newUserEmail, newRole.equals("Admin"));
                                    dialog.dismiss();
                                    Toast.makeText(UsersManagerActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Cập nhật thất bại
                                    Toast.makeText(UsersManagerActivity.this, "Cập nhật thất bại: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else {
                Toast.makeText(UsersManagerActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Cập nhật danh sách người dùng sau khi chỉnh sửa
    private void updateUserList(Users user, String newUserName, String newUserEmail, boolean isAdmin) {
        // Duyệt qua danh sách người dùng để tìm người dùng cần cập nhật
        for (int i = 0; i < usersList.size(); i++) {
            if (usersList.get(i).getId() == user.getId()) {
                usersList.get(i).setName(newUserName);
                usersList.get(i).setEmail(newUserEmail);
                usersList.get(i).setAdmin(isAdmin);
                userAdapter.notifyItemChanged(i);
                break;
            }
        }
    }
}
