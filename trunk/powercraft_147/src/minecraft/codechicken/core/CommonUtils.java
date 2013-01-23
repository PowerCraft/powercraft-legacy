package codechicken.core;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.block.Block;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommonUtils
{
	private static File minecraftDir;
	
	public static boolean isClient()
	{
		return FMLCommonHandler.instance().getSide().isClient();
	}
	
	public static File getWorldBaseSaveLocation(World world)
	{
		File savedir = getWorldSaveLocation(world);
		if(savedir == null)
		{
			return null;
		}
		else if(savedir.getName().contains("DIM"))
		{
			return savedir.getParentFile();
		}
		else
		{
			return savedir;
		}
	}
	
	public static File getWorldSaveLocation(World world, int dimension)
	{
		File basesave = getWorldBaseSaveLocation(world); 
		if(dimension != 0)
		{			
			return new File(basesave, world.provider.getSaveFolder());
		}
		else
		{
			return basesave;
		}
	}
	
	private static File getWorldSaveLocation(World world)
	{
		try
		{
			ISaveHandler worldsaver = world.getSaveHandler();
			IChunkLoader loader = worldsaver.getChunkLoader(world.provider);
			if(loader instanceof AnvilChunkLoader)
			{
				return ((AnvilChunkLoader)loader).chunkSaveLocation;
			}
			return null;
		}
		catch(IllegalAccessError e)
		{
			return ((WorldServer)world).getChunkSaveLocation();
		}
		catch(Exception e)
		{
            FMLCommonHandler.instance().raiseException(e, "Code Chicken Core", true);
			return null;
		}
	}
	
	public static String getWorldName(World world)
	{
		return world.getWorldInfo().getWorldName();
	}
	
	public static int getDimension(World world)
	{
		return world.provider.dimensionId;
	}
	
	public static File getModsFolder()
	{
		return new File(getMinecraftDir(), "mods");
	}
	
	public static File getMinecraftDir()
	{
		if(minecraftDir == null)
			minecraftDir = ReflectionManager.getField(Loader.class, File.class, Loader.instance(), "minecraftDir");
		
		return minecraftDir;
	}		

	public static String getRelativePath(File parent, File child)
	{
		if(parent.isFile() || !child.getPath().startsWith(parent.getPath()))
		{
			return null;
		}
		return child.getPath().substring(parent.getPath().length() + 1);
	}
	
	public static int getFreeBlockID(int preferred)
	{
		for(int i = preferred; i < 255; i++)
		{
			if(Block.blocksList[i] == null)
			{
				return i;
			}
		}
		for(int i = preferred - 1; i > 0; i--)
		{
			if(Block.blocksList[i] == null)
			{
				return i;
			}
		}
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] subArray(T[] args, int i)
	{
		if(i > args.length)			
			return (T[]) Array.newInstance(args.getClass().getComponentType(), 0);
		
		T[] narray = (T[]) Array.newInstance(args.getClass().getComponentType(), args.length-i);
		System.arraycopy(args, i, narray, 0, narray.length);
		return narray;
	}
	
	private static byte[] charWidth = new byte[]{4, 2, 5, 6, 6, 6, 6, 3, 5, 5, 5, 6, 2, 6, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 5, 6, 5, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 4, 6, 6, 3, 6, 6, 6, 6, 6, 5, 6, 6, 2, 6, 5, 3, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 5, 2, 5, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 3, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 3, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 2, 6, 6};
	
    public static int getCharWidth(char c)
    {
        if (c == 167)
            return -1;
        else
        {
            int charIndex = ChatAllowedCharacters.allowedCharacters.indexOf(c);
            if(charIndex + 32 > charWidth.length || charIndex < 0)
            	return 0;
            return charWidth[charIndex + 32];
        }
    }
	
	public static int getStringWidth(String s)
	{
        if (s == null)
        {
            return 0;
        }
        else
        {
            int width = 0;
            boolean var3 = false;

            for (int charIndex = 0; charIndex < s.length(); ++charIndex)
            {
                char c = s.charAt(charIndex);
                int charWidth = getCharWidth(c);

                if (charWidth < 0 && charIndex < s.length() - 1)
                {
                    ++charIndex;
                    c = s.charAt(charIndex);

                    if (c != 108 && c != 76)
                    {
                        if (c == 114 || c == 82)
                        {
                            var3 = false;
                        }
                    }
                    else
                    {
                        var3 = true;
                    }

                    charWidth = getCharWidth(c);
                }

                width += charWidth;

                if (var3)
                {
                    ++width;
                }
            }

            return width;
        }
	}
	
	public static List<String> formatMessage(String message)
	{
		LinkedList<String> splitNotice = new LinkedList<String>();
		String[] splits = message.split(" ");
		String partial = "";
		int colour = 7;
		for(int i = 0; i < splits.length; i++)
		{
			String next = partial.length() == 0 ? splits[i] : partial+" "+splits[i];
			if(getStringWidth(next) > 377)
			{
				splitNotice.add(colourPrefix(colour)+partial);
	            for(int charPos = 0; charPos < partial.length(); charPos++)
	            {
		            for(; partial.length() > charPos + 1 && partial.charAt(charPos) == '\247'; charPos++)
					{
					    char c = partial.toLowerCase().charAt(charPos + 1);
					    if(c == 'k')
					    {
					        continue;
					    }
					    colour = "0123456789abcdef".indexOf(c);
					    if(colour < 0 || colour > 15)
					    {
					        colour = 15;
					    }
					}
	            }
				
				partial = splits[i];
	            	            
	            continue;
			}
			partial = next;
		}
		splitNotice.add(colourPrefix(colour)+partial);
		
		return splitNotice;
	}
	
	public static String colourPrefix(int colour)
	{
		if(colour == -1)
			return "";
		return "\247"+"0123456789abcdef".charAt(colour);
	}

	private static Field mystMapDimension;
	static
	{
		try
		{
			mystMapDimension = MapData.class.getDeclaredField("dimension_myst");
		}
		catch(Exception e)
		{}
	}
	
	/**
	 * Handles Mystcraft Dimensions :D
	 */
	public static int getDimension(MapData mapdata)
	{		
		if(mystMapDimension != null)
		{
			try
			{
				return mystMapDimension.getInt(mapdata);
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			return mapdata.dimension;
		}
	}

	/**
	 * Handles Mystcraft Dimensions :D
	 */
	public static void setDimension(MapData mapdata, int dimension)	
	{	
		if(mystMapDimension != null)
		{
			try
			{
				mystMapDimension.setInt(mapdata, dimension);
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			mapdata.dimension = (byte) dimension;
		}
	}
	
	public static boolean isBlock(int ID)
	{
		return ID < Block.blocksList.length && Block.blocksList[ID] != null && Block.blocksList[ID].blockID != 0;
	}

    public static ModContainer findModContainer(String modID)
    {
        for(ModContainer mc : Loader.instance().getModList())
            if(modID.equals(mc.getModId()))
                return mc;
        
        return null;
    }
}
