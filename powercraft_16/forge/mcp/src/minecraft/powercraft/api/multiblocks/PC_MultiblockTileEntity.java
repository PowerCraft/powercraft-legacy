package powercraft.api.multiblocks;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
public abstract class PC_MultiblockTileEntity {

	protected PC_MultiblockIndex index;
	protected PC_TileEntityMultiblock multiblock;
	protected int thickness;


	public PC_TileEntityMultiblock getTileEntity() {

		return multiblock;
	}


	protected PC_MultiblockTileEntity(int thickness) {

		this.thickness = thickness;
	}


	public int getThickness() {

		return thickness;
	}


	public boolean isClient() {

		return multiblock.isClient();
	}


	public void setIndexAndMultiblock(PC_MultiblockIndex index, PC_TileEntityMultiblock multiblock) {

		this.index = index;
		this.multiblock = multiblock;
	}


	public boolean onAdded() {

		return true;
	}


	public void onBreak() {

	}


	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(Random random) {

	}


	public void onNeighborBlockChange(int neighborID) {

	}


	public int getRedstonePowerValue(int side) {

		return 0;
	}


	public int getLightValue() {

		return 0;
	}


	public boolean canConnectRedstone(int side) {

		return false;
	}


	public int getLightOpacity() {

		return 0;
	}


	public void onChunkUnload() {

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


	public void loadFromNBT(NBTTagCompound nbtCompoundTag) {

	}


	public void saveToNBT(NBTTagCompound nbtCompoundTag) {

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

}
