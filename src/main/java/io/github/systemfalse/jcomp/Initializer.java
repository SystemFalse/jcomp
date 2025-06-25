package io.github.systemfalse.jcomp;

import java.util.function.Supplier;

/**
 * This interface is used to initialize a component. It can be used to
 * set component properties before or after its creation using constructor.
 *
 * @param <C> component type
 */
public interface Initializer<C extends Component> extends Supplier<C> {
    /**
     * Method sets the value of the property with the given name and returns
     * this object for chaining.
     *
     * @param property property name
     * @param value property value
     * @return this object
     */
    Initializer<C> set(String property, Object value);
}
