package powercraft.api.registry;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import powercraft.api.block.PC_Block;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.tileentity.PC_TileEntity;

public final class PC_GresRegistry {

	private static HashMap<String, Class<? extends PC_IGresClient>> guis = new HashMap<String, Class<? extends PC_IGresClient>>();
	private static HashMap<String, Class<? extends PC_GresBaseWithInventory>> containers = new HashMap<String, Class<? extends PC_GresBaseWithInventory>>();
	
	public static void registerGresGui(String name, Class<? extends PC_IGresClient> gui) {
		guis.put(name, gui);
	}

	public static void registerGresContainer(String name, Class<? extends PC_GresBaseWithInventory> container) {
		containers.put(name, container);
	}

	public static void openGres(String name, EntityPlayer player,
			PC_TileEntity te, Object... o) {
		PC_RegistryServer.getInstance().openGres(name, player, te, o);
	}

	public static Class<? extends PC_IGresClient> getGui(String name) {
		if(guis.containsKey(name)){
			return guis.get(name);
		}
		return null;
	}
	
	public static Class<? extends PC_GresBaseWithInventory> getContainer(String name) {
		if(containers.containsKey(name)){
			return containers.get(name);
		}
		return null;
	}
	
	public static boolean shouldOpenGui(Block block, ItemStack itemStack) {
		if (itemStack == null) {
			return true;
		}
		if (!(itemStack.getItem() instanceof ItemBlock)) {
			return true;
		}
		ItemBlock itemBlock = (ItemBlock) itemStack.getItem();
		Block iBlock = Block.blocksList[itemBlock.getBlockID()];
		if (iBlock instanceof PC_Block) {
			PC_Block pcBlock = (PC_Block) iBlock;
			Object o = pcBlock.msg(PC_MSGRegistry.MSG_OPEN_GUI_OR_PLACE_BLOCK, block);
			if (o instanceof Boolean) {
				return (Boolean) o;
			}
		}
		return false;
	}
	
}
