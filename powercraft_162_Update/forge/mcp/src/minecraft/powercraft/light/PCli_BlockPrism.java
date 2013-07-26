package powercraft.light;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_BeamTracer.BeamHitResult;
import powercraft.api.PC_BeamTracer.BeamSettings;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

@PC_BlockInfo(name="Prism", tileEntity=PCli_TileEntityPrism.class)
public class PCli_BlockPrism extends PC_Block implements PC_IItemInfo {

	public PCli_BlockPrism(int id) {
		super(id, Material.glass, "prism");
		float f = 0.4F;
		float f1 = 1.0F;
		setBlockBounds(0.5F - f, 0.1F, 0.5F - f, 0.5F + f, f1 - 0.1F, 0.5F + f);
		setHardness(1.0F);
		setResistance(4.0F);
		setStepSound(Block.soundStoneFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
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
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9) {
		ItemStack ihold = player.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.itemID != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().itemID];
				if(bhold.blockID != Block.thinGlass.blockID)
					return false;
			}
		}
		
		if(world.isRemote)
			return true;
		
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
			if (ihold.getItem().itemID == Block.thinGlass.blockID) {

				if (isGlassPanelOnSide(world, i, j, k, angle) == false) {

					PCli_TileEntityPrism teo = PC_Utils.getTE(world, i, j, k);
					if (teo != null) {
						teo.setPrismSide(angle, true);
					}
					if (!PC_Utils.isCreative(player)) {
						ihold.stackSize--;
					}
					drop = false;

				}

			}
		}
		if (drop) {

			if (isGlassPanelOnSide(world, i, j, k, angle)) {

				PCli_TileEntityPrism teo = PC_Utils.getTE(world, i, j, k);
				if (teo != null) {
					teo.setPrismSide(angle, false);
				}
				if (!PC_Utils.isCreative(player)) {
					PC_Utils.dropItemStack(world, i, j, k, new ItemStack(Block.thinGlass, 1));
				}

			}

		}

		return true;
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

		PCli_TileEntityPrism teo = PC_Utils.getTE(iblockaccess, x, y, z);

		if (teo == null) {
			return false;
		}

		return teo.getPrismSide(side);
	}
	
	@Override
	public int getRenderColor(int i) {
		return 0xffffcc;
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		return 0xffffcc;
	}
	
	public boolean renderInventoryBlock(int metadata, Object renderer) {
		float px = 0.0625F;
		setBlockBounds(3 * px, 3 * px, 3 * px, 12 * px, 12 * px, 12 * px);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(4 * px, 4 * px, 2 * px, 11 * px, 11 * px, 13 * px);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(2 * px, 4 * px, 4 * px, 13 * px, 11 * px, 11 * px);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(4 * px, 2 * px, 4 * px, 11 * px, 13 * px, 11 * px);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(0, 0, 0, 1, 1, 1);
		return true;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
		return true;
	}



	/** prism redirection vector for side */
	private static final PC_VecI[] prismMove = { new PC_VecI(0, -1, 0), new PC_VecI(0, 1, 0), new PC_VecI(1, 0, 0), new PC_VecI(1, 0, 1),
			new PC_VecI(0, 0, 1), new PC_VecI(-1, 0, 1), new PC_VecI(-1, 0, 0), new PC_VecI(-1, 0, -1), new PC_VecI(0, 0, -1),
			new PC_VecI(1, 0, -1) };
	
	/**
	 * Get index of prism side which faces the beam
	 * 
	 * @param move beam movement vector
	 * @return side number
	 */
	private int getPrismSideFacingMove(PC_VecI move) {
		for (int i = 0; i < 10; i++) {
			if (prismMove[i].equals(move.mul(-1))) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * get movement vector from prism's side
	 * 
	 * @param side the side number
	 * @return vector (coord)
	 */
	private PC_VecI getPrismOutputMove(int side) {
		return prismMove[side];
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

	@Override
	public BeamHitResult onBlockHitByBeam(World world, int x, int y, int z, BeamSettings settings) {
		
		PCli_TileEntityPrism prism = PC_Utils.getTE(world, x, y, z);

		int sideCount = 0;
		int[] side = new int[10];

		int thisPrismSide = getPrismSideFacingMove(settings.getMove());

		for (int h = 0; h < 10; h++) {
			if (prism.getPrismSide(h)) {
				side[sideCount] = h;
				sideCount++;
			}
		}

		if (sideCount >= 1) {

			for (int h = 0; h < sideCount; h++) {
				PC_VecI newMove = getPrismOutputMove(side[h]).copy();
				settings.getBeamTracer().forkBeam(new BeamSettings(settings.getBeamTracer(), settings.getPos(), newMove, settings.getColor(), settings.getStrength(), settings.getLength() / Math.round(sideCount * 0.75F)));
			}

		}

		if (sideCount > 0) {
			return BeamHitResult.STOP;
		}

		return BeamHitResult.CONTINUE;
	}
	
}
