package io.github.systemfalse.jcomp.annotations;

import java.lang.annotation.*;

/**
 * Configures parameter usage in component actions.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER})
@Documented
public @interface ParameterRef {
    /**
     * Indicates that parameter is required. Required parameter must be set in
     * arguments.
     *
     * @return required
     */
    boolean required() default true;

    /**
     * Sets default value of the parameter. Only optional parameters can
     * have default value.
     *
     * @return default value
     */
    String defaultValue() default "";

    /**
     * Sets default value of the parameter received from field with modifiers
     * {@code public}, {@code static} and {@code final}.
     * Following strings are supported:
     * <ul>
     *     <li>{@code THIS_CLASS_FIELD_NAME}</li>
     *     <li>{@code package_name.ClassName.FIELD_NAME}</li>
     *     <li>{@code module_name/package_name.ClassName.FIELD_NAME}</li>
     * </ul>
     * @return constant name
     */
    String defaultValueConstant() default "";

    /**
     * Sets method that computes default value of the parameter. Method must be applicable
     * to {@code java.util.Function<List<Object>, Object>}, where argument is list of parameter
     * values before current parameter.
     * Following string are supported:
     * <ul>
     *     <li>{@code thisClassMethodName} - if method is not static, it will be copied to the component class</li>
     *     <li>{@code package_name.ClassName.methodName} - expects only {@code public static} methods</li>
     *     <li>{@code module_name/package_name.ClassName.methodName} - expects only {@code public static} methods</li>
     * </ul>
     * @return computed value
     */
    String defaultComputedValue() default "";
}
