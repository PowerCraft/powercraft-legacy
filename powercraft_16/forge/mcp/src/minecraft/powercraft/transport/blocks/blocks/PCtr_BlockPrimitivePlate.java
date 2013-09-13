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
import powercraft.transport.helper.PC_EntityFractionless;
import powercraft.transport.PCtr_ModuleTransport;
import powercraft.transport.helper.PC_EntityDictionary;
import powercraft.transport.helper.PCtr_BeltHelper;
import powercraft.transport.helper.PCtr_MaterialConveyor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@PC_BlockInfo(name = "PrimitivePlate", blockid = "primitivePlate", defaultid = 2050)
public class PCtr_BlockPrimitivePlate extends PC_BlockWithoutTileEntity
{
	private static PC_EntityDictionary transportDictionary = PCtr_ModuleTransport.trDict;
	
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
			
//			System.out.println("Current motion: " + emotion.toString() + " Direction :" + newDir.toString());			

			if (transportDictionary.HasEntityID(entity.entityId))
			{
				PC_Vec3 storedmotion = transportDictionary.GetMotionForID(entity.entityId);
				// belt should ignore UP and DOWN directions
				PC_Direction oldDir = PCtr_BeltHelper.returnPrimaryDirection(storedmotion, true);		
//				System.out.println("  -> Stored motion: " + storedmotion.toString() + " Direction : " + oldDir.toString());
				
				// same entity, same dir
				if (oldDir == newDir)
				{					
					boolean isgreater = transportDictionary.NewMotionGreater(entity.entityId, emotion, oldDir);				
					if (!isgreater)
					{						
//						double motiondelta = 0;						
						switch (oldDir)
						{
							case NORTH:
								//motiondelta = Math.abs(storedmotion.z) - Math.abs(emotion.z);
								//System.out.println("  -> Going North with Delta: " + motiondelta);
								entity.motionZ = storedmotion.z; // .addVelocity(0, 0, -motiondelta);
//								System.out.println("  -> Added Velocity, New motion (z): " + entity.motionZ);
								break;
							case EAST:
								//motiondelta = Math.abs(storedmotion.x) - Math.abs(emotion.x);
								//System.out.println("  -> Going East with Delta: " + motiondelta);
								entity.motionX = storedmotion.x;
								//System.out.println("  -> Added Velocity, New motion (x): " + entity.motionX);								
								break;
							case SOUTH:
								//motiondelta = Math.abs(storedmotion.z) - Math.abs(emotion.z);
								//System.out.println("  -> Going South with Delta: " + motiondelta);
								entity.motionZ = storedmotion.z; // .addVelocity(0, 0, motiondelta);
								//System.out.println("  -> Added Velocity, New motion (z): " + entity.motionZ);								
								break;
							case WEST:
								//motiondelta = Math.abs(storedmotion.x) - Math.abs(emotion.x);
								//System.out.println("  -> Going West with Delta: " + motiondelta);
								entity.motionX = storedmotion.x;
								//System.out.println("  -> Added Velocity, New motion (x): " + entity.motionX);								
								break;
							default:
									break;
						}
					}
					else
					{ // our motion has increased! update with new values, ignore y
//						System.out.println(" ** Increased Speed! : " + entity.entityId);
//						System.out.println("New Motion X, Y, Z: " + emotion.x + ", " + emotion.y + ", " + emotion.z);						
						transportDictionary.UpdateEntityValues(entity.entityId, emotion, true);
					}
				}
				else
				{   // same entity but new dir
					transportDictionary.UpdateEntityValues(entity.entityId, emotion, true);
					// this will allow blocks to force objects going certain directions by 
					// changing the stored velocities in a direction
					// even if they are in the middle of the block. think separator belt or mobs
				}
								
			}
			else
			{
//				System.out.println("Adding EntityID : " + entity.entityId);
				transportDictionary.AddEntityValues(entity.entityId, emotion);				
			}
//			System.out.println("--------------------------------------------------------------------");
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
