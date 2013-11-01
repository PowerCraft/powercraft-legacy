package powercraft.api.multiblocks.covers;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import powercraft.api.multiblocks.PC_BlockMultiblock;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;

public class PC_CoverTileEntity extends PC_MultiblockTileEntity {

	private int metadata;
	private Block block;
	
	public PC_CoverTileEntity(NBTTagCompound nbtTagCompound) {
		super(nbtTagCompound);
		metadata = nbtTagCompound.getInteger("metadata");
		block = Block.blocksList[nbtTagCompound.getInteger("block")];
	}
	
	public PC_CoverTileEntity(ItemStack itemStack) {
		super(PC_CoverItem.getThickness(itemStack));
		ItemStack inner = PC_CoverItem.getInner(itemStack);
		metadata = inner.getItemDamage();
		block = Block.blocksList[inner.itemID];
	}

	@Override
	public List<ItemStack> getDrop() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		list.add(PC_CoverItem.getCoverItem(thickness, metadata, block));
		return list;
	}

	@Override
	public boolean isSolid() {
		return true;
	}

	@Override
	public void renderWorldBlock(RenderBlocks renderer) {
		PC_BlockMultiblock.colorMultiplier = 0xFFFFFFFF;
		Icon[] icons = new Icon[6];
		for(int i=0; i<6; i++){
			icons[i] = block.getIcon(i, metadata);
		}
		PC_BlockMultiblock.setIcons(icons);
		AxisAlignedBB aabb = getSelectionBox();
		renderer.setRenderBounds(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
		renderer.renderStandardBlock(PC_BlockMultiblock.block, multiblock.xCoord, multiblock.yCoord, multiblock.zCoord);
	}

	@Override
	public void saveToNBT(NBTTagCompound nbtCompoundTag) {
		super.saveToNBT(nbtCompoundTag);
		nbtCompoundTag.setInteger("metadata", metadata);
		nbtCompoundTag.setInteger("block", block.blockID);
	}

	@Override
	public ItemStack getPickBlock() {
		return PC_CoverItem.getCoverItem(thickness, metadata, block);
	}

	@Override
	public float getHardness(EntityPlayer player) {
		return block.blockHardness;
	}
	
}
