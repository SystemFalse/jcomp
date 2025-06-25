package io.github.systemfalse.jcomp;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * This interface represents function that maps one component type to another. Mapper
 * creates new component and setts its properties from the source component. Each
 * component can has own realization of mappers for different types, but if there is
 * no mapper for the given type, the default mapper is used.
 *
 * @param <C1> source type
 * @param <C2> target type
 */
public interface Mapper<C1 extends Component, C2 extends Component> extends Function<C1, C2> {
    /**
     * Method returns source component type.
     *
     * @return source component type
     */
    ComponentType<C1> from();

    /**
     * Method returns target component type.
     *
     * @return target component type
     */
    ComponentType<C2> to();

    /**
     * Method creates default mapper.
     * <p>
     * Default mapper creates new instance of target type and setts its properties from
     * the source component only if they are of the same type and have the same name.
     * </p>
     *
     * @param from source component type
     * @param to target component type
     * @return default mapper for given types
     * @param <T1> source type
     * @param <T2> target type
     */
    static <T1 extends Component, T2 extends Component> Mapper<T1, T2> defaultMapper(ComponentType<T1> from,
                                                                                     ComponentType<T2> to) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        return new DefaultMapper<>(from, to);
    }
}

record DefaultMapper<C1 extends Component, C2 extends Component>(ComponentType<C1> from,
                                                                 ComponentType<C2> to) implements Mapper<C1, C2> {
    @Override
    public C2 apply(C1 comp) {
        var mapped = to.initialize().get();
        for (var property : to.properties()) {
            Optional<Property<?>> o1 = comp.property(property), o2 = mapped.property(property);
            if (o1.isPresent() && o2.isPresent()) {
                Property<?> p1 = o1.get(), p2 = o2.get();
                if (p2.type().isAssignableFrom(p1.type())) {
                    p2.set(p1.get());
                }
            }
        }
        return mapped;
    }
}
