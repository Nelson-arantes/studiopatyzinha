package com.project.studiopatyzinha.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.MainActivity;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Opinionpattern;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class OpinionAdapter extends RecyclerView.Adapter<OpinionAdapter.MeuViewHolder> {
    ArrayList<Opinionpattern> comments;
    View view;

    public OpinionAdapter(ArrayList<Opinionpattern> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, null, false);
        return new MeuViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MeuViewHolder holder, int position) {
        Opinionpattern comment = comments.get(position);
        if (comment.getOpinion().isEmpty()) {
            holder.comment.setVisibility(View.GONE);
        } else {
            holder.comment.setText(comment.getOpinion());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(comment.getDate()));
        String date = "";
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
            date += "0";
        }
        date += calendar.get(Calendar.DAY_OF_MONTH) + " de " + MainActivity.meses.get(calendar.get(Calendar.MONTH));
        if (calendar.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR)) {
            date += " de " + calendar.get(Calendar.YEAR);
        }
        if (!comment.getGrade().isEmpty()) {
            holder.grade_comment.setText(comment.getGrade());
            holder.grade_comment.setVisibility(View.VISIBLE);
        }
        holder.date.setText(date);
        holder.trash.setVisibility(View.GONE);
//        if (comment.getIduser().equals(Login.pessoa.getId())) {
//            holder.trash.setVisibility(View.VISIBLE);
//        } else {
//            holder.trash.setVisibility(View.GONE);
//        }
//        holder.trash.setOnClickListener(v -> {
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            comments.remove(holder.getAdapterPosition());
//            Comentar.post.setComentarios(comments);
//            Comentar.post.setCommentsQuant(comments.size()+"");
//            db.collection("Blog").document(Login.blogstring).set(Comentar.post);
//            notifyDataSetChanged();
//        });
        holder.nome_user.setText(comment.getNomeuser());
        Glide.with(view).load(comment.getFtuser()).into(holder.photo_user);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        CircleImageView photo_user;
        TextView nome_user, date;
        ImageView trash;
        TextView comment, grade_comment;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            photo_user = itemView.findViewById(R.id.photo_user_post);
            nome_user = itemView.findViewById(R.id.nameUser_post);
            date = itemView.findViewById(R.id.date_post);
            comment = itemView.findViewById(R.id.txt_comment);
            grade_comment = itemView.findViewById(R.id.grade_comment);
            trash = itemView.findViewById(R.id.trashcomment);

        }
    }
}
