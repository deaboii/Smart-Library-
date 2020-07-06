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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ControlActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener
{
    RadioGroup radioGroup;

    private EditText rollnumber;
    private Button managebtn;
    private String key="",type="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        key=getIntent().getStringExtra("key");

        rollnumber=findViewById(R.id.rollnumber);
        managebtn=findViewById(R.id.managebtn);

        radioGroup=findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);


        managebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                manage();
            }
        });

    }

    private void manage()
    {
        if(TextUtils.isEmpty(rollnumber.getText().toString()))
        {

            Toast.makeText(this, "Please Enter Student Roll Number..", Toast.LENGTH_SHORT).show();

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
                        if(dataSnapshot.child("College").child(key).child("Student").child(rollnumber.getText().toString()).exists() ||dataSnapshot.child("College").child(key).child("Teacher").child(rollnumber.getText().toString()).exists() )
                        {


                            if(type.equals("issue"))
                            {

                                Intent intent = new Intent(ControlActivity.this,Main2Activity.class);
                                intent.putExtra("roll",rollnumber.getText().toString());
                                intent.putExtra("Admin","Admin");
                                intent.putExtra("key",key);
                                startActivity(intent);

                            }
                            else if(type.equals("reference"))
                            {
                                Intent intent = new Intent(ControlActivity.this,Main2Activity.class);
                                intent.putExtra("roll",rollnumber.getText().toString());
                                intent.putExtra("Admin","Admin1");
                                intent.putExtra("key",key);
                                startActivity(intent);
                            }
                            else
                                if(type.equals("teacher"))
                            {
                                Intent intent = new Intent(ControlActivity.this,Main2Activity.class);
                                intent.putExtra("roll",rollnumber.getText().toString());
                                intent.putExtra("Admin","teacher");
                                intent.putExtra("key",key);
                                startActivity(intent);


                            }
                                else
                                    {

                                        Toast.makeText(ControlActivity.this, "Please select one", Toast.LENGTH_SHORT).show();

                                }


                        }
                        else
                        {

                            Toast.makeText(ControlActivity.this, "Account with this number not exist ", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });




        }


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        switch (checkedId)
        {
         case R.id.radio_issue_books:
                type="issue";
                break;

            case R.id.radio_reference_book:
                type="reference";
                break;

            case R.id.radio_teacher_books:
                type="teacher";
                break;


        }

    }
}
