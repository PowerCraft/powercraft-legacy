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
import powercraft.transport.helper.PC_EntityDictionary;
import powercraft.transport.helper.PCtr_BeltHelper;
import powercraft.transport.helper.PCtr_MaterialConveyor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@PC_BlockInfo(name = "PrimitivePlate", blockid = "primitivePlate", defaultid = 2050)
public class PCtr_BlockPrimitivePlate extends PC_BlockWithoutTileEntity
{
	
	public PCtr_BlockPrimitivePlate(int id)
	{
		super(id, PCtr_MaterialConveyor.getMaterial());
		setCreativeTab(CreativeTabs.tabBlock);
		this.slipperiness = 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void loadIcons()
	{
		this.blockIcon = PC_TextureRegistry.registerIcon("primitiveplate");
	}

	@Override
	public void registerRecipes()
	{

	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (!PCtr_BeltHelper.isEntityIgnored(entity)) // ignores FX entities and such
		{
			// creates PC_Vec out of current motion
			PC_Vec3 emotion = new PC_Vec3(entity.motionX, entity.motionY, entity.motionZ);
			PC_Direction newDir = PCtr_BeltHelper.returnPrimaryDirection(emotion, true);
			if (newDir == PC_Direction.UNKNOWN)
			{
				return;
			}
			
			if (PC_EntityDictionary.HasEntityID(entity.entityId))
			{
				PC_Vec3 storedmotion = PC_EntityDictionary.GetMotionForID(entity.entityId);
				// belt should ignore UP and DOWN directions
				PC_Direction oldDir = PCtr_BeltHelper.returnPrimaryDirection(storedmotion, true);		
				
				// same entity, same dir
				if (oldDir == newDir)
				{					
					boolean isgreater = PC_EntityDictionary.NewMotionGreater(entity.entityId, emotion, oldDir);				
					if (!isgreater)
					{						
						switch (oldDir)
						{
							case NORTH:
								entity.motionZ = storedmotion.z;
								break;
							case EAST:
								entity.motionX = storedmotion.x;
								break;
							case SOUTH:
								entity.motionZ = storedmotion.z;
								break;
							case WEST:
								entity.motionX = storedmotion.x;
								break;
							default:
									break;
						}
					}
					else
					{ // our motion has increased! update with new values, ignore y
						PC_EntityDictionary.UpdateEntityValues(entity.entityId, emotion, true);
					}
				}
				else
				{   // same entity but new dir
					PC_EntityDictionary.UpdateEntityValues(entity.entityId, emotion, true);
					// this will allow blocks to force objects going certain directions by 
					// changing the stored velocities in a direction
					// even if they are in the middle of the block. think separator belt or mobs
				}
			}
			else
			{
				PC_EntityDictionary.AddEntityValues(entity.entityId, emotion);				
			}
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
