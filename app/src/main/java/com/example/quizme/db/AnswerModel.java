package com.example.quizme.db;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class AnswerModel {
    private String Answer;
    private int IdQuestion;
    private int Id;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerModel that = (AnswerModel) o;
        return IdQuestion == that.IdQuestion && Id == that.Id && Objects.equals(Answer, that.Answer);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(Answer, IdQuestion, Id);
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public int getIdQuestion() {
        return IdQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        IdQuestion = idQuestion;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
