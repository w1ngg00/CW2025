package com.comp2042.controller;

/**
 * Defines the type of action a {@code MoveEvent} represents.
 */
public enum EventType {
    /** Brick moves down one unit (Soft Drop). */
    DOWN,
    /** Brick moves left one unit. */
    LEFT,
    /** Brick moves right one unit. */
    RIGHT,
    /** Brick rotates (90 degrees left). */
    ROTATE
}
