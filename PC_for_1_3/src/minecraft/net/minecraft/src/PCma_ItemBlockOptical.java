package net.minecraft.src;



/**
 * Replacement ItemBlock for mirror/prism. Sets tile entity and has description.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_ItemBlockOptical extends ItemBlock {

	/**
	 * @param i item ID
	 */
	public PCma_ItemBlockOptical(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getBlockID() {
		return mod_PCmachines.optical.blockID;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return super.getItemName() + "." + (itemstack.getItemDamage() == 0 ? "mirror" : "prism");
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
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

		if (world.canBlockBePlacedAt(getBlockID(), i, j, k, false, l)) {
			Block block = mod_PCmachines.optical;
			if (world.setBlock(i, j, k, block.blockID)) {

				int meta = MathHelper.floor_double((((entityplayer.rotationYaw + 180F) * 16F) / 360F) + 0.5D) & 0xf;
				if (itemstack.getItemDamage() != 0) {
					meta = 0;
				}
				world.setBlockMetadataWithNotify(i, j, k, meta);

				PCma_TileEntityOptical teo = (PCma_TileEntityOptical) world.getBlockTileEntity(i, j, k);
				if (teo == null) {
					teo = new PCma_TileEntityOptical();
				}
				if (itemstack.getItemDamage() == 0) {
					teo.setMirror();
				} else {
					teo.setPrism();
				}
				world.setBlockTileEntity(i, j, k, teo);

				mod_PCmachines.optical.onBlockPlaced(world, i, j, k, i);
				mod_PCmachines.optical.onBlockPlacedBy(world, i, j, k, entityplayer);
				world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F,
						block.stepSound.getPitch() * 0.8F);
				world.markBlocksDirty(i, j, k, i, j, k);
				world.markBlockNeedsUpdate(i, j, k);

				itemstack.stackSize--;
			}
		}
		return true;
	}

	@Override
	public String getItemName() {
		return mod_PClogic.lightOff.getBlockName();
	}

	@Override
	public int getIconFromDamage(int i) {
		return mod_PCmachines.optical.getBlockTextureFromSideAndMetadata(1, 0);
	}
}
