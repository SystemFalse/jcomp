package io.github.systemfalse.jcomp;

import java.util.Objects;

/**
 * Indicates that the requested action was not found in component.
 */
public class NoSuchActionException extends RuntimeException {
    private final Component handler;
    private final String actionName;

    /**
     * Constructor that takes component and requested action and creates exception.
     *
     * @param handler component handler
     * @param actionName requested action
     */
    public NoSuchActionException(Component handler, String actionName) {
        super("No such action: \"" + actionName + "\" in component " +
                Objects.requireNonNull(handler).getClass().getCanonicalName());
        this.handler = handler;
        this.actionName = actionName;
    }

    /**
     * Method returns component that caused this exception.
     *
     * @return component
     */
    public Component getHandler() {
        return handler;
    }

    /**
     * Method returns requested action name.
     *
     * @return action name
     */
    public String getActionName() {
        return actionName;
    }
}
