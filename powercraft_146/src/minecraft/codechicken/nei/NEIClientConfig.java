package codechicken.nei;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import codechicken.core.ClassDiscoverer;
import codechicken.core.ConfigFile;
import codechicken.core.ConfigTag;
import codechicken.core.ClientUtils;
import codechicken.core.IStringMatcher;
import codechicken.nei.GuiNEISettings.NEIOption;
import codechicken.nei.api.API;
import codechicken.nei.api.GuiInfo;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.api.LayoutStyle;
import codechicken.nei.api.TaggedInventoryArea;
import codechicken.nei.asm.NEIModContainer;
import codechicken.nei.recipe.RecipeInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.world.EnumGameType;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.world.World;

public class NEIClientConfig
{	
    public static ItemVisibilityHash vishash;
    
    private static boolean statesSaved[] = new boolean[7];
    private static boolean configLoaded;
    private static boolean internalEnabled = false;    
    
    private static boolean SMPMagnetMode;
    private static boolean SMPCreativeInv;
    
    private static boolean hasSMPCounterpart;
    private static HashSet<InterActionMap> permissableActions = new HashSet<InterActionMap>();
    private static HashSet<ItemHash> bannedBlocks = new HashSet<ItemHash>();
    private static HashSet<Integer> disabledProperties = new HashSet<Integer>();
    
    public static NBTTagCompound saveCompound = new NBTTagCompound();
    public static File saveFile = new File(Minecraft.getMinecraftDir(), "saves/NEI.dat");
    public static ConfigFile globalConfig = new ConfigFile(new File(Minecraft.getMinecraftDir(), "config/NEI.cfg"));
    
    public static NBTTagCompound localCompound;
    public static File localSave;
    public static ConfigFile worldConfig;
    public static ItemStack creativeInv[];
    
	public static boolean global = false;
    
    static
	{
		setDefaults();
	}
    
