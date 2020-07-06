package com.deeaboi.smartlib.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deeaboi.smartlib.R;

public class StudentListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView stSlNo,stName,stNumber,stBranch,stFine;
    public StudentListViewHolder(@NonNull View itemView)
    {
        super(itemView);

        stSlNo=(TextView)itemView.findViewById(R.id.stSlNo);
        stName=(TextView)itemView.findViewById(R.id.stName);
        stNumber=(TextView)itemView.findViewById(R.id.stphone);
        stBranch=(TextView)itemView.findViewById(R.id.stBrnch);
        stFine=(TextView)itemView.findViewById(R.id.stFine);


    }

    @Override
    public void onClick(View v)
    {



    }


}

