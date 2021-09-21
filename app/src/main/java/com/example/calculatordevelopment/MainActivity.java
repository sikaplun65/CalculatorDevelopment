package com.example.calculatordevelopment;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CalculatorImpl calculator;
    private EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.input_text_edit_text);
        calculator = new CalculatorImpl(inputText);


        int[] numberIds = new int[]{
                R.id.digit_zero_button, R.id.digit_one_button, R.id.digit_two_button, R.id.digit_three_button, R.id.digit_four_button,
                R.id.digit_five_button, R.id.digit_six_button, R.id.digit_seven_button, R.id.digit_eight_button, R.id.digit_nine_button,
                R.id.comma_button
        };

        int[] actionsIds = new int[]{
                R.id.minus_button, R.id.plus_button,
                R.id.divide_button, R.id.multiply_button
        };

        int[] resultAndClearIds = new int[]{
                R.id.clear_button, R.id.result_button, R.id.return_button
        };


        View.OnClickListener numberButtonClickListener = view -> {
            calculator.addArgument(((TextView) view).getText().toString());
            inputText = calculator.getInputText();
        };

        View.OnClickListener actionButtonOnclickListener = view -> {
            calculator.addOperator(((TextView) view).getText().toString());
            inputText = calculator.getInputText();
        };

        View.OnClickListener resultAndClearButtonOnClickListener = view ->{
            calculator.onActionResultAndClear(view.getId());
            inputText = calculator.getInputText();
        };


        for (int numberId : numberIds) {
            findViewById(numberId).setOnClickListener(numberButtonClickListener);
        }

        for (int actionsId : actionsIds) {
            findViewById(actionsId).setOnClickListener(actionButtonOnclickListener);
        }

        for(int resultAndClearId: resultAndClearIds){
            findViewById(resultAndClearId).setOnClickListener(resultAndClearButtonOnClickListener);
        }

    }
}