package cpw.mods.fml.common.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetLoginHandler;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet1Login;

public interface IConnectionHandler
{
    void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager);

    String connectionReceived(NetLoginHandler netHandler, INetworkManager manager);

    void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager);

    void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager);

    void connectionClosed(INetworkManager manager);

    void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login);
}
