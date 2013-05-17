package powercraft.api.tube;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_Item;
import powercraft.api.registry.PC_TextureRegistry;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public abstract class PC_ItemTube extends PC_Item {
	
	private String[] tubeIconNames;
	private Icon[] tubeIcons;	
	
	protected PC_ItemTube(int id, String textureName, String... textureNames) {
		super(id, textureName);
		tubeIconNames = textureNames;
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit) {
		if(world.isRemote)
			return true;
		Block block = PC_Utils.getBlock(world, x, y, z);
		ItemStack copy = itemStack.copy();
		copy.stackSize = 1;
		boolean move = true;
		if(block instanceof PC_BlockTube){
			if(((PC_BlockTube)block).setTube(world, x, y, z, copy)){
				itemStack.stackSize--;
				return true;
			}
		}else{
			if (block == Block.snow && (PC_Utils.getMD(world, x, y, z) & 7) < 1) {
				side = 1;
				move = false;
			} else {
				move = !PC_Utils.isBlockReplaceable(world, x, y, z);
			}
		}
		if(move){
			PC_VecI offset = PC_Direction.getFromMCDir(side).getOffset();
			x += offset.x;
			y += offset.y;
			z += offset.z;
		}
		
		PC_BlockTube.tube.setBlockBloundsForTube(world, x, y, z);
		
		if (!player.canPlayerEdit(x, y, z, side, itemStack)) {
			return false;
		} else if (y == 255 && block.blockMaterial.isSolid()) {
			return false;
		} else if (world.canPlaceEntityOnSide(PC_BlockTube.tube.blockID, x, y, z, false, side, null, itemStack)) {
			
			if (PC_Utils.setBlock(world, x, y, z, PC_BlockTube.tube)) {
				world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), block.stepSound.getPlaceSound(),
						(block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
			}
			
		} 
		block = PC_Utils.getBlock(world, x, y, z);
		if(block instanceof PC_BlockTube){
			if(((PC_BlockTube)block).setTube(world, x, y, z, copy)){
				itemStack.stackSize--;
				return true;
			}
		}
		return false;
	}

	protected boolean isCompatibleTo(ItemStack thisTube, ItemStack otherTube) {
		return ((PC_ItemTube)otherTube.getItem()).getTubeType()==getTubeType();
	}

	public static boolean areTubesCompatible(ItemStack tube1, ItemStack tube2){
		if(tube1==null||tube2==null)
			return false;
		return ((PC_ItemTube)tube1.getItem()).isCompatibleTo(tube1, tube2)||((PC_ItemTube)tube2.getItem()).isCompatibleTo(tube2, tube1);
	}

	public Icon getIconFromSide(ItemStack tube, PC_Direction formMCDir) {
		int index = formMCDir.getMCDir();
		if(index>=tubeIcons.length){
			index = tubeIcons.length-1;
		}
		return tubeIcons[index];
	}

	public void onIconLoading() {
		tubeIcons = new Icon[tubeIconNames.length];
		for(int i=0; i<tubeIconNames.length; i++){
			tubeIcons[i] = PC_TextureRegistry.registerIcon(getModule(), tubeIconNames[i]);
		}
	}
	
	public abstract PC_TubeType getTubeType();

	public boolean canTubeConnectTo(IBlockAccess world, int x, int y, int z, ItemStack tube, PC_Direction dir) {
		PC_VecI offset = dir.getOffset();
		x += offset.x;
		y += offset.y;
		z += offset.z;
		Block block = PC_Utils.getBlock(world, x, y, z);
		if(block instanceof PC_Block){
			return ((PC_Block)block).canTubeConnectTo(world, x, y, z, tube, dir.mirror());
		}
		return false;
	}
	
}
