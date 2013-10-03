package powercraft.transport.blocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;
import powercraft.api.blocks.PC_Block;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.transport.blocks.itemblocks.PCtr_ItemBlockUpgradeableBelt;
import powercraft.transport.blocks.tileentities.PCtr_TEUpgradeableBelt;
import powercraft.transport.helper.PCtr_BeltHelper;
import powercraft.transport.helper.PCtr_MaterialConveyor;

@PC_BlockInfo(name = "SpeedBoostBelt", blockid = "speedbelt", defaultid = 2053, tileEntity=PCtr_TEUpgradeableBelt.class, itemBlock=PCtr_ItemBlockUpgradeableBelt.class)
public class PCtr_BlockBeltSpeedBooster extends PC_Block
{
	public PCtr_BlockBeltSpeedBooster(int id)
	{
		super(id, PCtr_MaterialConveyor.getMaterial());
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public void loadIcons()
	{
		this.blockIcon = PC_TextureRegistry.registerIcon("speedbelt");
	}

	@Override
	public void registerRecipes()
	{
		
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {

		int l = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		PC_Utils.setMD(world, x, y, z, PC_Direction.PLAYER2MD[l]);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (!PCtr_BeltHelper.isEntityIgnored(entity))
		{
			PC_Direction blockrotation; // -??????
			if (entity.getEntityData().hasKey("PC_StoredMotions"))
			{
				
			}
			else
			{
				entity.getEntityData().setBoolean("PC_StoredMotions", true);
				entity.getEntityData().setDouble("PC_MotionX", entity.motionX);
				entity.getEntityData().setDouble("PC_MotionY", entity.motionY);
				entity.getEntityData().setDouble("PC_MotionZ", entity.motionZ);
			}
		}
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

