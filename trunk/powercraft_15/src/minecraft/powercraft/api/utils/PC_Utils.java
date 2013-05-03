package powercraft.api.utils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_APIModule;
import powercraft.api.annotation.PC_Shining;
import powercraft.api.block.PC_Block;
import powercraft.api.interfaces.PC_INBT;
import powercraft.api.network.PC_PacketHandler;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_RegistryServer;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.launcher.PC_LauncherUtils;
import powercraft.launcher.loader.PC_ModLoader;

public class PC_Utils {
	
	protected static PC_Utils instance;
	
	private static Random rand = new Random();
	
	public static final int BLOCK_NOTIFY = 1, BLOCK_UPDATE = 2, BLOCK_ONLY_SERVERSIDE = 4;
	
	protected PC_Utils() {
		
	}
	
	public static boolean create() {
		if (instance == null) {
			instance = new PC_Utils();
			PC_RegistryServer.create();
			return true;
		}
		
		return false;
	}
	
	protected boolean iIsClient() {
		return false;
	}
	
	protected EnumGameType iGetGameTypeFor(EntityPlayer player) {
		return ((EntityPlayerMP) player).theItemInWorldManager.getGameType();
	}
	
	protected World iGetWorldForDimension(int dimension) {
		return mcs().worldServerForDimension(dimension);
	}
	
	protected boolean iIsEntityFX(Entity entity) {
		return false;
	}
	
	protected EntityPlayer iGetPlayer() {
		return null;
	}
	
	protected void iSpawnParticle(String name, Object[] o) {
		
	}
	
	protected File iGetMCDirectory() {
		return mcs().getFile("");
	}
	
	protected void iChatMsg(String tr) {
		
	}
	
	public static int getBID(IBlockAccess blockAccess, int x, int y, int z) {
		return blockAccess.getBlockId(x, y, z);
	}
	
	public static int getBID(IBlockAccess blockAccess, PC_VecI pos) {
		return getBID(blockAccess, pos.x, pos.y, pos.z);
	}
	
	public static boolean setBID(World world, int x, int y, int z, int blockID, int meta, int flag) {
		return world.setBlock(x, y, z, blockID, meta, flag);
	}
	
	public static boolean setBID(World world, PC_VecI pos, int blockID, int meta, int flag) {
		return setBID(world, pos.x, pos.y, pos.z, blockID, meta, flag);
	}
	
	public static boolean setBID(World world, int x, int y, int z, int blockID, int meta) {
		return setBID(world, x, y, z, blockID, meta, BLOCK_NOTIFY | BLOCK_UPDATE);
	}
	
	public static boolean setBID(World world, PC_VecI pos, int blockID, int meta) {
		return setBID(world, pos.x, pos.y, pos.z, blockID, meta);
	}
	
	public static boolean setBID(World world, int x, int y, int z, int blockID) {
		return setBID(world, x, y, z, blockID, 0);
	}
	
	public static boolean setBID(World world, PC_VecI pos, int blockID) {
		return setBID(world, pos.x, pos.y, pos.z, blockID);
	}
	
	public static int getMD(IBlockAccess blockAccess, int x, int y, int z) {
		return blockAccess.getBlockMetadata(x, y, z);
	}
	
	public static int getMD(IBlockAccess blockAccess, PC_VecI pos) {
		return getMD(blockAccess, pos.x, pos.y, pos.z);
	}
	
	public static boolean setMD(World world, int x, int y, int z, int meta, int flag) {
		return world.setBlockMetadataWithNotify(x, y, z, meta, flag);
	}
	
	public static boolean setMD(World world, PC_VecI pos, int meta, int flag) {
		return setMD(world, pos.x, pos.y, pos.z, meta, flag);
	}
	
	public static boolean setMD(World world, int x, int y, int z, int meta) {
		return world.setBlockMetadataWithNotify(x, y, z, meta, BLOCK_NOTIFY | BLOCK_UPDATE);
	}
	
	public static boolean setMD(World world, PC_VecI pos, int meta) {
		return setMD(world, pos.x, pos.y, pos.z, meta);
	}
	
	public static <T extends TileEntity> T getTE(IBlockAccess blockAccess, int x, int y, int z) {
		return (T) blockAccess.getBlockTileEntity(x, y, z);
	}
	
	public static <T extends TileEntity> T getTE(IBlockAccess blockAccess, PC_VecI pos) {
		return getTE(blockAccess, pos.x, pos.y, pos.z);
	}
	
