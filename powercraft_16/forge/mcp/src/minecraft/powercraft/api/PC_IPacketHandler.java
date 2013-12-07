package powercraft.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * 
 * Packethandler for packet messeging
 * 
 * @author XOR
 *
 */
public interface PC_IPacketHandler {

	/**
	 * will be call if a packet for this handler arrives
	 * @param world the world in which the packet arrives
	 * @param entityPlayer the player the packet have sended
	 * @param packet the packet
	 */
	public void handlePacket(World world, EntityPlayer entityPlayer, NBTTagCompound packet);
	
}
