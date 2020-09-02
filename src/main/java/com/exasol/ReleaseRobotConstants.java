package com.exasol;

/**
 * This class contains common project's constants.
 */
public final class ReleaseRobotConstants {
    public static final String VERSION_REGEX = "(\\d+)\\.(\\d+)\\.(\\d+)";
    public static final String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    public static final String TICKET_NUMBER_REGEX = "#[1-9]\\d*\\b";

    private ReleaseRobotConstants() {
        // prevent instantiation
    }
}