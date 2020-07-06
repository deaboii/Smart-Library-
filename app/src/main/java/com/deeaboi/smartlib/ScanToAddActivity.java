package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.deeaboi.smartlib.Model.Admin;
import com.deeaboi.smartlib.Prevalent.AdminPrevalent;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ScanToAddActivity extends AppCompatActivity
{

    private ImageView bookimage;
    private Button AddBookBtn;
    private EditText authorName,bookCategory,bookTitle,bookDescription;
    private String BASE_URL="https://www.googleapis.com/books/v1/volumes?q=isbn:";
    private RequestQueue mRequestQueue;
    private String thumbnail,saveCurrentDate,savecurrentTime,productRandomkey;
    private DatabaseReference productRef;
    private String keyval="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_to_add);

        keyval =getIntent().getExtras().get("key").toString();

        bookimage=findViewById(R.id.bookImage);
        authorName=findViewById(R.id.bookAuthor);
        bookTitle=findViewById(R.id.bookTitle);
        bookCategory=findViewById(R.id.bookCategory);
        bookDescription=findViewById(R.id.bookDescription);
        AddBookBtn=findViewById(R.id.addBookbtn);

        mRequestQueue = Volley.newRequestQueue(this);

        productRef= FirebaseDatabase.getInstance().getReference().child("College").child(keyval).child("Library Books");

        Scan();

        AddBookBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                validateBook();
            }
        });


        Calendar calender = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        saveCurrentDate =currentDate.format(calender.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        savecurrentTime =currentTime.format(calender.getTime());
        productRandomkey =saveCurrentDate+savecurrentTime;

        //Toast.makeText(this, "key: "+keyval, Toast.LENGTH_SHORT).show();

    }

    private void validateBook()
    {
        if(TextUtils.isEmpty(authorName.getText().toString()))
        {
            Toast.makeText(this, "Enter author name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(bookTitle.getText().toString()))
        {
            Toast.makeText(this, "Enter Book Title", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(bookCategory.getText().toString()))
        {

            Toast.makeText(this, "Enter Book Catergory", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(bookDescription.getText().toString()))
        {

            Toast.makeText(this, "Enter Book Description", Toast.LENGTH_SHORT).show();
        }
        else {

            AddtoDatabase();

        }




    }

    private void AddtoDatabase()
    {

              SaveBookInfotoDatabase();

    }

    private void SaveBookInfotoDatabase()
    {
        String author =authorName.getText().toString();
        String category =bookCategory.getText().toString();
        String title = bookTitle.getText().toString();
        String Description = bookDescription.getText().toString();


        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomkey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",savecurrentTime);
        productMap.put("Author",author);
        productMap.put("image",thumbnail);
        productMap.put("Category",category);
        productMap.put("pname",title);
        productMap.put("Description",Description);

        productRef.child(productRandomkey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent=new Intent(ScanToAddActivity.this,NewAdminActivity.class);
                            intent.putExtra("key",keyval);
                            startActivity(intent);

                            Toast.makeText(ScanToAddActivity.this, "Book added Successfully...", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {

                            String message =task.getException().toString();
                            Toast.makeText(ScanToAddActivity.this, "Error:"+ message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }



    private void Scan()
    {
        IntentIntegrator integrator =new IntentIntegrator(ScanToAddActivity.this);
        integrator.setCaptureActivity(AnyOrientationActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan Barcode");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(scanningResult.getContents() == null)
        {
            Intent originalIntent = scanningResult.getOriginalIntent();
            if (originalIntent == null)
            {
                Log.d("MainActivity", "Cancelled scan");

                Intent intent = new Intent(ScanToAddActivity.this,AddBooksActivity.class);
                intent.putExtra("key",keyval);
                startActivity(intent);
                finish();

                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
            else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION))
            {
                Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                Intent intent = new Intent(ScanToAddActivity.this,AddBooksActivity.class);
                intent.putExtra("key",keyval);
                startActivity(intent);
                finish();
                Toast.makeText(this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
            }
        }

        else
        {

            if (scanningResult != null)
            {

                //get content from Intent Result
                String scanContent = scanningResult.getContents();
                //get format name of data scanned
                String scanFormat = scanningResult.getFormatName();
                Log.v("SCAN", "content: " + scanContent + " - format: " + scanFormat);
                if (scanContent != null && scanFormat != null && scanFormat.equalsIgnoreCase("EAN_13"))
                {

                    // Toast.makeText(this, "result:" + scanContent+"format"+scanFormat, Toast.LENGTH_SHORT).show();
                    bookshow(scanContent);
                }
                else
                {
                    //not ean
                    Toast toast = Toast.makeText(getApplicationContext(), "Not a valid scan!", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(ScanToAddActivity.this,AddBooksActivity.class);
                    intent.putExtra("key",keyval);
                    startActivity(intent);
                    finish();

                }
            }
            else
            {

                //invalid scan data or scan canceled
                Toast toast = Toast.makeText(getApplicationContext(), "No book scan data received!", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(ScanToAddActivity.this,AddBooksActivity.class);
                intent.putExtra("key",keyval);
                startActivity(intent);
                finish();
            }



        }



    }

    private void bookshow(String scanContent)
    {

        Uri uri=Uri.parse(BASE_URL+scanContent);
        Uri.Builder buider = uri.buildUpon();
        parseJson(buider.toString());




    }

    private void parseJson(String keys)
    {

        final JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, keys.toString(), null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                String title ="Not Available";
                                String author ="Not Available";
                                String publishedDate = "NoT Available";
                                String description = "No Description";
                                String categories ="Not Avialable";
                                int pageCount = 1000;
                                // String categories = "No categories Available ";
                                //String buy ="";

                                String price = "NOT_FOR_SALE";
                                try {

                                    JSONArray items = response.getJSONArray("items");

                                    for (int i = 0 ; i< items.length() ;i++)
                                    {
                                        JSONObject item = items.getJSONObject(i);
                                        JSONObject volumeInfo = item.getJSONObject("volumeInfo");



                                        try{
                                            title = volumeInfo.getString("title");

                                            JSONArray authors = volumeInfo.getJSONArray("authors");
                                            if(authors.length() == 1){
                                                author = authors.getString(0);
                                            }else {
                                                author = authors.getString(0) + "|" +authors.getString(1);
                                            }


                                          //  publishedDate = volumeInfo.getString("publishedDate");
                                         //   pageCount = volumeInfo.getInt("pageCount");
//



                                            JSONObject saleInfo = item.getJSONObject("saleInfo");
                                            JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                                            price = listPrice.getString("amount") + " " +listPrice.getString("currencyCode");
                                            description = volumeInfo.getString("description");
                                            // buy = saleInfo.getString("buyLink");
                                            categories = volumeInfo.getJSONArray("categories").getString(0);

                                        }
                                        catch (Exception e)
                                        {

                                        }
                                        try
                                        {
                                            thumbnail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");


                                        }
                                        catch (JSONException e)
                                        {
                                            thumbnail="";
                                            e.printStackTrace();
                                        }

//                                        try
//                                        {
//                                            double decNumStars = Double.parseDouble(volumeInfo.getString("averageRating"));//------------------------------------------------------------------------------------
//                                            int numStars = (int)decNumStars;
//                                            starlayout.setTag(numStars);
//                                            starlayout.removeAllViews();
//                                            for(int s=0; s<numStars; s++){
//                                                starViews[s].setImageResource(R.drawable.star);
//                                                starlayout.addView(starViews[s]);
//                                            }
//
//                                        }
//                                        catch (NumberFormatException e)
//                                        {
//                                            e.printStackTrace();
//                                        }
//                                        catch (JSONException e)
//                                        {
//                                            starlayout.removeAllViews();
//                                            e.printStackTrace();
//                                        }


//                                        try
//                                        {
//                                            ratingCount.setText(" : "+volumeInfo.getString("ratingsCount")+" ratings");
//                                        }
//                                        catch(JSONException jse)
//                                        {
//                                            ratingCount.setText("");
//                                            jse.printStackTrace();
//                                        }



                                   //     previewLink = volumeInfo.getString("previewLink");
                                    //    url = volumeInfo.getString("infoLink");

//                                        previewBtn.setVisibility(View.VISIBLE);
//                                        linkBtn.setVisibility(View.VISIBLE);
                                        //  bookprice.setVisibility(View.VISIBLE);

                                        bookTitle.setText(title);
                                        authorName.setText(author);
                                        //   date.setText("PUBLISHED:"+publishedDate);
                                        bookDescription.setText(description);
                                        bookCategory.setText(categories);
                                        // tvPrice.setText(price);
                                        //   bookprice.setText("Price: "+price+" ");
                                        // Toast.makeText(ScannerActivity.this, "pages"+pageCount, Toast.LENGTH_SHORT).show();
                                        //----          bookpages.setText("Pages: "+pageCount+" ");


                                       // Glide.with(ScanToAddActivity.this).load(thumbnail).into(bookimage);

                                        Picasso.get().load(thumbnail).into(bookimage);
                                       // Toast.makeText(ScanToAddActivity.this, "key: "+keyval, Toast.LENGTH_SHORT).show();







                                        // progressBar.setVisibility(View.INVISIBLE);

                                    }


                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                    Log.e("TAG" , e.toString());

                                }

                            }
                        }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        mRequestQueue.add(request);



    }


}