	public static void setTE(World world, int x, int y, int z, TileEntity te) {
		world.setBlockTileEntity(x, y, z, te);
	}
	
	public static void setTE(World world, PC_VecI pos, TileEntity te) {
		setTE(world, pos.x, pos.y, pos.z, te);
	}
	
	public static <T extends Block> T getBlock(IBlockAccess blockAccess, int x, int y, int z) {
		return (T) Block.blocksList[getBID(blockAccess, x, y, z)];
	}
	
	public static <T extends Block> T getBlock(IBlockAccess blockAccess, PC_VecI pos) {
		return getBlock(blockAccess, pos.x, pos.y, pos.z);
	}
	
	public static boolean setBlock(World world, int x, int y, int z, Block block, int meta, int flag) {
		int id = 0;
		if (block != null) {
			id = block.blockID;
		}
		return setBID(world, x, y, z, id, meta, flag);
	}
	
	public static boolean setBlock(World world, PC_VecI pos, Block block, int meta, int flag) {
		return setBlock(world, pos.x, pos.y, pos.z, block, meta, flag);
	}
	
	public static boolean setBlock(World world, int x, int y, int z, Block block, int meta) {
		return setBlock(world, x, y, z, block, meta, BLOCK_NOTIFY | BLOCK_UPDATE);
	}
	
	public static boolean setBlock(World world, PC_VecI pos, Block block, int meta) {
		return setBlock(world, pos.x, pos.y, pos.z, block, meta);
	}
	
	public static boolean setBlock(World world, int x, int y, int z, Block block) {
		return setBlock(world, x, y, z, block, 0);
	}
	
	public static boolean setBlock(World world, PC_VecI pos, Block block) {
		return setBlock(world, pos.x, pos.y, pos.z, block);
	}
	
	public static boolean isBlockReplaceable(World world, int x, int y, int z) {
		Block block = getBlock(world, x, y, z);
		if (block == null)
			return true;
		int blockID = block.blockID;
		if (blockID == Block.vine.blockID || blockID == Block.tallGrass.blockID || blockID == Block.deadBush.blockID) {
			return true;
		}
		return block.isBlockReplaceable(world, x, y, z);
	}
	
	public static boolean isBlockReplaceable(World world, PC_VecI pos) {
		return isBlockReplaceable(world, pos.x, pos.y, pos.z);
	}
	
	public static int getBlockRedstonePowereValue(World world, int x, int y, int z){
		return world.getStrongestIndirectPower(x, y, z);
	}
	
	public static int getBlockRedstonePowereValue(World world, PC_VecI pos){
		return getBlockRedstonePowereValue(world, pos.x, pos.y, pos.z);
	}
	
	public static void markBlockForUpdate(World world, int x, int y, int z) {
		world.markBlockForUpdate(x, y, z);
	}
	
	public static void markBlockForUpdate(World world, PC_VecI pos) {
		markBlockForUpdate(world, pos.x, pos.y, pos.z);
	}
	
	public static void setBlockBounds(Block block, double x, double y, double z, double width, double height, double depht) {
		block.setBlockBounds((float)x, (float)y, (float)z, (float)width, (float)height, (float)depht);
	}
	
	public static void setBlockState(World world, int x, int y, int z, boolean on) {
		Block b = getBlock(world, x, y, z);
		
		if (b instanceof PC_Block) {
			int meta = getMD(world, x, y, z);
			PC_TileEntity te = getTE(world, x, y, z);
			Class c = b.getClass();
			
			if (c.isAnnotationPresent(PC_Shining.class)) {
				Block bon = (Block) PC_ReflectHelper.getFieldsWithAnnotation(c, c, PC_Shining.ON.class).get(0);
				Block boff = (Block) PC_ReflectHelper.getFieldsWithAnnotation(c, c, PC_Shining.OFF.class).get(0);
				
				if ((b == bon && !on) || (b == boff && on)) {
					if (on) {
						b = bon;
					} else {
						b = boff;
					}
					
					if(!world.isRemote){
						PC_GlobalVariables.tileEntity.add(0, te);
					}
					
					setBID(world, x, y, z, b.blockID, meta);
					
					if(!world.isRemote){
						PC_GlobalVariables.tileEntity.remove(0);
					}
					
					if (te != null) {
						PC_PacketHandler.sendTileEntity(te);
					}
					
				}
			}
		}
	}
	
