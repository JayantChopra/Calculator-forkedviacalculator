package com.houarizegai.calculator.theme.properties;

import java.util.regex.Pattern;

/**
 * Theme represents a collection of UI color values and a theme name used by the calculator.
 *
 * <p>All color properties expect a non-null hex color string in one of the common CSS hex formats:
 * - #RGB (3 hex digits)
 * - #RRGGBB (6 hex digits)
 * - #AARRGGBB (8 hex digits, alpha + RGB)
 *
 * Color values are normalized by trimming whitespace and converted to uppercase.
 * Theme name must be non-null, non-empty and not exceed MAX_NAME_LENGTH characters.
 */
public class Theme {

    // Maximum allowed length for a theme name.
    private static final int MAX_NAME_LENGTH = 100;

    // Acceptable hex color formats: 3, 6 or 8 hex digits following '#'.
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#(?:[A-Fa-f0-9]{3}|[A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$");

    // Internal, more descriptive field names. Public getter/setter names are preserved for API compatibility.
    private String themeName;
    private String applicationBackgroundColor;
    private String textColorValue;
    private String buttonEqualTextColor;
    private String operatorBackgroundColor;
    private String numbersBackgroundColor;
    private String buttonEqualBackgroundColor;

    /**
     * Returns the theme name.
     *
     * @return the non-null, non-empty theme name (trimmed). Maximum length: {@value #MAX_NAME_LENGTH}.
     */
    public String getName() {
        return themeName;
    }

    /**
     * Sets the theme name.
     *
     * <p>Expected semantics:
     * - Non-null, non-empty string after trimming.
     * - Maximum length: {@value #MAX_NAME_LENGTH} characters.
     *
     * @param name the theme name to set
     * @throws IllegalArgumentException if {@code name} is null, empty, or exceeds maximum length
     */
    public void setName(String name) {
        this.themeName = validateName(name);
    }

    /**
     * Returns the application background color.
     *
     * @return a hex color string in formats #RGB, #RRGGBB or #AARRGGBB (uppercase, trimmed)
     */
    public String getApplicationBackground() {
        return applicationBackgroundColor;
    }

    /**
     * Sets the application background color.
     *
     * <p>Accepted formats: #RGB, #RRGGBB, #AARRGGBB. Value is normalized (trimmed and uppercased).
     *
     * @param applicationBackground hex color string for application background
     * @throws IllegalArgumentException if the color is null, empty, or not a valid hex color string
     */
    public void setApplicationBackground(String applicationBackground) {
        this.applicationBackgroundColor = validateColor(applicationBackground, "applicationBackground");
    }

    /**
     * Returns the primary text color used in the UI.
     *
     * @return a hex color string in formats #RGB, #RRGGBB or #AARRGGBB (uppercase, trimmed)
     */
    public String getTextColor() {
        return textColorValue;
    }

    /**
     * Sets the primary text color used in the UI.
     *
     * <p>Accepted formats: #RGB, #RRGGBB, #AARRGGBB. Value is normalized (trimmed and uppercased).
     *
     * @param textColor hex color string for primary text
     * @throws IllegalArgumentException if the color is null, empty, or not a valid hex color string
     */
    public void setTextColor(String textColor) {
        this.textColorValue = validateColor(textColor, "textColor");
    }

    /**
     * Returns the text color used on the "=" button.
     *
     * @return a hex color string in formats #RGB, #RRGGBB or #AARRGGBB (uppercase, trimmed)
     */
    public String getBtnEqualTextColor() {
        return buttonEqualTextColor;
    }

    /**
     * Sets the text color used on the "=" button.
     *
     * <p>Accepted formats: #RGB, #RRGGBB, #AARRGGBB. Value is normalized (trimmed and uppercased).
     *
     * @param btnEqualTextColor hex color string for "=" button text
     * @throws IllegalArgumentException if the color is null, empty, or not a valid hex color string
     */
    public void setBtnEqualTextColor(String btnEqualTextColor) {
        this.buttonEqualTextColor = validateColor(btnEqualTextColor, "btnEqualTextColor");
    }

