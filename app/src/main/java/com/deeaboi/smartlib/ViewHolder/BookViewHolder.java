package com.deeaboi.smartlib.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deeaboi.smartlib.R;

public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductbookname,txtproductbookauther,DateTime,Fines;
    public ImageView imageView;
    public BookViewHolder(@NonNull View itemView)
    {
        super(itemView);
                                        //user_issue_fragment


        imageView=(ImageView)itemView.findViewById(R.id.product_image);
        txtproductbookname=(TextView)itemView.findViewById(R.id.product_book_name);
        txtproductbookauther=(TextView)itemView.findViewById(R.id.product_book_author);
        DateTime=(TextView)itemView.findViewById(R.id.issued_date_time);
        Fines=(TextView)itemView.findViewById(R.id.fine);


    }
    @Override
    public void onClick(View view)
    {

    }
}
