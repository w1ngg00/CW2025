package com.comp2042.model.bricks;

import com.comp2042.model.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the specific shape matrices for the {@code ZBrick}.
 * This class implements {@link Brick} and provides the specific rotational states
 * for the "Z" shaped Tetromino.
 */
final class ZBrick implements Brick {

    // Stores all rotational shapes for this specific brick
    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an {@code ZBrick}.
     * This constructor defines and adds all possible rotational states
     * (as {@code int[][]} matrices) for the Z-Brick to the internal shape list.
     */
    public ZBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 7, 0, 0},
                {7, 7, 0, 0},
                {7, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * {@inheritDoc}
     * This implementation returns a deep copy of the shape list for the {@code ZBrick}
     * to prevent the original matrices from being modified.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
