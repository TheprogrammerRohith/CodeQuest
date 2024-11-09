package com.example.codequest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private Map<Integer,String> userAnswers;
    private List<Questions> questions;
    private String language,difficulty;
    private ArrayList<CardView> cards=new ArrayList<>();
    private int score=0;
    private ArrayList<QuizResult> list;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference();
        list=new ArrayList<>();
        TextView score_txt=findViewById(R.id.score_txt);
        CardView que1=findViewById(R.id.que1);
        CardView que2=findViewById(R.id.que2);
        CardView que3=findViewById(R.id.que3);
        CardView que4=findViewById(R.id.que4);
        CardView que5=findViewById(R.id.que5);
        CardView que6=findViewById(R.id.que6);
        CardView que7=findViewById(R.id.que7);
        CardView que8=findViewById(R.id.que8);
        CardView que9=findViewById(R.id.que9);
        CardView que10=findViewById(R.id.que10);
        Button home_btn=findViewById(R.id.home_btn);
        Button review_btn=findViewById(R.id.report_btn);
        cards.add(que1);cards.add(que2);cards.add(que3);cards.add(que4);cards.add(que5);
        cards.add(que6);cards.add(que7);cards.add(que8);cards.add(que9);cards.add(que10);

        userAnswers=(Map<Integer, String>) getIntent().getSerializableExtra("userAnswers");
        questions=(List<Questions>) getIntent().getSerializableExtra("questions");
        language=getIntent().getStringExtra("language");
        difficulty=getIntent().getStringExtra("difficulty");

        for(Map.Entry<Integer,String> entry:userAnswers.entrySet()){
            int idx = entry.getKey();
            String ans=questions.get(idx).getAnswer();
            if(ans.equals(entry.getValue())){
                score++;
                turnToGreen(cards.get(idx));
            }
            else{
                turnToRed(cards.get(idx));
            }
        }

        score_txt.setText(String.valueOf(score));
        storeResultInFirebase();

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportActivity.this,HomePage.class);

                startActivity(intent);
            }
        });

        review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this,ReviewActivity.class);
                intent.putExtra("userAnswers",(Serializable) userAnswers);
                intent.putExtra("questions",(Serializable) questions);
                startActivity(intent);
            }
        });

    }

    private void storeResultInFirebase() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            database = database.child("quiz_results").child(userId);

            checkPreviousResults(database, resultId -> {
                if (resultId.equals("no")) {
                    // No previous result, add a new record
                    String newResultId = database.push().getKey();
                    QuizResult result = new QuizResult(newResultId, score, language, difficulty);
                    if (newResultId != null) {
                        database.child(newResultId).setValue(result);
                    } else {
                        Log.d("firebase error", "resultId not created");
                    }
                } else {
                    // Previous result found, update the existing score
                    database.child(resultId).child("score").setValue(score);
                }
            });
        }
    }


    private void checkPreviousResults(DatabaseReference database, FirebaseResultCallback callback) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    // Loop through the results and retrieve the score, language, and difficulty
                    for (DataSnapshot resultSnapshot : snapshot.getChildren()) {
                        QuizResult obj = resultSnapshot.getValue(QuizResult.class);
                        list.add(obj);
                    }
                    // Find existing result
                    for (QuizResult obj : list) {
                        if (obj.getLanguage().equals(language) && obj.getDifficulty().equals(difficulty)) {
                            callback.onResultReceived(obj.getResultId());
                            return;
                        }
                    }
                }
                callback.onResultReceived("no"); // No existing result found
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase error", "Database error: " + error.getMessage());
                callback.onResultReceived("no");
            }
        });
    }


    private void turnToRed(CardView cardView) {
        cardView.setBackgroundColor(getResources().getColor(R.color.red));
    }

    private void turnToGreen(CardView cardView) {
        cardView.setBackgroundColor(getResources().getColor(R.color.green));
    }

}