package net.minecraft.src;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * Radio device block with tile entity.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_BlockRadio extends BlockContainer implements PC_IBlockType {

	/**
	 * @param id ID
	 */
	protected PClo_BlockRadio(int id) {
		super(id, Block.stone.blockIndexInTexture, Material.ground);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.255F, 1.0F);
	}

	@Override
	public void addCollidingBlockToList(World world, int x, int y, int z, AxisAlignedBB collidedbox, List list, Entity par7Entity) {
		setBlockBoundsBasedOnState(world, x, y, z);
		super.addCollidingBlockToList(world, x, y, z, collidedbox, list, par7Entity);
//
//		setBlockBounds(0.65F, 0, 0.65F, 0.95F, 0.9F, 0.65F);
//		super.getCollidingBoundingBoxes(world, x, y, z, collidedbox, list);
	}

	@Override
	public void setBlockBoundsForItemRender() {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.255F, 1.0F);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.255F, 1.0F);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		return true;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		// stonebrick texture
		return 6;
	}

	@Override
	public int getRenderType() {
		// only for inventory.
		return PClo_Renderer.radioRenderer;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		ItemStack ihold = entityplayer.getCurrentEquippedItem();

		PClo_TileEntityRadio ter = getTE(world, x, y, z);
		if (ter == null) return false;


		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
				if (bhold instanceof PC_IBlockType) {
					return false;
				}
			} else if (ihold.getItem().shiftedIndex == mod_PCcore.activator.shiftedIndex) {

				if (ter.isTransmitter()) {

					if (ihold.hasTagCompound()) {
						ihold.getTagCompound().setString("RadioChannel", ter.channel);
					} else {
						NBTTagCompound tag = new NBTTagCompound();
						tag.setString("RadioChannel", ter.channel);
						ihold.setTagCompound(tag);
					}

					PC_Utils.chatMsg(PC_Lang.tr("pc.radio.activatorGetChannel", new String[] { ter.channel }), true);

				} else {
					if (ihold.hasTagCompound()) {
						String chnl = ihold.getTagCompound().getString("RadioChannel");
						if (!chnl.equals("")) {
							ter.channel = chnl;

							PC_CoordI pos = ter.getCoord();

							ter.active = mod_PClogic.RADIO.getChannelState(chnl);
							if (ter.active) {
								PC_Utils.mc().theWorld.setBlockMetadataWithNotify(pos.x, pos.y, pos.z, 1);
							}

							PC_Utils.mc().theWorld.scheduleBlockUpdate(pos.x, pos.y, pos.z, mod_PClogic.radio.blockID, 1);

							PC_Utils.chatMsg(PC_Lang.tr("pc.radio.activatorSetChannel", new String[] { chnl }), true);
							world.playSoundEffect(x, y, z, "note.snare", (world.rand.nextFloat() + 0.7F) / 2.0F, 0.5F);
						}
					}
				}
				return true;
			}
		}

		ItemStack holditem = entityplayer.getCurrentEquippedItem();
		if (holditem != null) {
			if (holditem.itemID == mod_PClogic.portableTx.shiftedIndex) {
				String channel = mod_PClogic.default_radio_channel;

				channel = getTE(world, x, y, z).getChannel();

				PClo_ItemRadioRemote.setChannel(holditem, channel);
				world.playSoundAtEntity(entityplayer, "note.snare", (world.rand.nextFloat() + 0.7F) / 2.0F,
						1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
				return true;
			}
		}

		int rtype = ter.isTransmitter() ? PClo_GuiRadio.TRANSMITTER : PClo_GuiRadio.RECEIVER;
		String channel = ter.getChannel();

		PC_Utils.openGres(entityplayer, new PClo_GuiRadio(entityplayer.dimension, ter.getCoord(), channel, rtype));

		return true;
	}

	/**
	 * Get radio tile entity at coordinates
	 * 
	 * @param iblockaccess block access
	 * @param i x
	 * @param j y
	 * @param k z
	 * @return the tile entity or null
	 */
	public static PClo_TileEntityRadio getTE(IBlockAccess iblockaccess, int i, int j, int k) {
		TileEntity te = iblockaccess.getBlockTileEntity(i, j, k);
		if (te == null) {
			return null;
		}
		PClo_TileEntityRadio ter = (PClo_TileEntityRadio) te;

		return ter;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return -1;
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
		world.setBlockAndMetadataWithNotify(i, j, k, 0, 0);
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);

		PClo_TileEntityRadio ter = getTE(world, i, j, k);

		if (ter.isTransmitter()) {
			mod_PClogic.RADIO.disconnectFromRedstoneBus(ter);
		}



		if (!PC_Utils.isCreative()) {
			dropBlockAsItem_do(world, i, j, k, new ItemStack(mod_PClogic.radio, 1, getTE(world, i, j, k).isTransmitter() ? 0 : 1));
		}

		super.breakBlock(world, i, j, k, par5, par6);
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		int meta = world.getBlockMetadata(i, j, k);

		PClo_TileEntityRadio ter = getTE(world, i, j, k);

		if (ter.isTransmitter()) {

			boolean on = (meta == 1);
			boolean power = isGettingPower(world, i, j, k);

			if (on && !power) {
				world.setBlockMetadataWithNotify(i, j, k, 0);
				ter.setTransmitterState(false);

			} else if (!on && power) {
				world.setBlockMetadataWithNotify(i, j, k, 1);
				ter.setTransmitterState(true);
			}

		} else {

			boolean on = (meta == 1);
			boolean power = ter.active;

			if (on && !power) {
				world.setBlockMetadataWithNotify(i, j, k, 0);
				ter.setTransmitterState(false);

			} else if (!on && power) {
				world.setBlockMetadataWithNotify(i, j, k, 1);
				ter.setTransmitterState(true);
			}
		}

		world.markBlockNeedsUpdate(i, j, k);
		world.notifyBlocksOfNeighborChange(i, j - 1, k, world.getBlockId(i, j - 1, k));
	}

	private boolean isGettingPower(World world, int i, int j, int k) {
		return ((world.isBlockGettingPowered(i, j, k) || world.isBlockGettingPowered(i, j - 1, k) || world.isBlockIndirectlyGettingPowered(i, j, k) || world
				.isBlockIndirectlyGettingPowered(i, j - 1, k)));
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {

		world.scheduleBlockUpdate(i, j, k, blockID, 1);

	}

	@Override
	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		int meta = iblockaccess.getBlockMetadata(i, j, k);
		if (getTE(iblockaccess, i, j, k).isReceiver() && meta == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l) {
		return isPoweringTo(world, i, j, k, l);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		int i1 = world.getBlockMetadata(i, j, k);
		if (i1 != 1) {
			return;
		}

		boolean tiny = getTE(world, i, j, k).renderMicro;

		double x = (i + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double y = (j + (tiny ? 0.2F : 0.9F)) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double z = (k + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;

		world.spawnParticle("reddust", x, y, z, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public int getMobilityFlag() {
		return 2;
	}

	@Override
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("NO_PICKUP");
		set.add("TRANSLUCENT");
		set.add("REDSTONE");
		set.add("LOGIC");
		set.add("RADIO");

		return set;
	}

	@Override
	public Set<String> getItemFlags(ItemStack stack) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		set.add("RADIO");
		return set;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new PClo_TileEntityRadio();
	}
}
