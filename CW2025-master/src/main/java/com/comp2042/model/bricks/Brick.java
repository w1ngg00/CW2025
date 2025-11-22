package com.comp2042.model.bricks;

import java.util.List;

/**
 * Represents the fundamental contract for a Tetris piece (Tetromino).
 * All specific brick types (e.g., IBrick, JBrick) must implement this interface.
 */
public interface Brick {

    /**
     * Gets the list of all possible rotation shapes for this brick.
     * Each {@code int[][]} in the list represents one rotational state,
     * typically defined in a 4x4 or 3x3 matrix.
     *
     * @return A {@link List} containing all rotational matrices ({@code int[][]}) for the brick.
     */
    List<int[][]> getShapeMatrix();
}
