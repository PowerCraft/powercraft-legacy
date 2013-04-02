package powercraft.api.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;
import powercraft.launcher.loader.PC_ModuleObject;

public class PC_ItemBlock extends ItemBlock implements PC_IItemInfo {
	
	private PC_ModuleObject module;
	
	public PC_ItemBlock(int id) {
		super(id);
	}
	
	public void setModule(PC_ModuleObject module) {
		this.module = module;
	}
	
	@Override
	public PC_ModuleObject getModule() {
		return module;
	}
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> itemStacks) {
		itemStacks.add(new ItemStack(this));
		return itemStacks;
	}
	
	@Override
	public boolean showInCraftingTool() {
		return true;
	}
	
	@Override
	public void getSubItems(int index, CreativeTabs creativeTab, List list) {
		list.addAll(getItemStacks(new ArrayList<ItemStack>()));
	}
	
	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int dir, float xHit, float yHit, float zHit) {
		int blockID = PC_Utils.getBID(world, x, y, z);
		int metadata = PC_Utils.getMD(world, x, y, z);
		
		if (blockID == Block.snow.blockID && (metadata & 7) < 1) {
			dir = 1;
		} else if (!PC_Utils.isBlockReplaceable(world, x, y, z)) {
			
			PC_VecI offset = PC_Direction.getFormMCDir(dir).getOffset();
			
			x += offset.x;
			y += offset.y;
			z += offset.z;
			
		}
		
		blockID = getBlockID();
		Block block = Block.blocksList[blockID];
		
		if (itemStack.stackSize == 0) {
			return false;
		} else if (!entityPlayer.canPlayerEdit(x, y, z, dir, itemStack)) {
			return false;
		} else if (y == 255 && block.blockMaterial.isSolid()) {
			return false;
		} else if (world.canPlaceEntityOnSide(blockID, x, y, z, false, dir, entityPlayer, itemStack)) {
			metadata = this.getMetadata(itemStack.getItemDamage());
			metadata = block.onBlockPlaced(world, x, y, z, dir, xHit, yHit, zHit, metadata);
			if (block instanceof PC_Block) {
				metadata = ((PC_Block) block).makeBlockMetadata(itemStack, entityPlayer, world, x, y, z, dir, xHit, yHit, zHit, metadata);
			}
			
			if (placeBlockAt(itemStack, entityPlayer, world, x, y, z, dir, xHit, yHit, zHit, metadata)) {
				world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), block.stepSound.getPlaceSound(),
						(block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				--itemStack.stackSize;
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns true if the given ItemBlock can be placed on the given side of the given block position.
	 */
	public boolean canPlaceItemBlockOnSide(World world, int x, int y, int z, int dir, EntityPlayer entityPlayer, ItemStack itemStack) {
		int blockID = PC_Utils.getBID(world, x, y, z);
		
		if (blockID == Block.snow.blockID) {
			dir = 1;
		} else if (!PC_Utils.isBlockReplaceable(world, x, y, z)) {
			PC_VecI offset = PC_Direction.getFormMCDir(dir).getOffset();
			
			x += offset.x;
			y += offset.y;
			z += offset.z;
		}
		
		return world.canPlaceEntityOnSide(getBlockID(), x, y, z, false, dir, (Entity) null, itemStack);
	}
	
	/**
	 * Called to actually place the block, after the location is determined
	 * and all permission checks have been made.
	 * 
	 * @param stack
	 *            The item stack that was used to place the block. This can be changed inside the method.
	 * @param player
	 *            The player who is placing the block. Can be null if the block is not being placed by a player.
	 * @param dir
	 *            The dir the player (or machine) right-clicked on.
	 */
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int dir, float hitX, float hitY, float hitZ,
			int metadata) {
		
		int blockID = getBlockID();
		
		if (!PC_Utils.setBID(world, x, y, z, blockID, metadata)) {
			return false;
		}
		
		if (PC_Utils.getBID(world, x, y, z) == blockID) {
			Block.blocksList[blockID].onBlockPlacedBy(world, x, y, z, player, stack);
			Block.blocksList[blockID].onPostBlockPlaced(world, x, y, z, metadata);
		}
		
		return true;
	}
	
	public void doCrafting(ItemStack itemStack, InventoryCrafting inventoryCrafting) {
		
	}
	
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		PC_Block block = (PC_Block) Block.blocksList[getBlockID()];
		names.add(new LangEntry(block.getUnlocalizedName() + ".name", block.getName()));
		return names;
	}
	
	public int getBurnTime(ItemStack fuel) {
		return 0;
	}
	
}
