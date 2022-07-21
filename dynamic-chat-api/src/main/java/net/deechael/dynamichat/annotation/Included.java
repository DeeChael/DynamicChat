package net.deechael.dynamichat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When a method requires two number parameter to set a range,
 * this annotation can introduce whether the number will be included
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PARAMETER})
public @interface Included {
}
