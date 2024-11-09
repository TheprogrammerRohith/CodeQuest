package com.example.codequest;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomePageFragment extends Fragment {

    int c1 = 0, c2 = 0, c3 = 0;
    LinearLayout mode_layout,language_layout;
    TextView python, clang, java;
    CardView easy_card, medium_card,hard_card;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_home_page, container, false);

        CardView python_card = myView.findViewById(R.id.python_card);
        CardView clang_card = myView.findViewById(R.id.clang_card);
        CardView java_card = myView.findViewById(R.id.java_card);
        mode_layout=myView.findViewById(R.id.mode_layout);
        language_layout=myView.findViewById(R.id.language_layout);
        easy_card=myView.findViewById(R.id.easy_card);
        medium_card=myView.findViewById(R.id.medium_card);
        hard_card=myView.findViewById(R.id.hard_card);

        python = myView.findViewById(R.id.python);
        clang = myView.findViewById(R.id.clang);
        java = myView.findViewById(R.id.java);

        // Inside onClick for language cards
        python_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language_layout.setVisibility(View.INVISIBLE);
                mode_layout.setVisibility(View.VISIBLE);
                setupModeCardClickListeners("python");
            }
        });

        clang_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language_layout.setVisibility(View.INVISIBLE);
                mode_layout.setVisibility(View.VISIBLE);
                setupModeCardClickListeners("c");
            }
        });

        java_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language_layout.setVisibility(View.INVISIBLE);
                mode_layout.setVisibility(View.VISIBLE);
                setupModeCardClickListeners("java");
            }
        });

        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reset the text and selection state when the fragment becomes visible
        mode_layout.setVisibility(View.GONE);
        language_layout.setVisibility(View.VISIBLE);

    }
    private void setupModeCardClickListeners(final String language) {
        easy_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchQuizActivity(language, "easy");
            }
        });

        medium_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchQuizActivity(language, "medium");
            }
        });

        hard_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchQuizActivity(language, "hard");
            }
        });
    }

    // Launches the Quiz Activity with selected language and difficulty
    private void launchQuizActivity(String language, String difficulty) {
        Intent intent = new Intent(getActivity(), QuizActivity.class);
        intent.putExtra("language", language);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }
}

