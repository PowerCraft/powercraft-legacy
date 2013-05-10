package powercraft.api.hacks;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderPlayer;
import net.minecraft.src.World;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_ItemArmor;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.utils.PC_Utils;

public class PC_Hooks {
	
	public static String getArmorTexture(ItemArmor itemArmor, ItemStack stack, Entity entity, int slot, int layer, String _default){
		if (itemArmor instanceof PC_ItemArmor) {
			String hook = ((PC_ItemArmor) itemArmor).getArmorTexture(stack, entity, slot, layer);
			if(hook!=null)
				return hook;
		}
		if(_default!=null)
			return _default;
		return "/armor/" + getArmorFilenamePrefix(itemArmor.renderIndex) + "_" + (slot == 2 ? 2 : 1) + (layer==1?"":"_b")+".png";
	}

	public static ModelBiped getArmorModel(ItemArmor itemArmor, ItemStack stack, Entity entity, int slot, ModelBiped _default) {
		if (itemArmor instanceof PC_ItemArmor) {
			ModelBiped hook = ((PC_ItemArmor) itemArmor).getArmorModel(entity, stack, slot);
			if(hook!=null)
				return hook;
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
