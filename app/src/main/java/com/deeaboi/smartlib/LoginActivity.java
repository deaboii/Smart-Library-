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

import com.deeaboi.smartlib.Model.Admin;
import com.deeaboi.smartlib.Model.Teacher;
import com.deeaboi.smartlib.Model.Users;
import com.deeaboi.smartlib.Prevalent.AdminPrevalent;
import com.deeaboi.smartlib.Prevalent.Prevalent;
import com.deeaboi.smartlib.Prevalent.TeacherPrevalent;
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
     private EditText InputKey,InputPassword,InputPhoneno;
     private Button LoginButton;
     private ProgressDialog loadingBar;
     private String parentDbName = "Users";
     private CheckBox chkBoxRememberMe,ChkBoxRememberMeAdmin,ChkBoxRememberMeTeacher;
     private TextView Forgetpassword;
     private  DatabaseReference UsersRef;
    //private FirebaseAuth mAuth;
    private String type="",phone;




    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Intent intent =getIntent();
        Bundle bundle =intent.getExtras();
        if(bundle!=null)
        {
            type = getIntent().getExtras().get("value").toString();
        }


        UsersRef=FirebaseDatabase.getInstance().getReference().child("College");

        LoginButton = (Button)findViewById(R.id.login_btn);

        InputPhoneno=(EditText)findViewById(R.id.login_phone_number_input);

        InputKey = (EditText)findViewById(R.id.login_key); //chnge
        InputPassword= (EditText)findViewById(R.id.login_password_input);
        Forgetpassword=(TextView) findViewById(R.id.forget_password_link);
//        AdminLink=(TextView)findViewById(R.id.admin_panel_link);
//        NotAdminLink=(TextView)findViewById(R.id.not_admin_panel_link);

        loadingBar = new ProgressDialog(this);


        chkBoxRememberMe=(CheckBox)findViewById(R.id.remember_me_chk);// user working perfectly
        ChkBoxRememberMeAdmin=(CheckBox)findViewById(R.id.remember_me_chk_admin); // for admin
        ChkBoxRememberMeTeacher=(CheckBox)findViewById(R.id.remember_me_chk_teacher) ; // for tecahers



        Paper.init(this);



        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LoginUser();
            }
        });

        if(type.equals("admin"))
        {
            LoginButton.setText("Admin Login");
            parentDbName="Admins";
            chkBoxRememberMe.setVisibility(View.GONE);
            ChkBoxRememberMeTeacher.setVisibility(View.GONE);
            InputPhoneno.setVisibility(View.GONE);
            ChkBoxRememberMeAdmin.setVisibility(View.VISIBLE);


        }
        else
            if(type.equals("teacher"))
        {
           LoginButton.setText("Teacher Login");
            parentDbName="Teacher";
            chkBoxRememberMe.setVisibility(View.GONE);
            ChkBoxRememberMeAdmin.setVisibility(View.GONE);
            InputPhoneno.setVisibility(View.VISIBLE);
            ChkBoxRememberMeTeacher.setVisibility(View.VISIBLE);


        }
        else
            if(type.equals("student"))
            {

               LoginButton.setText("Student Login");
                parentDbName="Users";
                chkBoxRememberMe.setVisibility(View.VISIBLE);
                InputPhoneno.setVisibility(View.VISIBLE);
                ChkBoxRememberMeTeacher.setVisibility(View.GONE);


            }

