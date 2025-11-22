package com.comp2042.view;

import com.comp2042.GameConfig;
import com.comp2042.controller.InputHandler;
import com.comp2042.controller.MoveEvent;
import com.comp2042.model.DownData;
import com.comp2042.model.GameSettings;
import com.comp2042.model.ViewData;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.List;

/**
 * The {@code GuiController} class handles all GUI interactions
 * It is responsible for rendering bricks, detecting user input,
 * refreshing the game view, and managing visual effects and state transitions
 * such as pause and game over.
 * This controller is associated with {@code game_layout.fxml} and interacts with
 * {@link com.comp2042.controller.GameController} to send and receive game events.
 */
public class GuiController implements Initializable {

    /** The main grid pane that holds the static, merged bricks (the game board). */
    @FXML
    private GridPane gamePanel;
    /** The Group container for displaying notifications (e.g., "+100", "Speed UP!"). */
    @FXML
    private Group groupNotification;
    /** The grid pane that moves around to display the currently falling brick. */
    @FXML
    private GridPane brickPanel;

    // Group that wraps the gamePanel and overlays (must match fx:id in FXML)
    @FXML
    private Group boardGroup;

    /** The label used to display the current score. */
    @FXML
    private Label scoreLabel;
    /** The label used to display the persistent high score. */
    @FXML
    private Label highScoreLabel;

    @FXML private Label linesLabel;

    /** The 4x4 grid pane for the "next" brick. */
    @FXML
    private GridPane nextBrickPanel;
    @FXML
    private GridPane nextBrickPanel2;
    @FXML
    private GridPane nextBrickPanel3;
    @FXML
    private GridPane nextBrickPanel4;

    //** The button for pausing/resuming the game. */
    @FXML
    private Button pauseButton;
    /** The button for restarting the game. */
    @FXML
    private Button restartButton;

    /** The custom panel displayed on game over. */
    @FXML
    private GameOverPanel gameOverPanel;
    /** The 4x4 grid pane for displaying the ghost piece (drop forecast). */
    @FXML
    private GridPane ghostBrickPanel;
    /** The 4x4 grid pane for displaying the held brick. */
    @FXML
    private GridPane holdBrickPanel;


    /** 2D array holding the {@link Rectangle} objects for the main game board (displayMatrix). */
    private Rectangle[][] displayMatrix;

    /** 2D array holding the {@link Rectangle} objects for the "next" brick panel. */
    private Rectangle[][] nextBrickRectangles;
    private Rectangle[][] nextBrickRectangles2;
    private Rectangle[][] nextBrickRectangles3;
    private Rectangle[][] nextBrickRectangles4;

    /** 2D array holding the {@link Rectangle} objects for the ghost piece panel. */
    private Rectangle[][] ghostRectangles;

    /** 2D array holding the {@link Rectangle} objects for the hold piece panel. */
    private Rectangle[][] holdBrickRectangle;

    /** A reference to the Controller (implements {@link InputEventListener}). */
    private InputEventListener eventListener;

    /** 2D array holding the {@link Rectangle} objects for the currently falling brick panel. */
    private Rectangle[][] rectangles;

    /** A reference to the Main application class, used for switching scenes. */
    private Main mainApp;

    /** Cached image for buttons. */
    private Image pauseImg;
    private Image resumeImg;
    private Image restartImg;

    /** Cached {@link ImageView} for icons. */
    private ImageView pauseIconView;
    private ImageView resumeIconView;

    // MediaPlayer
    /** Shared {@link MediaPlayer} for sounds. */
    private MediaPlayer clearRowSoundPlayer;
    private MediaPlayer speedUpSoundPlayer;

    /** JavaFX property tracking the pause state. */
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    /** JavaFX property tracking the game over state. */
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private GameSettings settings;

    // pixel size of the visible board (calculated in initGameView / adjusted)
    private double boardPixelWidth = 0;
    private double boardPixelHeight = 0;

    // current cell size (can be adjusted when scene size changes)
    private double currentBrickSize = GameConfig.BRICK_SIZE;

