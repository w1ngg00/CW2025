package com.comp2042.model;

/**
 * An immutable data object that bundles the results of a downward move event (like a soft drop or hard drop).
 * This class is used to pass the result of a completed turn—which may include
 * information about cleared rows ({@link ClearRow}) and the new state of the
 * falling/ghost bricks ({@link ViewData})—from the controller to the view.
 */
public final class DownData {
    /** The result of any row-clearing operation that occurred. */
    private final ClearRow clearRow;
    /** The updated view data (new brick position, next brick) for the view to render. */
    private final ViewData viewData;

    /**
     * Constructs a new {@code DownData} object.
     *
     * @param clearRow The result of the row clearing operation. This can be {@code null}
     * if no rows were cleared during this move.
     * @param viewData The updated {@link ViewData} to be rendered by the GUI.
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Gets the result of the row clearing operation.
     *
     * @return The {@link ClearRow} object, or {@code null} if no rows were cleared.
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Gets the updated view data for the current game state.
     *
     * @return The {@link ViewData} to be rendered.
     */
    public ViewData getViewData() {
        return viewData;
    }
}