	public static void setBlockState(World world, PC_VecI pos, boolean on) {
		setBlockState(world, pos.x, pos.y, pos.z, on);
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
	
	public static void hugeUpdate(World world, PC_VecI pos) {
		hugeUpdate(world, pos.x, pos.y, pos.z);
	}
	
	public static void notifyNeighbour(World world, int x, int y, int z) {
		world.notifyBlocksOfNeighborChange(x, y, z, getBID(world, x, y, z));
	}
	
	public static void notifyNeighbour(World world, PC_VecI pos) {
		notifyNeighbour(world, pos.x, pos.y, pos.z);
	}
	
	public static void notifyBlockOfNeighborChange(World world, int x, int y, int z, int blockID) {
		Block block = getBlock(world, x, y, z);
		if (block != null) {
			block.onNeighborBlockChange(world, x, y, z, blockID);
		}
	}
	
	public static void notifyBlockOfNeighborChange(World world, PC_VecI pos, int blockID) {
		notifyBlockOfNeighborChange(world, pos.x, pos.y, pos.z, blockID);
	}
	
	public static boolean isPlayerOPOrOwner(EntityPlayer player) {
		if (mcs().getConfigurationManager().getOps().contains(player.username.trim().toLowerCase()))
			return true;
		return mcs().getServerOwner() == player.getEntityName();
	}
	
	public static EnumGameType getGameTypeFor(EntityPlayer player) {
		return instance.iGetGameTypeFor(player);
	}
	
	public static boolean isCreative(EntityPlayer player) {
		return getGameTypeFor(player).isCreative();
	}
	
	public static EntityPlayer getPlayer() {
		return instance.iGetPlayer();
	}
	
	public static boolean anyPlayerInNear(World world, int x, int y, int z, double dist) {
		return world.getClosestPlayer(x + 0.5D, y + 0.5D, z + 0.5D, dist) != null;
	}
	
	public static boolean playerInNear(World world, int x, int y, int z, double dist) {
		EntityPlayer player = getPlayer();
		if (player == null)
			return false;
		x -= player.posX;
		y -= player.posY;
		z -= player.posZ;
		return x * x + y * y + z * z <= dist * dist;
	}
	
	public static void dropItemStack(World world, int x, int y, int z, ItemStack itemstack) {
		if (itemstack != null && !world.isRemote) {
			float f = rand.nextFloat() * 0.8F + 0.1F;
			float f1 = rand.nextFloat() * 0.8F + 0.1F;
			float f2 = rand.nextFloat() * 0.8F + 0.1F;
			
			while (itemstack.stackSize > 0) {
				int j = rand.nextInt(21) + 10;
				
				if (j > itemstack.stackSize) {
					j = itemstack.stackSize;
				}
				
				itemstack.stackSize -= j;
				EntityItem entityitem = new EntityItem(world, x + f, y + f1, z + f2, new ItemStack(itemstack.itemID, j, itemstack.getItemDamage()));
				
				if (itemstack.hasTagCompound()) {
					entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
				}
				
				float f3 = 0.05F;
				entityitem.motionX = (float) rand.nextGaussian() * f3;
				entityitem.motionY = (float) rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float) rand.nextGaussian() * f3;
				entityitem.delayBeforeCanPickup = 10;
				world.spawnEntityInWorld(entityitem);
			}
		}
	}
	
	public static void dropItemStack(World world, PC_VecI pos, ItemStack itemstack) {
		dropItemStack(world, pos.x, pos.y, pos.z, itemstack);
	}
	
	public static void spawnMobs(World world, int x, int y, int z, String type) {
		byte count = 5;
		boolean spawnParticles = playerInNear(world, x, y, z, 16);
		
		for (int q = 0; q < count; q++) {
			EntityLiving entityliving = (EntityLiving) EntityList.createEntityByName(type, world);
			
			if (entityliving == null) {
				return;
			}
			
			int c = world.getEntitiesWithinAABB(entityliving.getClass(), AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(8D, 4D, 8D)).size();
			
			if (c >= 6) {
				if (spawnParticles) {
					double d = world.rand.nextGaussian() * 0.02D;
					double d1 = world.rand.nextGaussian() * 0.02D;
					double d2 = world.rand.nextGaussian() * 0.02D;
					world.spawnParticle("smoke", x + 0.5D, y + 0.4D, z + 0.5D, d, d1, d2);
				}
				
				return;
			}
			
			double d3 = x + (world.rand.nextDouble() - world.rand.nextDouble()) * 3D;
			double d4 = (y + world.rand.nextInt(3)) - 1;
			double d5 = z + (world.rand.nextDouble() - world.rand.nextDouble()) * 3D;
			entityliving.setLocationAndAngles(d3, d4, d5, world.rand.nextFloat() * 360F, 0.0F);
			
			if (world.checkIfAABBIsClear(entityliving.boundingBox) && world.getCollidingBoundingBoxes(entityliving, entityliving.boundingBox).size() == 0) {
				world.spawnEntityInWorld(entityliving);
				
				if (spawnParticles) {
					world.playAuxSFX(2004, x, y, z, 0);
					entityliving.spawnExplosionParticle();
				}
				
				return;
			}
		}
	}
	
