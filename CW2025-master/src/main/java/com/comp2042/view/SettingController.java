package com.comp2042.view;

import com.comp2042.model.GameSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the settings screen (settings_screen.fxml).
 * This class manages the UI elements on the settings screen, primarily
 * sliders for adjusting the volume of the game's shared {@link MediaPlayer} instances.
 * It receives the {@link Main} app reference for navigation and the players
 * for volume control.
 */
public class SettingController implements Initializable {

    /** The FXML {@link Slider} that controls the volume. */
    @FXML
    private Slider clearRowVolumeSlider;
    @FXML
    private Slider speedUpVolumeSlider;

    @FXML private Button moveLeftKeyButton;
    @FXML private Button moveRightKeyButton;
    @FXML private Button rotateLeftKeyButton;
    @FXML private Button rotateRightKeyButton;
    @FXML private Button softDropKeyButton;
    @FXML private Button hardDropKeyButton;
    @FXML private Button holdKeyButton;
    @FXML private Button moveLeftMostKeyButton;
    @FXML private Button moveRightMostKeyButton;
    @FXML private Button resetButton;

    /** A reference to the main application class for switching scenes. */
    private Main mainApp;

    /** The shared {@link MediaPlayer} for the row clear sound and speed up sound, received from Main. */
    private MediaPlayer clearRowPlayer;
    private MediaPlayer speedUpPlayer;

    private GameSettings settings;


    /**
     * Constructs the SettingController instance.
     * This default constructor is called automatically by the JavaFX {@code FXMLLoader}
     * before the {@code initialize()} method is invoked. It is used to create
     * the controller object before injecting FXML elements.
     */
    public SettingController() {}

    // This method will be called automatically
    /**
     * Initializes the controller.
     * This method is called automatically by JavaFX after the FXML file is loaded.
     * It adds listeners to both volume sliders, which update the volume
     * of the corresponding {@link MediaPlayer} in real-time.
     *
     * @param url The location used to resolve relative paths for the root object, or null if not known.
     * @param resourceBundle The resources used to localize the root object, or null if not known.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.settings = new GameSettings();
        loadKeybindButtons();


        clearRowVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (clearRowPlayer != null) {
                // convert the slider value to sound volume
                clearRowPlayer.setVolume(newValue.doubleValue() / 100.0);
            }
        });

        speedUpVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (speedUpPlayer != null) {
                speedUpPlayer.setVolume(newValue.doubleValue() / 100.0);
            }
        });
    }

    /**
     * Load the current saved keybind and set to button text
     */
    private void loadKeybindButtons() {
        moveLeftKeyButton.setText(settings.getKeyCode("MOVE_LEFT").name());
        moveRightKeyButton.setText(settings.getKeyCode("MOVE_RIGHT").name());
        rotateLeftKeyButton.setText(settings.getKeyCode("ROTATE_LEFT").name());
        rotateRightKeyButton.setText(settings.getKeyCode("ROTATE_RIGHT").name());
        softDropKeyButton.setText(settings.getKeyCode("SOFT_DROP").name());
        hardDropKeyButton.setText(settings.getKeyCode("HARD_DROP").name());
        holdKeyButton.setText(settings.getKeyCode("HOLD").name());
        moveLeftMostKeyButton.setText(settings.getKeyCode("MOVE_LEFT_MOST").name());
        moveRightMostKeyButton.setText(settings.getKeyCode("MOVE_RIGHT_MOST").name());
    }


    @FXML
    private void onChangeMoveLeft() { captureKeyForAction(moveLeftKeyButton, "MOVE_LEFT"); }
    @FXML
    private void onChangeMoveRight() { captureKeyForAction(moveRightKeyButton, "MOVE_RIGHT"); }
    @FXML
    private void onChangeLeftMost() { captureKeyForAction(moveLeftMostKeyButton, "MOVE_LEFT_MOST"); }
    @FXML
    private void onChangeRightMost() { captureKeyForAction(moveRightMostKeyButton, "MOVE_RIGHT_MOST"); }
    @FXML
    private void onChangeSoftDrop() { captureKeyForAction(softDropKeyButton, "SOFT_DROP"); }
    @FXML
    private void onChangeHardDrop() { captureKeyForAction(hardDropKeyButton, "HARD_DROP"); }
    @FXML
    private void onChangeRotateLeft() { captureKeyForAction(rotateLeftKeyButton, "ROTATE_LEFT"); }
    @FXML
    private void onChangeRotateRight() { captureKeyForAction(rotateRightKeyButton, "ROTATE_RIGHT"); }
    @FXML
    private void onChangeHold() { captureKeyForAction(holdKeyButton, "HOLD"); }

    /**
     * Resets all keybindings to their default values.
     * This method calls {@link GameSettings#setDefaultSettings()} and then
     * updates the UI to reflect the default keys.
     */
    @FXML
    private void onResetKeys() {
        settings.setDefaultSettings();
        loadKeybindButtons();
    }


    /**
     * Captures the next key press to rebind a specific game action.
     * When a button is clicked, this method:
     * Changes the button text to "Press any key..." to prompt the user.
     * Sets a one-time {@code setOnKeyPressed} event listener on the button.
     * Updates the {@link GameSettings} model with the new {@link KeyCode} when a key is pressed.
     * Updates the button text to the new key name.
     *
     * @param button The button that was clicked to initiate the change.
     * @param action The string identifier for the action in {@link GameSettings} (e.g., "MOVE_LEFT").
     */
    private void captureKeyForAction(Button button, String action) {
        String originalText = button.getText();
        button.setText("Press any key...");

        button.setOnKeyPressed(event -> {
            KeyCode newKey = event.getCode();
            settings.setKeyCode(action, newKey);
            button.setText(newKey.name());

            button.setOnKeyPressed(null);
            event.consume();
        });
    }


    /**
     * Injects the shared {@link MediaPlayer} instances from the {@link Main} application.
     * This method also sets the initial value of the sliders to match the
     * players' current volume levels.
     *
     * @param clearRowPlayer The shared player for the line clear sound.
     * @param speedUpPlayer  The shared player for the speed up sound.
     */
    public void setupVolumeControls(MediaPlayer clearRowPlayer, MediaPlayer speedUpPlayer) {
        this.clearRowPlayer = clearRowPlayer;
        this.speedUpPlayer = speedUpPlayer;
        if (clearRowPlayer != null) {
            clearRowVolumeSlider.setValue(this.clearRowPlayer.getVolume() * 100.0);
        }
        if (speedUpPlayer != null) {
            speedUpVolumeSlider.setValue(this.speedUpPlayer.getVolume() * 100.0);
        }
    }

    /**
     * Handles the "Back" button click event.
     * Tells the main application to navigate back to the main menu screen.
     */
    @FXML
    private void goToMainMenu() {
        // Save the settings before go back to menu
        settings.saveSettings();
        mainApp.showMainMenuScreen();
    }

    /**
     * Sets the reference to the main application class.
     * This is used for scene switching (e.g., returning to the main menu).
     *
     * @param mainApp The instance of the {@link Main} application.
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
