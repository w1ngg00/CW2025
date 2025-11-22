package com.comp2042.model;

/**
 * Defines the public contract for any class that represents the core Tetris game board and state.
 * Implementations are responsible for managing the matrix, brick position, and collision logic.
 */
public interface Board {

    /**
     * Attempts to move the current brick down by one unit.
     * @return true if the brick can move, false if collide
     */
    boolean moveBrickDown();

    /**
     * Moves the brick down instantly until it collides.
     * @return The number of rows the brick was dropped.
     */
    int hardDrop();

    /**
     * Attempts to move the current brick left by one unit.
     * @return true if the brick can move, false if collide
     */
    boolean moveBrickLeft();

    /**
     * Moves the brick all the way to the left until it collides.
     */
    void moveBrickLeftMost();

    /**
     * Attempts to move the current brick right by one unit.
     * @return true if the brick can move, false if collide
     */
    boolean moveBrickRight();

    /**
     * Moves the brick all the way to the right until it collides.
     */
    void moveBrickRightMost();

    /**
     * Attempts to rotate the current brick left by 90 degrees.
     * @return true if the brick can move, false if collide
     */
    boolean rotateLeftBrick();
    //void rotateLeftBrick();

    /**
     * Attempts to rotate the currently falling brick 90 degrees right.
     * @return true if the rotation was successful, false if a collision occurred.
     */
    boolean rotateRightBrick();
    //void rotateRightBrick();

    /**
     * Generates a new random brick, sets it as the currently falling brick,
     * and sets its initial position
     *
     * @return true if the newly spawned brick immediately intersects the background (Game Over condition), false otherwise.
     */
    boolean createNewBrick();

    /**
     * Sets up the board with pre-placed obstacles for Hard mode.
     * This is called by GameController *before* the first brick is created.
     */
    void initializeWithObstacles();

    /**
     * Retrieves the current state of the game board matrix (fixed background bricks).
     *
     * @return The 2D array representing the merged blocks.
     */
    int[][] getBoardMatrix();

    /**
     * Generates and retrieves the current view data for the falling brick and the next brick preview.
     *
     * @return ViewData object containing brick shape, position, and the next brick preview shape.
     */
    ViewData getViewData();

    /**
     * Swaps the currently falling brick with the brick in the "Hold" slot.
     * This action is typically only allowed once per new brick.
     *
     * @return {@code true} if the swap was successful, {@code false} if a swap is not allowed (e.g., already swapped this turn).
     */
    boolean swapHoldBrick();

    /**
     * Retrieves the shape of the brick currently in the "Hold" slot.
     *
     * @return The {@code int[][]} matrix of the held brick, or {@code null} if the hold slot is empty.
     */
    int[][] getHoldBrickShape();

    /**
     * Merges the currently falling brick into the static background matrix (when the brick lands).
     */
    void mergeBrickToBackground();

    /**
     * Checks the current game matrix for complete rows, removes them, and calculates the score bonus.
     * Updates the internal game matrix with the resulting configuration.
     *
     * @return A {@link ClearRow} object detailing the result of the row clearance.
     */
    ClearRow clearRows();

    /**
     * Retrieves the {@link Score} object managing the player's score.
     *
     * @return The current score tracker.
     */
    Score getScore();

    /**
     * Resets the game state: clears the board matrix, resets the score, and spawns a new brick.
     */
    void newGame();

    /**
     * Spawns a single obstacle block at a random X coordinate
     * and hard drops it into the board.
     */
    void spawnAndHardDropObstacle();
}
