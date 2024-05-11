package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultActivity extends AppCompatActivity {

    TextView txtScore;
    Button btnReset, btnLogout;
    Category categories;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Log.d("State", "Result Activity");

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        String numberOfQuestions = intent.getStringExtra("numberQuestions");
        String cat = intent.getStringExtra("category");
        String diff = intent.getStringExtra("difficulty");

        mauth = FirebaseAuth.getInstance();
        txtScore = findViewById(R.id.txtScore);
        btnLogout = findViewById(R.id.btnLogout);
        btnReset = findViewById(R.id.btnReset);
        categories = new Category();

        txtScore.setText(score + " / " + numberOfQuestions + " : "+ (score/Integer.parseInt(numberOfQuestions))*100+"%");

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                Intent intent = new Intent(ResultActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apiUrl = "https://opentdb.com/api.php?"
                        + "amount=" + numberOfQuestions + "&" + "category=" + categories.categories.get(cat)
                        + "&" + "difficulty=" + diff + "&" + "type=multiple";

                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        String apiUrl = params[0];
                        StringBuilder result = new StringBuilder();
                        try {
                            URL url = new URL(apiUrl);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            InputStream input = conn.getInputStream();
                            InputStreamReader reader = new InputStreamReader(input);
                            int data = reader.read();
                            while(data != -1){
                                result.append((char) data);
                                data = reader.read();
                            }
                            reader.close();
                            conn.disconnect();
                        } catch (IOException e) {
                            Log.e("AsyncTask", "Error fetching data from API", e);
                            return null;
                        }
                        return result.toString();
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        if (result != null) {
                            Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
                            intent.putExtra("JsonScrap", result);
                            intent.putExtra("numberQuestions", numberOfQuestions);
                            intent.putExtra("category", cat);
                            intent.putExtra("difficulty", diff);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ResultActivity.this, "Failed to get data from API", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute(apiUrl);
//                Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
//                startActivity(intent);
            }
        });

    }

    void logout(){
        mauth.signOut();
    }
}