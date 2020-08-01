package com.example.pingo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainPagerAdapter extends FragmentStateAdapter {
    private static  final int TAB_NUM = 3;

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ContactFragment();
            case 1:
                return new ChatFragment();
            default:
                return new SettingFragment();
        }
    }

    @Override
    public int getItemCount() {
        return TAB_NUM;
    }
}
