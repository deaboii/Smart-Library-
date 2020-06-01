package com.deeaboi.smartlib;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deeaboi.smartlib.Model.IssuedUserBooks;
import com.deeaboi.smartlib.ViewHolder.BookViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Main2Activity extends AppCompatActivity
{
    private RecyclerView searchList;
    private  String value="",type="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent =getIntent();
        Bundle bundle =intent.getExtras();

        if(bundle!=null)
        {
          value=getIntent().getExtras().get("roll").toString();
            type=getIntent().getExtras().get("Admin").toString();
        }

        searchList=findViewById(R.id.search_list);

        searchList.setLayoutManager(new LinearLayoutManager(Main2Activity.this));

                onStart();

    }

    @Override
    protected void onStart()
    {

        super.onStart();
        if( type.equals("Admin"))
        {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UserBooks").child(value).child("Issue");
            FirebaseRecyclerOptions<IssuedUserBooks> options =
                    new FirebaseRecyclerOptions.Builder<IssuedUserBooks>()
                            .setQuery(reference, IssuedUserBooks.class)
                            .build();
            FirebaseRecyclerAdapter<IssuedUserBooks, BookViewHolder> adapter =
                    new FirebaseRecyclerAdapter<IssuedUserBooks, BookViewHolder>(options)
                    {
                        @Override
                        protected void onBindViewHolder(@NonNull BookViewHolder productBookViewHolder, int i, @NonNull final IssuedUserBooks productsBooks)
                        {
                            productBookViewHolder.txtproductbookname.setText(productsBooks.getPname());
                            productBookViewHolder.txtproductbookauther.setText(productsBooks.getAuthor());
                            productBookViewHolder.DateTime.setText("Issued On:" + productsBooks.getDate());

                            Picasso.get().load(productsBooks.getImage()).into(productBookViewHolder.imageView);

                            productBookViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {

                                    CharSequence options[]=new CharSequence[]
                                            {
                                             "Yes","No"
                                            };
                                    AlertDialog.Builder builder =new AlertDialog.Builder(Main2Activity.this);
                                    builder.setTitle("Remove Issue Book  !");

                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {
                                            if (i == 0)
                                            {
                                               //String uID = getRef(i).getKey();
                                                String uID= productsBooks.getPid();
                                               reference.child(uID).removeValue();

                                            }
                                            else
                                                {
                                                    finish();
                                                }

                                        }
                                    });

                                    builder.show();

                                }
                            });


                        }

                        @NonNull
                        @Override
                        public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                        {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issued_layout, parent, false);
                            BookViewHolder holder = new BookViewHolder(view);
                            return holder;
                        }
                    };
            searchList.setAdapter(adapter);
            adapter.startListening();
        }
        else if(type.equals("Admin1"))
        {

           final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UserBooks").child(value).child("Refrence"); //value =Roll from intent
           //

            FirebaseRecyclerOptions<IssuedUserBooks> options =
                    new FirebaseRecyclerOptions.Builder<IssuedUserBooks>()
                            .setQuery(reference, IssuedUserBooks.class)
                            .build();
            FirebaseRecyclerAdapter<IssuedUserBooks, BookViewHolder> adapter =
                    new FirebaseRecyclerAdapter<IssuedUserBooks, BookViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull BookViewHolder productBookViewHolder, int i, @NonNull final IssuedUserBooks productsBooks) {
                            productBookViewHolder.txtproductbookname.setText(productsBooks.getPname());
                            productBookViewHolder.txtproductbookauther.setText(productsBooks.getAuthor());
                            productBookViewHolder.DateTime.setText("Issued On:" + productsBooks.getDate());

                            Picasso.get().load(productsBooks.getImage()).into(productBookViewHolder.imageView);

                            productBookViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {

                                    CharSequence options[]=new CharSequence[]
                                            {
                                                    "Yes","No"
                                            };
                                    AlertDialog.Builder builder =new AlertDialog.Builder(Main2Activity.this);
                                    builder.setTitle("Remove Issue Book  !");

                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i)
                                        {
                                            if (i == 0)
                                            {
                                               // String uID = getRef(i).getKey(); // error

                                                String uID= productsBooks.getPid();
                                                reference.child(uID).removeValue();

                                            }
                                            else
                                            {
                                                finish();
                                            }

                                        }
                                    });

                                    builder.show();

                                }
                            });


                        }

                        @NonNull
                        @Override
                        public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issued_layout, parent, false);
                            BookViewHolder holder = new BookViewHolder(view);
                            return holder;
                        }
                    };
            searchList.setAdapter(adapter);
            adapter.startListening();
        }


    }



}