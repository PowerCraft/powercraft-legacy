package powercraft.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_IItemInfo;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.inventory.PC_ISpecialInventoryTextures;
import powercraft.management.PC_VecI;

public class PCma_BlockBlockBuilder extends PC_Block implements PC_ISpecialInventoryTextures, PC_IItemInfo{
	private static final int TXDOWN = 109, TXTOP = 156, TXSIDE = 140, TXFRONT = 108, TXBACK = 124;
	
	public PCma_BlockBlockBuilder(int id) {
		super(id, TXDOWN, Material.ground);
		setHardness(0.7F);
		setResistance(10.0F);
		setStepSound(Block.soundStoneFootstep);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int s, int m) {
		if (s == 1) {
			return TXTOP;
		}
		if (s == 0) {
			return TXDOWN;
		} else {
			if (m == s) {
				return TXFRONT;
			}
			if ((m == 2 && s == 3) || (m == 3 && s == 2) || (m == 4 && s == 5) || (m == 5 && s == 4)) {
				return TXBACK;
			}
			return TXSIDE;
		}
	}

	@Override
	public int getInvTexture(int i, int m) {
		if (i == 1) {
			return TXTOP;
		}
		if (i == 0) {
			return TXDOWN;
		}
		if (i == 3) {
			return TXFRONT;
		} else if (i == 4) {
			return TXBACK;
		} else {
			return TXSIDE;
		}
	}
	
	@Override
	public int tickRate() {
		return 1;
	}
	
	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		setDispenserDefaultDirection(world, i, j, k);
	}

	private void setDispenserDefaultDirection(World world, int i, int j, int k) {
		if (!world.isRemote) {
			int l = world.getBlockId(i, j, k - 1);
			int i1 = world.getBlockId(i, j, k + 1);
			int j1 = world.getBlockId(i - 1, j, k);
			int k1 = world.getBlockId(i + 1, j, k);
			byte byte0 = 3;
			if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1]) {
				byte0 = 3;
			}
			if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l]) {
				byte0 = 2;
			}
			if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1]) {
				byte0 = 5;
			}
			if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1]) {
				byte0 = 4;
			}
			world.setBlockMetadataWithNotify(i, j, k, byte0);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		ItemStack ihold = entityplayer.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().itemID != blockID) {

				Block bhold = Block.blocksList[ihold.getItem().itemID];
				return false;

			}
		}

		if (world.isRemote) {
			return true;
		}

		Gres.openGres("BlockBuilder", entityplayer, GameInfo.<PC_TileEntity>getTE(world, i, j, k));

		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (l > 0 && Block.blocksList[l].canProvidePower()) {
			boolean flag = isIndirectlyPowered(world, i, j, k);
			if (flag) {
				world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
			}
		}
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		if (!world.isRemote && isIndirectlyPowered(world, i, j, k)) {
			PCma_TileEntityBlockBuilder tileentity = (PCma_TileEntityBlockBuilder) world.getBlockTileEntity(i, j, k);
			if (tileentity != null) {
				tileentity.useItem();
			}
		}
	}

	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCma_TileEntityBlockBuilder();
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = MathHelper.floor_double(((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

		if (entityliving instanceof EntityPlayer && GameInfo.isPlacingReversed((EntityPlayer)entityliving)) {
			l = ValueWriting.reverseSide(l);
		}

		if (l == 0) {
			l = 2;
		} else if (l == 1) {
			l = 5;
		} else if (l == 2) {
			l = 3;
		} else if (l == 3) {
			l = 4;
		}

		world.setBlockMetadataWithNotify(i, j, k, l);
	}

	private boolean isIndirectlyPowered(World world, int i, int j, int k) {
		if (world.isBlockGettingPowered(i, j, k)) {
			return true;
		}

		if (world.isBlockIndirectlyGettingPowered(i, j, k)) {
			return true;
		}

		if (world.isBlockGettingPowered(i, j - 1, k)) {
			return true;
		}

		if (world.isBlockIndirectlyGettingPowered(i, j - 1, k)) {
			return true;
		}
		return false;
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch (msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Block Builder";
		case PC_Utils.MSG_ITEM_FLAGS:{
			List<String> list = (List<String>)obj[1];
			list.add(PC_Utils.NO_BUILD);
			return list;
		}case PC_Utils.MSG_BLOCK_FLAGS:{
			List<String> list = (List<String>)obj[0];
	   		list.add(PC_Utils.NO_HARVEST);
	   		list.add(PC_Utils.NO_PICKUP);
	   		list.add(PC_Utils.HARVEST_STOP);
	   		return list;
		}
		}
		return null;
	}
	
}
