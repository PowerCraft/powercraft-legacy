package powercraft.light;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import powercraft.core.PC_BeamTracer;
import powercraft.core.PC_BeamTracer.result;
import powercraft.core.PC_Block;
import powercraft.core.PC_Color;
import powercraft.core.PC_CoordD;
import powercraft.core.PC_CoordI;
import powercraft.core.PC_IBeamSpecialHandling;
import powercraft.core.PC_IBlockRenderer;
import powercraft.core.PC_ICraftingToolDisplayer;
import powercraft.core.PC_MathHelper;
import powercraft.core.PC_Renderer;
import powercraft.core.PC_Utils;

public class PCli_BlockMirrow extends PC_Block implements PC_IBeamSpecialHandling, PC_IBlockRenderer, PC_ICraftingToolDisplayer {

	public PCli_BlockMirrow(int id) {
		super(id, Material.glass);
		float f = 0.4F;
		float f1 = 1.0F;
		setBlockBounds(0.5F - f, 0.1F, 0.5F - f, 0.5F + f, f1 - 0.1F, 0.5F + f);
		setHardness(1.0F);
		setResistance(4.0F);
		setStepSound(Block.soundStoneFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public String getDefaultName() {
		return "Mirrow";
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PCli_TileEntityMirrow();
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
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving player)
    {
		int m = MathHelper.floor_double((((player.rotationYaw + 180F) * 16F) / 360F) + 0.5D) & 0xf;
		world.setBlockMetadataWithNotify(i, j, k, m);
    }
	
	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9) {
		ItemStack ihold = player.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.itemID == PC_Utils.getPCObjectIDByName("PCco_BlockPowerCrystal")) {

				PCli_TileEntityMirrow teo = PC_Utils.getTE(world, i, j, k, blockID);
				if (teo != null) {
					teo.setMirrorColor(ihold.getItemDamage());
				}

				return true;
			}

			if (ihold.getItem() instanceof ItemBlock && ihold.itemID != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
				return false;
			}
		}

		int m = MathHelper.floor_double((((player.rotationYaw + 180F) * 16F) / 360F) + 0.5D) & 0xf;
		world.setBlockMetadataWithNotify(i, j, k, m);

		return true;
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

		PCli_TileEntityMirrow teo = PC_Utils.getTE(iblockaccess, x, y, z);