    /**
     * Construct an instance of GuiController
     * This constructor is automatically invoked by the JavaFX runtime
     * ({@code FXMLLoader}) when an FXML file is loaded.
     * Injection of values into {@code @FXML} fields and initial setup
     * are typically performed within the {@code initialize()} method.
     */
    public GuiController() {}

    /**
     * Initializes the GUI controller.
     * This method is called automatically by JavaFX after the FXML file is loaded.
     *
     * @param location  the location used to resolve relative paths for the root object.
     * @param resources the resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("fonts/digital.ttf").toExternalForm(), 38);

        // Load assets
        try {
            pauseImg = new Image(getClass().getResourceAsStream("/icons/pauseButton.png"));
            resumeImg = new Image(getClass().getResourceAsStream("/icons/resumeButton.png"));
            restartImg = new Image(getClass().getResourceAsStream("/icons/restartButton.png"));
            pauseIconView = new ImageView(pauseImg);
            resumeIconView = new ImageView(resumeImg);
            ImageView restartIconView = new ImageView(restartImg);
            pauseIconView.setFitWidth(25);
            resumeIconView.setFitWidth(25);
            restartIconView.setFitWidth(25);
            pauseIconView.setPreserveRatio(true);
            resumeIconView.setPreserveRatio(true);
            restartIconView.setPreserveRatio(true);
            pauseButton.setGraphic(pauseIconView);
            restartButton.setGraphic(restartIconView);
        } catch (Exception e) {
            System.err.println("Failed to load icon img: " + e.getMessage());
            pauseButton.setText("Pause");
            restartButton.setText("Restart");
        }

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        // Setup the game over panel's "Main Menu" button action
        gameOverPanel.setMainMenu(() -> mainApp.showMainMenuScreen());
        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);

        // when scene becomes available, ensure we adjust sizes and center on resize
        gamePanel.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // adjust immediately and on resize
                newScene.widthProperty().addListener((o, oldW, newW) -> adjustBoardToScene(newScene));
                newScene.heightProperty().addListener((o, oldH, newH) -> adjustBoardToScene(newScene));
                Platform.runLater(() -> adjustBoardToScene(newScene));
            }
        });
    }

    /**
     * Receives the shared {@link MediaPlayer} instances.
     *
     * @param clearRowSoundPlayer Line Clear Sound
     * @param speedUpSoundPlayer Speed Up Sound
     */
    public void setupSoundPlayers(MediaPlayer clearRowSoundPlayer, MediaPlayer speedUpSoundPlayer) {
        this.clearRowSoundPlayer = clearRowSoundPlayer;
        this.speedUpSoundPlayer = speedUpSoundPlayer;
    }

    /**
     * play the given {@link MediaPlayer} instance safely (handles null).
     * Stops the player first to ensure it plays from the beginning.
     * @param player The {@link MediaPlayer} to play.
     */
    public void playSound(MediaPlayer player) {
        // Stop the previous media
        player.stop();
        player.play();
    }

