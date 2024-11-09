package com.example.codequest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {

    private TextView que, opt1, opt2, opt3, opt4;
    private Button pre_btn,nxt_btn,home_btn;
    private CardView card_opt1,card_opt2,card_opt3,card_opt4;
    private Map<Integer,String> userAnswers;
    private List<Questions> questions;
    private int current_idx=0;
    private TextView explanation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review);
        que = findViewById(R.id.question);
        opt1 = findViewById(R.id.option1);
        opt2 = findViewById(R.id.option2);
        opt3 = findViewById(R.id.option3);
        opt4 = findViewById(R.id.option4);
        card_opt1=findViewById(R.id.card_opt1);
        card_opt2=findViewById(R.id.card_opt2);
        card_opt3=findViewById(R.id.card_opt3);
        card_opt4=findViewById(R.id.card_opt4);
        nxt_btn=findViewById(R.id.nxt_btn);
        pre_btn=findViewById(R.id.pre_btn);
        explanation=findViewById(R.id.explanation);
        home_btn=findViewById(R.id.home_btn);

        userAnswers=(Map<Integer, String>) getIntent().getSerializableExtra("userAnswers");
        questions=(List<Questions>) getIntent().getSerializableExtra("questions");

        if (!questions.isEmpty()) {
            displayQuestions(current_idx);
        }

        nxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_idx++;
                pre_btn.setVisibility(View.VISIBLE);
                if(current_idx<questions.size() ){
                    displayQuestions(current_idx);
                }
                else{
                    nxt_btn.setVisibility(View.INVISIBLE);
                    home_btn.setVisibility(View.VISIBLE);
                }
            }
        });

        pre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle visibility of "Previous" and "Next" buttons
                if (current_idx > 0) {
                    current_idx--;

                    nxt_btn.setVisibility(View.VISIBLE);
                    home_btn.setVisibility(View.INVISIBLE);

                    if (current_idx == 0) {
                        pre_btn.setVisibility(View.INVISIBLE);
                    }
                }

                // Display the current question based on updated index
                displayQuestions(current_idx);
            }
        });


        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void displayQuestions(int currentIdx) {
        resetOptionColors();
        que.setText(questions.get(currentIdx).getQuestion());
        List<String> opts = questions.get(currentIdx).getOptions();
        opt1.setText(opts.get(0));
        opt2.setText(opts.get(1));
        opt3.setText(opts.get(2));
        opt4.setText(opts.get(3));
        explanation.setText(questions.get(currentIdx).getExplanation());
        setOptionColors(currentIdx);
    }

    private void setOptionColors(int currentIdx) {
        // Get the user's answer and the correct answer for the current question
        String userAnswer = userAnswers.get(currentIdx);
        String correctAnswer = questions.get(currentIdx).getAnswer();
        String option1=opt1.getText().toString().substring(0,1);
        String option2=opt2.getText().toString().substring(0,1);
        String option3=opt3.getText().toString().substring(0,1);
        String option4=opt4.getText().toString().substring(0,1);
        // Set the background color for each option based on the user's answer and the correct answer
        if (option1.equals(correctAnswer)) {
            card_opt1.setCardBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // Correct answer - green
        } else if (option1.equals(userAnswer)) {
            card_opt1.setCardBackgroundColor(getResources().getColor(android.R.color.holo_blue_light)); // User's answer - blue
        }

        if (option2.equals(correctAnswer)) {
            card_opt2.setCardBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else if (option2.equals(userAnswer)) {
            card_opt2.setCardBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        }

        if (option3.equals(correctAnswer)) {
            card_opt3.setCardBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else if (option3.equals(userAnswer)) {
            card_opt3.setCardBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        }

        if (option4.equals(correctAnswer)) {
            card_opt4.setCardBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else if (option4.equals(userAnswer)) {
            card_opt4.setCardBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        }
    }


    private void resetOptionColors() {
        card_opt1.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        card_opt2.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        card_opt3.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        card_opt4.setCardBackgroundColor(getResources().getColor(android.R.color.white));
    }
}