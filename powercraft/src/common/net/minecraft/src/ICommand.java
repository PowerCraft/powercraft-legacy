package net.minecraft.src;

import java.util.List;

public interface ICommand extends Comparable
{
    String getCommandName();

    String getCommandUsage(ICommandSender var1);

    List getCommandAliases();

    void processCommand(ICommandSender var1, String[] var2);

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    boolean canCommandSenderUseCommand(ICommandSender var1);

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    List addTabCompletionOptions(ICommandSender var1, String[] var2);

    boolean func_82358_a(int var1);
}
