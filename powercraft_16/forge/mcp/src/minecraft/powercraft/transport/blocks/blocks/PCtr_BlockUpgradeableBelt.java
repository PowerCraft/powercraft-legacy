package powercraft.transport.blocks.blocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Vec3;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_BlockRotated;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.api.upgrade.PC_ItemUpgrade;
import powercraft.transport.helper.PCtr_BeltHelper;
import powercraft.transport.helper.PCtr_MaterialConveyor;
import powercraft.tutorial.PC_ItemTutorial;
import powercraft.tutorial.PC_TileEntityTutorial;

@PC_BlockInfo(name = "UpgradableBelt", blockid = "upgradableBelt", defaultid = 2051,tileEntity=PC_TileEntityTutorial.class)
public class PCtr_BlockUpgradeableBelt extends PC_BlockRotated
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
		PC_TileEntityTutorial tentity = PC_Utils.getTE(world, i, j, k);
		if (!entity.onGround) return;
		PC_Vec3 emotion = new PC_Vec3(entity.motionX, entity.motionY, entity.motionZ);
		PC_Direction eDir = PCtr_BeltHelper.returnPrimaryDirection(emotion, true);
		switch (eDir)
		{
			case NORTH:
			case SOUTH:
				entity.motionZ *= tentity.speed; // use the new upgradeable tile entity speed 
				break;
			case EAST:
			case WEST:
				entity.motionX *= tentity.speed;
			//$FALL-THROUGH$  <-- not sure what this means (Buggi)
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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit)
	{
		PC_ItemUpgrade[] temp = new PC_ItemUpgrade[1];
		temp[0] = new PC_ItemTutorial();
		PC_TileEntityTutorial tentity = PC_Utils.getTE(world, x, y, z);
		
		tentity.onUpgradesChanged(temp); // manually fire the event with the new item
		
		return false;		
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
	@Override
	public int getLightOpacity(World world, int x, int y, int z)
	{
		// TODO Auto-generated method stub
		//System.out.println("getLightOpacity inside block");
		return 0;
		//return super.getLightOpacity(world, x, y, z);
	}
}
