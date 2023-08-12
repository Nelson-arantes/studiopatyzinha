package com.project.studiopatyzinha.Adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.MainActivity;
import com.project.studiopatyzinha.R;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MeuViewHolder> {
    ArrayList<String> tagslist;

    public TagAdapter(ArrayList<String> tagslist) {
        this.tagslist = tagslist;
    }

    @NonNull
    @Override
    public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_pattern, parent, false);
        return new MeuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuViewHolder holder, int position) {
        String stringtag = tagslist.get(position);
        holder.txt.setText(stringtag);
        holder.delete.setOnClickListener(v -> {
            tagslist.remove(position);
            MainActivity.tags=MainActivity.tags.replace(stringtag+";","");
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return tagslist.size();
    }

    public class MeuViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        ImageView delete;

        public MeuViewHolder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.imgdelete_tag);
            txt = itemView.findViewById(R.id.txt_tag);

        }
    }
}
