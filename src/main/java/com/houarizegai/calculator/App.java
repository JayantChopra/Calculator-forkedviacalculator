package com.houarizegai.calculator;

import com.houarizegai.calculator.ui.CalculatorUI;

/**
 * Application entry point for the Calculator application.
 *
 * <p>This class's main method is responsible for starting the application by
 * instantiating the CalculatorUI. Expected side effects include creating and
 * displaying the application's user interface and allocating associated GUI
 * resources. Any initialization errors are logged to standard error and the
 * application exits with a non-zero status to signal failure to the caller.
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Starting Calculator application...");
        try {
            // Initialize and start the calculator UI. Keep startup semantics unchanged:
            // simply constructing CalculatorUI performs the necessary initialization.
            final CalculatorUI calculatorUI = new CalculatorUI();
            System.out.println("Calculator UI initialized successfully.");
        } catch (IllegalArgumentException iae) {
            // Specific handling for invalid arguments during initialization.
            System.err.println("Invalid argument during startup: " + iae.getMessage());
            iae.printStackTrace(System.err);
            // Fallback: terminate with non-zero status so supervising processes detect failure.
            System.exit(1);
        } catch (IllegalStateException ise) {
            // Specific handling for illegal state encountered during startup.
            System.err.println("Illegal state encountered during startup: " + ise.getMessage());
            ise.printStackTrace(System.err);
            System.exit(1);
        } catch (Exception e) {
            // Catch-all for unexpected startup errors. Log details for debugging.
            System.err.println("Unexpected error during application startup: " + e.getMessage());
            e.printStackTrace(System.err);
            // Fallback behavior: exit with non-zero status to indicate startup failure.
            System.exit(1);
        }
    }
}