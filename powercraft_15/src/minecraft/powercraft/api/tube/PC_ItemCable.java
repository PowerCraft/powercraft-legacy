package powercraft.api.tube;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import powercraft.api.item.PC_Item;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public final class PC_ItemCable extends PC_Item {

	public static PC_ItemCable cable;
	
	public PC_ItemCable(int id) {
		super(id, "Cable");
		setCreativeTab(CreativeTabs.tabMisc);
		cable = this;
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit) {
		if(world.isRemote)
			return true;
		Block block = PC_Utils.getBlock(world, x, y, z);
		PC_Direction dir = PC_Direction.getFormMCDir(side);
		if(!(block instanceof PC_BlockTube)){
			if (block == Block.snow && (PC_Utils.getMD(world, x, y, z) & 7) < 1) {
				side = 1;
				dir = PC_Direction.getFormMCDir(side);
			} else {
				if(!PC_Utils.isBlockReplaceable(world, x, y, z)){
					PC_VecI offset = dir.getOffset();
					x += offset.x;
					y += offset.y;
					z += offset.z;
					dir = dir.mirror();
				}
			}
			
			Block block2 = PC_Utils.getBlock(world, x, y, z);
			
			if(!(block2 instanceof PC_BlockTube)){
				PC_BlockTube.tube.setBlockBloundsForCable(world, x, y, z);
				
				if (!player.canPlayerEdit(x, y, z, side, itemStack)) {
					return false;
				} else if (y == 255 && block.blockMaterial.isSolid()) {
					return false;
				} else if (world.canPlaceEntityOnSide(PC_BlockTube.tube.blockID, x, y, z, false, side, null, itemStack)) {
					
					if (PC_Utils.setBlock(world, x, y, z, PC_BlockTube.tube, 0, PC_Utils.BLOCK_UPDATE)) {
						world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), block.stepSound.getPlaceSound(),
								(block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
					}
					
				} 
			}
			
		}else{
			PC_TileEntityTube teTube = PC_Utils.getTE(world, x, y, z);
			if(teTube.getTube()==null){
				dir = dir.mirror();
			}
		}
			
		block = PC_Utils.getBlock(world, x, y, z);
		if(block instanceof PC_BlockTube){
			if(((PC_BlockTube)block).setCable(world, x, y, z, dir, itemStack.getItemDamage())){
				itemStack.stackSize--;
				return true;
			}
		}
		return false;
	}

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		for(int i=0; i<16; i++){
			names.add(new LangEntry(getUnlocalizedName()+"."+i, "Cable "+ItemDye.dyeColorNames[i]));
		}
		return names;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return getUnlocalizedName()+"."+itemStack.getItemDamage();
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		for(int i=0; i<16; i++){
			arrayList.add(new ItemStack(this, 1, i));
		}
		return arrayList;
	}
	
	public static int getColor(int cable){
		return ItemDye.dyeColors[cable];
	}

	@Override
	public int getColorFromItemStack(ItemStack itemStack, int pass) {
		return getColor(itemStack.getItemDamage());
	}
	
}
