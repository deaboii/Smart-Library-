package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class NewPasswordActivity extends AppCompatActivity
{
    private EditText newPassword,confirmPassword;
    private Button changePassword;
    private String PhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

         PhoneNumber=getIntent().getStringExtra("phonenumber");

        newPassword=(EditText)findViewById(R.id.new_password);
        confirmPassword=(EditText)findViewById(R.id.confirm_password);
        changePassword=(Button)findViewById(R.id.change_password);

        changePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               validate(newPassword,confirmPassword);
            }
        });

    }

    private void validate(EditText newPassword, EditText confirmPassword)
    {
        String newpasskey=newPassword.getText().toString();
        String confirmpasskey =confirmPassword.getText().toString();

        if(TextUtils.isEmpty(newpasskey))
        {
            Toast.makeText(this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmpasskey))
        {
            Toast.makeText(this, "Enter confirm Password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            check(newpasskey,confirmpasskey);

        }

    }

    private void check(String newpasskey, String confirmpasskey)
    {
        if(newpasskey.equals(confirmpasskey))
        {
           updatepassword(newpasskey);
        }
        else
        {
            Toast.makeText(this, "Password not Matching", Toast.LENGTH_SHORT).show();
        }

    }

    private void updatepassword(final String newpasskey)
    {
        final DatabaseReference PassRef;
        PassRef = FirebaseDatabase.getInstance().getReference();
        PassRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Users").child(PhoneNumber).exists())
                {
                    HashMap<String,Object> userDataMap= new HashMap<>();

                    userDataMap.put("password",newpasskey);

                    PassRef.child("Users").child(PhoneNumber).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(NewPasswordActivity.this, "Password Changed Successfully.", Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(NewPasswordActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }

                                    else
                                    {

                                        Toast.makeText(NewPasswordActivity.this, "Error : Please Try Again...", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }

}
