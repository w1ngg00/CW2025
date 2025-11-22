package com.comp2042;

/**
 * Contains all static configuration values and magic numbers for the game.
 * This class is final and non-instantiable.
 */
public final class GameConfig {

    // No need to create an instance for this just read from this file
    /** Private constructor to prevent instantiation of this utility class. */
    private GameConfig() {}

    // Window
    /** Width of the application window in pixels. */
    public static final int WINDOW_WIDTH = 500;
    /** Height of the application window in pixels. */
    public static final int WINDOW_HEIGHT = 600;

    // Board
    /** Height of the game board matrix (rows). */
    public static final int BOARD_HEIGHT = 25;
    /** Width of the game board matrix (columns). */
    public static final int BOARD_WIDTH = 10;
    /** X coordinate for spawning new bricks. */
    public static final int BRICK_SPAWN_X = 4;
    /** Y coordinate for spawning new bricks. */
    public static final int BRICK_SPAWN_Y = 1;
    /** The size of a single brick square in pixels. */
    public static final int BRICK_SIZE = 20;


    // Timing and Difficulty
    /** The initial speed of the game loop's auto-drop in milliseconds (EASY mode base). */
    public static final int GAME_SPEED_MS = 400;
    /** The timer to generate random brick in Extra Hard mode (in milliseconds). */
    public static final int EXTRA_TIMER = 5000;
    /** The probability (0.0 to 1.0) of an obstacle spawning in Hard/Extra mode. */
    public static final double OBSTACLE_PROBABILITY = 0.6;
    /** The number of lines to clear to advance one level. */
    public static final int ROWS_PER_LEVEL = 5;
    /** The factor by which game speed increases per level (e.g., 0.95 = 5% faster). */
    public static final double SPEED_INCREASE_FACTOR = 0.95;
    /** The Y-offset for "Speed UP!" notifications to avoid overlapping score. */
    public static final double SPEEDUP_NOTIFICATION_Y_OFFSET = 30.0;


    // UI
    /** The Y-offset used to position the falling brick panel correctly on screen. */
    public static final int BRICK_PANEL_Y_OFFSET = -42;
    /** The size (in px) of the main "Next Brick" panel. */
    public static final double NEXT_BRICK_SIZE_LARGE = 12.0;
    /** The size (in px) of the smaller "Next Brick" queue panels. */
    public static final double NEXT_BRICK_SIZE_SMALL = 8.0;



    // Score
    /** Base score added per line clear (multiplied by lines^2). */
    public static final int SCORE_BASE_PER_LINE = 50;
    /** Score added per unit for a soft drop (user pressing DOWN). */
    public static final int SOFT_DROP_SCORE = 1;
    /** Score multiplier for a hard drop per row moved. */
    public static final int HARD_DROP_SCORE_MULTIPLIER = 2;
}
