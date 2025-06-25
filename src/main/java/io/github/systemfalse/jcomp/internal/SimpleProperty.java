package io.github.systemfalse.jcomp.internal;

import com.google.common.base.Defaults;
import io.github.systemfalse.jcomp.Property;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class SimpleProperty<T> implements Property<T> {
    private final Class<T> type;
    private final String name;
    private T value;
    private final UnaryOperator<T> cloneFunction;

    public SimpleProperty(Class<T> type, String name) {
        this(type, name, Defaults.defaultValue(type));
    }

    public SimpleProperty(Class<T> type, String name, Object initialValue) {
        this(type, name, initialValue, null);
    }

    public SimpleProperty(Class<T> type, String name, Object initialValue, UnaryOperator<T> cloneFunction) {
        this.type = Objects.requireNonNull(type);
        this.name = Objects.requireNonNull(name);
        if (!type.isInstance(initialValue)) {
            throw new IllegalArgumentException("default value is not instance of class " + type.getCanonicalName());
        }
        this.value = type.cast(initialValue);
        this.cloneFunction = cloneFunction != null ? cloneFunction : UnaryOperator.identity();
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void set(Object value) {
        if (type.isInstance(value)) {
            this.value = type.cast(value);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public SimpleProperty<T> clone() {
        SimpleProperty<T> clone;
        try {
            clone = (SimpleProperty<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            //should never happen
            throw new RuntimeException(e);
        }
        clone.value = cloneFunction.apply(value);
        return clone;
    }
}
