package com.comp2042.model;

/**
 * Represents the game difficulty levels.
 */
public enum Difficulty {
    /** Easy difficulty (default speed, no obstacles). */
    EASY,
    /** Normal difficulty (speed increases with lines cleared). */
    NORMAL,
    /** Hard difficulty (speed increases, includes initial obstacles). */
    HARD,
    /** Extra difficulty (hard mode + random falling obstacles). */
    EXTRA
}
