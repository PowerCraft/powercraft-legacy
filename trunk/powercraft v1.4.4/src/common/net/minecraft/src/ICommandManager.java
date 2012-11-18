package net.minecraft.src;

import java.util.List;
import java.util.Map;

public interface ICommandManager
{
    void executeCommand(ICommandSender var1, String var2);

    List getPossibleCommands(ICommandSender var1, String var2);

    List getPossibleCommands(ICommandSender var1);

    Map getCommands();
}
