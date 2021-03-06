package powercraft.api;


import java.io.File;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;


public class PC_Utils {
	
	public static final int BLOCK_NOTIFY = 1, BLOCK_UPDATE = 2, BLOCK_ONLY_SERVERSIDE = 4;

	protected static PC_Utils instance;

	public PC_Utils() {

		instance = this;
		TickRegistry.registerTickHandler(new PC_TickHandler(), Side.SERVER);
		if(getClass()==PC_Utils.class)
			powercraft.api.registries.PC_Registry.createServerRegistry();
	}


	public static ItemStack getItemStack(Object obj) {

		if (obj instanceof ItemStack) {
			return (ItemStack) obj;
		} else if (obj instanceof Block) {
			return new ItemStack((Block) obj);
		} else if (obj instanceof Item) {
			return new ItemStack((Item) obj);
		} else {
			PC_Logger.severe("Can't make %s to ItemStack", obj.getClass());
		}
		return null;
	}


	public static int getBID(IBlockAccess blockAccess, int x, int y, int z) {

		return blockAccess.getBlockId(x, y, z);
	}


	public static int getBID(IBlockAccess blockAccess, PC_Vec3I pos) {

		return getBID(blockAccess, pos.x, pos.y, pos.z);
	}


	public static boolean setBID(World world, int x, int y, int z, int blockID, int meta, int flag) {

		return world.setBlock(x, y, z, blockID, meta, flag);
	}


	public static boolean setBID(World world, PC_Vec3I pos, int blockID, int meta, int flag) {

		return setBID(world, pos.x, pos.y, pos.z, blockID, meta, flag);
	}


	public static boolean setBID(World world, int x, int y, int z, int blockID, int meta) {

		return setBID(world, x, y, z, blockID, meta, BLOCK_NOTIFY | BLOCK_UPDATE);
	}


	public static boolean setBID(World world, PC_Vec3I pos, int blockID, int meta) {

		return setBID(world, pos.x, pos.y, pos.z, blockID, meta);
	}


	public static boolean setBID(World world, int x, int y, int z, int blockID) {

		return setBID(world, x, y, z, blockID, 0);
	}


	public static boolean setBID(World world, PC_Vec3I pos, int blockID) {

		return setBID(world, pos.x, pos.y, pos.z, blockID);
	}


	public static int getMD(IBlockAccess blockAccess, int x, int y, int z) {

		TileEntity te = getTE(blockAccess, x, y, z);
		if(te!=null){
			return te.getBlockMetadata();
		}
		return blockAccess.getBlockMetadata(x, y, z);
	}


	public static int getMD(IBlockAccess blockAccess, PC_Vec3I pos) {

		return getMD(blockAccess, pos.x, pos.y, pos.z);
	}


	public static boolean setMD(World world, int x, int y, int z, int meta, int flag) {

		return world.setBlockMetadataWithNotify(x, y, z, meta, flag);
	}


	public static boolean setMD(World world, PC_Vec3I pos, int meta, int flag) {

		return setMD(world, pos.x, pos.y, pos.z, meta, flag);
	}


	public static boolean setMD(World world, int x, int y, int z, int meta) {

		return world.setBlockMetadataWithNotify(x, y, z, meta, BLOCK_NOTIFY | BLOCK_UPDATE);
	}


	public static boolean setMD(World world, PC_Vec3I pos, int meta) {

		return setMD(world, pos.x, pos.y, pos.z, meta);
	}


	@SuppressWarnings("unchecked")
	public static <T extends TileEntity> T getTE(IBlockAccess blockAccess, int x, int y, int z) {

		return (T) blockAccess.getBlockTileEntity(x, y, z);
	}


	public static <T extends TileEntity> T getTE(IBlockAccess blockAccess, PC_Vec3I pos) {

		return getTE(blockAccess, pos.x, pos.y, pos.z);
	}


	public static void setTE(World world, int x, int y, int z, TileEntity te) {

		world.setBlockTileEntity(x, y, z, te);
	}


	public static void setTE(World world, PC_Vec3I pos, TileEntity te) {

		setTE(world, pos.x, pos.y, pos.z, te);
	}


