package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.deeaboi.smartlib.Model.IssuedUserBooks;
import com.deeaboi.smartlib.Model.ProductsBooks;
import com.deeaboi.smartlib.ViewHolder.BookListViewHolder;
import com.deeaboi.smartlib.ViewHolder.BookViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GenerateBookListActivity extends AppCompatActivity
{
    private RecyclerView bookListView;
    private String key="";
    private  int count;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_book_list);

        key=getIntent().getStringExtra("key");

        bookListView =findViewById(R.id.bookListView);

        bookListView.setLayoutManager(new LinearLayoutManager(GenerateBookListActivity.this));

        onStart();


    }


    @Override
    protected void onStart()
    {
        super.onStart();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("College").child(key).child("Library Books");

        FirebaseRecyclerOptions <ProductsBooks> options =
             new FirebaseRecyclerOptions.Builder<ProductsBooks>()
                .setQuery(reference,ProductsBooks.class)
                .build();

        FirebaseRecyclerAdapter <ProductsBooks, BookListViewHolder> adapter=
                new FirebaseRecyclerAdapter<ProductsBooks, BookListViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull BookListViewHolder bookListViewHolder, int i, @NonNull ProductsBooks productsBooks)
                    {


                        reference.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                 count = (int) dataSnapshot.getChildrenCount();
                                Toast.makeText(GenerateBookListActivity.this, "Total "+count+" no of Books", Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });

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

               bookListView.setAdapter(adapter);
               adapter.startListening();
    }

}
