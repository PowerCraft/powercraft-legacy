package cpw.mods.fml.common;

public interface INetworkHandler
{
    boolean onChat(Object... data);
    void onPacket250Packet(Object... data);
    void onServerLogin(Object handler);
}
