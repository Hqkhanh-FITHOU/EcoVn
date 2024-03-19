package com.fithou.ecovn.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fithou.ecovn.R;
import com.fithou.ecovn.model.authModels;
import com.fithou.ecovn.model.product.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private authModels user;
    private String avatarPath = "";

    FirebaseFirestore db;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        Uri uri = Uri.parse(getAvatarComment(comment.getUser_id()));
        holder.img_user_comment.setImageURI(uri);
        holder.user_name_comment.setText(user.getName());
        holder.rating_comment.setRating(comment.getStar());
        holder.content_comment.setText(comment.getContent());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        holder.time_comment.setText(simpleDateFormat.format(comment.getDateTime()));
    }

    private String getAvatarComment(String uid) {
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(authModels.class);
                if(user != null){
                    avatarPath = user.getImage();
                }
            }
        });
        return avatarPath;
    }

    @Override
    public int getItemCount() {
        if(comments != null){
            return comments.size();
        }
        return 0;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_user_comment;
        TextView user_name_comment;
        RatingBar rating_comment;
        TextView content_comment;
        TextView time_comment;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            img_user_comment = itemView.findViewById(R.id.img_user_comment);
            user_name_comment = itemView.findViewById(R.id.user_name_comment);
            rating_comment = itemView.findViewById(R.id.rating_comment);
            content_comment = itemView.findViewById(R.id.content_comment);
            time_comment = itemView.findViewById(R.id.time_comment);
        }

    }
}
