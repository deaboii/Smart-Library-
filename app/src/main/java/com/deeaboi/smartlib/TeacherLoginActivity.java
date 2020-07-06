package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.deeaboi.smartlib.Model.ProductsBooks;
import com.deeaboi.smartlib.Prevalent.Prevalent;
import com.deeaboi.smartlib.ViewHolder.BookListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class TeacherLoginActivity extends AppCompatActivity
{
    private Button logoutBtn;
    private RecyclerView teacherBookListView;
    private String key="",phone="";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        logoutBtn=findViewById(R.id.teacher_logout);
        teacherBookListView=findViewById(R.id.teacher_book_list);
        key=getIntent().getStringExtra("key");
        phone=getIntent().getStringExtra("phone");


        logoutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Paper.book().destroy();
                Intent intent=new Intent(TeacherLoginActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        teacherBookListView.setLayoutManager(new LinearLayoutManager(TeacherLoginActivity.this));
        onStart();

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("College").child(key).child("TeachersBooks").child(phone);

        FirebaseRecyclerOptions <ProductsBooks> options=
                new FirebaseRecyclerOptions.Builder<ProductsBooks>()
                .setQuery(reference,ProductsBooks.class)
                .build();


        FirebaseRecyclerAdapter<ProductsBooks, BookListViewHolder> adapter=
                new FirebaseRecyclerAdapter<ProductsBooks, BookListViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull BookListViewHolder bookListViewHolder, int i, @NonNull ProductsBooks productsBooks)
                    {

                        bookListViewHolder.bookName.setText(productsBooks.getPname());
                        bookListViewHolder.bookCategory.setText(productsBooks.getCategory());
                        bookListViewHolder.bookAuthor.setText(productsBooks.getAuthor());
                        bookListViewHolder.slNo.setText(String.valueOf(i+1));

                    }

                    @NonNull
                    @Override
                    public BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booklist_layout, parent, false);
                        BookListViewHolder holder = new BookListViewHolder(view);
                        return holder;
                    }
                };

        teacherBookListView.setAdapter(adapter);
        adapter.startListening();



    }
}
