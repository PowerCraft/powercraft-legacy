package powercraft.transport.blocks.blocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Vec3I;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_BlockWithoutTileEntity;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.transport.helper.PCtr_BeltHelper;
import powercraft.transport.helper.PCtr_MaterialConveyor;

@PC_BlockInfo(name = "PrimitivePlate", blockid = "primitivePlate", defaultid = 2050)
public class PCtr_BlockPrimitivePlate extends PC_BlockWithoutTileEntity
{
	public PCtr_BlockPrimitivePlate(int id)
	{
		super(id, PCtr_MaterialConveyor.getMaterial());
		setCreativeTab(CreativeTabs.tabBlock);
		this.slipperiness = 0;
	}

	@Override
	public void loadIcons()
	{
		this.blockIcon = PC_TextureRegistry.registerIcon("primitiveplate");
	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k,
			Entity entity)
	{
		boolean tmp;
		// if(entity instanceof EntityPlayer){
		// float f = ((EntityPlayer) entity).getRotationYawHead();
		// if(f<)
		// }
		if (entity.onGround) {
			PC_Vec3I current_pos = new PC_Vec3I(i,j,k); 
			MoveEntity(world, entity, current_pos);
		}
	}

	private void MoveEntity(World world, Entity entity, PC_Vec3I current_pos)
	{
		// this speeds up the items, and I noticed the items stopped after a
		// certain time
		// I didn't think you wanted the items sped up but just continue on the
		// path		
		/**
		 * if(isXDir){ 
		 * entity.motionX*=multiplier;
		 * entity.motionZ+=entity.motionZ*4*factor; 
		 * }else{
		 * entity.motionX+=entity.motionX*4*factor; 
		 * entity.motionZ*=multiplier;
		 * }
		 */
		PC_Direction entity_direction;
		if (Math.abs(entity.motionX) > Math.abs(entity.motionZ))
		{
			if (entity.motionX > 0)
				entity_direction = PC_Direction.EAST;
			else
				entity_direction = PC_Direction.WEST;
		}		
		else
		{
			if (entity.motionZ > 0)
				entity_direction = PC_Direction.SOUTH;
			else
				entity_direction = PC_Direction.NORTH;
		}		
		PCtr_BeltHelper.moveEntityOnBelt(world, entity, current_pos, true, true, entity_direction);
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i,
			int j, int k)
	{
		return PCtr_BeltHelper.getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i,
			int j, int k)
	{
		return PCtr_BeltHelper.getSelectedBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i,
			int j, int k)
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
