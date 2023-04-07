package com.example.testapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.testapp.fragments.Chat;
import com.example.testapp.fragments.Friends;
import com.example.testapp.fragments.Map;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new Chat();
            case 1: return new Map();
            case 2: return new Friends();
            default: return new Chat();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
