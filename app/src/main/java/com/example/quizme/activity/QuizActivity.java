package com.example.quizme.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizme.R;
import com.example.quizme.databinding.ActivityQuizBinding;
import com.example.quizme.db.DbHelper;
import com.example.quizme.db.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;
    DbHelper databaseHelper;
    List<Question> questions;
    int index = 0;
    int AnswerCountFlag = 0;
    Question question;
    int answerCount = 0;
    int correctAnswers = 0;
    SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mSettings = getSharedPreferences("mysettings", 0);
        questions = new ArrayList<Question>();
        databaseHelper = new DbHelper(getApplicationContext());

        binding.nextBtn.setEnabled(false);
        binding.nextBtn.setBackgroundColor(Color.GRAY);
        /*databaseHelper.onUpgrade(db,1,2);*/
        int catId = mSettings.getInt("catId", 1);
       /*  Random random = new Random();
        final int rand = random.nextInt(12);*/
        for (Question q : databaseHelper.getAllCatQuestions(catId)) {
            if(q!=null&&!databaseHelper.getAllAnswers(q.getQuestionId()).isEmpty()){
                questions.add(q);
                Collections.shuffle(questions);
                setNextQuestion();

            }

        }
        if(questions.isEmpty()){
            Intent intent = new Intent(QuizActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Извините, данная категория в разработке!.", Toast.LENGTH_SHORT).show();
        }





    }

    void setNextQuestion() {

        if(index < questions.size()) {
            binding.nextBtn.setEnabled(false);
            binding.nextBtn.setBackgroundColor(Color.GRAY);
            binding.questionCounter.setText(String.format("%d/%d", (index+1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
            Log.d("HttpCodeID", "getQuestionId - "+question.getQuestionId());
            answerCount = databaseHelper.getAllAnswers(question.getQuestionId()).size();
        }
    }

    void checkAnswer(TextView textView) {
        binding.nextBtn.setEnabled(true);
        binding.nextBtn.setBackgroundColor(Color.parseColor("#03A9F4"));
        if(answerCount==4)
        {   AnswerCountFlag++;
            String selectedAnswer = textView.getText().toString();
            Log.d("HttpCodeID", "answerCount - "+answerCount);
            if(selectedAnswer.equals(databaseHelper.getAllAnswers(question.getQuestionId()).get(0).getAnswer())||
                    selectedAnswer.equals(databaseHelper.getAllAnswers(question.getQuestionId()).get(1).getAnswer())||
                    selectedAnswer.equals(databaseHelper.getAllAnswers(question.getQuestionId()).get(2).getAnswer())||
                    selectedAnswer.equals(databaseHelper.getAllAnswers(question.getQuestionId()).get(3).getAnswer())) {
                correctAnswers++;

                textView.setBackground(getResources().getDrawable(R.drawable.option_right));
                textView.setEnabled(false);
            } else {
                /*showAnswer();*/
                textView.setEnabled(false);
                textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            }
            if(AnswerCountFlag==4){

                binding.option1.setEnabled(false);
                binding.option2.setEnabled(false);
                binding.option3.setEnabled(false);
                binding.option4.setEnabled(false);}

        }
        if(answerCount==3)
        {   AnswerCountFlag++;
            String selectedAnswer = textView.getText().toString();
            Log.d("HttpCodeID", "answerCount - "+answerCount);
            if(selectedAnswer.equals(databaseHelper.getAllAnswers(question.getQuestionId()).get(0).getAnswer())||
                    selectedAnswer.equals(databaseHelper.getAllAnswers(question.getQuestionId()).get(1).getAnswer())||
                    selectedAnswer.equals(databaseHelper.getAllAnswers(question.getQuestionId()).get(2).getAnswer())) {
                correctAnswers++;
                textView.setBackground(getResources().getDrawable(R.drawable.option_right));
                textView.setEnabled(false);
            } else {
                /*showAnswer();*/
                textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
                textView.setEnabled(false);
            }
            if(AnswerCountFlag==3){
                binding.option1.setEnabled(false);
                binding.option2.setEnabled(false);
                binding.option3.setEnabled(false);
                binding.option4.setEnabled(false);}

        }


        if(answerCount==2)
        {   AnswerCountFlag++;
            String selectedAnswer = textView.getText().toString();
            Log.d("HttpCodeID", "answerCount - "+answerCount);
            if(selectedAnswer.equals(databaseHelper.getAllAnswers(question.getQuestionId()).get(0).getAnswer())||
                    selectedAnswer.equals(databaseHelper.getAllAnswers(question.getQuestionId()).get(1).getAnswer())) {
                correctAnswers++;
                textView.setBackground(getResources().getDrawable(R.drawable.option_right));
                textView.setEnabled(false);

            } else {
                /*showAnswer();*/
                textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
                textView.setEnabled(false);
            }
            if(AnswerCountFlag==2){

                binding.option1.setEnabled(false);
                binding.option2.setEnabled(false);
                binding.option3.setEnabled(false);
                binding.option4.setEnabled(false);}

        }




        if(answerCount ==1)
        {

        String selectedAnswer = textView.getText().toString();
        Log.d("HttpCodeID", "answerCount - "+answerCount);
        if(selectedAnswer.equals(databaseHelper.getAllAnswers(question.getQuestionId()).get(0).getAnswer())) {
            correctAnswers++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            /*showAnswer();*/
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
            binding.option1.setEnabled(false);
            binding.option2.setEnabled(false);
            binding.option3.setEnabled(false);
            binding.option4.setEnabled(false);
        }

    }

    void reset() {
        binding.option1.setEnabled(true);
        binding.option2.setEnabled(true);
        binding.option3.setEnabled(true);
        binding.option4.setEnabled(true);
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                TextView selected = (TextView) view;
                checkAnswer(selected);

                break;
            case R.id.nextBtn:
                reset();
                if(index < questions.size()-1) {
                    index++;
                    setNextQuestion();
                } else {
                    binding.nextBtn.setEnabled(false);
                    SharedPreferences.Editor editor = mSettings.edit();
                    mSettings = getSharedPreferences("mysettings", 0);
                    if(mSettings.getInt("levels",5)>0){
                        int i = mSettings.getInt("levels",5)-1;
                        editor.putInt("levels", i );
                        editor.commit();
                    }

                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswers);
                   /* intent.putExtra("total", questions.size());*/
                    startActivity(intent);

                    Toast.makeText(this, "Тест завершен.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



}