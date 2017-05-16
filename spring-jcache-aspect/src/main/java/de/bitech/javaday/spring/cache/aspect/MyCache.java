/**
 * 
 */
package de.bitech.javaday.spring.cache.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker f&uuml;r Cache
 * 
 * @author awi
 * @since 03.07.2016
 * @version 0.0.1-SNAPSHOT
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface MyCache {

}
