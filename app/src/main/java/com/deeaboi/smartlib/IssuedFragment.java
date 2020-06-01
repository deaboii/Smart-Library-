package com.deeaboi.smartlib;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deeaboi.smartlib.Model.IssuedUserBooks;
import com.deeaboi.smartlib.Prevalent.Prevalent;
import com.deeaboi.smartlib.ViewHolder.BookViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class IssuedFragment extends Fragment
{
    private RecyclerView IssuedBooklist;
   private  DatabaseReference issuedReference;
   private View IssuedFragmentView;
   private  DatabaseReference reference;
   Integer fine;
    public IssuedFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

      IssuedFragmentView= inflater.inflate(R.layout.fragment_issued, container, false);

      reference = FirebaseDatabase.getInstance().getReference().child("UserBooks").child(Prevalent.CurrentOnlineUser.getPhone()).child("Issue");

      IssuedBooklist=(RecyclerView)IssuedFragmentView.findViewById(R.id.user_issue_book_list);

      IssuedBooklist.setLayoutManager(new LinearLayoutManager(getContext()));//getcontext() for context

        return IssuedFragmentView;
    }


    @Override
    public void onStart()
    {
        super.onStart();




        FirebaseRecyclerOptions<IssuedUserBooks> options =
                new FirebaseRecyclerOptions.Builder<IssuedUserBooks>()
                        .setQuery(reference, IssuedUserBooks.class)
                        .build();

        FirebaseRecyclerAdapter<IssuedUserBooks, BookViewHolder> adapter =
                new FirebaseRecyclerAdapter<IssuedUserBooks, BookViewHolder>(options)
                {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    protected void onBindViewHolder(@NonNull BookViewHolder productBookViewHolder, int i, @NonNull final IssuedUserBooks productsBooks)
                    {
                        productBookViewHolder.txtproductbookname.setText(productsBooks.getPname());
                        productBookViewHolder.txtproductbookauther.setText(productsBooks.getAuthor());
                        productBookViewHolder.DateTime.setText("Issued On:" + productsBooks.getDate());

                        Picasso.get().load(productsBooks.getImage()).into(productBookViewHolder.imageView);


                       // -----------------------------------------------------------------------------------------------------------------------------

                        final String saveCurrentDate;

                        Calendar callForDate = Calendar.getInstance();

                        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                        saveCurrentDate = currentDate.format(callForDate.getTime());

                        String dateAfterString = saveCurrentDate.toString();
                        String dateBeforeString =productsBooks.getDate().toString();

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

                        productBookViewHolder.Fines.setText("Fine:"+fine);


                        //-----------------------------------------------------------------------------------------------------------------------------calculate Total fine and store in database

                        upadteFine(fine);


                    }

                    @NonNull
                    @Override
                    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_issue_fragment, parent, false);
                        BookViewHolder holder = new BookViewHolder(view);
                        return holder;
                    }
                };
        IssuedBooklist.setAdapter(adapter);
        adapter.startListening();




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void upadteFine(Integer fine)
    {

        DatabaseReference FineListRef = FirebaseDatabase.getInstance().getReference().child("Fine");

        final HashMap<String, Object> fineMap = new HashMap<>();
        fineMap.put("Fine", fine);

        FineListRef.child(Prevalent.CurrentOnlineUser.getPhone().toString()).updateChildren(fineMap);



    }
}
