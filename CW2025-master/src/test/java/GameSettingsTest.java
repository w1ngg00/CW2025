import com.comp2042.model.GameSettings;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the file I/O and properties logic of the {@code GameSettings} class.
 */
public class GameSettingsTest {

    private static final String SETTINGS_FILE = "settings.txt";

    /**
     * Constructs the test suite for {@code GameSettings}.
     */
    public GameSettingsTest() {}

    /**
     * delete the settings file before/after tests
     */
    @BeforeEach
    @AfterEach
    void deleteSettingFile() {
        new File(SETTINGS_FILE).delete();
    }

    @Test
    @DisplayName("Test to check whether default settings will be loaded if theres no file")
    void testLoadSettings_NoFile() {
        // Arrange
        // Act
        GameSettings settings = new GameSettings();
        // Aseset
        assertEquals(KeyCode.F, settings.getKeyCode("MOVE_LEFT"));
        assertEquals(KeyCode.SLASH, settings.getKeyCode("MOVE_LEFT_MOST"));
    }


    @Test
    @DisplayName("Test saving and loading a custom keyboard")
    void testSaveAndLoadSettings() {
        // Arrange (crete default and cange)
        GameSettings settings = new GameSettings();
        settings.setKeyCode("MOVE_LEFT", KeyCode.LEFT);
        // Act
        settings.saveSettings();
        // Assert
        GameSettings newSettings = new GameSettings();
        assertEquals(KeyCode.LEFT, newSettings.getKeyCode("MOVE_LEFT"));
    }
}
