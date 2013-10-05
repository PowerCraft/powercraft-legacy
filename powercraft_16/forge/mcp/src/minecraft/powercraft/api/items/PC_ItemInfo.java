package powercraft.api.items;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PC_ItemInfo {

	public String name();


	public String itemid();


	public int defaultid();
	
}
