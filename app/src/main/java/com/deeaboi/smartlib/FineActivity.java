package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deeaboi.smartlib.Prevalent.Prevalent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class FineActivity extends AppCompatActivity
{
    private AdView mAdView;
    Integer fine, fineref,totalfine= 0,totalfineref=0 ;
    private TextView totalFines;
    private Button payfinebtn;
    public Integer subtotalfine;
    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine);

        totalFines=(TextView)findViewById(R.id.Totalfineview);
        payfinebtn=(Button)findViewById(R.id.payfinebtn);


        FineCalculator();



        payfinebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                payUsingUpi();
            }
        });



        MobileAds.initialize(this, new OnInitializationCompleteListener()
        {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus)
            {

            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    private void payUsingUpi()
    {

        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "dpkpradhan649@okhdfcbank")
                        .appendQueryParameter("pn", "Edit odisha")
                      //  .appendQueryParameter("mc", "your-merchant-code")
                       // .appendQueryParameter("tr", "your-transaction-ref-id")
                        .appendQueryParameter("tn", "Library Fine")
                        .appendQueryParameter("am", String.valueOf(subtotalfine))
                        .appendQueryParameter("cu", "INR")
                       // .appendQueryParameter("url", "your-transaction-url")
                        .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);



      //  intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
       // activity.startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);

        Intent chooser = Intent.createChooser(intent, "Pay with");
        if(null != chooser.resolveActivity(getPackageManager()))
        {
            startActivityForResult(chooser,UPI_PAYMENT);
        }
        else
        {
            Toast.makeText(FineActivity.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }








    }

    private void FineCalculator()
    {
        DatabaseReference rootref= FirebaseDatabase.getInstance().getReference();
//        DatabaseReference fineRef=rootref.child("UserBooks").child(Prevalent.CurrentOnlineUser.getPhone()).child("Issue");
        DatabaseReference fineRef=rootref.child("College").child(Prevalent.CurrentOnlineUser.getKey()).child("UserBooks").child(Prevalent.CurrentOnlineUser.getPhone()).child("Issue");

        ValueEventListener valueEventListener =new ValueEventListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {

                    String date = ds.child("date").getValue(String.class);
                    Log.d("TAG", date);

                    //------------------------------------------------------------------------calcfine
                    String saveCurrentDate;

                    Calendar callForDate = Calendar.getInstance();

                    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                    saveCurrentDate = currentDate.format(callForDate.getTime());

                    String dateAfterString = saveCurrentDate.toString();
                    String dateBeforeString =date.toString();

                    LocalDate dateBefore = LocalDate.parse(dateBeforeString);
                    LocalDate dateAfter = LocalDate.parse(dateAfterString);

                    long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore,dateAfter);

                    if (noOfDaysBetween >14)
                    {
                        fine= (int) (noOfDaysBetween*2);

                    }
                    else
                    {
                        fine=0;
                    }

                    totalfine=totalfine+fine;


                }

                //totalFines.setText(totalfine.toString());

               // Toast.makeText(FineActivity.this, "Totalfine:"+totalfine, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };
        fineRef.addListenerForSingleValueEvent(valueEventListener);

        fineRefCalculator();

    }

    private void fineRefCalculator()
    {
        DatabaseReference rootrefs= FirebaseDatabase.getInstance().getReference();
        //DatabaseReference fineRefs=rootrefs.child("UserBooks").child(Prevalent.CurrentOnlineUser.getPhone()).child("Refrence");

        DatabaseReference fineRefs=rootrefs.child("College").child(Prevalent.CurrentOnlineUser.getKey()).child("UserBooks").child(Prevalent.CurrentOnlineUser.getPhone()).child("Refrence");

        ValueEventListener valueEventListeners =new ValueEventListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {

                    String date = ds.child("date").getValue(String.class);
                    Log.d("TAG", date);

                    //------------------------------------------------------------------------calcfine
                    String saveCurrentDate;

                    Calendar callForDate = Calendar.getInstance();

                    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                    saveCurrentDate = currentDate.format(callForDate.getTime());

                    String dateAfterString = saveCurrentDate.toString();




                    String dateBeforeString =date.toString();

                    LocalDate dateBefore = LocalDate.parse(dateBeforeString);
                    LocalDate dateAfter = LocalDate.parse(dateAfterString);

                    long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore,dateAfter);

                    if (noOfDaysBetween >1)
                    {
                        fineref= (int) (noOfDaysBetween*5);

                    }
                    else
                    {
                        fineref=0;
                    }

                    totalfineref=totalfineref+fineref;





                }

               // totalFines.setText(totalfine.toString());

                //Toast.makeText(FineActivity.this, "Totalfine:"+totalfine, Toast.LENGTH_SHORT).show();

                   subtotalfine=totalfine+totalfineref;

                totalFines.setText(subtotalfine.toString());

                HashMap<String, Object> userDataMap = new HashMap<>();
                userDataMap.put("fine",subtotalfine.toString());
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();
                RootRef.child("College").child(Prevalent.CurrentOnlineUser.getKey()).child("Student").child(Prevalent.CurrentOnlineUser.getPhone()).updateChildren(userDataMap);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };
        fineRefs.addListenerForSingleValueEvent(valueEventListeners);



    }




    //-----------------------------------upi result code


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data)
    {
        if (isConnectionAvailable(FineActivity.this))
        {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(FineActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(FineActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);

            }
            else {
                Toast.makeText(FineActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);

            }
        } else {
            Log.e("UPI", "Internet issue: ");

            Toast.makeText(FineActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }




}
