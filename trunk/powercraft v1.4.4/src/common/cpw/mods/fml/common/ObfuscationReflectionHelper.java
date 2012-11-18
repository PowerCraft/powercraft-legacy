package cpw.mods.fml.common;

import java.util.Arrays;
import java.util.logging.Level;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToFindFieldException;

public class ObfuscationReflectionHelper
{
    public static boolean obfuscation;

    @SuppressWarnings("unchecked")
    public static <T, E> T getPrivateValue(Class <? super E > classToAccess, E instance, int fieldIndex)
    {
        try
        {
            return ReflectionHelper.getPrivateValue(classToAccess, instance, fieldIndex);
        }
        catch (UnableToAccessFieldException e)
        {
            FMLLog.log(Level.SEVERE, e, "There was a problem getting field index %d from %s", fieldIndex, classToAccess.getName());
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, E> T getPrivateValue(Class <? super E > classToAccess, E instance, String... fieldNames)
    {
        try
        {
            return ReflectionHelper.getPrivateValue(classToAccess, instance, fieldNames);
        }
        catch (UnableToFindFieldException e)
        {
            FMLLog.log(Level.SEVERE, e, "Unable to locate any field %s on type %s", Arrays.toString(fieldNames), classToAccess.getName());
            throw e;
        }
        catch (UnableToAccessFieldException e)
        {
            FMLLog.log(Level.SEVERE, e, "Unable to access any field %s on type %s", Arrays.toString(fieldNames), classToAccess.getName());
            throw e;
        }
    }

    @Deprecated
    public static <T, E> void setPrivateValue(Class <? super T > classToAccess, T instance, int fieldIndex, E value)
    {
        setPrivateValue(classToAccess, instance, value, fieldIndex);
    }

    public static <T, E> void setPrivateValue(Class <? super T > classToAccess, T instance, E value, int fieldIndex)
    {
        try
        {
            ReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldIndex);
        }
        catch (UnableToAccessFieldException e)
        {
            FMLLog.log(Level.SEVERE, e, "There was a problem setting field index %d on type %s", fieldIndex, classToAccess.getName());
            throw e;
        }
    }

    @Deprecated
    public static <T, E> void setPrivateValue(Class <? super T > classToAccess, T instance, String fieldName, E value)
    {
        setPrivateValue(classToAccess, instance, value, fieldName);
    }

    public static <T, E> void setPrivateValue(Class <? super T > classToAccess, T instance, E value, String... fieldNames)
    {
        try
        {
            ReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldNames);
        }
        catch (UnableToFindFieldException e)
        {
            FMLLog.log(Level.SEVERE, e, "Unable to locate any field %s on type %s", Arrays.toString(fieldNames), classToAccess.getName());
            throw e;
        }
        catch (UnableToAccessFieldException e)
        {
            FMLLog.log(Level.SEVERE, e, "Unable to set any field %s on type %s", Arrays.toString(fieldNames), classToAccess.getName());
            throw e;
        }
    }

    public static void detectObfuscation(Class<?> clazz)
    {
        obfuscation = !clazz.getSimpleName().equals("World");
    }
}
