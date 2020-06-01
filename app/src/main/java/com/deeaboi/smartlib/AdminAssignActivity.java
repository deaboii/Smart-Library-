package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deeaboi.smartlib.Model.ProductsBooks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAssignActivity extends AppCompatActivity
{

    private Button AssignBooks;
    private EditText inputRollno;
    private ImageView imageView;
    private String productId ="";
    private TextView BookName,AuthorName;
    private CheckBox chkBoxIssuedBooks,chkBoxRefrenceBooks;
    private DatabaseReference NotificationRef;

    private  String image;//Deepak for book image in Userbooks


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assign);

        Toast.makeText(this, "Assign Books Here", Toast.LENGTH_SHORT).show();

        NotificationRef=FirebaseDatabase.getInstance().getReference().child("Notifications");


        productId= getIntent().getStringExtra("pid");

        AssignBooks =findViewById(R.id.Add_to_Students);
        inputRollno =findViewById(R.id.rollnumber);
        imageView =findViewById(R.id.product_Book_image);

        chkBoxIssuedBooks=(CheckBox)findViewById(R.id.Issued_Books_chk);
        chkBoxRefrenceBooks=(CheckBox)findViewById(R.id.Refrence_Books_chk);

        BookName=findViewById(R.id.product_book_name);
        AuthorName=findViewById(R.id.product_book_author);
        getProductBookDetails(productId);


        AssignBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                assignTostudents();
            }
        });



    }

    private void assignTostudents()//show video how  to add product frm cart to final order admin -admin pannel
    {
        if(TextUtils.isEmpty(inputRollno.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Roll Number...", Toast.LENGTH_SHORT).show();

        }
        else {
            if (chkBoxIssuedBooks.isChecked())
                  {


                final String saveCurrentDate, saveCurrentTime;

            Calendar callForDate = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
            saveCurrentDate = currentDate.format(callForDate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(callForDate.getTime());


            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("UserBooks");



            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("pid", productId);
            cartMap.put("pname", BookName.getText().toString());
            cartMap.put("Author", AuthorName.getText().toString());
            cartMap.put("date", saveCurrentDate);
            cartMap.put("time", saveCurrentTime);
            cartMap.put("image",image);

            cartListRef.child(inputRollno.getText().toString()).child("Issue").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        //do something here for fargment to show on user activity

                        HashMap<String,String> issuenotificationMap=new HashMap<>();
                        issuenotificationMap.put("bookid",productId);
                        issuenotificationMap.put("type","Issue");
                        issuenotificationMap.put("date",saveCurrentDate);
                        NotificationRef.child(inputRollno.getText().toString()).push().setValue(issuenotificationMap);


                        Toast.makeText(AdminAssignActivity.this, "Book Added to Database", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminAssignActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                    }
                }
            });

                  }

            else if(chkBoxRefrenceBooks.isChecked())

                      {

                          final String saveCurrentDate, saveCurrentTime;

                          Calendar callForDate = Calendar.getInstance();

                          SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                          saveCurrentDate = currentDate.format(callForDate.getTime());

                          SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                          saveCurrentTime = currentTime.format(callForDate.getTime());


                          final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("UserBooks");


                          final HashMap<String, Object> cartMap = new HashMap<>();
                          cartMap.put("pid", productId);
                          cartMap.put("pname", BookName.getText().toString());
                          cartMap.put("Author", AuthorName.getText().toString());
                          cartMap.put("date", saveCurrentDate);
                          cartMap.put("time", saveCurrentTime);
                          cartMap.put("image",image);

                          cartListRef.child(inputRollno.getText().toString()).child("Refrence").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if (task.isSuccessful()) {
                                      //do something here for fargment to show on user activity

                                      HashMap<String,String> issuenotificationMap=new HashMap<>();
                                      issuenotificationMap.put("bookid",productId);
                                      issuenotificationMap.put("type","Refrence");
                                      issuenotificationMap.put("date",saveCurrentDate);
                                      NotificationRef.child(inputRollno.getText().toString()).push().setValue(issuenotificationMap);


                                      Toast.makeText(AdminAssignActivity.this, "Book Added to Database", Toast.LENGTH_SHORT).show();

                                      Intent intent = new Intent(AdminAssignActivity.this, AdminCategoryActivity.class);
                                      startActivity(intent);
                                  }
                              }
                          });
                      }
            else
            {
                Toast.makeText(this, "Please select Issue or Refrenec", Toast.LENGTH_SHORT).show();
            }


            }

    }


    private void getProductBookDetails(String productId)
    {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products Books");
        productsRef.child(productId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {

                    ProductsBooks products = dataSnapshot.getValue(ProductsBooks.class);

                    BookName.setText(products.getPname());
                    AuthorName.setText(products.getAuthor());

                    Picasso.get().load(products.getImage()).into(imageView);

                     image =dataSnapshot.child("image").getValue().toString();//Deepak

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }
}
