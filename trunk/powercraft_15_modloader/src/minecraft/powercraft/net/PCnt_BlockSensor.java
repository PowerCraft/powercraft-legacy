package powercraft.net;

import java.util.List;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import powercraft.api.annotation.PC_BlockInfo;
import powercraft.api.block.PC_Block;
import powercraft.api.registry.PC_GresRegistry;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;


/**
 * Entity Proximity Sensor
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
@PC_BlockInfo(name="Sensor", itemBlock=PCnt_ItemBlockSensor.class, tileEntity=PCnt_TileEntitySensor.class)
public class PCnt_BlockSensor extends PC_Block {

	/**
	 * proximity sensor
	 * 
	 * @param id block ID
	 */
	public PCnt_BlockSensor(int id) {
		super(id, Material.ground, "radio_red", "sensor_item", "sensor_mob", "sensor_player");
		setHardness(0.35F);
		setResistance(30.0F);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		setCreativeTab(CreativeTabs.tabRedstone);
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
	public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity) {
		setBlockBounds(0F, 0F, 0F, 1F, 0.255F, 1F);
		super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		setBlockBounds(0.375F, 0.2F, 0.375F, 1F - 0.375F, 0.7F, 1F - 0.375F);
		super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		setBlockBounds(0.3125F, 0.5F, 0.3125F, 1F - 0.3125F, 0.875F, 1F - 0.3125F);
		super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox(i, j, k, (double) i + 1, (double) j + 1, (double) k + 1);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9) {

		PC_GresRegistry.openGres("Sensor", player, PC_Utils.<PC_TileEntity>getTE(world, i, j, k));
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
		PCnt_TileEntitySensor ent = PC_Utils.getTE(world, pos);
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
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (!PC_Utils.<PCnt_TileEntitySensor>getTE(world, i, j, k).isActive()) {
			return;
		}

		double ii = i + 0.2D + random.nextDouble() * 0.6;
		double jj = j + 0.5D + random.nextDouble() * 0.4;
		double kk = k + 0.2D + random.nextDouble() * 0.6;

		world.spawnParticle("reddust", ii, jj, kk, 0, 0, 0);
	}

	@Override
	public boolean renderInventoryBlock(int metadata, Object renderer){

		float px = 0.0625F;

		Icon icon = metadata == 0 ? sideIcons[1] : metadata == 1 ? sideIcons[2] : sideIcons[3];

		setBlockBounds(0, 0, 0, 16 * px, 4 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, this, icon);
		setBlockBounds(6 * px, 4 * px, 6 * px, 10 * px, 9 * px, 10 * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, this, icon);
		setBlockBounds(5 * px, 8 * px, 5 * px, 11 * px, 14 * px, 11 * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, this, sideIcons[0]);
		setBlockBounds(0, 0, 0, 1, 1, 1);
		return true;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Object renderer) {
		return true;
	}

	@Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        int type = PC_Utils.<PCnt_TileEntitySensor>getTE(world, x, y, z).getGroup();
        boolean remove = super.removeBlockByPlayer(world, player, x, y, z);

        if (remove && !PC_Utils.isCreative(player))
        {
            dropBlockAsItem_do(world, x, y, z, new ItemStack(PCnt_App.sensor, 1, type));
        }

        return remove;
    }

}
