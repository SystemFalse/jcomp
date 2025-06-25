package io.github.systemfalse.jcomp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a field is component property. THe initial value
 * of property will be the same as the value of the field.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.RECORD_COMPONENT})
public @interface PropertyRef {

}
