package net.minecraft.src;

import java.util.HashSet;
import java.util.Set;

/**
 * PowerCraft block type tests.<br>
 * Checks block flags at given position in world.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PC_BlockUtils {
	
	public static boolean isPowerCraftBlock(World world, PC_CoordI pos, String flag){
		return getBlockFlags(world, pos).contains(flag);
	}
	
	/**
	 * Check if block in world has given type flag
	 * @param world the world
	 * @param pos position
	 * @param flag block flag to check
	 * @return has flag
	 */
	public static boolean hasFlag(World world, PC_CoordI pos, String flag){
		return getBlockFlags(world, pos).contains(flag);
	}

	
	/**
	 * Check if block in stack has given type flag
	 * @param stack stack
	 * @param flag block flag to check
	 * @return has flag
	 */
	public static boolean hasFlag(ItemStack stack, String flag){
		return getItemFlags(stack).contains(flag);
	}
	
	
	/**
	 * Get block flags in world at given position.
	 * @param world the world
	 * @param pos the coordinate
	 * @return set of flags, or empty set if not instance of {@link PC_IBlockType}
	 */
	public static Set<String> getBlockFlags(World world, PC_CoordI pos){
		
		int id = pos.getId(world);

		if (Block.blocksList[id] != null && Block.blocksList[id] instanceof PC_IBlockType) {
			PC_IBlockType type = (PC_IBlockType) Block.blocksList[id];
			Set<String> flags = type.getBlockFlags(world, pos);
			flags.add("POWERCRAFT");
			return flags;
		}

		return new HashSet<String>();
	}
	

	
	/**
	 * Get item-block flags for itemstack.
	 * @param stack the stack
	 * @return set of flags, or empty set if block not instance of {@link PC_IBlockType}
	 */
	public static Set<String> getItemFlags(ItemStack stack){
		
		if(stack == null) return new HashSet<String>();
		if(stack.getItem() instanceof ItemBlock) return new HashSet<String>();

		if (Block.blocksList[stack.getItem().shiftedIndex] != null && Block.blocksList[stack.getItem().shiftedIndex] instanceof PC_IBlockType) {
			PC_IBlockType type = (PC_IBlockType) Block.blocksList[stack.getItem().shiftedIndex];
			Set<String> flags = type.getItemFlags(stack.getItemDamage());
			flags.add("POWERCRAFT");
			return flags;
		}

		return new HashSet<String>();
	}

}
