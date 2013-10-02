package powercraft.api.multiblocks;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import powercraft.api.PC_Direction;
import powercraft.api.PC_INBT;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
public abstract class PC_MultiblockTileEntity implements PC_INBT {

	protected PC_MultiblockIndex index;
	protected PC_TileEntityMultiblock multiblock;
	protected int thickness;

	protected PC_MultiblockTileEntity(NBTTagCompound nbtTagCompound) {
		thickness = nbtTagCompound.getInteger("thickness");
	}
	
	protected PC_MultiblockTileEntity(int thickness) {

		this.thickness = thickness;
	}
	
	public PC_TileEntityMultiblock getTileEntity() {

		return multiblock;
	}


	public int getThickness() {

		return thickness;
	}


	public boolean isClient() {

		return multiblock.isClient();
	}


	protected void setIndexAndMultiblock(PC_MultiblockIndex index, PC_TileEntityMultiblock multiblock) {

		this.index = index;
		this.multiblock = multiblock;
	}


	protected boolean onAdded() {

		return true;
	}


	protected void onBreak() {

	}


	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(Random random) {

	}


	public void onNeighborBlockChange(int neighborID) {

	}


	public int getRedstonePowerValue(PC_Direction side) {

		return 0;
	}


	public int getLightValue() {

		return 0;
	}


	public boolean canConnectRedstone(PC_Direction side) {

		return false;
	}


	public int getLightOpacity() {

		return 0;
	}


	protected void onChunkUnload() {

	}


	@SideOnly(Side.CLIENT)
	public void renderWorldBlock(RenderBlocks renderer) {

	}


	public boolean canMixWith(PC_MultiblockTileEntity tileEntity) {

		return false;
	}


	public PC_MultiblockTileEntity mixWith(PC_MultiblockTileEntity tileEntity) {

		return this;
	}


	@Override
	public void saveToNBT(NBTTagCompound nbtCompoundTag) {
		nbtCompoundTag.setInteger("thickness", thickness);
	}


	public void invalidate() {

	}


	public void validate() {

	}


	public void update() {

	}


	public List<AxisAlignedBB> getCollisionBoxes() {

		AxisAlignedBB aabb = getSelectionBox();
		if (aabb == null) return null;
		List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
		list.add(aabb);
		return list;
	}


	public AxisAlignedBB getSelectionBox() {

		double thickness16 = thickness / 16.0;
		double thickness32 = thickness / 32.0;
		switch (index) {
			case CENTER:
				return AxisAlignedBB.getBoundingBox(0.5 - thickness32, 0.5 - thickness32, 0.5 - thickness32, 0.5 + thickness32, 0.5 + thickness32,
						0.5 + thickness32);
			case CORNERBOTTOMNORTHEAST:
				break;
			case CORNERBOTTOMNORTHWEST:
				break;
			case CORNERBOTTOMSOUTHEAST:
				break;
			case CORNERBOTTOMSOUTHWEST:
				break;
			case CORNERTOPNORTHEAST:
				break;
			case CORNERTOPNORTHWEST:
				break;
			case CORNERTOPSOUTHEAST:
				break;
			case CORNERTOPSOUTHWEST:
				break;
			case EDGEBOTTOMEAST:
				break;
			case EDGEBOTTOMNORTH:
				break;
			case EDGEBOTTOMSOUTH:
				break;
			case EDGEBOTTOMWEST:
				break;
			case EDGENORTHEAST:
				break;
			case EDGENORTHWEST:
				break;
			case EDGESOUTHEAST:
				break;
			case EDGESOUTHWEST:
				break;
			case EDGETOPEAST:
				break;
			case EDGETOPNORTH:
				break;
			case EDGETOPSOUTH:
				break;
			case EDGETOPWEST:
				break;
			case FACEBOTTOM:
				return AxisAlignedBB.getBoundingBox(0, 0, 0, 1, thickness16, 1);
			case FACEEAST:
				return AxisAlignedBB.getBoundingBox(1 - thickness16, 0, 0, 1, 1, 1);
			case FACENORTH:
				return AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, thickness16);
			case FACESOUTH:
				return AxisAlignedBB.getBoundingBox(0, 0, 1 - thickness16, 1, 1, 1);
			case FACETOP:
				return AxisAlignedBB.getBoundingBox(0, 1 - thickness16, 0, 1, 1, 1);
			case FACEWEST:
				return AxisAlignedBB.getBoundingBox(0, 0, 0, thickness16, 1, 1);
			default:
				break;
		}
		return null;
	}


	public abstract List<ItemStack> getDrop();


	public PC_MultiblockIndex getIndex() {

		return index;
	}

	@SideOnly(Side.CLIENT)
	public void renderTileEntityAt(float timeStamp) {
		
	}

}
