package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.formats.NativeAd;
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
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddBooksActivity extends AppCompatActivity
{
   private ImageView bookimage,scan_image;
   private EditText titles,authors,categoriess,description;
   private Button addBookBtn;
    private Uri ImageUri;
    private String categoryName,Auther,Pname,Descrip,saveCurrentDate,savecurrentTime;
    private ProgressDialog loadingBar;
    private String productRandomkey,downloadImageUrl,key;
    private StorageReference productImageRef;
    private DatabaseReference productRef;
    private static final int GalleryPick = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);

         key =getIntent().getExtras().get("key").toString();

        productImageRef= FirebaseStorage.getInstance().getReference().child("Book Images");

//        productRef= FirebaseDatabase.getInstance().getReference().child("Products Books");
        productRef= FirebaseDatabase.getInstance().getReference().child("College").child(key).child("Library Books");

        bookimage=findViewById(R.id.bookImage);
        titles=findViewById(R.id.bookTitle); //  title=findViewById(R.id.bookTitle);
        authors=findViewById(R.id.bookAuthor);
        categoriess=findViewById(R.id.bookCategory);
        description=findViewById(R.id.bookDescription);
        scan_image=findViewById(R.id.scan_image);
        loadingBar = new ProgressDialog(this);

        addBookBtn=findViewById(R.id.addBookbtn);


        bookimage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenGallery();

            }
        });

        addBookBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ValidateBook();
            }
        });

        scan_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                Intent intent=new Intent(AddBooksActivity.this,ScanToAddActivity.class);
                intent.putExtra("key",key);
                startActivity(intent);



            }
        });



    }



    private void OpenGallery()
    {

        CropImage.activity(ImageUri)
//                .setAspectRatio(2, 2)
                .start(AddBooksActivity.this);


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
            bookimage.setImageURI(ImageUri);

        }




    }




    private void ValidateBook()
    {
        if(ImageUri == null)
        {
            Toast.makeText(this, "Please Select Cover Image", Toast.LENGTH_SHORT).show();
        }
       else if (TextUtils.isEmpty(Auther= authors.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Author Name", Toast.LENGTH_SHORT).show();

        }
       else if(TextUtils.isEmpty(Pname=titles.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Book Name", Toast.LENGTH_SHORT).show();

        }
       else if(TextUtils.isEmpty(Descrip=description.getText().toString()))
        {
            Toast.makeText(this, "Enter Book Description", Toast.LENGTH_SHORT).show();

        }
       else if (TextUtils.isEmpty(categoryName=categoriess.getText().toString()))
        {
            Toast.makeText(this, "Enter Book Category", Toast.LENGTH_SHORT).show();
        }

         else
        {

            StoreBookInfo();
        }


    }

    private void StoreBookInfo()
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
                Toast.makeText(AddBooksActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AddBooksActivity.this, "Image Uploaded Successfully :)", Toast.LENGTH_SHORT).show();
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

                            Toast.makeText(AddBooksActivity.this, "Got Book Image url", Toast.LENGTH_SHORT).show();

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
        productMap.put("Description",Descrip);

        productRef.child(productRandomkey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent=new Intent(AddBooksActivity.this,NewAdminActivity.class);
                            intent.putExtra("key",key);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AddBooksActivity.this, "Book added Successfully...", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message =task.getException().toString();
                            Toast.makeText(AddBooksActivity.this, "Error:"+ message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


}
