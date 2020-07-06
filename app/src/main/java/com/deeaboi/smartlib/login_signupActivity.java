package com.deeaboi.smartlib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class login_signupActivity extends AppCompatActivity
{
    private TextView login,signup;
    private Button admin,teacher,student;
   private  ImageView back;
    private String type ="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        login=(TextView)findViewById(R.id.loginView);
        signup=(TextView)findViewById(R.id.SignupView);

      admin=(Button)findViewById(R.id.admin);
      teacher=(Button)findViewById(R.id.teacher);
      student=(Button)findViewById(R.id.student);

      back=(ImageView)findViewById(R.id.back);

        Intent intent =getIntent();
        Bundle bundle =intent.getExtras();

        if(bundle!=null)
        {
            type = getIntent().getExtras().get("login").toString();
        }



        if(type.equals("login"))
        {
            login.setVisibility(View.VISIBLE);

            admin.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Intent intent = new Intent(login_signupActivity.this,LoginActivity.class);
                    intent.putExtra("value","admin");
                    startActivity(intent);

                }
            });



            teacher.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Intent intent = new Intent(login_signupActivity.this,LoginActivity.class);
                    intent.putExtra("value","teacher");
                    startActivity(intent);

                }
            });


            student.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Intent intent = new Intent(login_signupActivity.this,LoginActivity.class);
                    intent.putExtra("value","student");
                    startActivity(intent);

                }
            });



        }
        else
        {
            signup.setVisibility(View.VISIBLE);

            admin.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent=new Intent(login_signupActivity.this,PhoneActivity.class);
                    intent.putExtra("value","admin");
                    startActivity(intent);

                }
            });

            teacher.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent=new Intent(login_signupActivity.this,PhoneActivity.class);
                    intent.putExtra("value","teacher");
                    startActivity(intent);
                }
            });

            student.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(login_signupActivity.this,PhoneActivity.class);
                    intent.putExtra("value","student");
                    startActivity(intent);

                }
            });




        }

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent1 =new Intent(login_signupActivity.this,MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                finish();
            }
        });




    }
}
