package powercraft.transport.blocks.blocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Vec3I;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_BlockWithoutTileEntity;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.transport.helper.PCtr_BeltHelper;
import powercraft.transport.helper.PCtr_MaterialElevator;

@PC_BlockInfo(name = "ElevatorUp", blockid = "elevatorup", defaultid = 2060)
public class PCtr_BlockClassicElevator extends PC_BlockWithoutTileEntity
{
	protected PC_Direction elevatorDirection;
	// an Elevator is simply a conveyer belt that moves items up or down... so able to use base classes for belts.
	public PCtr_BlockClassicElevator(int id)
	{
		super(id, PCtr_MaterialElevator.getMaterial());
		setCreativeTab(CreativeTabs.tabBlock);
		elevatorDirection = PC_Direction.UP;
	}

	@Override
	public void loadIcons()
	{		
		this.blockIcon = PC_TextureRegistry.registerIcon("elevatorup");
	}
	
	public PC_Direction getDirection()
	{
		return elevatorDirection;
	}

	@Override
	public void registerRecipes()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isOpaqueCube()
	{ 
		return false;
	}
	
	@Override
	public void setBlockBoundsForItemRender()
	{ 
		this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{ 
		return false;
	}
	
	
	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
	{
		// TODO 1) check if valid entity (done)		
		System.out.println("EntityID: " + par5Entity.entityId);
		PC_Vec3I curposition = new PC_Vec3I(par2, par3, par4);
		System.out.println("Position: " + curposition.toString());
		if (!PCtr_BeltHelper.isEntityIgnored(par5Entity))
		{
			moveEntity(par1World, par5Entity, curposition);
		}
	}

	private void moveEntity(World world, Entity entity, PC_Vec3I curPosition)
	{
		boolean isPlayer = entity instanceof EntityPlayer;		
		//PCtr_BeltHelper.moveEntityOnBelt(world, entity, curPosition, isPlayer ? false : true, true, elevatorDirection);
	}
	
	@Override	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	{ 
		return null;
	}
}
