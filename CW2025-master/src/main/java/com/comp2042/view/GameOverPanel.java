package com.comp2042.view;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * A custom JavaFX component that displays the "GAME OVER" message
 * and a "Main Menu" button.
 * This panel extends {@link VBox} to vertically align the label and the button.
 * It provides a method ({@link #setMainMenu(Runnable)}) to inject the action
 * that should be performed when the "Main Menu" button is clicked.
 */
// change to VBox to align label and button vertically
public class GameOverPanel extends StackPane {

    /**
     * Stores the action (as a {@link Runnable}) to be executed when the
     * "Main Menu" button is clicked. This is typically set by the GuiController.
     */
    private Runnable onMainMenu;    // field to save action

    /**
     * Constructs the GameOverPanel.
     * Initializes the "GAME OVER" label and "Main Menu" button,
     * sets their styles and layout (centered, with spacing), and
     * configures the button's click event.
     */
    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        final Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().add("ipad-dark-grey");

        VBox contentBox = new VBox(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(gameOverLabel, mainMenuButton);

        // Overlay
        this.getChildren().add(contentBox);
        this.setAlignment(Pos.CENTER);

        this.getStyleClass().add("game-over-overlay");
        this.setVisible(false);
        this.setOpacity(0);

        mainMenuButton.setOnAction(event -> {
            onMainMenu.run();
        });
    }

    /**
     * Sets the action to be executed when the "Main Menu" button is pressed.
     * This method is used to pass the navigation logic (e.g., {@code mainApp.showMainMenuScreen()})
     * from the {@link GuiController} into this panel.
     *
     * @param action The {@link Runnable} to execute on button click.
     */
    public void setMainMenu(Runnable action) {
        this.onMainMenu = action;
    }


    /**
     * Displays this panel with a fade-in animation.
     * This method performs the following sequence:
     * Sets the panel's visibility to {@code true}.
     * Brings the panel to the front of the scene graph to ensure it overlays other elements.
     * Plays a {@link FadeTransition} that changes the opacity from 0.0 (transparent) to 1.0 (opaque) over 1 second.
     */
    public void showWithAnimation() {
        this.setVisible(true);
        this.toFront();

        // fade-in animation
        FadeTransition fade = new FadeTransition(Duration.seconds(1), this);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
}
