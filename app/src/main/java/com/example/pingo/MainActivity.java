package com.example.pingo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ActionBarTheme);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ViewPager2 viewPager2 = findViewById(R.id.viewPagerMain);
        viewPager2.setAdapter(new MainPagerAdapter(this));

        TabLayout tabMain = findViewById(R.id.tabMain);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabMain, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case 0:
                        tab.setText("Friends");
                        tab.setIcon(R.drawable.ic_contact);
                        break;
                    case 1:
                        tab.setText("Chat");
                        tab.setIcon(R.drawable.ic_chat);
                        BadgeDrawable chatBadge = tab.getOrCreateBadge();
                        chatBadge.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.colorChatBadge)
                        );
                        chatBadge.setVisible(true);
                        chatBadge.setNumber(100);
                        break;
                    case 2:
                        tab.setText("Setting");
                        tab.setIcon(R.drawable.ic_setting);
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
    }


}