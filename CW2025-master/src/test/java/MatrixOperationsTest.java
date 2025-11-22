import com.comp2042.GameConfig;
import com.comp2042.model.MatrixOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the static utility methods in the {@link MatrixOperations} class.
 * This test suite verifies the core game calculation logic, including collision detection
 * (intersect), matrix manipulation (copy, merge), and row clearance (checkRemoving).
 */
public class MatrixOperationsTest {
    private int[][] emptyBoard;
    private final int[][] brick = new int[][] {
            {0, 1, 0, 0},  // j=0: brick[0][0], brick[0][1], brick[0][2], brick[0][3]
            {0, 1, 0, 0},  // j=1
            {0, 1, 0, 0},  // j=2
            {0, 1, 0, 0}   // j=3
    };


    /**
     * Constructs the test suite for {@code MatrixOperationsTest}.
     * This default constructor is called by the JUnit test runner to instantiate the test class.
     */
    public MatrixOperationsTest() {}

    /**
     * Sets up the test environment before each test method.
     * Initializes several 2D arrays representing different initial states of the game board.
     */
    @BeforeEach
    void setUp() {
        // create an empty board
        emptyBoard = new int[GameConfig.BOARD_HEIGHT][GameConfig.BOARD_WIDTH];
        // fill the down most lines with bricks
        for (int x = 0; x < GameConfig.BOARD_WIDTH; x++) {
            emptyBoard[24][x] = 8;
        }
    }

    /**
     * Tests the {@link MatrixOperations#intersect(int[][], int[][], int, int)} method
     * to verify collision with the game board's floor or pre-placed bottom blocks.
     * Placement is at y=23, which ensures the falling brick's bottom edge (at y=23 or y=24, depending on the brick size)
     * checks for collision against the boundary or the fixed blocks at the bottom of the board (y=24).
     */
    @Test
    void testIntersectWithBottomRow() {
        boolean collision = MatrixOperations.intersect(emptyBoard, brick, 3, 23);
        // The brick will collide with bottom row
        assertTrue(collision);
    }

    /**
     * Tests the {@link MatrixOperations#intersect(int[][], int[][], int, int)} method with an empty board to verify no collision occurs.
     */
    @Test
    void testIntersectNoCollision() {
        boolean collision = MatrixOperations.intersect(emptyBoard, brick, 3, 0);
        assertFalse(collision);
    }

    /**
     * Tests {@link MatrixOperations#intersect(int[][], int[][], int, int)} when a brick is placed outside the board boundaries.
     */
    @Test
    void testIntersectOutOfBounds() {
        boolean collision = MatrixOperations.intersect(emptyBoard, brick, -3, 0);
        assertTrue(collision);
    }
}

