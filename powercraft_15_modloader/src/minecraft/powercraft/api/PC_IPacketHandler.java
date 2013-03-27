package powercraft.api;

import net.minecraft.src.EntityPlayer;

public interface PC_IPacketHandler {

	boolean handleIncomingPacket(EntityPlayer player, Object[] o);

}
