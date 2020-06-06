package com.example.phonewallpaper.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.phonewallpaper.R;
import com.example.phonewallpaper.fragments.FavouriteFragment;
import com.example.phonewallpaper.fragments.HomeFragment;
import com.example.phonewallpaper.fragments.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        displayFragment(new HomeFragment());
    }

    private void displayFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_area,fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;

            case R.id.nav_fav:
                fragment = new FavouriteFragment();
                break;

            case R.id.nav_settings:
                fragment = new SettingFragment();
                break;
                default:
                    fragment = new HomeFragment();
        }
        displayFragment(fragment);
        return true;
    }
}
