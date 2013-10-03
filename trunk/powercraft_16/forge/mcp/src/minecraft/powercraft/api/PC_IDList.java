package powercraft.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.SOURCE)
public @interface PC_IDList {

	public PC_IDElement[] elements();

	@Target({})
	@Retention(RetentionPolicy.SOURCE)
	public @interface PC_IDElement{

		public int id();
		public Class<?> clazz();
	}
}

