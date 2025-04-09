package com.mine.quizzz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView tvQuestion;
    private ChipGroup chipGroup;
    private Chip[] chips;
    private Button btnSubmit;
    private ProgressBar progressBar;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion  = findViewById(R.id.tvQuestion);
        chipGroup   = findViewById(R.id.chipGroup);
        btnSubmit   = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);

        // collect your chips into an array
        chips = new Chip[] {
            findViewById(R.id.chip0),
            findViewById(R.id.chip1),
            findViewById(R.id.chip2),
            findViewById(R.id.chip3)
        };

        userName = getIntent().getStringExtra("USER_NAME");

        loadQuestions();
        showQuestion();

        btnSubmit.setOnClickListener(v -> {
            int selectedId = chipGroup.getCheckedChipId();
            if (selectedId == View.NO_ID) {
                Toast.makeText(this, "Select an answer first", Toast.LENGTH_SHORT).show();
                return;
            }
            btnSubmit.setEnabled(false);

            // find index of the selected chip
            int selectedIndex = -1;
            for (int i = 0; i < chips.length; i++) {
                if (chips[i].getId() == selectedId) {
                    selectedIndex = i;
                    break;
                }
            }

            Question q = questions.get(currentIndex);

            // highlight correct & wrong
            for (int i = 0; i < chips.length; i++) {
                if (i == q.getCorrectIndex()) {
                    chips[i].setChipBackgroundColorResource(R.color.correct_green);
                }
                if (i == selectedIndex && i != q.getCorrectIndex()) {
                    chips[i].setChipBackgroundColorResource(R.color.incorrect_red);
                }
            }
            if (selectedIndex == q.getCorrectIndex()) score++;

            // after a short delay, move on
            new Handler().postDelayed(() -> {
                currentIndex++;
                if (currentIndex < questions.size()) {
                    showQuestion();
                    btnSubmit.setEnabled(true);
                } else {
                    Intent intent = new Intent(this, FinalActivity.class);
                    intent.putExtra("SCORE", score);
                    intent.putExtra("TOTAL", questions.size());
                    intent.putExtra("USER_NAME", userName);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        });
    }

    private void loadQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question(
            "Who wrote 'Romeo and Juliet'?",
            new String[]{"Charles Dickens", "William Shakespeare", "Mark Twain", "Jane Austen"},
            1
        ));

        questions.add(new Question(
            "What is 2 + 2?",
            new String[]{"3", "4", "5", "6"},
            1
        ));

        questions.add(new Question(
            "Which element has atomic number 1?",
            new String[]{"Oxygen", "Hydrogen", "Carbon", "Helium"},
            1
        ));

        questions.add(new Question(
            "What is the chemical symbol for water?",
            new String[]{"WO", "H₂O", "O₂H", "HO₂"},
            1
        ));

        questions.add(new Question(
            "In which continent is the Sahara Desert?",
            new String[]{"Asia", "Africa", "Australia", "South America"},
            1
        ));

        questions.add(new Question(
            "What year did the first man land on the Moon?",
            new String[]{"1965", "1969", "1971", "1959"},
            1
        ));

        questions.add(new Question(
            "Which language is primarily spoken in Brazil?",
            new String[]{"Spanish", "Portuguese", "French", "English"},
            1
        ));
    }

    private void showQuestion() {
        Question q = questions.get(currentIndex);
        tvQuestion.setText(q.getQuestion());
        for (int i = 0; i < chips.length; i++) {
            chips[i].setText(q.getOptions()[i]);
            chips[i].setChecked(false);
            // reset background to transparent
            chips[i].setChipBackgroundColorResource(android.R.color.transparent);
        }
        chipGroup.clearCheck();

        int percent = (int) (((double) currentIndex / questions.size()) * 100);
        progressBar.setProgress(percent);
    }
}
