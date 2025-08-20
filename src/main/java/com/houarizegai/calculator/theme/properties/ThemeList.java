package com.houarizegai.calculator.theme.properties;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Container for a collection of Theme instances.
 *
 * <p>Semantics:
 * - This class holds a reference to a List&lt;Theme&gt; provided by callers.
 * - getThemes() returns the backing list reference (i.e., it is mutable). We
 *   deliberately preserve this behavior to avoid changing the public API.
 *
 * Thread-safety:
 * - Instances of this class are <strong>not</strong> thread-safe. If multiple
 *   threads access the same ThemeList instance concurrently and at least one
 *   of the threads modifies the backing list, external synchronization is
 *   required.
 *
 * Nullability:
 * - By contract, callers must provide a non-null list to setThemes(...).
 *   Attempting to set a null list will result in NullPointerException.
 *   Prior to any setThemes call, the internal list may be null; getters will
 *   handle that case (for example, getThemesAsMap() returns an empty map).
 */
public class ThemeList {
    private List<Theme> themes;

    /**
     * Returns the backing list of Theme objects.
     *
     * <p>Important notes for callers:
     * - This method returns the actual backing list reference; it is not a
     *   defensive copy. Modifications to the returned list will affect this
     *   ThemeList instance.
     * - The returned value may be null if no list has been set.
     *
     * @return the backing List of Theme objects, or null if none has been set
     */
    public List<Theme> getThemes() {
        return themes;
    }

    /**
     * Sets the backing list of Theme objects for this instance.
     *
     * <p>Behavior and expectations:
     * - The provided list must not be null. Passing null will cause a
     *   NullPointerException.
     * - The list is assigned directly (no defensive copy) in order to preserve
     *   existing behavior and performance characteristics; therefore the caller
     *   retains responsibility for controlling concurrent access and for any
     *   further mutations.
     *
     * @param themes non-null List of Theme objects to become the backing list
     * @throws NullPointerException if {@code themes} is null
     */
    public void setThemes(List<Theme> themes) {
        this.themes = Objects.requireNonNull(themes, "themes must not be null");
    }

    /**
     * Returns a map of theme name to Theme instance constructed from the
     * backing list.
     *
     * <p>Details:
     * - If the backing list is null or empty, an immutable empty map is
     *   returned.
     * - If multiple Theme instances share the same name, the first occurrence
     *   in the list is preserved and later ones are ignored.
     * - The returned map is a new map and modifications to it do not affect the
     *   backing list.
     *
     * @return an immutable Map mapping theme names to Theme objects; never null
     */
    public Map<String, Theme> getThemesAsMap() {
        return buildThemesMap();
    }

    /*
     * Helper: Safely obtain a Stream from the backing list. This avoids
     * repetitive null checks at call sites and centralizes the "null means
     * empty" policy for stream-based operations.
     */
    private Stream<Theme> safeStreamOfThemes() {
        return themes == null ? Stream.empty() : themes.stream();
    }

    /*
     * Helper: Build a Map from theme name -> Theme instance.
     * - Preserves the first occurrence for duplicate names.
     * - Returns Collections.emptyMap() when there are no entries.
     */
    private Map<String, Theme> buildThemesMap() {
        Map<String, Theme> map = safeStreamOfThemes()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        Theme::getName,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
        return map.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(map);
    }
}