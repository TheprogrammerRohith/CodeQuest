package com.example.codequest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashBoardFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference dRef;
    private String userId;
    private LinearLayout progress_bar;
    MyAdapter adapter;
    ArrayList<QuizResult> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_dash_board, container, false);
        auth =FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(user != null){
            userId=user.getUid();
        }
        progress_bar=myView.findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);
        recyclerView=myView.findViewById(R.id.recycler_view);
        dRef= FirebaseDatabase.getInstance().getReference().child("quiz_results").child(userId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();
        adapter=new MyAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    // Loop through the results and retrieve the score, language, and difficulty
                    for (DataSnapshot resultSnapshot : snapshot.getChildren()) {
                        QuizResult obj =resultSnapshot.getValue(QuizResult.class);
                        list.add(obj);
                    }
                    adapter.notifyDataSetChanged();
                    progress_bar.setVisibility(View.INVISIBLE);
                }
                if(list.isEmpty()){
                    progress_bar.setVisibility(View.INVISIBLE);
                    LinearLayout.LayoutParams textview_layout = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    TextView textView = new TextView(getContext());
                    textView.setText("No previous scores");
                    textView.setLayoutParams(textview_layout);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(20);
                    textview_layout.setMargins(30,100,30,20);
                    recyclerView.setVisibility(View.GONE); // Hide the RecyclerView
                    // Add the TextView to your layout
                    // For example, if you have a LinearLayout:
                    FrameLayout frameLayout=myView.findViewById(R.id.dashboard_framelayout);
                    frameLayout.addView(textView);
                }
                else {
                    Log.d("FirebaseData", "No results found for userId: " + userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return myView;
    }
}