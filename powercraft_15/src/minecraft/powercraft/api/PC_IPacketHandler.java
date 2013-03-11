package powercraft.api;

import net.minecraft.entity.player.EntityPlayer;

public interface PC_IPacketHandler {

	boolean handleIncomingPacket(EntityPlayer player, Object[] o);

}