	public static boolean isEntityFX(Entity entity) {
		return instance.iIsEntityFX(entity);
	}
	
	public static void spawnEntityInWorld(World world, Entity entity, boolean clientToo) {
		if (world.isRemote) {
			if (!clientToo)
				return;
		} else {
			if (isEntityFX(entity))
				return;
		}
		world.spawnEntityInWorld(entity);
	}
	
	public static void spawnParticle(String name, Object... o) {
		instance.iSpawnParticle(name, o);
	}
	
	public static int getWorldDimension(World worldObj) {
		return worldObj.provider.dimensionId;
	}
	
	public static World getWorldForDimension(int dimension) {
		return instance.iGetWorldForDimension(dimension);
	}
	
	public static MinecraftServer mcs() {
		return MinecraftServer.getServer();
	}
	
	public static String getMinecraftVersion() {
		return PC_LauncherUtils.getMinecraftVersion();
	}
	
	public static boolean usingForge() {
		return PC_LauncherUtils.usingModLoader(PC_ModLoader.FORGE_MODLOADER);
	}
	
	public static CreativeTabs getCreativeTab(CreativeTabs _default) {
		return PC_APIModule.creativeTab;
	}
	
	public static boolean isClient() {
		return instance.iIsClient();
	}
	
	public static boolean isServer() {
		return !instance.iIsClient();
	}
	
	public static ItemStack getContainerItemStack(ItemStack itemStack) {
		return itemStack.getItem().getContainerItemStack(itemStack);
	}
	
	public static File getMCDirectory() {
		return instance.iGetMCDirectory();
	}
	
	public static File getPowerCraftFile() {
		return PC_LauncherUtils.getPowerCraftFile();
	}
	
	public static <T extends PC_INBT<T>> T loadFromNBT(NBTTagCompound nbttagcompound, String string, T nbt) {
		NBTTagCompound nbttag = nbttagcompound.getCompoundTag(string);
		return nbt.readFromNBT(nbttag);
	}
	
	public static void saveToNBT(NBTTagCompound nbttagcompound, String string, PC_INBT nbt) {
		NBTTagCompound nbttag = nbt.writeToNBT(new NBTTagCompound());
		if (nbttag != null)
			nbttagcompound.setCompoundTag(string, nbttag);
	}
	
	public static List<Integer> parseIntList(String list) {
		if (list == null) {
			return null;
		}
		
		String[] parts = list.split(",");
		ArrayList<Integer> intList = new ArrayList<Integer>();
		
		for (String part : parts) {
			try {
				intList.add(Integer.parseInt(part.trim()));
			} catch (NumberFormatException e) {
			}
		}
		
		return intList;
	}
	
