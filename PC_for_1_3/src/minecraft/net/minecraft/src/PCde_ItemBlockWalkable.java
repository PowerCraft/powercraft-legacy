package net.minecraft.src;


/**
 * Replacement ItemBlock for BlockDecorative, which sets the tile entity when
 * placed.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCde_ItemBlockWalkable extends ItemBlock {

	/**
	 * @param i ID
	 */
	public PCde_ItemBlockWalkable(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getBlockID() {
		return mod_PCdeco.walkable.blockID;
	}

	@Override
	public boolean tryPlaceIntoWorld(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float par8, float par9, float par10) {
		int id = world.getBlockId(i, j, k);

		if (id == Block.snow.blockID) {
			l = 1;
		} else if (id != Block.vine.blockID && id != Block.tallGrass.blockID && id != Block.deadBush.blockID) {
			if (l == 0) {
				j--;
			}

			if (l == 1) {
				j++;
			}

			if (l == 2) {
				k--;
			}

			if (l == 3) {
				k++;
			}

			if (l == 4) {
				i--;
			}

			if (l == 5) {
				i++;
			}
		}

		if (itemstack.stackSize == 0) {
			return false;
		}

		if (!entityplayer.canPlayerEdit(i, j, k)) {
			return false;
		}



		// special placing rules for Ledge
		if (world.getBlockId(i, j - 1, k) == mod_PCdeco.walkable.blockID) {
			TileEntity te = world.getBlockTileEntity(i, j - 1, k);
			if (te != null && te instanceof PCde_TileEntityWalkable) {
				PCde_TileEntityWalkable tew = (PCde_TileEntityWalkable) te;

				int dir = ((MathHelper.floor_double(((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

				if (itemstack.getItemDamage() == 0 && PC_Utils.isPlacingReversed()) {
					dir = PC_Utils.reverseSide(dir);
				}
				int meta = world.getBlockMetadata(i, j - 1, k);

				i -= Direction.offsetX[dir];
				k -= Direction.offsetZ[dir];

				if (tew.type == 1) {

					if (meta == dir) {

						if (!PC_Utils.isPlacingReversed()) {
							j += 1;
						}

					} else if (PC_Utils.isPlacingReversed() && itemstack.getItemDamage() == 1) {
						j--;
					}

				} else if (tew.type == 0 && itemstack.getItemDamage() == 1) {
					if (PC_Utils.isPlacingReversed()) {
						j--;
					}
				}

				j--;

			}
		}


		if (j == 255 && Block.blocksList[getBlockID()].blockMaterial.isSolid()) {
			return false;
		}

		if (world.canPlaceEntityOnSide(mod_PCdeco.walkable.blockID, i, j, k, false, l, entityplayer)) {
			Block block = mod_PCdeco.walkable;
			if (world.setBlock(i, j, k, block.blockID)) {
				// set tile entity
				PCde_TileEntityWalkable ted = (PCde_TileEntityWalkable) world.getBlockTileEntity(i, j, k);
				if (ted == null) {
					ted = (PCde_TileEntityWalkable) ((BlockContainer) block).createNewTileEntity(world);
				}
				ted.type = itemstack.getItemDamage();
				world.setBlockTileEntity(i, j, k, ted);
				//block.onBlockPlaced(world, i, j, k, l);
				block.onBlockPlacedBy(world, i, j, k, entityplayer);

				world.markBlocksDirty(i, j, k, i, j, k);
				world.markBlockNeedsUpdate(i, j, k);

				world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F,
						block.stepSound.getPitch() * 0.8F);

				itemstack.stackSize--;
			}
		}
		return true;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return super.getItemName() + "." + itemstack.getItemDamage();
	}

}
