package io.github.systemfalse.jcomp;

import java.util.Objects;
import java.util.function.Function;

/**
 * This interface represents a property of a component. {@code Property} can be
 * either handler or supplier of a value. All properties have their type and name.
 *
 * @param <T> type of the property
 */
public interface Property<T> extends Cloneable {
    /**
     * Method returns class of the value of this property.
     *
     * @return class of the value
     */
    Class<T> type();

    /**
     * Method returns name of this property.
     *
     * @return name of the property
     */
    String name();

    /**
     * Method returns value of this property.
     *
     * @return value of the property
     */
    T get();

    /**
     * Method returns value of this property after applying given mapper.
     *
     * @param mapper mapper
     * @return mapped value
     * @param <R> target type
     */
    default <R> R get(Function<T, R> mapper) {
        return mapper.apply(get());
    }

    /**
     * Method returns whether this property is read-only.
     * @return {@code true} if property is read-only, {@code false} otherwise
     */
    boolean isReadOnly();

    /**
     * Method sets value of this property. If this property is read-only, an
     * {@code UnsupportedOperationException} is thrown.
     * @param value value to set
     *
     * @throws ClassCastException if given value is not applicable for the property
     * @throws UnsupportedOperationException if this property is read-only
     */
    void set(Object value);

    /**
     * Method returns new read-only property that is mapped to the given type. New
     * property is linked with the original property.
     *
     * @param type target type
     * @param converter converter
     * @return mapped property
     * @param <R> target type
     */
    default <R> Property<R> map(Class<R> type, Function<T, R> converter) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(converter, "converter");
        return new ReadOnlyMappedProperty<>(type, this, converter);
    }

    /**
     * Method returns new property that is mapped to the given type. New property
     * is linked with the original property.
     *
     * @param type target type
     * @param forward converter from the original type to the new type
     * @param backward converter from the new type to the original type
     * @return mapped property
     * @param <R> target type
     */
    default <R> Property<R> map(Class<R> type, Function<T, R> forward, Function<R, T> backward) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(forward, "forward");
        Objects.requireNonNull(backward, "backward");
        return new WritableMappedProperty<>(type, this, forward, backward);
    }

    /**
     * Method returns a copy of this property. New property has the same value as original,
     * but it is but not linked with the original property.
     *
     * @return a copy of this property
     */
    Property<T> clone();

    /**
     * Method returns read-only version of this property. If property is already
     * read-only, it is returned as is.
     *
     * @return read-only property
     */
    default Property<T> asReadOnly() {
        if (isReadOnly()) return this;
        else return new ReadOnlyProperty<>(this);
    }
}

class ReadOnlyProperty<T> implements Property<T> {
    Property<T> property;

    public ReadOnlyProperty(Property<T> property) {
        this.property = property;
    }

    @Override
    public Class<T> type() {
        return property.type();
    }

    @Override
    public String name() {
        return property.name();
    }

    @Override
    public T get() {
        return property.get();
    }

    @Override
    public <R> R get(Function<T, R> mapper) {
        return property.get(mapper);
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void set(Object value) {
        throw new UnsupportedOperationException("unmodifiable property");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Property<T> clone() {
        ReadOnlyProperty<T> clone;
        try {
            clone = (ReadOnlyProperty<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            //should never happen
            throw new RuntimeException(e);
        }
        clone.property = property.clone();
        return clone;
    }
}

class ReadOnlyMappedProperty<T1, T2> implements Property<T2> {
    final Class<T2> type;
    Property<T1> property;
    final Function<T1, T2> forward;

    ReadOnlyMappedProperty(Class<T2> type, Property<T1> property, Function<T1, T2> forward) {
        this.type = type;
        this.property = property;
        this.forward = forward;
    }

    @Override
    public Class<T2> type() {
        return type;
    }

    @Override
    public String name() {
        return property.name();
    }

    @Override
    public T2 get() {
        return forward.apply(property.get());
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void set(Object value) {
        throw new UnsupportedOperationException("unmodifiable property");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Property<T2> clone() {
        ReadOnlyMappedProperty<T1, T2> clone;
        try {
            clone = (ReadOnlyMappedProperty<T1, T2>) super.clone();
        } catch (CloneNotSupportedException e) {
            //should never happen
            throw new RuntimeException(e);
        }
        clone.property = property.clone();
        return clone;
    }
}

class WritableMappedProperty<T1, T2> extends ReadOnlyMappedProperty<T1, T2> {
    final Function<T2, T1> backward;

    WritableMappedProperty(Class<T2> type, Property<T1> property, Function<T1, T2> forward, Function<T2, T1> backward) {
        super(type, property, forward);
        this.backward = backward;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void set(Object value) {
        if (type.isInstance(value)) {
            property.set(backward.apply(type.cast(value)));
        }
    }
}
