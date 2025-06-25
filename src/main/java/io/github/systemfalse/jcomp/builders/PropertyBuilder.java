package io.github.systemfalse.jcomp.builders;

import io.github.systemfalse.jcomp.Property;
import io.github.systemfalse.jcomp.internal.SimpleProperty;

import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class PropertyBuilder<T> {
    private static final Pattern PROPERTY_REGEX = Pattern.compile("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");

    private Class<T> type;
    private String name;
    private T initialValue;
    private UnaryOperator<T> cloneFunction = UnaryOperator.identity();

    private boolean built;
    private Property<T> instance;

    PropertyBuilder() {

    }

    void checkBuilt() {
        if (built) {
            throw new IllegalStateException("property was built and can not be modified");
        }
    }

    public PropertyBuilder<T> setType(Class<T> type) {
        checkBuilt();
        Objects.requireNonNull(type, "type");
        this.type = type;
        return this;
    }

    public PropertyBuilder<T> setName(String name) {
        checkBuilt();
        Objects.requireNonNull(name, "name");
        if (!PROPERTY_REGEX.matcher(name).matches()) {
            throw new IllegalArgumentException("name is not valid identifier");
        }
        this.name = name;
        return this;
    }

    public PropertyBuilder<T> setInitialValue(T initialValue) {
        checkBuilt();
        Objects.requireNonNull(initialValue, "initialValue");
        this.initialValue = initialValue;
        return this;
    }

    public PropertyBuilder<T> setCloneFunction(UnaryOperator<T> cloneFunction) {
        checkBuilt();
        this.cloneFunction = cloneFunction;
        return this;
    }

    public Property<T> confirm() {
        if (!built) {
            if (type == null) {
                throw new IllegalStateException("property class was not set");
            }
            if (name == null) {
                throw new IllegalStateException("property name was not set");
            }
            if (initialValue != null && !type.isInstance(initialValue)) {
                throw new IllegalStateException("default value is not instance of class " + type.getCanonicalName());
            }
            instance = new SimpleProperty<>(type, name, initialValue, cloneFunction);
            built = true;
        }
        return instance;
    }
}
