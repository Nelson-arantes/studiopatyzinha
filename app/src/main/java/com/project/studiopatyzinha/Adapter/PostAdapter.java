package com.project.studiopatyzinha.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.project.studiopatyzinha.Comentar;
import com.project.studiopatyzinha.Login;
import com.project.studiopatyzinha.MainActivity;
import com.project.studiopatyzinha.Perfilbarber;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.PostPattern;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MeuViewHolder> implements Filterable {
    public static ArrayList<PostPattern> postsAll;
    ArrayList<PostPattern> posts;

    Context context;
    boolean ifgotoperfil;

    public PostAdapter(ArrayList<PostPattern> posts, Context context, boolean ifgotoperfil) {
        this.postsAll = new ArrayList<>(posts);
        this.posts = posts;
        this.ifgotoperfil = ifgotoperfil;
        this.context = context;
    }

    @NonNull
    @Override
    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.postpattern, null, false);
        return new MeuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolder holder, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        PostPattern post = posts.get(position);
        Glide.with(context).load(post.getPessoa().getImgUri()).into(holder.photo_user_post);
        if (!post.getPhoto().equals("")) {
            Glide.with(context).load(post.getPhoto()).override(3200, 1500).into(holder.photo);
            holder.photo.setVisibility(View.VISIBLE);
        } else {
            holder.photo.setVisibility(View.GONE);
        }
        holder.nome_user.setText(post.getPessoa().getNome());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(post.getDate()));
        String date = "";
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
            date += "0";
        }
        date += calendar.get(Calendar.DAY_OF_MONTH) + " de " + MainActivity.meses.get(calendar.get(Calendar.MONTH));
        holder.date.setText(date);
        holder.curtir.setOnClickListener(v -> {
            if (post.getLikesids().containsKey(Login.pessoa.getId())) {
                post.setLikesquants("" + (Integer.parseInt(post.getLikesquants()) - 1));
                holder.curtir.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_favorite));
                post.getLikesids().remove(Login.pessoa.getId());
            } else {
                post.setLikesquants("" + (Integer.parseInt(post.getLikesquants()) + 1));
                holder.curtir.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_favorite_full));
                post.getLikesids().put(Login.pessoa.getId(), "");
            }
            db.collection("Blog").document(post.getId()).set(post);
            notifyDataSetChanged();
        });
        holder.comentar.setOnClickListener(v -> {
            Intent s = new Intent(context, Comentar.class);
            Login.blogstring = post.getId();
            s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(s);
        });
        if (post.getLikesids().containsKey(Login.pessoa.getId())) {
            holder.curtir.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_favorite_full));
        } else {
            holder.curtir.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_favorite));

        }
        holder.quantlikes.setText(post.getLikesquants() + " Likes");
        holder.commentsquant.setText(post.getCommentsQuant() + " Comentários");
        holder.texto.setText(post.getDesc());
        if (Login.pessoa.getBarbersposts().containsKey(post.getPessoa().getId())) {
            holder.followbnt_post.setText("Parar de seguir");
        } else {
            holder.followbnt_post.setText("Seguir ");
        }
        if (post.getPessoa().getId().equals(Login.pessoa.getId())) {
            holder.followbnt_post.setVisibility(View.GONE);
            holder.menu.setVisibility(View.VISIBLE);

        } else {
            holder.followbnt_post.setVisibility(View.VISIBLE);
            holder.menu.setVisibility(View.GONE);

        }
        holder.menu.setOnClickListener(v -> {
            holder.showMoreOptions(holder.menu);
        });
        holder.followbnt_post.setOnClickListener(v -> {
            if (Login.pessoa.getBarbersposts().containsKey(post.getPessoa().getId())) {
                Login.pessoa.getBarbersposts().remove(post.getPessoa().getId());
            } else {
                Login.pessoa.getBarbersposts().put(post.getPessoa().getId(), "");
            }
            db.collection("Contas").document(Login.pessoa.getId()).set(Login.pessoa);
            notifyDataSetChanged();
        });
        String a = Integer.parseInt(post.getViews()) + 1 + "";
        post.setViews(a);
        db.collection("Blog").document(post.getId()).set(post);
        if (ifgotoperfil) {
            holder.perfilbarber.setOnClickListener(v -> {
                Login.blogstring = post.getPessoa().getId();
                context.startActivity(new Intent(context, Perfilbarber.class));
            });
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            //run on back
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<PostPattern> filteredList = new ArrayList<>();

                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(postsAll);
                } else {
                    for (int i = 0; i < postsAll.size(); i++) {
                        if (postsAll.get(i).getTag().toLowerCase().contains(constraint.toString().toLowerCase()) || postsAll.get(i).getDesc().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(postsAll.get(i));
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            //run on ui thread
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                posts.clear();
                posts.addAll((Collection<? extends PostPattern>) results.values);
                notifyDataSetChanged();
            }
        };

        return filter;
    }


    public class MeuViewHolder extends RecyclerView.ViewHolder {
        CircleImageView photo_user_post;
        TextView nome_user, date, texto, quantlikes, commentsquant;
        ImageView curtir, comentar, photo, menu;
        Button followbnt_post;
        LinearLayout perfilbarber;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            perfilbarber = itemView.findViewById(R.id.linearLayout2_post);
            followbnt_post = itemView.findViewById(R.id.followbnt_post);
            photo_user_post = itemView.findViewById(R.id.photo_user_post);
            date = itemView.findViewById(R.id.date_post);
            menu = itemView.findViewById(R.id.menu_post);
            texto = itemView.findViewById(R.id.txt_post);
            photo = itemView.findViewById(R.id.photo_post);
            curtir = itemView.findViewById(R.id.likebnt_post);
            comentar = itemView.findViewById(R.id.comentarbnt_post);
            nome_user = itemView.findViewById(R.id.nameUser_post);
            commentsquant = itemView.findViewById(R.id.quantscomments);
            quantlikes = itemView.findViewById(R.id.quantlikes_post);

        }

        private void showMoreOptions(ImageView bnt) {
            PopupMenu popupMenu = new PopupMenu(context, bnt, Gravity.END);
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Editar");
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Apagar");
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == 0) {
                    editando();
                } else {
                    apagar();
                }
                return false;
            });
            popupMenu.show();

        }

        private void editando() {
            MainActivity.addOrEdit(context, getAdapterPosition(), true, posts.get(getAdapterPosition()));
            MainActivity.initBlog();
        }

        private void apagar() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference("Blog").child(Login.pessoa.getId() + "").child(posts.get(getAdapterPosition()).getId()).removeValue();
            db.collection("Blog").document(posts.get(getAdapterPosition()).getId()).delete().addOnSuccessListener(command -> {
                posts.remove(getAdapterPosition());
                notifyDataSetChanged();
            });

        }
    }
}
