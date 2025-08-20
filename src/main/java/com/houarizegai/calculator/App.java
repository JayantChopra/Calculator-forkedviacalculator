package com.houarizegai.calculator;

import com.houarizegai.calculator.ui.CalculatorUI;

/**
 * Application entry point for the Calculator application.
 *
 * <p>Expected runtime environment:
 * - Java SE (desktop) with a graphical display (AWT/Swing) available.
 * - The application creates a GUI on startup; launching in a headless environment may fail.
 *
 * CLI/GUI assumptions:
 * - Command-line arguments are accepted by the launcher but are currently ignored by the UI.
 * - The default behavior is to open the graphical CalculatorUI.
 */
public class App {

    /**
     * Main entry point. Keeps startup logic minimal and delegates initialization to a helper.
     *
     * <p>This method logs and rethrows fatal exceptions so that the JVM exit behavior is preserved.
     *
     * @param args command-line arguments (currently ignored by the launcher)
     */
    public static void main(String[] args) {
        try {
            startApplication(args);
        } catch (Throwable fatalError) {
            System.err.println("Fatal error during application startup: " + fatalError.getMessage());
            fatalError.printStackTrace(System.err);
            if (fatalError instanceof Error) {
                throw (Error) fatalError;
            } else if (fatalError instanceof RuntimeException) {
                throw (RuntimeException) fatalError;
            } else {
                throw new RuntimeException(fatalError);
            }
        }
    }

    /**
     * Performs the actual application startup.
     *
     * <p>Separated from main to keep the entry point small and readable. Any non-trivial
     * startup logic should be placed here or delegated further.
     *
     * @param runtimeArguments command-line arguments passed from main
     */
    private static void startApplication(String[] runtimeArguments) {
        new CalculatorUI();
    }
}