package powercraft.teleport;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.Tessellator;
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
import powercraft.management.PC_Renderer;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;

public class PCtp_BlockTeleporter extends PC_Block {

	protected PCtp_BlockTeleporter(int id) {
		super(id, 14, Material.portal);
		setHardness(1.0F);
		setResistance(8.0F);
		setStepSound(Block.soundMetalFootstep);
		setBlockBounds(0.125F, 0.0F, 0.125F, 1.0F - 0.125F, 1.0F - 0.125F, 1.0F - 0.125F);
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
		Gres.openGres("Teleporter", entityplayer, i, j, k, entityplayer.dimension);
		
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		super.onBlockPlacedBy(world, i, j, k, entityliving);
		if(!world.isRemote){
			System.out.println("onBlockPlacedBy");
			PCtp_TeleporterData td;
			if(entityliving instanceof EntityPlayer)
				td = PCtp_TeleporterManager.getTeleporterDataAt(((EntityPlayer)entityliving).dimension, i, j, k);
			else
				td = PCtp_TeleporterManager.getTeleporterDataAt(entityliving.worldObj.getWorldInfo().getDimension(), i, j, k);
			if(td==null){
				td = new PCtp_TeleporterData();
				td.pos.setTo(i, j, k);
				td.setName("");
				td.defaultTarget="";
				if(entityliving instanceof EntityPlayer)
					td.dimension = ((EntityPlayer)entityliving).dimension;
				else
					td.dimension = entityliving.worldObj.getWorldInfo().getDimension();
				PCtp_TileEntityTeleporter tet = (PCtp_TileEntityTeleporter)world.getBlockTileEntity(i, j, k);
				tet.dimension = td.dimension;
				System.out.println("td.dimension:"+td.dimension);
				PCtp_TeleporterManager.add(td);
				PC_Utils.sendToPacketHandler(null, "TeleporterNetHandler", i, j, k, "", "", td.dimension);
			}//else{
			//	if(td.dimension!=worldObj.worldInfo.getDimension())
			//		PC_Utils.sendToPacketHandler(PC_Utils.mc().thePlayer, "TeleporterNetHandler", 0, xCoord, yCoord, zCoord, "", "", worldObj.worldInfo.getDimension());
			//}
			if (entityliving instanceof EntityPlayer) {
				PC_Utils.openGres((EntityPlayer)entityliving, PCnt_GuiTeleporter.class, i, j, k, td.dimension);
			}
		}
		//((PCnt_TileEntityTeleporter)world.getBlockTileEntity(i, j, k)).createData();
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {

		world.setBlockAndMetadataWithNotify(i, j, k, 0, 0);
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);
		
		super.breakBlock(world, i, j, k, par5, par6);
	}
	
	@Override
	public TileEntity newTileEntity(World world, int metadata) {
		return new PCtp_TileEntityTeleporter();
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
		
		PCtp_TeleporterData td = null;
		if(entity instanceof EntityPlayer)
			td = PCtp_TeleporterManager.getTeleporterDataAt(((EntityPlayer)entity).dimension, i, j, k);
		else
			td = PCtp_TeleporterManager.getTeleporterDataAt(world.worldInfo.getDimension(), i, j, k);
		
		if(td == null)
			return;
		
		if (entity == null) {
			return;
		}
		
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
		
		System.out.println("onEntityCollidedWithBlock");
		PCtp_TeleporterManager.teleportEntityTo(entity, td.defaultTarget);

	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {

		if (random.nextInt(60) == 0) {
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
		
		PCtp_TeleporterData td = null;
		if(iblockaccess instanceof World)
			td = PCtp_TeleporterManager.getTeleporterDataAt(((World)iblockaccess).worldInfo.getDimension(), i, j, k);
			Block.blockGold.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
			renderblocks.renderStandardBlock(Block.blockGold, i, j, k);
			float m = 0.0625F * 6F;
			float n = 0.0625F * 10F;
			if(td!=null){
				if (td.direction.equals("N")) {
					Block.blockGold.setBlockBounds(m, 0, 0.0625F, n, 0.125F, 0.0625F * 2);
				} else if (td.direction.equals("S")) {
					Block.blockGold.setBlockBounds(m, 0, 1 - 0.0625F * 2, n, 0.125F, 1 - 0.0625F);
				} else if (td.direction.equals("E")) {
					Block.blockGold.setBlockBounds(1 - 0.0625F * 2, 0, m, 1 - 0.0625F, 0.125F, n);
				} else if (td.direction.equals("W")) {
					Block.blockGold.setBlockBounds(0.0625F, 0, m, 0.0625F * 2, 0.125F, n);
				}
			}
			renderblocks.renderStandardBlock(Block.blockGold, i, j, k);
		/*} else {
			Block.blockSteel.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
			renderblocks.renderStandardBlock(Block.blockSteel, i, j, k);
		}*/

		Block.blockSteel.setBlockBounds(0.4375F, 0.125F, 0.4375F, 1F - 0.4375F, 0.25F, 1F - 0.4375F);
		renderblocks.renderStandardBlock(Block.blockSteel, i, j, k);

		float centr = 0.0625F * 4;
		Block.blockSteel.setBlockBounds(0.5F - centr, 0.5F - centr, 0.5F - centr, 0.5F + centr, 0.5F + centr, 0.5F + centr);
		renderblocks.renderStandardBlock(Block.blockSteel, i, j, k);

		Block.blockSteel.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		Block.blockGold.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		if(true) {
			block.setBlockBounds(0.1875F, 0.1875F, 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F);
			renderblocks.renderStandardBlock(block, i, j, k);
			block.setBlockBounds(0.125F, 0.0F, 0.125F, 1.0F - 0.125F, 1.0F - 0.125F, 1.0F - 0.125F);
		}

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
	public Set<String> getBlockFlags(World world, PC_CoordI pos) {

		Set<String> set = new HashSet<String>();

		set.add("NO_HARVEST");
		set.add("NO_PICKUP");
		set.add("TRANSLUCENT");
		set.add("BELT");
		set.add("TELEPORTER");

		return set;
	}

	@Override
	public Set<String> getItemFlags(ItemStack stack) {
		Set<String> set = new HashSet<String>();
		set.add("NO_BUILD");
		set.add("TELEPORTER");
		return set;
	}

	@Override
	public Object msg(IBlockAccess world, PC_VecI pos, int msg, Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
