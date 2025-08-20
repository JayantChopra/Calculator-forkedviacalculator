package com.houarizegai.calculator.theme.properties;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents a visual theme for the calculator application.
 *
 * <p>This class is a plain data holder for theme properties such as colors and a name.
 * It performs basic validation on inputs (non-null and color format checks) and normalizes
 * color values to an uppercase hex representation. Equality, hashCode and toString are
 * implemented to include all properties.</p>
 */
public class Theme {

    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$");

    private String name;
    private String applicationBackground;
    private String textColor;
    private String btnEqualTextColor;
    private String operatorBackground;
    private String numbersBackground;
    private String btnEqualBackground;

    /**
     * Creates an empty Theme instance. Use setters to populate properties.
     * This no-arg constructor is provided for serialization frameworks and tests.
     */
    public Theme() {
        // Intentionally empty to allow frameworks to instantiate then set properties.
    }

    /**
     * Creates a Theme with all properties specified.
     *
     * @param name                  the unique name for the theme; must be non-null and non-empty
     * @param applicationBackground background color for the application; must be a hex color like "#RRGGBB" or "#AARRGGBB"
     * @param textColor             primary text color; must be a hex color
     * @param btnEqualTextColor     text color for the equals button; must be a hex color
     * @param operatorBackground    background color for operator buttons; must be a hex color
     * @param numbersBackground     background color for number buttons; must be a hex color
     * @param btnEqualBackground    background color for the equals button; must be a hex color
     * @throws IllegalArgumentException if any argument is null, empty (for name) or a color string is invalid
     */
    public Theme(String name,
                 String applicationBackground,
                 String textColor,
                 String btnEqualTextColor,
                 String operatorBackground,
                 String numbersBackground,
                 String btnEqualBackground) {
        this.setName(name);
        this.setApplicationBackground(applicationBackground);
        this.setTextColor(textColor);
        this.setBtnEqualTextColor(btnEqualTextColor);
        this.setOperatorBackground(operatorBackground);
        this.setNumbersBackground(numbersBackground);
        this.setBtnEqualBackground(btnEqualBackground);
    }

    /**
     * Returns the theme name.
     *
     * @return theme name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the theme name.
     *
     * @param name the theme name; must be non-null and not blank
     * @throws IllegalArgumentException if name is null or blank
     */
    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    /**
     * Returns the application background color.
     *
     * @return application background color string (hex)
     */
    public String getApplicationBackground() {
        return applicationBackground;
    }

    /**
     * Sets the application background color.
     *
     * @param applicationBackground color string in hex format (e.g. "#FFFFFF" or "#FF000000")
     * @throws IllegalArgumentException if the color string is null or invalid
     */
    public void setApplicationBackground(String applicationBackground) {
        this.applicationBackground = normalizeAndValidateColor(applicationBackground, "applicationBackground");
    }

    /**
     * Returns the primary text color.
     *
     * @return text color string (hex)
     */
    public String getTextColor() {
        return textColor;
    }

    /**
     * Sets the primary text color.
     *
     * @param textColor color string in hex format
     * @throws IllegalArgumentException if the color string is null or invalid
     */
    public void setTextColor(String textColor) {
        this.textColor = normalizeAndValidateColor(textColor, "textColor");
    }

    /**
     * Returns the equals button text color.
     *
     * @return equals button text color string (hex)
     */
    public String getBtnEqualTextColor() {
        return btnEqualTextColor;
    }

    /**
     * Sets the equals button text color.
     *
     * @param btnEqualTextColor color string in hex format
     * @throws IllegalArgumentException if the color string is null or invalid
     */
    public void setBtnEqualTextColor(String btnEqualTextColor) {
        this.btnEqualTextColor = normalizeAndValidateColor(btnEqualTextColor, "btnEqualTextColor");
    }

    /**
     * Returns the operator buttons background color.
     *
     * @return operator background color string (hex)
     */
    public String getOperatorBackground() {
        return operatorBackground;
    }

    /**
     * Sets the operator buttons background color.
     *
     * @param operatorBackground color string in hex format
     * @throws IllegalArgumentException if the color string is null or invalid
     */
    public void setOperatorBackground(String operatorBackground) {
        this.operatorBackground = normalizeAndValidateColor(operatorBackground, "operatorBackground");
    }

