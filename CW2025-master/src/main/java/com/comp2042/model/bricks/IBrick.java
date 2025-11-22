package com.comp2042.model.bricks;

import com.comp2042.model.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the specific shape matrices for the {@code IBrick}.
 * This class implements {@link Brick} and provides the specific rotational states
 * for the "I" shaped Tetromino.
 */
final class IBrick implements Brick {

    // Stores all rotational shapes for this specific brick
    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an {@code IBrick}.
     * This constructor defines and adds all possible rotational states
     * (as {@code int[][]} matrices) for the I-Brick to the internal shape list.
     */
    public IBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        });
    }

    /**
     * {@inheritDoc}
     * This implementation returns a deep copy of the shape list for the {@code IBrick}
     * to prevent the original matrices from being modified.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