	@SuppressWarnings("unchecked")
	public static <T extends Block> T getBlock(IBlockAccess blockAccess, int x, int y, int z) {

		return (T) Block.blocksList[getBID(blockAccess, x, y, z)];
	}


	public static <T extends Block> T getBlock(IBlockAccess blockAccess, PC_Vec3I pos) {

		return getBlock(blockAccess, pos.x, pos.y, pos.z);
	}


	public static boolean setBlock(World world, int x, int y, int z, Block block, int meta, int flag) {

		int id = 0;
		if (block != null) {
			id = block.blockID;
		}
		return setBID(world, x, y, z, id, meta, flag);
	}


	public static boolean setBlock(World world, PC_Vec3I pos, Block block, int meta, int flag) {

		return setBlock(world, pos.x, pos.y, pos.z, block, meta, flag);
	}


	public static boolean setBlock(World world, int x, int y, int z, Block block, int meta) {

		return setBlock(world, x, y, z, block, meta, BLOCK_NOTIFY | BLOCK_UPDATE);
	}


	public static boolean setBlock(World world, PC_Vec3I pos, Block block, int meta) {

		return setBlock(world, pos.x, pos.y, pos.z, block, meta);
	}


	public static boolean setBlock(World world, int x, int y, int z, Block block) {

		return setBlock(world, x, y, z, block, 0);
	}


	public static boolean setBlock(World world, PC_Vec3I pos, Block block) {

		return setBlock(world, pos.x, pos.y, pos.z, block);
	}


	public static File getPowerCraftFile(String directory, String f) {

		File file = instance.iGetPowerCraftFile();
		if (!file.exists()) file.mkdir();
		if (directory != null) {
			file = new File(file, directory);
			if (!file.exists()) file.mkdir();
		}
		return new File(file, f);
	}


	public static MinecraftServer mcs() {

		return MinecraftServer.getServer();
	}


	public static ItemStack getSmeltingResult(ItemStack item) {

		return FurnaceRecipes.smelting().getSmeltingResult(item);
	}


	public static int getRedstoneValue(World world, int x, int y, int z) {

		return world.getStrongestIndirectPower(x, y, z);
	}


	public static PC_Direction getRotation(IBlockAccess world, int x, int y, int z) {

		return PC_Direction.getOrientation((getMD(world, x, y, z) & 3) + 2);
	}

	public static PC_Direction rotate(PC_Direction side, IBlockAccess world, int x, int y, int z) {
		return side.rotateMD(getRotation(world, x, y, z).ordinal());
	}
	

	public static EnumGameType getGameTypeFor(EntityPlayer player) {

		return instance.iGetGameTypeFor(player);
	}


	public static boolean isCreativ(EntityPlayer entityPlayer) {

		return getGameTypeFor(entityPlayer).isCreative();
	}


	public static void notifyBlockOfNeighborChange(World world, int x, int y, int z, int blockID) {

		Block block = getBlock(world, x, y, z);
		if (block != null) {
			block.onNeighborBlockChange(world, x, y, z, blockID);
		}
	}


