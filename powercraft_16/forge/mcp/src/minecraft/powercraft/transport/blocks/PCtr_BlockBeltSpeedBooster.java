package powercraft.transport.blocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_EntityTracker;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.transport.blocks.itemblocks.PCtr_ItemBlockUpgradeableBelt;
import powercraft.transport.blocks.tileentities.PCtr_TEUpgradeableBelt;
import powercraft.transport.helper.PCtr_BeltHelper;
import powercraft.transport.helper.PCtr_MaterialConveyor;

@PC_BlockInfo(name = "SpeedBelt", blockid = "speedbelt", defaultid = 2053, tileEntity=PCtr_TEUpgradeableBelt.class, itemBlock=PCtr_ItemBlockUpgradeableBelt.class, rotateable=true)
public class PCtr_BlockBeltSpeedBooster extends PC_Block
{
	public Icon beltSideTexture;
	
	public PCtr_BlockBeltSpeedBooster(int id)
	{
		super(id, PCtr_MaterialConveyor.getMaterial());
		setCreativeTab(CreativeTabs.tabBlock);
		
	}

	// Bottom texture also needed
	@Override
	public void loadIcons()
	{
		this.blockIcon = PC_TextureRegistry.registerIcon("speedbelt");
		beltSideTexture = PC_TextureRegistry.registerIcon("beltside");
	}
	
	@Override
	public void registerRecipes()
	{
		
	}
	
	/**
	 * Stores MD of direction 
	 * (2, 3, 0, 1 = N, E, S, W respectively)
	 */
	// Form XOR
	// DONE BY PC_Block.modifyMetadataPostPlace
	// But not used PC_Direction.PLAYER2MD[l], so use it when you call getBlockRotation(...)
	/*@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) 
	{		
		int l = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;				
		PC_Utils.setMD(world, x, y, z, PC_Direction.PLAYER2MD[l]);
//		this.rotateBlock(world, x, y, z, ForgeDirection. PC_Direction.PLAYER2MD[l]);
	}*/
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (!PC_EntityTracker.isEntityIgnored(entity)) // sanity check 									
		{			
			// N, E, S, W = 1, 2, 0, 3
			int blockrotation = getBlockRotation(world, x, y, z);			
			//PC_EntityTracker.increaseEntitySpeedXYZ(entity,	increaseX, increaseY, increaseZ);
		}
	}

	// AND BOTTOM?? You need also a bottom texture, take a look on the bottom :/
	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, PC_Direction side) 
	{	
		if (side == PC_Direction.TOP)
		{
			return blockIcon;
		}
		return beltSideTexture;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit)
	{
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
	public AxisAlignedBB getCollisionBoundingBox(World world, int i, int j, int k)
	{
		return PCtr_BeltHelper.getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(World world, int i, int j, int k)
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
		return 0;
	}
}