    /**
     * Set the reference to the main application.
     *
     * @param mainApp Instance of Main Application
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Initializes the game board and brick display on the GUI.
     *
     * @param boardMatrix the logical board data matrix.
     * @param brick       the view data of the current falling brick.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(currentBrickSize, currentBrickSize);
                setRectangleData(boardMatrix[i][j], rectangle);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        // calculate visible board pixel size (used for centering)
        int cols = boardMatrix[0].length;
        int visibleRows = boardMatrix.length - 2; // first two rows usually hidden in many Tetris impls
        boardPixelWidth = cols * currentBrickSize;
        boardPixelHeight = visibleRows * currentBrickSize;

        // initialize the ghost panel
        ghostRectangles = initializeNextBrickPanel(ghostBrickPanel, currentBrickSize);
        // set color for ghost piece
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ghostRectangles[i][j].setFill(Color.TRANSPARENT);
            }
        }

        // nextBrick panel initialization (4x4)
        nextBrickRectangles = initializeNextBrickPanel(nextBrickPanel, GameConfig.NEXT_BRICK_SIZE_LARGE);
        nextBrickRectangles2 = initializeNextBrickPanel(nextBrickPanel2, GameConfig.NEXT_BRICK_SIZE_SMALL);
        nextBrickRectangles3 = initializeNextBrickPanel(nextBrickPanel3, GameConfig.NEXT_BRICK_SIZE_SMALL);
        nextBrickRectangles4 = initializeNextBrickPanel(nextBrickPanel4, GameConfig.NEXT_BRICK_SIZE_SMALL);
        holdBrickRectangle = initializeNextBrickPanel(holdBrickPanel, GameConfig.NEXT_BRICK_SIZE_LARGE);

        // Initialize the falling brick panel
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(currentBrickSize, currentBrickSize);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        // initial placement for brickPanel (will be updated as sizes change)
        updateMovingPanelsPosition(brick);

        // ensure center/resize is applied now (in case scene already present)
        Scene s = gamePanel.getScene();
        if (s != null) Platform.runLater(() -> adjustBoardToScene(s));
    }

    /**
     * When scene size changes, recompute an appropriate cell size and resize all rectangles.
     * Uses heuristics to leave space for hold/next columns and score UI.
     */
    private void adjustBoardToScene(Scene scene) {
        if (displayMatrix == null) return;

        int cols = displayMatrix[0].length;
        int visibleRows = displayMatrix.length - 2;

        // Heuristic available area for the center board: reserve sides for Hold/Next/spacing and top/bottom UI
        double availW = Math.max(200, scene.getWidth() * 0.46); // center area width (tweak fraction if needed)
        double availH = Math.max(200, scene.getHeight() * 0.72); // center area height

        double newCell = Math.floor(Math.min((availW - 4) / cols, (availH - 4) / visibleRows));
        if (newCell < 4) newCell = 4;

        // if same size no-op
        if (Math.abs(newCell - currentBrickSize) < 0.1) {
            centerBoard(); // still recenter because scene changed
            return;
        }

        currentBrickSize = newCell;

        // update static display matrix sizes
        for (int i = 2; i < displayMatrix.length; i++) {
            for (int j = 0; j < displayMatrix[i].length; j++) {
                Rectangle r = displayMatrix[i][j];
                if (r != null) {
                    r.setWidth(currentBrickSize);
                    r.setHeight(currentBrickSize);
                }
            }
        }

        // update falling brick rectangles
        if (rectangles != null) {
            for (int i = 0; i < rectangles.length; i++) {
                for (int j = 0; j < rectangles[i].length; j++) {
                    Rectangle r = rectangles[i][j];
                    if (r != null) { r.setWidth(currentBrickSize); r.setHeight(currentBrickSize); }
                }
            }
        }

        // update ghost & preview panels
        if (ghostRectangles != null) {
            for (int i = 0; i < ghostRectangles.length; i++)
                for (int j = 0; j < ghostRectangles[i].length; j++) {
                    Rectangle r = ghostRectangles[i][j];
                    if (r != null) { r.setWidth(currentBrickSize); r.setHeight(currentBrickSize); }
                }
        }
        if (nextBrickRectangles != null) updateNextRectanglesSize(nextBrickRectangles, GameConfig.NEXT_BRICK_SIZE_LARGE);
        if (nextBrickRectangles2 != null) updateNextRectanglesSize(nextBrickRectangles2, GameConfig.NEXT_BRICK_SIZE_SMALL);
        if (nextBrickRectangles3 != null) updateNextRectanglesSize(nextBrickRectangles3, GameConfig.NEXT_BRICK_SIZE_SMALL);
        if (nextBrickRectangles4 != null) updateNextRectanglesSize(nextBrickRectangles4, GameConfig.NEXT_BRICK_SIZE_SMALL);
        if (holdBrickRectangle != null) updateNextRectanglesSize(holdBrickRectangle, GameConfig.NEXT_BRICK_SIZE_LARGE);

        // update computed board pixel sizes
        boardPixelWidth = cols * currentBrickSize;
        boardPixelHeight = visibleRows * currentBrickSize;

        // recenter & update moving panel offsets
        centerBoard();
    }

