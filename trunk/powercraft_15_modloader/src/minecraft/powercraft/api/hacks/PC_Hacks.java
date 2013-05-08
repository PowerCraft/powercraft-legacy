package powercraft.api.hacks;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderPlayer;
import net.minecraft.src.World;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_ItemArmor;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.utils.PC_Utils;

public class PC_Hacks {
	
	public static String getArmorTextureFile(ItemStack itemStack, String _default) {
		Item item = itemStack.getItem();
		if (item instanceof PC_ItemArmor) {
			return ((PC_ItemArmor) item).getArmorTextureFile(itemStack);
		}
		return _default;
	}

	public static String getArmorFilenamePrefix(int index){
		String[] array = PC_ReflectHelper.getValue(RenderPlayer.class, RenderPlayer.class, 3, String[].class);
		return array[index];
	}
	
	public static boolean canBlockCatchFire(IBlockAccess world, int x, int y, int z) {
		Block b = PC_Utils.getBlock(world, x, y, z);
		int meta = PC_Utils.getMD(world, x, y, z);
		if (b instanceof PC_Block) {
			return ((PC_Block) b).isFlammable(world, x, y, z, PC_Utils.getMD(world, x, y, z));
		}
		return false;
	}

	public static int getChanceToEncourageFire(World world, int x, int y, int z, int chance) {
		Block b = PC_Utils.getBlock(world, x, y, z);
		int meta = PC_Utils.getMD(world, x, y, z);
		int newChance=0;
		if (b instanceof PC_Block) {
			newChance = ((PC_Block) b).getFlammability(world, x, y, z, meta);
		}
		return newChance>chance?newChance:chance;
	}
	
}