    /**
     * Returns the numbers buttons background color.
     *
     * @return numbers background color string (hex)
     */
    public String getNumbersBackground() {
        return numbersBackground;
    }

    /**
     * Sets the numbers buttons background color.
     *
     * @param numbersBackground color string in hex format
     * @throws IllegalArgumentException if the color string is null or invalid
     */
    public void setNumbersBackground(String numbersBackground) {
        this.numbersBackground = normalizeAndValidateColor(numbersBackground, "numbersBackground");
    }

    /**
     * Returns the equals button background color.
     *
     * @return equals button background color string (hex)
     */
    public String getBtnEqualBackground() {
        return btnEqualBackground;
    }

    /**
     * Sets the equals button background color.
     *
     * @param btnEqualBackground color string in hex format
     * @throws IllegalArgumentException if the color string is null or invalid
     */
    public void setBtnEqualBackground(String btnEqualBackground) {
        this.btnEqualBackground = normalizeAndValidateColor(btnEqualBackground, "btnEqualBackground");
    }

    /**
     * Checks equality based on all theme properties.
     *
     * @param o other object
     * @return true if all properties are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Theme)) return false;
        Theme theme = (Theme) o;
        return Objects.equals(name, theme.name) &&
                Objects.equals(applicationBackground, theme.applicationBackground) &&
                Objects.equals(textColor, theme.textColor) &&
                Objects.equals(btnEqualTextColor, theme.btnEqualTextColor) &&
                Objects.equals(operatorBackground, theme.operatorBackground) &&
                Objects.equals(numbersBackground, theme.numbersBackground) &&
                Objects.equals(btnEqualBackground, theme.btnEqualBackground);
    }

    /**
     * Computes hash code consistent with equals().
     *
     * @return computed hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name,
                applicationBackground,
                textColor,
                btnEqualTextColor,
                operatorBackground,
                numbersBackground,
                btnEqualBackground);
    }

    /**
     * Returns a string representation including all properties.
     *
     * @return string representation of the theme
     */
    @Override
    public String toString() {
        return "Theme{" +
                "name='" + name + '\'' +
                ", applicationBackground='" + applicationBackground + '\'' +
                ", textColor='" + textColor + '\'' +
                ", btnEqualTextColor='" + btnEqualTextColor + '\'' +
                ", operatorBackground='" + operatorBackground + '\'' +
                ", numbersBackground='" + numbersBackground + '\'' +
                ", btnEqualBackground='" + btnEqualBackground + '\'' +
                '}';
    }

    // -------------------------
    // Private helper utilities
    // -------------------------

    /**
     * Validates that a name is non-null and not blank.
     *
     * @param candidateName the name to validate
     * @throws IllegalArgumentException if candidateName is null or blank
     */
    private static void validateName(String candidateName) {
        if (candidateName == null) {
            throw new IllegalArgumentException("Theme name must not be null.");
        }
        if (candidateName.trim().isEmpty()) {
            throw new IllegalArgumentException("Theme name must not be empty or blank.");
        }
    }

    /**
     * Validates and normalizes a color string. The returned value is an uppercase hex color string.
     *
     * @param colorValue the color string to validate (must be non-null and match hex pattern)
     * @param propertyName name of the property being validated for clearer error messages
     * @return normalized (uppercase) color string
     * @throws IllegalArgumentException if colorValue is null or not a valid hex color
     */
    private static String normalizeAndValidateColor(String colorValue, String propertyName) {
        if (colorValue == null) {
            throw new IllegalArgumentException(propertyName + " must not be null.");
        }
        String trimmed = colorValue.trim();
        if (!isValidHexColor(trimmed)) {
            throw new IllegalArgumentException(propertyName + " must be a valid hex color like \"#RRGGBB\" or \"#AARRGGBB\". Provided: " + colorValue);
        }
        return formatColor(trimmed);
    }

    /**
     * Pure helper that checks whether a string matches accepted hex color formats.
     *
     * @param color candidate color string
     * @return true if the color is in #RRGGBB or #AARRGGBB hex format
     */
    private static boolean isValidHexColor(String color) {
        return color != null && HEX_COLOR_PATTERN.matcher(color).matches();
    }

    /**
     * Pure helper that formats a hex color to a canonical uppercase representation.
     *
     * @param color validated color string
     * @return uppercase color string
     */
    private static String formatColor(String color) {
        return color.toUpperCase();
    }
}