package com.comp2042.model;

/**
 * An immutable data object that holds the result of a potential rotation attempt.
 * This class is typically returned by a {@link BrickRotator} to pass both the
 * 2D matrix ({@code shape}) of the *next* rotational state and the
 * {@code position} (index) that state corresponds to.
 */
public final class NextShapeInfo {

    /** The 2D matrix of the next rotational shape. */
    private final int[][] shape;

    /** The index (position) of this shape in the brick's rotation list. */
    private final int position;

    /**
     * Constructs a new {@code NextShapeInfo} object.
     *
     * @param shape    The 2D {@code int[][]} matrix of the new shape.
     * @param position The index of this new shape (e.g., 0, 1, 2, or 3).
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Gets a deep copy of the shape matrix.
     *
     * @return A new 2D array instance representing the shape.
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Gets the index (position) of this shape in the rotation list.
     *
     * @return The rotation index.
     */
    public int getPosition() {
        return position;
    }
}
