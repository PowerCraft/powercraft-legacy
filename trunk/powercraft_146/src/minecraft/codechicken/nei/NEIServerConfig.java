package codechicken.nei;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import codechicken.core.PacketCustom;

import net.minecraft.server.MinecraftServer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import codechicken.core.CommonUtils;
import codechicken.core.ConfigFile;
import codechicken.core.ConfigTag;
import codechicken.core.ServerUtils;
import codechicken.nei.api.ItemInfo;

public class NEIServerConfig
{
	public static HashMap<String, PlayerSave> playerSaves = new HashMap<String, PlayerSave>();
	
	public static HashMap<ItemHash, HashSet<String>> bannedblocks = new HashMap<ItemHash, HashSet<String>>();
	public static ConfigFile serverConfig = new ConfigFile(new File(CommonUtils.getMinecraftDir(), "config/NEIServer.cfg")).setComment("NEI Server Permissions : Names are Comma (,) separated : ALL, OP and NONE are special names");
	public static File worldSaveFile;
	public static File worldSaveDir;
	public static NBTTagCompound worldCompound;
	
	private static MinecraftServer server;
	
	public static void load(World world)
	{
		if(MinecraftServer.getServer() == server)
			return;
		
		System.out.println("Loading NEI");
		server = MinecraftServer.getServer();
		
		initDefaults();
		loadBannedBlocks();
		loadSavedConfig(world);
	}	
	
