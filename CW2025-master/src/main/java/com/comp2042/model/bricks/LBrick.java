package com.comp2042.model.bricks;

import com.comp2042.model.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the specific shape matrices for the {@code LBrick}.
 * This class implements {@link Brick} and provides the specific rotational states
 * for the "L" shaped Tetromino.
 */
final class LBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an {@code LBrick}.
     * This constructor defines and adds all possible rotational states
     * (as {@code int[][]} matrices) for the L-Brick to the internal shape list.
     */
    public LBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 3, 3, 3},
                {0, 3, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 3, 3, 0},
                {0, 0, 3, 0},
                {0, 0, 3, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 0, 3, 0},
                {3, 3, 3, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 3, 0, 0},
                {0, 3, 0, 0},
                {0, 3, 3, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * {@inheritDoc}
     * This implementation returns a deep copy of the shape list for the {@code LBrick}
     * to prevent the original matrices from being modified.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
