package codechicken.packager;

public @interface SrcPackager
{
    /**
     * 
     * @return the Name of the Package
     */
    String getName() default "@Packager";
    /**
     * 
     * @return a list of all packages containing src
     * Eg. codechicken.core or codechicken.nei.ItemInfo
     */
    String[] getClasses() default "@Packager";
    /**
     * 
     * @return A list of src directories with paths relative to the mods folder
     * These should be the source folders you have linked in eclipse
     * Suffixing with ";subdir" will put them in that dir within the zip
     * Eg. {"CodeChickenCore/Client;client", "CodeChickenCore/Server;server", "CodeChickenCore/Common;common"}
     */
    String[] getMappedDirectories() default "@Packager";
    
}
