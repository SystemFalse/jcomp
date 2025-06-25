package io.github.systemfalse.jcomp;

import java.util.List;
import java.util.function.Function;

/**
 * This interface represents a parameter of an action. All parameters have their
 * type and name. A parameter also can have preset index if action supports
 * multiple overloads.
 */
public interface Parameter {
    /**
     * Method returns class of the parameter value.
     *
     * @return class
     */
    Class<?> type();

    /**
     * Method returns name of the parameter.
     *
     * @return parameter name
     */
    String name();

    /**
     * Method checks if given value is valid for this parameter.
     *
     * @param value value to check
     * @return {@code true} if value is valid, {@code false} otherwise
     */
    boolean isValid(Object value);

    /**
     * Method returns whether this parameter is required. If parameter is required
     * its value must be provided in arguments.
     *
     * @return {@code true} if parameter is required, {@code false} otherwise
     */
    default boolean isRequired() {
        return true;
    }

    /**
     * Method returns default value function of the parameter. If this parameter is required
     * {@code null} is returned.
     *
     * @return default function
     */
    default Function<List<Object>, Object> defaultValue() {
        return null;
    }
}
