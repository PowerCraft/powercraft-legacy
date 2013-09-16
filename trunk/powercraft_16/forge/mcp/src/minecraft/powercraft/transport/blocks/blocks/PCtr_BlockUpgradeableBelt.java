package powercraft.transport.blocks.blocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Vec3;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_BlockWithoutTileEntity;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.transport.helper.PCtr_BeltHelper;
import powercraft.transport.helper.PCtr_MaterialConveyor;

@PC_BlockInfo(name = "UpgradableBelt", blockid = "upgradableBelt", defaultid = 2051)
public class PCtr_BlockUpgradeableBelt extends PC_BlockWithoutTileEntity
{

	public PCtr_BlockUpgradeableBelt(int id)
	{
		super(id, PCtr_MaterialConveyor.getMaterial());
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public void loadIcons()
	{
		this.blockIcon = PC_TextureRegistry.registerIcon("upgradeablebelt");

	}

	@Override
	public void registerRecipes()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
	{
		if (!entity.onGround) return;
		PC_Vec3 emotion = new PC_Vec3(entity.motionX, entity.motionY, entity.motionZ);
		PC_Direction eDir = PCtr_BeltHelper.returnPrimaryDirection(emotion, true);
		switch (eDir)
		{
			case NORTH:
			case SOUTH:
				entity.motionZ *= 2;
				break;
			case EAST:
			case WEST:
				entity.motionX *= 2;
			//$FALL-THROUGH$
			case DOWN:
				break;
			case UP:
				break;				
			case UNKNOWN:
				break;

			default:
				break;
		}
	}

	@Override
	public boolean isOpaqueCube()
	{
		return PCtr_BeltHelper.isOpaqueCube();
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return PCtr_BeltHelper.renderAsNormalBlock();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return PCtr_BeltHelper.getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return PCtr_BeltHelper.getSelectedBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
	{
		PCtr_BeltHelper.setBlockBoundsBasedOnState(this, iblockaccess, i, j, k);
	}

	@Override
	public void setBlockBoundsForItemRender()
	{
		PCtr_BeltHelper.setBlockBoundsForItemRender(this);
	}

	@Override
	public int tickRate(World world)
	{
		return PCtr_BeltHelper.tickRate(world);
	}
}
