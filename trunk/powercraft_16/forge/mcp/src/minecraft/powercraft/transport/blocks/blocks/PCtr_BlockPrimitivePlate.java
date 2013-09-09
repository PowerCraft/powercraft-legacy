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
public class PCtr_BlockPrimitivePlate extends PCtr_BlockBeltBase
{
	public PCtr_BlockPrimitivePlate(int id)
	{
		super(id, PCtr_MaterialConveyor.getMaterial(), false);
		setCreativeTab(CreativeTabs.tabBlock);
		this.slipperiness = 0;
	}

	@Override
	public void loadIcons()
	{
		this.blockIcon = PC_TextureRegistry.registerIcon("primitiveplatea");
	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
	{
		// boolean tmp;
		// if(entity instanceof EntityPlayer){
		// float f = ((EntityPlayer) entity).getRotationYawHead();
		// if(f<)
		// }
		if (entity.onGround)
		{
			PC_Vec3I current_pos = new PC_Vec3I(i, j, k);
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
		 * if(isXDir){ entity.motionX*=multiplier;
		 * entity.motionZ+=entity.motionZ*4*factor; }else{
		 * entity.motionX+=entity.motionX*4*factor; entity.motionZ*=multiplier;
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
}
