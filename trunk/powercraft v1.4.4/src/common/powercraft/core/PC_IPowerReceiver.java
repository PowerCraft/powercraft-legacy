package powercraft.core;

import net.minecraft.src.World;

public interface PC_IPowerReceiver {

	public void receivePower(World world, int x, int y, int z, float power);
	
}
