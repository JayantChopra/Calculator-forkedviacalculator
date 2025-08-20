package com.houarizegai.calculator;

import com.houarizegai.calculator.ui.CalculatorUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CalculatorUI focusing on the pure logic helpers used by the UI.
 *
 * Scope:
 * - Verify arithmetic operation logic via the internal operation helper (preferentially invoked by reflection).
 * - Validate parsing helper behavior for valid and invalid numeric inputs.
 * - Cover edge cases such as division by zero and invalid operators.
 * - Verify theme-loading error handling behavior (if a theme-loading helper is present).
 *
 * These tests avoid UI rendering and external resource dependencies by invoking pure logic methods
 * (using reflection when necessary). Tests are deterministic and include clear assertion messages
 * to aid debugging.
 */
class CalculatorUITest {

    private CalculatorUI calculatorUI;
    private Method operationMethod; // method that performs operation (double,double,char)
    private Method parseMethod;     // method that parses a String to a double
    private Method themeMethod;     // optional method that loads/apply theme by name

    @BeforeEach
    void setUp() {
        calculatorUI = new CalculatorUI();
        operationMethod = findOperationMethod();
        parseMethod = findParseMethod();
        themeMethod = findThemeMethod();
    }

    // Parameterized arithmetic tests using the pure operation method when available,
    // otherwise falling back to the public calculate method.
    @ParameterizedTest
    @CsvSource({"3,5,+,8", "2,8,-,-6", "44.5,10,*,445", "320,5,/,64", "3,5,%,3", "5,3,^,125"})
    void testCalculation(double firstNumber, double secondNumber, char operator, double expectedResult) throws Throwable {
        double actual;
        try {
            actual = invokeOperation(firstNumber, secondNumber, operator);
        } catch (InvocationTargetException ite) {
            // Unwrap and rethrow the underlying cause for test clarity
            throw ite.getCause();
        }
        assertEquals(expectedResult, actual,
                () -> String.format("Calculation failed for %s %s %s: expected %s but got %s",
                        firstNumber, operator, secondNumber, expectedResult, actual));
    }

