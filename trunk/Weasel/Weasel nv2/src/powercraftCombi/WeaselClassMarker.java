package powercraftCombi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface WeaselClassMarker {
	public String weaselName() default "";
	
	@Target({ElementType.METHOD, ElementType.FIELD})
	public @interface Invisible{
	}
	
	@Target(ElementType.TYPE)
	public static @interface WeaselClassList{
		public Class<?>[] classes();
	}
}
