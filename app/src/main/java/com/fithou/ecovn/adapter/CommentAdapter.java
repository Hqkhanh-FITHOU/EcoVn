package com.fithou.ecovn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fithou.ecovn.R;
import com.fithou.ecovn.model.user.authModels;
import com.fithou.ecovn.model.product.Comment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private authModels user;
    private Context context;

    FirebaseFirestore db;


    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    public CommentAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
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
        loadAvatarAndImage(comment.getUser_id(),holder);

        holder.rating_comment.setRating(comment.getStar());
        holder.content_comment.setText(comment.getContent());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        holder.time_comment.setText(simpleDateFormat.format(comment.getDateTime()));
    }

    private void loadAvatarAndImage(String uid, CommentViewHolder holder) {
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(authModels.class);
                if(user != null){
                    Glide.with(context).load(user.getImage()).into(holder.img_user_comment);
                    holder.user_name_comment.setText(user.getName());
                }
            }
        });
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
