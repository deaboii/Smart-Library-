package com.deeaboi.smartlib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.deeaboi.smartlib.Model.Users;
import com.deeaboi.smartlib.ViewHolder.StudentListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentListActivity extends AppCompatActivity
{
    private RecyclerView Student_List;
    private String key="";
    private  int count;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        key=getIntent().getStringExtra("key");

        Student_List=findViewById(R.id.Student_list);

        Student_List.setLayoutManager(new LinearLayoutManager(StudentListActivity.this));

        onStart();

    }


    @Override
    protected void onStart()
    {
        super.onStart();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("College").child(key).child("Student");

        FirebaseRecyclerOptions<Users> options=
                new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(reference,Users.class)
                .build();

        FirebaseRecyclerAdapter<Users, StudentListViewHolder> adapter=
                new FirebaseRecyclerAdapter<Users, StudentListViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull StudentListViewHolder studentListViewHolder, int i, @NonNull Users users)
                    {


                        reference.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                count = (int) dataSnapshot.getChildrenCount();
                                Toast.makeText(StudentListActivity.this, "Total "+count+" no of Students", Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });

                        studentListViewHolder.stSlNo.setText(String.valueOf(i+1)); //serial number
                        studentListViewHolder.stName.setText(users.getName());
                        studentListViewHolder.stNumber.setText(users.getPhone());
                        studentListViewHolder.stFine.setText(users.getFine());










                    }

                    @NonNull
                    @Override
                    public StudentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list, parent, false);
                        StudentListViewHolder holder =new StudentListViewHolder(view);
                        return holder;
                    }
                };


                Student_List.setAdapter(adapter);
                   adapter.startListening();


    }


}
