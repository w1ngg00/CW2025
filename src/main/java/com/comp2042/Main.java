package com.comp2042.view;

import com.comp2042.GameConfig;
import com.comp2042.controller.GameController;
import com.comp2042.model.Difficulty;
import com.comp2042.model.GameSettings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Main entry point for the TetrisJFX application.
 * Scenes are sized to the screen and the game view is scaled to keep aspect ratio
 * so the UI fills the fullscreen even if FXML uses a fixed "logical" size.
 */
public class Main extends Application {

    private Stage primaryStage;
    private MediaPlayer clearRowSoundPlayer;
    private MediaPlayer speedUpSoundPlayer;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("TetrisJFX");

        // start fullscreen and prevent accidental ESC exit
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        loadSounds();
        showMainMenuScreen();

        primaryStage.show();
    }

    private void loadSounds() {
        try {
            URL clearResource = getClass().getResource("/sounds/clearRowSound.mp3");
            URL speedResource = getClass().getResource("/sounds/speedUpSound.mp3");

            if (clearResource != null) {
                Media clearMedia = new Media(clearResource.toExternalForm());
                clearRowSoundPlayer = new MediaPlayer(clearMedia);
            }
            if (speedResource != null) {
                Media speedMedia = new Media(speedResource.toExternalForm());
                speedUpSoundPlayer = new MediaPlayer(speedMedia);
            }
        } catch (Exception e) {
            System.err.println("Failed to load sounds: " + e.getMessage());
        }
    }

    /**
     * Show the main menu. Scene is created using the full visual bounds so
     * background ImageView / CSS that use percentages or scene size will fill.
     */
    public void showMainMenuScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("menu.fxml"));
            Parent root = fxmlLoader.load();

            MainMenuController controller = fxmlLoader.getController();
            controller.setMainApp(this);

            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());

            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true); // keep fullscreen
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show the game screen. Many FXML/game layouts are designed at a "logical" size
     * (GameConfig.WINDOW_WIDTH / WINDOW_HEIGHT). To keep UI sharp and centered on any
     * physical screen size we:
     *  - create a Scene sized to the screen visual bounds
     *  - wrap the loaded root in a Group, scale the Group to fit while preserving aspect ratio
     *  - center the scaled group inside the Scene using a StackPane (this keeps the game perfectly centered)
     */
    public void showGameScreen(Difficulty difficulty) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
            Parent root = fxmlLoader.load();

            GuiController guiController = fxmlLoader.getController();
            guiController.setMainApp(this);

            GameSettings settings = new GameSettings();

            // screen size
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            double screenW = bounds.getWidth();
            double screenH = bounds.getHeight();

            // logical game size (the size your FXML/game was designed for)
            double logicalW = GameConfig.WINDOW_WIDTH;
            double logicalH = GameConfig.WINDOW_HEIGHT;

            // ensure the loaded root uses the logical size so its bounds are predictable
            if (root instanceof Region) {
                Region r = (Region) root;
                r.setPrefSize(logicalW, logicalH);
                r.setMinSize(logicalW, logicalH);
                r.setMaxSize(logicalW, logicalH);
                r.resize(logicalW, logicalH);
            } else {
                root.resize(logicalW, logicalH);
            }

            // compute uniform scale to fit the screen while preserving aspect ratio
            double scaleX = screenW / logicalW;
            double scaleY = screenH / logicalH;
            double scale = Math.min(scaleX, scaleY);

            // wrap root in a Group so we can scale it (Group won't force layout size)
            Group scaledGroup = new Group(root);
            scaledGroup.setScaleX(scale);
            scaledGroup.setScaleY(scale);

            // place scaled group inside a StackPane so it is centered in the Scene
            StackPane container = new StackPane(scaledGroup);
            StackPane.setAlignment(scaledGroup, Pos.CENTER);
            container.setPrefSize(screenW, screenH);

            // create scene sized to the screen so background and other elements fill fullscreen
            Scene scene = new Scene(container, screenW, screenH);

            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true); // ensure fullscreen remains enabled

            // start game controller after scene set
            new GameController(guiController, difficulty, clearRowSoundPlayer, speedUpSoundPlayer, settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show settings screen. Keep it scaled similarly so it looks consistent on large screens.
     * Settings is shown as a centered dialog but scales to a sensible fraction of the screen.
     */
    public void showSettingScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("settingScreen.fxml"));
            Parent root = loader.load();
            SettingController controller = loader.getController();
            controller.setupVolumeControls(clearRowSoundPlayer, speedUpSoundPlayer);
            controller.setMainApp(this);

            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            double width = Math.max(420, bounds.getWidth() * 0.4);
            double height = Math.max(510, bounds.getHeight() * 0.6);

            Scene scene = new Scene(root, width, height);
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}