    private void updateNextRectanglesSize(Rectangle[][] rects, double baseSize) {
        // keep previews their own fixed sizes (no need to scale them with board) but ensure set
        for (int i = 0; i < rects.length; i++)
            for (int j = 0; j < rects[i].length; j++) {
                Rectangle r = rects[i][j];
                if (r != null) { r.setWidth(baseSize); r.setHeight(baseSize); }
            }
    }

    /**
     * Updates the position of moving panels (brickPanel / ghost / notifications) using current sizes.
     */
    private void updateMovingPanelsPosition(ViewData brick) {
        double scaleFactor = currentBrickSize / GameConfig.BRICK_SIZE;
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * currentBrickSize);
        brickPanel.setLayoutY(GameConfig.BRICK_PANEL_Y_OFFSET * scaleFactor + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * currentBrickSize);
    }

    /**
     * Renders all "next" brick preview panels.
     * @param nextBricks A {@link List} of {@code int[][]} shapes from the queue.
     */
    private void displayNextBricks(List<int[][]> nextBricks) {
        // Panel 1
        displayNextBrick(nextBricks.get(0), nextBrickRectangles);
        // Panel 2
        displayNextBrick(nextBricks.get(1), nextBrickRectangles2);
        // Panel 3
        displayNextBrick(nextBricks.get(2), nextBrickRectangles3);
        // Panel 4
        displayNextBrick(nextBricks.get(3), nextBrickRectangles4);

    }

    /**
     * Helper method to render a single brick shape onto a specific preview panel.
     * @param nextBrick The {@code int[][]} shape to draw.
     * @param rects The 4x4 {@link Rectangle} array (e.g., {@code nextBrickRectangles}) to draw on.
     */
    private void displayNextBrick(int[][] nextBrick, Rectangle[][] rects) {
        // need to initialize the panel to not overwrite
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rects[i][j].setFill(Color.TRANSPARENT);
            }
        }
        // set the next brick
        for (int i = 0; i < nextBrick.length; i++) {
            for (int j = 0; j < nextBrick[i].length; j++) {
                if (nextBrick[i][j] != 0) setRectangleData(nextBrick[i][j], rects[i][j]);
            }
        }
    }

    /**
     * Renders the "hold" brick preview panel.
     * @param holdingBrick The {@code int[][]} shape of the held brick, or {@code null}.
     */
    private void displayHoldBrick(int[][] holdingBrick) {
        // Clear the panel
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                holdBrickRectangle[i][j].setFill(Color.TRANSPARENT);
            }
        }

        // Set the new held brick shape
        if (holdingBrick != null) {
            for (int i = 0; i < holdingBrick.length; i++) {
                for (int j = 0; j < holdingBrick[i].length; j++) {
                    if (holdingBrick[i][j] != 0) {
                        setRectangleData(holdingBrick[i][j], holdBrickRectangle[i][j]);
                    }
                }
            }
        }
    }

    /**
     * A factory helper method to initialize a 4x4 grid of {@link Rectangle} objects.
     *
     * @param panel The {@link GridPane} to add the rectangles to.
     * @param size  The pixel size (width/height) of each rectangle.
     * @return The 2D array of created {@link Rectangle} objects.
     */
    private Rectangle[][] initializeNextBrickPanel(GridPane panel, double size) {
        Rectangle[][] rectangles = new Rectangle[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rectangle = new Rectangle(size, size);
                rectangles[i][j] = rectangle;
                panel.add(rectangle, j, i);
            }
        }
        return rectangles;
    }

    /**
     * Converts an integer color code into a {@link Paint} object.
     *
     * @param i color index (0–7).
     * @return the corresponding {@link Paint} color.
     */
    private Paint getFillColor(int i) {
        return switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            case 8 -> Color.GRAY;   // For obstacles
            default -> Color.WHITE;
        };
    }

    /**
     * Updates the position and color of the currently falling brick.
     *
     * @param brick the updated brick view data.
     */
    public void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            double scaleFactor = currentBrickSize / GameConfig.BRICK_SIZE;

            // refresh the dropping brick (use currentBrickSize)
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * currentBrickSize);
            brickPanel.setLayoutY(GameConfig.BRICK_PANEL_Y_OFFSET * scaleFactor + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * currentBrickSize);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }

            // update ghost piece
            int[][] ghostBrick = brick.getBrickData();

            // X coordinate is the same as dropping brick
            ghostBrickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * currentBrickSize);
            // Y coordinate -> scaled offset
            ghostBrickPanel.setLayoutY(GameConfig.BRICK_PANEL_Y_OFFSET * scaleFactor + gamePanel.getLayoutY() + brick.getGhostYPosition() * brickPanel.getHgap() + brick.getGhostYPosition() * currentBrickSize);

            // Update the ghost brick shape
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    // if there is a block in the 4x4 panel -> pass the brick color
                    if (i < ghostBrick.length && j < ghostBrick[i].length && ghostBrick[i][j] != 0) setRectangleData(ghostBrick[i][j], ghostRectangles[i][j]);
                    // set as transparent
                    else { setRectangleData(0, ghostRectangles[i][j]); }
                }
            }

            // call holding brick and next brick display method
            displayHoldBrick(brick.getHoldBrickData());
            displayNextBricks(brick.getNextBrickData());
        }
    }

    /**
     * Refreshes the static background (merged bricks) of the game.
     *
     * @param board the matrix representing the fixed blocks on the board.
     */
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    /**
     * Sets the color and rounded corner style for a rectangle.
     *
     * @param color     color code of the brick.
     * @param rectangle the target rectangle to modify.
     */
    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    /**
     * Moves the current brick down one step and updates the view.
     * If a row is cleared, a floating score notification is shown.
     *
     * @param event the downward move event.
     */
    public void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                showNotification("+" + downData.getClearRow().getScoreBonus(), 0);
                playSound(clearRowSoundPlayer);
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    /**
     * Handles the result of a "Hard Drop" (user pressing Space).
     */
    public void handleHardDrop() {
        DownData downData = eventListener.onHardDropEvent();
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            showNotification("+" + downData.getClearRow().getScoreBonus(), 0);
            playSound(clearRowSoundPlayer);
        }
        refreshBrick(downData.getViewData());
        gamePanel.requestFocus();
    }

    /**
     * To display a generalized notification panel
     * @param text text to display on the notification
     * @param yOffset offset for y (0 = bonus score, 30 = speed up)
     */
    public void showNotification(String text, double yOffset) {
        NotificationPanel speedUpPanel = new NotificationPanel(text);
        // move Y coordinates
        speedUpPanel.setLayoutY(yOffset);
        groupNotification.getChildren().add(speedUpPanel);
        speedUpPanel.showScore(groupNotification.getChildren());
    }

    /**
     * Sets the input event listener for the GUI controller.
     *
     * @param eventListener the listener implementing {@link InputEventListener}.
     * @param settings The {@link GameSettings} object containing the user's keybindings.
     */
    public void setEventListener(InputEventListener eventListener, GameSettings settings) {
        this.eventListener = eventListener;
        this.settings = settings;
        InputHandler inputHandler = new InputHandler(this, this.eventListener, settings);
        gamePanel.setOnKeyPressed(inputHandler);
    }

    /**
     * Binds the displayed score to the game’s score property.
     *
     * @param integerProperty the score property from the Score object (Model).
     */
    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString());
    }

    /**
     * Binds the displayed total lines cleared to the game's lines property.
     * @param integerProperty the total lines cleared
     */
    public void bindLines(IntegerProperty integerProperty) {
        linesLabel.textProperty().bind(integerProperty.asString());
    }

    /**
     * Updates the High Score label with new high score.
     * @param score New high score.
     */
    public void updateHighScore(int score) {
        highScoreLabel.setText(String.valueOf(score));
    }

    /**
     * Displays the game over panel and stops the game.
     */
    public void gameOver() {
        eventListener.saveGameScore();
        gameOverPanel.showWithAnimation();
        isGameOver.setValue(Boolean.TRUE);
    }

    /**
     * Starts a new game and resets the game state.
     *
     * @param actionEvent the event triggering the new game.
     */
    public void newGame(ActionEvent actionEvent) {
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        pauseButton.setGraphic(pauseIconView);
    }

    /**
     * Toggles between pause and resume states.
     */
    public void pauseGame(ActionEvent actionEvent) {
        if (isPause.getValue() == Boolean.FALSE) {
            eventListener.stopGame();
            isPause.setValue(Boolean.TRUE);
            pauseButton.setGraphic(resumeIconView);
        } else {
            eventListener.resumeGame();
            isPause.setValue(Boolean.FALSE);
            pauseButton.setGraphic(pauseIconView);
        }
        gamePanel.requestFocus();
    }

    public boolean isPause() { return isPause.get(); }
    public boolean isGameOver() { return isGameOver.get(); }
    public InputEventListener getEventListener() { return this.eventListener; }

    @FXML
    private void goBackToMenu(ActionEvent actionEvent) {
        eventListener.stopGame();
        isPause.setValue(Boolean.TRUE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to go back to menu?");
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            mainApp.showMainMenuScreen();
        } else {
            eventListener.resumeGame();
            isPause.setValue(Boolean.FALSE);
            gamePanel.requestFocus();
        }
    }

    @FXML
    private void onShowKeybindings() {
        eventListener.stopGame();
        isPause.setValue(Boolean.TRUE);
        String keybindings =
                "Move Left:\t\t" + settings.getKeyCode("MOVE_LEFT").name() + "\n" +
                "Move Right:\t\t" + settings.getKeyCode("MOVE_RIGHT").name() + "\n" +
                "Rotate Left:\t\t" + settings.getKeyCode("ROTATE_LEFT").name() + "\n" +
                "Rotate Right:\t\t" + settings.getKeyCode("ROTATE_RIGHT").name() + "\n" +
                "Soft Drop:\t\t" + settings.getKeyCode("SOFT_DROP").name() + "\n" +
                "Hard Drop:\t\t" + settings.getKeyCode("HARD_DROP").name() + "\n" +
                "Hold:\t\t\t" + settings.getKeyCode("HOLD").name() + "\n" +
                "Move Left Most: \t" + settings.getKeyCode("MOVE_LEFT_MOST").name() + "\n" +
                "Move Right Most:\t" + settings.getKeyCode("MOVE_RIGHT_MOST").name();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Keybindings");
        alert.setHeaderText("Current Keybindings");
        alert.setContentText(keybindings);
        alert.showAndWait();

        isPause.setValue(Boolean.FALSE);
        eventListener.resumeGame();
        gamePanel.requestFocus();
    }

    /**
     * Center the visible game board within the current scene.
     */
    private void centerBoard() {
        Scene s = gamePanel.getScene();
        if (s == null) return;
        if (boardPixelWidth <= 0 || boardPixelHeight <= 0) return;

        double sceneW = s.getWidth();
        double sceneH = s.getHeight();
        double x = (sceneW - boardPixelWidth) / 2.0;
        double y = (sceneH - boardPixelHeight) / 2.0;

        if (Double.isNaN(x) || Double.isNaN(y)) return;
        gamePanel.setLayoutX(x < 0 ? 0 : x);
        gamePanel.setLayoutY(y < 0 ? 0 : y);

        // align moving overlays with the centered board
        brickPanel.setLayoutX(gamePanel.getLayoutX());
        brickPanel.setLayoutY(gamePanel.getLayoutY());
        ghostBrickPanel.setLayoutX(gamePanel.getLayoutX());
        ghostBrickPanel.setLayoutY(gamePanel.getLayoutY());
        groupNotification.setLayoutX(gamePanel.getLayoutX());
        groupNotification.setLayoutY(gamePanel.getLayoutY());
    }

}
