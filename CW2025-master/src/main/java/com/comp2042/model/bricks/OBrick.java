package com.comp2042.model.bricks;

import com.comp2042.model.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the specific shape matrices for the {@code OBrick}.
 * This class implements {@link Brick} and provides the specific rotational states
 * for the "O" shaped Tetromino.
 */
final class OBrick implements Brick {

    // Stores all rotational shapes for this specific brick
    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an {@code OBrick}.
     * This constructor defines and adds all possible rotational states
     * (as {@code int[][]} matrices) for the O-Brick to the internal shape list.
     */
    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * {@inheritDoc}
     * This implementation returns a deep copy of the shape list for the {@code OBrick}
     * to prevent the original matrices from being modified.
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}
