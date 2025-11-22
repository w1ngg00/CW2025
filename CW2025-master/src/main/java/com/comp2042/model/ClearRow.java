package com.comp2042.model;

/**
 * An immutable data object that holds the result of a row-clearing operation.
 * This class encapsulates the number of lines removed, the new state of the game matrix
 * after the lines have been removed and shifted down, and the score bonus awarded.
 */
public final class ClearRow {

    /** The total number of lines successfully removed in this operation. */
    private final int linesRemoved;
    /** The new state of the game matrix after rows have been cleared and collapsed. */
    private final int[][] newMatrix;
    /** The calculated score bonus for this specific row-clearing event. */
    private final int scoreBonus;

    /**
     * Constructs a new ClearRow result.
     *
     * @param linesRemoved The number of lines that were cleared.
     * @param newMatrix    The resulting game matrix after the clearing.
     * @param scoreBonus   The score bonus awarded for clearing these lines.
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Gets the number of lines that were removed.
     *
     * @return The total lines cleared (e.g., 0, 1, 2, 3, or 4).
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets a deep copy of the new game matrix after rows were cleared.
     *
     * @return A new 2D array representing the board state post-clearance.
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Gets the score bonus awarded for this row clear.
     *
     * @return The calculated score bonus.
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}
