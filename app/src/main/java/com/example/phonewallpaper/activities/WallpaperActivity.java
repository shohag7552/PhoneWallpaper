package com.example.phonewallpaper.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.phonewallpaper.R;
import com.example.phonewallpaper.adapter.WallpapersAdapter;
import com.example.phonewallpaper.models.Wallpaper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WallpaperActivity extends AppCompatActivity {

    public List<Wallpaper> wallpaperList;
    public RecyclerView recyclerView;
    private WallpapersAdapter adapter;

    private DatabaseReference dbWallpaperRef;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);



        wallpaperList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        adapter = new WallpapersAdapter(this,wallpaperList);

        progressBar = findViewById(R.id.progressbar);


        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(category);
        setSupportActionBar(toolbar);


        dbWallpaperRef = FirebaseDatabase.getInstance().getReference("images").child(category);

        progressBar.setVisibility(View.VISIBLE);
        dbWallpaperRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()){
                    for (DataSnapshot wallpaperSnapshot: dataSnapshot.getChildren()){

                        Wallpaper w = wallpaperSnapshot.getValue(Wallpaper.class);

                        wallpaperList.add(w);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
