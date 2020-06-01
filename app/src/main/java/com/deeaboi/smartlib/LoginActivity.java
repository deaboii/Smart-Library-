package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deeaboi.smartlib.Model.Users;
import com.deeaboi.smartlib.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity

{
     private EditText InputPhoneNumber,InputPassword;
     private Button LoginButton;
     private ProgressDialog loadingBar;
     private String parentDbName = "Users";
     private CheckBox chkBoxRememberMe;
     private TextView AdminLink,NotAdminLink,Forgetpassword;
     private  DatabaseReference UsersRef;
    //private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UsersRef=FirebaseDatabase.getInstance().getReference().child("Users");

        LoginButton = (Button)findViewById(R.id.login_btn);
        InputPhoneNumber = (EditText)findViewById(R.id.login_phone_number_input);
        InputPassword= (EditText)findViewById(R.id.login_password_input);
        Forgetpassword=(TextView) findViewById(R.id.forget_password_link);
        AdminLink=(TextView)findViewById(R.id.admin_panel_link);
        NotAdminLink=(TextView)findViewById(R.id.not_admin_panel_link);

        loadingBar = new ProgressDialog(this);


        chkBoxRememberMe=(CheckBox)findViewById(R.id.remember_me_chk);
        Paper.init(this);



        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
             LoginButton.setText("Admin Login");
             AdminLink.setVisibility(View.INVISIBLE);
             NotAdminLink.setVisibility(View.VISIBLE);
             parentDbName="Admins";
             chkBoxRememberMe.setVisibility(View.INVISIBLE);

            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";
                chkBoxRememberMe.setVisibility(View.VISIBLE);
            }
        });

        Forgetpassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(intent);
            }
        });


    }

    private void LoginUser()
    {
        String phone =InputPhoneNumber.getText().toString();
        String password =InputPassword.getText().toString();

          if(TextUtils.isEmpty(phone))
            {
                Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
            }
              else if(TextUtils.isEmpty(password))
                   {
                       Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
                   }
           else

          {
              loadingBar.setTitle("Login Account");
              loadingBar.setMessage("Please Wait While Login to Your Account");
              loadingBar.setCanceledOnTouchOutside(false);
              loadingBar.show();

              AllowAccessToAccount(phone,password);

          }
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {

            if(chkBoxRememberMe.isChecked())
            {
                Paper.book().write(Prevalent.UserPhoneKey,phone);
                Paper.book().write(Prevalent.UserPasswordKey,password);
            }





        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                  if(dataSnapshot.child(parentDbName).child(phone).exists())
                  {
                      final Users userData =dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                      if(userData.getPhone().equals(phone))
                      {
                          if(userData.getPassword().equals(password))
                          {

                              if(parentDbName.equals("Admins"))
                              {

                                  Toast.makeText(LoginActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                                  loadingBar.dismiss();
                                  Intent intent=new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                  startActivity(intent);

                              }
                              else if(parentDbName.equals("Users"))
                              {

                                  FirebaseInstanceId.getInstance().getInstanceId()
                                          .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                              @Override
                                              public void onSuccess(InstanceIdResult instanceIdResult)
                                              {
                                                  String deviceToken = instanceIdResult.getToken();
                                                  UsersRef.child(phone).child("device_token")
                                                          .setValue(deviceToken)
                                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                              @Override
                                                              public void onComplete(@NonNull Task<Void> task)
                                                              {
                                                                  if (task.isSuccessful())
                                                                  {
                                                                      // Sign in success, update UI with the signed-in user's information
                                                                      Toast.makeText(LoginActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                                                                      loadingBar.dismiss();
                                                                      Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                                                      Prevalent.CurrentOnlineUser =userData;
                                                                      startActivity(intent);


                                                                  }
                                                              }
                                                          });
                                              }
                                          });


                              }

                          }
                          else
                          {
                              loadingBar.dismiss();
                              Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                          }

                      }


                  }
                    else
                  {

                      Toast.makeText(LoginActivity.this, "Account with this" + phone + "number do not Exit", Toast.LENGTH_SHORT).show();
                      loadingBar.dismiss();

                  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }
}
