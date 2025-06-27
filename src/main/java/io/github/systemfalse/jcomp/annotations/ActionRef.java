package io.github.systemfalse.jcomp.annotations;

import java.lang.annotation.*;

/**
 * Indicates that a method is component action.
 * <p>
 * For using different type of parameters, methods must have different
 * presets.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ActionRef {
    /**
     * Sets index of method preset. Preset is a set of overloaded methods with
     * different type of parameters.
     * <p>
     * Example of methods in same preset:
     * <pre>{@code
     * public int max(int[] arr) {...}
     * public int max(int[] arr, int off, int len) {...}}</pre>
     * Example of methods in different presets:
     * <pre>{@code
     * @ActionRef(preset = 0)
     * public Color getColor(int x, int y) {...}
     * @ActionRef(preset = 1)
     * public Color getColor(Point point) {...}}</pre>
     * </p>
     * @return index of preset
     */
    int preset() default 0;
}
