package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParamActivity extends AppCompatActivity {

    Spinner spCategory, spDifficulty, spQstNumber;
    Button start;
    ArrayAdapter<String> catAdapter, diffAdapter, qNumAdapter;
    Category categories;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);

        spCategory = findViewById(R.id.spCat);
        spDifficulty = findViewById(R.id.spDiff);
        spQstNumber = findViewById(R.id.spQNbr);
        start = findViewById(R.id.btnStart);
        categories = new Category();

        ArrayList<String> categoryList = new ArrayList<>(categories.getCategoryList());
        ArrayList<String> diffList = new ArrayList<>();
        diffList.add("easy");
        diffList.add("medium");
        diffList.add("hard");
        ArrayList<String> qNumList = new ArrayList<>();
        qNumList.add("3");
        qNumList.add("4");
        qNumList.add("5");
        qNumList.add("6");
        qNumList.add("7");

        catAdapter = new ArrayAdapter<>(ParamActivity.this, android.R.layout.simple_spinner_dropdown_item, categoryList);
        diffAdapter = new ArrayAdapter<>(ParamActivity.this, android.R.layout.simple_spinner_dropdown_item, diffList);
        qNumAdapter = new ArrayAdapter<>(ParamActivity.this, android.R.layout.simple_spinner_dropdown_item, qNumList);

        spCategory.setAdapter(catAdapter);
        spDifficulty.setAdapter(diffAdapter);
        spQstNumber.setAdapter(qNumAdapter);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = spCategory.getSelectedItem().toString();
                String diff = spDifficulty.getSelectedItem().toString();
                String questions = spQstNumber.getSelectedItem().toString();

                String apiUrl = "https://opentdb.com/api.php?"
                        + "amount=" + questions + "&" + "category=" + categories.categories.get(cat)
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
                            Intent intent = new Intent(ParamActivity.this, QuizActivity.class);
                            intent.putExtra("JsonScrap", result);
                            intent.putExtra("numberQuestions", questions);
                            intent.putExtra("category", cat);
                            intent.putExtra("difficulty", diff);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ParamActivity.this, "Failed to get data from API", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute(apiUrl);
            }
        });

//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String cat = spCategory.getSelectedItem().toString();
//                String diff = spDifficulty.getSelectedItem().toString();
//                String questions = spQstNumber.getSelectedItem().toString();
//
//                String apiUrl = "https://opentdb.com/api.php?"
//                        + "amount=" + questions + "&" + "category=" + categories.categories.get(cat)
//                        + "&" + "difficulty=" + diff + "&" + "type=multiple";
//
//
//                StringBuilder result = new StringBuilder();
//                Map<String, List<String>> questionsResponses = new HashMap<>();
//
//                ExecutorService executor = Executors.newSingleThreadExecutor();
//                //Handler handler = new Handler(Looper.getMainLooper());
//
//                executor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            URL url = new URL(apiUrl);
//                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                            InputStream input = conn.getInputStream();
//                            InputStreamReader reader = new InputStreamReader(input);
//                            int data = reader.read();
//                            while(data != -1){
//                                result.append((char) data);
//                                data = reader.read();
//                            }
////                            JSONArray jsonArr = new JSONArray(new JSONObject(result.toString()).getString("result"));
////                            for (int i = 0; i < jsonArr.length(); i++) {
////                                JSONObject jsonObj = jsonArr.getJSONObject(i);
////                                String question = jsonObj.getString("question");
////                                List<String> answers = jsonArrayToList(new JSONArray(jsonObj.getString("incorrect_answers")));
////                                answers.add(jsonObj.getString("correct_answer"));
////                                questionsResponses.put(question, answers);
////                            }
//                            reader.close();
//                            conn.disconnect();
//                        } catch (MalformedURLException e) {
//                            throw new RuntimeException(e);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//
//
//                    }
//                });
//                executor.shutdown();
//
//                Intent intent = new Intent(ParamActivity.this, QuizActivity.class);
//                intent.putExtra("JsonScrap", result.toString());
//                startActivity(intent);
//            }
//        });
    }

}