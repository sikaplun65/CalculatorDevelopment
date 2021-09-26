package com.example.calculatordevelopment;


import android.os.Parcel;
import android.os.Parcelable;

public class Calculator extends Digit implements Parcelable {

    private final Digit firstOperand;
    private final Digit secondOperand;
    private Operation currentOperator;

    public Calculator() {
        firstOperand = new Digit();
        secondOperand = new Digit();
        currentOperator = Operation.NONE;
    }

    public enum Operation {
        PLUS, MINUS, MULTIPLY, DIVIDE, COMMA, NONE
    }

    protected Calculator(Parcel in, Digit firstOperand, Digit secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }

    public Operation getCurrentOperator() { return currentOperator; }

    public Digit getFirstOperand() {
        return firstOperand;
    }

    public Digit getSecondOperand() {
        return secondOperand;
    }

    public boolean onDigit(int arg) {
        if (currentOperator == Operation.NONE) {
            if (!firstOperand.canEdit)
                return false;
            firstOperand.push(arg);
        } else {
            secondOperand.push(arg);
        }
        return true;
    }

    public boolean onOperator(Operation arg) {
        if (arg == Operation.COMMA) {
            if (currentOperator == Operation.NONE) {
                if (firstOperand.isFractional() || !firstOperand.canEdit) {
                    return false;
                }
                firstOperand.setFractional();
            } else {
                if (secondOperand.isFractional()){
                    return false;
                }
                secondOperand.setFractional();
            }
        } else {
            if (currentOperator != Operation.NONE && !secondOperand.getValue().isEmpty()) {
                calculatedResult();
            }
            currentOperator = arg;
        }
        return true;
    }


    public double calculatedResult() {
        double result = 0.;
        switch (currentOperator) {
            case PLUS:
                result = firstOperand.valueDigit() + secondOperand.valueDigit();
                break;
            case MULTIPLY:
                result = firstOperand.valueDigit() * secondOperand.valueDigit();
                break;
            case MINUS:
                result = firstOperand.valueDigit() - secondOperand.valueDigit();
                break;
            case DIVIDE:
                result = firstOperand.valueDigit() / secondOperand.valueDigit();
                break;
            default:
                break;
        }
        reset();
        firstOperand.setValue(String.valueOf(result));
        firstOperand.canEdit = false;
        return result;
    }

    public boolean deleteLastItem() {
        if (currentOperator != Operation.NONE) {
            if (secondOperand.canPop()) {
                secondOperand.pop();
                return true;
            }
            currentOperator = Operation.NONE;
            return true;
        } else {
            if (firstOperand.canPop()) {
                firstOperand.pop();
                if (firstOperand.getValue().isEmpty()) {
                    reset();
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public void reset() {
        currentOperator = Operation.NONE;
        firstOperand.reset();
        secondOperand.reset();
    }

    public String inversionPositiveDigitToNegativeDigitAndViceVersa() {

        if (currentOperator != Operation.NONE) {
            if (secondOperand.getValue().length() != 0 && currentOperator != Operation.MINUS && currentOperator != Operation.PLUS) {
                secondOperand.setValue(String.valueOf(-Double.parseDouble(secondOperand.getValue())));
                return getStr();
            } else if (secondOperand.getValue().length() != 0 && currentOperator == Operation.PLUS) {
                currentOperator = Operation.MINUS;
                return firstOperand.getValue() + "-" + secondOperand.getValue();
            } else {
                return getStr();
            }
        } else if (firstOperand.getValue().length() != 0) {

            firstOperand.setValue(String.valueOf(-Double.parseDouble(firstOperand.getValue())));
            return getStr();
        }
        return getStr();
    }

    public String getStr() {
        String operator = currentOperator == Operation.NONE ? "" : currentOperator == Operation.PLUS ? "+" :
                currentOperator == Operation.MINUS ? "-" : currentOperator == Operation.MULTIPLY ? "x" : "/";

        return firstOperand.getValue() + operator + secondOperand.getValue();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstOperand.getValue());
        dest.writeString(secondOperand.getValue());
    }

    public static final Creator<Calculator> CREATOR = new Creator<Calculator>() {
        public Digit secondOperand;
        public Digit firstOperand;

        @Override
        public Calculator createFromParcel(Parcel in) {
            return new Calculator(in, firstOperand, secondOperand);
        }

        @Override
        public Calculator[] newArray(int size) {
            return new Calculator[size];
        }
    };

}