		if (teo == null) {
			return 0;
		}
		return teo.getMirrorColor();

	}

	@Override
	public int getRenderColor(int i) {
		return 0x999999;
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		return 0x999999;
	}

	/** angle rounded to 45 for vertical beam colliding with mirror */
	private static final int mirrorTo45[] = { 0, 0, 45, 90, 90, 90, 135, 180, 180, 180, 225, 270, 270, 270, 315, 0 };
	
	/**
	 * Get horizontal angle from movement vector
	 * 
	 * @param move movement vector
	 * @return angle
	 */
	private static float getAngleFromMove(PC_CoordI move) {
		float beamAngle = 0;
		if (move.x == 0 && move.z == -1) {
			beamAngle = 0;
		}
		if (move.x == 1 && move.z == -1) {
			beamAngle = 45;
		}
		if (move.x == 1 && move.z == 0) {
			beamAngle = 90;
		}
		if (move.x == 1 && move.z == 1) {
			beamAngle = 135;
		}
		if (move.x == 0 && move.z == 1) {
			beamAngle = 180;
		}
		if (move.x == -1 && move.z == 1) {
			beamAngle = 225;
		}
		if (move.x == -1 && move.z == 0) {
			beamAngle = 270;
		}
		if (move.x == -1 && move.z == -1) {
			beamAngle = 315;
		}
		return beamAngle;
	}
	
	/** mirror angle for meta */
	private static final float mirrorAngle[] = new float[16];
	static {
		for (int a = 0; a < 8; a++) {
			mirrorAngle[a] = a * 22.5F;
			mirrorAngle[a + 8] = a * 22.5F;
		}
	}
	
	/**
	 * Get movement vector from angle
	 * 
	 * @param angle
	 * @return vector (coord)
	 */
	private static PC_CoordI getMoveFromAngle(float angle) {
		int angleint = Math.round(angle);
		switch (angleint) {
			case 0:
				return new PC_CoordI(0, 0, -1);
			case 45:
				return new PC_CoordI(1, 0, -1);
			case 90:
				return new PC_CoordI(1, 0, 0);
			case 135:
				return new PC_CoordI(1, 0, 1);
			case 180:
				return new PC_CoordI(0, 0, 1);
			case 225:
				return new PC_CoordI(-1, 0, 1);
			case 270:
				return new PC_CoordI(-1, 0, 0);
			case 315:
				return new PC_CoordI(-1, 0, -1);
		}
		return null;
	}

	/**
	 * Get real difference of two angles
	 * 
	 * @param firstAngle
	 * @param secondAngle
	 * @return result
	 */
	private static float angleDiff(float firstAngle, float secondAngle) {

		float difference = secondAngle - firstAngle;

		while (difference < -180) {
			difference += 360;
		}

		while (difference > 180) {
			difference -= 360;
		}

		return difference;

	}

	/**
	 * Convert invalid angle to 0-360
	 * 
	 * @param angle to convert
	 * @return converted
	 */
	private static float fixAngle(float angle) {

		while (angle > 360) {
			angle -= 360;
		}

		while (angle < 0) {
			angle += 360;
		}

		return angle;

	}
	
	@Override
	public result onHitByBeamTracer(PC_BeamTracer beamTracer, PC_CoordI cnt, PC_CoordI move, PC_Color color, float strength, int distanceToMove) {
		int mirrorColor = PCli_BlockMirrow.getMirrorColor(beamTracer.getWorld(), cnt.x, cnt.y, cnt.z);
		if (mirrorColor == -1 || mirrorColor == color.getMeta()) {
			// vertical beam
			if (move.x == 0 && move.z == 0) {
	
				int a = mirrorTo45[PC_Utils.getMD(beamTracer.getWorld(), cnt.x, cnt.y, cnt.z)];
				PC_CoordI reflected = getMoveFromAngle(a).getInverted();
	
				move.x = reflected.x;
				move.z = reflected.z;
	
			} else {
				float beamAngle = getAngleFromMove(move);
				float mAngle = mirrorAngle[PC_Utils.getMD(beamTracer.getWorld(), cnt.x, cnt.y, cnt.z)];
	
				float diff = angleDiff(beamAngle, mAngle);
	
				// the reflection
				float beamNew = beamAngle + diff * 2;
	
				beamNew = fixAngle(beamNew);
	
				PC_CoordI reflected = getMoveFromAngle(beamNew).getInverted();
	
				move.x = reflected.x;
				move.z = reflected.z;
			}
		}
		return result.CONTINUE;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer) {
		Block steel = Block.blockSteel;
		float px = 0.0625F;
		steel.setBlockBounds(0 * px, 6 * px, 7 * px, 15 * px, 15 * px, 9 * px);
		PC_Renderer.renderInvBox(renderer, steel, 0);
		steel.setBlockBounds(3 * px, 0 * px, 7 * px, 5 * px, 6 * px, 9 * px);
		PC_Renderer.renderInvBox(renderer, steel, 0);
		steel.setBlockBounds(10 * px, 0 * px, 7 * px, 12 * px, 6 * px, 9 * px);
		PC_Renderer.renderInvBox(renderer, steel, 0);
		steel.setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		
	}

	@Override
	public String getCraftingToolModule() {
		return mod_PowerCraftLight.getInstance().getNameWithoutPowerCraft();
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}
	
	@Override
	public List<String> getBlockFlags(World world, PC_CoordI pos, List<String> list) {

		list.add(PC_Utils.NO_HARVEST);
		list.add(PC_Utils.NO_PICKUP);
		list.add(PC_Utils.PASSIVE);
		return list;
	}

	@Override
	public List<String> getItemFlags(ItemStack stack, List<String> list) {
		list.add(PC_Utils.NO_BUILD);
		return list;
	}
	
}
