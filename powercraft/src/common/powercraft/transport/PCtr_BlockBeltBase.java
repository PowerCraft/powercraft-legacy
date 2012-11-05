package powercraft.transport;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.core.PC_Block;
import powercraft.core.PC_CoordI;
import powercraft.core.PC_ICraftingToolDisplayer;
import powercraft.core.PC_IRotatedBox;
import powercraft.core.PC_ISwapTerrain;
import powercraft.core.PC_Renderer;

public abstract class PCtr_BlockBeltBase extends PC_Block implements PC_IRotatedBox, PC_ISwapTerrain {

	public PCtr_BlockBeltBase(int id) {
		super(id, 0, PCtr_MaterialConveyor.getMaterial());
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, PCtr_BeltHelper.HEIGHT, 1.0F);
		setHardness(0.22F);
		setResistance(8.0F);
		setStepSound(Block.soundMetalFootstep);
		setCreativeTab(CreativeTabs.tabTransport);
	}

	@Override
	public abstract void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity);
	@Override
	public abstract String getDefaultName();
	@Override
	public abstract int getBlockTextureFromSideAndMetadata(int i, int j);
	@Override
	public abstract boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9);
	
	@Override
	public String getTerrainFile() {
		return mod_PowerCraftTransport.getInstance().getTerrainFile();
	}

	@Override
	public int getRotation(int meta) {
		return PCtr_BeltHelper.getRotation(meta);
	}

	@Override
	public boolean renderItemHorizontal() {
		return false;
	}

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}
	
	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return getBlockTextureFromSideAndMetadata(l, iblockaccess.getBlockMetadata(i, j, k));
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return PC_Renderer.getRendererID(true);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(i, 0.0F + j, k, (i + 1), (j + PCtr_BeltHelper.HEIGHT_COLLISION + 0.0F), (k + 1));
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		float f = 0;
		f = 0.0F + PCtr_BeltHelper.HEIGHT_SELECTED;
		return AxisAlignedBB.getBoundingBox(i, 0.0F + j, k, (i + 1), j + f, (float) k + 1);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + PCtr_BeltHelper.HEIGHT, 1.0F);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 0.6F, 1.0F);
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return l != 1;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = PCtr_BeltHelper.getPlacedMeta(entityliving);
		world.setBlockMetadataWithNotify(i, j, k, l);
	}

	@Override
	public int tickRate() {
		return 1;
	}
	
}
