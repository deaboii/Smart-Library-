package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.RadialGradient;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class AdminAssignActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener
{

    RadioGroup radioGroup;

    private Button AssignBooks;
    private EditText inputRollno;
    private ImageView imageView;
    private String productId ="",key="",type="";
    private TextView BookName,AuthorName;
    private DatabaseReference NotificationRef,Token;

    private  String image,device_token;//Deepak for book image in Userbooks


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assign);

        Toast.makeText(this, "Assign Books Here", Toast.LENGTH_SHORT).show();



        productId= getIntent().getStringExtra("pid");
        key=getIntent().getStringExtra("key");

       // NotificationRef=FirebaseDatabase.getInstance().getReference().child("College").child(key).child("Notifications");

        AssignBooks =findViewById(R.id.Add_to_Students);
        inputRollno =findViewById(R.id.rollnumber);
        imageView =findViewById(R.id.product_Book_image);

        BookName=findViewById(R.id.product_book_name);
        AuthorName=findViewById(R.id.product_book_author);
        getProductBookDetails(productId);


        radioGroup=findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);





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

        else
        {
            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {


                    if(dataSnapshot.child("College").child(key).child("Student").child(inputRollno.getText().toString()).exists() || dataSnapshot.child("College").child(key).child("Teacher").child(inputRollno.getText().toString()).exists())
                    {



                        NotificationRef=FirebaseDatabase.getInstance().getReference().child("Notifications");
                        Token= FirebaseDatabase.getInstance().getReference().child("College").child(key).child("Student").child(inputRollno.getText().toString());


                        Token.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if (dataSnapshot.exists())
                                {

                                     device_token = dataSnapshot.child("device_token").getValue().toString();


                                }
//                                else {
//                                    Toast.makeText(AdminAssignActivity.this, "Device token not found ", Toast.LENGTH_SHORT).show();
//
//                                }
//

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });



                       // if (chkBoxIssuedBooks.isChecked())
                        if(type.equals("issue"))
                        {


                            final String saveCurrentDate, saveCurrentTime;

                            Calendar callForDate = Calendar.getInstance();

                            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                            saveCurrentDate = currentDate.format(callForDate.getTime());

                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                            saveCurrentTime = currentTime.format(callForDate.getTime());


//            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("UserBooks");
                            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("College").child(key).child("UserBooks");



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
                                        issuenotificationMap.put("clgid",key);
                                        issuenotificationMap.put("device_token",device_token);
                                        NotificationRef.child(inputRollno.getText().toString()).push().setValue(issuenotificationMap);


                                        Toast.makeText(AdminAssignActivity.this, "Book Added Successfully", Toast.LENGTH_SHORT).show();

                                        //Intent intent = new Intent(AdminAssignActivity.this, AdminCategoryActivity.class);
                                        Intent intent = new Intent(AdminAssignActivity.this,NewAdminActivity.class);
                                        intent.putExtra("key",key);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        }

                       // else if(chkBoxRefrenceBooks.isChecked())
                        else if(type.equals("reference"))

                        {

                            final String saveCurrentDate, saveCurrentTime;

                            Calendar callForDate = Calendar.getInstance();

                            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                            saveCurrentDate = currentDate.format(callForDate.getTime());

                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                            saveCurrentTime = currentTime.format(callForDate.getTime());


                            final DatabaseReference cartListRef =  FirebaseDatabase.getInstance().getReference().child("College").child(key).child("UserBooks");


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
                                    if (task.isSuccessful())
                                    {
                                        //do something here for fargment to show on user activity

                                        HashMap<String,String> issuenotificationMap=new HashMap<>();
                                        issuenotificationMap.put("bookid",productId);
                                        issuenotificationMap.put("type","Reference");
                                        issuenotificationMap.put("date",saveCurrentDate);
                                        issuenotificationMap.put("clgid",key);
                                        issuenotificationMap.put("device_token",device_token);
                                        NotificationRef.child(inputRollno.getText().toString()).push().setValue(issuenotificationMap);


                                        Toast.makeText(AdminAssignActivity.this, "Book Added Successfully", Toast.LENGTH_SHORT).show();

//                                      Intent intent = new Intent(AdminAssignActivity.this, AdminCategoryActivity.class);
                                        Intent intent = new Intent(AdminAssignActivity.this,NewAdminActivity.class);
                                        intent.putExtra("key",key);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }


                        else

                            if(type.equals("teacher"))

                            {
                                final String saveCurrentDate, saveCurrentTime;

                                Calendar callForDate = Calendar.getInstance();

                                SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                                saveCurrentDate = currentDate.format(callForDate.getTime());

                                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                                saveCurrentTime = currentTime.format(callForDate.getTime());


                                final DatabaseReference cartListRef =  FirebaseDatabase.getInstance().getReference().child("College").child(key).child("TeachersBooks");


                                final HashMap<String, Object> cartMap = new HashMap<>();
                                cartMap.put("pid", productId);
                                cartMap.put("pname", BookName.getText().toString());
                                cartMap.put("Author", AuthorName.getText().toString());
                                cartMap.put("date", saveCurrentDate);
                                cartMap.put("time", saveCurrentTime);
                                cartMap.put("image",image);

                                cartListRef.child(inputRollno.getText().toString()).child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            //do something here for fargment to show on user activity


                                            Toast.makeText(AdminAssignActivity.this, "Book Added Successfully", Toast.LENGTH_SHORT).show();

//                                      Intent intent = new Intent(AdminAssignActivity.this, AdminCategoryActivity.class);
                                            Intent intent = new Intent(AdminAssignActivity.this,NewAdminActivity.class);
                                            intent.putExtra("key",key);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });





                           }


                        else
                        {

                            Toast.makeText(AdminAssignActivity.this, "Please select one", Toast.LENGTH_SHORT).show();

                        }




                    }
                    else
                        {
                            Toast.makeText(AdminAssignActivity.this, "Account not exist with this number", Toast.LENGTH_SHORT).show();

                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });


        }




            }




    private void getProductBookDetails(String productId)
    {

       // DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products Books");
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("College").child(key).child("Library Books");
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        switch (checkedId)
        {
            case R.id.radio_issue:
                type="issue";
                break;

            case R.id.radio_reference:
                type="reference";
                break;

            case R.id.radio_teacher:
                type="teacher";
                break;


        }



    }
}
