package com.example.vehicleassistance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class notificationFragment extends Fragment {
    private View view;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_notification,container,false);
        viewPager=view.findViewById(R.id.viewPager_notification);
        tabLayout=view.findViewById(R.id.tabLayout_notificationFragment);
        viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new UpcomingNotificationFragment(),"");
        viewPagerAdapter.addFragment(new OldNotificationsFragment(),"");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.car_wash);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_notifications_black_24dp);
        return view;
    }
}
