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
import android.widget.EditText;
import android.widget.Toast;

import com.deeaboi.smartlib.Model.ProductsBooks;
import com.deeaboi.smartlib.ViewHolder.ProductBookViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity
{
       protected Button searchbtn;
       private EditText inputText;
       private RecyclerView searchList;
       private String searchinput;
       private String type ="",key="";
  //watch video admin accesss products for both user and admin

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


//                Intent intent =getIntent();
//                Bundle bundle =intent.getExtras();
//
//                    if(bundle!=null)
//           {
//             type = getIntent().getExtras().get("Admin").toString();
//             key=getIntent().getExtras().get("key").toString();
//            }


        type = getIntent().getStringExtra("Admin");
        key=getIntent().getStringExtra("key");





        inputText =findViewById(R.id.search_book_name);
        searchbtn =findViewById(R.id.search_btn);
        searchList=findViewById(R.id.search_list);

        searchList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        searchbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                   searchinput = inputText.getText().toString();
                   onStart();
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

//        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products Books");chng the reference for update datbase
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("College").child(key).child("Library Books");

        FirebaseRecyclerOptions<ProductsBooks> options =
                new FirebaseRecyclerOptions.Builder<ProductsBooks>()
                .setQuery(reference.orderByChild("pname").startAt(searchinput), ProductsBooks.class)
                .build();


        FirebaseRecyclerAdapter<ProductsBooks, ProductBookViewHolder> adapter=
                new FirebaseRecyclerAdapter<ProductsBooks, ProductBookViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductBookViewHolder productBookViewHolder, final int i, @NonNull final ProductsBooks productsBooks)
                    {
                         productBookViewHolder.txtproductbookname.setText(productsBooks.getPname());
                        productBookViewHolder.txtproductbookauther.setText(productsBooks.getAuthor());

                        Picasso.get().load(productsBooks.getImage()).into(productBookViewHolder.imageView);

                        productBookViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                if(type.equals("Admin") )
                                {
                                    Intent intent= new Intent(SearchActivity.this,AdminAssignActivity.class);
                                    intent.putExtra("pid",productsBooks.getPid());
                                    intent.putExtra("key",key);
                                    startActivity(intent);
                                    finish();

                                }
                                else if (type.equals("User"))
                                {
                                    Toast.makeText(SearchActivity.this, "Search Books Here", Toast.LENGTH_SHORT).show();

                                    //do on click listener for students


                                }

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ProductBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_books_layout,parent,false);
                        ProductBookViewHolder holder =new ProductBookViewHolder(view);
                        return holder;
                    }
                };
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}
