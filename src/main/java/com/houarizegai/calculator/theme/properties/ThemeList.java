package com.houarizegai.calculator.theme.properties;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Container for a collection of Theme objects with utility accessors.
 *
 * <p>
 * This class encapsulates a list of Theme instances and provides a convenient
 * method to expose them as a Map keyed by the theme name. It is defensive about
 * null or empty internal collections when producing the map view: if the
 * underlying list is null or empty the map view returned is an immutable empty map.
 * </p>
 *
 * <p>
 * Usage:
 * - Use getThemes() to retrieve the backing list (may be null if not set).
 * - Use setThemes(List) to assign a list of Theme instances.
 * - Use getThemesAsMap() to obtain a Map keyed by Theme.getName(); this method
 *   handles null/empty backing lists safely and returns an empty map in such cases.
 * </p>
 *
 * Note: This class preserves the public API contract of exposing the backing
 * list via getThemes()/setThemes(List). It does not modify or normalize the
 * stored reference in the setter to avoid surprising callers that rely on
 * identity or mutability of the provided list.
 */
public class ThemeList {
    private List<Theme> themes;

    // Named constant for the empty map returned when there are no themes.
    private static final Map<String, Theme> EMPTY_THEME_MAP = Collections.emptyMap();

    // Named constant for the identity function used when building the map.
    private static final Function<Theme, Theme> THEME_IDENTITY_FUNCTION = Function.identity();

    /**
     * Returns the backing list of themes as provided via setThemes().
     *
     * <p>
     * This method returns the list reference that was set previously. It may be null
     * if no list has been set. Callers who need to work with the collection safely
     * should handle the null case or use getThemesAsMap() which provides a null-safe view.
     * </p>
     *
     * @return the backing List of Theme instances, or null if none has been set
     */
    public List<Theme> getThemes() {
        return themes;
    }

    /**
     * Sets the backing list of Theme instances.
     *
     * <p>
     * This setter accepts a null reference to explicitly clear the stored themes.
     * The method intentionally does not defensively copy the provided list to preserve
     * original semantics and avoid surprising callers that depend on the same list
     * instance being shared.
     * </p>
     *
     * @param themes the list of Theme instances to store, or null to clear
     */
    public void setThemes(List<Theme> themes) {
        // No defensive copy here to preserve existing behavior and reference semantics.
        this.themes = themes;
    }

    /**
     * Returns the themes keyed by their name.
     *
     * <p>
     * This method is null-safe: if the backing list of themes is null or empty an
     * immutable empty map is returned. The method preserves existing behavior for
     * non-empty lists and delegates to a private helper to construct the map.
     * </p>
     *
     * @return a Map where keys are Theme.getName() and values are the corresponding Theme;
     *         or an immutable empty map when there are no themes
     * @throws NullPointerException if the backing list contains null elements or if
     *                              any Theme.getName() call returns null (consistent with
     *                              the original behavior when streaming such lists)
     */
    public Map<String, Theme> getThemesAsMap() {
        List<Theme> safeList = getSafeThemesList();
        if (safeList.isEmpty()) {
            // Explicit handling for empty or null backing lists: return an immutable empty map.
            return EMPTY_THEME_MAP;
        }
        return createThemeMapFromList(safeList);
    }

    /**
     * Returns a non-null list reference for internal processing.
     *
     * <p>
     * If the stored themes reference is null, this returns an immutable empty list.
     * This helper centralizes the null-to-empty-list normalization for use by
     * collection-processing methods, reducing duplication.
     * </p>
     *
     * @return a non-null List to use for processing (may be empty)
     */
    private List<Theme> getSafeThemesList() {
        return themes == null ? Collections.emptyList() : themes;
    }

    /**
     * Builds a map from the provided list of Theme instances keyed by name.
     *
     * <p>
     * This helper centralizes the stream-to-map conversion and documents that the
     * method will propagate runtime exceptions produced by the collector (for example,
     * if duplicate keys are encountered or nulls are present). This behavior is
     * intentionally consistent with the original implementation.
     * </p>
     *
     * @param themeList non-null, possibly empty list of Theme instances
     * @return a Map of theme name to Theme instance
     * @throws NullPointerException if themeList contains null entries or a Theme name is null
     */
    private Map<String, Theme> createThemeMapFromList(List<Theme> themeList) {
        Objects.requireNonNull(themeList, "themeList must not be null");
        // Delegate to stream collector; preserve original behavior regarding duplicates and nulls.
        return themeList.stream().collect(Collectors.toMap(Theme::getName, THEME_IDENTITY_FUNCTION));
    }
}