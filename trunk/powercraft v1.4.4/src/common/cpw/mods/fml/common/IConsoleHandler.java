package cpw.mods.fml.common;

public interface IConsoleHandler
{
    public boolean handleCommand(String command, Object... data);
}
