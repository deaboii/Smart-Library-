package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminEditActivity extends AppCompatActivity
{

    private EditText name,author;
    private Button applychangebtn;
    private ImageView image;
    private String productId="",key="";
    private DatabaseReference editRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit);

        productId=getIntent().getStringExtra("pid");
        key=getIntent().getStringExtra("key");

       // editRef= FirebaseDatabase.getInstance().getReference().child("Products Books").child(productId);
        editRef= FirebaseDatabase.getInstance().getReference().child("College").child(key).child("Library Books").child(productId);


        applychangebtn=findViewById(R.id.apply_btn_edit);
        name=findViewById(R.id.edit_name);
        author=findViewById(R.id.edit_author);
        image=findViewById(R.id.edit_image);

         DisplayBooks();

         applychangebtn.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View view)
             {
                 change();
             }
         });
    }


    private void DisplayBooks()
    {
        editRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                   if(dataSnapshot.exists())
                   {
                        String Ename= dataSnapshot.child("pname").getValue().toString();
                        String Eauthor =dataSnapshot.child("Author").getValue().toString();
                        String Eimage =dataSnapshot.child("image").getValue().toString();

                        name.setText(Ename);
                        author.setText(Eauthor);
                        Picasso.get().load(Eimage).into(image);

                   }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }

    private void change()
    {
      String EditName=name.getText().toString();
      String EditAuthor=author.getText().toString();

      if(EditName.equals(""))
      {
          Toast.makeText(this, "Please Enter Book Name...", Toast.LENGTH_SHORT).show();
      }
       else if(EditAuthor.equals(""))
      {

          Toast.makeText(this, "Plaese Enter Author Name...", Toast.LENGTH_SHORT).show();
      }
       else
      {
          HashMap<String,Object> productMap= new HashMap<>();
          productMap.put("pid",productId);
          productMap.put("pname",EditName);
          productMap.put("Author",EditAuthor);

          editRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>()
          {
              @Override
              public void onComplete(@NonNull Task<Void> task)
              {
                  if(task.isSuccessful())
                  {
                      Toast.makeText(AdminEditActivity.this, "Book Upadated Successfully..", Toast.LENGTH_SHORT).show();

                      //Intent intent=new Intent(AdminEditActivity.this,AdminCategoryActivity.class);
                      Intent intent=new Intent(AdminEditActivity.this,NewAdminActivity.class);
                      intent.putExtra("key",key);
                      startActivity(intent);
                      finish();
                  }
              }
          });

      }
    }
}