	public static void saveToNBT(NBTTagCompound nbtTag, String key, Object value) {
		if (value == null) {
			return;
		} else if (value.getClass().isArray()) {
			NBTTagCompound nbtTag2 = new NBTTagCompound();
			int size = Array.getLength(value);
			nbtTag2.setInteger("count", size);
			nbtTag2.setString("type", value.getClass().getName());
			for (int i = 0; i < size; i++) {
				saveToNBT(nbtTag2, "value[" + i + "]", Array.get(value, i));
			}
			nbtTag.setCompoundTag(key, nbtTag2);
		} else if (value instanceof PC_INBT) {
			NBTTagCompound nbtTag2 = new NBTTagCompound();
			nbtTag2.setString("type", value.getClass().getName());
			saveToNBT(nbtTag2, "value", (PC_INBT) value);
			nbtTag.setCompoundTag(key, nbtTag2);
		} else if (value instanceof List) {
			List l = (List) value;
			NBTTagCompound nbtTag2 = new NBTTagCompound();
			int size = l.size();
			nbtTag2.setInteger("count", size);
			nbtTag2.setString("type", l.getClass().getName());
			for (int i = 0; i < size; i++) {
				saveToNBT(nbtTag2, "value[" + i + "]", l.get(i));
			}
			nbtTag.setCompoundTag(key, nbtTag2);
		} else if (value instanceof Map) {
			Map<?, ?> m = (Map) value;
			NBTTagCompound nbtTag2 = new NBTTagCompound();
			int size = m.size();
			nbtTag2.setInteger("count", size);
			nbtTag2.setString("type", m.getClass().getName());
			int i=0;
			for (Entry e:m.entrySet()) {
				saveToNBT(nbtTag2, "key[" + i + "]", e.getKey());
				saveToNBT(nbtTag2, "value[" + i + "]", e.getValue());
				i++;
			}
			nbtTag.setCompoundTag(key, nbtTag2);
		} else if (value instanceof Byte) {
			nbtTag.setByte(key, (Byte) value);
		} else if (value instanceof Short) {
			nbtTag.setShort(key, (Short) value);
		} else if (value instanceof Integer) {
			nbtTag.setInteger(key, (Integer) value);
		} else if (value instanceof Long) {
			nbtTag.setLong(key, (Long) value);
		} else if (value instanceof Float) {
			nbtTag.setFloat(key, (Float) value);
		} else if (value instanceof Double) {
			nbtTag.setDouble(key, (Double) value);
		} else if (value instanceof Boolean) {
			NBTTagCompound nbtTag2 = new NBTTagCompound();
			nbtTag2.setString("type", Boolean.class.getName());
			nbtTag2.setBoolean("value", (Boolean) value);
			nbtTag.setCompoundTag(key, nbtTag2);
		} else if (value instanceof String) {
			nbtTag.setString(key, (String) value);
		} else if (value instanceof ItemStack) {
			NBTTagCompound nbtTag2 = new NBTTagCompound();
			nbtTag2.setString("type", ItemStack.class.getName());
			((ItemStack) value).writeToNBT(nbtTag2);
			nbtTag.setCompoundTag(key, nbtTag2);
		}
	}
	
	public static Object loadFromNBT(NBTTagCompound nbtTag, String key) {
		Object value = nbtTag.getTag(key);
		if (value instanceof NBTTagCompound) {
			NBTTagCompound nbtTag2 = nbtTag.getCompoundTag(key);
			try {
				Class c = Class.forName(nbtTag2.getString("type"));
				if (c.isArray()) {
					int size = nbtTag2.getInteger("count");
					Object a = Array.newInstance(c.getComponentType(), size);
					for (int i = 0; i < size; i++) {
						Array.set(a, i, loadFromNBT(nbtTag2, "value[" + i + "]"));
					}
					return a;
				} else if (c == ItemStack.class) {
					return ItemStack.loadItemStackFromNBT(nbtTag2);
				} else if (c == Boolean.class) {
					return nbtTag2.getBoolean("value");
				} else {
					try {
						Object o = c.newInstance();
						if (o instanceof PC_INBT) {
							o = loadFromNBT(nbtTag2, "value", (PC_INBT) o);
						} else if (o instanceof List) {
							int size = nbtTag2.getInteger("count");
							for (int i = 0; i < size; i++) {
								((List) o).add(loadFromNBT(nbtTag2, "value[" + i + "]"));
							}
						} else if (o instanceof Map) {
							int size = nbtTag2.getInteger("count");
							for (int i = 0; i < size; i++) {
								((Map) o).put(loadFromNBT(nbtTag2, "key[" + i + "]"), loadFromNBT(nbtTag2, "value[" + i + "]"));
							}
						}
						return o;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if (value instanceof NBTTagByte) {
			return ((NBTTagByte) value).data;
		} else if (value instanceof NBTTagShort) {
			return ((NBTTagShort) value).data;
		} else if (value instanceof NBTTagInt) {
			return ((NBTTagInt) value).data;
		} else if (value instanceof NBTTagLong) {
			return ((NBTTagLong) value).data;
		} else if (value instanceof NBTTagFloat) {
			return ((NBTTagFloat) value).data;
		} else if (value instanceof NBTTagDouble) {
			return ((NBTTagDouble) value).data;
		} else if (value instanceof NBTTagString) {
			return ((NBTTagString) value).data;
		}
		return null;
	}
	
	public static double ticksToSecs(int ticks) {
		return ticks * 0.05D;
	}
	
	public static int ticksToSecsInt(int ticks) {
		return Math.round(ticks * 0.05F);
	}
	
	public static int secsToTicks(double secs) {
		return (int) (secs * 20);
	}

	public static void chatMsg(String tr) {
		instance.iChatMsg(tr);
	}
	
}
