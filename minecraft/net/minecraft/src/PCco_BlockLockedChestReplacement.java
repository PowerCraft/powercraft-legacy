package net.minecraft.src;


import net.minecraft.src.forge.ITextureProvider;


/**
 * Block injected to blocksList to replace the standard LockedChest block.<br>
 * Used by the Container Pickup function of Activators. The block behaves
 * exactly the same as ordinary locked chest, BUT! if its an item, it has
 * texture of the moved inventory.<br>
 * Non-vanilla blocks with texture file replacement, or blocks with custom
 * renderers have only a dispenser-bottom texture, as their real textures could
 * not be used here.
 * 
 * @author MightyPork
 */
public class PCco_BlockLockedChestReplacement extends BlockLockedChest {
	/**
	 * @param par1 id
	 */
	protected PCco_BlockLockedChestReplacement(int par1) {
		super(par1);
		blockIndexInTexture = 26;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int damage) {

		Block block = Block.blocksList[damage];

		if (block != null && (block.isOpaqueCube() || block.renderAsNormalBlock() || Block.isNormalCube(damage) || damage == Block.chest.blockID)) {
			if (block.getRenderType() == 0 || damage == Block.chest.blockID) {
				if (!(block instanceof PC_ISwapTerrain) && !(block instanceof ITextureProvider)) {
					return Block.blocksList[damage].getBlockTextureFromSideAndMetadata(side, damage);
				}
			}
		}

		if (damage == 0) {
			return super.getBlockTextureFromSideAndMetadata(side, damage);
		} else {
			return Block.dispenser.getBlockTextureFromSideAndMetadata(0, 0);
		}
	}
}
