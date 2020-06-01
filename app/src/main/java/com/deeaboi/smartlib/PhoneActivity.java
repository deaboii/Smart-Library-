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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity
{
     private EditText phoneno,verifcode;
     private Button sendbtn,verifybtn;
     private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
     private String mVerificationId ;
     private PhoneAuthProvider.ForceResendingToken mResendToken;
     private FirebaseAuth mAuth;
     private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        phoneno =(EditText)findViewById(R.id.phone_no_input);
        verifcode=(EditText)findViewById(R.id.verifiaction_input);
        sendbtn=(Button)findViewById(R.id.send_verification_code);
        verifybtn=(Button)findViewById(R.id.verfycode_btn);
        mAuth=FirebaseAuth.getInstance();
        LoadingBar=new ProgressDialog(this);

         sendbtn.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View view)
             {
                 verifyphone();
             }
         });

         callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
         {
             @Override
             public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
             {
                 signInWithPhoneAuthCredential(phoneAuthCredential);
             }

             @Override
             public void onVerificationFailed(@NonNull FirebaseException e)
             {

                 LoadingBar.dismiss();
                 Toast.makeText(PhoneActivity.this, "Invalid phone Number", Toast.LENGTH_SHORT).show();
                 phoneno.setVisibility(View.VISIBLE);
                 sendbtn.setVisibility(View.VISIBLE);

                 verifcode.setVisibility(View.INVISIBLE);
                 verifybtn.setVisibility(View.INVISIBLE);

             }

             public void onCodeSent(@NonNull String verificationId,
                                    @NonNull PhoneAuthProvider.ForceResendingToken token)
             {
                 LoadingBar.dismiss();
                 mVerificationId = verificationId;
                 mResendToken = token;
                 Toast.makeText(PhoneActivity.this, "Code Send Successfully", Toast.LENGTH_SHORT).show();

                   phoneno.setVisibility(View.INVISIBLE);
                   sendbtn.setVisibility(View.INVISIBLE);

                   verifcode.setVisibility(View.VISIBLE);
                   verifybtn.setVisibility(View.VISIBLE);



             }



         };
         verifybtn.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View view)
             {
                 phoneno.setVisibility(View.INVISIBLE);
                 sendbtn.setVisibility(View.INVISIBLE);

                 String verificationCode = verifcode.getText().toString();
                 if(TextUtils.isEmpty(verificationCode))
                 {
                     Toast.makeText(PhoneActivity.this, "Please enter Verification Code", Toast.LENGTH_SHORT).show();

                 }
                 else
                 {
                     LoadingBar.setTitle("Code Verification");
                     LoadingBar.setMessage("please Wait, While verifing Your Code...");
                     LoadingBar.setCanceledOnTouchOutside(false);
                     LoadingBar.show();

                     PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,verificationCode);
                     signInWithPhoneAuthCredential(credential);

                 }
             }
         });
    }

    private void verifyphone()
    {
        String phonenumber=phoneno.getText().toString();
       // String verificationcode =verifcode.getText().toString();
        if(TextUtils.isEmpty(phonenumber))
        {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();

        }
        else
        {
            validatephone(phonenumber);
        }

    }

    private void validatephone(final String phonenumber)
    {
        DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("Users").child(phonenumber).exists())
                {
                    Toast.makeText(PhoneActivity.this, "Account already exist with this phone number ", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(PhoneActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else
                {

                    LoadingBar.setTitle("Phone Verification");
                    LoadingBar.setMessage("please Wait, While verifing Your phone number...");
                    LoadingBar.setCanceledOnTouchOutside(false);
                    LoadingBar.show();
                    String indiaphonenumber ="+91"+phonenumber;

                    PhoneAuthProvider.getInstance().verifyPhoneNumber
                            (
                            indiaphonenumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneActivity.this,               // Activity (for callback binding)
                            callbacks
                            );        // OnVerificationStateChangedCallbacks
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError
            ) {

            }
        });



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            LoadingBar.dismiss();
                            Intent intent=new Intent(PhoneActivity.this,RegisterActivity.class);
                            intent.putExtra("phonenumber",phoneno.getText().toString());
                            startActivity(intent);
                        }
                        else
                         {
                             String message =task.getException().toString();
                             Toast.makeText(PhoneActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





}
