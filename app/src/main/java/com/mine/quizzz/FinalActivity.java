package com.mine.quizzz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FinalActivity extends AppCompatActivity {
    private TextView tvFinalScore;
    private Button btnTakeNewQuiz, btnFinish;
    private String userName;
    private int score, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        tvFinalScore   = findViewById(R.id.tvFinalScore);
        btnTakeNewQuiz = findViewById(R.id.btnTakeNewQuiz);
        btnFinish      = findViewById(R.id.btnFinish);

        Intent intent = getIntent();
        userName = intent.getStringExtra("USER_NAME");
        score    = intent.getIntExtra("SCORE", 0);
        total    = intent.getIntExtra("TOTAL", 0);

        tvFinalScore.setText("Well done, " + userName + "!\nYour score: " + score + " / " + total);

        btnTakeNewQuiz.setOnClickListener(v -> {
            // Restart QuizActivity with the preserved user name.
            Intent newIntent = new Intent(FinalActivity.this, QuizActivity.class);
            newIntent.putExtra("USER_NAME", userName);
            startActivity(newIntent);
            finish();
        });

        btnFinish.setOnClickListener(v -> {
            // Close the app
            finishAffinity();
        });
    }
}
