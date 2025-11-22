package com.comp2042.model;

import com.comp2042.GameConfig;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility class containing static methods for performing
 * matrix operations required by the Tetris game.
 * This class includes methods for collision detection (intersect),
 * merging bricks, copying matrices, and checking for completed rows.
 * It cannot be instantiated.
 */
public class MatrixOperations {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    //We don't want to instantiate this utility class
    private MatrixOperations(){}

    /**
     * Checks if a brick's shape intersects with the game board matrix at a specific position.
     * An intersection occurs if the brick is out of bounds or overlaps with an existing block.
     *
     * @param matrix The main game board matrix.
     * @param brick  The brick's shape matrix to check.
     * @param x      The target X (column) position of the brick's top-left corner.
     * @param y      The target Y (row) position of the brick's top-left corner.
     * @return {@code true} if there is a collision or bounds violation, {@code false} otherwise.
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper method to check if a specific coordinate is outside the bounds of the game matrix.
     *
     * @param matrix  The main game board matrix.
     * @param targetX The X coordinate (column) to check.
     * @param targetY The Y coordinate (row) to check.
     * @return {@code true} if the coordinate is out of bounds, {@code false} otherwise.
     */
    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        boolean returnValue = true;
        if (targetX >= 0 && targetY < matrix.length && targetX < matrix[targetY].length) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Performs a deep copy of a 2D integer array (matrix).
     *
     * @param original The 2D array to copy.
     * @return A new 2D array instance containing a copy of the original data.
     */
    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    /**
     * Merges a brick's shape onto a copy of the game board matrix at a specific position.
     * This is used to "stamp" a landed brick onto the background.
     *
     * @param filledFields The main game board matrix.
     * @param brick        The brick's shape matrix to merge.
     * @param x            The X (column) position of the brick.
     * @param y            The Y (row) position of the brick.
     * @return A new 2D array representing the merged matrix.
     */
    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0) {
                    copy[targetY][targetX] = brick[j][i];
                }
            }
        }
        return copy;
    }

    /**
     * Checks the game matrix for completed (full) rows, removes them, and collapses the rows above.
     * It also calculates the score bonus based on the number of lines cleared.
     *
     * @param matrix The current game board matrix to check.
     * @return A {@link ClearRow} object containing the new matrix, the number of lines removed,
     * and the calculated score bonus.
     */
    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow);
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }
        int scoreBonus = GameConfig.SCORE_BASE_PER_LINE * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), tmp, scoreBonus);
    }

    /**
     * Performs a deep copy of a List containing 2D integer arrays (matrices).
     *
     * @param list The list of 2D arrays to copy.
     * @return A new {@link List} containing deep copies of the original matrices.
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
