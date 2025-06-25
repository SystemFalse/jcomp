package io.github.systemfalse.jcomp;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * The {@code ComponentType} interface represents an object that can be used to create
 * new component instances. Component type has methods for creating new components,
 * checking whether the given component is an instance of the type, getting component
 * property and action names and casting component to this type.
 * <p>
 * Clients are not required to implement this interface. Instead, they can use
 * {@link ComponentFactory} for creating component type builders or wrappers. Alternatively,
 * they can use {@link io.github.systemfalse.jcomp.annotations.ComponentProcessor} for
 * generating component types automatically.
 * </p>
 *
 * @param <T> type of the component that can be created
 * @see ComponentFactory
 * @see io.github.systemfalse.jcomp.annotations.ComponentProcessor
 */
public interface ComponentType<T extends Component> extends Type {
    /**
     * Method returns whether the given component is an instance of the type. Component
     * is instance of the type when it has equal properties and actins.
     * <p>
     * This method is equaled to {@link Class#isInstance(Object)}.
     * </p>
     *
     * @param comp component to check
     * @return {@code true} if component is an instance of the type, {@code false} otherwise
     */
    boolean isInstance(Component comp);

    /**
     * Method returns set of component property names.
     *
     * @return set of property names
     */
    Set<String> properties();

    /**
     * Method returns set of component action names.
     *
     * @return set of action names
     */
    Set<String> actions();

    /**
     * Method checks whether the given component can be cast to this type and returns
     * cast component without creating new instance.
     * <p>
     * This method is equaled to {@link Class#cast(Object)}.
     * </p>
     *
     * @param comp component to cast
     * @return cast component
     */
    T cast(Component comp);

    /**
     * Method returns component initializer. That object can be used to initialize component
     * properties before or after its creation using constructor.
     *
     * @return component initializer
     * @see Initializer
     */
    Initializer<T> initialize();
}
