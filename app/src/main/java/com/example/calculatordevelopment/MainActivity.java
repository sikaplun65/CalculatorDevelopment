package com.example.calculatordevelopment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
    private final int  LIGHT = 301;
    private final int DARK = 201;
    private final int CHANGE_THEME = 101;

    private final String CALCULATOR_STR = "CALCULATOR_STR";
    private Calculator calculator;
    private EditText inputText;
    private int curTheme = LIGHT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SharedPreferences.getCurrentTheme();
        if(savedInstanceState != null){
            if(curTheme == LIGHT){
                setTheme(R.style.my_light_theme);
            }else if(curTheme == DARK){
                setTheme(R.style.my_dark_theme);
            }
        }

        if(curTheme == LIGHT){
            setTheme(R.style.my_light_theme);
        }else if(curTheme == DARK){
            setTheme(R.style.my_dark_theme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.input_text_edit_text);
        if(savedInstanceState == null){
            calculator = new Calculator();
        }else {
            calculator = savedInstanceState.getParcelable(CALCULATOR_STR);
        }

        inputDigit();
        inputOperator();
        showResult();
        сhooseTheme();
    }

    private void сhooseTheme() {
        findViewById(R.id.settings_button).setOnClickListener(v ->{
            Intent intent = new Intent(this,ChangeThemeActivity.class);
            startActivityForResult(intent, CHANGE_THEME);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHANGE_THEME && resultCode == RESULT_OK) {
            String theme = data.getExtras().getString("theme");
            if("light".equals(theme)){
                curTheme = LIGHT;
            }else if("dark".equals(theme)){
                curTheme = DARK;
            }
            recreate();
        }
    }


    private void showResult() {
        findViewById(R.id.clear_button).setOnClickListener(v -> {
            calculator.reset();
            inputText.setText("");
        });

        findViewById(R.id.result_button).setOnClickListener(v -> {
            calculateAndShowResult();
        });

        findViewById(R.id.return_button).setOnClickListener(v -> {
            if (calculator.deleteLastItem()) {
                deleteLast();
            }
        });

        findViewById(R.id.inversion_negative_positive_button).setOnClickListener(v -> {
            inputText.setText(calculator.inversionPositiveDigitToNegativeDigitAndViceVersa());
        });
    }


    private void inputOperator() {

        int[] actionsIds = new int[]{R.id.minus_button, R.id.plus_button, R.id.divide_button, R.id.multiply_button, R.id.comma_button};

        View.OnClickListener actionButtonOnclickListener = this::onClickOperator;

        for (int actionsId : actionsIds) {
            findViewById(actionsId).setOnClickListener(actionButtonOnclickListener);
        }

    }

    private void inputDigit() {
        int[] numberIds = new int[]{
                R.id.digit_zero_button, R.id.digit_one_button, R.id.digit_two_button, R.id.digit_three_button, R.id.digit_four_button,
                R.id.digit_five_button, R.id.digit_six_button, R.id.digit_seven_button, R.id.digit_eight_button, R.id.digit_nine_button
        };

        View.OnClickListener numberButtonClickListener = view -> {

            boolean changed = calculator.onDigit(Integer.parseInt(((TextView) view).getText().toString()));
            if (changed) {
                inputText.getText().append(((Button) view).getText().toString());
                if(calculator.getFirstOperand().getSize() != 0 && calculator.getFirstOperand().getValueDigit() == 0
                        || calculator.getSecondOperand().getSize() != 0 && calculator.getSecondOperand().getValueDigit() == 0){
                    inputText.getText().append(",");
                }
            }
        };

        for (int numberId : numberIds) {
            findViewById(numberId).setOnClickListener(numberButtonClickListener);
        }
    }

    private String showRes(double result) {
        if (calculator.getFirstOperand().isFractional() || calculator.getSecondOperand().isFractional()) {
            return String.valueOf(result);
        } else {
            return String.valueOf((int) result);
        }
    }

    private boolean endsWithOperatore() {
        String currText = inputText.getText().toString();
        return currText.endsWith("+") || currText.endsWith("-") || currText.endsWith("/") || currText.endsWith("x");
    }

    private void calculateAndShowResult() {
        if (!endsWithOperatore() && !calculator.getSecondOperand().getValue().isEmpty()){
            inputText.setText(String.valueOf(calculator.calculatedResult()));
        }
    }

    private void deleteLast(){
        String t = inputText.getText().toString();
        inputText.setText(t.substring(0, t.length() - 1));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CALCULATOR_STR, calculator);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("NonConstantResourceId")
    private void onClickOperator(View view) {
        if (!calculator.getFirstOperand().getValue().isEmpty() || view.getId() == R.id.comma_button) {
            if (endsWithOperatore() && calculator.getSecondOperand().getValue().isEmpty()) {
                if (view.getId() != R.id.comma_button)
                    deleteLast();
            }
            boolean changed = false;
            switch (view.getId()) {
                case R.id.minus_button:
                    calculateAndShowResult();
                    changed = calculator.onOperator(Calculator.Operation.MINUS);
                    break;
                case R.id.plus_button:
                    calculateAndShowResult();
                    changed = calculator.onOperator(Calculator.Operation.PLUS);
                    break;
                case R.id.divide_button:
                    calculateAndShowResult();
                    changed = calculator.onOperator(Calculator.Operation.DIVIDE);
                    break;
                case R.id.multiply_button:
                    calculateAndShowResult();
                    changed = calculator.onOperator(Calculator.Operation.MULTIPLY);
                    break;
                case R.id.comma_button:
                    changed = calculator.onOperator(Calculator.Operation.COMMA);
                    break;
                default:
            }
            if (changed)
                inputText.getText().append(((Button) view).getText().toString());
        }
    }
}