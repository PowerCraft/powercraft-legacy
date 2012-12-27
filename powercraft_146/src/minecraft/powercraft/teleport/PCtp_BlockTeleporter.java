package powercraft.teleport;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.management.PC_Block;
import powercraft.management.PC_IItemInfo;
import powercraft.management.PC_Property;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecF;
import powercraft.management.PC_VecI;

public class PCtp_BlockTeleporter extends PC_Block implements PC_IItemInfo{

	public PCtp_BlockTeleporter(int id) {
		super(id, 14, Material.portal, false);
		setHardness(1.0F);
		setResistance(8.0F);
		setStepSound(Block.soundMetalFootstep);
		setBlockBounds(0.125F, 0.0F, 0.125F, 1.0F - 0.125F, 1.0F - 0.125F, 1.0F - 0.125F);
		setCreativeTab(CreativeTabs.tabTransport);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(i, j, k, (i + 1), (j + 0.03125), (k + 1));
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(i + 0.125D, j, k + 0.125D, (double) i + 1 - 0.125D, j + 1D, (double) k + 1 - 0.125D);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		ItemStack ihold = entityplayer.getCurrentEquippedItem();
		if (ihold != null) {
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().shiftedIndex != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().shiftedIndex];
				return false;
			}
		}
		if(!world.isRemote)
			PCtp_TeleporterManager.openGui(entityplayer, i, j, k);
		
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		super.onBlockPlacedBy(world, i, j, k, entityliving);
		if(!world.isRemote){
			int dimension = 0;
			PC_VecI pos = new PC_VecI(i, j, k);
			if(entityliving instanceof EntityPlayer)
				dimension = ((EntityPlayer)entityliving).dimension;
			else
				dimension = entityliving.worldObj.getWorldInfo().getDimension();
			PCtp_TeleporterData td = PCtp_TeleporterManager.getTeleporterData(dimension, pos);
			if(td==null){
				td = new PCtp_TeleporterData();
				PCtp_TeleporterManager.registerTeleporterData(dimension, pos, td);
			}//else{
			//	if(td.dimension!=worldObj.worldInfo.getDimension())
			//		PC_Utils.sendToPacketHandler(PC_Utils.mc().thePlayer, "TeleporterNetHandler", 0, xCoord, yCoord, zCoord, "", "", worldObj.worldInfo.getDimension());
			//}
			if (entityliving instanceof EntityPlayer) {
				PCtp_TeleporterManager.openGui((EntityPlayer)entityliving, td);
			}
		}
		//((PCnt_TileEntityTeleporter)world.getBlockTileEntity(i, j, k)).createData();
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCtp_TileEntityTeleporter();
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {

		world.setBlockAndMetadataWithNotify(i, j, k, 0, 0);
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);
		
		PCtp_TeleporterManager.releaseTeleporterData(world.getWorldInfo().getDimension(), new PC_VecI(i, j, k));
		
		super.breakBlock(world, i, j, k, par5, par6);
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
	public int quantityDropped(Random par1Random) {
		return 1;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		
		if (entity == null || world.isRemote) {
			return;
		}

		int dimension;
		if(entity instanceof EntityPlayer)
			dimension = ((EntityPlayer)entity).dimension;
		else
			dimension = entity.worldObj.getWorldInfo().getDimension();
		
		PCtp_TeleporterData td = PCtp_TeleporterManager.getTeleporterData(dimension, new PC_VecI(i, j, k));
		
		if(td == null)
			return;
		
		if(!(entity instanceof EntityLiving || entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow))
			return;
		
		
		if ((entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntitySlime) && !td.animals) {
			return;
		}

		if ((entity instanceof EntityMob || entity instanceof EntityGhast || entity instanceof EntityDragon || entity instanceof EntityGolem)
				&& !td.monsters) {
			return;
		}

		if ((entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow) && !td.items) {
			return;
		}

		if ((entity instanceof EntityPlayer) && !td.players) {
			return;
		}

		if ((entity instanceof EntityPlayer) && !entity.isSneaking() && td.sneakTrigger) {
			return;
		}
		
		if ((entity instanceof EntityPlayer) && td.playerChoose) {
			PCtp_TileEntityTeleporter te = GameInfo.getTE(world, i, j, k);
			if(!te.playersForTeleport.contains(entity)){
				PCtp_TeleporterManager.openTeleportGui((EntityPlayer)entity, td);
				te.playersForTeleport.add((EntityPlayer)entity);
			}
			return;
		}
		
		PCtp_TeleporterManager.teleportEntityTo(entity, td);

	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {

		PCtp_TileEntityTeleporter te = GameInfo.getTE(world, x, y, z);
		
		if (random.nextInt(60) == 0 && te.soundEnabled) {
			ValueWriting.playSound(x + 0.5D, y + 0.5D, z + 0.5D, "portal.portal", 0.1F, random.nextFloat() * 0.4F + 0.8F);
		}

		for (int i = 0; i < 8; i++) {
			// target pos
			double d = x + random.nextFloat();
			double d1 = y + random.nextFloat();
			double d2 = z + random.nextFloat();

			// initial position
			double d3 = 0.0D;
			double d4 = 0.0D;
			double d5 = 0.0D;

			d3 = -0.75F + random.nextFloat() * 1.5F;
			d4 = -0.25F + random.nextFloat() * 1F;
			d5 = -0.75F + random.nextFloat() * 1.5F;

			world.spawnParticle("portal", d, d1, d2, d3, d4, d5);
		}
	}

	public void renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer) {
		Tessellator tessellator = Tessellator.instance;

		tessellator.draw();

		tessellator.startDrawingQuads();
		
		PCtp_TileEntityTeleporter td = GameInfo.getTE(world, x, y, z);
		
		ValueWriting.setBlockBounds(Block.blockGold, 0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
		PC_Renderer.renderStandardBlock(renderer, Block.blockGold, x, y, z);
		float m = 0.0625F * 6F;
		float n = 0.0625F * 10F;
		if(td!=null){
			if (td.direction == PCtp_TeleporterData.N) {
				ValueWriting.setBlockBounds(Block.blockGold, m, 0, 0.0625F, n, 0.125F, 0.0625F * 2);
			} else if (td.direction == PCtp_TeleporterData.S) {
				ValueWriting.setBlockBounds(Block.blockGold, m, 0, 1 - 0.0625F * 2, n, 0.125F, 1 - 0.0625F);
			} else if (td.direction == PCtp_TeleporterData.E) {
				ValueWriting.setBlockBounds(Block.blockGold, 1 - 0.0625F * 2, 0, m, 1 - 0.0625F, 0.125F, n);
			} else if (td.direction == PCtp_TeleporterData.W) {
				ValueWriting.setBlockBounds(Block.blockGold, 0.0625F, 0, m, 0.0625F * 2, 0.125F, n);
			}
		}
		PC_Renderer.renderStandardBlock(renderer, Block.blockGold, x, y, z);

		ValueWriting.setBlockBounds(Block.blockSteel, 0.4375F, 0.125F, 0.4375F, 1F - 0.4375F, 0.25F, 1F - 0.4375F);
		PC_Renderer.renderStandardBlock(renderer, Block.blockSteel, x, y, z);

		float centr = 0.0625F * 4;
		ValueWriting.setBlockBounds(Block.blockSteel, 0.5F - centr, 0.5F - centr, 0.5F - centr, 0.5F + centr, 0.5F + centr, 0.5F + centr);
		PC_Renderer.renderStandardBlock(renderer, Block.blockSteel, x, y, z);

		ValueWriting.setBlockBounds(Block.blockSteel, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		ValueWriting.setBlockBounds(Block.blockGold, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		ValueWriting.setBlockBounds(block, 0.1875F, 0.1875F, 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F);
		PC_Renderer.renderStandardBlock(renderer, block, x, y, z);
		ValueWriting.setBlockBounds(block, 0.125F, 0.0F, 0.125F, 1.0F - 0.125F, 1.0F - 0.125F, 1.0F - 0.125F);
		
		tessellator.draw();
		
		tessellator.startDrawingQuads();
	}
	
	public void renderInventoryBlock(Block block, int metadata, int modelID, Object renderer) {
		ValueWriting.setBlockBounds(Block.blockSteel, 0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
		PC_Renderer.renderInvBox(renderer, Block.blockSteel, 0);

		ValueWriting.setBlockBounds(Block.blockSteel, 0.4375F, 0.125F, 0.4375F, 1F - 0.4375F, 0.25F, 1F - 0.4375F);
		PC_Renderer.renderInvBox(renderer, Block.blockSteel, 0);

		float centr = 0.0625F * 4;
		ValueWriting.setBlockBounds(Block.blockSteel, 0.5F - centr, 0.5F - centr, 0.5F - centr, 0.5F + centr, 0.5F + centr, 0.5F + centr);
		PC_Renderer.renderInvBox(renderer, Block.blockSteel, 0);

		ValueWriting.setBlockBounds(Block.blockSteel, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		ValueWriting.setBlockBounds(block, 0.1875F, 0.0F, 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F);
		PC_Renderer.renderInvBox(renderer, block, 0);
		ValueWriting.setBlockBounds(block, 0.125F, 0.0F, 0.125F, 1.0F - 0.125F, 1.0F - 0.125F, 1.0F - 0.125F);
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			return "Teleporter";
		case PC_Utils.MSG_LOAD_FROM_CONFIG:
			setLightValue(((PC_Property)obj[0]).getInt("brightness", 4) * 0.0625F);
			break;
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
			renderWorldBlock(world, pos.x, pos.y, pos.z, (Block)obj[0], (Integer)obj[1], obj[2]);
			break;
			
		default:
			return null;
		}
		return true;
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

}
