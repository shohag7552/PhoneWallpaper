package com.example.phonewallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phonewallpaper.R;
import com.example.phonewallpaper.activities.Set_WallpaperActivity;
import com.example.phonewallpaper.models.Category;
import com.example.phonewallpaper.models.Wallpaper;

import java.util.List;

public class WallpapersAdapter extends RecyclerView.Adapter<WallpapersAdapter.CategoryViewHolder> {

    private Context c;
    private List<Wallpaper> wallpaperList;

    public WallpapersAdapter(Context c, List<Wallpaper> wallpaperList) {
        this.c = c;
        this.wallpaperList = wallpaperList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.recyclerview_wallpaper,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        Wallpaper wallpaper = wallpaperList.get(position);
        holder.textView.setText(wallpaper.title);
        Glide.with(c)
                .load(wallpaper.url)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    class CategoryViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        ImageView imageView;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view_title);
            imageView = itemView.findViewById(R.id.image_view);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Wallpaper w = wallpaperList.get(position);

            Intent wIntent  = new Intent(c, Set_WallpaperActivity.class);
            wIntent.putExtra("title",w.title);
            wIntent.putExtra("url",w.url);
            c.startActivity(wIntent);
        }
    }
}
