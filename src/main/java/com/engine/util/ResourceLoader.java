package com.engine.util;

import java.io.InputStream;

public class ResourceLoader {
    /**
     * Loads a resource from classpath, automatically handling path conversion
     */
    public static InputStream getResourceAsStream(String path) {
        // Strip "src/main/resources/" if present
        if (path.startsWith("src/main/resources/")) {
            path = path.substring("src/main/resources/".length());
        }

        InputStream stream = ResourceLoader.class.getClassLoader().getResourceAsStream(path);
        if (stream == null) {
            throw new RuntimeException("Resource not found: " + path);
        }
        return stream;
    }
    
    /**
     * Returns the full path to a resource, useful for debugging or logging
     */
    public static String getResourcePath(String path) {
        // Strip "src/main/resources/" if present
        if (path.startsWith("src/main/resources/")) {
            path = path.substring("src/main/resources/".length());
        }
        return ResourceLoader.class.getClassLoader().getResource(path).getPath();
    }
}