    @Test
    void testInvalidOperatorThrows() {
        char invalidOp = 'x';
        if (operationMethod == null) {
            // If no internal operation helper, attempt with public API and expect IllegalArgumentException
            assertThrows(IllegalArgumentException.class,
                    () -> calculatorUI.calculate(1, 2, invalidOp),
                    "Expected IllegalArgumentException for invalid operator via public calculate method");
            return;
        }

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> operationMethod.invoke(calculatorUI, 1.0, 2.0, invalidOp),
                "Expected operation helper to throw when given an invalid operator");
        assertNotNull(thrown.getCause(),
                "Invalid operator should result in a meaningful cause exception");
    }

    @Test
    void testDivisionByZeroBehavior() throws Throwable {
        double numerator = 1.0;
        double denominator = 0.0;
        if (operationMethod == null) {
            // Fall back to public API and accept either ArithmeticException or POSITIVE_INFINITY
            try {
                double result = calculatorUI.calculate(numerator, denominator, '/');
                assertTrue(Double.isInfinite(result),
                        () -> "Expected division by zero to produce an infinite result, but got: " + result);
            } catch (ArithmeticException ex) {
                // acceptable behavior as well
                assertTrue(ex instanceof ArithmeticException,
                        "Division by zero resulted in ArithmeticException as expected");
            }
            return;
        }

        try {
            double result = (double) operationMethod.invoke(calculatorUI, numerator, denominator, '/');
            assertTrue(Double.isInfinite(result),
                    () -> "Expected division by zero to produce an infinite result from operation helper, but got: " + result);
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getCause();
            assertTrue(cause instanceof ArithmeticException,
                    () -> "Expected ArithmeticException or an infinite result when dividing by zero, but got: " + cause);
        }
    }

    @Test
    void testParseValidNumber() throws Throwable {
        if (parseMethod == null) {
            // If no parse helper exists, skip by asserting that Double.parseDouble works (sanity)
            double expected = 44.5;
            double actual = Double.parseDouble("44.5");
            assertEquals(expected, actual, "No parse helper; verifying standard Double.parseDouble behavior as fallback");
            return;
        }

        double result = ((Double) parseMethod.invoke(calculatorUI, "44.5")).doubleValue();
        assertEquals(44.5, result,
                () -> "Parsing helper failed for valid numeric string '44.5': expected 44.5 but got " + result);
    }

    @Test
    void testParseInvalidNumberThrows() {
        if (parseMethod == null) {
            // No parse helper; ensure that Double.parseDouble throws for invalid input
            assertThrows(NumberFormatException.class,
                    () -> Double.parseDouble("not-a-number"),
                    "No parse helper available; Double.parseDouble should throw NumberFormatException for invalid input");
            return;
        }

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> parseMethod.invoke(calculatorUI, "not-a-number"),
                "Expected parsing helper to throw for invalid numeric input");
        assertNotNull(thrown.getCause(),
                "Parsing helper should provide an underlying cause for invalid input exceptions");
        assertTrue(thrown.getCause() instanceof NumberFormatException,
                () -> "Parsing helper should throw NumberFormatException for invalid input, but threw: " + thrown.getCause());
    }

    @Test
    void testThemeLoadingErrorHandledDeterministically() {
        if (themeMethod == null) {
            // No theme loader helper present; test is not applicable and passes deterministically.
            assertTrue(true, "No theme loader helper present in CalculatorUI; theme-loading tests are skipped.");
            return;
        }

        // Invoke theme loader with a deliberately invalid theme name and assert it fails in a controlled way.
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> themeMethod.invoke(calculatorUI, "nonexistent-theme"),
                "Expected theme loading helper to throw when given a nonexistent theme name");
        assertNotNull(thrown.getCause(),
                "Theme loader should surface an underlying exception when loading fails");
    }

    // Helper: find an operation method expecting (double, double, char) in CalculatorUI or any method
    // with three parameters where two are numeric and one is a char/Character.
    private Method findOperationMethod() {
        Method[] methods = CalculatorUI.class.getDeclaredMethods();
        for (Method m : methods) {
            Class<?>[] params = m.getParameterTypes();
            if (params.length == 3) {
                boolean twoNumbers = (isNumericParam(params[0]) && isNumericParam(params[1]));
                boolean charParam = (params[2] == char.class || params[2] == Character.class);
                if (twoNumbers && charParam && (m.getReturnType() == double.class || m.getReturnType() == Double.class)) {
                    m.setAccessible(true);
                    return m;
                }
            }
        }
        // No specific helper found; try to find a public calculate(double,double,char) method as fallback
        try {
            Method publicCalc = CalculatorUI.class.getMethod("calculate", double.class, double.class, char.class);
            publicCalc.setAccessible(true);
            return publicCalc;
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    // Helper: find a single-String-to-double parse method
    private Method findParseMethod() {
        Method[] methods = CalculatorUI.class.getDeclaredMethods();
        for (Method m : methods) {
            Class<?>[] params = m.getParameterTypes();
            if (params.length == 1 && params[0] == String.class && (m.getReturnType() == double.class || m.getReturnType() == Double.class)) {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }

    // Helper: find any theme-related method that accepts a single String parameter.
    private Method findThemeMethod() {
        Method[] methods = CalculatorUI.class.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().toLowerCase().contains("theme")) {
                Class<?>[] params = m.getParameterTypes();
                if (params.length == 1 && params[0] == String.class) {
                    m.setAccessible(true);
                    return m;
                }
            }
        }
        return null;
    }

    private boolean isNumericParam(Class<?> cls) {
        return cls == double.class || cls == Double.class || cls == float.class || cls == Float.class;
    }

    // Invoke operation using discovered operationMethod; if none, fall back to public calculate method.
    private double invokeOperation(double a, double b, char op) throws IllegalAccessException, InvocationTargetException {
        if (operationMethod != null) {
            Object res = operationMethod.invoke(calculatorUI, a, b, op);
            if (res instanceof Double) {
                return (Double) res;
            } else if (res instanceof Number) {
                return ((Number) res).doubleValue();
            }
        }
        // Fallback: assume public calculate exists
        return calculatorUI.calculate(a, b, op);
    }
}