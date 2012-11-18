package powercraft.core;

import net.minecraft.src.EntityPlayer;

public interface PC_IPacketHandler
{
    public boolean handleIncomingPacket(EntityPlayer player, Object[]o);
}
