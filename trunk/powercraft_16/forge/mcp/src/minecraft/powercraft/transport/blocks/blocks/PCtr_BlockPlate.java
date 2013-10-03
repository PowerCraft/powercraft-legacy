package powercraft.transport.blocks.blocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_MathHelper;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_BlockWithoutTileEntity;
import powercraft.transport.helper.PCtr_BeltHelper;
import powercraft.transport.helper.PCtr_MaterialElevator;

@PC_BlockInfo(name="Plate", blockid="plate", defaultid=2052)
public class PCtr_BlockPlate extends PC_BlockWithoutTileEntity {

	public PCtr_BlockPlate(int id) {
		super(id, PCtr_MaterialElevator.getMaterial());
		setCreativeTab(CreativeTabs.tabTransport);
		this.slipperiness = 1;
	}

	@Override
	public void loadIcons() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerRecipes() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(PCtr_BeltHelper.isEntityIgnored(entity) || !entity.onGround){
			return;
		}
		NBTTagCompound tagCompound = entity.getEntityData();
		NBTTagCompound powerCraftTag;
		if(tagCompound.hasKey("PowerCraft")){
			powerCraftTag = tagCompound.getCompoundTag("PowerCraft");
		}else{
			powerCraftTag = new NBTTagCompound();
			tagCompound.setCompoundTag("PowerCraft", powerCraftTag);
		}
		int lastUpdateTime = powerCraftTag.getInteger("lastUpdateTime");
		powerCraftTag.setInteger("lastUpdateTime", entity.ticksExisted);
		if(lastUpdateTime+1!=entity.ticksExisted || lastUpdateTime==0){
			powerCraftTag.setDouble("motionX", entity.motionX);
			powerCraftTag.setDouble("motionY", entity.motionY);
			powerCraftTag.setDouble("motionZ", entity.motionZ);
		}else{
			double savedMotionX = powerCraftTag.getDouble("motionX");
			double savedMotionY = powerCraftTag.getDouble("motionY");
			double savedMotionZ = powerCraftTag.getDouble("motionZ");
			if(entity instanceof EntityLivingBase){
				EntityLivingBase living = (EntityLivingBase)entity;
				float s = PC_MathHelper.sin(living.rotationYaw * (float)Math.PI / 180.0F);
	            float c = PC_MathHelper.cos(living.rotationYaw * (float)Math.PI / 180.0F);
	            savedMotionX += (living.moveStrafing * c - living.moveForward * s)*0.01;
	            savedMotionZ += (living.moveForward * c + living.moveStrafing * s)*0.01;
	            powerCraftTag.setDouble("motionX", savedMotionX);
	            powerCraftTag.setDouble("motionZ", savedMotionZ);
			}
			entity.motionX = savedMotionX;
			entity.motionY = savedMotionY;
			entity.motionZ = savedMotionZ;
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
