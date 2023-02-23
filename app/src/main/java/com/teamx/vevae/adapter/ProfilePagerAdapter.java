package com.teamx.vevae.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.teamx.vevae.fragments.StoreProfile.StoreHomeFragment;
import com.teamx.vevae.fragments.StoreProfile.StoreReviewFragment;

import org.jetbrains.annotations.NotNull;

public class ProfilePagerAdapter extends FragmentStateAdapter {

    public ProfilePagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new StoreHomeFragment();
            case 1:
                return new StoreReviewFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
