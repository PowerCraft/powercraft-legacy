package powercraft.transport.blocks.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.blocks.PC_BlockRotated;
import powercraft.transport.helper.PCtr_BeltHelper;
import powercraft.transport.helper.PCtr_MaterialConveyor;

public class PCtr_BlockUpgradeableBelt extends PC_BlockRotated {

	public PCtr_BlockUpgradeableBelt(int id) {
		super(id, PCtr_MaterialConveyor.getMaterial());
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public void loadIcons() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerRecipes() {
		// TODO Auto-generated method stub

	}

	
	@Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity){
		if(!entity.onGround) return;
		
    }
    
	@Override
	public boolean isOpaqueCube() {
	    return PCtr_BeltHelper.isOpaqueCube();
	}
	@Override
	public boolean renderAsNormalBlock() {
	    return PCtr_BeltHelper.renderAsNormalBlock();
	}
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i,
			int j, int k) {
			    return PCtr_BeltHelper.getCollisionBoundingBoxFromPool(world, i, j, k, false);
			}
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i,
			int j, int k) {
		return PCtr_BeltHelper.getSelectedBoundingBoxFromPool(world, i,j,k, false);
	}
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i,
			int j, int k) {
		PCtr_BeltHelper.setBlockBoundsBasedOnState(this, iblockaccess, i, j, k, false);
			}
	@Override
	public void setBlockBoundsForItemRender() {
		PCtr_BeltHelper.setBlockBoundsForItemRender(this, false);
	}
	@Override
	public int tickRate(World world) {
	    return PCtr_BeltHelper.tickRate(world);
	}
}
