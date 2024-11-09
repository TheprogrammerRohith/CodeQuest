package com.example.codequest;

import java.io.Serializable;
import java.util.List;

public class Questions implements Serializable {
    String question;
    List<String> options;
    String answer;
    String explanation;

    Questions(String question, List<String> options, String answer, String explanation) {
        this.question = question;
        this.options = options;
        this.answer = answer;
        this.explanation = explanation;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }

    public String getExplanation() {
        return explanation;
    }

}