    private static void setDefaults()
    {
    	globalConfig.setComment("Main configuration of NEI.:Most of these options can be changed ingame.:Deleting any element will restore it to it's default value");
    	
    	globalConfig.getTag("KeyBinding").useBraces();
    	globalConfig.getTag("options").useBraces();
    	globalConfig.getTag("command").useBraces().setComment("Change these options if you have a different mod installed on the server that handles the commands differently, Eg. Bukkit Essentials");
    	globalConfig.setNewLineMode(1);
    	
    	globalConfig.getTag("options.enable").setPosition(0).getBooleanValue(true);
    	API.addSetting(new NEIOption("options.enable")
		{    		
			@Override
			public String updateText()
			{
				return "NEI "+(enabled() ? "Enabled" : "Disabled");
			}			
		});
    	
    	globalConfig.getTag("options.hidden").setPosition(2).getBooleanValue(false);
    	
    	globalConfig.getTag("options.cheatmode").setPosition(3).setNewLine(true).getIntValue(2);
    	API.addSetting(new NEIOption("options.cheatmode")
		{
			@Override
			public String updateText()
			{
				return new String[]{"Recipe", "Utility", "Cheat"}[intValue()]+" Mode";
			}
			
			@Override
			public void onClick()
			{
				NEIClientConfig.cycleCheatMode();
			}
		});
        globalConfig.getTag("options.lockmode").setPosition(4).setComment("For those who can't help themselves:Set this to a mode and you will be unable to change it ingame").getIntValue(-1);
        globalConfig.getTag("options.utility actions").setPosition(5).setDefaultValue("delete, magnet");
        {
        	StringBuilder actionlist = new StringBuilder();
        	InterActionMap[] actions = InterActionMap.values();
        	for(int i = 0; i < actions.length; i++)
    		{
        		if(i != 0)
        			actionlist.append(", ");
        		actionlist.append(actions[i].getName());
    		}
        	
        	globalConfig.getTag("options.utility actions").setComment("list the actions that are considered 'utilities' rather than cheats:Choose from "+actionlist.toString());
        }        
        
    	globalConfig.getTag("options.layout style").setPosition(6).setNewLine(true).getIntValue(0);
    	API.addSetting(new NEIOption("options.layout style")
		{			
			@Override
			public String updateText()
			{
				return LayoutManager.getLayoutStyle().getName();
			}
			
			@Override
			public void onClick()
			{
				NEIClientConfig.cycleLayoutStyle();
			}
		});
    	globalConfig.getTag("options.edge-align buttons").setPosition(7).getBooleanValue(false);

    	globalConfig.getTag("options.show ids").setPosition(8).setNewLine(true).getIntValue(1);
    	API.addSetting(new NEIOption("options.show ids")
		{			
			@Override
			public String updateText()
			{
				return "ItemID's: "+new String[]{"Hidden", "Auto", "Shown"}[intValue()];
			}
			
			@Override
			public void onClick()
			{
				NEIClientConfig.cycleIDVisibility();
			}
		});

    	globalConfig.getTag("options.searchinventories").setPosition(10).getBooleanValue(false);
    	globalConfig.getTag("options.inworld tooltips").setPosition(11).getBooleanValue(false);
    	API.addSetting(new NEIOption("options.inworld tooltips")
		{			
			@Override
			public String updateText()
			{
				return "Highlight tips "+(enabled() ? "shown" : "hidden");
			}
		});
    	
    	globalConfig.getTag("command.creative").setComment("{0} = 1 for creative, 0 for survival, {1} = player").setDefaultValue("/gamemode {0} {1}");
    	globalConfig.getTag("command.give").setNewLine(true).setComment("{0} = player, {1} = itemID, {2} = quantity, {3} = itemDamage").setDefaultValue("/give {0} {1} {2} {3}");
    	globalConfig.getTag("command.time").setNewLine(true).setComment("{0} = worldtime").setDefaultValue("/time set {0}");
    	globalConfig.getTag("command.rain").setNewLine(true).setComment("{0} = 1 for on, 0 for off").setDefaultValue("/toggledownfall");
    	globalConfig.getTag("command.heal").setNewLine(true).setComment("The vanilla server doesn't have a heal command, however others may:{0} = player").setDefaultValue("null");

    	checkCheatMode();

        addBlockIDSettings();
        setDefaultKeyBindings();
        
        globalConfig.saveConfig();
    }

	private static void addBlockIDSettings()
	{
		API.addSetting(new NEIOption("")
		{			
			@Override
			public String updateText()
			{
				return "Block/Item ID Settings";
			}
			
			@Override
			public void onClick()
			{
				NEIClientUtils.mc().displayGuiScreen(new GuiNEIBlockIDs(((GuiNEISettings)NEIClientUtils.mc().currentScreen).parentScreen));
			}
		}.setGlobalOnly());
		
		globalConfig.getTag("ID dump").useBraces().setComment("Block/Item ID settings, configured via the options menu ingame.");
		globalConfig.getTag("ID dump.show empty blockIDs").getBooleanValue(false);
		API.addSetting(GuiNEIBlockIDs.class, new NEIOption("ID dump.show empty blockIDs")
		{			
			@Override
			public String updateText()
			{
				return (enabled() ? "Show" : "Hide") + " Unused BlockIDs";
			}
		});
		globalConfig.getTag("ID dump.dump on load").getBooleanValue(false);
		API.addSetting(GuiNEIBlockIDs.class, new NEIOption("ID dump.dump on load")
		{
			@Override
			public String updateText()
			{
				return (enabled() ? "Dump ID Map" : "Do Nothing") + " on Load";
			}
		}.setGlobalOnly());
		globalConfig.getTag("ID dump.blockIDs").getBooleanValue(true);
		API.addSetting(GuiNEIBlockIDs.class, new NEIOption("ID dump.blockIDs")
		{
			@Override
			public String updateText()
			{
				return (enabled() ? "Dump" : "Ignore") + " BlockIDs";
			}
		}.setGlobalOnly());
		globalConfig.getTag("ID dump.itemIDs").getBooleanValue(false);
		API.addSetting(GuiNEIBlockIDs.class, new NEIOption("ID dump.itemIDs")
		{
			@Override
			public String updateText()
			{
				return (enabled() ? "Dump" : "Ignore") + " ItemIDs";
			}
		}.setGlobalOnly());
		globalConfig.getTag("ID dump.unused blockIDs").getBooleanValue(true);
		API.addSetting(GuiNEIBlockIDs.class, new NEIOption("ID dump.unused blockIDs")
		{
			@Override
			public String updateText()
			{
				return (enabled() ? "Dump" : "Ignore") + " Unused BlockIDs";
			}
		}.setGlobalOnly());
		globalConfig.getTag("ID dump.unused itemIDs").getBooleanValue(false);
		API.addSetting(GuiNEIBlockIDs.class, new NEIOption("ID dump.unused itemIDs")
		{
			@Override
			public String updateText()
			{
				return (enabled() ? "Dump" : "Ignore") + " Unused ItemIDs";
			}
		}.setGlobalOnly());
		
		API.addSetting(GuiNEIBlockIDs.class, new NEIOption("")
		{
			@Override
			public String updateText()
			{
				return NEIClientConfig.canDump() ? "Dump ID Map Now" : "Nothing To Dump";
			}
			
			public void onClick()
			{
				if(NEIClientConfig.canDump())
					NEIClientUtils.dumpIDs();
			}
		}.setGlobalOnly());

		if(!canDump())
			globalConfig.getTag("ID dump.dump on load").setBooleanValue(false);
	}
	
