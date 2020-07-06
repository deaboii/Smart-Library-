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
import android.widget.Button;
import android.widget.EditText;

import com.deeaboi.smartlib.Model.ProductsBooks;
import com.deeaboi.smartlib.ViewHolder.ProductBookViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminBookControlActivity extends AppCompatActivity
{


    protected Button searchbtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String searchinput,key="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_book_control);


        inputText =findViewById(R.id.search_book_name);
        searchbtn =findViewById(R.id.search_btn);
        searchList=findViewById(R.id.search_list);

        key=getIntent().getStringExtra("key");

        searchList.setLayoutManager(new LinearLayoutManager(AdminBookControlActivity.this));

        searchbtn.setOnClickListener(new View.OnClickListener() {
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

       // final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products Books");
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("College").child(key).child("Library Books");





        FirebaseRecyclerOptions<ProductsBooks> options =
                new FirebaseRecyclerOptions.Builder<ProductsBooks>()
                        .setQuery(reference.orderByChild("pname").startAt(searchinput), ProductsBooks.class)
                        .build();
        FirebaseRecyclerAdapter<ProductsBooks, ProductBookViewHolder> adapter=
                new FirebaseRecyclerAdapter<ProductsBooks, ProductBookViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductBookViewHolder productBookViewHolder, int i, @NonNull final ProductsBooks productsBooks)
                    {
                        productBookViewHolder.txtproductbookname.setText(productsBooks.getPname());
                        productBookViewHolder.txtproductbookauther.setText(productsBooks.getAuthor());

                        Picasso.get().load(productsBooks.getImage()).into(productBookViewHolder.imageView);

                        productBookViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {

                                CharSequence options[]=new CharSequence[]
                                        {
                                                "Delete","Edit"
                                        };
                                AlertDialog.Builder builder =new AlertDialog.Builder(AdminBookControlActivity.this);
                                builder.setTitle("Admin Control");

                                builder.setItems(options, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        if (i == 0)
                                        {
                                           // String uID = getRef(i).getKey();
                                            String uID= productsBooks.getPid();
                                            reference.child(uID).removeValue();                   // problem whle deleteing a book ..

                                        }
                                        else if(i==1)
                                        {
                                            Intent intent= new Intent(AdminBookControlActivity.this,AdminEditActivity.class);
                                           intent.putExtra("pid",productsBooks.getPid());
                                           intent.putExtra("key",key);
                                            startActivity(intent);

                                        }

                                    }
                                });

                                builder.show();

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
