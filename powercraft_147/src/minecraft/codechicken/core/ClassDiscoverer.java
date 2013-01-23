package codechicken.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.relauncher.RelaunchLibraryManager;

public class ClassDiscoverer
{
	public IStringMatcher matcher;
	public Class<?>[] superclasses;
	public ArrayList<Class<?>> classes;
	public ModClassLoader modClassLoader;
	
	public ClassDiscoverer(IStringMatcher matcher, Class<?>... superclasses) 
	{
		this.matcher = matcher;
		this.superclasses = superclasses;
		
		classes = new ArrayList<Class<?>>();
		modClassLoader = (ModClassLoader)Loader.instance().getModClassLoader();
	}	
	
	public ArrayList<Class<?>> findClasses()
	{		
        try
		{
        	findClasspathMods();
		}
        catch(Exception e)
		{
        	FMLCommonHandler.instance().raiseException(e, "Code Chicken Core", true);
		}
        return classes;
	}

	private void addClass(String resource)
	{
		try
        {
			String classname = resource.replace(".class", "").replace("\\", ".").replace("/", ".");
            Class<?> class1 = Class.forName(classname, true, modClassLoader);
            for(Class<?> superclass : superclasses)
                if(!superclass.isAssignableFrom(class1))
                    return;
            
            classes.add(class1);
        }
        catch(Exception cnfe)
        {
           	System.err.println("Unable to load class: "+resource);
           	cnfe.printStackTrace();
        }
	}
	
	private void findClasspathMods() throws FileNotFoundException, IOException
    {
		List<String> knownLibraries = ImmutableList.<String>builder().addAll(modClassLoader.getDefaultLibraries()).addAll(RelaunchLibraryManager.getLibraries()).build();
        File[] minecraftSources = modClassLoader.getParentSources();
        HashSet<String> searchedSources = new HashSet<String>();
        for (File minecraftSource : minecraftSources)
        {
        	if(searchedSources.contains(minecraftSource.getAbsolutePath()))
        		continue;
        	searchedSources.add(minecraftSource.getAbsolutePath());
        	
            if (minecraftSource.isFile())
            {
                if (!knownLibraries.contains(minecraftSource.getName()))
                {
                    FMLLog.fine("Found a minecraft related file at %s, examining for codechicken classes", minecraftSource.getAbsolutePath());
                    readFromZipFile(minecraftSource);
                }
            }
            else if (minecraftSource.isDirectory())
            {
                FMLLog.fine("Found a minecraft related directory at %s, examining for codechicken classes", minecraftSource.getAbsolutePath());
                readFromDirectory(minecraftSource, minecraftSource);
            }
        }
    }
	
	private void readFromZipFile(File file) throws IOException
	{
		FileInputStream fileinputstream = new FileInputStream(file);
        ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
        do
        {
            ZipEntry zipentry = zipinputstream.getNextEntry();
            if(zipentry == null)
            {
                break;
            }
            String fullname = zipentry.getName().replace('\\', '/');
            int pos = fullname.lastIndexOf('/');
            String name = pos == -1 ? fullname : fullname.substring(pos+1);
            if(!zipentry.isDirectory() && matcher.matches(name))
            {
            	addClass(fullname);
            }
        } 
        while(true);
        fileinputstream.close();
	}

	private void readFromDirectory(File directory, File basedirectory)
	{
		for(File child : directory.listFiles())
		{
			if(child.isDirectory())
			{
				readFromDirectory(child, basedirectory);
			}
			else if(child.isFile() && matcher.matches(child.getName()))
			{
				String fullname = CommonUtils.getRelativePath(basedirectory, child);
            	//System.out.println("Attempting load of codechicken related file: "+fullname+" in "+child.getPath());
				addClass(fullname);
			}
		}
	}
}
