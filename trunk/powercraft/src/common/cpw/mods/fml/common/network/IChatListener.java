package cpw.mods.fml.common.network;

import net.minecraft.src.NetHandler;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet3Chat;

public interface IChatListener
{
    public Packet3Chat serverChat(NetHandler handler, Packet3Chat message);

    public Packet3Chat clientChat(NetHandler handler, Packet3Chat message);
}
