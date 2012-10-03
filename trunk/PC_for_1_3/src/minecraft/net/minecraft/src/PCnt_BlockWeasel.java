package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.forge.ITextureProvider;

public class PCnt_BlockWeasel extends BlockContainer implements PC_ISwapTerrain, PC_IBlockType, ITextureProvider {

	@Override
	public int tickRate() {
		return 1;
	}

	/** List of gate updates (burn-out counting) */
	private static List<RedstoneUpdateInfo> burnoutList = new ArrayList<RedstoneUpdateInfo>();

	/**
	 * Check for burn-out (prevents immense lag caused by short circuits and
	 * fast pulsars)
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param addToList add to burn-out list, (false = only check)
	 * @return is burnt out
	 */
	private boolean checkForBurnout(World world, int x, int y, int z, boolean addToList) {
		if (addToList) {
			burnoutList.add(new RedstoneUpdateInfo(x, y, z, world.getWorldTime()));
		}

		int i = 0;

		for (int j = 0; j < burnoutList.size(); j++) {
			RedstoneUpdateInfo redstoneupdateinfo = burnoutList.get(j);

			if (redstoneupdateinfo.x == x && redstoneupdateinfo.y == y && redstoneupdateinfo.z == z && ++i >= 20) {
				return true;
			}
		}

		return false;
	}

	/**
	 * gate block
	 * 
	 * @param id block ID
	 */
	protected PCnt_BlockWeasel(int id) {
		super(id, Material.ground);
		blockIndexInTexture = 6;
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PCnt_TileEntityWeasel();
	}

	@Override
	public String getTextureFile() {
		return getTerrainFile();
	}

	/**
	 * Get weasel tile entity at position
	 * 
	 * @param iblockaccess block access
	 * @param x
	 * @param y
	 * @param z
	 * @return the tile entity or null
	 */
	public PCnt_TileEntityWeasel getTE(IBlockAccess iblockaccess, int x, int y, int z) {
		TileEntity te = iblockaccess.getBlockTileEntity(x, y, z);
		if (te == null) {
			return null;
		}
		return (PCnt_TileEntityWeasel) te;
	}

	/**
	 * Get weasel plugin at position
	 * 
	 * @param iblockaccess block access
	 * @param x
	 * @param y
	 * @param z
	 * @return the tile entity or null
	 */
	public PCnt_WeaselPlugin getPlugin(IBlockAccess iblockaccess, int x, int y, int z) {
		TileEntity te = iblockaccess.getBlockTileEntity(x, y, z);
		if (te == null || !(te instanceof PCnt_TileEntityWeasel)) {
			return null;
		}
		return ((PCnt_TileEntityWeasel) te).getPlugin();
	}

	/**
	 * Get gate type
	 * 
	 * @param iblockaccess world
	 * @param x x
	 * @param y y
	 * @param z z
	 * @return type index
	 */
	public int getType(IBlockAccess iblockaccess, int x, int y, int z) {
		PCnt_TileEntityWeasel tew = getTE(iblockaccess, x, y, z);
		return tew.getType();
	}

