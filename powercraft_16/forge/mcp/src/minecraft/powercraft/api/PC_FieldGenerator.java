package powercraft.api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Used in Module classes for generating block instances, item instances and other instances
 * 
 * @author XOR
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PC_FieldGenerator {

	/**
	 * the class form which a instance should be created.
	 * if not specified, the class of the filed type will be used
	 * @return the class for the instance
	 */
	public Class<?> value() default void.class;

}
