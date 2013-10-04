package powercraft.api.multiblocks.cable;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;
import powercraft.api.multiblocks.PC_MultiblockIndex;
import powercraft.api.multiblocks.PC_MultiblockItem;
import powercraft.api.multiblocks.PC_MultiblockType;
import powercraft.api.multiblocks.PC_TileEntityMultiblock;


public abstract class PC_CableItem extends PC_MultiblockItem {

	public PC_CableItem(int id) {

		super(id);
	}


	@Override
	public PC_MultiblockType getMultiblockType() {

		return PC_MultiblockType.FACE;
	}

	@Override
	public int handleMultiblockClick(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float xHit,
			float yHit, float zHit, boolean secoundTry) {
		PC_TileEntityMultiblock tileEntityMultiblock = PC_Utils.getTE(world, x, y, z);
		PC_Direction bestSide = null;
		float minDist = 100.0f;
		for (PC_Direction dir : PC_Direction.VALID_DIRECTIONS) {
			float xD = (dir.offsetX + 1) / 2.0f - xHit;
			float yD = (dir.offsetY + 1) / 2.0f - yHit;
			float zD = (dir.offsetZ + 1) / 2.0f - zHit;
			float dist = (float) Math.sqrt(xD * xD + yD * yD + zD * zD);
			if (dist < minDist) {
				bestSide = dir;
				minDist = dist;
			}
		}
		if (bestSide!=null && tileEntityMultiblock.setMultiblockTileEntity(PC_MultiblockIndex.FACEINDEXFORDIR[bestSide.ordinal()], getTileEntity(itemStack))) {
			itemStack.stackSize--;
			return 1;
		}
		return -1;
	}
	
}
