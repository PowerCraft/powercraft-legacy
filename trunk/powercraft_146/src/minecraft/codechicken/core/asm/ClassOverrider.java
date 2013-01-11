package codechicken.core.asm;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import codechicken.core.asm.ObfuscationManager.ClassMapping;

public class ClassOverrider
{
	public static byte[] overrideBytes(String name, byte[] bytes, ClassMapping classMapping, File location)
	{
		if(!name.equals(classMapping.classname) || !ObfuscationManager.obfuscated)
			return bytes;
		
		try
		{
			ZipFile zip = new ZipFile(location);
			ZipEntry entry = zip.getEntry(name.replace('.', '/')+".class");
			if(entry == null)
				System.out.println(name+" not found in "+location.getName());
			else
			{
				InputStream zin = zip.getInputStream(entry);
				bytes = new byte[(int) entry.getSize()];
				zin.read(bytes);
				zin.close();
				System.out.println(name+" was overriden from "+location.getName());
			}
			zip.close();
		}
		catch(Exception e)
		{
			throw new RuntimeException("Error overriding "+name+" from "+location.getName(), e);
		}
		return bytes;
	}
}

	
