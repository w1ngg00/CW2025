package com.comp2042.view;

import com.comp2042.GameConfig;
import com.comp2042.controller.GameController;
import com.comp2042.model.Difficulty;
import com.comp2042.model.GameSettings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * The main entry point for the TetrisJFX application.
 * This class extends {@link Application} and is responsible for:
 * 1. Storing the primary {@link Stage} (the main window).
 * 2. Loading global resources like {@link MediaPlayer} objects at startup.
 * 3. Managing scene transitions between the Main Menu, Settings screen, and Game screen.
 * 4. Passing shared resources (like this Main instance and MediaPlayers) to the controllers.
 */
public class Main extends Application {

    /** The primary window (Stage) of the application. */
    // field for store Stage
    private Stage primaryStage;

    /** The globally shared player for the line clear sound. */
    private MediaPlayer clearRowSoundPlayer;
    /** The globally shared player for the speed up sound. */
    private MediaPlayer speedUpSoundPlayer;

    /**
     * The main entry point for this JavaFX application.
     * This method is called after the JFX toolkit is initialized.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.setTitle("TetrisJFX");

        // Enable fullscreen mode
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        // Load shared media players
        loadSounds();

        // Show the first screen
        showMainMenuScreen();

        // Show the stage
        primaryStage.show();
    }

    /**
     * Loads the sound files (e.g., clear row, speed up) from resources
     * and initializes the shared {@link MediaPlayer} fields.
     * This is called once at startup to ensure all scenes share the same players.
     */
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
            System.err.println("Failed to load douns: " + e.getMessage());
        }
    }

    /**
     * Loads and displays the Main Menu scene (menu.fxml).
     * It also passes a reference of this {@link Main} instance to the {@link MainMenuController}.
     */
    public void showMainMenuScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("menu.fxml"));
            Parent root = fxmlLoader.load();

            // pass the reference of the Main class to MainMenuController
            MainMenuController controller = fxmlLoader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Loads and displays the main game scene.
     * This method is called by MainMenuController.
     *
     * @param difficulty The difficulty level chosen from the menu.
     */
    public void showGameScreen(Difficulty difficulty) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
            Parent root = fxmlLoader.load();
            GuiController c = fxmlLoader.getController();
            c.setMainApp(this);
            GameSettings settings = new GameSettings();
            primaryStage.setScene(new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT));
            primaryStage.setFullScreen(true);
            // handles selected difficulty (pass Difficulty Enum to GameController)
            new GameController(c, difficulty, clearRowSoundPlayer, speedUpSoundPlayer, settings);
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Loads and displays the settings scene (settingScreen.fxml).
     * This method passes the shared {@link MediaPlayer} objects to the
     * {@link SettingController} so their volume can be adjusted.
     */
    public void showSettingScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("settingScreen.fxml"));
            Parent root = loader.load();
            SettingController controller = loader.getController();
            // pass the both sounds
            controller.setupVolumeControls(clearRowSoundPlayer, speedUpSoundPlayer);

            controller.setMainApp(this);

            primaryStage.setScene(new Scene(root, 420, 510));
            primaryStage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method, which serves as the entry point for the application.
     * It calls {@link Application#launch(String...)} to start the JavaFX application.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch(args);
    }
}