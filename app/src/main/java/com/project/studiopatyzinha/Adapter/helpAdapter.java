package com.project.studiopatyzinha.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Descpattern;

import java.util.ArrayList;

public class helpAdapter extends RecyclerView.Adapter<helpAdapter.ViewHolder> {
    ArrayList<String> funcs;
    Context context;

    public helpAdapter(ArrayList<String> titles) {
        this.funcs = titles;
    }

    @NonNull
    @Override
    public helpAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_func, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull helpAdapter.ViewHolder holder, int position) {
        String desc = funcs.get(position);
        holder.parentfuncs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "mostrando o "+desc, Toast.LENGTH_SHORT).show();
            }
        });
        holder.Namefunc.setText(desc);
        holder.trash.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return funcs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ftfunc, trash;
        TextView Namefunc;
        ConstraintLayout parentfuncs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trash = itemView.findViewById(R.id.deletefunc);
            ftfunc = itemView.findViewById(R.id.imageView2);
            parentfuncs = itemView.findViewById(R.id.parentfuncs);
            Namefunc = itemView.findViewById(R.id.Namefunc);
        }
    }
}