	public static void hugeUpdate(World world, int x, int y, int z) {

		int blockID = getBID(world, x, y, z);
		notifyBlockOfNeighborChange(world, x - 2, y, z, blockID);
		notifyBlockOfNeighborChange(world, x - 1, y, z, blockID);
		notifyBlockOfNeighborChange(world, x + 1, y, z, blockID);
		notifyBlockOfNeighborChange(world, x + 2, y, z, blockID);
		notifyBlockOfNeighborChange(world, x, y - 2, z, blockID);
		notifyBlockOfNeighborChange(world, x, y - 1, z, blockID);
		notifyBlockOfNeighborChange(world, x, y + 1, z, blockID);
		notifyBlockOfNeighborChange(world, x, y + 2, z, blockID);
		notifyBlockOfNeighborChange(world, x, y, z - 2, blockID);
		notifyBlockOfNeighborChange(world, x, y, z - 1, blockID);
		notifyBlockOfNeighborChange(world, x, y, z + 1, blockID);
		notifyBlockOfNeighborChange(world, x, y, z + 2, blockID);
		notifyBlockOfNeighborChange(world, x - 1, y + 1, z - 1, blockID);
		notifyBlockOfNeighborChange(world, x + 1, y + 1, z - 1, blockID);
		notifyBlockOfNeighborChange(world, x + 1, y + 1, z + 1, blockID);
		notifyBlockOfNeighborChange(world, x - 1, y + 1, z + 1, blockID);
		notifyBlockOfNeighborChange(world, x + 1, y + 1, z, blockID);
		notifyBlockOfNeighborChange(world, x - 1, y + 1, z, blockID);
		notifyBlockOfNeighborChange(world, x, y + 1, z + 1, blockID);
		notifyBlockOfNeighborChange(world, x, y + 1, z - 1, blockID);
		notifyBlockOfNeighborChange(world, x - 1, y, z - 1, blockID);
		notifyBlockOfNeighborChange(world, x + 1, y, z - 1, blockID);
		notifyBlockOfNeighborChange(world, x + 1, y, z + 1, blockID);
		notifyBlockOfNeighborChange(world, x - 1, y, z + 1, blockID);
		notifyBlockOfNeighborChange(world, x - 1, y - 1, z - 1, blockID);
		notifyBlockOfNeighborChange(world, x + 1, y - 1, z - 1, blockID);
		notifyBlockOfNeighborChange(world, x + 1, y - 1, z + 1, blockID);
		notifyBlockOfNeighborChange(world, x - 1, y - 1, z + 1, blockID);
		notifyBlockOfNeighborChange(world, x + 1, y - 1, z, blockID);
		notifyBlockOfNeighborChange(world, x - 1, y - 1, z, blockID);
		notifyBlockOfNeighborChange(world, x, y - 1, z + 1, blockID);
		notifyBlockOfNeighborChange(world, x, y - 1, z - 1, blockID);
	}


	protected File iGetPowerCraftFile() {

		return mcs().getFile("PowerCraft");
	}


	protected EnumGameType iGetGameTypeFor(EntityPlayer player) {

		return ((EntityPlayerMP) player).theItemInWorldManager.getGameType();
	}

	public static ResourceLocation getResourceLocation(PC_Module module, String file) {

		return new ResourceLocation(module.getMetadata().modId.toLowerCase(), file);
	}
	
	public static void spawnItem(World world, double x, double y, double z, ItemStack itemStack){
		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && itemStack != null) {
			float f = 0.7F;
			double d0 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5;
			double d1 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5;
			double d2 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5;
			EntityItem entityitem = new EntityItem(world, x + d0, y + d1, z + d2, itemStack);
			entityitem.delayBeforeCanPickup = 10;
			world.spawnEntityInWorld(entityitem);
		}
	}
	
	public static void spawnItems(World world, double x, double y, double z, List<ItemStack> itemStacks){
		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && itemStacks != null) {
			for (ItemStack itemStack : itemStacks) {
				if(itemStack!=null){
					float f = 0.7F;
					double d0 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5;
					double d1 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5;
					double d2 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5;
					EntityItem entityitem = new EntityItem(world, x + d0, y + d1, z + d2, itemStack);
					entityitem.delayBeforeCanPickup = 10;
					world.spawnEntityInWorld(entityitem);
				}
			}
		}
	}

	public static void spawnEntity(World world, Entity entity) {
		if (!world.isRemote){
			world.spawnEntityInWorld(entity);
		}
	}


	public static boolean isOP(String username) {
		return MinecraftServer.getServerConfigurationManager(mcs()).getOps().contains(username);
	}


	public static boolean isClient() {
		return instance.iisClient();
	}
	
	protected boolean iisClient(){
		return false;
	}

	private static final int redstone2Side[] = {-2, -2, 0, 2, 3, 1, -1};
	public static boolean canConnectRedstone(World world, int x, int y, int z, PC_Direction side) {
		Block block = getBlock(world, x, y, z);
		int s = redstone2Side[side.ordinal()];
		if(s==-2){
			return block.canProvidePower();
		}
		return block.canConnectRedstone(world, x, y, z, redstone2Side[side.ordinal()]);
	}
	
}
