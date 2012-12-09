package powercraft.management;

import net.minecraft.src.EntityPlayer;

public interface PC_IPacketHandler {

	boolean handleIncomingPacket(EntityPlayer player, Object[] o);

}
