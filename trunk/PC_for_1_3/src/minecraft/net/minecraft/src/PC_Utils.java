package net.minecraft.src;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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

	public static MinecraftServer mcs() {
		return MinecraftServer.getServer();
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

	public static PC_GresBase createGui(Class<? extends PC_GresBase> c, EntityPlayer player, Object[] o){
		if(c==null)
			return null;
		PC_GresBase gb=null;
		Constructor conA[] = c.getConstructors();
		for(Constructor con:conA){
			boolean ok=true;
			Class p[] = con.getParameterTypes();
			Object l[] = new Object[p.length];
			int oP=0;
			for(int i=0; i<p.length; i++){
				if(p[i] == EntityPlayer.class){
					l[i] = player;
				}else if(p[i] == TileEntity.class){
					if(oP+2<o.length){
						l[i] = player.worldObj.getBlockTileEntity((Integer)o[oP++], (Integer)o[oP++], (Integer)o[oP++]);
					}else
						ok=false;
				}else if(p[i] == Object.class){
					l[i] = o[oP++];
				}else{
					ok=false;
					break;
				}
			}
			if(ok){
				try {
					gb = (PC_GresBase)con.newInstance(l);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return gb;
	}
	
	public static void openGres(EntityPlayer entityplayer, Class<? extends PC_GresBase> c, Object... o){
		if(!(entityplayer instanceof EntityPlayerMP))
			return;
		if(c==null)
			return;
		int guiID = 0;
		try{
			Field var6 = EntityPlayerMP.class.getDeclaredFields()[17];
	        var6.setAccessible(true);
	        guiID = var6.getInt(entityplayer);
	        guiID = guiID % 100 + 1;
	        var6.setInt(entityplayer, guiID);
		} catch (Exception e){
            e.printStackTrace();
        }
		ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream sendData;
		try
        {
            sendData = new ObjectOutputStream(data);
            sendData.writeObject("GUI");
            sendData.writeObject(c.getName());
            sendData.writeInt(guiID);
            sendData.writeInt(entityplayer.dimension);
            int size = o.length;
            sendData.writeInt(size);
            for(int i=0; i<size; i++){
            	sendData.writeObject(o[i]);
            }
            sendData.writeObject("End GUI");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
		send(entityplayer, data);
		PC_GresContainerManager cm = new PC_GresContainerManager(entityplayer, createGui(c, entityplayer, o));      
        entityplayer.craftingInventory = cm;
        entityplayer.craftingInventory.windowId = guiID;
        entityplayer.craftingInventory.addCraftingToCrafters((EntityPlayerMP)entityplayer);
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
	        sendData.writeObject("End TileEntity");
		} catch (IOException e) {
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
	        sendData.writeObject("End Block");
		} catch (IOException e) {
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
	        sendData.writeObject("End PacketHandler");
		} catch (IOException e) {
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
	
}
