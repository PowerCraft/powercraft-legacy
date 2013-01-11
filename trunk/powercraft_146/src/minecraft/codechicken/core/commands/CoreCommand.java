package codechicken.core.commands;

import java.util.List;

import codechicken.core.ServerUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public abstract class CoreCommand implements ICommand
{	
	public abstract boolean OPOnly();
	
	@Override
	public String getCommandUsage(ICommandSender var1)
	{
		return "/" + getCommandName() + " help";
	}
	
	@Override
	public void processCommand(ICommandSender listener, String[] args)
	{
		if(args.length < minimumParameters() ||
				args.length == 1 && args[0].equals("help"))
		{
			printHelp(listener);
			return;
		}
		
		String command = getCommandName();
		for(String arg : args)
			command+=" "+arg;
		
		handleCommand(command, listener.getCommandSenderName(), args, listener);
	}
	
	public abstract void handleCommand(String command, String playername, String[] args, ICommandSender listener);
	
	public abstract void printHelp(ICommandSender listener);
	
	public final EntityPlayerMP getPlayer(String name)
	{
		return ServerUtils.getPlayer(name);
	}
	
	public WorldServer getWorld(int dimension)
	{
		return DimensionManager.getWorld(dimension);
	}

	public WorldServer getWorld(EntityPlayer player)
	{
		return (WorldServer) player.worldObj;
	}

	public Integer parseInteger(String parse)
	{
		try
		{
			return Integer.parseInt(parse);
		}
		catch(NumberFormatException e)
		{
			return null;
		}
	}

	@Override
	public int compareTo(Object arg0)
	{
		return getCommandName().compareTo(((ICommand)arg0).getCommandName());
	}

	@Override
	public List<?> getCommandAliases()
	{
		return null;
	}

	@Override
	public List<?> addTabCompletionOptions(ICommandSender var1, String[] var2)
	{
		return null;
	}
	
	@Override
	public boolean isUsernameIndex(int var1)
	{
		return false;
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender var1)
	{
		if(OPOnly())
		{
			if(var1 instanceof EntityPlayer)
				return ServerUtils.isPlayerOP(var1.getCommandSenderName());
			else if(var1 instanceof MinecraftServer)
				return true;
			else
				return false;
		}
		else
			return true;
	}

	
	public abstract int minimumParameters();
}
