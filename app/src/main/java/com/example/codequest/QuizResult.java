package com.example.codequest;

public class QuizResult {
    public String resultId;
    public int score;
    public String language;
    public String difficulty;

    public QuizResult(){}
    public QuizResult(String resultId, int score, String language, String difficulty) {
        this.resultId = resultId;
        this.score = score;
        this.language = language;
        this.difficulty = difficulty;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }
}
