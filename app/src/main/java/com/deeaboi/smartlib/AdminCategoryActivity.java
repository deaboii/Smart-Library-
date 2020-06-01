package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity
{
    private ImageView mech,civil;
    private ImageView eee,ece;
    private ImageView ee,cse;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        mech=(ImageView)findViewById(R.id.mechanical_logo);
        civil=(ImageView)findViewById(R.id.civil_logo);
        eee=(ImageView)findViewById(R.id.EEE_logo);
        ee=(ImageView)findViewById(R.id.EE_logo);
        ece=(ImageView)findViewById(R.id.ece_logo);
        cse=(ImageView)findViewById(R.id.CSE_logo);

        mToolbar = (Toolbar)findViewById(R.id.main_appbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Admin Panel");

        mech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Mechanical");
                startActivity(intent);
            }
        });

        civil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Civil");
                startActivity(intent);
            }
        });

        eee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Electrical&Electronics");
                startActivity(intent);
            }
        });

        ece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Electronics&Communication");
                startActivity(intent);
            }
        });
        ee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Electrical");
                startActivity(intent);
            }
        });

        cse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","ComputerScience");
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
         super.onOptionsItemSelected(item);
         if(item.getItemId() == R.id.logout_menu_btn)
         {
             Intent intent= new Intent(AdminCategoryActivity.this,MainActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
             startActivity(intent);
             finish();

         }
        if(item.getItemId() == R.id.update_books)
        {
            Intent intent= new Intent(AdminCategoryActivity.this,AdminBookControlActivity.class);
            startActivity(intent);//need to change

        }
        if(item.getItemId() == R.id.assign_books)
        {
            Intent intent= new Intent(AdminCategoryActivity.this,SearchActivity.class);
            intent.putExtra("Admin","Admin");
            startActivity(intent);

        }
        if(item.getItemId() == R.id.controls)
        {
            Intent intent= new Intent(AdminCategoryActivity.this,ControlActivity.class);
            startActivity(intent);

        }
        return true;

    }
}
