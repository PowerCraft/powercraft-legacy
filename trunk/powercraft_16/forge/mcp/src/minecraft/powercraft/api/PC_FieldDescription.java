package powercraft.api;

/**
 * 
 * Maks fields wich should be saved, synced with client
 * 
 * @author XOR
 *
 */
public @interface PC_FieldDescription {

	/**
	 * 
	 * The name of the field, when nothing, the real name will be used
	 * 
	 * @return overriden name of the field
	 */
	public String name() default "";
	
	/**
	 * 
	 * If the name of the field changed you can give here other names to search fore
	 * 
	 * @return other names
	 */
	public String[] otherNames() default {};
	
	/**
	 * 
	 * Should this field be saved when the world is be saved
	 * 
	 * @return indicates if it sould be saved
	 */
	public boolean save() default true;
	
	/**
	 * 
	 * Should this field be synced with the clients
	 * 
	 * @return indicates if it sould be synced
	 */
	public boolean sync() default false;
	
	/**
	 * 
	 * Should this field be synced with the clients which have the gui open
	 * 
	 * @return indicates if it sould be synced when the gui is open
	 */
	public boolean guiSync() default true;
	
}
