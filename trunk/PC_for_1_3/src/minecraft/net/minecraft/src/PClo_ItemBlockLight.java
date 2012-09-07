package net.minecraft.src;


/**
 * Item Block replacement for lights (sets tile entity)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_ItemBlockLight extends ItemBlock {

	/**
	 * @param i ID
	 */
	public PClo_ItemBlockLight(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getBlockID() {
		return mod_PClogic.lightOff.blockID;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		boolean lamp = itemstack.getItemDamage() >= 16 && itemstack.getItemDamage() < 32;
		boolean huge = itemstack.getItemDamage() >= 32 && itemstack.getItemDamage() < 48;
		return super.getItemName() + "." + itemstack.getItemDamage() % 16 + (lamp ? ".stable" : huge ? ".huge" : "");
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

		int i1 = 0;
		
		if (l == 1 && world.isBlockNormalCube(i, j - 1, k)) {
			i1 = 0;
		}
		if (l == 2 && world.isBlockNormalCube(i, j, k + 1)) {
			i1 = 1;
		} else if (l == 3 && world.isBlockNormalCube(i, j, k - 1)) {
			i1 = 2;
		} else if (l == 4 && world.isBlockNormalCube(i + 1, j, k)) {
			i1 = 3;
		} else if (l == 5 && world.isBlockNormalCube(i - 1, j, k)) {
			i1 = 4;
		}
		if (l == 0 && world.isBlockNormalCube(i, j + 1, k)) {
			i1 = 5;
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


		boolean lamp = itemstack.getItemDamage() >= 16 && itemstack.getItemDamage() < 32;
		boolean huge = itemstack.getItemDamage() >= 32 && itemstack.getItemDamage() < 48;
		Block block = lamp ? mod_PClogic.lightOn : mod_PClogic.lightOff;

		if (world.canPlaceEntityOnSide(block.blockID, i, j, k, false, l, entityplayer)) {
			if (world.setBlockWithNotify(i, j, k, block.blockID)) {
				//block.onBlockPlaced(world, i, j, k, l);
				block.onBlockPlacedBy(world, i, j, k, entityplayer);
				world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F,
						block.stepSound.getPitch() * 0.8F);

				// set tile entity
				PClo_TileEntityLight tei = (PClo_TileEntityLight) world.getBlockTileEntity(i, j, k);
				if (tei == null) {
					tei = (PClo_TileEntityLight) ((BlockContainer) block).createNewTileEntity(world);
				}

				if (lamp) tei.isStable = true;
				if (huge) tei.isHuge = true;

				tei.setColor(new PC_Color());
				world.setBlockTileEntity(i, j, k, tei);
				world.setBlockMetadataWithNotify(i, j, k, i1);

				if (lamp) {
					PClo_BlockLight.onPoweredBlockChange(world, i, j, k, true);
				}



				itemstack.stackSize--;
			}
		}
		return true;
	}

	@Override
	public int getIconFromDamage(int i) {
		return mod_PClogic.lightOn.getBlockTextureFromSideAndMetadata(1, 0);
	}
}
