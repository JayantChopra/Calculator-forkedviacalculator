package com.houarizegai.calculator;

import com.houarizegai.calculator.ui.CalculatorUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for CalculatorUI.
 *
 * Intent:
 * - Verify core arithmetic operations behave as expected for typical numeric inputs.
 * - Add defensive checks for edge numeric cases (large values, zero operands, division by zero)
 *   while remaining tolerant to implementation-specific behavior (e.g., returning Infinity vs throwing).
 *
 * Preconditions:
 * - Each test starts with a fresh CalculatorUI instance (setUp).
 * - Tests explicitly reset shared state in tearDown to avoid leakage between tests.
 *
 * Notes:
 * - Assertions for floating-point results use a small delta to avoid brittle failures due to precision.
 * - Edge-case tests are written to accept multiple valid behaviors (e.g., Infinity or thrown exception)
 *   rather than forcing a specific implementation change.
 */
class CalculatorUITest {

    private CalculatorUI calculatorUI;
    private static final double DOUBLE_COMPARISON_DELTA = 1e-9;

    @BeforeEach
    void setUp() {
        // Create a fresh UI instance before each test to avoid shared mutable state across tests.
        calculatorUI = new CalculatorUI();
    }

    @AfterEach
    void tearDown() {
        // Explicitly clear shared reference to ensure no test relies on stale global state.
        calculatorUI = null;
    }

    /**
     * Helper that delegates to the CalculatorUI.calculate method.
     * Centralizing the call makes test intent clearer and reduces duplication.
     */
    private double performCalculation(double firstNumber, double secondNumber, char operator) {
        return calculatorUI.calculate(firstNumber, secondNumber, operator);
    }

    /**
     * Helper for asserting floating point expected vs actual values with a clear message and delta.
     */
    private void assertDoubleEqualsWithMessage(double expected, double actual, String context) {
        assertEquals(expected, actual, DOUBLE_COMPARISON_DELTA,
                String.format("Expected %s but was %s for %s", expected, actual, context));
    }

    /**
     * Parameterized tests for common arithmetic operations.
     *
     * Preconditions:
     * - Inputs are regular finite numbers.
     */
    @ParameterizedTest
    @DisplayName("Basic arithmetic operations: +, -, *, /, %, ^")
    @CsvSource({
            "3,5,+,8",
            "2,8,-,-6",
            "44.5,10,*,445",
            "320,5,/,64",
            "3,5,%,3",
            "5,3,^,125"
    })
    void testCalculation_basicOperations(double firstNumber, double secondNumber, char operator, double expectedResult) {
        double actual = performCalculation(firstNumber, secondNumber, operator);
        assertDoubleEqualsWithMessage(expectedResult, actual,
                String.format("operation %s %c %s", firstNumber, operator, secondNumber));
    }

    /**
     * Additional sanity checks for zero and simple combinations that historically can be edge cases.
     *
     * Preconditions:
     * - Inputs include zeros and expect straightforward arithmetic behavior.
     */
    @ParameterizedTest
    @DisplayName("Zero operand behavior for + and *")
    @CsvSource({
            "0,5,+,5",
            "5,0,*,0",
            "0,0,+,0"
    })
    void testZeroOperandBehavior(double firstNumber, double secondNumber, char operator, double expectedResult) {
        double actual = performCalculation(firstNumber, secondNumber, operator);
        assertDoubleEqualsWithMessage(expectedResult, actual,
                String.format("operation %s %c %s", firstNumber, operator, secondNumber));
    }

    /**
     * Check behavior for very large numbers to ensure operations do not unexpectedly produce NaN.
     *
     * Preconditions:
     * - Uses large but finite doubles.
     *
     * Acceptable outcomes:
     * - Result is finite OR the behavior results in a defined IEEE floating-point value (Infinity/NaN).
     *   The test asserts the result is a numeric value (finite or infinite) but not an unhandled exception.
     */
    @Test
    @DisplayName("Large number multiplication results in a numeric (finite or infinite) value")
    void testLargeNumberMultiplicationProducesNumericResult() {
        double a = 1e154;
        double b = 1e154;
        double result = performCalculation(a, b, '*');
        // We're tolerant: multiplication of very large numbers may produce Infinity; ensure no exception and numeric outcome.
        assertTrue(!Double.isNaN(result), "Expected non-NaN numeric result for large multiplication, got NaN");
    }

    /**
     * Verify divide-by-zero behavior is reasonable.
     *
     * Preconditions:
     * - Dividing a finite non-zero number by zero may either:
     *   - return +/-Infinity (IEEE-754), or
     *   - throw an ArithmeticException depending on implementation.
     *
     * This test accepts both behaviors but fails if an unexpected non-numeric finite value is returned.
     */
    @Test
    @DisplayName("Division by zero yields Infinity/NaN or throws ArithmeticException (implementation-tolerant)")
    void testDivideByZeroBehavior() {
        try {
            double result = performCalculation(1.0, 0.0, '/');
            // Accept Infinity or NaN; if finite, that's unexpected for a divide-by-zero scenario here.
            assertTrue(Double.isInfinite(result) || Double.isNaN(result),
                    "Divide by zero should produce Infinity or NaN; was: " + result);
        } catch (ArithmeticException expected) {
            // Acceptable alternative behavior: throwing an arithmetic exception.
            assertTrue(true);
        }
    }
}