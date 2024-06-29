package com.example.foodorder.adapter;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.domain.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<Users> users;
    private OnUserActionListener listener;

    public UserAdapter(List<Users> users, OnUserActionListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = users.get(position);
        holder.userNameTv.setText(user.getName());
        holder.emailTv.setText(user.getEmail());
        holder.roleTv.setText(user.isAdmin() ? "Admin" : "User");
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userNameTv, emailTv, roleTv;
        private ImageView deleteIv, editIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTv = itemView.findViewById(R.id.userNameTextView);
            emailTv = itemView.findViewById(R.id.userEmailTextView);
            roleTv = itemView.findViewById(R.id.userRoleTextView);
            deleteIv = itemView.findViewById(R.id.deleteBtn);
            editIv = itemView.findViewById(R.id.editBtn);

            deleteIv.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteUser(users.get(position), position);
                }
            });

            editIv.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onEditUser(users.get(position));
                }
            });


        }
    }

    public interface OnUserActionListener {
        void onDeleteUser(Users user, int position);
        void onEditUser(Users user);
    }
}