    /**
     * Returns the background color used for operator buttons (e.g., +, -, ×, ÷).
     *
     * @return a hex color string in formats #RGB, #RRGGBB or #AARRGGBB (uppercase, trimmed)
     */
    public String getOperatorBackground() {
        return operatorBackgroundColor;
    }

    /**
     * Sets the background color used for operator buttons.
     *
     * <p>Accepted formats: #RGB, #RRGGBB, #AARRGGBB. Value is normalized (trimmed and uppercased).
     *
     * @param operatorBackground hex color string for operator button background
     * @throws IllegalArgumentException if the color is null, empty, or not a valid hex color string
     */
    public void setOperatorBackground(String operatorBackground) {
        this.operatorBackgroundColor = validateColor(operatorBackground, "operatorBackground");
    }

    /**
     * Returns the background color used for numeric buttons.
     *
     * @return a hex color string in formats #RGB, #RRGGBB or #AARRGGBB (uppercase, trimmed)
     */
    public String getNumbersBackground() {
        return numbersBackgroundColor;
    }

    /**
     * Sets the background color used for numeric buttons.
     *
     * <p>Accepted formats: #RGB, #RRGGBB, #AARRGGBB. Value is normalized (trimmed and uppercased).
     *
     * @param numbersBackground hex color string for numeric button background
     * @throws IllegalArgumentException if the color is null, empty, or not a valid hex color string
     */
    public void setNumbersBackground(String numbersBackground) {
        this.numbersBackgroundColor = validateColor(numbersBackground, "numbersBackground");
    }

    /**
     * Returns the background color used for the "=" button.
     *
     * @return a hex color string in formats #RGB, #RRGGBB or #AARRGGBB (uppercase, trimmed)
     */
    public String getBtnEqualBackground() {
        return buttonEqualBackgroundColor;
    }

    /**
     * Sets the background color used for the "=" button.
     *
     * <p>Accepted formats: #RGB, #RRGGBB, #AARRGGBB. Value is normalized (trimmed and uppercased).
     *
     * @param btnEqualBackground hex color string for "=" button background
     * @throws IllegalArgumentException if the color is null, empty, or not a valid hex color string
     */
    public void setBtnEqualBackground(String btnEqualBackground) {
        this.buttonEqualBackgroundColor = validateColor(btnEqualBackground, "btnEqualBackground");
    }

    // -----------------------
    // Private helper methods
    // -----------------------

    /**
     * Validates and normalizes a color string. Trims whitespace and converts to uppercase.
     *
     * @param color the input color string to validate
     * @param propertyName the logical property name used to generate clear exception messages
     * @return the normalized color string (trimmed and uppercased)
     * @throws IllegalArgumentException if the input is null, empty, or does not match accepted hex formats
     */
    private String validateColor(String color, String propertyName) {
        if (color == null) {
            throw new IllegalArgumentException(propertyName + " must not be null");
        }
        String normalized = color.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(propertyName + " must not be empty or whitespace");
        }
        // Normalize to uppercase for consistent internal representation.
        String upper = normalized.toUpperCase();
        if (!HEX_COLOR_PATTERN.matcher(upper).matches()) {
            throw new IllegalArgumentException(propertyName + " must be a hex color string in formats #RGB, #RRGGBB or #AARRGGBB");
        }
        return upper;
    }

    /**
     * Validates and normalizes a theme name. Trims whitespace.
     *
     * @param name the input name to validate
     * @return trimmed name
     * @throws IllegalArgumentException if the input is null, empty, or too long
     */
    private String validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("name must not be empty or whitespace");
        }
        if (trimmed.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("name must not exceed " + MAX_NAME_LENGTH + " characters");
        }
        return trimmed;
    }
}