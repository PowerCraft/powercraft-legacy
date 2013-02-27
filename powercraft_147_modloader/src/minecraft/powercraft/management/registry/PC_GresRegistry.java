package powercraft.management.registry;

import java.util.HashMap;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import powercraft.management.block.PC_Block;
import powercraft.management.tileentity.PC_TileEntity;

public final class PC_GresRegistry {

	private static HashMap<String, Class> guis = new HashMap<String, Class>();
	
	public static void registerGres(String name, Class gui) {
		guis.put(name, gui);
	}

	public static void registerGresArray(Object[] o) {
		if (o == null) {
			return;
		}

		for (int i = 0; i < o.length; i += 2) {
			registerGres((String) o[i], (Class) o[i + 1]);
		}
	}

	public static void openGres(String name, EntityPlayer player,
			PC_TileEntity te, Object... o) {
		PC_RegistryServer.getInstance().openGres(name, player, te, o);
	}

	public static Class getGui(String name) {
		if(guis.containsKey(name)){
			return guis.get(name);
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
