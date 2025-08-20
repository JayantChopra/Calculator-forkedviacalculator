package com.houarizegai.calculator.theme;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.houarizegai.calculator.theme.properties.Theme;
import com.houarizegai.calculator.theme.properties.ThemeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class responsible for loading theme definitions from a YAML configuration file.
 *
 * <p>Assumptions:
 * - Themes are defined in a YAML file located at CONFIG_PATH.
 * - The YAML structure maps to ThemeList and Theme value objects.
 *
 * <p>Behavior:
 * - loadThemes() attempts to read and parse the configuration file and returns a map of themes.
 * - On any validation, IO or parsing failure the method returns an empty map (never null) and logs
 *   the failure at a WARNING level.
 *
 * <p>Error conditions:
 * - Missing or unreadable configuration file results in an empty map and a logged warning.
 * - Malformed YAML or mapping errors result in an empty map and a logged warning.
 *
 * <p>Note: This class is not instantiable; all functionality is exposed via static methods. Helper
 * package-private seams are provided to allow unit tests to exercise parsing behavior without
 * exposing additional public APIs.
 */
public class ThemeLoader {

    private static final Logger LOGGER = Logger.getLogger(ThemeLoader.class.getName());

    private static final String CONFIG_PATH = "src/main/resources/application.yaml";

    private ThemeLoader() {
        throw new AssertionError("Constructor is not allowed");
    }

    /**
     * Loads theme definitions from the configured YAML file and returns them as a map keyed by
     * theme name.
     *
     * <p>On success: returns a non-null Map<String, Theme> containing the loaded themes (may be
     * empty if no themes are defined).
     *
     * <p>On failure (file not found, unreadable, parse error, invalid structure): logs a warning and
     * returns an empty map. This method never returns null.
     *
     * @return a Map of theme names to Theme instances; never null. Returns Collections.emptyMap()
     *     on failure.
     */
    public static Map<String, Theme> loadThemes() {
        ObjectMapper mapper = createYamlMapper();
        File configFile = new File(CONFIG_PATH);

        if (!isReadableConfigFile(configFile)) {
            LOGGER.log(Level.WARNING, "Theme configuration file not found or not readable: {0}", CONFIG_PATH);
            return Collections.emptyMap();
        }

        try {
            ThemeList themeList = parseThemeList(configFile, mapper);
            if (!isValidThemeList(themeList)) {
                LOGGER.log(Level.WARNING, "Parsed theme list is empty or invalid in file: {0}", CONFIG_PATH);
                return Collections.emptyMap();
            }

            Map<String, Theme> themes = themeList.getThemesAsMap();
            return themes != null ? themes : Collections.emptyMap();
        } catch (JsonProcessingException jpe) {
            // More specific parsing/mapping problem (invalid YAML or mapping issues).
            LOGGER.log(Level.WARNING, "Failed to parse theme configuration YAML: " + CONFIG_PATH, jpe);
            return Collections.emptyMap();
        } catch (IOException ioe) {
            // IO problems reading the file.
            LOGGER.log(Level.WARNING, "I/O error while reading theme configuration file: " + CONFIG_PATH, ioe);
            return Collections.emptyMap();
        } catch (RuntimeException re) {
            // Defensive: catch unchecked exceptions from mapping/validation to preserve outward behavior.
            LOGGER.log(Level.WARNING, "Unexpected error while loading themes from: " + CONFIG_PATH, re);
            return Collections.emptyMap();
        }
    }

    /**
     * Create and configure an ObjectMapper for YAML processing.
     *
     * Package-private to allow unit tests to replace or inspect the mapper if needed.
     *
     * @return configured ObjectMapper for YAML.
     */
    static ObjectMapper createYamlMapper() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        return mapper;
    }

    /**
     * Parse a ThemeList instance from the provided configuration file using the given mapper.
     *
     * Package-private seam to allow unit tests to exercise parsing behavior.
     *
     * @param file the YAML configuration file to parse; must be non-null and readable.
     * @param mapper the ObjectMapper configured for YAML parsing; must be non-null.
     * @return the parsed ThemeList instance, may be null if file contains no content.
     * @throws IOException if an I/O error occurs while reading the file.
     * @throws JsonProcessingException if parsing or mapping fails due to malformed content.
     */
    static ThemeList parseThemeList(File file, ObjectMapper mapper) throws IOException, JsonProcessingException {
        if (file == null) {
            throw new IllegalArgumentException("file must not be null");
        }
        if (mapper == null) {
            throw new IllegalArgumentException("mapper must not be null");
        }

        try (InputStream in = new FileInputStream(file)) {
            // readValue(InputStream, Class) will throw JsonProcessingException on malformed YAML
            return mapper.readValue(in, ThemeList.class);
        }
    }

    /**
     * Validate that the provided ThemeList contains usable theme data.
     *
     * @param themeList the ThemeList to validate (may be null).
     * @return true if themeList is non-null and contains a non-null map of themes; false otherwise.
     */
    private static boolean isValidThemeList(ThemeList themeList) {
        if (themeList == null) {
            return false;
        }
        Map<String, Theme> themes = themeList.getThemesAsMap();
        return themes != null && !themes.isEmpty();
    }

    /**
     * Checks whether the configuration file exists and is readable.
     *
     * @param file the file to check; may be null.
     * @return true if file exists, is a normal file and can be read; false otherwise.
     */
    private static boolean isReadableConfigFile(File file) {
        return file != null && file.exists() && file.isFile() && file.canRead();
    }
}