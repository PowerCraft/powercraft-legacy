package net.minecraft.src;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;


/**
 * Common PowerCraft's utilities<br>
 * (useful functions)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_Utils {

	/**
	 * @return minecraft instance
	 */
	public static Minecraft mc() {
		return ModLoader.getMinecraftInstance();
	}

	/**
	 * Get if player is in creative mode.
	 * 
	 * @return is in creative.
	 */
	public static boolean isCreative() {
		return ModLoader.getMinecraftInstance().playerController.isInCreativeMode();
	}

	/**
	 * Check if two objects are equal; null-safe test.
	 * 
	 * @param a first
	 * @param b second
	 * @return are equal
	 */
	public static boolean areObjectsEqual(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}

	/**
	 * Open Resizable GUI Screen for player
	 * 
	 * @param entityplayer
	 * @param gres resizable GUI
	 */
	/*public static void openGres(EntityPlayer entityplayer, PC_GresBase gres) {
		ModLoader.openGUI(entityplayer, new PC_GresGui(gres));
	}*/

	/**
	 * Open Minecraft GUI Screen for player
	 * 
	 * @param entityplayer
	 * @param gui ordinary GUI
	 */
	/*public static void openGui(EntityPlayer entityplayer, GuiScreen gui) {
		ModLoader.openGUI(entityplayer, gui);
	}*/

	/**
	 * Write PC_INBT object to NBT compound tag, creating a wrapping compound
	 * tag for it.
	 * 
	 * @param parent parent compound tag
	 * @param wrapperTagName name of the wrapper tag, which is later inserted
	 *            into parent
	 * @param stored object to store
	 * @return the compound wrapping tag
	 */
	public static NBTTagCompound saveToNBT(NBTTagCompound parent, String wrapperTagName, PC_INBT stored) {
		NBTTagCompound tag = new NBTTagCompound();
		stored.writeToNBT(tag);
		parent.setCompoundTag(wrapperTagName, tag);
		return tag;
	}

	/**
	 * Load PC_INBT object from NBT compound tag, when it was stored using
	 * "writeWrappedToNBT" method.<br>
	 * 
	 * @param parent parent compound tag
	 * @param wrapperTagName name of the wrapper compound tag, in which the
	 *            object is stored
	 * @param loaded object to load
	 */
	public static PC_INBT loadFromNBT(NBTTagCompound parent, String wrapperTagName, PC_INBT loaded) {
		return loaded.readFromNBT(parent.getCompoundTag(wrapperTagName));
	}

	// === FURNACE ===

	/**
	 * Tests if a given stack is a fuel
	 * 
	 * @param itemstack stack with item
	 * @return is fuel
	 */
	public static boolean isFuel(ItemStack itemstack) {
		if (itemstack == null) {
			return false;
		}

		int i = itemstack.getItem().shiftedIndex;

		return (i < 256 && Block.blocksList[i] != null && Block.blocksList[i].blockMaterial == Material.wood) || (i == Item.stick.shiftedIndex)
				|| (i == Item.coal.shiftedIndex) || (i == Item.bucketLava.shiftedIndex) || (i == Block.sapling.blockID)
				|| ModLoader.addAllFuel(i, itemstack.getItemDamage()) > 0;
	}

	/**
	 * Tests if a given stack can be smelted
	 * 
	 * @param itemstack stack with item
	 * @return is smeltable
	 */
	public static boolean isSmeltable(ItemStack itemstack) {
		if (itemstack == null || FurnaceRecipes.smelting().getSmeltingResult(itemstack.getItem().shiftedIndex) == null) {
			return false;
		}
		return true;
	}

	// === BLOCK PLACING UTILS ===

	/**
	 * Is "reverse placing key" pressed?
	 * 
	 * @return true if placing is reversed.
	 */
	public static boolean isPlacingReversed() {
		return mod_PCcore.instance.cfg().isKeyDown(mod_PCcore.pk_keyReverse);
	}

	/**
	 * Reverse side (0,1,2,3). Usually used together with isPlacingReversede().
	 * 
	 * @param l side
	 * @return reversed side
	 */
	public static int reverseSide(int l) {
		if (l == 0) {
			l = 2;
		} else if (l == 2) {
			l = 0;
		} else if (l == 1) {
			l = 3;
		} else if (l == 3) {
			l = 1;
		}

		return l;
	}

	// === MESSAGES ===

	/**
	 * Sends chat message onto the screen.
	 * 
	 * @param msg message
	 * @param clear clear screen before the messsage
	 */
	public static void chatMsg(String msg, boolean clear) {
		if (clear) {
			/**
			 * @todo fix
			 * ModLoader.getMinecraftInstance().ingameGUI.clearChatMessages();
			 */
		}
		ModLoader.getMinecraftInstance().thePlayer.addChatMessage(msg);
	}


	// === CONVERSIONS ===

	/**
	 * Split comma separated list of integers.
	 * 
	 * @param list String containing the list.
	 * @return array of integers or null.
	 */
	public static List<Integer> parseIntList(String list) {
		if (list == null) {
			return null;
		}
		String[] parts = list.split(",");

		ArrayList<Integer> intList = new ArrayList<Integer>();

		for (String part : parts) {
			try {
				intList.add(Integer.parseInt(part));
			} catch (NumberFormatException e) {}
		}

		return intList;

	}

	/**
	 * Convert float to string, remove the mess at the end.
	 * 
	 * @param f float
	 * @return string
	 */
	public static String floatToString(float f) {
		String s = Float.toString(f);
		s = s.replaceAll("([0-9]+\\.[0-9]+)00+[0-9]+", "$1");
		s = s.replaceAll("0+$", "");
		s = s.replaceAll("\\.$", "");
		return s;
	}

	/**
	 * Convert double to string, remove the mess at the end.
	 * 
	 * @param d double
	 * @return string
	 */
	public static String doubleToString(double d) {
		String s = Double.toString(d);
		s = s.replaceAll("([0-9]+\\.[0-9]+)00+[0-9]+", "$1");
		s = s.replaceAll("0+$", "");
		s = s.replaceAll("\\.$", "");
		return s;
	}

	/**
	 * Convert ticks to seconds
	 * 
	 * @param ticks ticks count
	 * @return seconds (double)
	 */
	public static double ticksToSecs(int ticks) {
		return ticks * 0.05D;
	}

	/**
	 * Convert ticks to seconds
	 * 
	 * @param ticks ticks count
	 * @return rounded seconds (int)
	 */
	public static int ticksToSecsInt(int ticks) {
		return Math.round(ticks * 0.05F);
	}

	/**
	 * Convert seconds to ticks
	 * 
	 * @param secs seconds (double)
	 * @return ticks count
	 */
	public static int secsToTicks(double secs) {
		return (int) Math.round(secs * 20);
	}

	// === TIME FORMAT ===

	/**
	 * Format ticks to a HH:MM:SS string, omit leading zeros.
	 * 
	 * @param ticks ticks
	 * @return formatted time
	 */
	public static String formatTimeTicks(int ticks) {
		return formatTimeSecs(ticksToSecsInt(ticks));
	}

	/**
	 * Format seconds to a HH:MM:SS string, omit leading zeros.
	 * 
	 * @param secs seconds
	 * @return formatted time
	 */
	public static String formatTimeSecs(int secs) {
		int mins = 0, hours = 0;
		if (secs >= 60) {
			mins = secs / 60;
			secs = secs % 60;
		}

		if (mins >= 60) {
			hours = mins / 60;
			mins = mins % 60;
		}

		return (hours > 0 ? hours + ":" : "") + (hours > 0 || mins > 0 ? mins + ":" : "") + secs;
	}

	public static PC_GresBase createGui(Class c, EntityPlayer player, TileEntity te){
		if(c==null)
			return null;
		PC_GresBase gb=null;
		try {
			gb = (PC_GresBase)c.getConstructor(new Class[]{EntityPlayer.class, TileEntity.class}).newInstance(player, te);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gb;
	}
	
	public static void openGres(EntityPlayer entityplayer, Class c,
			TileEntity te) {
		if(entityplayer instanceof EntityPlayerMP){
			Enumeration<Integer> ei = PC_Module.guiList.keys();
			while(ei.hasMoreElements()){
				Integer i=ei.nextElement();
				Class cgui = PC_Module.guiList.get(i);
				if(cgui==c){
					PC_GresContainerManager cm = new PC_GresContainerManager(entityplayer, createGui(c, entityplayer, te));
					if(te!=null)
						ModLoader.serverOpenWindow((EntityPlayerMP)entityplayer, cm, i, te.xCoord, te.yCoord, te.zCoord);
					else
						ModLoader.serverOpenWindow((EntityPlayerMP)entityplayer, cm, i, 0, -1, 0);
					break;
				}
			}
		}
	}
	
	public static void openGres(EntityPlayer entityplayer, Class c,
			World world, int x, int y, int z) {
		if(entityplayer instanceof EntityPlayerMP){
			Enumeration<Integer> ei = PC_Module.guiList.keys();
			while(ei.hasMoreElements()){
				Integer i=ei.nextElement();
				Class cgui = PC_Module.guiList.get(i);
				if(cgui==c){
					TileEntity te = null;
					if(y>-1)
						te = world.getBlockTileEntity(x, y, z);
					PC_GresContainerManager cm = new PC_GresContainerManager(entityplayer, createGui(c, entityplayer, te));
					if(te!=null)
						ModLoader.serverOpenWindow((EntityPlayerMP)entityplayer, cm, i, te.xCoord, te.yCoord, te.zCoord);
					else
						ModLoader.serverOpenWindow((EntityPlayerMP)entityplayer, cm, i, x, y, z);
					break;
				}
			}
		}
	}
	
	public static void setTileEntity(EntityPlayer player, TileEntity tileEntity, Object... o){
		setTileEntityArray(player, tileEntity, o);
	}
	
	public static void setTileEntityArray(EntityPlayer player, TileEntity tileEntity, Object[] o) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
    	ObjectOutputStream sendData;
		try {
			sendData = new ObjectOutputStream(data);
			sendData.writeObject("TileEntity");
	        sendData.writeInt(tileEntity.xCoord);
	        sendData.writeInt(tileEntity.yCoord);
	        sendData.writeInt(tileEntity.zCoord);
	        sendData.writeInt(o.length);
	        for(int i=0; i<o.length; i++)
	        	sendData.writeObject(o[i]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send(player, data);
	}
	
	public static void setBlock(EntityPlayer player, Block block, Object... o){
		setBlockArray(player, block, o);
	}
	
	public static void setBlockArray(EntityPlayer player, Block block, Object[] o){
		ByteArrayOutputStream data = new ByteArrayOutputStream();
    	ObjectOutputStream sendData;
		try {
			sendData = new ObjectOutputStream(data);
			sendData.writeObject("Block");
	        sendData.writeInt(block.blockID);
	        sendData.writeInt(o.length);
	        for(int i=0; i<o.length; i++)
	        	sendData.writeObject(o[i]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send(player, data);
	}
	
	public static void sendToPacketHandler(EntityPlayer player, String name, Object... o){
		sendToPacketHandlerArray(player, name, o);
	}
	
	public static void sendToPacketHandlerArray(EntityPlayer player, String name, Object[] o){
		ByteArrayOutputStream data = new ByteArrayOutputStream();
    	ObjectOutputStream sendData;
		try {
			sendData = new ObjectOutputStream(data);
			sendData.writeObject("PacketHandler");
	        sendData.writeObject(name);
	        sendData.writeInt(o.length);
	        for(int i=0; i<o.length; i++)
	        	sendData.writeObject(o[i]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send(player, data);
	}
	
	public static void send(EntityPlayer player, ByteArrayOutputStream data){
        Packet250CustomPayload packet =  new Packet250CustomPayload("PowerCraft", data.toByteArray());
        if(player!=null)
	        if(player.worldObj.isRemote)
	        	ModLoader.clientSendPacket(packet);
	        else
	        	ModLoader.serverSendPacket(((EntityPlayerMP)player).serverForThisPlayer, packet);
        else{
        	System.out.println("Server to All Clients");
        	MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(packet);
        }
	}
	
	public static void registerDataMemory(PC_INBT mem){
		mod_PCcore.registerDataMemory(mem);
	}
	
	public static void unRegisterDataMemory(PC_INBT mem){
		mod_PCcore.unRegisterDataMemory(mem);
	}
	
}
