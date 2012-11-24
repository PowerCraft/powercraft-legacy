package cpw.mods.fml.common.network;

import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet250CustomPayload;

public interface IPacketHandler
{
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player);
}
