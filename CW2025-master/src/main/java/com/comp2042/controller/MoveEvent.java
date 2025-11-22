package com.comp2042.controller;


/**
 * A simple, immutable data object representing a single game action.
 * This class is used to pass event details (what happened and who caused it)
 * from the input handler or game loop to the {@link GameController}.
 */
public final class MoveEvent {
    // The type of action that occured (DOWN, LEFT, ROTATE)
    private final EventType eventType;
    // The source of the event (USER, THREAD)
    private final EventSource eventSource;

    /**
     * Constructs a new MoveEvent.
     *
     * @param eventType The type of action (DOWN, LEFT, etc.).
     * @param eventSource The source of the action (USER or THREAD).
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the type of action that occurred.
     *
     * @return The {@link EventType}.
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the source of the event.
     *
     * @return The {@link EventSource}.
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
