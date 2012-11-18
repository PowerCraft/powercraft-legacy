package net.minecraft.src;

public class RConConsoleSource implements ICommandSender
{
    public static final RConConsoleSource consoleBuffer = new RConConsoleSource();
    private StringBuffer chatBuffer = new StringBuffer();

    public void resetLog()
    {
        this.chatBuffer.setLength(0);
    }

    public String getChatBuffer()
    {
        return this.chatBuffer.toString();
    }

    public String getCommandSenderName()
    {
        return "Rcon";
    }

    public void sendChatToPlayer(String par1Str)
    {
        this.chatBuffer.append(par1Str);
    }

    public boolean canCommandSenderUseCommand(int par1, String par2Str)
    {
        return true;
    }

    public String translateString(String par1Str, Object ... par2ArrayOfObj)
    {
        return StringTranslate.getInstance().translateKeyFormat(par1Str, par2ArrayOfObj);
    }

    public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(0, 0, 0);
    }
}
