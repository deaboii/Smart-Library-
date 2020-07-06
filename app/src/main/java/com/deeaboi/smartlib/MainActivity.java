package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.deeaboi.smartlib.Model.Admin;
import com.deeaboi.smartlib.Model.Teacher;
import com.deeaboi.smartlib.Model.Users;
import com.deeaboi.smartlib.Prevalent.AdminPrevalent;
import com.deeaboi.smartlib.Prevalent.Prevalent;
import com.deeaboi.smartlib.Prevalent.TeacherPrevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity

{

     private Button joinNowButton,loginButton;
     private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton=(Button)findViewById(R.id.main_join_now_btn);
        loginButton=(Button)findViewById(R.id.main_login_btn);
        loadingBar=new ProgressDialog(this);

        Paper.init(this);


        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(intent);

                Intent intent =new Intent(MainActivity.this,login_signupActivity.class);
                intent.putExtra("login","login");
                startActivity(intent);


            }

        });

        joinNowButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                Intent intent=new Intent(MainActivity.this,PhoneActivity.class);
//                startActivity(intent);

                Intent intent = new Intent(MainActivity.this,login_signupActivity.class);
                startActivity(intent);


            }
        });


         String UserPhoneKey= Paper.book().read(Prevalent.UserPhoneKey);
         String UserPasswordKey= Paper.book().read(Prevalent.UserPasswordKey);
         String CollegeKeyNo = Paper.book().read(Prevalent.CollegeKeyNo);

         if(UserPhoneKey != "" && UserPasswordKey != "" && CollegeKeyNo !="")
         {

           if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey) && !TextUtils.isEmpty(CollegeKeyNo) )
           {

               AllowAccess(UserPhoneKey,UserPasswordKey,CollegeKeyNo);


               loadingBar.setTitle("Already Logged in");
               loadingBar.setMessage("Please Wait...");
               loadingBar.setCanceledOnTouchOutside(false);
               loadingBar.show();

           }

         }

         //-----------------------------------------Below for admin remember me-----------------------------------------------------------------------------------------

//        String AdminPhoneKey= Paper.book().read(AdminPrevalent.AdminPhoneKey);   //no need


        String AdminPasswordKey= Paper.book().read(AdminPrevalent.AdminPasswordKey);
        String AdminCollegeKeyNo = Paper.book().read(AdminPrevalent.AdminCollegeKey);

              if(AdminCollegeKeyNo!="" && AdminPasswordKey!="" )
              {
                  if (!TextUtils.isEmpty(AdminPasswordKey) && !TextUtils.isEmpty(AdminCollegeKeyNo)) {

                      AllowAccessAdmin(AdminCollegeKeyNo, AdminPasswordKey);

                      loadingBar.setTitle("Already Logged in");
                      loadingBar.setMessage("Please Wait...");
                      loadingBar.setCanceledOnTouchOutside(false);
                      loadingBar.show();


                  }
              }

        //-------------------------------------------------------fot teacher remember


        String TeacherPhoneKey =Paper.book().read(TeacherPrevalent.TeacherPhoneKey);
        String TeacherPasswordkey =Paper.book().read(TeacherPrevalent.TeacherPasswordKey);
        String TeacherCollegekey=Paper.book().read(TeacherPrevalent.TeacherCollegeKeyNo);

        if(TeacherPhoneKey !="" && TeacherCollegekey!="" && TeacherPasswordkey !="")
        {

            if (!TextUtils.isEmpty(TeacherPhoneKey) && !TextUtils.isEmpty(TeacherCollegekey) && !TextUtils.isEmpty(TeacherPasswordkey)) {
                AllowAccessTeacher(TeacherCollegekey, TeacherPhoneKey, TeacherPasswordkey);
                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please Wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();


            }
        }



    }

    private void AllowAccessTeacher(final String teacherCollegekey, final String teacherPhoneKey, final String teacherPasswordkey)
    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("College").child(teacherCollegekey).child("Teacher").child(teacherPhoneKey).exists())
                {
                    Teacher teacherdata =dataSnapshot.child("College").child(teacherCollegekey).child("Teacher").child(teacherPhoneKey).getValue(Teacher.class);

                    if(teacherdata.getPhone().equals(teacherPhoneKey))
                    {
                        if(teacherdata.getKey().equals(teacherCollegekey))
                        {
                            if(teacherdata.getPassword().equals(teacherPasswordkey))
                            {

                                Toast.makeText(MainActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent=new Intent(MainActivity.this,TeacherLoginActivity.class);
                                intent.putExtra("key",teacherCollegekey);
                                intent.putExtra("phone",teacherPhoneKey);
                                startActivity(intent);


                            }
                            else
                            {

                                Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }


                        }


                    }

                }

                else
                {
                    Toast.makeText(MainActivity.this, "Account with "+teacherPhoneKey+" no not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });


    }

    private void AllowAccessAdmin(final String adminCollegeKeyNo, final String adminPasswordKey)
    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.child("College").child(adminCollegeKeyNo).exists())
                {
                    Admin admindata =dataSnapshot.child("College").child(adminCollegeKeyNo).child("Data").getValue(Admin.class);

                    if(admindata.getKey().equals(adminCollegeKeyNo))
                    {
                        if(admindata.getPassword().equals(adminPasswordKey))
                        {

                            Toast.makeText(MainActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent=new Intent(MainActivity.this,NewAdminActivity.class);
                            intent.putExtra("key",adminCollegeKeyNo);
                            startActivity(intent);



                        }
                        else
                        {

                            Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();


                        }


                    }


                }
                else {

                    Toast.makeText(MainActivity.this, "College with this "+adminCollegeKeyNo+" id not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });





    }


    private void AllowAccess(final String phone, final String password, final String collegeKeyNo)
    {



        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("College").child(collegeKeyNo).child("Student").child(phone).exists())
                {
                    Users userData =dataSnapshot.child("College").child(collegeKeyNo).child("Student").child(phone).getValue(Users.class);

                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {

                            Toast.makeText(MainActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent=new Intent(MainActivity.this,HomeActivity.class);

                            Prevalent.CurrentOnlineUser = userData;
                            startActivity(intent);
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }

                    }


                }
                else
                {

                    Toast.makeText(MainActivity.this, "Account with this" + phone + "number do not Exit", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}



