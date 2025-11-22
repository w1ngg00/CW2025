import com.comp2042.model.Difficulty;
import com.comp2042.model.HighScoreManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests the core functionality and file I/O logic of the {@link HighScoreManager} class.
 * This test suite verifies loading, saving, and persistence of high scores across
 * different difficulty levels, ensuring scores are correctly separated.
 * All tests clean up by deleting the temporary high score files (e.g., highscore_easy.txt)
 * after execution to prevent interference.
 */
public class HighScoreManagerTest {

    private final String EASY_FILE = "highscore_easy.txt";
    private final String HARD_FILE = "highscore_hard.txt";

    /**
     * Constructs the test suite for {@code HighScoreManager}.
     * This constructor is used by the JUnit test runner to instantiate the test class.
     */
    public HighScoreManagerTest() {}

    /**
     * Executes cleanup after every test method.
     * Deletes the high score files created during the test to ensure a clean state for the next test.
     */
    @BeforeEach
    @AfterEach
    void tearDown() {
        new File(EASY_FILE).delete();
        new File(HARD_FILE).delete();
    }

    /**
     * Tests that a new manager loads the score as 0 when the corresponding file does not exist.
     */
    @Test
    @DisplayName("Test to load a high score when the targeting file doesnt exist")
    void testLoadingHighScore() {
        // Arrange
        // Act
        HighScoreManager manager = new HighScoreManager(Difficulty.EASY);   // not exist
        // Assert
        assertEquals(0, manager.getHighScore());    // since the file doesnt exis
    }

    /**
     * Tests saving a score higher than the current high score.
     */
    @Test
    @DisplayName("Test to save a new hgih score")
    void testSaveHighScore() {
        // Arrange
        HighScoreManager manager = new HighScoreManager(Difficulty.EASY);
        // Act
        boolean result = manager.saveHighScore(5000);
        // Assert
        assertTrue(result); // save highscore should be successfully executed
        assertEquals(5000, manager.getHighScore()); // since 5000 had been saved, it should return 5000
    }

    /**
     * Tests loading a high score that was previously saved to the persistent file.
     * @throws IOException If file writing fails during setup.
     */
    @Test
    @DisplayName("Test loading a saved high score")
    void testLoadHighScore() throws IOException {
        // Arrange
        // Prepare a file with high score
        File file = new File(HARD_FILE);
        java.io.FileWriter writer = new java.io.FileWriter(file);
        writer.write("5000");
        writer.close();
        // Act
        HighScoreManager manager = new HighScoreManager(Difficulty.HARD);   // constructor will call loadHighScore() automatically
        // Assert
        assertEquals(5000, manager.getHighScore());
    }

    /**
     * Tests trying to save a score that is lower than the current high score.
     */
    @Test
    @DisplayName("Test to save a score with not the highest score")
    void testSaveHighScore_NotHighScore() {
        // Arrange (manager, save high score)
        HighScoreManager manager = new HighScoreManager(Difficulty.HARD);
        manager.saveHighScore(5000);
        // Act (try to save the score)
        boolean result = manager.saveHighScore(4999);
        // Assert
        assertFalse(result);    // the result shouldnt be saved in the file since its not the highest
        assertEquals(5000, manager.getHighScore()); // the high score should be 5000 as its the highest
    }

    /**
     * Tests that high scores for different difficulty modes do not overwrite each other.
     */
    @Test
    @DisplayName("Test to see whether the high scores from different mode load to the correct file")
    void testDifficultSeparation() {
        // Arrange
        HighScoreManager easyManager = new HighScoreManager(Difficulty.EASY);
        HighScoreManager hardManager = new HighScoreManager(Difficulty.HARD);
        // Act
        easyManager.saveHighScore(10000);
        hardManager.saveHighScore(20000);
        // Assert
        assertEquals(10000, new HighScoreManager(Difficulty.EASY).getHighScore());
        assertEquals(20000, new HighScoreManager(Difficulty.HARD).getHighScore());
    }
}
