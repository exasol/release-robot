package com.exasol.releasedroid.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents maven pom.xml file.
 */
public class MavenPom {
    private final String artifactId;
    private final String version;
    private final Map<String, String> properties;
    private final Map<String, MavenPlugin> plugins;

    private MavenPom(final Builder builder) {
        this.artifactId = builder.artifactId;
        this.version = builder.version;
        this.properties = builder.properties;
        this.plugins = builder.plugins;
    }

    /**
     * Get project's artifact id.
     * 
     * @return artifact id
     */
    public String getArtifactId() {
        return this.artifactId;
    }

    /**
     * Check if artifact id presents.
     * 
     * @return true if artifactId presents
     */
    public boolean hasArtifactId() {
        return this.artifactId != null && !this.artifactId.isEmpty();
    }

    /**
     * Get version.
     * 
     * @return version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Check if version presents.
     *
     * @return true if version presents
     */
    public boolean hasVersion() {
        return this.version != null && !this.version.isEmpty();
    }

    /**
     * Get properties map.
     * 
     * @return properties map
     */
    public Map<String, String> getProperties() {
        return this.properties;
    }

    /**
     * Check if properties present.
     *
     * @return true if properties present
     */
    public boolean hasProperties() {
        return !this.properties.isEmpty();
    }

    /**
     * Get a map of maven plugins.
     * 
     * @return map with plugins' names and {@link MavenPlugin}
     */
    public Map<String, MavenPlugin> getPlugins() {
        return this.plugins;
    }

    /**
     * Check if plugins present.
     *
     * @return true if plugins present
     */
    public boolean hasPlugins() {
        return !this.plugins.isEmpty();
    }

    /**
     * Get a {@link MavenPom} builder.
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for the {@link MavenPom}.
     */
    public static class Builder {
        private String artifactId;
        private String version;
        private Map<String, String> properties = new HashMap<>();
        private Map<String, MavenPlugin> plugins = new HashMap<>();

        /**
         * Add version.
         * 
         * @param version version
         * @return builder instance for fluent programming
         */
        public Builder version(final String version) {
            this.version = version;
            return this;
        }

        /**
         * Add artifact id.
         *
         * @param artifactId artifact id
         * @return builder instance for fluent programming
         */
        public Builder artifactId(final String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        /**
         * Add properties map.
         *
         * @param properties properties as a map
         * @return builder instance for fluent programming
         */
        public Builder properties(final Map<String, String> properties) {
            this.properties = properties;
            return this;
        }

        /**
         * Create a new instance of {@link MavenPom}.
         * 
         * @return new instance of {@link MavenPom}
         */
        public MavenPom build() {
            return new MavenPom(this);
        }

        /**
         * Add plugins.
         *
         * @param plugins map with plugins' names and {@link MavenPlugin}
         * @return builder instance for fluent programming
         */
        public Builder plugins(final Map<String, MavenPlugin> plugins) {
            this.plugins = plugins;
            return this;
        }
    }
}