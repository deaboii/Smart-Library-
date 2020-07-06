package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class collegeSignupActivity extends AppCompatActivity
{
    private EditText clgname,clgaddress,clgpin,clgkey,clgpassword;
    private Button CreatAdminbtn;
    private ImageView backbtn;
    private String clgPhonenumber="",clgkeys;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_signup);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null)
        {
            clgPhonenumber=getIntent().getExtras().get("phonenumber").toString();

        }




        clgname=(EditText)findViewById(R.id.clgname);
        clgaddress=(EditText)findViewById(R.id.clgaddress);
        clgpin=(EditText)findViewById(R.id.clgpin);
        clgkey=(EditText)findViewById(R.id.clgKey);
        clgpassword=(EditText)findViewById(R.id.clgpassword);

        CreatAdminbtn=(Button)findViewById(R.id.CreatCollegeAccount);
        backbtn=(ImageView)findViewById(R.id.back);


        backbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent= new Intent(collegeSignupActivity.this,PhoneActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });


        CreatAdminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Validate();

            }
        });





    }

    private void Validate()
    {
        if(TextUtils.isEmpty(clgname.getText().toString()))
        {
            Toast.makeText(this, "Please enter College name..", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(clgaddress.getText().toString()))
        {
            Toast.makeText(this, "Please enter College Address..", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(clgpin.getText().toString()))
        {
            Toast.makeText(this, "Please enter College Pin..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(clgkey.getText().toString()))
        {

            Toast.makeText(this, "Enter a UniqueKey..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(clgpassword.getText().toString()))
        {

            Toast.makeText(this, "Enter a Password..", Toast.LENGTH_SHORT).show();
        }
        else
        {

            CreatAccount();

        }


    }

    private void CreatAccount()
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                clgkeys=clgkey.getText().toString();

//                if(!dataSnapshot.child("College").child(clgPhonenumber).exists())
//                if(!dataSnapshot.child("College").child(clgkeys).child("Data").child(clgPhonenumber).exists())


                if(!dataSnapshot.child("College").child(clgkeys).exists())


                {
                    HashMap<String,Object> userDataMap= new HashMap<>();
                    userDataMap.put("Name",clgname.getText().toString());
                    userDataMap.put("Address",clgaddress.getText().toString());
                    userDataMap.put("Pin",clgpin.getText().toString());
                    userDataMap.put("Key",clgkey.getText().toString());
                    userDataMap.put("Password",clgpassword.getText().toString());
                    userDataMap.put("Phone",clgPhonenumber);

                    HashMap<String,Object> userDataMap1= new HashMap<>();
                    userDataMap1.put("Name",clgname.getText().toString());
                    userDataMap1.put("key",clgkeys);

                    RootRef.child("CollegePhone").child(clgPhonenumber).updateChildren(userDataMap1); // to store college phone on a difireet node for sign up verify where have or not

//                    RootRef.child("College").child(clgPhonenumber).updateChildren(userDataMap)
                    RootRef.child("College").child(clgkeys).child("Data").updateChildren(userDataMap) //new database format
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(collegeSignupActivity.this, "Account Created Successfully.", Toast.LENGTH_SHORT).show();


                                        Intent intent=new Intent(collegeSignupActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }

                                    else
                                    {

                                        Toast.makeText(collegeSignupActivity.this, "Error : Please Try Again...", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });



                }
                else{

                    Toast.makeText(collegeSignupActivity.this, "Key already exist, Use a different key", Toast.LENGTH_SHORT).show();

                }











            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }


}
