package codechicken.core.asm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import cpw.mods.fml.relauncher.IClassTransformer;
import cpw.mods.fml.relauncher.RelaunchClassLoader;

public class DelegatedTransformer implements IClassTransformer
{
    private static ArrayList<IClassTransformer> delegatedTransformers;
    private static Method m_defineClass;
    private static Field f_cachedClasses;
    
    public DelegatedTransformer()
    {
        delegatedTransformers = new ArrayList<IClassTransformer>();
        try
        {
            m_defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
            m_defineClass.setAccessible(true);
            f_cachedClasses = RelaunchClassLoader.class.getDeclaredField("cachedClasses");
            f_cachedClasses.setAccessible(true);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public byte[] transform(String name, byte[] bytes)
    {
        for(IClassTransformer trans : delegatedTransformers)
            bytes = trans.transform(name, bytes);
        return bytes;
    }

    public static void addTransformer(String transformer, JarFile jar, File jarFile)
    {
        try
        {
            byte[] bytes = null;
            bytes = CodeChickenCorePlugin.cl.getClassBytes(transformer);
            
            if(bytes == null)
            {            
                String resourceName = transformer.replace('.', '/')+".class";
                ZipEntry entry = jar.getEntry(resourceName);
                if(entry == null)
                    throw new Exception("Failed to add transformer: "+transformer+". Entry not found in jar file "+jarFile.getName());
                
                bytes = readFully(jar.getInputStream(entry));                
            }
            
            Class<?> clazz = (Class<?>) m_defineClass.invoke(CodeChickenCorePlugin.cl, transformer, bytes, 0, bytes.length);            
            ((Map<String, Class>)f_cachedClasses.get(CodeChickenCorePlugin.cl)).put(transformer, clazz);
            
            if(!IClassTransformer.class.isAssignableFrom(clazz))
                throw new Exception("Failed to add transformer: "+transformer+" is not an instance of IClassTransformer");
            delegatedTransformers.add((IClassTransformer) clazz.newInstance());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public static byte[] readFully(InputStream stream) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(stream.available());
        int r;
        while ((r = stream.read()) != -1)
        {
            bos.write(r);
        }

        return bos.toByteArray();
    }
}
