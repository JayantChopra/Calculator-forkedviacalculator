package com.houarizegai.calculator.util;

import java.awt.*;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Utility class for converting hexadecimal color strings into java.awt.Color instances.
 *
 * <p>Expected input format for hex color strings:
 * - A non-null String of exactly 6 hexadecimal characters (0-9, a-f, A-F).
 * - No leading "#" or "0x" prefixes are accepted by this utility.
 *
 * <p>Behavior:
 * - If the input is null, hex2Color returns null (preserves legacy behavior).
 * - If the input is non-null but malformed (wrong length or contains non-hex characters),
 *   hex2Color throws IllegalArgumentException with a descriptive message.
 *
 * <p>Examples:
 * - "FFA07A" -> new Color(255, 160, 122)
 * - "000000" -> new Color(0, 0, 0)
 */
public class ColorUtil {

    private static final int EXPECTED_HEX_LENGTH = 6;
    private static final int COMPONENT_HEX_LENGTH = 2;
    private static final int RED_OFFSET = 0;
    private static final int GREEN_OFFSET = 2;
    private static final int BLUE_OFFSET = 4;
    private static final Pattern SIX_HEX_CHARS_PATTERN = Pattern.compile("^[0-9a-fA-F]{6}$");

    private ColorUtil() {
        throw new AssertionError("Constructor is not allowed");
    }

    /**
     * Converts a 6-character hexadecimal color string into a java.awt.Color.
     *
     * @param colorHex A 6-character hex string representing RGB (e.g., "FFA07A").
     *                 Must be exactly 6 characters long and contain only hex digits (0-9, a-f, A-F).
     *                 Null is accepted and will result in a null return value.
     * @return A new Color instance representing the provided hex color, or null if colorHex is null.
     * @throws IllegalArgumentException if colorHex is non-null but not a 6-character hex string.
     */
    public static Color hex2Color(String colorHex) {
        if (colorHex == null) {
            return null;
        }

        validateSixCharHex(colorHex);

        int red = parseHexComponent(colorHex, RED_OFFSET);
        int green = parseHexComponent(colorHex, GREEN_OFFSET);
        int blue = parseHexComponent(colorHex, BLUE_OFFSET);

        return new Color(red, green, blue);
    }

    /**
     * Validates that the provided string is exactly six hexadecimal characters.
     *
     * Package-private to allow unit tests to exercise validation edge-cases.
     *
     * @param hex A non-null string to validate.
     * @throws IllegalArgumentException if the string is not exactly six hex characters.
     */
    static void validateSixCharHex(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException("Hex string must not be null");
        }
        if (hex.length() != EXPECTED_HEX_LENGTH) {
            throw new IllegalArgumentException(
                    "Hex string must be exactly " + EXPECTED_HEX_LENGTH + " characters long: received length "
                            + hex.length());
        }
        if (!SIX_HEX_CHARS_PATTERN.matcher(hex).matches()) {
            throw new IllegalArgumentException("Hex string contains invalid characters: expected [0-9a-fA-F]");
        }
    }

    /**
     * Parses a 2-character hex component from the provided hex string starting at the given offset.
     *
     * Package-private to facilitate unit testing of individual parsing edge-cases.
     *
     * @param hex    A validated 6-character hex string.
     * @param offset The starting index of the 2-character component (0, 2, or 4).
     * @return The parsed integer value in the range [0,255].
     * @throws IllegalArgumentException if offset is invalid or parsing fails.
     */
    static int parseHexComponent(String hex, int offset) {
        if (hex == null) {
            throw new IllegalArgumentException("Hex string must not be null");
        }
        if (offset < 0 || offset + COMPONENT_HEX_LENGTH > hex.length()) {
            throw new IllegalArgumentException("Invalid offset for hex component: " + offset);
        }
        String component = hex.substring(offset, offset + COMPONENT_HEX_LENGTH);
        try {
            int value = Integer.parseInt(component, 16);
            if (value < 0 || value > 255) {
                throw new IllegalArgumentException("Parsed hex component out of range: " + component);
            }
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Failed to parse hex component: " + component, ex);
        }
    }
}