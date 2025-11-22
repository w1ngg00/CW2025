package com.comp2042.model;

import javafx.scene.input.KeyCode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;


/**
 * Manages saving and loading user game settings (e.g., keybindings) to a persistent file.
 * This class ensures that user-defined controls persist between sessions.
 */
public class GameSettings {
    // file to save settings
    private static final String SETTINGS_FILE = "settings.txt";
    private final Properties properties = new Properties();

    /**
     * Constructs a new GameSettings object and loads existing settings from file.
     */
    public GameSettings() {
        loadSettings();
    }

    /**
     * Loads settings from settings.txt. If the file doesn't exist, it creates one with default settings.
     */
    public void loadSettings() {
        File file = new File(SETTINGS_FILE);
        if (!file.exists()) {
            setDefaultSettings(); // if no then create default setting to save
            saveSettings(); // create a new file
            return;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
        }
    }

    /**
     * Saves the current settings (keybindings) to settings.txt.
     */
    // save the current setting
    public void saveSettings() {
        try (FileWriter writer = new FileWriter(SETTINGS_FILE)) {
            properties.store(writer, "Tetris Game Settings");
        } catch (IOException e) {
            System.err.println("Failed to save settings; "+ e.getMessage());
        }
    }

    /**
     * Sets the default keybindings (your "Custom" layout).
     */
    public void setDefaultSettings() {
        properties.setProperty("MOVE_LEFT", KeyCode.F.name());
        properties.setProperty("MOVE_RIGHT", KeyCode.J.name());
        properties.setProperty("ROTATE_LEFT", KeyCode.S.name());
        properties.setProperty("ROTATE_RIGHT", KeyCode.L.name());
        properties.setProperty("SOFT_DROP", KeyCode.SPACE.name()); // Single Tap
        properties.setProperty("HARD_DROP", KeyCode.SPACE.name()); // Double Tap
        properties.setProperty("MOVE_LEFT_MOST", KeyCode.SLASH.name()); //
        properties.setProperty("MOVE_RIGHT_MOST", KeyCode.SHIFT.name());
        properties.setProperty("HOLD", KeyCode.V.name());
    }

    /**
     * Gets a specific keybinding as a KeyCode.
     * @param action    The action (e.g., "MOVE_LEFT").
     * @return          The saved KeyCode.
     */
    public KeyCode getKeyCode(String action) {
        String keyName = properties.getProperty(action);
        try { return KeyCode.valueOf(keyName); }
        catch (Exception e) {
            System.err.println("Invalid key");
            // if error, set as default
            setDefaultSettings();
            return getKeyCode(action);
        }
    }

    /**
     * Sets new keybind
     * @param action    The action (eg, "MOVE_LEFT")
     * @param code      The new KeyCode
     */
    public void setKeyCode(String action, KeyCode code) {
        properties.setProperty(action, code.name());
    }
}
