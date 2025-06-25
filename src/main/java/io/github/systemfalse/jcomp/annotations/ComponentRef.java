package io.github.systemfalse.jcomp.annotations;

import java.lang.annotation.*;

/**
 * Indicates that class is component definition. Annotated class will be used
 * to create implementation of {@link io.github.systemfalse.jcomp.Component}.
 * To define property of the component, use {@link PropertyRef} annotation.
 * To define action of the component, use {@link ActionRef} annotation.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface ComponentRef {
    /**
     * Sets name of the component implementation. Supported strings are:
     * <ul>
     *     <li>ClassName - generated class will be put in the same package</li>
     *     <li>package_name.ClassName - generated class will be put in the same module</li></li>
     * </ul>
     * By default, it is expected that annotated class is named accordingly to this regex:
     * {@code (?<component>[a-zA-Z_$][\w$]*)Component}, where group {@code component} is
     * generated component class name.
     * @return component name
     */
    String component() default "";
}
