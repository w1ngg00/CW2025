package com.comp2042.controller;

import com.comp2042.GameConfig;
import com.comp2042.model.*;
import com.comp2042.view.GuiController;
import com.comp2042.view.InputEventListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer;


/**
 * The main controller for the Tetris game, implementing the {@link InputEventListener} interface.
 * This class acts as the bridge between the {@link Board} (Model) and the {@link GuiController} (View).
 * It is responsible for managing the game's core logic, including the game loop (Timeline),
 * difficulty settings, sound events, and high score persistence.
 */
public class GameController implements InputEventListener {

    /** The logical game board model. */
    // Original constructor for SimpleBoard -> SimpleBoard(int width, int height) -> ERROR
    private final Board board = new SimpleBoard(GameConfig.BOARD_HEIGHT, GameConfig.BOARD_WIDTH);

    /** The main game loop timeline. */
    private Timeline timeLine;

    // Droppping obstacles in Extra Hard Mode
    private Timeline obstacleTimeline;

    /** Reference to the GUI controller for updating the view. */
    private final GuiController viewGuiController;
    // Get Difficulty
    private final Difficulty difficulty;

    // current game speed with lines
    private double currentGameSpeed;
    private int totalLinesForNextLevel;

    // MediaPlayer field
    private final MediaPlayer clearRowSoundPlayer;
    private final MediaPlayer speedUpSoundPlayer;

    // HighScoreManager reference
    private final HighScoreManager highScoreManager;

    /**
     * Creates a new GameController.
     *
     * @param c                 The {@link GuiController} (View) instance this controller will manage.
     * @param difficulty        The {@link Difficulty} level selected by the player.
     * @param clearRowPlayer    The shared {@link MediaPlayer} for the line clear sound.
     * @param speedUpPlayer     The shared {@link MediaPlayer} for the speed up sound.
     * @param settings          The {@link GameSettings} object containing the user's keybindings.
     */
    public GameController(GuiController c, Difficulty difficulty, MediaPlayer clearRowPlayer, MediaPlayer speedUpPlayer, GameSettings settings) {
        viewGuiController = c;
        this.difficulty = difficulty;

        // store the shared players
        this.clearRowSoundPlayer = clearRowPlayer;
        this.speedUpSoundPlayer = speedUpPlayer;

        viewGuiController.setEventListener(this, settings);

        viewGuiController.setupSoundPlayers(this.clearRowSoundPlayer, this.speedUpSoundPlayer);

        // initialize with difficulty
        initializeDifficulty(difficulty);

        // initialize HighScoreManager and pass to GUI
        this.highScoreManager = new HighScoreManager(this.difficulty);
        viewGuiController.updateHighScore(highScoreManager.getHighScore()); // Display high score to the GUI

        board.createNewBrick();
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindLines(board.getScore().totalLinesClearedProperty());

        gameLoop();
    }

    /**
     * Saves the current game score, updating the high score if necessary.
     * If the final score is higher than the saved high score, it updates the high score file
     * and requests the GUI to display a "New High Score!" notification.
     */
    public void saveGameScore() {
        int finalScore = board.getScore().getScore();
        boolean newHighScore = highScoreManager.saveHighScore(finalScore);

        if (newHighScore) {
            viewGuiController.updateHighScore(highScoreManager.getHighScore());
            // show the notification
            viewGuiController.showNotification("New High Score!", 0);
        }
    }

    /**
     * Sets game parameters based on the selected difficulty.
     * For HARD mode, it also instructs the board to set up obstacles.
     * @param difficulty The {@link Difficulty} selected difficulty.
     */
    private void initializeDifficulty(Difficulty difficulty) {
        this.currentGameSpeed = GameConfig.GAME_SPEED_MS;
        this.totalLinesForNextLevel = GameConfig.ROWS_PER_LEVEL;
        switch (difficulty) {
            case EASY:
                // speed = 400ms, no change
                break;
            case NORMAL:
                // speed up with clear lines
                //this.currentGameSpeed =
                break;
            case HARD:
                // Normal + obstacle
                board.initializeWithObstacles();
                break;
            case EXTRA:
                // HARD + Random obstacle generation
                board.initializeWithObstacles();
                // Timer for obstacles
                startObstacleTimer();
                break;
        }
    }

