package com.comp2042.model.bricks;

/**
 * Defines the contract for any class responsible for generating new {@link Brick} instances.
 * Implementations will control the logic for which brick comes next (e.g., random, bag system).
 */
public interface BrickGenerator {

    /**
     * Retrieves the next brick to be played and advances the generator's queue or state.
     * This method is called by the {@code Board} to spawn a new falling piece.
     *
     * @return The next {@link Brick} instance to be actively played.
     */
    Brick getBrick();

    /**
     * Retrieves the upcoming brick for preview purposes (e.g., "Next Brick" display)
     * *without* advancing the generator's state.
     *
     * @return The upcoming {@link Brick} instance.
     */
    Brick getNextBrick();
}