	public static boolean canDump()
	{
		return getBooleanSetting("ID dump.itemIDs") || getBooleanSetting("ID dump.blockIDs") || getBooleanSetting("ID dump.unused itemIDs") || getBooleanSetting("ID dump.unused blockIDs");
	}

	private static void setDefaultKeyBindings()
	{
    	API.addKeyBind("recipe", "Recipe", Keyboard.KEY_R);
    	API.addKeyBind("usage", "Usage", Keyboard.KEY_U);
    	API.addKeyBind("back", "Previous Recipe", Keyboard.KEY_BACK);
    	API.addKeyBind("enchant", "Enchantment", Keyboard.KEY_X);
    	API.addKeyBind("prev", "Prev Page", Keyboard.KEY_PRIOR);
    	API.addKeyBind("next", "Next Page", Keyboard.KEY_NEXT);
    	API.addKeyBind("hide", "Hide\\Show", Keyboard.KEY_O);
	}

    public static void loadWorld(String saveName)
    {
    	ClientHandler.instance().setWorld(ClientUtils.getWorld());
    	setInternalEnabled(true);
    	System.out.println("Loading World: "+saveName);
    	loadConfig(ClientUtils.getWorld());
    	
    	File saveDir = new File(ClientUtils.getMinecraftDir(), "saves/NEI/"+saveName);
    	if(!saveDir.exists())
    		saveDir.mkdirs();
    	
    	worldConfig = new ConfigFile(new File(saveDir, "NEI.cfg"));
    	localSave = new File(saveDir, "NEI.dat");
    	
    	try
    	{
	    	if(!localSave.exists())
	    		localSave.createNewFile();
	    	
	    	if(localSave.length() == 0)
	    		localCompound = new NBTTagCompound();
	    	else
	    	{
				DataInputStream din = new DataInputStream(new FileInputStream(localSave));
				localCompound = (NBTTagCompound) NBTBase.readNamedTag(din);
				din.close();
	    	}
    	}
    	catch(Exception e)
    	{
    		NEIClientUtils.reportException(e);
    	}
    	
    	worldConfig.setComment("World based configuration of NEI.:Most of these options can be changed ingame.:Deleting any element will restore it to it's default value");
    	
    	worldConfig.getTag("options").useBraces();
    	worldConfig.getTag("options.searchinventories").getBooleanValue(false);
    	worldConfig.removeTag("saved");
    	
    	setDefaultBoolean("magnetmode", false);
    	setDefaultBoolean("creativeinv", false);
    	
    	for(String s : AllowedPropertyMap.nameSet)
    	{
    		setDefaultBoolean("disabled-"+s, false);
    	}
    	
    	if(!localCompound.hasKey("search"))
    		localCompound.setString("search", "");
        
        if(!localCompound.hasKey("quantity"))
        	localCompound.setInteger("quantity", 0);
        
		creativeInv = new ItemStack[54];
        NBTTagList itemList = localCompound.getTagList("creativeitems");
        if(itemList != null)
        {
    		for(int tagPos = 0; tagPos < itemList.tagCount(); tagPos++)
    		{
    			NBTTagCompound stacksave = (NBTTagCompound)itemList.tagAt(tagPos);
    			
            	creativeInv[stacksave.getByte("Slot") & 0xff] = ItemStack.loadItemStackFromNBT(stacksave);
    		}
        }
        
		saveLocalConfig();
    		
    	LayoutManager.searchField.setText(getSearchExpression());
        LayoutManager.quantity.setText(Integer.toString(getItemQuantity()));
    }
    