//        AdminLink.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//            // LoginButton.setText("Admin Login");
//             AdminLink.setVisibility(View.INVISIBLE);
//             NotAdminLink.setVisibility(View.VISIBLE);
//           //  parentDbName="Admins";
//           //  chkBoxRememberMe.setVisibility(View.INVISIBLE);
//
//            }
//        });
//
//        NotAdminLink.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//              //  LoginButton.setText("Login");
//                AdminLink.setVisibility(View.VISIBLE);
//                NotAdminLink.setVisibility(View.INVISIBLE);
//                parentDbName="Users";
//                chkBoxRememberMe.setVisibility(View.VISIBLE);
//            }
//        });


        Forgetpassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                    Intent intent=new Intent(LoginActivity.this,ForgetActivity.class);
                    intent.putExtra("type",type); //admin,teacher,student
                    startActivity(intent);


            }
        });


    }

    private void LoginUser()
    {

        if(parentDbName.equals("Admins"))
        {
            String key =InputKey.getText().toString(); //InputPhoneNumber.getText().toString()
            String password =InputPassword.getText().toString();

            if(TextUtils.isEmpty(key))
            {
                Toast.makeText(this, "Please enter your College ID.", Toast.LENGTH_SHORT).show();
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

                AllowAccessToAccount(key,password);

            }

        }
        else if(parentDbName.equals("Users"))
            {

                String key =InputKey.getText().toString(); //InputPhoneNumber.getText().toString()
                 phone=InputPhoneno.getText().toString();
                String password =InputPassword.getText().toString();

                if(TextUtils.isEmpty(phone))
                {
                    Toast.makeText(this, "Please enter your phone no. ", Toast.LENGTH_SHORT).show();

                }
                else if(TextUtils.isEmpty(key))
                {
                    Toast.makeText(this, "Please enter your College id.", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                }
                else

                {
                    loadingBar.setTitle("Login Account");
                    loadingBar.setMessage("Please Wait While Login to Your Account");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    AllowAccessToAccount(key,password);

                }



            }
            else if(parentDbName.equals("Teacher"))
            {

                String key =InputKey.getText().toString(); //InputPhoneNumber.getText().toString()
                phone=InputPhoneno.getText().toString();
                String password =InputPassword.getText().toString();

                if(TextUtils.isEmpty(phone))
                {
                    Toast.makeText(this, "Please enter your phone no. ", Toast.LENGTH_SHORT).show();

                }
                else if(TextUtils.isEmpty(key))
                {
                    Toast.makeText(this, "Please enter your College id.", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                }
                else

                {
                    loadingBar.setTitle("Login Account");
                    loadingBar.setMessage("Please Wait While Login to Your Account");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    AllowAccessToAccount(key,password);

                }

            }





//        String key =InputKey.getText().toString(); //InputPhoneNumber.getText().toString()
//        String password =InputPassword.getText().toString();
//
//          if(TextUtils.isEmpty(key))
//            {
//                Toast.makeText(this, "Please enter your Login id.", Toast.LENGTH_SHORT).show();
//            }
//              else if(TextUtils.isEmpty(password))
//                   {
//                       Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
//                   }
//           else
//
//          {
//              loadingBar.setTitle("Login Account");
//              loadingBar.setMessage("Please Wait While Login to Your Account");
//              loadingBar.setCanceledOnTouchOutside(false);
//              loadingBar.show();
//
//              AllowAccessToAccount(key,password);
//
//          }
    }

    private void AllowAccessToAccount(final String key, final String password)
    {

            if(chkBoxRememberMe.isChecked()) // for student
            {
                Paper.book().write(Prevalent.UserPhoneKey,phone);
                Paper.book().write(Prevalent.UserPasswordKey,password);
                Paper.book().write(Prevalent.CollegeKeyNo,key);
            }

            if(ChkBoxRememberMeAdmin.isChecked()) // for admin
            {

                Paper.book().write(AdminPrevalent.AdminPasswordKey,password);
                Paper.book().write(AdminPrevalent.AdminCollegeKey,key);


            }
            if (ChkBoxRememberMeTeacher.isChecked())
            {

                Paper.book().write(TeacherPrevalent.TeacherPhoneKey,phone);
                Paper.book().write(TeacherPrevalent.TeacherCollegeKeyNo,key);
                Paper.book().write(TeacherPrevalent.TeacherPasswordKey,password);


            }




        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {



                if(parentDbName.equals("Admins"))
                {


                    if(dataSnapshot.child("College").child(key).exists())
                    {

                        final Admin userData =dataSnapshot.child("College").child(key).child("Data").getValue(Admin.class);

                        if(userData.getKey().equals(key))
                        {
                            if(userData.getPassword().equals(password))
                            {
                                Toast.makeText(LoginActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                // Intent intent=new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                Intent intent=new Intent(LoginActivity.this,NewAdminActivity.class);
                                intent.putExtra("key",key);

                                startActivity(intent);

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
                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Account with this number is not exist", Toast.LENGTH_SHORT).show();

                    }



                }
                else if(parentDbName.equals("Users"))

                // ypu need tocheak for key and phone  and password if exist -----

                {
                    if(dataSnapshot.child("College").child(key).exists())
                    {
                        if(dataSnapshot.child("College").child(key).child("Student").child(phone).exists())
                        {
                            final Users userData =dataSnapshot.child("College").child(key).child("Student").child(phone).getValue(Users.class);


                            if(userData.getPhone().equals(phone))
                            {
                                if(userData.getPassword().equals(password))
                                {



                                    FirebaseInstanceId.getInstance().getInstanceId()
                                            .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                                @Override
                                                public void onSuccess(InstanceIdResult instanceIdResult)
                                                {
                                                    String deviceToken = instanceIdResult.getToken();


                                                    UsersRef.child(key).child("Student").child(phone).child("device_token")
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
                                else
                                {
                                    loadingBar.dismiss();
                                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                }



                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Account with this number not exist", Toast.LENGTH_SHORT).show();

                            }


                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Account with this number not exist", Toast.LENGTH_SHORT).show();

                        }

                    }

                    else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Enter a correct college Id", Toast.LENGTH_SHORT).show();

                    }





                }
                else if(parentDbName.equals("Teacher"))
                    {

                        if(dataSnapshot.child("College").child(key).exists())

                        {
                            if(dataSnapshot.child("College").child(key).child("Teacher").child(phone).exists())
                            {


                                final Teacher userData =dataSnapshot.child("College").child(key).child("Teacher").child(phone).getValue(Teacher.class);

                                if(userData.getPhone().equals(phone))
                                {
                                    if(userData.getPassword().equals(password))
                                    {


                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(LoginActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent=new Intent(LoginActivity.this,TeacherLoginActivity.class);
                                        intent.putExtra("key",key);
                                        intent.putExtra("phone",phone);
                                        TeacherPrevalent.CurrentTeacher =userData;
                                        startActivity(intent);


                                    }
                                    else
                                    {

                                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();


                                    }


                                }
                                else
                                {

                                    Toast.makeText(LoginActivity.this, "Account with this number not exist", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();


                                }


                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Account with this number not exist", Toast.LENGTH_SHORT).show();

                            }


                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Enter a correct college Id", Toast.LENGTH_SHORT).show();

                        }



                    }


//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------




//
//
////                  if(dataSnapshot.child(parentDbName).child(phone).exists())
//                if(dataSnapshot.child("College").child(phone).exists())   //insteed of phone i amusing college id
//
//                  {
//                      final Users userData =dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
//
//                      if(userData.getPhone().equals(phone))
//                      {
//                          if(userData.getPassword().equals(password))
//                          {
//
//                              if(parentDbName.equals("Admins"))
//                              {
//
//                                  Toast.makeText(LoginActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
//                                  loadingBar.dismiss();
//                                // Intent intent=new Intent(LoginActivity.this,AdminCategoryActivity.class);
//                                  Intent intent=new Intent(LoginActivity.this,NewAdminActivity.class);
//
//                                  startActivity(intent);
//
//                              }
//                              else if(parentDbName.equals("Users"))
//                              {
//
//                                  FirebaseInstanceId.getInstance().getInstanceId()
//                                          .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
//                                              @Override
//                                              public void onSuccess(InstanceIdResult instanceIdResult)
//                                              {
//                                                  String deviceToken = instanceIdResult.getToken();
//                                                  UsersRef.child(phone).child("device_token")
//                                                          .setValue(deviceToken)
//                                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                              @Override
//                                                              public void onComplete(@NonNull Task<Void> task)
//                                                              {
//                                                                  if (task.isSuccessful())
//                                                                  {
//                                                                      // Sign in success, update UI with the signed-in user's information
//                                                                      Toast.makeText(LoginActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
//                                                                      loadingBar.dismiss();
//                                                                      Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
//                                                                      Prevalent.CurrentOnlineUser =userData;
//                                                                      startActivity(intent);
//
//
//                                                                  }
//                                                              }
//                                                          });
//                                              }
//                                          });
//
//
//                              }
//
//                          }
//                          else
//                          {
//                              loadingBar.dismiss();
//                              Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
//                          }
//
//                      }
//
//
//                  }
//                    else
//                  {
//
//                      Toast.makeText(LoginActivity.this, "Account with this" + phone + "number do not Exit", Toast.LENGTH_SHORT).show();
//                      loadingBar.dismiss();
//
//                  }
//

















            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }
}
