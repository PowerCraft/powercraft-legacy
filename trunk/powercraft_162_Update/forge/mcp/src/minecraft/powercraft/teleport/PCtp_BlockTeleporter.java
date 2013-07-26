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
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_BeamTracer.BeamSettings;
import powercraft.api.PC_BeamTracer.BeamHitResult;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.item.PC_IItemInfo;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.registry.PC_SoundRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Direction;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;
import powercraft.launcher.PC_Property;

@PC_BlockInfo(name="Teleporter", tileEntity=PCtp_TileEntityTeleporter.class)
public class PCtp_BlockTeleporter extends PC_Block implements PC_IItemInfo{

	public PCtp_BlockTeleporter(int id) {
		super(id, Material.portal);
		setHardness(1.0F);
		setResistance(8.0F);
		setStepSound(Block.soundMetalFootstep);
		setBlockBounds(0.125F, 0.0F, 0.125F, 1.0F - 0.125F, 1.0F - 0.125F, 1.0F - 0.125F);
		setCreativeTab(CreativeTabs.tabTransport);
	}
	
	@Override
	public Icon getIcon(PC_Direction par1, int par2) {
		return Block.portal.getIcon(par1.getMCDir(), par2);
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
			if (ihold.getItem() instanceof ItemBlock && ihold.getItem().itemID != blockID) {
				Block bhold = Block.blocksList[ihold.getItem().itemID];
				return false;
			}
		}
		if(!world.isRemote)
			PCtp_TeleporterManager.openGui(entityplayer, i, j, k);
		
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemStack) {
		super.onBlockPlacedBy(world, i, j, k, entityliving, itemStack);
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
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {

		PC_Utils.setBID(world, i, j, k, 0, 0);
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
			PCtp_TileEntityTeleporter te = PC_Utils.getTE(world, i, j, k);
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

		PCtp_TileEntityTeleporter te = PC_Utils.getTE(world, x, y, z);
		
		if (random.nextInt(60) == 0 && te.soundEnabled) {
			PC_SoundRegistry.playSound(x + 0.5D, y + 0.5D, z + 0.5D, "portal.portal", 0.1F, random.nextFloat() * 0.4F + 0.8F);
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

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
		Tessellator tessellator = Tessellator.instance;

		tessellator.draw();

		tessellator.startDrawingQuads();
		
		PCtp_TileEntityTeleporter td = PC_Utils.getTE(world, x, y, z);
		
		PC_Utils.setBlockBounds(Block.blockIron, 0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
		PC_Renderer.renderStandardBlock(renderer, Block.blockIron, x, y, z);
		float m = 0.0625F * 6F;
		float n = 0.0625F * 10F;
		if(td!=null){
			if (td.direction == PCtp_TeleporterData.N) {
				PC_Utils.setBlockBounds(Block.blockIron, m, 0, 0.0625F, n, 0.125F, 0.0625F * 2);
			} else if (td.direction == PCtp_TeleporterData.S) {
				PC_Utils.setBlockBounds(Block.blockIron, m, 0, 1 - 0.0625F * 2, n, 0.125F, 1 - 0.0625F);
			} else if (td.direction == PCtp_TeleporterData.E) {
				PC_Utils.setBlockBounds(Block.blockIron, 1 - 0.0625F * 2, 0, m, 1 - 0.0625F, 0.125F, n);
			} else if (td.direction == PCtp_TeleporterData.W) {
				PC_Utils.setBlockBounds(Block.blockIron, 0.0625F, 0, m, 0.0625F * 2, 0.125F, n);
			}
		}
		PC_Renderer.renderStandardBlock(renderer, Block.blockIron, x, y, z);

		PC_Utils.setBlockBounds(Block.blockIron, 0.4375F, 0.125F, 0.4375F, 1F - 0.4375F, 0.25F, 1F - 0.4375F);
		PC_Renderer.renderStandardBlock(renderer, Block.blockIron, x, y, z);

		float centr = 0.0625F * 4;
		PC_Utils.setBlockBounds(Block.blockIron, 0.5F - centr, 0.5F - centr, 0.5F - centr, 0.5F + centr, 0.5F + centr, 0.5F + centr);
		PC_Renderer.renderStandardBlock(renderer, Block.blockIron, x, y, z);

		PC_Utils.setBlockBounds(Block.blockIron, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		setBlockBounds(0.1875F, 0.1875F, 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F);
		PC_Renderer.renderStandardBlock(renderer, this, x, y, z);
		setBlockBounds(0.125F, 0.0F, 0.125F, 1.0F - 0.125F, 1.0F - 0.125F, 1.0F - 0.125F);
		
		tessellator.draw();
		
		tessellator.startDrawingQuads();
		return true;
	}
	
	public boolean renderInventoryBlock(int modelID, Object renderer) {
		PC_Utils.setBlockBounds(Block.blockIron, 0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
		PC_Renderer.renderInvBox(renderer, Block.blockIron, 0);

		PC_Utils.setBlockBounds(Block.blockIron, 0.4375F, 0.125F, 0.4375F, 1F - 0.4375F, 0.25F, 1F - 0.4375F);
		PC_Renderer.renderInvBox(renderer, Block.blockIron, 0);

		float centr = 0.0625F * 4;
		PC_Utils.setBlockBounds(Block.blockIron, 0.5F - centr, 0.5F - centr, 0.5F - centr, 0.5F + centr, 0.5F + centr, 0.5F + centr);
		PC_Renderer.renderInvBox(renderer, Block.blockIron, 0);

		PC_Utils.setBlockBounds(Block.blockIron, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		setBlockBounds(0.1875F, 0.0F, 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F);
		PC_Renderer.renderInvBox(renderer, this, 0);
		setBlockBounds(0.125F, 0.0F, 0.125F, 1.0F - 0.125F, 1.0F - 0.125F, 1.0F - 0.125F);
		return true;
	}
	
	public BeamHitResult onHitByBeamTracer(IBlockAccess world, BeamSettings bs) {
		PCtp_TileEntityTeleporter teTP = PC_Utils.getTE(world, bs.getPos());
		if(teTP.defaultTarget!=null){
			bs.getBeamTracer().forkBeam(new BeamSettings(bs.getBeamTracer(), teTP.defaultTarget, PCtp_TeleporterManager.coords[teTP.defaultTargetDirection], bs.getColor(), bs.getStrength(), bs.getLength()-1));
			return BeamHitResult.STOP;
		}
		return BeamHitResult.CONTINUE;
	}
	
	@Override
	public void initConfig(PC_Property config) {
		super.initConfig(config);
		setLightValue(config.getInt("brightness", 4) * 0.0625F);
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this));
		return arrayList;
	}

}