    public static int getItemQuantity()
	{
		return localCompound.getInteger("quantity");
	}

	private static void setDefaultBoolean(String setting, boolean value)
	{
		if(!localCompound.hasKey(setting))
			localCompound.setBoolean(setting, value);
	}

	public static boolean isWorldSpecific(String setting)
    {
    	return worldConfig != null && worldConfig.containsTag(setting);
    }
    
    public static void copyWorldSetting(String setting)
    {
    	if(worldConfig != null)
    		worldConfig.getTag(setting).setValue(globalConfig.getTag(setting).getValue());
    }
    
    public static void removeWorldSetting(String setting)
    {
    	if(worldConfig != null)
    		worldConfig.removeTag(setting);
    }

    public static boolean isStateSaved(int i)
    {
        return statesSaved[i];
    }

    public static ConfigTag getSetting(String s)
    {
    	if(worldConfig == null || global || !worldConfig.containsTag(s))
    		return globalConfig.getTag(s);
    	else
    		return worldConfig.getTag(s);
    }
    
    public static boolean getBooleanSetting(String s)
    {
        return getSetting(s).getBooleanValue();
    }

    public static boolean isEnabled()
    {
        return internalEnabled && getBooleanSetting("options.enable");
    }
    
    public static void setEnabled(boolean flag)
    {
        getSetting("options.enable").setBooleanValue(flag);
    }
    
	public static int getKeyBinding(String string)
	{
		return getSetting("KeyBinding."+string).getIntValue();
	}
	
	public static void setKeyBinding(String string, int key)
	{
		getSetting("KeyBinding."+string).setIntValue(key);
	}
	
	public static void setDefaultKeyBinding(String string, int key)
	{
		globalConfig.getTag("KeyBinding."+string).getIntValue(key);
	}
 
    public static int getCheatMode()
    {
        return getIntSetting("options.cheatmode");
    }

    public static void cycleCheatMode()
	{
    	cycleSetting("options.cheatmode", 3);
    	checkCheatMode();
	}
    
    private static void checkCheatMode()
    {
    	if(getLockedMode() != -1)
			setIntSetting("options.cheatmode", getLockedMode());
    }
    
    public static int getLockedMode()
    {
    	return getIntSetting("options.lockmode");
    }

    public static int getLayoutStyle()
	{
		return getIntSetting("options.layout style");
	}

    protected static void cycleLayoutStyle()
	{
		LinkedList<Integer> list = new LinkedList<Integer>();
		for(Entry<Integer, LayoutStyle> entry : LayoutManager.layoutStyles.entrySet())
		{
			list.add(entry.getKey());
		}
		Collections.sort(list);
		
		int currentLayout = getLayoutStyle();
		int nextLayout = currentLayout;
		
		if(nextLayout == list.getLast())//loop list
			nextLayout = -1;
		for(Integer i : list)
		{
			if(i > nextLayout)
			{
				nextLayout = i;
				break;
			}
		}
		
		setIntSetting("options.layout style", nextLayout);
	}
    
