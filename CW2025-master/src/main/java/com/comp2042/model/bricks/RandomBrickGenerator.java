package com.comp2042.model.bricks;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Implements the {@link BrickGenerator} interface using a queue-based system.
 * This class maintains a queue of upcoming bricks (e.g., the next 4) to support
 * multi-brick previews in the UI. When a brick is requested via {@link #getBrick()},
 * it pulls the next one from the queue and adds a new random brick to the end.
 */
public class RandomBrickGenerator implements BrickGenerator {

    /** @deprecated This field is part of an older implementation and is no longer used by the queue system. */
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    /** Holds the queue of upcoming bricks for the game. */
    private Queue<Brick> upcomingBricks;

    /** Defines the number of bricks to keep in the preview queue. */
    // Queue size
    private static final int UPCOMING_QUEUE_SIZE = 4;

    /**
     * Constructs a new RandomBrickGenerator.
     * Initializes the {@code upcomingBricks} queue and populates it
     * with the initial set of bricks based on {@code UPCOMING_QUEUE_SIZE}.
     */
    public RandomBrickGenerator() {
        // initialize the queue and fill with 4 bricks
        upcomingBricks = new LinkedList<>();
        for (int i = 0; i < UPCOMING_QUEUE_SIZE; i++) {
            upcomingBricks.add(newBrick());
        }
    }

    /**
     * {@inheritDoc}
     * This implementation polls the next brick from the front of the
     * {@code upcomingBricks} queue and adds a new random brick to the end
     * of the queue to maintain its size.
     */
    @Override
    public Brick getBrick() {
        Brick brick = upcomingBricks.poll();
        upcomingBricks.add(newBrick());
        return brick;
    }

    /**
     * {@inheritDoc}
     * This implementation peeks at the front of the {@code upcomingBricks} queue.
     *
     * @return The next {@link Brick} in the queue (without removing it).
     */
    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }

    /**
     * Gets a list of shapes for all bricks currently in the preview queue.
     * This is used by the UI to display multiple "next" bricks.
     *
     * @return A {@link List} of {@code int[][]} matrices representing the
     * default rotation (index 0) of each brick in the queue.
     */
    public List<int[][]> getNextBrickShape() {
        // empty list for storing results
        List<int[][]> shapesList = new ArrayList<>();
        // take the inside of the queue as brick and iterate
        for (Brick brick : upcomingBricks) {
            // get the default shape
            int[][] shape = brick.getShapeMatrix().get(0);
            // append the shape to empty list
            shapesList.add(shape);
        }
        return shapesList;
    }

    /**
     * Creates a new {@link Brick} instance of a random type (I, J, L, O, S, T, Z).
     *
     * @return A new, randomly selected {@link Brick}.
     */
    private Brick newBrick() {
        int randomBrick = (int) (Math.random() * 7);    // 0 ~ 6

        return switch (randomBrick) {
            case 0 -> new IBrick();
            case 1 -> new JBrick();
            case 2 -> new LBrick();
            case 3 -> new OBrick();
            case 4 -> new SBrick();
            case 5 -> new TBrick();
            case 6 -> new ZBrick();
            default -> new IBrick();
        };
    }
}
