package io.github.systemfalse.jcomp;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * This interface represents a context for an action. It is used for passing
 * arguments in free form.
 *
 * @param <T> action return type
 */
public interface ActionContext<T> extends Callable<T> {
    /**
     * Method sets an argument with given name and value.
     *
     * @param name parameters name
     * @param value argument value
     * @return this context
     */
    ActionContext<T> with(String name, Object value);

    /**
     * Method sets an argument with given index and value.
     *
     * @param index parameters index
     * @param value argument value
     * @return this context
     */
    ActionContext<T> with(int index, Object value);

    /**
     * Method adds an argument with given value to the end of the argument list.
     *
     * @param value argument value
     * @return this context
     */
    ActionContext<T> with(Object value);

    /**
     * Method adds given argument values to the end of the argument list.
     *
     * @param values argument values
     * @return this context
     */
    ActionContext<T> with(Object... values);

    /**
     * Method maps the action return type with the given mapper function.
     *
     * @param mapper mapper
     * @return new action context
     * @param <R> mapped return type
     */
    default <R> ActionContext<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return new MappedActionContext<>(this, mapper);
    }

    /**
     * Method performs the action and returns a result. If the action has
     * several presets, this method automatically select the most suitable.
     *
     * @return action result
     *
     * @throws ActionException if the action fails
     */
    T call() throws ActionException;
}

class MappedActionContext<T, R> implements ActionContext<R> {
    private ActionContext<T> context;
    private final Function<T, R> mapper;

    MappedActionContext(ActionContext<T> context, Function<T, R> mapper) {
        this.context = context;
        this.mapper = mapper;
    }

    public ActionContext<R> with(String name, Object value) {
        context = context.with(name, value);
        return this;
    }

    public ActionContext<R> with(int index, Object value) {
        context = context.with(index, value);
        return this;
    }

    public ActionContext<R> with(Object value) {
        context = context.with(value);
        return this;
    }

    public ActionContext<R> with(Object... values) {
        context = context.with(values);
        return this;
    }

    public R call() throws ActionException {
        T result = context.call();
        return mapper.apply(result);
    }
}
