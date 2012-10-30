package powercraft.core;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;

public class PCco_BlockPowerCrystal extends PC_Block implements PC_IConfigLoader, PC_ISwapTerrain, PC_IBlockRenderer, PC_IWorldSpawnGenerate {

	/** enable sounds */
	public static boolean makeSound;
	public static int genCrystalsInChunk;
	public static int genCrystalsDepositMaxCount;
	public static int genCrystalsMaxY;
	public static int genCrystalsMinY;
	
	public PCco_BlockPowerCrystal(int id){
		super(id, 49, Material.glass);
		setHardness(0.5F);
		setResistance(0.5F);
		setBlockName("PCcoPowerCrystal");
		setStepSound(Block.soundGlassFootstep);
		setLightValue(1.0F);
		setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
	public void loadFromConfig(Configuration config) {
		setLightValue(PC_Utils.getConfigInt(config, Configuration.CATEGORY_GENERAL, "pc.powercrystal.bright", 16) * 0.0625F);
		makeSound = PC_Utils.getConfigBool(config, Configuration.CATEGORY_GENERAL, "pc.powercrystal.makeSound", true);
		genCrystalsInChunk = PC_Utils.getConfigInt(config, Configuration.CATEGORY_GENERAL, "pc.powercrystal.in_chunk", 3, "Number of deposits in each 16x16 chunk.");
		genCrystalsDepositMaxCount = PC_Utils.getConfigInt(config, Configuration.CATEGORY_GENERAL, "pc.powercrystal.deposit_max_size", 4, "Highest crystal count in one deposit");
		genCrystalsMaxY = PC_Utils.getConfigInt(config, Configuration.CATEGORY_GENERAL, "pc.powercrystal.min_y", 5, "Min Y coordinate of crystal deposits.");
		genCrystalsMinY = PC_Utils.getConfigInt(config, Configuration.CATEGORY_GENERAL, "pc.powercrystal.max_y", 15, "Max Y coordinate of crystal deposits.");
	}

	@Override
	public String getDefaultName() {
		return "Power Crystal";
	}
	
	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		/**
		 * @todo
		 * onBlockPlaced(world, i, j, k, 0);
		 */
		super.onBlockAdded(world, i, j, k);
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
	public int damageDropped(int i) {
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
		return mod_PowerCraftCore.getInstance().getTerrainFile();
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return PC_Renderer.getRendererID(true);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {



		int id_under = world.getBlockId(i, j - 1, k);
		if (makeSound && PC_Utils.isSoundEnabled()) {
			EntityPlayer player = world.getClosestPlayer(i + 0.5D, j + 0.5D, k + 0.5D, 12);
			if (player != null) {

				if (id_under == Block.stone.blockID || id_under == 7 || id_under == blockID) {

					int distance = (int) Math.round(player.getDistanceSq(i + 0.5D, j + 0.5D, k + 0.5D) / 10);

					if (distance == 0) {
						distance = 1;
					}

					if (random.nextInt(distance) == 0) {
						PC_Utils.playSound(i + 0.5D, j + 0.5D, k + 0.5D, "random.orb", 0.15F,
								0.5F * ((random.nextFloat() - random.nextFloat()) * 0.7F + 1.8F));
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

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			Object renderer) {
		PC_Renderer.bindTexture(((PC_ISwapTerrain) block).getTerrainFile());
		
		Random posRand = new Random(metadata);

		for (int q = 3 + posRand.nextInt(3); q > 0; q--) {
			float x, y, z, a, b, c;
			x = 0.0F + posRand.nextFloat() * 0.6F;
			y = 0.0F + posRand.nextFloat() * 0.6F;
			z = 0.0F + posRand.nextFloat() * 0.6F;

			a = 0.2F + Math.max(posRand.nextFloat() * (0.7F - x), 0.3F);
			b = 0.2F + Math.max(posRand.nextFloat() * (0.7F - y), 0.3F);
			c = 0.2F + Math.max(posRand.nextFloat() * (0.7F - z), 0.3F);

			block.setBlockBounds(x, y, z, x + a, y + b, z + c);
			PC_Renderer.renderInvBox(renderer, block, metadata);
		}
		block.setBlockBounds(0, 0, 0, 1, 1, 1);
		PC_Renderer.bindTexture("/terrain.png");

	}

	@Override
	public void renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, Object renderer) {
		PC_Renderer.tessellatorDraw();
		PC_Renderer.tessellatorStartDrawingQuads();
		PC_Renderer.bindTexture(((PC_ISwapTerrain) block).getTerrainFile());
		
		Random posRand = new Random(x + x * y * z + z + world.getBlockMetadata(x, y, z));

		for (int q = 3 + posRand.nextInt(2); q > 0; q--) {
			float i, j, k, a, b, c;

			i = posRand.nextFloat() * 0.6F;
			j = (q == 2 ? 0.001F : posRand.nextFloat() * 0.6F);
			k = posRand.nextFloat() * 0.6F;

			a = i + 0.3F + posRand.nextFloat() * (0.7F - i);
			b = j + 0.3F + posRand.nextFloat() * (0.7F - j);
			c = k + 0.3F + posRand.nextFloat() * (0.7F - k);

			block.setBlockBounds(i, j, k, a, b, c);
			PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		}
		block.setBlockBounds(0, 0, 0, 1, 1, 1);
		
		PC_Renderer.tessellatorDraw();
		PC_Renderer.tessellatorStartDrawingQuads();
		PC_Renderer.bindTexture("/terrain.png");
		
	}

	@Override
	public int getSpawnsInChunk(Random random) {
		return genCrystalsInChunk;
	}

	@Override
	public int getBlocksOnSpawnPoint(Random random) {
		return random.nextInt(MathHelper.clamp_int(genCrystalsDepositMaxCount - 1, 1, 10)) + 2;
	}

	@Override
	public PC_CoordI getSpawnPoint(Random random) {
		return new PC_CoordI(random.nextInt(16), 
				random.nextInt(MathHelper.clamp_int(genCrystalsMaxY - genCrystalsMinY, 1, 255)) + genCrystalsMinY, 
				random.nextInt(16));
	}
	
}
