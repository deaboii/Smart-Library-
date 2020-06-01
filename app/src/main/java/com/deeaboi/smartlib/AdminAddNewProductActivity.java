package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


import static java.nio.file.Paths.get;

public class AdminAddNewProductActivity extends AppCompatActivity
{
    private String categoryName,Auther,Pname, saveCurrentDate,savecurrentTime;
    private Button AddNewBook;
    private EditText InputBookName,InputBookAuther;
    private ImageView InputBookImage;

    private static final int GalleryPick = 1;

    private Uri ImageUri;
    private String productRandomkey,downloadImageUrl;
    private StorageReference productImageRef;
    private DatabaseReference productRef;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        categoryName = getIntent().getExtras().get("category").toString();

        productImageRef= FirebaseStorage.getInstance().getReference().child("Book Images");

        productRef=FirebaseDatabase.getInstance().getReference().child("Products Books");

        AddNewBook=(Button)findViewById(R.id.add_new_book);
        InputBookImage=(ImageView)findViewById(R.id.select_product_image);
        InputBookName=(EditText) findViewById(R.id.product_book_name);
        InputBookAuther=(EditText) findViewById(R.id.product_book_author);
        loadingBar = new ProgressDialog(this);

        InputBookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });

        AddNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });
    }


    private void OpenGallery()
    {
//        Intent galleryIntent= new Intent();
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
       // startActivityForResult(galleryIntent, GalleryPick);

        CropImage.activity(ImageUri)
//                .setAspectRatio(2, 2)
                .start(AdminAddNewProductActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
       // if(requestCode==GalleryPick  && requestCode== RESULT_OK && data!=null)
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
//          ImageUri=data.getData();
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            ImageUri = result.getUri();
          InputBookImage.setImageURI(ImageUri);

        }

    }

    private void ValidateProductData()
    {
       Auther=InputBookAuther.getText().toString();
       Pname=InputBookName.getText().toString();

       if(ImageUri == null)
       {
           Toast.makeText(this, "Please Select Cover Image ", Toast.LENGTH_SHORT).show();
       }
         else if(TextUtils.isEmpty(Auther))
       {
           Toast.makeText(this, "Please Enter Author Name", Toast.LENGTH_SHORT).show();
       }

         else if(TextUtils.isEmpty(Pname))
       {
           Toast.makeText(this, "Please Enter Book Name", Toast.LENGTH_SHORT).show();
       }

         else
       {
           StoreBookInformation();
       }

    }

    private void StoreBookInformation()
    {
        loadingBar.setTitle("Adding New Book");
        loadingBar.setMessage("Please Wait While Adding New Book");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calender = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        saveCurrentDate =currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        savecurrentTime =currentTime.format(calender.getTime());

        productRandomkey =saveCurrentDate +savecurrentTime;

        final StorageReference filepath = productImageRef.child(ImageUri.getLastPathSegment() + productRandomkey + ".jpg");

        final UploadTask uploadTask= filepath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message= e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminAddNewProductActivity.this, "Image Uploaded Successfully :)", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if(!task.isSuccessful())
                        {
                                 throw  task.getException();

                        }


                        downloadImageUrl=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl= task.getResult().toString();

                            Toast.makeText(AdminAddNewProductActivity.this, "Got Book Image url", Toast.LENGTH_SHORT).show();
                            SaveBookInfotoDatabase();
                        }
                    }
                });
            }
        });



    }

    private void SaveBookInfotoDatabase()
    {
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomkey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",savecurrentTime);
        productMap.put("Author",Auther);
        productMap.put("image",downloadImageUrl);
        productMap.put("Category",categoryName);
        productMap.put("pname",Pname);

        productRef.child(productRandomkey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                       if(task.isSuccessful())
                       {
                           Intent intent=new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                           startActivity(intent);

                           loadingBar.dismiss();
                           Toast.makeText(AdminAddNewProductActivity.this, "Book added Successfully...", Toast.LENGTH_SHORT).show();
                           
                       }
                       else
                       {
                           loadingBar.dismiss();
                           String message =task.getException().toString();
                           Toast.makeText(AdminAddNewProductActivity.this, "Error:"+ message, Toast.LENGTH_SHORT).show();
                       }
                    }
                });


    }

}
