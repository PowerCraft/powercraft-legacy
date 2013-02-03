package powercraft.net;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;


/**
 * Entity Proximity Sensor
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCnt_BlockSensor extends PC_Block {

	/**
	 * proximity sensor
	 * 
	 * @param id block ID
	 */
	public PCnt_BlockSensor(int id) {
		super(id, 6, Material.ground);
		setHardness(0.35F);
		setResistance(30.0F);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCnt_TileEntitySensor();
	}

	@Override
	public int damageDropped(int i) {
		return 0;
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return 0;
	}

	@Override
	public void addCollidingBlockToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
		setBlockBounds(0F, 0F, 0F, 1F, 0.255F, 1F);
		super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		setBlockBounds(0.375F, 0.2F, 0.375F, 1F - 0.375F, 0.7F, 1F - 0.375F);
		super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		setBlockBounds(0.3125F, 0.5F, 0.3125F, 1F - 0.3125F, 0.875F, 1F - 0.3125F);
		super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(i, j, k, (double) i + 1, (double) j + 1, (double) k + 1);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9) {
		ItemStack ihold = player.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().itemID != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().itemID];
				
				return false;
			}
		}

		Gres.openGres("Sensor", player, GameInfo.<PC_TileEntity>getTE(world, i, j, k));
		//PC_Utils.openGres(player, new PClo_GuiSensor((PClo_TileEntitySensor) new PC_CoordI(i, j, k).getTileEntity(world)));
		return true;
	}

	@Override
	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {
		printRange(world, new PC_VecI(i, j, k));
	}

	/**
	 * SHow current range (distance) using chat.
	 * 
	 * @param world the world
	 * @param pos device position.
	 */
	public static void printRange(World world, PC_VecI pos) {
		PCnt_TileEntitySensor ent = GameInfo.getTE(world, pos);
		ent.printRange();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		if (!world.getBlockMaterial(i, j - 1, k).isSolid()) {
			return false;
		} else {
			return super.canPlaceBlockAt(world, i, j, k);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public boolean isProvidingWeakPower(IBlockAccess world, int i, int j, int k, int l) {
		return GameInfo.<PCnt_TileEntitySensor>getTE(world, i, j, k).isActive();
	}

	@Override
	public boolean isProvidingStrongPower(IBlockAccess world, int i, int j, int k, int l) {
		return isProvidingWeakPower(world, i, j, k, l);
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (!GameInfo.<PCnt_TileEntitySensor>getTE(world, i, j, k).isActive()) {
			return;
		}

		double ii = i + 0.2D + random.nextDouble() * 0.6;
		double jj = j + 0.5D + random.nextDouble() * 0.4;
		double kk = k + 0.2D + random.nextDouble() * 0.6;

		world.spawnParticle("reddust", ii, jj, kk, 0, 0, 0);
	}


	@Override
	public int getBlockTexture(IBlockAccess world, int i, int j, int k, int l) {
		int meta = world.getBlockMetadata(i, j, k);

		// this is used only for particles (the lower side makes particles)
		switch (meta) {
			case 0:
				return 4;
			case 1:
				return 6;
			case 2:
				return 37;
		}
		return 48;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int s, int m) {
		switch (m) {
			case 0:
				return 4;
			case 1:
				return 6;
			case 2:
				return 37;
		}
		return 48;
	}

	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer){
		PC_Renderer.swapTerrain(getTextureFile());

		float px = 0.0625F;

		int tx = metadata == 0 ? 65 : metadata == 1 ? 6 : 64;

		ValueWriting.setBlockBounds(block, 0, 0, 0, 16 * px, 4 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, tx);
		ValueWriting.setBlockBounds(block, 6 * px, 4 * px, 6 * px, 10 * px, 9 * px, 10 * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, tx);
		ValueWriting.setBlockBounds(block, 5 * px, 8 * px, 5 * px, 11 * px, 14 * px, 11 * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 68);
		ValueWriting.setBlockBounds(block, 0, 0, 0, 1, 1, 1);
		
		PC_Renderer.resetTerrain(true);
	}
	
	@Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        int type = GameInfo.<PCnt_TileEntitySensor>getTE(world, x, y, z).getGroup();
        boolean remove = super.removeBlockByPlayer(world, player, x, y, z);

        if (remove && !GameInfo.isCreative(player))
        {
            dropBlockAsItem_do(world, x, y, z, new ItemStack(PCnt_App.sensor, 1, type));
        }

        return remove;
    }
	
	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
			list.add(PC_Utils.NO_HARVEST);
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
