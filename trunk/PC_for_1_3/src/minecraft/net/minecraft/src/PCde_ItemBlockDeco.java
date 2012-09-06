package net.minecraft.src;


/**
 * Replacement ItemBlock for BlockDecorative, which sets the tile entity when
 * placed.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCde_ItemBlockDeco extends ItemBlock {

	/**
	 * @param i ID
	 */
	public PCde_ItemBlockDeco(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getBlockID() {
		return mod_PCdeco.deco.blockID;
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

		if (j == 255 && Block.blocksList[getBlockID()].blockMaterial.isSolid()) {
			return false;
		}

		if (world.canPlaceEntityOnSide(mod_PCdeco.deco.blockID, i, j, k, false, l, entityplayer)) {
			Block block = mod_PCdeco.deco;
			if (world.setBlock(i, j, k, block.blockID)) {
				// set tile entity
				PCde_TileEntityDeco ted = (PCde_TileEntityDeco) world.getBlockTileEntity(i, j, k);
				if (ted == null) {
					ted = (PCde_TileEntityDeco) ((BlockContainer) block).createNewTileEntity(world);
				}
				ted.type = itemstack.getItemDamage();
				world.setBlockTileEntity(i, j, k, ted);
				/** @todo block.onBlockPlaced(world, i, j, k, l); */
				block.onBlockPlacedBy(world, i, j, k, entityplayer);

				world.markBlocksDirty(i, j, k, i, j, k);
				world.markBlockNeedsUpdate(i, j, k);

				//PC_Utils.setTileEntity(entityplayer, ted, "type", itemstack.getItemDamage());
				
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
