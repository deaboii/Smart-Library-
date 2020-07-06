package com.deeaboi.smartlib;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.deeaboi.smartlib.Prevalent.Prevalent;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{


    private ViewPager myViewpager;
    private TabLayout mytabLayout;
    private TabsAccessorAdapter mytabsAccessorAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Smart Library");

        Paper.init(this);
        toolbar.setSubtitle("Best Library Managment");


        myViewpager=(ViewPager)findViewById(R.id.main_tabs_pager);
        mytabsAccessorAdapter= new TabsAccessorAdapter(getSupportFragmentManager());
        myViewpager.setAdapter(mytabsAccessorAdapter);

        mytabLayout=(TabLayout)findViewById(R.id.main_tabs);
        mytabLayout.setupWithViewPager(myViewpager);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView =navigationView.getHeaderView(0);
        TextView UserNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

         UserNameTextView.setText(Prevalent.CurrentOnlineUser.getName());

        Picasso.get().load(Prevalent.CurrentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView); // circle view on tabs upper and name


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_searchbooks)
        {
            Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
            intent.putExtra("Admin","Users");
            intent.putExtra("key",Prevalent.CurrentOnlineUser.getKey());
            startActivity(intent);
        }
        else if (id == R.id.nav_oders)
        {
            Intent intent=new Intent(HomeActivity.this,FineActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_categories)
        {
            Intent shareintent =new Intent(Intent.ACTION_SEND);
            shareintent.setType("text/plain");
            String sharebody="https://play.google.com/store/apps/details?id=com.deeaboi.smartlib";
            String sharesub="Install from Playstore";
            shareintent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
            shareintent.putExtra(Intent.EXTRA_TEXT,sharebody);
            startActivity(Intent.createChooser(shareintent,"Share with"));
        }
        else if (id == R.id.nav_settings)
        {
            Intent intent=new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout)
        {
           Paper.book().destroy();

            Intent intent=new Intent(HomeActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
