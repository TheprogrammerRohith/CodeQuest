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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private TextView que, opt1, opt2, opt3, opt4;
    private Button nxt_btn,sub_btn,pre_btn;
    List<Questions> questions;
    CardView card_opt1,card_opt2,card_opt3,card_opt4;
    int current_idx=0;
    String fileName="";
    private Map<Integer, String> userAnswers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
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
        sub_btn=findViewById(R.id.sub_btn);

        String language = getIntent().getStringExtra("language");
        String difficulty = getIntent().getStringExtra("difficulty");
        if(language.equals("python") && difficulty.equals("easy")){
            fileName="python_easy.txt";
        }
        if(language.equals("python") && difficulty.equals("medium")){
            fileName="python_medium.txt";
        }
        if(language.equals("python") && difficulty.equals("hard")){
            fileName="python_hard.txt";
        }
        if(language.equals("c") && difficulty.equals("easy")){
            fileName="c_easy.txt";
        }
        if(language.equals("c") && difficulty.equals("medium")){
            fileName="c_medium.txt";
        }
        if(language.equals("c") && difficulty.equals("hard")){
            fileName="c_hard.txt";
        }
        if(language.equals("java") && difficulty.equals("easy")){
            fileName="java_easy.txt";
        }
        if(language.equals("java") && difficulty.equals("medium")){
            fileName="java_medium.txt";
        }
        if(language.equals("java") && difficulty.equals("hard")){
            fileName="java_hard.txt";
        }

        questions = readQuestionsFromFile(fileName);

        if (!questions.isEmpty()) {
            displayQuestions(current_idx);
        } else {
            que.setText("No questions found.");
        }

        nxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_idx++;
                if(current_idx<questions.size() ){
                    displayQuestions(current_idx);
                }
                else{
                    nxt_btn.setVisibility(View.INVISIBLE);
                    sub_btn.setVisibility(View.VISIBLE);
                }
            }
        });

        pre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (current_idx > 0) {
                    current_idx--;

                    nxt_btn.setVisibility(View.VISIBLE);
                    sub_btn.setVisibility(View.INVISIBLE);

                    if (current_idx == 0) {
                        pre_btn.setVisibility(View.INVISIBLE);
                    }
                }
                displayQuestions(current_idx);
            }
        });

        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this, ReportActivity.class);
                intent.putExtra("userAnswers",(Serializable) userAnswers);
                intent.putExtra("questions",(Serializable) questions);
                intent.putExtra("language",language);
                intent.putExtra("difficulty",difficulty);
                startActivity(intent);
            }
        });

        View.OnClickListener optionClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetOptionColors();
                CardView selectedCard = (CardView) v;
                selectedCard.setCardBackgroundColor(getResources().getColor(R.color.blue));
                String selectedOption = ((TextView) selectedCard.getChildAt(0)).getText().toString();

                userAnswers.put(current_idx, selectedOption.substring(0, 1));
            }
        };

        card_opt1.setOnClickListener(optionClickListener);
        card_opt2.setOnClickListener(optionClickListener);
        card_opt3.setOnClickListener(optionClickListener);
        card_opt4.setOnClickListener(optionClickListener);
    }

    private void displayQuestions(int currentIdx) {
        resetOptionColors();
        que.setText(questions.get(currentIdx).getQuestion());
        List<String> opts = questions.get(currentIdx).getOptions();
        opt1.setText(opts.get(0));
        opt2.setText(opts.get(1));
        opt3.setText(opts.get(2));
        opt4.setText(opts.get(3));

    }

    private void resetOptionColors() {
        card_opt1.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        card_opt2.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        card_opt3.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        card_opt4.setCardBackgroundColor(getResources().getColor(android.R.color.white));
    }

    public List<Questions> readQuestionsFromFile(String fileName) {
        List<Questions> questions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringBuilder questionBuilder = new StringBuilder(line);

                // Continue reading the question until the first option is found
                while ((line = br.readLine()) != null && !line.startsWith("a) ")) {
                    questionBuilder.append("\n").append(line);
                }
                String questionText = questionBuilder.toString();

                List<String> options = new ArrayList<>();
                options.add(line); // Adding option a)

                // Read the next 3 options (b, c, and d)
                for (int i = 0; i < 3; i++) {
                    options.add(br.readLine());
                }

                String answer = br.readLine();
                String explanation = br.readLine();
                questions.add(new Questions(questionText, options, answer, explanation));

                // Read the empty line between questions (if exists)
                br.readLine();
            }
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading questions from file", e);
        }

        return questions;
    }
}