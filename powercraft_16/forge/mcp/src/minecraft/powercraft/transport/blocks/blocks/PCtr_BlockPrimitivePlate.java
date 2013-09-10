package powercraft.transport.blocks.blocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.blocks.PC_BlockInfo;
import powercraft.api.blocks.PC_BlockWithoutTileEntity;
import powercraft.api.registries.PC_TextureRegistry;
import powercraft.transport.helper.PC_EntityVirtualPet;
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
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity){
    	if(entity instanceof PC_EntityVirtualPet){
    		
    		return;
    	}
    	if(entity.ridingEntity == null){
	    	PCtr_BeltHelper.addTrackerToEntity(entity);
	    	return;
	    }
    	if(2+2==4) return;
    	if(entity.onGround && (i<=entity.posX && i+1>entity.posX) && (k<=entity.posZ && k+1>entity.posZ)){
    		updateSpeed(entity, entity.posX-i, entity.posZ-k);
    	}
    }
    
    private void updateSpeed(Entity entity, double offX, double offZ){
    	PCtr_BeltHelper.backupPercentageSpeed(1, entity);
    	if(Math.abs(entity.motionX)>Math.abs(entity.motionZ)){
    		entity.motionZ*=0.5f;
    		entity.motionZ+=(Math.abs(entity.motionX)>1?1:Math.abs(entity.motionX))*1.5*(0.5-offZ);
    	}else{
    		entity.motionX*=0.5f;
    		entity.motionX+=(Math.abs(entity.motionZ)>1?1:Math.abs(entity.motionZ))*1.5*(0.5-offX);
    	}
    	
    	
    }
    
	@Override
	public boolean isOpaqueCube() {
	    return PCtr_BeltHelper.isOpaqueCube();
	}
	@Override
	public boolean renderAsNormalBlock() {
	    return PCtr_BeltHelper.renderAsNormalBlock();
	}
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i,
			int j, int k) {
			    return PCtr_BeltHelper.getCollisionBoundingBoxFromPool(world, i, j, k);
			}
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i,
			int j, int k) {
		return PCtr_BeltHelper.getSelectedBoundingBoxFromPool(world, i,j,k);
	}
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i,
			int j, int k) {
		PCtr_BeltHelper.setBlockBoundsBasedOnState(this, iblockaccess, i, j, k);
			}
	@Override
	public void setBlockBoundsForItemRender() {
		PCtr_BeltHelper.setBlockBoundsForItemRender(this);
	}
	@Override
	public int tickRate(World world) {
	    return PCtr_BeltHelper.tickRate(world);
	}
}
