package net.minecraft.src;


import java.util.Random;


/**
 * Ore Generator which supports block metadata.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_WorldGenMinableMetadata extends WorldGenerator {

	private int id, meta, size;

	/**
	 * Get generator for id and metadata.
	 * 
	 * @param blockID block id
	 * @param blockMeta block meta
	 * @param depositSize size of the deposit
	 */
	public PC_WorldGenMinableMetadata(int blockID, int blockMeta, int depositSize) {
		super();
		id = blockID;
		meta = blockMeta;
		size = depositSize;
	}

	/* This is copied from WorldGenMinable, but altered to support metadata. */
	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		float f = par2Random.nextFloat() * (float) Math.PI;
		double d = (par3 + 8) + (MathHelper.sin(f) * size) / 8F;
		double d1 = (par3 + 8) - (MathHelper.sin(f) * size) / 8F;
		double d2 = (par5 + 8) + (MathHelper.cos(f) * size) / 8F;
		double d3 = (par5 + 8) - (MathHelper.cos(f) * size) / 8F;
		double d4 = (par4 + par2Random.nextInt(3)) - 2;
		double d5 = (par4 + par2Random.nextInt(3)) - 2;

		for (int i = 0; i <= size; i++) {
			double d6 = d + ((d1 - d) * i) / size;
			double d7 = d4 + ((d5 - d4) * i) / size;
			double d8 = d2 + ((d3 - d2) * i) / size;
			double d9 = (par2Random.nextDouble() * size) / 16D;
			double d10 = (MathHelper.sin((i * (float) Math.PI) / size) + 1.0F) * d9 + 1.0D;
			double d11 = (MathHelper.sin((i * (float) Math.PI) / size) + 1.0F) * d9 + 1.0D;
			int j = MathHelper.floor_double(d6 - d10 / 2D);
			int k = MathHelper.floor_double(d7 - d11 / 2D);
			int l = MathHelper.floor_double(d8 - d10 / 2D);
			int i1 = MathHelper.floor_double(d6 + d10 / 2D);
			int j1 = MathHelper.floor_double(d7 + d11 / 2D);
			int k1 = MathHelper.floor_double(d8 + d10 / 2D);

			for (int l1 = j; l1 <= i1; l1++) {
				double d12 = ((l1 + 0.5D) - d6) / (d10 / 2D);

				if (d12 * d12 >= 1.0D) {
					continue;
				}

				for (int i2 = k; i2 <= j1; i2++) {
					double d13 = ((i2 + 0.5D) - d7) / (d11 / 2D);

					if (d12 * d12 + d13 * d13 >= 1.0D) {
						continue;
					}

					for (int j2 = l; j2 <= k1; j2++) {
						double d14 = ((j2 + 0.5D) - d8) / (d10 / 2D);

						if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && par1World.getBlockId(l1, i2, j2) == Block.stone.blockID) {
							par1World.setBlockAndMetadata(l1, i2, j2, id, meta);
						}
					}
				}
			}
		}

		return true;
	}

}
