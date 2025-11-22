package com.comp2042.controller;

import com.comp2042.model.GameSettings;
import com.comp2042.model.ViewData;
import com.comp2042.view.GuiController;
import com.comp2042.view.InputEventListener;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * Handles all keyboard input for the game.
 * This class implements EventHandler and interprets KeyEvents,
 * delegating actions to the GuiController and InputEventListener.
 * This separation adheres to the Single Responsibility Principle.
 */
public class InputHandler implements EventHandler<KeyEvent> {
    // Reference to the GuiController
    private final GuiController guiController;
    private final InputEventListener gameController;
    private final GameSettings settings;

    private KeyCode K_MOVE_LEFT;
    private KeyCode K_MOVE_RIGHT;
    private KeyCode K_ROTATE_LEFT;
    private KeyCode K_ROTATE_RIGHT;
    private KeyCode K_SOFT_DROP;
    private KeyCode K_HARD_DROP;
    private KeyCode K_MOVE_LEFT_MOST;
    private KeyCode K_MOVE_RIGHT_MOST;
    private KeyCode K_HOLD;

    // TimeStamp for detecting double space
    private long lastSpacePressTime = 0;
    // Double-tap detection time (ms)
    private static final long DOUBLE_TAP_THRESHOLD = 300;

    /**
     * Creates a new InputHandler that bridges the View and the Controller.
     *
     * @param controller        The {@link GuiController} (View) used for checking game state and refreshing the brick display.
     * @param gameController    The {@link InputEventListener} (Controller) to which game logic commands are sent.
     * @param settings          The {@link GameSettings} object containing the user's keybindings.
     */
    public InputHandler(GuiController controller, InputEventListener gameController, GameSettings settings) {
        this.guiController = controller;
        this.gameController = gameController;
        this.settings = settings;

        loadKeybindings();
    }


    private void loadKeybindings() {
        K_MOVE_LEFT = settings.getKeyCode("MOVE_LEFT");
        K_MOVE_RIGHT = settings.getKeyCode("MOVE_RIGHT");
        K_ROTATE_LEFT = settings.getKeyCode("ROTATE_LEFT");
        K_ROTATE_RIGHT = settings.getKeyCode("ROTATE_RIGHT");
        K_SOFT_DROP = settings.getKeyCode("SOFT_DROP");
        K_HARD_DROP = settings.getKeyCode("HARD_DROP");
        K_MOVE_LEFT_MOST = settings.getKeyCode("MOVE_LEFT_MOST");
        K_MOVE_RIGHT_MOST = settings.getKeyCode("MOVE_RIGHT_MOST");
        K_HOLD = settings.getKeyCode("HOLD");
    }

    /**
     * Handles the keyboard input (KeyPressed event).
     * Interprets the key code and delegates the appropriate action.
     * Implements a double-tap detection for the SPACE key to differentiate
     * between Soft Drop (single tap) and Hard Drop (double tap).
     *
     * @param keyEvent The KeyEvent triggered by the user.
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        // Moved to start to handle N key to restart the game when game over
        if (keyEvent.getCode() == KeyCode.N) {
            guiController.newGame(null);
        }

        if (guiController.isPause() == Boolean.FALSE && guiController.isGameOver() == Boolean.FALSE) {
            KeyCode keyCode = keyEvent.getCode();

            // SLASH(/) : LEFT MOST
            if (keyCode == K_MOVE_LEFT_MOST) {
                ViewData data = gameController.onLeftMostEvent();
                guiController.refreshBrick(data);
                keyEvent.consume();
            }
            // F -> LEFT
            if (keyCode == K_MOVE_LEFT) {
                guiController.refreshBrick(guiController.getEventListener().onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                keyEvent.consume();
            }
            // J -> RIGHT
            if (keyCode == K_MOVE_RIGHT) {
                guiController.refreshBrick(guiController.getEventListener().onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                keyEvent.consume();
            }
            // SHIFT -> RIGHT MOST
            if (keyCode == K_MOVE_RIGHT_MOST) {
                ViewData data = gameController.onRightMostEvent();
                guiController.refreshBrick(data);
                keyEvent.consume();
            }

            // --- ROTATION ---
            // S -> ROTATE LEFT
            if (keyCode == K_ROTATE_LEFT) {
                guiController.refreshBrick(guiController.getEventListener().onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                keyEvent.consume();
            }
            // L -> ROTATE RIGHT
            if (keyCode == K_ROTATE_RIGHT) {
                guiController.refreshBrick(guiController.getEventListener().onRotateRightEvent());
                keyEvent.consume();
            }

            // Holding brick
            if (keyCode == K_HOLD) {
                guiController.refreshBrick(guiController.getEventListener().onHoldEvent());
                keyEvent.consume();
            }

            if (K_SOFT_DROP == K_HARD_DROP) {
                if (keyCode == K_SOFT_DROP) {
                    long now = System.currentTimeMillis();

                    if (now - lastSpacePressTime < DOUBLE_TAP_THRESHOLD) {
                        // DOUBLE SPACE -> HARD DROP
                        guiController.handleHardDrop();
                        keyEvent.consume();

                        // Reset timer
                        lastSpacePressTime = 0;
                    } else {
                        // DOWN
                        guiController.moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();

                        lastSpacePressTime = now;   // record the pressed time
                    }
                }
            } else {
                if (keyCode == K_SOFT_DROP) {
                    guiController.moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                    keyEvent.consume();
                } else if (keyCode == K_HARD_DROP) {
                    guiController.handleHardDrop();
                    keyEvent.consume();
                }
            }
        }
    }
}