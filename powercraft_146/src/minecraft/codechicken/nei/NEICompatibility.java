package codechicken.nei;

import codechicken.core.ReflectionManager;

public class NEICompatibility
{	
	public static boolean hasForge = ReflectionManager.classExists("net.minecraftforge.common.MinecraftForge");
}
