package com.houarizegai.calculator.theme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.houarizegai.calculator.theme.properties.Theme;
import com.houarizegai.calculator.theme.properties.ThemeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Utility class responsible for loading theme definitions from a YAML resource.
 *
 * <p>Expected resource:
 * - Path: "src/main/resources/application.yaml"
 * - The YAML must contain a structure that maps to {@link ThemeList}, whose
 *   getThemesAsMap() returns a Map&lt;String, Theme&gt;.
 *
 * <p>This class preserves the original public API: callers use {@link #loadThemes()}
 * which returns a non-null Map of themes. In case of problems reading/parsing the
 * resource, an empty map is returned (same behavior as original implementation).
 *
 * <p>Internally, I/O and parsing failures are wrapped in a well-defined unchecked
 * {@link ThemeLoadingException} to provide contextual error information. The public
 * method catches that exception and returns an empty map to preserve original behavior.
 */
public class ThemeLoader {

    private ThemeLoader() {
        throw new AssertionError("Constructor is not allowed");
    }

    /**
     * Loads themes declared in the YAML resource "src/main/resources/application.yaml".
     *
     * <p>Returns:
     * - a non-null Map&lt;String, Theme&gt; containing the loaded themes on success
     * - an empty map if the resource is missing, unreadable, or parsing fails
     *
     * Note: This method preserves the original behavior by returning an empty map
     * on failure rather than propagating exceptions. For diagnostics, failures are
     * wrapped internally in {@link ThemeLoadingException}.
     *
     * @return a non-null map of theme name to {@link Theme}; empty map on error
     */
    public static Map<String, Theme> loadThemes() {
        try {
            return loadThemesInternal();
        } catch (ThemeLoadingException e) {
            // Preserve original behavior: return an empty map on any loading error.
            return Collections.emptyMap();
        }
    }

    /**
     * Internal implementation that performs the actual file reading and YAML parsing.
     *
     * <p>This method wraps low-level IO and parsing exceptions into {@link ThemeLoadingException}
     * to provide contextual messages while keeping the public API stable.
     *
     * @return map of theme name to Theme; never null when successful
     * @throws ThemeLoadingException if reading or parsing fails
     */
    private static Map<String, Theme> loadThemesInternal() {
        final String resourcePath = "src/main/resources/application.yaml";

        // Validate the expected resource path
        if (resourcePath == null || resourcePath.trim().isEmpty()) {
            throw new ThemeLoadingException("Resource path for themes is null or empty");
        }

        File file = new File(resourcePath);
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            // Resource missing or unreadable; wrap in specific exception for diagnostics
            throw new ThemeLoadingException("Theme resource is missing or not readable at: " + resourcePath);
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();

        // Use try-with-resources to ensure the file stream is closed to avoid leaks.
        try (FileInputStream fis = new FileInputStream(file)) {
            // Read YAML into ThemeList. The mapper will map the YAML structure to ThemeList.
            ThemeList themeList = mapper.readValue(fis, ThemeList.class);

            // Defensive null checks: ensure non-null return values.
            if (themeList == null) {
                return Collections.emptyMap();
            }

            Map<String, Theme> themesMap = themeList.getThemesAsMap();
            return themesMap == null ? Collections.emptyMap() : themesMap;
        } catch (IOException e) {
            // Wrap IO exceptions with contextual information.
            throw new ThemeLoadingException("Failed to read or parse theme definitions from: " + resourcePath, e);
        } catch (Exception e) {
            // Catch-all for unexpected parsing/runtime exceptions and wrap them.
            throw new ThemeLoadingException("Unexpected error while loading themes from: " + resourcePath, e);
        }
    }

    /**
     * Well-defined unchecked exception used to wrap I/O and parsing failures
     * encountered while loading theme resources. Using a specific exception type
     * makes diagnostics clearer and allows differentiated handling if needed in
     * the future.
     */
    public static final class ThemeLoadingException extends RuntimeException {
        public ThemeLoadingException(String message) {
            super(message);
        }

        public ThemeLoadingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}