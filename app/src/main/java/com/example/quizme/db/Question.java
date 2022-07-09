package com.example.quizme.db;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Question {
    private String Question, Option1, Option2, Option3, Option4;
    private int CategoryId;
    private int QuestionId;


    public Question() {
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return CategoryId == question.CategoryId && QuestionId == question.QuestionId && Objects.equals(Question, question.Question) && Objects.equals(Option1, question.Option1) && Objects.equals(Option2, question.Option2) && Objects.equals(Option3, question.Option3) && Objects.equals(Option4, question.Option4);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(Question, Option1, Option2, Option3, Option4, CategoryId, QuestionId);
    }

    public Question(String question, String option1, String option2, String option3, String option4, String answer, int catId) {
        this.Question = question;
        this.Option1 = option1;
        this.Option2 = option2;
        this.Option3 = option3;
        this.Option4 = option4;
        this.CategoryId = catId;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        this.Question = question;
    }

    public int getCategoryId() {
        return CategoryId;
    }
    public int getQuestionId() {
        return QuestionId;
    }

    public void setCatId(int catId) {this.CategoryId = catId;}
    public void setQuestionId(int QuestionId) {
        this.QuestionId = QuestionId;
    }

    public String getOption1() {
        return Option1;
    }

    public void setOption1(String option1) {
        this.Option1 = option1;
    }

    public String getOption2() {
        return Option2;
    }

    public void setOption2(String option2) {
        this.Option2 = option2;
    }

    public String getOption3() {
        return Option3;
    }

    public void setOption3(String option3) {
        this.Option3 = option3;
    }

    public String getOption4() {
        return Option4;
    }

    public void setOption4(String option4) {
        this.Option4 = option4;
    }

}
