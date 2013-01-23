package codechicken.core.asm;

import java.util.HashMap;
import java.util.HashSet;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import cpw.mods.fml.relauncher.IClassTransformer;

public class ClassHeirachyManager implements IClassTransformer
{
	public static HashSet<String> knownClasses = new HashSet<String>();
	public static HashMap<String, String> superclasses = new HashMap<String, String>();
	
	/**
	 * Returns true if clazz extends, either directly or indirectly, superclass.
	 * @param clazz The class in question
	 * @param superclass The class being extended
	 * @param bytes The bytes for the clazz. Only needed if not already defined.
	 * @return
	 */
	public static boolean classExtends(String clazz, String superclass, byte[] bytes)
	{
		if(!knownClasses.contains(clazz))
		{
			new ClassHeirachyManager().transform(clazz, bytes);
		}
		
		return classExtends(clazz, superclass);
	}
	
	private static boolean classExtends(String clazz, String superclass)
	{		
		if(clazz.equals(superclass))
			return true;
		
		if(clazz.equals("java.lang.Object"))
			return false;
		
		try
		{
			if(!knownClasses.contains(clazz))
			{
				try
				{
					byte[] bytes = CodeChickenCorePlugin.cl.getClassBytes(clazz);
					if(bytes != null)
						new ClassHeirachyManager().transform(clazz, bytes);
				}
				catch(Exception e)
				{
				}
				
				if(!knownClasses.contains(clazz))
				{
					Class<?> aclass = Class.forName(clazz);
					
					knownClasses.add(clazz);
					superclasses.put(clazz, aclass.getSuperclass().getName());
				}
			}
		}
		catch(ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		
		if(!superclasses.containsKey(clazz))//just can't handle this
			return false;
		
		return classExtends(superclasses.get(clazz), superclass);
	}

	@Override
	public byte[] transform(String name, byte[] bytes)
	{
		if(!knownClasses.contains(name))
		{		
			ClassNode node = new ClassNode();
			ClassReader reader = new ClassReader(bytes);
			reader.accept(node, 0);
			
			knownClasses.add(name);
			superclasses.put(name, node.superName.replace('/', '.'));
		}
		
		return bytes;
	}
}
