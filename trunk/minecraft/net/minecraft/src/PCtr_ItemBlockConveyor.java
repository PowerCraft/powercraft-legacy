package net.minecraft.src;


/**
 * belt block item - the "shifting" functionality
 * 
 * @author MightyPork
 */
public class PCtr_ItemBlockConveyor extends ItemBlock {

	/**
	 * @param i ID
	 */
	public PCtr_ItemBlockConveyor(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(false);
	}

	@Override
	public int getBlockID() {
		return shiftedIndex;
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



		// special placing rules for Ledge
		if (PCtr_BeltBase.isConveyorAt(world, new PC_CoordI(i, j - 1, k))) {			

				int dir = ((MathHelper.floor_double(((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

				if (itemstack.getItemDamage() == 0 && PC_Utils.isPlacingReversed()) {
					dir = PC_Utils.reverseSide(dir);
				}	
				
				j--;
				
				int m = 0;
				while(!world.canBlockBePlacedAt(getBlockID(), i, j, k, false, l) || m == 128) {
					i -= Direction.offsetX[dir];
					k -= Direction.offsetZ[dir];
					m++;
				}
			}


		if (j == 255 && Block.blocksList[getBlockID()].blockMaterial.isSolid()) {
			return false;
		}

		if (world.canBlockBePlacedAt(getBlockID(), i, j, k, false, l)) {
			Block block = Block.blocksList[getBlockID()];
			if (world.setBlock(i, j, k, block.blockID)) {
				
				block.onBlockPlaced(world, i, j, k, l);
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
}
