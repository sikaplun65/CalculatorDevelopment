package com.example.calculatordevelopment;

public class Digit {

    private String value;

    boolean canEdit = true;

    public String getValue() { return value; }

    public void setValue(String value) {
        this.value = value;
    }

    public Digit() {
        value = "";
    }

    protected void push(int value) {
        this.value += value;
    }


    protected void pop() {
        value = value.substring(0, value.length() - 1);
    }

    protected boolean canPop() {
        return value.length() > 0;
    }

    protected void setFractional() {
        if (value.isEmpty())
            this.value = "0" + '.';
        else
            this.value += '.';
    }

    protected boolean isFractional() {
        return value.contains(".");
    }

    protected double valueDigit() {
        return Double.parseDouble(value);
    }


    protected void reset() {
        value = "";
        canEdit = true;
    }

}
