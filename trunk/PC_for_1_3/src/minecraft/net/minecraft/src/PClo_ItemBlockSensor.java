package net.minecraft.src;


/**
 * Item Block replacement for sensors (overrides itemNameIS)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_ItemBlockSensor extends ItemBlock {


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

		if (world.canPlaceEntityOnSide(mod_PClogic.sensor.blockID, i, j, k, false, l, entityplayer)) {
			Block block = mod_PClogic.sensor;
			if (world.setBlock(i, j, k, block.blockID)) {
				// set tile entity
				PClo_TileEntitySensor tes = (PClo_TileEntitySensor) world.getBlockTileEntity(i, j, k);
				if (tes == null) {
					tes = (PClo_TileEntitySensor) ((BlockContainer) block).createNewTileEntity(world);
				}
				tes.type = itemstack.getItemDamage();
				world.setBlockTileEntity(i, j, k, tes);
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


	/**
	 * @param i id
	 * @param block the block
	 */
	public PClo_ItemBlockSensor(int i) {
		super(i);
	}
	
	

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return (new StringBuilder()).append(super.getItemName()).append(".")
				.append(itemstack.getItemDamage() == 0 ? "item" : itemstack.getItemDamage() == 1 ? "living" : "player").toString();
	}

}