    public static String getStringSetting(String s)
    {
    	return getSetting(s).getValue();
    }
    
    public static boolean isHidden()
    {
        return !internalEnabled || getBooleanSetting("options.hidden");
    }

	public static boolean showIDs()
    {
        int i = getIntSetting("options.show ids");
        return i == 2 || (i == 1 && isEnabled() && !isHidden());
    }
	
	public static int getIDVisibility()
    {
        return getIntSetting("options.show ids");
    }

	public static void cycleIDVisibility()
	{
		cycleSetting("options.show ids", 3);
	}
    
    public static void toggleBooleanSetting(String setting)
	{
    	ConfigTag tag = getSetting(setting);
    	tag.setBooleanValue(!tag.getBooleanValue());
	}

    public static void cycleSetting(String setting, int max)
    {
    	ConfigTag tag = getSetting(setting);
    	tag.setIntValue((tag.getIntValue()+1)%max);
    }
    
    public static int getIntSetting(String setting)
    {
    	return getSetting(setting).getIntValue();
    }
    
    public static void setIntSetting(String setting, int val)
    {
    	getSetting(setting).setIntValue(val);
    }
    
	public static String getSearchExpression()
    {
    	return localCompound.getString("search");
    }
    
    public static void setSearchExpression(String expression)
    {    	
    	localCompound.setString("search", expression);
		saveLocalConfig();
    }
    
    public static boolean getMagnetMode()
    {
    	return SMPMagnetMode;
    }

	public static boolean invCreativeMode()
	{
		if(SMPCreativeInv && !isActionPermissable(InterActionMap.CREATIVE))
			SMPCreativeInv = false;
    	return SMPCreativeInv;
	}
	
	public static void setInvCreative(boolean b)
	{
		SMPCreativeInv = b;
	}

	public static void setMagnetMode(boolean b)
	{
		SMPMagnetMode = b;
	}
    
    public static boolean areDamageVariantsShown()
    {
    	return hasSMPCounterPart() || getSetting("command.give").getValue().contains("{3}");
    }

    public static void clearState(int state)
    {
        statesSaved[state] = false;
        saveCompound.setTag("save"+state, new NBTTagCompound());
        saveConfig();
    }
    
	public static void loadState(int state)
    {
        if(!statesSaved[state])
        {
            return;
        }
        
        NBTTagCompound statesave = saveCompound.getCompoundTag("save"+state);
    	GuiContainer currentContainer = NEIClientUtils.getGuiContainer();
    	LinkedList<TaggedInventoryArea> saveAreas = new LinkedList<TaggedInventoryArea>();
    	saveAreas.add(new TaggedInventoryArea(NEIClientUtils.mc().thePlayer.inventory));
    	
    	for(INEIGuiHandler handler : GuiInfo.guiHandlers)
    	{
    		List<TaggedInventoryArea> areaList = handler.getInventoryAreas(currentContainer);
    		if(areaList != null)
    			saveAreas.addAll(areaList);
    	}
    	
    	for(TaggedInventoryArea area : saveAreas)
    	{
    		if(!statesave.hasKey(area.tag))
    			continue;
    		
    		for(int slot : area.slots)
    		{
    			NEIClientUtils.setSlotContents(slot, null, area.isContainer());
    		}
    		
    		NBTTagList areaTag = statesave.getTagList(area.tag);
    		for(int i = 0; i < areaTag.tagCount(); i++)
    		{
    			NBTTagCompound stacksave = (NBTTagCompound) areaTag.tagAt(i);
    			int slot = stacksave.getByte("Slot")&0xFF;
    			if(!area.slots.contains(slot))
    				continue;
    			    			
    			NEIClientUtils.setSlotContents(slot, ItemStack.loadItemStackFromNBT(stacksave), area.isContainer());
    		}
    	}
    }

