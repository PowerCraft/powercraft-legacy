package powercraft.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface PC_IPacketHandler {

	public void handlePacket(World world, EntityPlayer entityPlayer, NBTTagCompound packet);
	
}
