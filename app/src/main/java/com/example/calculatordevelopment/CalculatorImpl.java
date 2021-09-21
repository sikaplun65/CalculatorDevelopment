package com.example.calculatordevelopment;

import android.annotation.SuppressLint;
import android.widget.EditText;


public class CalculatorImpl {

    private final EditText inputText;
    private String currentOperator;
    private Boolean isCanAddNumber;
    private Boolean isCanAddComma;
    private int countOperators;
    private double result;

    public EditText getInputText() {
        return inputText;
    }

    public CalculatorImpl(EditText inputText) {
        this.inputText = inputText;
        isCanAddNumber = true;
        isCanAddComma = true;
        countOperators = 0;
    }

    // формирование строки
    private void appendToLast(String str) {
        this.inputText.getText().append(str);

        if (getInput().length() > 2) {
            // если последний символ не оператор,а предпоследний оператор -> увеличиваем счетчик
            if (!endsWithOperatore() && penultimateOperatoreCharacter()) {
                countOperators++;
            }
        }
    }

    private boolean penultimateOperatoreCharacter() {
        // получаем предпоследний символ строки
        char ch = getInput().substring(inputText.getText().length() - 2).charAt(0);
        return currentOperator != null && currentOperator.charAt(0) == ch;
    }


    public void addArgument(String arg) {
        if (isCanAddNumber) {
            if (getInput().length() == 0 && arg.equals(",")) {
                appendToLast("0" + arg);
                isCanAddComma = false;
            } else if (getInput().length() != 0 && arg.equals("0")) {
                appendToLast(arg);
            } else {
                if (!arg.equals(",")) {
                    appendToLast(arg);
                } else if (isCanAddComma) {
                    isCanAddComma = false;
                    appendToLast(arg);
                }
            }
        }
    }

    public void addOperator(String arg) {
        // на данном этапе ограничение в одну операцию
        if (getInput().length() != 0 && countOperators < 1) {
            if (endsWithOperatore()) {
                replace(arg);
            } else {
                appendToLast(arg);
            }
            isCanAddNumber = true;
            isCanAddComma = true;
            currentOperator = arg;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onActionResultAndClear(int id) {

        switch (id) {
            case R.id.clear_button:
                clearText();
                isCanAddNumber = true;
                break;
            case R.id.result_button:
                if(!endsWithOperatore()){
                    calculateResult();
                    appendToLast(String.valueOf(result));
                    isCanAddNumber = false;
                }
                break;
            case R.id.return_button:
                removeLastChar(getInput());
                break;
        }
        countOperators = 0;
    }

    public void clearText() {
        inputText.getText().clear();
    }

    private void replace(String str) {
        inputText.getText().replace(getInput().length() - 1, getInput().length(), str);
    }

    private void removeLastChar(String str) {
        if (str != null && str.length() > 0){
            inputText.setText(str.substring(0, str.length() - 1));
        }
    }

    private boolean endsWithOperatore() {
        return getInput().endsWith("+") || getInput().endsWith("-") || getInput().endsWith("/") || getInput().endsWith("x");
    }

    private String getInput() {
        return this.inputText.getText().toString();
    }

    private void calculateResult() {

        String input = getInput();

        if (input.contains(",")) {
            input = input.replaceAll(",", ".");
        }

        String splitRegExp = currentOperator;

        if (currentOperator.equals("+")) {
            splitRegExp = String.format("\\%s", splitRegExp);
        }

        String[] subStr = input.split(splitRegExp);

        clearText();

        if (subStr.length == 2) {
            result = operate(subStr[0], subStr[1]);
        }
    }

    private double operate(String a, String b) {
        switch (currentOperator) {
            case "+":
                return Double.parseDouble(a) + Double.parseDouble(b);
            case "-":
                return Double.parseDouble(a) - Double.parseDouble(b);
            case "x":
                return Double.parseDouble(a) * Double.parseDouble(b);
            case "/":
                return Double.parseDouble(a) / Double.parseDouble(b);
            default:
                return -1;
        }
    }

}