    public static void saveState(int state)
    {    	
    	NBTTagCompound statesave = saveCompound.getCompoundTag("save"+state);
    	GuiContainer currentContainer = NEIClientUtils.getGuiContainer();
    	LinkedList<TaggedInventoryArea> saveAreas = new LinkedList<TaggedInventoryArea>();
    	saveAreas.add(new TaggedInventoryArea(NEIClientUtils.mc().thePlayer.inventory));
    	
    	for(INEIGuiHandler handler : GuiInfo.guiHandlers)
    	{
    		List<TaggedInventoryArea> areaList = handler.getInventoryAreas(currentContainer);
    		if(areaList != null)
    			saveAreas.addAll(areaList);
    	}
    	
    	for(TaggedInventoryArea area : saveAreas)
    	{
    		NBTTagList areaTag = new NBTTagList(area.tag);
    		
    		for(int i : area.slots)
    		{
    			ItemStack stack = area.getStackInSlot(i);
    			if(stack == null)
    				continue;
        		NBTTagCompound stacksave = new NBTTagCompound();
                stacksave.setByte("Slot", (byte)i);
                stack.writeToNBT(stacksave);
                areaTag.appendTag(stacksave);
    		}
    		statesave.setTag(area.tag, areaTag);
    	}
    	
    	saveCompound.setTag("save"+state, statesave);
        
        statesSaved[state] = true;
        saveConfig();
    }
    
    public static void loadStates()
    {
    	for(int state = 0; state < 7; state++)
    	{
    		if(saveCompound.hasKey("save"+state) && saveCompound.tagMap.get("save"+state) instanceof NBTTagList)
    			saveCompound.tagMap.remove("save"+state);
    		statesSaved[state] = saveCompound.getCompoundTag("save"+state).tagMap.size() > 0;
    	}
    }

	public static void loadConfig(World world)
	{
		if(configLoaded)
			return;
		
		loadSavedConfig();
		vishash = new ItemVisibilityHash();
		ItemInfo.load(world);
		GuiInfo.load();
		RecipeInfo.load();
		LayoutManager.load();
		NEIController.load();
		
		configLoaded = true;
		
		ClassDiscoverer classDiscoverer = new ClassDiscoverer(new IStringMatcher()
		{
			public boolean matches(String test)
			{
				return test.startsWith("NEI") && test.endsWith("Config.class");
			}
		}, IConfigureNEI.class);
		
		classDiscoverer.findClasses();
		
		for(Class<?> class1 : classDiscoverer.classes)
		{
			try
			{
				IConfigureNEI config = (IConfigureNEI)class1.newInstance();
	            config.loadConfig();
	            NEIModContainer.plugins.add(config);
	            System.out.println("Loaded "+class1.getName());
			}
			catch(Exception e)
			{
	            System.out.println("Failed to Load "+class1.getName());
				e.printStackTrace();
			}
		}
	}
	
	public static void saveConfig()
	{
		try
		{
			if(!saveFile.exists())
			{
				saveFile.createNewFile();
			}
			FileOutputStream fout = new FileOutputStream(saveFile);
			DataOutputStream dout = new DataOutputStream(fout);
			NBTBase.writeNamedTag(saveCompound, dout);
			dout.close();
			fout.close();
		}
		catch(Exception e)
		{
			NEIClientUtils.reportException(e);
		}
	}
	
	public static void saveLocalConfig()
	{
		try
		{
			DataOutputStream dout = new DataOutputStream(new FileOutputStream(localSave));
			NBTBase.writeNamedTag(localCompound, dout);
			dout.close();
		}
		catch(Exception e)
		{
			NEIClientUtils.reportException(e);
		}
	}
	
