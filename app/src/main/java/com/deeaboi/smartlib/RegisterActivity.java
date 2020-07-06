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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{
    private Button CreatAccountButton;
    private EditText InputName,InputPassword;
    private String  InputPhoneNumber="",Collegekey="",type="";
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)

    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        Intent intent =getIntent();
        Bundle bundle =intent.getExtras();              //add for teacher sign up good on student
//
//        if(bundle!=null)
//        {
//            InputPhoneNumber=getIntent().getStringExtra("phonenumber");
//            Collegekey=getIntent().getStringExtra("Collegekey");
//            type =getIntent().getStringExtra("type");
//
//
//        }


//good for student till now change for teacher...

          InputPhoneNumber=getIntent().getStringExtra("phonenumber");
          Collegekey=getIntent().getStringExtra("Collegekey");
          type =getIntent().getStringExtra("type");




        CreatAccountButton = (Button)findViewById(R.id.register_btn);
        InputName = (EditText)findViewById(R.id.Register_username_input);
        InputPassword= (EditText)findViewById(R.id.Register_password_input);
        loadingBar = new ProgressDialog(this);

         CreatAccountButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view)
             {
                 CreatAccount();
             }
         });

    }

    private void CreatAccount()
    {
        String name =InputName.getText().toString();
       // String phone =InputPhoneNumber.getText().toString();
        String password =InputPassword.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
        }
      //  else if(TextUtils.isEmpty(phone))
      //  {
         //   Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
      //  }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        }
        else
        {
          loadingBar.setTitle("Creat Account");
          loadingBar.setMessage("Please Wait While Creating Your Account ");
          loadingBar.setCanceledOnTouchOutside(false);
          loadingBar.show();

     ValidatePhoneNumber(name,InputPhoneNumber,password,Collegekey);


        }


    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password, final String collegekey)
    {
      final DatabaseReference RootRef;
      RootRef = FirebaseDatabase.getInstance().getReference();
      RootRef.addListenerForSingleValueEvent(new ValueEventListener()
      {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot)
          {

              if(type.equals("teacher"))
              {
                  if(!dataSnapshot.child("College").child(collegekey).child("Teacher").child(phone).exists())
                  {

                      HashMap<String, Object> userDataMap = new HashMap<>();
                      userDataMap.put("phone", phone);
                      userDataMap.put("password", password);
                      userDataMap.put("name", name);
                      userDataMap.put("key", Collegekey);
//                  RootRef.child("Users").child(phone).updateChildren(userDataMap)
                      RootRef.child("College").child(Collegekey).child("Teacher").child(phone).updateChildren(userDataMap)
                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if (task.isSuccessful())
                                      {
                                          Toast.makeText(RegisterActivity.this, "Account Created Successfully.", Toast.LENGTH_SHORT).show();
                                          loadingBar.dismiss();

                                          Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                          intent.putExtra("value", "teacher");
                                          startActivity(intent);
                                      }

                                      else
                                          {
                                          loadingBar.dismiss();
                                          Toast.makeText(RegisterActivity.this, "Error : Please Try Again...", Toast.LENGTH_SHORT).show();

                                      }

                                  }
                              });


                  }
                  else
                  {

                      Toast.makeText(RegisterActivity.this, "This" + phone + "number is Already exist", Toast.LENGTH_SHORT).show();
                      loadingBar.dismiss();
                      Toast.makeText(RegisterActivity.this, "Please Try Again with Another Phone Number", Toast.LENGTH_SHORT).show();

                      Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                      startActivity(intent);


                  }




              }
              else if(type.equals("student"))
                  {

//              if(!(dataSnapshot.child("Users").child(phone).exists()))
                  if (!(dataSnapshot.child("College").child(collegekey).child("Student").child(phone).exists()))
                  {
                      HashMap<String, Object> userDataMap = new HashMap<>();
                      userDataMap.put("phone", phone);
                      userDataMap.put("password", password);
                      userDataMap.put("name", name);
                      userDataMap.put("key", Collegekey);
//                  RootRef.child("Users").child(phone).updateChildren(userDataMap)
                      RootRef.child("College").child(Collegekey).child("Student").child(phone).updateChildren(userDataMap)
                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if (task.isSuccessful()) {
                                          Toast.makeText(RegisterActivity.this, "Account Created Successfully.", Toast.LENGTH_SHORT).show();
                                          loadingBar.dismiss();

                                          Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                          intent.putExtra("value", "student"); //chng to value
                                          startActivity(intent);
                                      } else {
                                          loadingBar.dismiss();
                                          Toast.makeText(RegisterActivity.this, "Error : Please Try Again...", Toast.LENGTH_SHORT).show();

                                      }

                                  }
                              });


                  }
                  else
                      {

                      Toast.makeText(RegisterActivity.this, "This" + phone + "number is Already exist", Toast.LENGTH_SHORT).show();
                      Toast.makeText(RegisterActivity.this, "Please Try Again with Another Phone Number", Toast.LENGTH_SHORT).show();
                      Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                      startActivity(intent);
                          loadingBar.dismiss();


                  }


              }



          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });

    }

}
