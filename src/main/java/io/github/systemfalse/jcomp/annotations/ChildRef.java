package io.github.systemfalse.jcomp.annotations;

import java.lang.annotation.*;

/**
 * Indicates that field is component child. Annotated field have type
 * of {@link io.github.systemfalse.jcomp.Component} or any of its subtypes.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.RECORD_COMPONENT})
@Documented
public @interface ChildRef {
    /**
     * Defines type of child component. If field has common type (like {@code
     * Component}), this value is used to initialize child component.
     *
     * @return child component type
     */
    String type() default "";
}
