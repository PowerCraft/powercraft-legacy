package net.minecraft.src;

/**
 * Attempt to fix the bug with items getting stuck on belts next to water
 * 
 * @author MightyPork
 *
 */
public class PCco_BlockWaterStationaryReplacement extends BlockStationary {

	/**
	 * @param par1
	 * @param par2Material
	 */
	public PCco_BlockWaterStationaryReplacement(int par1, Material par2Material) {
		super(par1, par2Material);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		super.onEntityCollidedWithBlock(world, i, j, k, entity);
		if(entity.stepHeight <= 0.25F) entity.stepHeight = 0.25F;
		
		if(entity instanceof EntityItem && world.getBlockMetadata(i, j, k) != 0 && ((EntityItem) entity).age%20 == 0) {
			PCtr_BeltBase.storeAllSides(world, new PC_CoordI(i,j,k), (EntityItem) entity);
		}
	}

}
