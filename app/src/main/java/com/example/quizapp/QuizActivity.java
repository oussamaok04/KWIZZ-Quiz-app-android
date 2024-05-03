package com.example.quizapp;

import static java.lang.System.exit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    TextView txtQuestion;
    RadioGroup rdAnswers;
    Button btnnext;
    String rightAnswer = "", result = "", numberOfQuestions, cat, diff;
    int rightID, score = 0, question = 1, rightIndex;
    RadioButton ans1, ans2, ans3, ans4;
    List<RadioButton> rds = new ArrayList<>();
    Map<String, List<String>> questionsResponses = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txtQuestion = findViewById(R.id.txtQuestion);
        rdAnswers = findViewById(R.id.rdGrp);
        btnnext = findViewById(R.id.btnNext);
        ans1 = findViewById(R.id.rdAnswer1);
        ans2 = findViewById(R.id.rdAnswer2);
        ans3 = findViewById(R.id.rdAnswer3);
        ans4 = findViewById(R.id.rdAnswer4);

        Intent paramIntent = getIntent();
        result = paramIntent.getStringExtra("JsonScrap");
        numberOfQuestions = paramIntent.getStringExtra("numberQuestions");
        cat = paramIntent.getStringExtra("category");
        diff = paramIntent.getStringExtra("difficulty");
        questionsResponses = new HashMap<>();

        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(new JSONObject(result.toString()).getString("results"));
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                String question = jsonObj.getString("question");
                List<String> answers = jsonArrayToList(new JSONArray(jsonObj.getString("incorrect_answers")));
                answers.add(jsonObj.getString("correct_answer"));
                //answers.sort(String::compareTo);
                questionsResponses.put(question, answers);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



        txtQuestion.setText(questionsResponses.keySet().toArray(new String[questionsResponses.size()])[0]);
//        ans1.setText(questionsResponses.get(txtQuestion.getText().toString()).get(0));
//        ans2.setText(questionsResponses.get(txtQuestion.getText().toString()).get(1));
//        ans3.setText(questionsResponses.get(txtQuestion.getText().toString()).get(2));
//        ans4.setText(questionsResponses.get(txtQuestion.getText().toString()).get(3));
//        rightAnswer = questionsResponses.get(txtQuestion.getText().toString()).get(3);

        rds.add(ans1);
        rds.add(ans2);
        rds.add(ans3);
        rds.add(ans4);

        rightIndex = new Random().nextInt(4);
        rightAnswer = questionsResponses.get(txtQuestion.getText().toString()).get(3);
        for (int i = 0; i < 4; i++) {
            if (i == rightIndex){
                rds.get(i).setText(rightAnswer);
                rds.get(i).setBackgroundResource(R.drawable.right_card);
                rightID = rds.get(i).getId();
            } else {
                rds.get(i).setText(questionsResponses.get(txtQuestion.getText().toString()).get(i));
            }
        }
        ans4.setText(questionsResponses.get(txtQuestion.getText().toString()).get(rightIndex));
        Log.d("ANS", rightAnswer);

//        for (RadioButton rd: rds) {
//            if (rd.getText().toString().equals(rightAnswer)) {
//                rightID = rd.getId();
//            }
//        }

        rdAnswers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton rdb = (RadioButton) group.getChildAt(i);
                    if (rdb.isChecked()){
                        rdb.setBackgroundResource(R.drawable.highlighted_card);
                        rdb.setTypeface(null, Typeface.BOLD);
                    } else {
                        rdb.setBackgroundResource(R.drawable.custom_card);
                        rdb.setTypeface(null, Typeface.NORMAL);
                    }
                }

            }
        });



        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //rdAnswers.getChildAt(rightIndex).setBackgroundResource(R.drawable.right_card);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (rdAnswers.getCheckedRadioButtonId() == rightID){
                            score += 1;
                        }
                        if (question < questionsResponses.size()){
//                    if (rdAnswers.getCheckedRadioButtonId() == rightID){
//                        score++;
//                    }
                            if (question == questionsResponses.size() - 1){
                                btnnext.setText("Show Result");
                            }
                            displayNextQuestion();
                            rdAnswers.clearCheck();
                        } else {
                            //countDown.cancel();
                            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                            intent.putExtra("score", score);
                            intent.putExtra("numberQuestions", numberOfQuestions);
                            intent.putExtra("category", cat);
                            intent.putExtra("difficulty", diff);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 1500);

            }
        });

    }

//    void startTimer(){
//        countDown = new CountDownTimer(5000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                timeLeft--;
//                timer.setProgress(timeLeft);
//            }
//
//            @Override
//            public void onFinish() {
//                countDown.cancel();
//                displayNextQuestion();
//                timeLeft = 5;
//                timer.setProgress(5);
//                if (!(question >= 5)){
//                    startTimer();
//                }
//            }
//        };
//        countDown.start();
//    }

    public void displayNextQuestion(){

        question++;
        txtQuestion.setText(questionsResponses.keySet().toArray(new String[questionsResponses.size()])[question - 1]);
        rightIndex = new Random().nextInt(4);
        rightAnswer = questionsResponses.get(txtQuestion.getText().toString()).get(3);
        for (int i = 0; i < 4; i++) {
            if (i == rightIndex){
                rds.get(i).setText(rightAnswer);
                //rds.get(i).setBackgroundResource(R.drawable.right_card);
                rightID = rds.get(i).getId();
            } else {
                rds.get(i).setText(questionsResponses.get(txtQuestion.getText().toString()).get(i));
            }
        }
        ans4.setText(questionsResponses.get(txtQuestion.getText().toString()).get(rightIndex));
        Log.d("ANS", rightAnswer);
        //questionsResponses.get(txtQuestion).sort(String::compareTo);
//        ans1.setText(questionsResponses.get(txtQuestion.getText().toString()).get(0));
//        ans2.setText(questionsResponses.get(txtQuestion.getText().toString()).get(1));
//        ans3.setText(questionsResponses.get(txtQuestion.getText().toString()).get(2));
//        ans4.setText(questionsResponses.get(txtQuestion.getText().toString()).get(3));

        //startTimer();
    }

    private static List<String> jsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                // Convert each element to String and add it to the list
                list.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}