package com.comp2042.view;

import com.comp2042.GameConfig;
import com.comp2042.controller.GameController;
import com.comp2042.model.Difficulty;
import com.comp2042.model.GameSettings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private Stage primaryStage;

    private MediaPlayer clearRowSoundPlayer;
    private MediaPlayer speedUpSoundPlayer;

    public Main() {}

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("TetrisJFX");

        // Make it true fullscreen (no window borders, covers entire screen)
        primaryStage.initStyle(StageStyle.UNDECORATED);  // Removes title bar
        primaryStage.setFullScreen(true);                // Enter fullscreen mode
        primaryStage.setFullScreenExitHint("");          // Optional: hide "Press ESC to exit fullscreen"

        // Load shared media players
        loadSounds();

        // Show the first screen
        showMainMenuScreen();
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

    public void showMainMenuScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main_menu.fxml"));
            Parent root = fxmlLoader.load();

            MainMenuController controller = fxmlLoader.getController();
            controller.setMainApp(this);

            // Use fullscreen dimensions - no fixed size needed
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGameScreen(Difficulty difficulty) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game_layout.fxml"));
            Parent root = fxmlLoader.load();
            GuiController c = fxmlLoader.getController();
            c.setMainApp(this);
            GameSettings settings = new GameSettings();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            // Ensure the primary stage remains fullscreen when switching to the game scene
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
            primaryStage.show();

            new GameController(c, difficulty, clearRowSoundPlayer, speedUpSoundPlayer, settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSettingScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings_screen.fxml"));
            Parent root = loader.load();
            SettingController controller = loader.getController();
            controller.setupVolumeControls(clearRowSoundPlayer, speedUpSoundPlayer);
            controller.setMainApp(this);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}