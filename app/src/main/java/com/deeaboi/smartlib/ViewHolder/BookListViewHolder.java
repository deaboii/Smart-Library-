package com.deeaboi.smartlib.ViewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deeaboi.smartlib.R;

public class BookListViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView slNo,bookName,bookCategory,bookAuthor;
    public BookListViewHolder(@NonNull View itemView)
    {
        super(itemView);

        slNo=(TextView)itemView.findViewById(R.id.slno);
        bookName=(TextView)itemView.findViewById(R.id.bookname_list);
        bookCategory=(TextView)itemView.findViewById(R.id.booksCategory);
        bookAuthor=(TextView)itemView.findViewById(R.id.booksAuthors);


    }

    @Override
    public void onClick(View v)
    {

    }
}
