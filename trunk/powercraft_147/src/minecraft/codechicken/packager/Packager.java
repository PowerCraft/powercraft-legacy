package codechicken.packager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public @Retention(RetentionPolicy.RUNTIME) @interface Packager
{
	/**
	 * 
	 * @return the Name of the Package
	 */
	String getName();
	/**
	 * 
	 * @return the Current Version of the mod
	 */
	String getVersion() default "@Mod";
	/**
	 * 
	 * @return a list of all packages containing src
	 * Eg. codechicken.core or codechicken.nei.ItemInfo
	 */
	String[] getClasses() default {""};
	/**
	 * 
	 * @return A list of src directories with paths relative to the mods folder
	 * These should be the source folders you have linked in eclipse
	 */
	String[] getBaseDirectories();
	/**
	 * 
	 * @return a list of packages or classes that are to be forcefully included (may not be in directory)
	 */
	String[] getForcedClasses() default {""};
	/**
	 * 
	 * @return A subdirectory in the zip to put editied notch code in. 
	 * Eg. For runtime patching 
	 */
	String getObfRelocationDir() default "";
}