	private static void loadSavedConfig()
	{
		try
		{
			if(!saveFile.exists())
			{
				saveFile.createNewFile();
			}
			if(saveFile.length() == 0)
			{
				return;
			}
			
			FileInputStream fin = new FileInputStream(saveFile);
			DataInputStream din = new DataInputStream(fin);
			
			saveCompound = (NBTTagCompound) NBTBase.readNamedTag(din);
			
			din.close();
			fin.close();
			
			loadStates();
			ItemVisibilityHash.loadStates();
		}
		catch(Exception e)
		{
			NEIClientUtils.reportException(e);
		}		
	}
	
	public static boolean hasSMPCounterPart()
	{
		return hasSMPCounterpart;
	}
	
	public static void setHasSMPCounterPart(boolean flag)
	{
		hasSMPCounterpart = flag;
		permissableActions.clear();
		bannedBlocks.clear();
		disabledProperties.clear();
	}
	
	public static void resetPermissableActions()
	{
		permissableActions.clear();
	}
	
	public static void addPermissableAction(InterActionMap action)
	{
		permissableActions.add(action);
	}

	public static boolean isActionPermissable(InterActionMap action)
	{
		return isActionPermissable(action.getName());
	}

	public static boolean isActionPermissable(String actionname)
	{
		if(!isEnabled() || isHidden())
			return false;
		
		if(actionname.equals("nbt"))
			return hasSMPCounterPart();
		
		InterActionMap action = InterActionMap.getAction(actionname);
		if(!isActionPermissableInMode(actionname))
			return false;
		
		if(action == InterActionMap.HEAL)
			if(!hasSMPCounterpart)
				return !getStringSetting("command.heal").equals("null");
		
		if(hasSMPCounterpart)
			return permissableActions.contains(action);
		else if(!hasSMPCounterpart && !action.requiresSMPCounterpart)
			return true;
		return false;
	}
	
	private static boolean isActionPermissableInMode(String actionmode)
	{
		if(getCheatMode() == 0)
			return false;
		else if(getCheatMode() == 2)
			return true;
		else
		{
			String[] actions = getUtilityDefinition();
			for(String action : actions)
			{
				if(action.equalsIgnoreCase(actionmode))
					return true;
			}			
		}
		return false;
	}

	private static String[] getUtilityDefinition()
	{
		return getStringSetting("options.utility actions").replace(" ", "").split(",");
	}

	public static void setBannedBlocks(ArrayList<ItemHash> ahash)
	{
		bannedBlocks.clear();
		for(ItemHash hash : ahash)
		{
			bannedBlocks.add(hash);
		}
	}
	
	public static void resetDisabledProperties()
	{
		disabledProperties.clear();
	}
	
	public static boolean canGetItem(ItemHash item)
	{
		return !bannedBlocks.contains(item);
	}
	
	public static boolean isPropertyDisabled(String name)
	{
		return disabledProperties.contains(AllowedPropertyMap.nameToIDMap.get(name));
	}
	
	/**
	 * SMP version
	 * @param ID
	 */
	public static void setPropertyDisabled(int ID)
	{
		disabledProperties.add(ID);
	}
	
	public static void setPropertyDisabled(String name, boolean disable)
	{
		if(hasSMPCounterPart())
			ClientPacketHandler.sendSetPropertyDisabled(name, disable);
	}
	
	public static void setItemQuantity(int i)
	{
		localCompound.setInteger("quantity", i);
		saveLocalConfig();
	}

	public static void setInternalEnabled(boolean b) 
	{
		internalEnabled = b;
	}

	public static void setCreativeMode(int mode) 
	{
    	setInvCreative(mode == 2);
        ((PlayerControllerMP)ClientUtils.mc().playerController).setGameType(mode != 0 ? EnumGameType.CREATIVE : EnumGameType.SURVIVAL);
	}

	public static boolean validateEnchantments()
	{
		return worldConfig.getTag("saved.validateenchantments").getBooleanValue(true);
	}
	
	public static void toggleEnchantmentValidation()
	{
		worldConfig.getTag("saved.validateenchantments").setBooleanValue(!validateEnchantments());
	}
}
