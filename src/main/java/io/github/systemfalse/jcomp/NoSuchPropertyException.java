package io.github.systemfalse.jcomp;

import java.util.Objects;

/**
 * Indicates that there is no property with the given name.
 */
public class NoSuchPropertyException extends RuntimeException {
    private final Component handler;
    private final String propertyName;

    /**
     * Constructor that takes component and property name and creates exception.
     *
     * @param handler component handler
     * @param propertyName property name
     */
    public NoSuchPropertyException(Component handler, String propertyName) {
        super("No such property: \"" + propertyName + "\" in component " +
                Objects.requireNonNull(handler).getClass().getCanonicalName());
        this.handler = handler;
        this.propertyName = propertyName;
    }

    /**
     * Method returns component that causes this exception.
     *
     * @return component handler
     */
    public Component getHandler() {
        return handler;
    }

    /**
     * Method returns requested property name.
     *
     * @return property name
     */
    public String getPropertyName() {
        return propertyName;
    }
}
