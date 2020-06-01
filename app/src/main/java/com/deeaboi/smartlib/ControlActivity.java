package com.deeaboi.smartlib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ControlActivity extends AppCompatActivity {

    private EditText rollnumber;
    private CheckBox chkissuebook,chkrefrencebook;
    private Button managebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        rollnumber=findViewById(R.id.rollnumber);
        managebtn=findViewById(R.id.managebtn);

        chkissuebook=(CheckBox)findViewById(R.id.Issued_Books_chk);
        chkrefrencebook=(CheckBox)findViewById(R.id.Refrence_Books_chk);

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
            if(chkissuebook.isChecked())
            {

                Intent intent = new Intent(ControlActivity.this,Main2Activity.class);
                intent.putExtra("roll",rollnumber.getText().toString());
                intent.putExtra("Admin","Admin");
                startActivity(intent);

            }
            else if(chkrefrencebook.isChecked())
            {
                Intent intent = new Intent(ControlActivity.this,Main2Activity.class);
                intent.putExtra("roll",rollnumber.getText().toString());
                intent.putExtra("Admin","Admin1");
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "Please select Issue or Refrenece", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
