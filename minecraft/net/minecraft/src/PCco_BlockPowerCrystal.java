package net.minecraft.src;


import java.util.Random;

import net.minecraft.src.forge.ITextureProvider;


/**
 * Power Crystal block.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCco_BlockPowerCrystal extends BlockBreakable implements PC_ISwapTerrain, ITextureProvider {

	/** enable sounds */
	public static boolean makeSound;

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		onBlockPlaced(world, i, j, k, 0);
	}

	@Override
	public int getBlockColor() {
		return PC_Color.crystal_colors[2];
	}

	@Override
	public int getRenderColor(int i) {
		return PC_Color.crystal_colors[MathHelper.clamp_int(i, 0, 7)];
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		return PC_Color.crystal_colors[MathHelper.clamp_int(iblockaccess.getBlockMetadata(i, j, k), 0, 7)];
	}

	/**
	 * power crystal
	 * 
	 * @param i id
	 * @param j texture index
	 */
	public PCco_BlockPowerCrystal(int i, int j) {
		super(i, j, Material.glass, false);
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return true;
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	protected int damageDropped(int i) {
		return i;
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return blockID;
	}

	@Override
	public int getMobilityFlag() {
		return 0;
	}

	@Override
	public String getTerrainFile() {
		return mod_PCcore.getTerrainFile();
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return PCco_Renderer.crystalRenderer;
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {



		int id_under = world.getBlockId(i, j - 1, k);
		if (makeSound && mod_PCcore.soundsEnabled) {
			EntityPlayer player = world.getClosestPlayer(i + 0.5D, j + 0.5D, k + 0.5D, 12);
			if (player != null) {

				if (id_under == Block.stone.blockID || id_under == 7 || id_under == blockID) {

					int distance = (int) Math.round(player.getDistanceSq(i + 0.5D, j + 0.5D, k + 0.5D) / 10);

					if (distance == 0) {
						distance = 1;
					}

					if (random.nextInt(distance) == 0) {
						world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "random.orb", 0.15F, 0.5F * ((random.nextFloat() - random.nextFloat()) * 0.7F + 1.8F));
					}

				}
			}
		}

		int meta = world.getBlockMetadata(i, j, k);

		if (meta == 8) {
			world.setBlockMetadataWithNotify(i, j, k, 0);
			meta = 0;
		}
		double r = PC_Color.red(getRenderColor(meta));
		double g = PC_Color.green(getRenderColor(meta));
		double b = PC_Color.blue(getRenderColor(meta));

		r = (r > 0D ? r : 0.001D);
		g = (g > 0D ? g : 0.001D);
		b = (b > 0D ? b : 0.001D);

		float y = j + random.nextFloat();
		float x = i + random.nextFloat();
		float z = k + random.nextFloat();

		world.spawnParticle("reddust", x, y, z, r, g, b);
	}
}