	/**
	 * Set device type
	 * 
	 * @param iblockaccess world
	 * @param x x
	 * @param y y
	 * @param z z
	 * @param type type index
	 */
	public void setType(IBlockAccess iblockaccess, int x, int y, int z, int type) {
		getTE(iblockaccess, x, y, z).setType(type);
	}

	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		return 6;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		return 6;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return PCnt_Renderer.weaselRenderer;
	}

	/**
	 * Get gate rotation, same as getRotation, but available statically
	 * 
	 * @param meta block meta
	 * @return rotation 0,1,2,3
	 */
	public static int getRotation(int meta) {
		return meta & 3;
	}

	@Override
	public String getTerrainFile() {
		return mod_PCnet.getTerrainFile();
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return -1;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {

		// drop the gate
		PCnt_TileEntityWeasel tew = getTE(world, x, y, z);

		if (tew != null) {

			PCnt_WeaselPlugin plugin = tew.getPlugin();

			if (plugin != null) {
				plugin.onBlockRemoval();

				if (!PC_Utils.isCreative()) {
					dropBlockAsItem_do(world, x, y, z, new ItemStack(mod_PCnet.weaselDevice, 1, tew.getType()));
				}
			}
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return true;
	}

	@Override
	public boolean canBlockStay(World world, int x, int y, int z) {
		return true;
	}

	// end of block setup

	// start of logic

	@Override
	public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int side) {
		return isPoweringTo(world, x, y, z, side);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {

		for (; burnoutList.size() > 0 && world.getWorldTime() - burnoutList.get(0).updateTime > 30L; burnoutList.remove(0)) {}
		if (checkForBurnout(world, x, y, z, false)) {
			// schedule "unpause" tick
			world.scheduleBlockUpdate(x, y, z, blockID, 3);
			return;
		}

		checkForBurnout(world, x, y, z, true);

		hugeUpdate(world, new PC_CoordI(x, y, z));

	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int x, int y, int z) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		PCnt_WeaselPlugin plugin = getPlugin(iblockaccess, x, y, z);
		if (plugin != null) {
			float[] bounds = plugin.getBounds();
			setBlockBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		setBlockBoundsBasedOnState(world, x, y, z);
		return super.getSelectedBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int l) {

		for (; burnoutList.size() > 0 && world.getWorldTime() - burnoutList.get(0).updateTime > 10L; burnoutList.remove(0)) {}
		if (checkForBurnout(world, x, y, z, false)) {
			world.scheduleBlockUpdate(x, y, z, blockID, 2);
			return;
		}
		
		checkForBurnout(world, x, y, z, true);

		getTE(world, x, y, z).onDirectPinChanged();

		return;
	}

	@Override
	public boolean isPoweringTo(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		int meta = iblockaccess.getBlockMetadata(x, y, z);
		int rotation = getRotation(meta);

		PCnt_TileEntityWeasel tew = getTE(iblockaccess, x, y, z);

		if (tew == null) return false;

		PCnt_WeaselPlugin plugin = tew.getPlugin();

		if (plugin == null) return false;

		boolean[] outputs = plugin.getWeaselOutputStates();

		for (int i = 0; i < rotation; i++) {
			boolean swap = outputs[0];
			outputs[0] = outputs[1];
			outputs[1] = outputs[2];
			outputs[2] = outputs[3];
			outputs[3] = swap;
		}

		boolean state = false;
		switch (side) {
			case 3:
				state = outputs[3];
				break;

			case 4:
				state = outputs[2];
				break;

			case 2:
				state = outputs[1];
				break;

			case 5:
				state = outputs[0];
				break;

			case 0:
				state = outputs[4];
				break;

			case 1:
				state = outputs[5];
				break;
		}

		return state;

	}

	/**
	 * Get redstone states on direct inputs. 0 BACK, 1 LEFT, 2 RIGHT, 3 FRONT, 5
	 * TOP, 4 BOTTOM
	 * 
	 * @param world
	 * @param pos the block position
	 * @return array of booleans
	 */
	public static boolean[] getWeaselInputStates(World world, PC_CoordI pos) {

		//@formatter:off
		boolean[] inputs = new boolean[]{
				powered_from_input(world, pos, 0),
				powered_from_input(world, pos, 1),
				powered_from_input(world, pos, 2),
				powered_from_input(world, pos, 3),
				powered_from_input(world, pos, 5),
				powered_from_input(world, pos, 4)
			};
		//@formatter:on

		return inputs;
	}

	/**
	 * Is the gate powered from given input? This method takes care of rotation
	 * for you. 0 BACK, 1 LEFT, 2 RIGHT, 3 FRONT, 4 BOTTOM, 5 TOP
	 * 
	 * @param world the World
	 * @param pos the block position
	 * @param inp the input number
	 * @return is powered
	 */
	public static boolean powered_from_input(World world, PC_CoordI pos, int inp) {
		if (world == null) return false;
		int x = pos.x, y = pos.y, z = pos.z;

		if (inp == 4) {
			boolean isProviding = (world.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0) || (world.getBlockId(x, y - 1, z) == Block.redstoneWire.blockID && world
					.getBlockMetadata(x, y - 1, z) > 0));
			return isProviding;
		}
		if (inp == 5) {
			boolean isProviding = (world.isBlockIndirectlyProvidingPowerTo(x, y + 1, z, 1) || (world.getBlockId(x, y + 1, z) == Block.redstoneWire.blockID && world
					.getBlockMetadata(x, y + 1, z) > 0));
			return isProviding;
		}

		int rotation = getRotation(world.getBlockMetadata(x, y, z));
		int N0 = 0, N1 = 1, N2 = 2, N3 = 3;
		if (inp == 0) {
			N0 = 0;
			N1 = 1;
			N2 = 2;
			N3 = 3;
		}
		if (inp == 1) {
			N0 = 3;
			N1 = 0;
			N2 = 1;
			N3 = 2;
		} else if (inp == 2) {
			N0 = 1;
			N1 = 2;
			N2 = 3;
			N3 = 0;
		} else if (inp == 3) {
			N0 = 2;
			N1 = 3;
			N2 = 0;
			N3 = 1;
		}

		if (rotation == N0) {
			return (world.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3) || world.getBlockId(x, y, z + 1) == Block.redstoneWire.blockID
					&& world.getBlockMetadata(x, y, z + 1) > 0);
		}
		if (rotation == N1) {
			return (world.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4) || world.getBlockId(x - 1, y, z) == Block.redstoneWire.blockID
					&& world.getBlockMetadata(x - 1, y, z) > 0);
		}
		if (rotation == N2) {
			return (world.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2) || world.getBlockId(x, y, z - 1) == Block.redstoneWire.blockID
					&& world.getBlockMetadata(x, y, z - 1) > 0);
		}
		if (rotation == N3) {
			return (world.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5) || world.getBlockId(x + 1, y, z) == Block.redstoneWire.blockID
					&& world.getBlockMetadata(x + 1, y, z) > 0);
		}
		return false;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving) {

		//int type = getType(world, x, y, z);

		int l = ((MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 2) % 4;

		if (PC_Utils.isPlacingReversed()) {
			l = PC_Utils.reverseSide(l);
		}

		world.setBlockMetadataWithNotify(x, y, z, l);

		PCnt_WeaselPlugin plugin = getPlugin(world, x, y, z);
		if (plugin != null) {
			plugin.onBlockPlaced(entityliving);
		}

	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		hugeUpdate(world, new PC_CoordI(x, y, z));
		super.onBlockAdded(world, x, y, z);
	}

	/**
	 * Perform hide redstone update around this gate.
	 * 
	 * @param world
	 * @param pos
	 */
	public static void hugeUpdate(World world, PC_CoordI pos) {
		if (world == null) return;
		int x = pos.x, y = pos.y, z = pos.z, id = mod_PCnet.weaselDevice.blockID;
		world.notifyBlocksOfNeighborChange(x, y, z, id);
		world.notifyBlocksOfNeighborChange(x + 1, y, z, id);
		world.notifyBlocksOfNeighborChange(x - 1, y, z, id);
		world.notifyBlocksOfNeighborChange(x, y, z + 1, id);
		world.notifyBlocksOfNeighborChange(x, y, z - 1, id);
		world.notifyBlocksOfNeighborChange(x, y - 1, z, id);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, id);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		PCnt_WeaselPlugin plugin = getPlugin(world, x, y, z);

		if (plugin != null) {
			plugin.onRandomDisplayTick(random);
			if (plugin.hasError()) {
				double d = (x + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
				double d1 = (y + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
				double d2 = (z + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;

				world.spawnParticle("largesmoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
			}
		}
	}


	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		
		ItemStack ihold = player.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock) {

				if (ihold.getItem().shiftedIndex == mod_PCnet.weaselDevice.blockID) {

					if (ihold.getItemDamage() != getType(world, x, y, z)) {
						return false;
					}

				} else {

					Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
					if (bhold instanceof PC_IBlockType) {

						return false;

					}

				}

			} else if (ihold.getItem().shiftedIndex == mod_PCcore.activator.shiftedIndex) {
				
				PCnt_WeaselPlugin plugin = getPlugin(world, x, y, z);
				if (plugin == null) return true;

				if (plugin.isMaster()) {

					if(!world.isRemote){
					
						if (ihold.hasTagCompound()) {
							ihold.getTagCompound().setString("WeaselNetwork", plugin.getNetwork().getName());
						} else {
							NBTTagCompound tag = new NBTTagCompound();
							tag.setString("WeaselNetwork", plugin.getNetwork().getName());
							ihold.setTagCompound(tag);
						}
	
						PC_Utils.chatMsg(PC_Lang.tr("pc.weasel.activatorGetNetwork", new String[] { plugin.getNetwork().getName() }), true);
					
					}
					
				} else {
					if (ihold.hasTagCompound()) {
						String network = ihold.getTagCompound().getString("WeaselNetwork");
						if (!network.equals("")) {
							if(!world.isRemote){
								plugin.setNetworkNameAndConnect(network);
								PC_Utils.chatMsg(PC_Lang.tr("pc.weasel.activatorSetNetwork", new String[] { plugin.getNetwork().getName() }), true);
							}else
								world.playSoundEffect(x, y, z, "note.snare", 1.0F, 0.5F);
						}
					}
				}
				return true;
			}
		}

		PCnt_WeaselPlugin plugin = getPlugin(world, x, y, z);
		if (plugin != null) return plugin.onClick(player);

		return false;
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {
		Set<String> set = new HashSet<String>();
		set.add("NO_HARVEST");
		set.add("TRANSLUCENT");
		set.add("REDSTONE");
		set.add("LOGIC");
		set.add("WEASEL");
		return set;
	}

	@Override
	public Set<String> getItemFlags(ItemStack itemstack) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		set.add("WEASEL");
		return set;
	}


}
