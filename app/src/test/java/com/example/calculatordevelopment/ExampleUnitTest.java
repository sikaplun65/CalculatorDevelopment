package com.example.calculatordevelopment;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        double EPS = 1e-10;

        Calculator cl = new Calculator();
        assertEquals(true, cl.onDigit(8));
        assertEquals(true, cl.onOperator(Calculator.Operation.COMMA));
        assertEquals(false, cl.onOperator(Calculator.Operation.COMMA));
        assertEquals(true, cl.onDigit(2));
        assertEquals(true, cl.onOperator(Calculator.Operation.MULTIPLY));
        assertEquals(true, cl.onDigit(2));
        assertEquals(true, cl.onOperator(Calculator.Operation.COMMA));
        assertEquals(true, cl.onDigit(5));

        assertEquals(20.5, cl.calculatedResult(), EPS);
    }
}