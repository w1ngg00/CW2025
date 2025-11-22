import com.comp2042.GameConfig;
import com.comp2042.model.Board;
import com.comp2042.model.SimpleBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the core functionality of the {@code SimpleBoard} class.
 */
public class SimpleBoardTest {

    private Board board;

    /**
     * Constructs the test suite for {@code SimpleBoard}.
     * This default constructor is called by the JUnit test runner to instantiate the test class,
     * allowing it to execute the defined unit tests against the {@code SimpleBoard} implementation.
     */
    public SimpleBoardTest() {}

    /**
     * This method will be executed before all tests (@Test)
     * Prevent effect between each test by creating new baord
     */
    @BeforeEach
    void setUp() {
        board = new SimpleBoard(GameConfig.BOARD_HEIGHT, GameConfig.BOARD_WIDTH);
    }

    /**
     * Test whether the brick can move down correctly
     */
    @Test
    void testMoveBrickDown() {
        // Preparation - spawn a brick
        board.createNewBrick();

        // Execute - action
        boolean canMove = board.moveBrickDown();

        // Evaluation - assert
        assertTrue(canMove);
    }

    /**
     * Test whether the hard drop acts correctly
     */
    @Test
    void testHardDrop() {
        // Preparation
        board.createNewBrick();
        // Execute
        int rowsDropped = board.hardDrop();
        // Evaluation
        assertTrue(rowsDropped > 20);
        // After hard drop -> cannot move down
        assertFalse(board.moveBrickDown());
    }

    @Test
    void testHoldBrick() {
        // Preparation
        board.createNewBrick();
        // Execute 1st hold (since cannot swap 2 times)
        boolean canSwap1 = board.swapHoldBrick();
        // Assert
        assertTrue(canSwap1);
        // 1st brick should be in the hold panel
        assertNotNull(board.getHoldBrickShape());

        // Execute 2nd hold
        boolean canSwap2 = board.swapHoldBrick();
        // Assert
        assertFalse(canSwap2);  // since this is the 2nd swap in a turn
    }


    @Test
    void testSpawnAndHardDropObstacle() {
        // Arrange
        board.newGame();
        board.createNewBrick();
        // Act
        board.spawnAndHardDropObstacle();
        // Assert - calculate the total number of 1x1 brick
        int count = 0;
        for (int y = 0; y < board.getBoardMatrix().length; y++) {
            for (int x = 0; x < board.getBoardMatrix()[y].length; x++) {
                if (board.getBoardMatrix()[y][x] != 0) {
                    count++;
                }
            }
        }
        // 4x1 bricks are now in the game screen
        assertEquals(4, count);
    }
}
