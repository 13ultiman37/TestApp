package com.example.testapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.testapp.R;
import com.example.testapp.adapters.ViewPagerAdapter;
import com.example.testapp.databinding.ActivityMainBinding;
import com.example.testapp.databinding.FragmentChatBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private String[] PERMISSIONS;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView bottomNavigationView;
    private FragmentChatBinding chatBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        chatBinding = FragmentChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigationView = binding.bottomNav;
        viewPager2 = binding.viewPager;
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        chatBinding.conversationsRecyclerView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.bottom_chats:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.bottom_map:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.bottom_friends:
                        viewPager2.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_chats).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_map).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_friends).setChecked(true);
                        break;
                }
                super.onPageSelected(position);
            }
        });

        PERMISSIONS = new String[]{
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
        };

        if (hasPermissions(getApplicationContext(), PERMISSIONS) == false) {
            showPermissionsAlert();
        }
    }

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Для полноценной и корректной работы приложения необходимо предоставить следующие разрешения: \n - Доступ к геоданным\n - Доступ к отправке уведомлений");
        builder.setNegativeButton("Отказаться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setPositiveButton("Предоставить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, 1);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean hasPermissions(Context context, String... PERMISSIONS) {
        if (context != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}