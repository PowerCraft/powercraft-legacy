package powercraft.net;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.Communication;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

public class PCnt_BlockRadio extends PC_Block {

	/**
	 * @param id ID
	 */
	public PCnt_BlockRadio(int id) {
		super(id, Block.stone.blockIndexInTexture, Material.ground);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.255F, 1.0F);
		setHardness(0.35F);
		setResistance(30.0F);
		setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCnt_TileEntityRadio();
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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		ItemStack ihold = entityplayer.getCurrentEquippedItem();

		PCnt_TileEntityRadio ter = GameInfo.getTE(world, x, y, z);
		if (ter == null) return false;


		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().itemID != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().itemID];
				return false;
			} else if (ihold.getItem().itemID == ModuleInfo.getPCObjectIDByName("PCco_ItemActivator")) {

				if (ter.isTransmitter()) {

					if (ihold.hasTagCompound()) {
						ihold.getTagCompound().setString("RadioChannel", ter.getChannel());
					} else {
						NBTTagCompound tag = new NBTTagCompound();
						tag.setString("RadioChannel", ter.getChannel());
						ihold.setTagCompound(tag);
					}

					Communication.chatMsg(Lang.tr("pc.radio.activatorGetChannel", new String[] { ter.getChannel() }), true);

				} else {
					if (ihold.hasTagCompound()) {
						String chnl = ihold.getTagCompound().getString("RadioChannel");
						if (!chnl.equals("")) {
							
							ter.setChannel(chnl);

							PC_VecI pos = ter.getCoord();

							ter.setActive(PCnt_RadioManager.getChannelState(chnl));
							if (ter.isActive()) {
								world.setBlockMetadataWithNotify(pos.x, pos.y, pos.z, 1);
							}

							world.scheduleBlockUpdate(pos.x, pos.y, pos.z, blockID, 1);

							Communication.chatMsg(Lang.tr("pc.radio.activatorSetChannel", new String[] { chnl }), true);
							world.playSoundEffect(x, y, z, "note.snare", (world.rand.nextFloat() + 0.7F) / 2.0F, 0.5F);
						}
					}
				}
				return true;
			}
		}

		ItemStack holditem = entityplayer.getCurrentEquippedItem();
		if (holditem != null) {
			if (holditem.itemID == PCnt_App.portableTx.itemID) {
				String channel = PCnt_RadioManager.default_radio_channel;

				channel = GameInfo.<PCnt_TileEntityRadio>getTE(world, x, y, z).getChannel();

				PCnt_ItemRadioRemote.setChannel(holditem, channel);
				world.playSoundAtEntity(entityplayer, "note.snare", (world.rand.nextFloat() + 0.7F) / 2.0F,
						1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
				return true;
			}
		}

		Gres.openGres("Radio", entityplayer, ter);
		
		/*int rtype = ter.isTransmitter() ? PClo_GuiRadio.TRANSMITTER : PClo_GuiRadio.RECEIVER;
		String channel = ter.getChannel();

		PC_Utils.openGres(entityplayer, new PClo_GuiRadio(entityplayer.dimension, ter.getCoord(), channel, rtype));*/

		return true;
	}

	
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLiving entity) {
		world.scheduleBlockUpdate(x, y, z, blockID, tickRate());
		super.onBlockPlacedBy(world, x, y, z, entity);
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
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
		PCnt_TileEntityRadio te = GameInfo.getTE(world, x, y, z);
        boolean remove = super.removeBlockByPlayer(world, player, x, y, z);

        if (remove){
        	if (te.isTransmitter() && te.isActive() && !world.isRemote) {
    			PCnt_RadioManager.transmitterOff(te.getChannel());
    		}
        	 if (!GameInfo.isCreative(player)){
        		 dropBlockAsItem_do(world, x, y, z, new ItemStack(PCnt_App.radio, 1, te.getType()));
        	 }
        }

        return remove;
    }

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		int meta = world.getBlockMetadata(i, j, k);

		PCnt_TileEntityRadio ter = GameInfo.getTE(world, i, j, k);

		if (ter.isTransmitter()) {

			ter.setTransmitterState(isGettingPower(world, i, j, k));
			
		}

		world.markBlockForUpdate(i, j, k);
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
	public boolean isProvidingWeakPower(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		PCnt_TileEntityRadio te = GameInfo.getTE(iblockaccess, i, j, k);
		if (te.isReceiver() && te.isActive()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isProvidingStrongPower(IBlockAccess world, int i, int j, int k, int l) {
		return isProvidingWeakPower(world, i, j, k, l);
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
		
		PCnt_TileEntityRadio te = GameInfo.getTE(world, i, j, k);
		if(!te.isActive())
			return;
		
		boolean tiny = te.isRenderMicro();

		double x = (i + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double y = (j + (tiny ? 0.2F : 0.9F)) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;
		double z = (k + 0.5F) + (random.nextFloat() - 0.5F) * 0.20000000000000001D;

		world.spawnParticle("reddust", x, y, z, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public int getMobilityFlag() {
		return 2;
	}
	
	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer){
		PC_Renderer.swapTerrain(this);

		int tx = metadata == 0 ? 69 : 70;

		float px = 0.0625F;


		ValueWriting.setBlockBounds(block, 0, 0, 0, 16 * px, 4 * px, 16 * px);
		PC_Renderer.renderInvBox(renderer, block, 0);
		ValueWriting.setBlockBounds(block, 6.5F * px, 4F * px, 6.5F * px, 9.5F * px, 7F * px, 9.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, tx);
		ValueWriting.setBlockBounds(block, 7F * px, 7F * px, 7F * px, 9F * px, 10F * px, 9F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, tx);
		ValueWriting.setBlockBounds(block, 7.5F * px, 10F * px, 7.5F * px, 8.5F * px, 13F * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, tx);
		ValueWriting.setBlockBounds(block, 7F * px, 12F * px, 7F * px, 9F * px, 14F * px, 9F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 68);
		ValueWriting.setBlockBounds(block, 0, 0, 0, 1, 1, 1);
		
		PC_Renderer.resetTerrain(true);

	}
	
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
			list.add(PC_Utils.NO_PICKUP);
	   		return list;
		}case PC_Utils.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}case PC_Utils.MSG_RENDER_INVENTORY_BLOCK:
			renderInventoryBlock((Block)obj[0], (Integer)obj[1], (Integer)obj[2], obj[3]);
			break;
		case PC_Utils.MSG_RENDER_WORLD_BLOCK:
			break;
		default:
			return null;
		}
		return true;
	}

}
