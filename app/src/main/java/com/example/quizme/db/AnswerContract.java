package com.example.quizme.db;

import android.provider.BaseColumns;

/**
 * Created by delaroy on 11/30/17.
 */

public class AnswerContract {

    public static class QuizEntry implements BaseColumns {
        public static final String TABLE_ANSWER = "answer";
        public static final String ANSWER_ID = "id";
        public static final String KEY_ANSWER = "answer";
        public static final String QUESTION_ID= "idQuestion"; //option d
    }
}
