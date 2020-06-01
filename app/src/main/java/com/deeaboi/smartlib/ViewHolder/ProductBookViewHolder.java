package com.deeaboi.smartlib.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deeaboi.smartlib.R;

public class ProductBookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductbookname,txtproductbookauther;
    public ImageView imageView;

    public ProductBookViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView=(ImageView)itemView.findViewById(R.id.product_image);
        txtproductbookname=(TextView)itemView.findViewById(R.id.product_book_name);
        txtproductbookauther=(TextView)itemView.findViewById(R.id.product_book_author);



    }

    @Override
    public void onClick(View view)
    {

    }
}