	private static void loadSavedConfig(World world)
    {
	    try
        {
            worldSaveDir = CommonUtils.getWorldBaseSaveLocation(world);
            worldSaveFile = new File(worldSaveDir, "NEI.dat");
            if(!worldSaveFile.getParentFile().exists())
                worldSaveFile.getParentFile().mkdirs();
            if(!worldSaveFile.exists())
                worldSaveFile.createNewFile();
            
            if(worldSaveFile.length() == 0)
                worldCompound = new NBTTagCompound();
            else
            {
                DataInputStream din = new DataInputStream(new FileInputStream(worldSaveFile));
                worldCompound = (NBTTagCompound) NBTBase.readNamedTag(din);
                din.close();
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void initDefaults()
	{
		serverConfig.setNewLineMode(1);
		serverConfig.getTag("permissions").useBraces();
		serverConfig.getTag("permissions").setComment("List of players who can use these features. :Eg. time=CodeChicken, Friend1");
		serverConfig.getTag("BannedBlocks").useBraces();
		serverConfig.getTag("BannedBlocks").setComment("List of players who can use these blocks. :Anyone not listed here will not have these blocks appear in their item panel.:format is {itemID}::{itemDamage}:Eg. 12::5=CodeChicken, Friend1");
		
		setDefaultFeature("time");
		setDefaultFeature("rain");
		setDefaultFeature("heal");
		setDefaultFeature("magnet");
		setDefaultFeature("creative");
		setDefaultFeature("enchant");
		setDefaultFeature("save-state");
		setDefaultFeature("item");
		setDefaultFeature("delete");
		setDefaultFeature("notify-item", "CONSOLE, OP");
		
		serverConfig.getTag("BannedBlocks."+Block.bedrock.blockID+":0").setDefaultValue("NONE");
	}
	
	private static void setDefaultFeature(String featurename, String... names)
	{
		if(names.length == 0)
			names = new String[]{"OP"};
		
		String list = "";
		for(int i = 0; i < names.length; i++)
		{
			if(i >= 1)
				list += ", ";
			list+=names[i];
		}
		serverConfig.getTag("permissions."+featurename).setDefaultValue(list);
	}

	public static boolean canPlayerUseFeature(String playername, String featurename)
	{
		return isPlayerInList(playername, getPlayerList("permissions."+featurename), true);		
	}
	
	public static boolean isPlayerInList(String playername, HashSet<String> list, boolean allowCards)
	{
		if(playername.equals("CONSOLE"))
			return list.contains(playername);
		
		playername = playername.toLowerCase();
			
		if(allowCards)
		{
			if(list.contains("ALL"))
				return true;
			if((ServerUtils.isPlayerOP(playername) || ServerUtils.isPlayerOwner(playername)) && list.contains("OP"))
				return true;
		}

		return list.contains(playername);
	}

	public static boolean isPropertyDisabled(int dim, String name)
	{
		return getDimCompound(dim).getBoolean("disabled"+name);
	}
	
	private static NBTTagCompound getDimCompound(int dim)
	{
		if(!worldCompound.hasKey("dim"+dim))
			worldCompound.setCompoundTag("dim"+dim, new NBTTagCompound());
		return worldCompound.getCompoundTag("dim"+dim);
	}

	public static void setPropertyDisabled(int dim, String name, boolean disable)
	{
		getDimCompound(dim).setBoolean("disabled"+name, disable);
		saveWorldCompound();
	}
	
	private static void saveWorldCompound()
	{
		try
		{
			DataOutputStream dout = new DataOutputStream(new FileOutputStream(worldSaveFile));
			NBTBase.writeNamedTag(worldCompound, dout);
			dout.close();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static HashSet<String> getPlayerList(String tag)
	{
		String[] list = serverConfig.getTag(tag).getValue("").replace(" ", "").split(",");
		return new HashSet<String>(Arrays.asList(list));
	}
	
	public static void addPlayerToList(String playername, String tag)
	{
		HashSet<String> list = getPlayerList(tag);
		
		if(!playername.equals("CONSOLE") && !playername.equals("ALL") && !playername.equals("OP"))
			playername = playername.toLowerCase();
		
		list.add(playername);
		savePlayerList(tag, list);
	}
	
	public static void remPlayerFromList(String playername, String tag)
	{
		HashSet<String> list = getPlayerList(tag);
		
		if(!playername.equals("CONSOLE") && !playername.equals("ALL") && !playername.equals("OP"))
			playername = playername.toLowerCase();
		
		list.remove(playername);
		savePlayerList(tag, list);
	}
	
	private static void savePlayerList(String tag, Collection<String> list)
	{
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(Iterator<String> iterator = list.iterator(); iterator.hasNext(); i++)
		{
			if(i != 0)
				sb.append(", ");
			
			sb.append(iterator.next());
		}
		
		serverConfig.getTag(tag).setValue(sb.toString());
	}
	
	private static void loadBannedBlocks()
	{
		ConfigTag banTag = serverConfig.getTag("BannedBlocks");
		for(Entry<String, ConfigTag> entry : banTag.childtags.entrySet())
		{
			String ident = entry.getKey();
			String num[] = ident.split(":");
			ItemHash hash = num.length == 1 ? new ItemHash(Integer.parseInt(num[0]), -1) : new ItemHash(Integer.parseInt(num[0]), Integer.parseInt(num[1]));
			
			bannedblocks.put(hash, getPlayerList(entry.getValue().qualifiedname));
		}
	}	

	public static PlayerSave forPlayer(String username)
	{
		return playerSaves.get(username);
	}

	public static void loadPlayer(EntityPlayer player)
	{
		System.out.println("Loading Player: "+player.username);
		playerSaves.put(player.username, new PlayerSave(player.username, new File(worldSaveDir, "NEI/players")));
	}

	public static void unloadPlayer(EntityPlayer player)
	{
		System.out.println("Unloading Player: "+player.username);
		PlayerSave playerSave = playerSaves.remove(player.username);
		if(playerSave != null)
			playerSave.save();
	}

	public static boolean authenticatePacket(EntityPlayerMP sender, PacketCustom packet)
	{
		switch(packet.getType())
		{
			case 1:
				return canPlayerUseFeature(sender.username, "item");
			case 10:
			case 11:
				return true;
			case 5:
			case 12:
				return true;//check this later
			case 6:
				return canPlayerUseFeature(sender.username, "magnet");
			case 7:
				return canPlayerUseFeature(sender.username, "time");
			case 4:
				return canPlayerUseFeature(sender.username, "delete");
			case 8:
				return canPlayerUseFeature(sender.username, "heal");
			case 9:
				return canPlayerUseFeature(sender.username, "rain");
			case 14:
			case 23:
				return canPlayerUseFeature(sender.username, "creative");
			case 21:
			case 22:
				return canPlayerUseFeature(sender.username, "creative");
		}
		return true;
	}
}
