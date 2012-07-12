package net.minecraft.src;

//import java.util.HashSet;

/**
 * Hacked water which works like conveyor and stores items into chests and other inv blocks.
 * This is the stationary water, moving is unchanged.
 * 
 * @author MightyPork
 *
 */
public class PCtr_BlockHackedWater extends BlockStationary {

//	private static HashSet<Integer> floating = new HashSet<Integer>();	
//	
//	static {
//		Integer[] floatingIDs = {
//			Item.stick.shiftedIndex,
//			Block.sapling.blockID,
//			Block.ladder.blockID,
//			Block.chest.blockID,
//			Block.wood.blockID,
//			Block.planks.blockID,
//			Block.plantRed.blockID,
//			Block.plantYellow.blockID,
//			Block.workbench.blockID,
//			Block.bookShelf.blockID,
//			Block.cactus.blockID,
//			Block.cloth.blockID,
//			Item.doorWood.shiftedIndex,
//			Block.trapdoor.blockID,
//			Block.fence.blockID,
//			Block.fenceGate.blockID,
//			Block.jukebox.blockID,
//			Block.melon.blockID,
//			Block.pumpkin.blockID,
//			Block.pressurePlatePlanks.blockID,
//			Block.pumpkinLantern.blockID,
//			Block.torchWood.blockID,
//			Block.torchRedstoneActive.blockID,
//			Block.vine.blockID,
//			Block.web.blockID,
//			Item.appleRed.shiftedIndex,
//			Item.boat.shiftedIndex,
//			Item.bed.shiftedIndex,
//			Item.feather.shiftedIndex,
//			Item.hoeWood.shiftedIndex,
//			Item.axeWood.shiftedIndex,
//			Item.pickaxeWood.shiftedIndex,
//			Item.shovelWood.shiftedIndex,
//			Item.swordWood.shiftedIndex,
//			Item.leather.shiftedIndex,
//			Item.snowball.shiftedIndex,
//			Block.ice.blockID,
//			Item.map.shiftedIndex,
//			Item.seeds.shiftedIndex,
//			Item.melon.shiftedIndex,
//			Item.pumpkinSeeds.shiftedIndex,
//			Item.melonSeeds.shiftedIndex,
//			Item.bread.shiftedIndex,
//			Item.bow.shiftedIndex,
//			Item.book.shiftedIndex,
//			Item.paper.shiftedIndex,
//			Item.cookie.shiftedIndex,
//			Item.painting.shiftedIndex,
//			Item.reed.shiftedIndex,
//			Item.silk.shiftedIndex,
//			Item.helmetLeather.shiftedIndex,
//			Item.plateLeather.shiftedIndex,
//			Item.legsLeather.shiftedIndex,
//			Item.bootsLeather.shiftedIndex					
//		};
//					
//				
//		for(Integer a : floatingIDs) {
//			floating.add(a);
//		}
//	}

	/**
	 * @param par1
	 * @param par2Material
	 */
	public PCtr_BlockHackedWater(int par1, Material par2Material) {
		super(par1, par2Material);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		super.onEntityCollidedWithBlock(world, i, j, k, entity);
		
		if(entity.stepHeight < 0.25F) entity.stepHeight = 0.25F;
		
		if(entity instanceof EntityItem) {
			if(entity.rand.nextFloat() < 0.8F &&  ((EntityItem) entity).age%20 == 0 && world.getBlockMetadata(i, j, k) != 0) {
				PCtr_BeltBase.storeAllSides(world, new PC_CoordI(i,j,k), (EntityItem) entity);
				PCtr_BeltBase.packItems(world, new PC_CoordI(i,j,k));
			}
			
			
			
//			ItemStack stack = ((EntityItem)entity).item;
//			int id = stack.itemID;
			if(entity.motionY < 0) {
				entity.motionY *= 0.6F;	
//				if(floating.contains(id)) {
//					if(entity.inWater && !entity.onGround && entity.rand.nextFloat() < 0.8F)
//					entity.motionY += 0.039999999105930328D;
//				}
			}
			
			
		}
	}

}
