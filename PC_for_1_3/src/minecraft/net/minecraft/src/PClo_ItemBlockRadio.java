package net.minecraft.src;


/**
 * ItemBlock replacing default itemblock for radio.<br>
 * Sets type into tile entity when the device is placed.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_ItemBlockRadio extends ItemBlock {

	/**
	 * @param i ID
	 */
	public PClo_ItemBlockRadio(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getBlockID() {
		return mod_PClogic.radio.blockID;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return super.getItemName() + "." + (itemstack.getItemDamage() == 0 ? "tx" : "rx");
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

		if (world.canBlockBePlacedAt(mod_PClogic.radio.blockID, i, j, k, false, l)) {
			Block block = mod_PClogic.radio;

			if (world.setBlockWithNotify(i, j, k, block.blockID)) {
				block.onBlockPlaced(world, i, j, k, l);
				block.onBlockPlacedBy(world, i, j, k, entityplayer);
				world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F,
						block.stepSound.getPitch() * 0.8F);

				// set tile entity
				PClo_TileEntityRadio ter = (PClo_TileEntityRadio) world.getBlockTileEntity(i, j, k);
				if (ter == null) {
					ter = (PClo_TileEntityRadio) ((BlockContainer) block).getBlockEntity();
				}


				ter.setType(itemstack.getItemDamage());
				ter.dim = entityplayer.dimension;

				world.setBlockTileEntity(i, j, k, ter);

				if (itemstack.getItemDamage() == 1) {
					ter.active = mod_PClogic.RADIO.getChannelState(mod_PClogic.default_radio_channel);
					if (ter.active) {
						world.setBlockMetadataWithNotify(i, j, k, 1);
					}
				}

				world.scheduleBlockUpdate(i, j, k, block.blockID, 1);

				itemstack.stackSize--;
			}
		}
		return true;
	}

	@Override
	public String getItemName() {
		return mod_PClogic.radio.getBlockName();
	}

	@Override
	public int getIconFromDamage(int i) {
		return mod_PClogic.radio.getBlockTextureFromSideAndMetadata(1, 0);
	}

	@Override
	public boolean isFull3D() {
		return false;
	}
}
