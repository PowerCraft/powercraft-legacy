/**
 * 
 */
package powercraft.api;

/**
 * @author Aaron
 *
 */
public @interface PC_FieldDescription {

	public String name() default "";
	public String[] otherNames() default {};
	public boolean save() default true;
	public boolean sync() default false;
	public boolean guiSync() default true;
	
}
