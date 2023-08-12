package com.project.studiopatyzinha.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.auth.User;
import com.project.studiopatyzinha.Chat_activity;
import com.project.studiopatyzinha.Login;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.PostPattern;
import com.project.studiopatyzinha.pattern.UserPattern;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adaptercontatos extends RecyclerView.Adapter<Adaptercontatos.meuViewHolder> implements Filterable {
    Context context;
    List<UserPattern> list;
    String idPessoaLocal;
    public static ArrayList<UserPattern> listall;

    public Adaptercontatos(Context context, List<UserPattern> list) {
        this.context = context;
        this.list = list;
        listall = new ArrayList<>(list);
        idPessoaLocal = Login.pessoa.getId() + "";
    }

    @NonNull
    @Override
    public meuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, parent, false);
        return new meuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull meuViewHolder holder, int position) {
        UserPattern pattern = list.get(holder.getAdapterPosition());
        holder.parent.setOnClickListener(v -> {
            Intent intent = new Intent(context, Chat_activity.class);
            intent.putExtra("userId", pattern.getIdUser());
            intent.putExtra("userName", pattern.getName_user());
            intent.putExtra("profilePic", pattern.getImg_user());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            pattern.setQuant_new_message("0");
            notifyDataSetChanged();
        });
        String quant = pattern.getQuant_new_message();
        if (quant != null && Integer.parseInt(quant) > 0) {
            holder.quantNewMessage.setText(quant);
            holder.quantNewMessage.setVisibility(View.VISIBLE);
        } else {
            holder.quantNewMessage.setVisibility(View.GONE);
        }
        Glide.with(context).load(pattern.getImg_user()).into(holder.img);
        String lasMessage = pattern.getLast_message_user();
        holder.lastMessage.setText(lasMessage);
        holder.time_lastMessage.setText(list.get(holder.getAdapterPosition()).getTime_last_message_user());
        holder.userName_list.setText(list.get(holder.getAdapterPosition()).getName_user());
        if (pattern.getStatuaLasMessage() != null) {
            if (pattern.getIdUser().equals(idPessoaLocal)) {
                if (pattern.getStatuaLasMessage().contains("enviada")) {
                    holder.img_status_message_preChat.setImageResource(R.drawable.tic_enviada);
                }
                if (pattern.getStatuaLasMessage().contains("recebida")) {
                    holder.img_status_message_preChat.setImageResource(R.drawable.tic_recebida);
                }
                if (pattern.getStatuaLasMessage().contains("visualizada")) {
                    holder.img_status_message_preChat.setImageResource(R.drawable.tic_visualisado);
                }
                holder.img_status_message_preChat.setVisibility(View.VISIBLE);
            } else {

                holder.img_status_message_preChat.setVisibility(View.GONE);
            }
        } else {
            holder.img_status_message_preChat.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            //run on back
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<UserPattern> filteredList = new ArrayList<>();
                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(listall);
                } else {
                    for (int i = 0; i < listall.size(); i++) {
                        if (listall.get(i).getName_user().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(listall.get(i));
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
                list.clear();
                list.addAll((Collection<? extends UserPattern>) results.values);
                notifyDataSetChanged();
            }
        };

        return filter;
    }

    public class meuViewHolder extends RecyclerView.ViewHolder {
        TextView lastMessage, userName_list, time_lastMessage, quantNewMessage;
        LinearLayout parent;
        ImageView img, img_status_message_preChat;

        public meuViewHolder(@NonNull View itemView) {
            super(itemView);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            img_status_message_preChat = itemView.findViewById(R.id.img_status_message_preChat);
            quantNewMessage = itemView.findViewById(R.id.quantNewMessage);
            parent = itemView.findViewById(R.id.parent_item_list);
            time_lastMessage = itemView.findViewById(R.id.time_lastMessage);
            userName_list = itemView.findViewById(R.id.userName_list);
            img = itemView.findViewById(R.id.img_pessoa_list);
        }
    }
}