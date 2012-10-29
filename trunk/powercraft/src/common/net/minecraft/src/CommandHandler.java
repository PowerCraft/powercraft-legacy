package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;

public class CommandHandler implements ICommandManager
{
    /** Map of Strings to the ICommand objects they represent */
    private final Map commandMap = new HashMap();

    /** The set of ICommand objects currently loaded. */
    private final Set commandSet = new HashSet();

    public void executeCommand(ICommandSender par1ICommandSender, String par2Str)
    {
        if (par2Str.startsWith("/"))
        {
            par2Str = par2Str.substring(1);
        }

        String[] var3 = par2Str.split(" ");
        String var4 = var3[0];
        var3 = dropFirstString(var3);
        ICommand var5 = (ICommand)this.commandMap.get(var4);

        try
        {
            if (var5 == null)
            {
                throw new CommandNotFoundException();
            }

            if (var5.canCommandSenderUseCommand(par1ICommandSender))
            {
                CommandEvent event = new CommandEvent(var5, par1ICommandSender, var3);
                if (!MinecraftForge.EVENT_BUS.post(event))
                {
                    var5.processCommand(par1ICommandSender, event.parameters);
                }
                else
                {
                    if (event.exception != null)
                    {
                        throw event.exception;
                    }
                }
            }
            else
            {
                par1ICommandSender.sendChatToPlayer("\u00a7cYou do not have permission to use this command.");
            }
        }
        catch (WrongUsageException var7)
        {
            par1ICommandSender.sendChatToPlayer("\u00a7c" + par1ICommandSender.translateString("commands.generic.usage", new Object[] {par1ICommandSender.translateString(var7.getMessage(), var7.getErrorOjbects())}));
        }
        catch (CommandException var8)
        {
            par1ICommandSender.sendChatToPlayer("\u00a7c" + par1ICommandSender.translateString(var8.getMessage(), var8.getErrorOjbects()));
        }
        catch (Throwable var9)
        {
            par1ICommandSender.sendChatToPlayer("\u00a7c" + par1ICommandSender.translateString("commands.generic.exception", new Object[0]));
            var9.printStackTrace();
        }
    }

    /**
     * adds the command and any aliases it has to the internal map of available commands
     */
    public ICommand registerCommand(ICommand par1ICommand)
    {
        List var2 = par1ICommand.getCommandAliases();
        this.commandMap.put(par1ICommand.getCommandName(), par1ICommand);
        this.commandSet.add(par1ICommand);

        if (var2 != null)
        {
            Iterator var3 = var2.iterator();

            while (var3.hasNext())
            {
                String var4 = (String)var3.next();
                ICommand var5 = (ICommand)this.commandMap.get(var4);

                if (var5 == null || !var5.getCommandName().equals(var4))
                {
                    this.commandMap.put(var4, par1ICommand);
                }
            }
        }

        return par1ICommand;
    }

    /**
     * creates a new array and sets elements 0..n-2 to be 0..n-1 of the input (n elements)
     */
    private static String[] dropFirstString(String[] par0ArrayOfStr)
    {
        String[] var1 = new String[par0ArrayOfStr.length - 1];

        for (int var2 = 1; var2 < par0ArrayOfStr.length; ++var2)
        {
            var1[var2 - 1] = par0ArrayOfStr[var2];
        }

        return var1;
    }

    /**
     * Performs a "begins with" string match on each token in par2. Only returns commands that par1 can use.
     */
    public List getPossibleCommands(ICommandSender par1ICommandSender, String par2Str)
    {
        String[] var3 = par2Str.split(" ", -1);
        String var4 = var3[0];

        if (var3.length == 1)
        {
            ArrayList var8 = new ArrayList();
            Iterator var6 = this.commandMap.entrySet().iterator();

            while (var6.hasNext())
            {
                Entry var7 = (Entry)var6.next();

                if (CommandBase.doesStringStartWith(var4, (String)var7.getKey()) && ((ICommand)var7.getValue()).canCommandSenderUseCommand(par1ICommandSender))
                {
                    var8.add(var7.getKey());
                }
            }

            return var8;
        }
        else
        {
            if (var3.length > 1)
            {
                ICommand var5 = (ICommand)this.commandMap.get(var4);

                if (var5 != null)
                {
                    return var5.addTabCompletionOptions(par1ICommandSender, dropFirstString(var3));
                }
            }

            return null;
        }
    }

    /**
     * returns all commands that the commandSender can use
     */
    public List getPossibleCommands(ICommandSender par1ICommandSender)
    {
        ArrayList var2 = new ArrayList();
        Iterator var3 = this.commandSet.iterator();

        while (var3.hasNext())
        {
            ICommand var4 = (ICommand)var3.next();

            if (var4.canCommandSenderUseCommand(par1ICommandSender))
            {
                var2.add(var4);
            }
        }

        return var2;
    }

    /**
     * returns a map of string to commads. All commands are returned, not just ones which someone has permission to use.
     */
    public Map getCommands()
    {
        return this.commandMap;
    }
}
