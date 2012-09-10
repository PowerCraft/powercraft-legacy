package net.minecraft.src;


import java.util.List;



/**
 * Replacement ItemBlock for gate, which sets the tile entity when placed.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCnt_ItemBlockWeasel extends ItemBlock {

	/**
	 * @param i ID
	 */
	public PCnt_ItemBlockWeasel(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getBlockID() {
		return mod_PCnet.weaselDevice.blockID;
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

		if (world.canPlaceEntityOnSide(mod_PCnet.weaselDevice.blockID, i, j, k, false, l, entityplayer)) {
			Block block = mod_PCnet.weaselDevice;
			if (world.setBlock(i, j, k, block.blockID)) {
				// set tile entity
				PCnt_TileEntityWeasel teg = (PCnt_TileEntityWeasel) world.getBlockTileEntity(i, j, k);
				if (teg == null) {
					teg = (PCnt_TileEntityWeasel) ((BlockContainer) block).createNewTileEntity(world);
				}
				teg.setType(itemstack.getItemDamage());
				world.setBlockTileEntity(i, j, k, teg);

				//block.onBlockPlaced(world, i, j, k, l);
				block.onBlockPlacedBy(world, i, j, k, entityplayer);

				PClo_BlockGate.hugeUpdate(world, i, j, k, block.blockID);
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
	public String getItemName() {
		return mod_PCnet.weaselDevice.getBlockName();
	}

	@Override
	public int getIconFromDamage(int i) {
		return mod_PCnet.weaselDevice.getBlockTextureFromSideAndMetadata(1, 0);
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		try {
			return super.getItemName() + "." + PCnt_WeaselType.names[itemstack.getItemDamage()];
		} catch (ArrayIndexOutOfBoundsException e) {
			return super.getItemName();
		}
	}

	@Override
	public boolean isFull3D() {
		return false;
	}

	@Override
	public boolean shouldRotateAroundWhenRendering() {
		return false;
	}

	@Override
	public void addInformation(ItemStack itemstack, List list) {
		list.add(getDescriptionForGate(itemstack.getItemDamage()));
	}

	/**
	 * Get description bubble for gate
	 * 
	 * @param dmg gate item damage value
	 * @return the description string
	 */
	public static String getDescriptionForGate(int dmg) {
		return PC_Lang.tr("pc.weasel." + PCnt_WeaselType.names[MathHelper.clamp_int(dmg, 0, PClo_GateType.TOTAL_GATE_COUNT - 1)] + ".desc");
	}
}
