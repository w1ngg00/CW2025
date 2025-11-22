package com.comp2042.controller;

/**
 * Defines the origin of a game action or event.
 */
public enum EventSource {
    /** The event was triggered by a user input (e.g., keyboard press). */
    USER,
    /** The event was triggered by the game loop (e.g., automatic brick fall). */
    THREAD
}
