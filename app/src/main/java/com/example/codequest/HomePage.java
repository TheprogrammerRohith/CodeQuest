package com.example.codequest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    private HomePageFragment homePageFragment;
    private DashBoardFragment dashBoardFragment;
    private TextView textview;
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref=getSharedPreferences("LoginSession",MODE_PRIVATE);
        String userName=pref.getString("UserName",null);
        textview.setText("Welcome to CodeQuest "+userName+" !!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        homePageFragment=new HomePageFragment();
        dashBoardFragment=new DashBoardFragment();
        ImageView menuButton = findViewById(R.id.menu_btn);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        FrameLayout frameLayout = findViewById(R.id.fragment);
        textview= findViewById(R.id.textview);
        // Set the ImageView onClickListener
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        setFragment(homePageFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                resetIconState(bottomNavigationView);
                int item_id=item.getItemId();
                if(item_id==R.id.home){
                    setFragment(homePageFragment);
                    item.setIcon(R.drawable.baseline_home_24);
                    return true;
                } else if (item_id==R.id.dashboard) {
                    setFragment(dashBoardFragment);
                    item.setIcon(R.drawable.baseline_dashboard_24);
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    // Function to show PopupMenu
    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(HomePage.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_options, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int item_id=item.getItemId();
                if(item_id==R.id.sign_out){
                    signOut();
                    return true;
                }
                else {
                    return false;
                }
            }
        });

        popup.show();
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentTransaction.commit();
    }

    private void resetIconState(BottomNavigationView bottomNavigationView) {
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            int itemId=item.getItemId();
            if(itemId==R.id.home){
                item.setIcon(R.drawable.outline_home_24);
            }
            else if (itemId==R.id.dashboard) {
                item.setIcon(R.drawable.outline_dashboard_24);
            }
        }
    }

    // Sign out function
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
        SharedPreferences pref=getSharedPreferences("LoginSession",MODE_PRIVATE);
        SharedPreferences.Editor editor= pref.edit();
        editor.clear();
        editor.apply();
        Intent intent=new Intent(HomePage.this, MainActivity.class);
        startActivity(intent);
        finish();  // or redirect to login page
    }
}
