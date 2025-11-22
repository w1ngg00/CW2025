package com.comp2042.model;

import java.util.List;

/**
 * An immutable data object that encapsulates all necessary information
 * for the {@link com.comp2042.view.GuiController} to render a single frame of the game.
 * This class is constructed by the {@link Board} and passed to the View.
 * It includes data for the falling brick, the ghost piece, the hold piece,
 * and the upcoming brick queue.
 */
public final class ViewData {

    /** The 2D matrix of the currently falling brick. */
    private final int[][] brickData;
    /** The current X (column) position of the falling brick. */
    private final int xPosition;
    /** The current Y (row) position of the falling brick. */
    private final int yPosition;
    /** The list of upcoming brick shapes for the "Next" preview queue. */
    private final List<int[][]> nextBrickData;  // to handle list of bricks
    /** The calculated Y (row) position for the ghost piece (drop forecast). */
    private final int ghostYPosition;   // Y coordinates of ghost piece
    /** The 2D matrix of the currently held brick, or {@code null} if none. */
    private final int[][] holdBrickData;

    /**
     * Constructs a new, comprehensive ViewData object.
     *
     * @param brickData       The 2D matrix of the currently falling brick.
     * @param xPosition       The current X (column) position of the falling brick.
     * @param yPosition       The current Y (row) position of the falling brick.
     * @param nextBrickData   The list of upcoming brick shapes for the preview queue.
     * @param ghostYPosition  The calculated Y (row) position for the ghost piece.
     * @param holdBrickData   The 2D matrix of the currently held brick, or {@code null}.
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextBrickData, int ghostYPosition, int[][] holdBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostYPosition = ghostYPosition;
        this.holdBrickData = holdBrickData;
    }

    /**
     * Gets a deep copy of the currently falling brick's shape matrix.
     *
     * @return A new {@code int[][]} instance of the brick's shape.
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Gets the current X (column) position of the falling brick.
     *
     * @return The X coordinate.
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the current Y (row) position of the falling brick.
     *
     * @return The Y coordinate.
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets the calculated Y (row) position for the ghost piece.
     *
     * @return The ghost piece's Y coordinate.
     */
    public int getGhostYPosition() { return ghostYPosition; }

    /**
     * Gets the list of upcoming brick shapes for the preview queue.
     *
     * @return A {@link List} of {@code int[][]} matrices.
     */
    public List<int[][]> getNextBrickData() {
        //return MatrixOperations.copy(nextBrickData);
        return nextBrickData;
    }

    /**
     * Gets the shape matrix of the currently held brick.
     *
     * @return The {@code int[][]} matrix of the held brick, or {@code null} if none.
     */
    public int[][] getHoldBrickData() { return holdBrickData; }
}
