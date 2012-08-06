package net.minecraft.src;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * Mirror / Prism block
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_BlockOptical extends BlockContainer implements PC_IBlockType {

	/**
	 * @param id ID
	 */
	protected PCma_BlockOptical(int id) {
		super(id, Material.glass);
		float f = 0.4F;
		float f1 = 1.0F;
		setBlockBounds(0.5F - f, 0.1F, 0.5F - f, 0.5F + f, f1 - 0.1F, 0.5F + f);
	}

	// but also render as normal block
	@Override
	public int getRenderType() {
		return PCma_Renderer.opticalRenderer;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PCma_TileEntityOptical();
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int i) {
		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int idDropped(int par1, Random random, int par3) {
		return -1;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
		if (!PC_Utils.isCreative()) {
			if (isMirror(world, i, j, k)) {
				dropBlockAsItem_do(world, i, j, k, new ItemStack(mod_PCmachines.optical, 1, 0));
			} else {
				dropBlockAsItem_do(world, i, j, k, new ItemStack(mod_PCmachines.optical, 1, 1));
				PCma_TileEntityOptical teo = getTE(world, i, j, k);
				if (teo != null) {
					for (int q = 0; q <= 9; q++) {
						if (teo.getPrismSide(q)) {
							dropBlockAsItem_do(world, i, j, k, new ItemStack(Block.thinGlass, 1));
						}
					}
				}
			}
		}
		super.breakBlock(world, i, j, k, par5, par6);
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9) {
		ItemStack ihold = player.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.itemID == mod_PCcore.powerCrystal.blockID) {

				PCma_TileEntityOptical teo = getTE(world, i, j, k);
				if (teo != null) {
					teo.setMirrorColor(ihold.getItemDamage());
				}

				return true;
			}

			if (ihold.getItem() instanceof ItemBlock && ihold.itemID != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
				if (bhold instanceof PC_IBlockType) {
					return false;
				}
			}
		}

		if (isMirror(world, i, j, k)) {
			int m = MathHelper.floor_double((((player.rotationYaw + 180F) * 16F) / 360F) + 0.5D) & 0xf;
			world.setBlockMetadataWithNotify(i, j, k, m);
		} else {
			// prism...

			int angle = MathHelper.floor_double((((player.rotationYaw + 180F) * 16F) / 360F) + 0.5D) & 0xf;
			angle &= 0xE;
			angle = angle >> 1;
			angle += 2;
			if (angle > 7) {
				angle = angle - 8;
			}

			angle += 2;

			// if close enough
			if (MathHelper.abs((float) player.posX - (i + 0.5F)) < 1.3F && MathHelper.abs((float) player.posZ - (k + 0.5F)) < 1.3F) {
				double d = (player.posY + 1.8200000000000001D) - player.yOffset;

				if (d - j > 2D) {
					angle = 1;
				}

				if (j - d > 0.0D) {
					angle = 0;
				}
			}

			boolean drop = true;
			if (ihold != null) {
				if (ihold.getItem().shiftedIndex == Block.thinGlass.blockID) {

					if (isGlassPanelOnSide(world, i, j, k, angle) == false) {

						PCma_TileEntityOptical teo = getTE(world, i, j, k);
						if (teo != null) {
							teo.setPrismSide(angle, true);
						}
						if (!PC_Utils.isCreative()) {
							ihold.stackSize--;
						}
						drop = false;

					}

				}
			}
			if (drop) {

				if (isGlassPanelOnSide(world, i, j, k, angle)) {

					PCma_TileEntityOptical teo = getTE(world, i, j, k);
					if (teo != null) {
						teo.setPrismSide(angle, false);
					}
					if (!PC_Utils.isCreative()) {
						dropBlockAsItem_do(world, i, j, k, new ItemStack(Block.thinGlass, 1));
					}

				}

			}

		}
		return true;
	}

	/**
	 * Get tile entity at position in world
	 * 
	 * @param iblockaccess
	 * @param x
	 * @param y
	 * @param z
	 * @return the tile entity
	 */
	public static PCma_TileEntityOptical getTE(IBlockAccess iblockaccess, int x, int y, int z) {
		TileEntity te = iblockaccess.getBlockTileEntity(x, y, z);
		if (te == null) {
			return null;
		}
		PCma_TileEntityOptical tem = (PCma_TileEntityOptical) te;

		return tem;
	}

	/**
	 * Check if prism's side is active (with glass pane)
	 * 
	 * @param iblockaccess
	 * @param x
	 * @param y
	 * @param z
	 * @param side
	 * @return has glass panel
	 */
	public static boolean isGlassPanelOnSide(IBlockAccess iblockaccess, int x, int y, int z, int side) {

		PCma_TileEntityOptical teo = getTE(iblockaccess, x, y, z);

		if (teo == null) {
			return false;
		}

		return getTE(iblockaccess, x, y, z).getPrismSide(side);
	}

	/**
	 * Check if device is mirror
	 * 
	 * @param iblockaccess
	 * @param x
	 * @param y
	 * @param z
	 * @return is mirror
	 */
	public static boolean isMirror(IBlockAccess iblockaccess, int x, int y, int z) {

		PCma_TileEntityOptical teo = getTE(iblockaccess, x, y, z);

		return teo != null && teo.isMirror();

	}

	/**
	 * Get mirror color
	 * 
	 * @param iblockaccess
	 * @param x
	 * @param y
	 * @param z
	 * @return the color index (crystal meta)
	 */
	public static int getMirrorColor(IBlockAccess iblockaccess, int x, int y, int z) {

		PCma_TileEntityOptical teo = getTE(iblockaccess, x, y, z);

		if (teo == null) {
			return 0;
		}
		return teo.getMirrorColor();

	}

	/**
	 * Check if device is prism
	 * 
	 * @param iblockaccess
	 * @param x
	 * @param y
	 * @param z
	 * @return is prism
	 */
	public static boolean isPrism(IBlockAccess iblockaccess, int x, int y, int z) {
		PCma_TileEntityOptical teo = getTE(iblockaccess, x, y, z);

		return teo != null && teo.isPrism();
	}

	@Override
	public int getRenderColor(int i) {
		if (i == 0) {
			return 0x999999;
		}
		return 0xffffcc;
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		if (isMirror(iblockaccess, i, j, k)) {
			return 0x999999;
		}
		return 0xffffcc;
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("NO_PICKUP");
		set.add("TRANSLUCENT");
		set.add("OPTICAL");

		if (isMirror(world, pos.x, pos.y, pos.z)) {
			set.add("MIRROR");
		} else {
			set.add("PRISM");
		}

		return set;
	}

	@Override
	public Set<String> getItemFlags(ItemStack stack) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		set.add("ATTACHED");
		if (stack.getItemDamage() == 0) {
			set.add("MIRROR");
		} else {
			set.add("PRISM");
		}
		return set;
	}

}
