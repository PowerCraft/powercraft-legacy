/**
 * 
 */
package powercraft.api;

import java.lang.reflect.Array;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

/**
 * @author Aaron
 *
 */
public class PC_NBTTagHandler {

	public static void saveToNBT(NBTTagCompound nbtTagCompound, String name, Object value){
		NBTBase base = getObjectNBT(value);
		if(base!=null)
			nbtTagCompound.setTag(name, base);
	}
	
	public static NBTBase getObjectNBT(Object value){
		if(value==null)
			return null;
		Class<?> c = value.getClass();
		if(c==Boolean.class){
			NBTTagByte b = new NBTTagByte("");
			b.data = (byte)((Boolean)value?1:0);
			return b;
		}else if(c==Byte.class){
			NBTTagByte b = new NBTTagByte("");
			b.data = (Byte)value;
			return b;
		}else if(c==Short.class){
			NBTTagShort s = new NBTTagShort("");
			s.data = (Short)value;
			return s;
		}else if(c==Integer.class){
			NBTTagInt i = new NBTTagInt("");
			i.data = (Integer)value;
			return i;
		}else if(c==Long.class){
			NBTTagLong l = new NBTTagLong("");
			l.data = (Long)value;
			return l;
		}else if(c==Float.class){
			NBTTagFloat f = new NBTTagFloat("");
			f.data = (Float)value;
			return f;
		}else if(c==Double.class){
			NBTTagDouble d = new NBTTagDouble("");
			d.data = (Double)value;
			return d;
		}else if(c==String.class){
			NBTTagString s = new NBTTagString("");
			s.data = (String)value;
			return s;
		}else if(c==int[].class){
			NBTTagIntArray a = new NBTTagIntArray("");
			a.intArray = (int[])value;
			return a;
		}else if(c==byte[].class){
			NBTTagByteArray a = new NBTTagByteArray("");
			a.byteArray = (byte[])value;
			return a;
		}else if(c.isArray()){
			NBTTagList list = new NBTTagList();
			int size = Array.getLength(value);
			for(int i=0; i<size; i++){
				Object obj = Array.get(value, i);
				NBTBase base = getObjectNBT(obj);
				if(base==null){
					base = new NBTTagCompound();
				}
				list.appendTag(base);
			}
			return list;
		}else if(c.isAssignableFrom(PC_INBT.class)){
			NBTTagCompound tag = new NBTTagCompound(c.getName());
			((PC_INBT)value).saveToNBT(tag);
			return tag;
		}
		return null;
	}
	
	public static Object loadFromNBT(NBTTagCompound nbtTagCompound, String name, Class<?> c){
		NBTBase base = nbtTagCompound.getTag(name);
		if(base==null)
			return null;
		return getObjectFromNBT(base, c);
	}

	public static Object getObjectFromNBT(NBTBase base, Class<?> c) {
		if(base instanceof NBTTagCompound){
			if(((NBTTagCompound)base).getName().equals("")){
				return null;
			}
		}
		if(c==Boolean.class || c==boolean.class){
			return ((NBTTagByte)base).data!=0;
		}else if(c==Byte.class || c==byte.class){
			return ((NBTTagByte)base).data;
		}else if(c==Short.class || c==short.class){
			return ((NBTTagShort)base).data;
		}else if(c==Integer.class || c==int.class){
			return ((NBTTagInt)base).data;
		}else if(c==Long.class || c==long.class){
			return ((NBTTagLong)base).data;
		}else if(c==Float.class || c==float.class){
			return ((NBTTagFloat)base).data;
		}else if(c==Double.class || c==double.class){
			return ((NBTTagDouble)base).data;
		}else if(c==String.class){
			return ((NBTTagString)base).data;
		}else if(c==int[].class){
			return ((NBTTagIntArray)base).intArray;
		}else if(c==byte[].class){
			return ((NBTTagByteArray)base).byteArray;
		}else if(c.isArray()){
			NBTTagList list = (NBTTagList) base;
			int size = list.tagCount();
			Class<?> ac = c.getDeclaringClass();
			Object array = Array.newInstance(ac, size);
			for(int i=0; i<size; i++){
				NBTBase obj = list.tagAt(i);
				Array.set(obj, i, getObjectFromNBT(base, ac));
			}
			return array;
		}else if(c.isAssignableFrom(PC_INBT.class)){
			
		}
		return null;
	}
	
}
