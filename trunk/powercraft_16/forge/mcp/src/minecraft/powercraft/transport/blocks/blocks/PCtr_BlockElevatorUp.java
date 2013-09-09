package powercraft.transport.blocks.blocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Vec3I;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.transport.helper.PCtr_BeltHelper;
import powercraft.transport.helper.PCtr_MaterialElevator;

@PC_BlockInfo(name = "ElevatorUp", blockid = "elevatorup", defaultid = 2060)
public class PCtr_BlockElevatorUp extends PCtr_BlockBeltBase
{
	// an Elevator is simply a conveyor belt that moves items up or down... so able to use base classes for belts.
	public PCtr_BlockElevatorUp(int id)
	{
		super(id, PCtr_MaterialElevator.getMaterial(), true);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public void loadIcons()
	{
		this.blockIcon = PC_TextureRegistry.registerIcon("elevatorup");
	}

	@Override
	public void registerRecipes()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
	{
		// TODO 1) check if valid entity (done)
		PC_Vec3I curposition = new PC_Vec3I(par2, par3, par4);
		if (!PCtr_BeltHelper.isEntityIgnored(par5Entity))
		{
			moveEntity(par1World, par5Entity, curposition);
		}
	}

	private void moveEntity(World world, Entity entity, PC_Vec3I curPosition)
	{
		// TODO: Check if there is an exit belt
		PCtr_BeltHelper.moveEntityOnBelt(world, entity, curPosition, true, true, PC_Direction.UP);
	}
	
	@Override	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	{ 
		return null;
	}
}
