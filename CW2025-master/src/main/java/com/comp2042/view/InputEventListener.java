package com.comp2042.view;

import com.comp2042.controller.MoveEvent;
import com.comp2042.model.DownData;
import com.comp2042.model.ViewData;

/**
 * Interface defining the contract for events passed from the View ({@link GuiController})
 * or {@link com.comp2042.controller.InputHandler} to the Controller ({@link com.comp2042.controller.GameController})
 * for processing game logic.
 * This interface allows the View to remain decoupled from the specific implementation
 * of the game logic, adhering to the Dependency Inversion Principle.
 */
public interface InputEventListener {

    /**
     * Handles Normal Drop (move down one step).
     * @param event The move event.
     * @return DownData containing the result of the move.
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles movement of the current brick to the left.
     * @param event the move event.
     * @return the updated {@link ViewData}.
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles moving the current brick instantly to the far left.
     * @return The updated {@link ViewData}.
     */
    ViewData onLeftMostEvent();

    /**
     * Handles movement of the current brick to the right.
     * @param event the move event.
     * @return the updated {@link ViewData}.
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles moving the current brick instantly to the far right.
     * @return The updated {@link ViewData}.
     */
    ViewData onRightMostEvent();

    /**
     * Handles rotation of the current brick.
     *
     * @param event the move event.
     * @return the updated {@link ViewData}.
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Handles rotation of the current brick 90 degrees right.
     * @return The updated {@link ViewData}.
     */
    ViewData onRotateRightEvent();

    /**
     * Handles Hard Drop (move down instantly).
     * @return DownData containing the final board state and score.
     */
    DownData onHardDropEvent();

    /**
     * Handles a "Hold" action (swaps the current brick with the held brick).
     *
     * @return The updated {@link ViewData} reflecting the board state after the swap.
     */
    ViewData onHoldEvent();

    /**
     * Stops the main game loop.
     */
    void stopGame();

    /**
     * Resumes the main game loop.
     */
    void resumeGame();

    /**
     * Starts a new game by resetting the board and refreshing the view.
     */
    void createNewGame();

    /**
     * Saves the current game score to the high score file.
     */
    void saveGameScore();
}
