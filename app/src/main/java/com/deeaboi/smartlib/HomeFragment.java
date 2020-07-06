package com.deeaboi.smartlib;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.deeaboi.smartlib.Prevalent.Prevalent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment
{
    public  View HomeFragmentview;
    private String key="";
    public ImageView addBooks,searchBooks,manageBooks,manageUsers,studentList,bookList,coursesOffred,assignBooks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
//        return super.onCreateView(inflater, container, savedInstanceState);
       // return inflater.inflate(R.layout.fragment_home,container,false);

       HomeFragmentview= inflater.inflate(R.layout.fragment_home,container,false);

       key=getActivity().getIntent().getExtras().getString("key");


        searchBooks=(ImageView)HomeFragmentview.findViewById(R.id.searchbooks);//done
        manageBooks=(ImageView)HomeFragmentview.findViewById(R.id.managebooks); //done
        manageUsers=(ImageView)HomeFragmentview.findViewById(R.id.manageusers); //done
        assignBooks=(ImageView)HomeFragmentview.findViewById(R.id.assignbooks); //done
       addBooks=(ImageView)HomeFragmentview.findViewById(R.id.addBooks);//done
       studentList=(ImageView)HomeFragmentview.findViewById(R.id.studentlist);//done
       bookList=(ImageView)HomeFragmentview.findViewById(R.id.booklist);//done
       coursesOffred=(ImageView)HomeFragmentview.findViewById(R.id.coursesoffered);


        return  HomeFragmentview;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        manageUsers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(getActivity(), ControlActivity.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

        assignBooks.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(getActivity(),SearchActivity.class);
                intent.putExtra("Admin","Admin");
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });
        searchBooks.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(getActivity(),SearchActivity.class);
               intent.putExtra("key",key);
               intent.putExtra("Admin","Users");
               startActivity(intent);
            }
        });
        manageBooks.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getActivity(),AdminBookControlActivity.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });
        addBooks.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(getActivity(),AddBooksActivity.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });

        bookList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(),GenerateBookListActivity.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });
        studentList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(),StudentListActivity.class);
                intent.putExtra("key",key);
                startActivity(intent);
            }
        });
        coursesOffred.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Available on next update", Toast.LENGTH_SHORT).show();
            }
        });









    }
}
