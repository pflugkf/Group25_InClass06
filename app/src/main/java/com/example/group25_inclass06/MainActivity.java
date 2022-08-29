/**
 * Assignment #: InClass06
 * File Name: Group25_InClass06 MainActivity.java
 * Full Name: Kristin Pflug
 */

package com.example.group25_inclass06;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Button generateButton;
    TextView average;
    SeekBar complexity;
    TextView complexityAmount;
    ProgressBar progressBar;
    TextView progressAmount;
    ListView numList;

    Handler handler;

    int numTimes = 0;
    ArrayList<Double> numArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_title);

        complexity = findViewById(R.id.complexity_seekbar);
        complexityAmount = findViewById(R.id.complexity_amount_label);
        generateButton = findViewById(R.id.buttonGenerate);
        generateButton.setBackgroundColor(Color.GREEN);

        progressAmount = findViewById(R.id.progressAmount);
        progressBar = findViewById(R.id.progressBar);
        average = findViewById(R.id.average_calculation);

        numList = findViewById(R.id.listView);
        numArrayList = new ArrayList<>();

        ArrayAdapter<Double> adapter = new ArrayAdapter<Double>(this, android.R.layout.simple_list_item_1, android.R.id.text1, numArrayList);
        numList.setAdapter(adapter);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                progressAmount.setText(message.what + "/" + numTimes);
                progressBar.setProgress(message.what);
                numArrayList.add((Double) message.obj);
                adapter.notifyDataSetChanged();
                average.setText(String.valueOf(getAverage(numArrayList)));
                return false;
            }
        });

        complexity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                complexityAmount.setText(i + " Times");
                progressBar.setMax(i);
                numTimes = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numArrayList.clear();
                adapter.notifyDataSetChanged();

                progressBar.setVisibility(View.VISIBLE);
                progressAmount.setVisibility(View.VISIBLE);
                findViewById(R.id.average_label).setVisibility(View.VISIBLE);
                average.setVisibility(View.VISIBLE);
                generateButton.setClickable(false);
                complexity.setClickable(false);
                generateButton.setBackgroundColor(Color.LTGRAY);

                ExecutorService threadPool = Executors.newFixedThreadPool(2);
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 1; i <= numTimes; i++){
                            double num = HeavyWork.getNumber();

                            Message m = new Message();
                            m.what = i;
                            m.obj = (Double) num;
                            handler.sendMessage(m);
                        }
                        generateButton.setClickable(true);
                        generateButton.setBackgroundColor(Color.GREEN);
                    }
                });

            }
        });
    }

    private double getAverage(ArrayList<Double> nums){
        double sum = 0.0;
        for(Double num : nums){
            sum += num;
        }

        return sum/nums.size();
    }
}