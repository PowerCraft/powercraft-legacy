package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class CommandHandler implements ICommandManager
{
    private final Map field_71562_a = new HashMap();
    private final Set field_71561_b = new HashSet();

    public void func_71556_a(ICommandSender par1ICommandSender, String par2Str)
    {
        if (par2Str.startsWith("/"))
        {
            par2Str = par2Str.substring(1);
        }

        String[] var3 = par2Str.split(" ");
        String var4 = var3[0];
        var3 = func_71559_a(var3);
        ICommand var5 = (ICommand)this.field_71562_a.get(var4);

        try
        {
            if (var5 == null)
            {
                throw new CommandNotFoundException();
            }

            if (var5.canCommandSenderUseCommand(par1ICommandSender))
            {
                var5.processCommand(par1ICommandSender, var3);
            }
            else
            {
                par1ICommandSender.func_70006_a("\u00a7cYou do not have permission to use this command.");
            }
        }
        catch (WrongUsageException var7)
        {
            par1ICommandSender.func_70006_a("\u00a7c" + par1ICommandSender.translateString("commands.generic.usage", new Object[] {par1ICommandSender.translateString(var7.getMessage(), var7.func_74844_a())}));
        }
        catch (CommandException var8)
        {
            par1ICommandSender.func_70006_a("\u00a7c" + par1ICommandSender.translateString(var8.getMessage(), var8.func_74844_a()));
        }
        catch (Throwable var9)
        {
            par1ICommandSender.func_70006_a("\u00a7c" + par1ICommandSender.translateString("commands.generic.exception", new Object[0]));
            var9.printStackTrace();
        }
    }

    public ICommand func_71560_a(ICommand par1ICommand)
    {
        List var2 = par1ICommand.getCommandAliases();
        this.field_71562_a.put(par1ICommand.getCommandName(), par1ICommand);
        this.field_71561_b.add(par1ICommand);

        if (var2 != null)
        {
            Iterator var3 = var2.iterator();

            while (var3.hasNext())
            {
                String var4 = (String)var3.next();
                ICommand var5 = (ICommand)this.field_71562_a.get(var4);

                if (var5 == null || !var5.getCommandName().equals(var4))
                {
                    this.field_71562_a.put(var4, par1ICommand);
                }
            }
        }

        return par1ICommand;
    }

    private static String[] func_71559_a(String[] par0ArrayOfStr)
    {
        String[] var1 = new String[par0ArrayOfStr.length - 1];

        for (int var2 = 1; var2 < par0ArrayOfStr.length; ++var2)
        {
            var1[var2 - 1] = par0ArrayOfStr[var2];
        }

        return var1;
    }

    public List func_71558_b(ICommandSender par1ICommandSender, String par2Str)
    {
        String[] var3 = par2Str.split(" ", -1);
        String var4 = var3[0];

        if (var3.length == 1)
        {
            ArrayList var8 = new ArrayList();
            Iterator var6 = this.field_71562_a.entrySet().iterator();

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
                ICommand var5 = (ICommand)this.field_71562_a.get(var4);

                if (var5 != null)
                {
                    return var5.addTabCompletionOptions(par1ICommandSender, func_71559_a(var3));
                }
            }

            return null;
        }
    }

    public List func_71557_a(ICommandSender par1ICommandSender)
    {
        ArrayList var2 = new ArrayList();
        Iterator var3 = this.field_71561_b.iterator();

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

    public Map func_71555_a()
    {
        return this.field_71562_a;
    }
}
