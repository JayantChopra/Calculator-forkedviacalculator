package com.houarizegai.calculator.util;

import java.awt.Color;

/**
 * Utility class for parsing hexadecimal color strings into {@link java.awt.Color} instances.
 *
 * <p>Accepted input formats:
 * - "RRGGBB" (6 hexadecimal digits)
 * - "#RRGGBB" (leading hash)
 * - "0xRRGGBB" or "0XRRGGBB" (leading 0x or 0X)
 *
 * <p>Behavior and edge cases:
 * - Leading and trailing whitespace is ignored.
 * - Hex digits are case-insensitive.
 * - Any input that is null, empty, has the wrong length after normalization,
 *   or contains non-hexadecimal characters will cause an {@link IllegalArgumentException}
 *   to be thrown with a descriptive message.
 *
 * <p>Examples:
 * - "#FF00AA" -> new Color(255, 0, 170)
 * - "ff00aa" -> new Color(255, 0, 170)
 *
 * <p>This class is not instantiable.
 */
public class ColorUtil {

    private ColorUtil() {
        throw new AssertionError("Constructor is not allowed");
    }

    /**
     * Converts a hexadecimal color string into a {@link java.awt.Color}.
     *
     * <p>Accepted formats:
     * - "RRGGBB" (6 hex digits)
     * - "#RRGGBB" (leading '#')
     * - "0xRRGGBB" or "0XRRGGBB" (leading "0x" or "0X")
     *
     * <p>On invalid input (null, empty, wrong length after normalization, non-hex characters),
     * this method throws an {@link IllegalArgumentException} with a clear message describing
     * the problem. This method is pure and has no side effects.
     *
     * @param colorHex the color string to parse
     * @return a {@link java.awt.Color} representing the parsed color
     * @throws IllegalArgumentException if the input is null, empty, malformed, or contains invalid hex digits
     */
    public static Color hex2Color(String colorHex) {
        String normalizedHex = normalizeHexString(colorHex);
        int redComponent = parseHexComponent(normalizedHex, 0);
        int greenComponent = parseHexComponent(normalizedHex, 2);
        int blueComponent = parseHexComponent(normalizedHex, 4);
        return new Color(redComponent, greenComponent, blueComponent);
    }

    /**
     * Normalizes an input color string by trimming whitespace and removing a leading "#" or "0x"/"0X"
     * prefix if present. The resulting string must contain exactly 6 hexadecimal characters.
     *
     * @param input the original color string
     * @return a 6-character hex string containing only hexadecimal digits (0-9, a-f, A-F)
     * @throws IllegalArgumentException if input is null, empty after trimming, or does not normalize to 6 hex digits
     */
    private static String normalizeHexString(String input) {
        if (input == null) {
            throw new IllegalArgumentException("colorHex must not be null");
        }
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("colorHex must not be empty or only whitespace");
        }

        String withoutPrefix;
        if (trimmed.startsWith("#")) {
            withoutPrefix = trimmed.substring(1);
        } else if (trimmed.startsWith("0x") || trimmed.startsWith("0X")) {
            withoutPrefix = trimmed.substring(2);
        } else {
            withoutPrefix = trimmed;
        }

        if (withoutPrefix.length() != 6) {
            throw new IllegalArgumentException("colorHex must contain exactly 6 hexadecimal digits after removing prefix; got: '" + withoutPrefix + "'");
        }

        return withoutPrefix;
    }

    /**
     * Parses two hexadecimal characters from the normalized 6-character hex string starting at the given index.
     *
     * @param normalizedHex a 6-character hex string (no prefixes, length == 6)
     * @param startIndex index of the first hex digit to parse (0, 2, or 4)
     * @return integer value in range [0, 255]
     * @throws IllegalArgumentException if parsing fails due to invalid hex digits or invalid indices
     */
    private static int parseHexComponent(String normalizedHex, int startIndex) {
        if (normalizedHex == null) {
            throw new IllegalArgumentException("normalizedHex must not be null");
        }
        if (startIndex < 0 || startIndex + 2 > normalizedHex.length()) {
            throw new IllegalArgumentException("startIndex out of range for normalizedHex: " + startIndex);
        }
        String hexPair = normalizedHex.substring(startIndex, startIndex + 2);
        try {
            int value = Integer.parseInt(hexPair, 16);
            if (value < 0 || value > 255) {
                throw new IllegalArgumentException("Parsed component out of range 0-255: '" + hexPair + "'");
            }
            return value;
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Invalid hexadecimal component '" + hexPair + "' in color string '" + normalizedHex + "'", nfe);
        }
    }
}