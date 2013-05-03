package powercraft.api.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;
import powercraft.launcher.loader.PC_ModuleObject;

public class PC_ItemBlock extends ItemBlock implements PC_IItemInfo {
	
	private static final PC_Direction[] sides = {PC_Direction.BOTTOM, PC_Direction.TOP, PC_Direction.FRONT, PC_Direction.BACK, PC_Direction.LEFT, PC_Direction.RIGHT};
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
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int dir, float xHit, float yHit, float zHit) {
		int blockID = PC_Utils.getBID(world, x, y, z);
		int metadata = PC_Utils.getMD(world, x, y, z);
		Block block = Block.blocksList[blockID];
		
		PC_Direction pcDir = PC_Direction.getFormMCDir(dir);
		
		if (blockID == Block.snow.blockID && (metadata & 7) < 1) {
			dir = 1;
		} else if (!PC_Utils.isBlockReplaceable(world, x, y, z)) {
			
			PC_VecI offset=null;
			
			if(block instanceof PC_Block){
				offset = ((PC_Block) block).moveBlockTryToPlaceAt(world, x, y, z, pcDir, xHit, yHit, zHit, itemStack, entityPlayer);
			}
			
			if(offset==null){
				offset = pcDir.getOffset();
			}
			
			x += offset.x;
			y += offset.y;
			z += offset.z;
			
		}
		
		if (itemStack.stackSize == 0) {
			return false;
		} 

		PC_VecI move;
		do{
			
			move=null;
			blockID = PC_Utils.getBID(world, x, y, z);
			metadata = PC_Utils.getMD(world, x, y, z);
			
			if(blockID!=0){
				block = Block.blocksList[blockID];
				if (!((blockID == Block.snow.blockID && (metadata & 7) < 1) || PC_Utils.isBlockReplaceable(world, x, y, z))) {
					if(block instanceof PC_Block){
						move = ((PC_Block) block).moveBlockTryToPlaceAt(world, x, y, z, pcDir, xHit, yHit, zHit, itemStack, entityPlayer);
					}
					if(move==null){
						return false;
					}
					x += move.x;
					y += move.y;
					z += move.z;
					continue;
				}
				
			}
			
			blockID = getBlockID();
			block = Block.blocksList[blockID];
			
			for(int i=0; i<6; i++){
				PC_VecI offset = sides[i].getOffset();
				Block b = PC_Utils.getBlock(world, x+offset.x, y+offset.y, z+offset.z);
				if(b instanceof PC_Block){
					PC_Block pcBlock = (PC_Block)b;
					move = pcBlock.moveBlockTryToPlaceOnSide(world, x, y, z, sides[i].mirror(), xHit, yHit, zHit, pcBlock, itemStack, entityPlayer);
					if(move!=null)
						break;
				}
			}
			if(move!=null){
				x += move.x;
				y += move.y;
				z += move.z;
			}
		}while(move!=null);
		
		blockID = getBlockID();
		block = Block.blocksList[blockID];
		
		if (!entityPlayer.canPlayerEdit(x, y, z, dir, itemStack)) {
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
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int dir, float hitX, float hitY, float hitZ,
			int metadata) {
		
		int blockID = getBlockID();
		
		if (!PC_Utils.setBID(world, x, y, z, blockID, metadata)) {
			return false;
		}
		
		if (PC_Utils.getBID(world, x, y, z) == blockID) {
			Block.blocksList[blockID].onBlockPlacedBy(world, x, y, z, player, stack);
			TileEntity te = PC_Utils.getTE(world, x, y, z);
			if(te instanceof PC_TileEntity){
				((PC_TileEntity)te).create(stack, player, world, x, y, z, dir, hitX, hitY, hitZ);
			}
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
