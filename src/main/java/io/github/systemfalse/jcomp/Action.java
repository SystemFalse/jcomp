package io.github.systemfalse.jcomp;

import java.util.List;

/**
 * This interface represents a method of a component. {@code Action} can be
 * invoked with method {@link #perform(Object...)} or {@link #createContext()}.
 *
 * @param <T> return type
 */
public interface Action<T> {
    /**
     * Method returns class of the return value. If action does not return any
     * value, {@code void.class} is returned.
     *
     * @return return class
     */
    Class<T> returnType();

    /**
     * Method returns name of the action.
     *
     * @return action name
     */
    String name();

    /**
     * Method returns number of parameters of this action.
     *
     * @return number of parameters
     */
    int parameterCount();

    /**
     * Method returns list of parameters of this action.
     *
     * @return list of parameters
     */
    List<Parameter> parameters();

    /**
     * Method creates new {@link ActionContext} for this action. Action context
     * can be used to set parameters and perform action.
     *
     * @return action context
     */
    ActionContext<T> createContext();

    /**
     * Method performs this action with the given parameters.
     *
     * @param args action arguments
     * @return action result or {@code null} if {@link #returnType()} returns {@code void.class}.
     */
    default T perform(Object... args) {
        return createContext().with(args).call();
    }
}
