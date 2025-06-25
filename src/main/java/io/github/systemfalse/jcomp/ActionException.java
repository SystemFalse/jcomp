package io.github.systemfalse.jcomp;

import java.util.Objects;

/**
 * Indicates that an exception occurred during action execution.
 */
public class ActionException extends RuntimeException {
    private final Component handler;
    private final String actionName;

    /**
     * Constructor that takes a handler and action name and creates new exception.
     *
     * @param handler component whose method caused the exception
     * @param actionName name of the action
     */
    public ActionException(Component handler, String actionName) {
        super("Exception occurred in action \"" + actionName + "\" of component " +
                Objects.requireNonNull(handler).getClass().getCanonicalName());
        this.handler = handler;
        this.actionName = actionName;
    }

    /**
     * Constructor that takes a handler, action name and a message.
     *
     * @param handler component whose method caused the exception
     * @param actionName name of the action
     * @param message exception message
     */
    public ActionException(Component handler, String actionName, String message) {
        super("Exception occurred in action \"" + actionName + "\" of component " +
                Objects.requireNonNull(handler).getClass().getCanonicalName() + ": " + message);
        this.handler = handler;
        this.actionName = actionName;
    }

    /**
     * Method constructs new exception from a handler, action name and a cause.
     *
     * @param handler component whose method caused the exception
     * @param actionName name of the action
     * @param cause exception cause
     */
    public ActionException(Component handler, String actionName, Throwable cause) {
        super("Exception occurred in action \"" + actionName + "\" of component " +
                Objects.requireNonNull(handler).getClass().getCanonicalName(), cause);
        this.handler = handler;
        this.actionName = actionName;
    }

    /**
     * Method constructs new exception from a handler, action name, message and cause.
     *
     * @param handler component whose method caused the exception
     * @param actionName name of the action
     * @param message exception message
     * @param cause exception cause
     */
    public ActionException(Component handler, String actionName, String message, Throwable cause) {
        super("Exception occurred in action \"" + actionName + "\" of component " +
                Objects.requireNonNull(handler).getClass().getCanonicalName() + ": " + message, cause);
        this.handler = handler;
        this.actionName = actionName;
    }

    /**
     * Method returns component that caused the exception.
     *
     * @return component
     */
    public Component getHandler() {
        return handler;
    }

    /**
     * Method returns name of the action that caused the exception.
     *
     * @return action name
     */
    public String getActionName() {
        return actionName;
    }
}
