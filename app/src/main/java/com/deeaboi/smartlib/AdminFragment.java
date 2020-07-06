package com.deeaboi.smartlib;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.paperdb.Paper;

public class AdminFragment extends Fragment
{

    public View AdminFragmentview;
    private String key="";
    private Button logoutBtn;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
//        return super.onCreateView(inflater, container, savedInstanceState);

        AdminFragmentview=inflater.inflate(R.layout.fragment_admin,container,false);

       // return inflater.inflate(R.layout.fragment_admin,container,false);

        key=getActivity().getIntent().getExtras().getString("key");
        logoutBtn=(Button)AdminFragmentview.findViewById(R.id.adminlogout);


        return AdminFragmentview;

        
    }

    @Override
    public void onStart()
    {
        super.onStart();

    logoutBtn.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Paper.book().destroy();
            Intent intent=new Intent(getActivity(),MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();


        }
    });



    }
}
