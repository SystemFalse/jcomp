package io.github.systemfalse.jcomp;

import java.util.Optional;
import java.util.function.Function;

/**
 * The {@code Component} interface serves as the base interface for all components
 * in a component-based architecture. Components contain properties aka fields,
 * actions aka methods and children. Child components can be retrieved by name or
 * index.
 * <p>
 * Clients are not required to implement this interface. Instead, they can use
 * {@link ComponentFactory} to create builders, instances or wrappers or
 * {@link io.github.systemfalse.jcomp.annotations.ComponentProcessor} to generate
 * components automatically.
 * </p>
 *
 * @see Property
 * @see Action
 * @see ComponentFactory
 * @see io.github.systemfalse.jcomp.annotations.ComponentProcessor
 */
public interface Component extends Cloneable {
    /**
     * Method returns the type of this component object. Type is used to determine
     * whether any component is an instance of the type and to create new component
     * instances.
     *
     * @return type of this component
     */
    ComponentType<?> type();

    /**
     * Method returns a copy of this component with the same type. This method also
     * clones all child components and properties.
     *
     * @return a copy of this component
     */
    Component clone();

    /**
     * Method returns the value of the property with the given name.
     *
     * @param property name of the property
     * @return value of the property
     *
     * @throws NoSuchPropertyException if property with given name was not found
     */
    Object get(String property);

    /**
     * Method returns the value of the property with the given name with a mapper.
     *
     * @param property name of the property
     * @param mapper mapper function
     * @return value of the property
     * @param <T> source type of the property
     * @param <R> target type of the property
     *
     * @throws NoSuchPropertyException if property with given name was not found
     */
    <T, R> R get(String property, Function<T, R> mapper);

    /**
     * Method sets the value of the property with the given name. If given value
     * is not applicable for the property, a {@code ClassCastException} is thrown.
     *
     * @param property name of the property
     * @param value value of the property
     *
     * @throws ClassCastException if given value is not applicable for the property
     * @throws UnsupportedOperationException if this property is read-only
     * @throws NoSuchPropertyException if property with given name was not found
     */
    void set(String property, Object value);

    /**
     * Method returns the property with the given name.
     *
     * @param property name of the property
     * @return property
     */
    Optional<Property<?>> property(String property);

    /**
     * Method creates {@link ActionContext} from action with the given name.
     *
     * @param action name of the action
     * @return action context
     *
     * @throws NoSuchActionException if action with the given name was not found
     */
    ActionContext<?> invoke(String action);

    /**
     * Method returns the action with the given name.
     *
     * @param action name of the action
     * @return action
     */
    Optional<Action<?>> action(String action);

    /**
     * Method returns new component mapped to the given type. Each component can
     * have own mappers for different types, but if there is no mapper for the
     * given type, the default mapper is used.
     *
     * @param type mapping type
     * @return mapped component
     * @param <C> target type
     *
     * @see Mapper
     */
    <C extends Component> C as(ComponentType<C> type);

    /**
     * Method returns new component mapped using the given mapper.
     *
     * @param mapper mapper to use
     * @return mapped component
     * @param <C1> source type
     * @param <C2> target type
     */
    @SuppressWarnings("unchecked")
    default <C1 extends Component, C2 extends Component> C2 map(Mapper<C1, C2> mapper) {
        if (mapper.from().isInstance(this)) {
            return mapper.apply((C1) this);
        }
        throw new IllegalArgumentException("unsuitable mapper");
    }

    /**
     * Method returns list of child components.
     *
     * @return list of child components
     */
    ComponentList children();
}