    /**
     * Initializes and starts the main game loop.
     * The game loop uses a {@link Timeline} that automatically triggers
     * a downward move every 400 milliseconds.
     */
    private void gameLoop() {
        if (timeLine != null) timeLine.stop();

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(this.currentGameSpeed),
                _ -> {
                    //onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                    DownData downData = onDownEvent(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                    // Update screen after a down event
                    viewGuiController.refreshBrick(downData.getViewData());
                }
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    /**
     * Initializes and starts the timer for spawning random obstacles in EXTRA HARD mode.
     * The timer calls {@link #dropRandomObstacle()} at a fixed interval.
     */
    private void startObstacleTimer() {
        obstacleTimeline = new Timeline(new KeyFrame(
                Duration.millis(GameConfig.EXTRA_TIMER),  // 15s interval
                _ -> dropRandomObstacle()
        ));
        obstacleTimeline.setCycleCount(Timeline.INDEFINITE);
        obstacleTimeline.play();
    }

    /**
     * (EXTRA HARD Mode) Called by the {@code obstacleTimeline} to periodically
     * drop a new obstacle onto the board.
     * This method has a chance (50%) to call the {@link Board#spawnAndHardDropObstacle()}
     * method and then forces the {@link GuiController} to refresh the background to show the new obstacle.
     */
    private void dropRandomObstacle() {
        if (Math.random() < 0.5) {
            // generate and drop by harddrop
            board.spawnAndHardDropObstacle();
            // Update view
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }
    }

    /**
     * Handles downward movement events for the current brick.
     * If the brick cannot move down further, it merges into the background,
     * rows are cleared, and a new brick is created.
     *
     * @param event the move event triggering this action.
     * @return the {@link DownData} including updated view and cleared row information.
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        // level-up flag
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {   // When row cleared
                board.getScore().add(clearRow.getScoreBonus());
                board.getScore().addToTotalLines(clearRow.getLinesRemoved());
                checkSpeedUp(); // check for speed up
            }
            if (board.createNewBrick()) {
                timeLine.stop();
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(GameConfig.SOFT_DROP_SCORE);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Handles Hard Drop (move down instantly).
     * Instantly drops the brick, calculates the score bonus, and finalizes the turn
     * (merge, clear rows, level-up check, new brick spawn).
     *
     * @return DownData containing the final board state and score.
     */
    @Override
    public DownData onHardDropEvent() {
        int moved_count = board.hardDrop();
        board.getScore().add(moved_count * GameConfig.HARD_DROP_SCORE_MULTIPLIER);
        // same logic as onDownEvent
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            board.getScore().addToTotalLines(clearRow.getLinesRemoved());
            checkSpeedUp(); // check for speed up
        }
        if (board.createNewBrick()) {
            timeLine.stop();
            viewGuiController.gameOver();
        }
        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Checks if the line clear count has reached the next level threshold
     * and increases the game speed if necessary, plays a sound.
     * shows a notification (via GuiController), and restarts the game loop.
     */
    private void checkSpeedUp() {
        // no speed change in EASY mode
        if (this.difficulty == Difficulty.EASY) { return; }
        int totalLines = board.getScore().getTotalLinesCleared();
        if (totalLines >= totalLinesForNextLevel) {
            // speed up -> 95% of original
            double newSpeed = this.currentGameSpeed * GameConfig.SPEED_INCREASE_FACTOR;
            // update threshold
            this.totalLinesForNextLevel += GameConfig.ROWS_PER_LEVEL;

            if (newSpeed != this.currentGameSpeed) {
                this.currentGameSpeed = newSpeed;
                viewGuiController.playSound(speedUpSoundPlayer);
                viewGuiController.showNotification("Speed UP!", GameConfig.SPEEDUP_NOTIFICATION_Y_OFFSET);
                gameLoop();
            }
        }
    }

    /**
     * Handles movement of the current brick to the left.
     *
     * @param event the move event.
     * @return the updated {@link ViewData}.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * Handles "Move Left Most" events.
     * @return The updated {@link ViewData} .
     */
    @Override
    public ViewData onLeftMostEvent() {
        board.moveBrickLeftMost();
        return board.getViewData();
    }

    /**
     * Handles movement of the current brick to the right.
     *
     * @param event the move event.
     * @return the updated {@link ViewData}.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * Handles "Move Right Most" events.
     * @return The updated {@link ViewData} for the view.
     */
    @Override
    public ViewData onRightMostEvent() {
        board.moveBrickRightMost();
        return board.getViewData();
    }

    /**
     * Handles rotation of the current brick.
     *
     * @param event the move event.
     * @return the updated {@link ViewData}.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    /**
     * Handles rotation right of the current brick.
     *
     * @return The updated {@link ViewData} for the view.
     */
    @Override
    public ViewData onRotateRightEvent() {
        board.rotateRightBrick();
        return board.getViewData();
    }

    /**
     * Handles "Hold" events (swap the current brick with the held brick).
     * @return The updated {@link ViewData} for the view.
     */
    @Override
    public ViewData onHoldEvent() {
        board.swapHoldBrick();
        return board.getViewData();
    }

    /**
     * Stops the main game loop.
     */
    @Override
    public void stopGame() {
        timeLine.stop();
        if (obstacleTimeline != null) {
            obstacleTimeline.stop();
        }
    }

    /**
     * Resumes the main game loop.
     */
    @Override
    public void resumeGame() {
        timeLine.play();
        if (obstacleTimeline != null) {
            obstacleTimeline.play();
        }
    }

    /**
     * Starts a new game by resetting the board and refreshing the view.
     */
    @Override
    public void createNewGame() {
        board.newGame();

        if (obstacleTimeline != null) {
            obstacleTimeline.stop();
            obstacleTimeline = null;
        }

        // check the difficulty
        if (this.difficulty == Difficulty.HARD) { board.initializeWithObstacles(); }
        if (this.difficulty == Difficulty.EXTRA) { board.initializeWithObstacles(); startObstacleTimer(); }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        timeLine.play();
    }
}
