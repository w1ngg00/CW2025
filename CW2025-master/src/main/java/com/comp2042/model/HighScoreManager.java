package com.comp2042.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Manages reading and writing the high score to a text file.
 * This adheres to SRP by separating file I/O logic from the {@link com.comp2042.controller.GameController}.
 * It loads the high score upon instantiation and provides methods to check
 * and save a new high score.
 */
public class HighScoreManager {

    /** The static file name used to store the high score. */
    // file name to save
    private static final String EASY_FILE = "highscore_easy.txt";
    private static final String NORMAL_FILE = "highscore_normal.txt";
    private static final String HARD_FILE = "highscore_hard.txt";
    private static final String EXTRA_FILE = "highscore_extra.txt";

    /** The currently loaded high score. */
    private int highScore;

    /**
     * Constructs a new HighScoreManager for a specific difficulty.
     * Automatically loads the high score for that difficulty upon creation.
     *
     * @param difficulty The difficulty level to manage.
     */
    public HighScoreManager(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.highScore = loadHighScore();
    }

    /**
     * Loads the high score from the file.
     * @return The saved high score, or 0 if no file exists.
     */
    public int loadHighScore() {
        try {
            File file = new File(getHighScoreFile());
            if (!file.exists()) { return 0; }
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextInt()) {
                int score = scanner.nextInt();
                scanner.close();
                return score;
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Failed to load high scsore" + e.getMessage());
        }
        return 0;
    }

    /**
     * Helper method to get the correct filename based on the current difficulty.
     * @return The filename (e.g., "highscore_easy.txt").
     */
    private String getHighScoreFile() {
        return switch (this.difficulty) {
            case EASY -> EASY_FILE;
            case NORMAL -> NORMAL_FILE;
            case HARD -> HARD_FILE;
            case EXTRA -> EXTRA_FILE;
        };
    }

    private Difficulty difficulty;

    /**
     * Checks if the new score is a high score and saves it to the file.
     * @param newScore The final score from the game.
     * @return true if this was a new high score, false otherwise.
     */
    public boolean saveHighScore(int newScore) {
        if (newScore > this.highScore) {
            this.highScore = newScore;
            // write to the file
            try {
                FileWriter writer = new FileWriter(getHighScoreFile(), false); // Overwrite
                writer.write(String.valueOf(newScore));
                writer.close();
                return true;
            } catch (IOException e) {
                System.err.println("Falied to save high score" + e.getMessage());
            }
        }
        return false;
    }

    /**
     * Gets the currently loaded high score. (Getter)
     * @return The high score.
     */
    public int getHighScore() {
        return this.highScore;
    }
}