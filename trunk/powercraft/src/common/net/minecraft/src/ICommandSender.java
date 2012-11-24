package net.minecraft.src;

public interface ICommandSender
{
    String getCommandSenderName();

    void sendChatToPlayer(String var1);

    boolean canCommandSenderUseCommand(int var1, String var2);

    String translateString(String var1, Object ... var2);

    ChunkCoordinates getPlayerCoordinates();
}
