package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * A final class that manages the game's score and line count.
 * This class uses JavaFX {@link IntegerProperty} to allow the GUI to bind
 * to the score and update automatically. It also tracks the total number of
 * lines cleared, which is used for calculating level progression.
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    // Calculate the total lines cleared
    private final IntegerProperty totalLinesCleared = new SimpleIntegerProperty(0);

    /**
     * Constructs a new Score tracker.
     * Initializes the score and total lines cleared to zero.
     */
    public Score() {}

    /**
     * Gets the JavaFX property for the score.
     * This allows the GUI (e.g., a {@link javafx.scene.control.Label}) to bind directly to the score value.
     *
     * @return The {@link IntegerProperty} for the score.
     */
    public IntegerProperty scoreProperty() { return score; }

    /**
     * Gets the JavaFX property for the total lines cleared.
     * This allows the GUI to bind directly to the lines value.
     *
     * @return The {@link IntegerProperty} for the total lines cleared.
     */
    public IntegerProperty totalLinesClearedProperty() { return totalLinesCleared; }

    /**
     * Gets the current total number of lines cleared.
     *
     * @return The cumulative number of lines cleared.
     */
    // Getter for totalLinesCleared
    public int getTotalLinesCleared() { return totalLinesCleared.get(); }

    /**
     * Adds the newly cleared lines to the cumulative total.
     *
     * @param lineCleared The number of lines cleared in the last turn.
     */
    // Calculate the total lines cleared
    public void addToTotalLines(int lineCleared) { this.totalLinesCleared.setValue(this.totalLinesCleared.get() + lineCleared); }

    /**
     * Adds a specified value to the current score.
     *
     * @param i The score value to add (e.g., bonus points).
     */
    public void add(int i){ score.setValue(score.getValue() + i); }

    /**
     * Gets the current score as a primitive integer.
     *
     * @return The current score value.
     */
    public int getScore() { return score.get(); }

    /**
     * Resets the score and total lines cleared to zero.
     * This is typically called at the start of a new game.
     */
    public void reset() {
        score.setValue(0);
        // BUG FIX -> speed up logic
        totalLinesCleared.setValue(0);
    }
}
