package powercraft.api;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagByte;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagDouble;
import net.minecraft.src.NBTTagFloat;
import net.minecraft.src.NBTTagInt;
import net.minecraft.src.NBTTagLong;
import net.minecraft.src.NBTTagShort;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.World;
import powercraft.api.annotation.PC_Shining;
import powercraft.api.block.PC_Block;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_BlockRegistry;
import powercraft.api.registry.PC_MSGRegistry;
import powercraft.api.registry.PC_RegistryServer;
import powercraft.api.tileentity.PC_TileEntity;
import powercraft.launcher.PC_LauncherUtils;
import powercraft.launcher.loader.PC_ModLoader;

public class PC_Utils implements PC_IPacketHandler {
	protected static PC_Utils instance;
	public static final int BACK = 0, LEFT = 1, RIGHT = 2, FRONT = 3,
			BOTTOM = 4, TOP = 5;

	protected static final int SPAWNPARTICLEONBLOCKS = 1;

	public static final int BLOCK_NOTIFY = 1, BLOCK_UPDATE = 2,
			BLOCK_ONLY_SERVERSIDE = 4;

	// protected static HashMap<String, Object> objects = new HashMap<String,
	// Object>();

	private static Random rand = new Random();

	public static String NO_HARVEST = "NO_HARVEST",
			HARVEST_STOP = "HARVEST_STOP", NO_BUILD = "NO_BUILD",
			NO_PICKUP = "NO_PICKUP", BEAMTRACER_STOP = "BEAMTRACER_STOP",
			PASSIVE = "PASSIVE";

	protected PC_Utils() {
		PC_PacketHandler.registerPackethandler("PacketUtils", this);
	}

	public static class ValueWriting {

		public static Object writeItemStack(ItemStack itemstack) {
			if (itemstack == null)
				return new Object[0];
			Object o[] = new Object[4];
			o[0] = itemstack.getItem().itemID;
			o[1] = itemstack.stackSize;
			o[2] = itemstack.getItemDamage();
			if (itemstack.stackTagCompound != null) {
				try {
					o[3] = CompressedStreamTools
							.compress(itemstack.stackTagCompound);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return o;
		}

		public static ItemStack readItemStack(Object[] o) {
			if (o.length != 4)
				return null;
			int itemID = (Integer) o[0];
			int stackSize = (Integer) o[1];
			int meta = (Integer) o[2];
			byte[] nbt = (byte[]) o[3];
			ItemStack itemstack = new ItemStack(itemID, stackSize, meta);
			if (nbt != null) {
				try {
					itemstack.stackTagCompound = CompressedStreamTools
							.decompress(nbt);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return itemstack;
		}

		public static int getFieldIDByName(Class c, String name) {
			Field f[] = c.getDeclaredFields();
			for (int i = 0; i < f.length; i++) {
				if (f[i].getName().equals(name))
					return i;
			}
			return -1;
		}

		public static void setBlockBounds(Block block, double x, double y,
				double z, double width, double height, double depht) {
			PC_ReflectHelper.setValue(Block.class, block, 184, x, double.class);
    		PC_ReflectHelper.setValue(Block.class, block, 185, y, double.class);
    		PC_ReflectHelper.setValue(Block.class, block, 186, z, double.class);
    		PC_ReflectHelper.setValue(Block.class, block, 187, width, double.class);
    		PC_ReflectHelper.setValue(Block.class, block, 188, height, double.class);
    		PC_ReflectHelper.setValue(Block.class, block, 189, depht, double.class);
		}

		public static void setBlockState(World world, int x, int y, int z,
				boolean on) {
			Block b = Block.blocksList[GameInfo.getBID(world, x, y, z)];

			if (b instanceof PC_Block) {
				int meta = GameInfo.getMD(world, x, y, z);
				PC_TileEntity te = GameInfo.getTE(world, x, y, z);
				Class c = b.getClass();

				if (c.isAnnotationPresent(PC_Shining.class)) {
					Block bon = (Block) PC_ReflectHelper
							.getFieldsWithAnnotation(c, c, PC_Shining.ON.class)
							.get(0);
					Block boff = (Block) PC_ReflectHelper
							.getFieldsWithAnnotation(c, c, PC_Shining.OFF.class)
							.get(0);

					if ((b == bon && !on) || (b == boff && on)) {
						if (on) {
							b = bon;
						} else {
							b = boff;
						}

						PC_GlobalVariables.tileEntity = te;

						world.setBlock(x, y, z, b.blockID,
								meta, BLOCK_NOTIFY);

						PC_GlobalVariables.tileEntity = null;

						if (te != null) {
							PC_PacketHandler.sendTileEntity(te);
						}
						GameInfo.markBlockForUpdate(world, x, y, z);
						hugeUpdate(world, x, y, z);

					}
				}
			}
		}

		public static boolean setMD(World world, PC_VecI pos, int md) {
			return setMD(world, pos.x, pos.y, pos.z, md);
		}

		public static boolean setMD(World world, int x, int y, int z, int md) {
			if (world != null) {
				return world.setBlockMetadataWithNotify(x, y, z, md, 0);
			}

			return false;
		}

		public static <t extends TileEntity> t setTE(World world, int x, int y,
				int z, t createTileEntity) {
			world.setBlockTileEntity(x, y, z, createTileEntity);
			return createTileEntity;
		}

		public static void spawnMobFromSpawner(World world, int x, int y, int z) {
			TileEntityMobSpawner te = PC_Utils.GameInfo.getTE(world, x, y, z,
					Block.mobSpawner.blockID);

			if (te != null) {
				ValueWriting.spawnMobs(world, x, y, z,
						PC_Utils.GameInfo.getMobID(te));
			}
		}

		public static void preventSpawnerSpawning(World world, int x, int y,
				int z) {
			TileEntityMobSpawner te = PC_Utils.GameInfo.getTE(world, x, y, z,
					Block.mobSpawner.blockID);

			if (te != null) {
				te.func_98049_a().field_98286_b = 20;
			}
		}

		public static int reverseSide(int l) {
			l = (l + 2) % 4;

			return l;
		}

		public static void hugeUpdate(World world, int x, int y, int z) {
			int blockID = PC_Utils.GameInfo.getBID(world, x, y, z);
			ValueWriting.notifyBlockOfNeighborChange(world, x - 2, y, z,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y, z,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y, z,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x + 2, y, z,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x, y - 2, z,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x, y - 1, z,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x, y + 1, z,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x, y + 2, z,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x, y, z - 2,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x, y, z - 1,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x, y, z + 1,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x, y, z + 2,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y + 1,
					z - 1, blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y + 1,
					z - 1, blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y + 1,
					z + 1, blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y + 1,
					z + 1, blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y + 1, z,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y + 1, z,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x, y + 1, z + 1,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x, y + 1, z - 1,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y, z - 1,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y, z - 1,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y, z + 1,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y, z + 1,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y - 1,
					z - 1, blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y - 1,
					z - 1, blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y - 1,
					z + 1, blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y - 1,
					z + 1, blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y - 1, z,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y - 1, z,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x, y - 1, z + 1,
					blockID);
			ValueWriting.notifyBlockOfNeighborChange(world, x, y - 1, z - 1,
					blockID);
		}

		public static void spawnMobs(World world, int x, int y, int z,
				String type) {
			byte count = 5;
			boolean spawnParticles = world.getClosestPlayer(x + 0.5D, y + 0.5D,
					z + 0.5D, 16D) != null;

			for (int q = 0; q < count; q++) {
				EntityLiving entityliving = (EntityLiving) EntityList
						.createEntityByName(type, world);

				if (entityliving == null) {
					return;
				}

				int c = world.getEntitiesWithinAABB(
						entityliving.getClass(),
						AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1,
								z + 1).expand(8D, 4D, 8D)).size();

				if (c >= 6) {
					if (spawnParticles) {
						double d = world.rand.nextGaussian() * 0.02D;
						double d1 = world.rand.nextGaussian() * 0.02D;
						double d2 = world.rand.nextGaussian() * 0.02D;
						world.spawnParticle("smoke", x + 0.5D, y + 0.4D,
								z + 0.5D, d, d1, d2);
					}

					return;
				}

				double d3 = x
						+ (world.rand.nextDouble() - world.rand.nextDouble())
						* 3D;
				double d4 = (y + world.rand.nextInt(3)) - 1;
				double d5 = z
						+ (world.rand.nextDouble() - world.rand.nextDouble())
						* 3D;
				entityliving.setLocationAndAngles(d3, d4, d5,
						world.rand.nextFloat() * 360F, 0.0F);

				if (world.checkIfAABBIsClear(entityliving.boundingBox)
						&& world.getCollidingBoundingBoxes(entityliving,
								entityliving.boundingBox).size() == 0) {
					world.spawnEntityInWorld(entityliving);

					if (spawnParticles) {
						world.playAuxSFX(2004, x, y, z, 0);
						entityliving.spawnExplosionParticle();
					}

					return;
				}
			}
		}

		public static boolean setBID(World world, int x, int y, int z, int id,
				int meta) {
			return world.setBlock(x, y, z, id, meta,
					BLOCK_NOTIFY | BLOCK_UPDATE);
		}

		public static ItemStack extractAndRemoveChest(World world, PC_VecI pos) {
			if (PC_MSGRegistry.hasFlag(world, pos, PC_Utils.NO_HARVEST)) {
				return null;
			}

			TileEntity tec = GameInfo.getTE(world, pos);

			if (tec == null) {
				return null;
			}

			ItemStack stack = new ItemStack(
					PC_BlockRegistry.getPCBlockByName("PCco_BlockBlockSaver"));
			NBTTagCompound blocktag = new NBTTagCompound();
			tec.writeToNBT(blocktag);
			int dmg = GameInfo.getBID(world, pos);
			stack.setItemDamage(dmg);
			blocktag.setInteger("BlockMeta", GameInfo.getMD(world, pos));
			stack.setTagCompound(blocktag);

			if (tec instanceof IInventory) {
				IInventory ic = (IInventory) tec;

				for (int i = 0; i < ic.getSizeInventory(); i++) {
					ic.setInventorySlotContents(i, null);
				}
			}

			tec.invalidate();
			ValueWriting.setBID(world, pos, 0, 0);
			return stack;
		}

		public static boolean setBID(World world, PC_VecI pos, int id, int meta) {
			return ValueWriting.setBID(world, pos.x, pos.y, pos.z, id, meta);
		}

		public static void notifyNeighbour(World world, int x, int y, int z) {
			world.notifyBlocksOfNeighborChange(x, y, z,
					GameInfo.getBID(world, x, y, z));
		}

		public static void notifyNeighbour(World world, PC_VecI pos) {
			ValueWriting.notifyNeighbour(world, pos.x, pos.y, pos.z);
		}

		public static boolean setBIDNoNotify(World world, int x, int y, int z,
				int id, int meta) {
			return world.setBlock(x, y, z, id, meta, 0);
		}

		public static boolean setBIDNoNotify(World world, PC_VecI pos, int id,
				int meta) {
			return PC_Utils.ValueWriting.setBID(world, pos.x, pos.y, pos.z, id,
					meta);
		}

		public static boolean setMDNoNotify(World world, int x, int y, int z,
				int meta) {
			return world.setBlockMetadataWithNotify(x, y, z, meta, 0);
		}

		public static boolean setMDNoNotify(World world, PC_VecI pos, int meta) {
			return PC_Utils.ValueWriting.setMDNoNotify(world, pos.x, pos.y,
					pos.z, meta);
		}

		public static void spawnParticle(String name, Object... o) {
			PC_Utils.instance.iSpawnParticle(name, o);
		}

		public static void notifyBlockOfNeighborChange(World world, int x,
				int y, int z, int blockId) {
			Block block = Block.blocksList[world.getBlockId(x, y, z)];

			if (block != null) {
				block.onNeighborBlockChange(world, x, y, z, blockId);
			}
		}

		public static void givePowerToBlock(World world, int x, int y, int z,
				float power) {
			List<PC_Struct3<PC_VecI, PC_IMSG, Float>> powerReceivers = GameInfo
					.getPowerReceiverConnectedTo(world, x, y, z);
			float receivers = powerReceivers.size();

			for (PC_Struct3<PC_VecI, PC_IMSG, Float> receiver : powerReceivers) {
				receiver.b.msg(PC_MSGRegistry.MSG_RECIVE_POWER, world,
						receiver.a.x, receiver.a.y, receiver.a.z, power
								/ receivers * receiver.c);
			}
		}

		public static void dropItemStack(World world, ItemStack itemstack,
				PC_VecI pos) {
			if (itemstack != null && !world.isRemote) {
				float f = PC_Utils.rand.nextFloat() * 0.8F + 0.1F;
				float f1 = PC_Utils.rand.nextFloat() * 0.8F + 0.1F;
				float f2 = PC_Utils.rand.nextFloat() * 0.8F + 0.1F;

				while (itemstack.stackSize > 0) {
					int j = PC_Utils.rand.nextInt(21) + 10;

					if (j > itemstack.stackSize) {
						j = itemstack.stackSize;
					}

					itemstack.stackSize -= j;
					EntityItem entityitem = new EntityItem(world, pos.x + f,
							pos.y + f1, pos.z + f2, new ItemStack(
									itemstack.itemID, j,
									itemstack.getItemDamage()));

					if (itemstack.hasTagCompound()) {
						entityitem.getEntityItem().setTagCompound(
								(NBTTagCompound) itemstack.getTagCompound()
										.copy());
					}

					float f3 = 0.05F;
					entityitem.motionX = (float) PC_Utils.rand.nextGaussian()
							* f3;
					entityitem.motionY = (float) PC_Utils.rand.nextGaussian()
							* f3 + 0.2F;
					entityitem.motionZ = (float) PC_Utils.rand.nextGaussian()
							* f3;
					entityitem.delayBeforeCanPickup = 10;
					world.spawnEntityInWorld(entityitem);
				}
			}
		}

	}

	public static class Converter {

		public static double ticksToSecs(int ticks) {
			return ticks * 0.05D;
		}

		public static int ticksToSecsInt(int ticks) {
			return Math.round(ticks * 0.05F);
		}

		public static int secsToTicks(double secs) {
			return (int) (secs * 20);
		}

		public static String doubleToString(double d) {
			return "" + d;
		}

	}

	public static class GameInfo {

		public static String getMinecraftVersion() {
			return PC_LauncherUtils.getMinecraftVersion();
		}

		public static CreativeTabs getCreativeTab(CreativeTabs _default) {
			return _default;
		}

		public static boolean isPlayerOPOrOwner(EntityPlayer player) {
			if (mcs().getConfigurationManager().getOps()
					.contains(player.username.trim().toLowerCase()))
				return true;
			return GameInfo.mcs().getServerOwner() == player.getEntityName();
		}

		public static boolean isClient() {
			return PC_Utils.instance.client();
		}

		public static void markBlockForUpdate(World world, int x, int y, int z) {
			world.markBlockForUpdate(x, y, z);
		}

		public static void markBlockForUpdate(World world, PC_VecI pos) {
			world.markBlockForUpdate(pos.x, pos.y, pos.z);
		}

		public static boolean isServer() {
			return !PC_Utils.instance.client();
		}

		public static int getWorldDimension(World worldObj) {
			return worldObj.provider.dimensionId;
		}

		public static EnumGameType getGameTypeFor(EntityPlayer player) {
			return PC_Utils.instance.iGetGameTypeFor(player);
		}

		public static boolean isCreative(EntityPlayer player) {
			return PC_Utils.GameInfo.getGameTypeFor(player).isCreative();
		}

		public static <t extends TileEntity> t getTE(IBlockAccess world, int x,
				int y, int z) {
			if (world != null) {
				TileEntity te = world.getBlockTileEntity(x, y, z);

				try {
					t tet = (t) te;
					return tet;
				} catch (ClassCastException e) {
					return null;
				}
			}

			return null;
		}

		public static <t extends TileEntity> t getTE(IBlockAccess world,
				PC_VecI vec) {
			return PC_Utils.GameInfo.getTE(world, vec.x, vec.y, vec.z);
		}

		public static <t extends TileEntity> t getTE(IBlockAccess world, int x,
				int y, int z, int blockID) {
			if (world != null) {
				if (GameInfo.getBID(world, x, y, z) == blockID) {
					TileEntity te = world.getBlockTileEntity(x, y, z);

					try {
						t tet = (t) te;
						return tet;
					} catch (ClassCastException e) {
						return null;
					}
				}
			}

			return null;
		}

		public static int getBID(IBlockAccess world, int x, int y, int z) {
			if (world != null) {
				return world.getBlockId(x, y, z);
			}

			return 0;
		}

		public static int getBID(IBlockAccess world, PC_VecI vec) {
			return PC_Utils.GameInfo.getBID(world, vec.x, vec.y, vec.z);
		}

		public static int getMD(IBlockAccess world, int x, int y, int z) {
			if (world != null) {
				return world.getBlockMetadata(x, y, z);
			}

			return 0;
		}

		public static int getMD(IBlockAccess world, PC_VecI vec) {
			return PC_Utils.GameInfo.getMD(world, vec.x, vec.y, vec.z);
		}

		/*public static boolean isChestEmpty(World world, int x, int y, int z,
				ItemStack itemStack) {
			IInventory invAt = Inventory.getCompositeInventoryAt(world,
					new PC_VecI(x, y, z));

			if (invAt != null) {
				if (itemStack == null || itemStack.itemID == 0) {
					return Inventory.isInventoryEmpty(invAt);
				} else {
					return Inventory.isInventoryEmptyOf(invAt, itemStack);
				}
			}

			List<IInventory> list = world.getEntitiesWithinAABB(
					IInventory.class,
					AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)
							.expand(0.6D, 0.6D, 0.6D));

			if (list.size() >= 1) {
				if (itemStack == null || itemStack.itemID == 0) {
					return Inventory.isInventoryEmpty(list.get(0));
				} else {
					return Inventory.isInventoryEmptyOf(list.get(0), itemStack);
				}
			}

			List<PC_IInventoryWrapper> list2 = world.getEntitiesWithinAABB(
					PC_IInventoryWrapper.class,
					AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)
							.expand(0.6D, 0.6D, 0.6D));

			if (list2.size() >= 1) {
				if (list2.get(0).getInventory() != null) {
					if (itemStack == null || itemStack.itemID == 0) {
						return Inventory.isInventoryEmpty(list2.get(0)
								.getInventory());
					} else {
						return Inventory.isInventoryEmptyOf(list2.get(0)
								.getInventory(), itemStack);
					}
				}
			}

			return false;
		}

		public static boolean isChestFull(World world, int x, int y, int z,
				ItemStack itemStack) {
			IInventory invAt = Inventory.getCompositeInventoryAt(world,
					new PC_VecI(x, y, z));

			if (invAt != null) {
				if (itemStack == null || itemStack.itemID == 0) {
					return Inventory.hasInventoryNoFreeSlots(invAt);
				} else {
					return Inventory.hasInventoryPlaceFor(invAt, itemStack);
				}
			}

			List<IInventory> list = world.getEntitiesWithinAABB(
					IInventory.class,
					AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)
							.expand(0.6D, 0.6D, 0.6D));

			if (list.size() >= 1) {
				if (itemStack == null || itemStack.itemID == 0) {
					return Inventory.hasInventoryNoFreeSlots(list.get(0));
				} else {
					return Inventory.hasInventoryPlaceFor(list.get(0),
							itemStack);
				}
			}

			List<PC_IInventoryWrapper> list2 = world.getEntitiesWithinAABB(
					PC_IInventoryWrapper.class,
					AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)
							.expand(0.6D, 0.6D, 0.6D));

			if (list2.size() >= 1) {
				if (list2.get(0).getInventory() != null) {
					if (itemStack == null || itemStack.itemID == 0) {
						return Inventory.hasInventoryNoFreeSlots(list2.get(0)
								.getInventory());
					} else {
						return Inventory.hasInventoryPlaceFor(list2.get(0)
								.getInventory(), itemStack);
					}
				}
			}

			return false;
		}*/

		public static String getMobID(TileEntityMobSpawner te) {
			return te.func_98049_a().func_98276_e();
		}

		public static File getMCDirectory() {
			return PC_Utils.instance.iGetMCDirectory();
		}

		public static MinecraftServer mcs() {
			return MinecraftServer.getServer();
		}

		public static boolean isEntityFX(Entity entity) {
			return PC_Utils.instance.iIsEntityFX(entity);
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

		public static Block getBlock(IBlockAccess world, PC_VecI pos) {
			return GameInfo.getBlock(world, pos.x, pos.y, pos.z);
		}

		public static File getPowerCraftFile() {
			return PC_LauncherUtils.getPowerCraftFile();
		}

		public static boolean isPoweredDirectly(World world, int x, int y, int z) {
			return getPoweredDirectlyValue(world, x, y, z) > 0;
		}

		public static int getPoweredDirectlyValue(World world, int x, int y,
				int z) {
			return world.getBlockPowerInput(x, y, z);
		}

		public static boolean isPoweredIndirectly(World world, int x, int y,
				int z) {
			return world.isBlockIndirectlyGettingPowered(x, y, z);
		}

		public static boolean isPoweredIndirectly(World world, PC_VecI pos) {
			return isPoweredIndirectly(world, pos.x, pos.y,
					pos.z);
		}

		public static boolean isPoweredDirectly(World world, PC_VecI pos) {
			return isPoweredDirectly(world, pos.x, pos.y,
					pos.z);
		}

		public static boolean usingForge() {
			return PC_LauncherUtils
					.usingModLoader(PC_ModLoader.FORGE_MODLOADER);
		}

		public static float giveConductorValueFor(Block b) {
			if (b == null) {
				return 0.0f;
			}

			if (b.blockID == Block.blockSteel.blockID) {
				return 0.8f;
			}

			if (b.blockID == Block.blockGold.blockID) {
				return 0.9f;
			}

			if (b instanceof PC_IMSG) {
				Object o = ((PC_IMSG) b).msg(PC_MSGRegistry.MSG_CONDUCTIVITY);
				if (o instanceof Float)
					return (Float) o;
			}

			return 0.0f;
		}

		public static Block getBlock(IBlockAccess world, int x, int y, int z) {
			return Block.blocksList[PC_Utils.GameInfo.getBID(world, x, y, z)];
		}

		public static boolean poweredFromInput(World world, int x, int y,
				int z, int inp) {
			return GameInfo.poweredFromInput(world, x, y, z, inp, 0);
		}

		public static boolean poweredFromInput(World world, int x, int y,
				int z, int inp, int rotation) {
			if (world == null) {
				return false;
			}

			if (inp == 4) {
				boolean isProviding = (world.getIndirectPowerLevelTo(
						x, y - 1, z, 0) > 0 || (world.getBlockId(x, y - 1, z) == Block.redstoneWire.blockID && world
						.getBlockMetadata(x, y - 1, z) > 0));
				return isProviding;
			}

			if (inp == 5) {
				boolean isProviding = (world.getIndirectPowerLevelTo(
						x, y + 1, z, 1) > 0 || (world.getBlockId(x, y + 1, z) == Block.redstoneWire.blockID && world
						.getBlockMetadata(x, y + 1, z) > 0));
				return isProviding;
			}

			int N0 = 0, N1 = 1, N2 = 2, N3 = 3;

			if (inp == 0) {
				N0 = 0;
				N1 = 1;
				N2 = 2;
				N3 = 3;
			}

			if (inp == 1) {
				N0 = 3;
				N1 = 0;
				N2 = 1;
				N3 = 2;
			} else if (inp == 2) {
				N0 = 1;
				N1 = 2;
				N2 = 3;
				N3 = 0;
			} else if (inp == 3) {
				N0 = 2;
				N1 = 3;
				N2 = 0;
				N3 = 1;
			}

			if (rotation == N0) {
				return (world.getIndirectPowerLevelTo(x, y, z + 1, 3) > 0 || world
						.getBlockId(x, y, z + 1) == Block.redstoneWire.blockID
						&& world.getBlockMetadata(x, y, z + 1) > 0);
			}

			if (rotation == N1) {
				return (world.getIndirectPowerLevelTo(x - 1, y, z, 4) > 0 || world
						.getBlockId(x - 1, y, z) == Block.redstoneWire.blockID
						&& world.getBlockMetadata(x - 1, y, z) > 0);
			}

			if (rotation == N2) {
				return (world.getIndirectPowerLevelTo(x, y, z - 1, 2) > 0 || world
						.getBlockId(x, y, z - 1) == Block.redstoneWire.blockID
						&& world.getBlockMetadata(x, y, z - 1) > 0);
			}

			if (rotation == N3) {
				return (world.getIndirectPowerLevelTo(x + 1, y, z, 5) > 0 || world
						.getBlockId(x + 1, y, z) == Block.redstoneWire.blockID
						&& world.getBlockMetadata(x + 1, y, z) > 0);
			}

			return false;
		}

		public static void searchPowerReceiverConnectedTo(World world, int x,
				int y, int z,
				List<PC_Struct3<PC_VecI, PC_IMSG, Float>> receivers,
				List<PC_Struct2<PC_VecI, Float>> allpos, float power) {
			Block b = PC_Utils.GameInfo.getBlock(world, x, y, z);
			PC_VecI pos = new PC_VecI(x, y, z);
			PC_Struct2<PC_VecI, Float> oldStruct = null;

			for (PC_Struct2<PC_VecI, Float> s : allpos) {
				if (s.a.equals(pos)) {
					oldStruct = s;
					break;
				}
			}

			if (b instanceof PC_IMSG) {
				Object o = ((PC_IMSG) b).msg(
						PC_MSGRegistry.MSG_CAN_RECIVE_POWER, b);
				if (o instanceof Boolean && ((Boolean) o) == true) {
					if (oldStruct == null) {
						receivers.add(new PC_Struct3<PC_VecI, PC_IMSG, Float>(
								pos, (PC_IMSG) b, power));
					}
				}
				return;
			}

			float value = PC_Utils.GameInfo.giveConductorValueFor(b);

			if (value < 0.01f) {
				return;
			}

			if (oldStruct == null) {
				oldStruct = new PC_Struct2<PC_VecI, Float>(pos, 0.0f);
				allpos.add(oldStruct);
			}

			if (power > oldStruct.b) {
				oldStruct.b = power;
				power *= value;

				if (power < 0.01f) {
					return;
				}

				searchPowerReceiverConnectedTo(world, x + 1, y, z, receivers,
						allpos, power);
				searchPowerReceiverConnectedTo(world, x - 1, y, z, receivers,
						allpos, power);
				searchPowerReceiverConnectedTo(world, x, y + 1, z, receivers,
						allpos, power);
				searchPowerReceiverConnectedTo(world, x, y - 1, z, receivers,
						allpos, power);
				searchPowerReceiverConnectedTo(world, x, y, z + 1, receivers,
						allpos, power);
				searchPowerReceiverConnectedTo(world, x, y, z - 1, receivers,
						allpos, power);
			}
		}

		public static List<PC_Struct3<PC_VecI, PC_IMSG, Float>> getPowerReceiverConnectedTo(
				World world, int x, int y, int z) {
			Random rand = new Random();
			List<PC_Struct3<PC_VecI, PC_IMSG, Float>> receivers = new ArrayList<PC_Struct3<PC_VecI, PC_IMSG, Float>>();
			List<PC_Struct2<PC_VecI, Float>> blocks = new ArrayList<PC_Struct2<PC_VecI, Float>>();
			PC_Utils.GameInfo.searchPowerReceiverConnectedTo(world, x, y, z,
					receivers, blocks, 1.0f);
			PC_PacketHandler.sendToPacketHandler(true, world, "PacketUtils",
					PC_Utils.SPAWNPARTICLEONBLOCKS, blocks);
			return receivers;
		}

		public static ItemStack getContainerItemStack(ItemStack itemStack) {
			return new ItemStack(itemStack.getItem().getContainerItem());
		}

		public static World getWorldForDimension(int dimension) {
			return instance.iGetWorldForDimension(dimension);
		}

	}

	public static class SaveHandler {

		public static <T extends PC_INBT<T>> T loadFromNBT(
				NBTTagCompound nbttagcompound, String string, T nbt) {
			NBTTagCompound nbttag = nbttagcompound.getCompoundTag(string);
			return nbt.readFromNBT(nbttag);
		}

		public static void saveToNBT(NBTTagCompound nbttagcompound,
				String string, PC_INBT nbt) {
			NBTTagCompound nbttag = nbt.writeToNBT(new NBTTagCompound());
			if (nbttag != null)
				nbttagcompound.setCompoundTag(string, nbttag);
		}

		public static void saveToNBT(NBTTagCompound nbtTag, String key,
				Object value) {
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
				SaveHandler.saveToNBT(nbtTag2, "value", (PC_INBT) value);
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
						Object a = Array
								.newInstance(c.getComponentType(), size);
						for (int i = 0; i < size; i++) {
							Array.set(a, i,
									loadFromNBT(nbtTag2, "value[" + i + "]"));
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
								loadFromNBT(nbtTag2, "value", (PC_INBT) o);
							} else if (o instanceof List) {
								int size = nbtTag2.getInteger("count");
								for (int i = 0; i < size; i++) {
									((List) o).add(loadFromNBT(nbtTag2,
											"value[" + i + "]"));
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

	}

	public static class Communication {

		public static void chatMsg(String msg, boolean clear) {
			PC_Utils.instance.iChatMsg(msg, clear);
		}

	}

	public static class Coding {

		public static boolean areObjectsEqual(Object a, Object b) {
			return a == null ? b == null : a.equals(b);
		}

		public static Constructor findBestConstructor(Class c, Class[] cp) {
			Constructor[] constructors = c.getConstructors();

			for (Constructor constructor : constructors) {
				Class[] cep = constructor.getParameterTypes();

				if ((cep == null && cp == null)
						|| (cep == null && cp.length == 0)
						|| (cep.length == 0 && cp == null)) {
					return constructor;
				}

				if (cep.length == cp.length) {
					boolean ok = true;

					for (int i = 0; i < cep.length; i++) {
						if (cep[i].isPrimitive()) {
							if (cep[i].equals(boolean.class)) {
								cep[i] = Boolean.class;
							} else if (cep[i].equals(int.class)) {
								cep[i] = Integer.class;
							} else if (cep[i].equals(float.class)) {
								cep[i] = Float.class;
							} else if (cep[i].equals(double.class)) {
								cep[i] = Double.class;
							} else if (cep[i].equals(long.class)) {
								cep[i] = Long.class;
							} else if (cep[i].equals(char.class)) {
								cep[i] = Integer.class;
							} else if (cep[i].equals(short.class)) {
								cep[i] = Short.class;
							}
						}

						if (!cep[i].isAssignableFrom(cp[i])) {
							ok = false;
							break;
						}
					}

					if (ok) {
						return constructor;
					}
				}
			}

			return null;
		}

		public static int getValueNum(Class c, String n) {
			Field[] fields = c.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getName().equals(n)) {
					return i;
				}
			}
			return -1;
		}

	}

	/*public static class Inventory {

		public static IInventory getCompositeInventoryAt(IBlockAccess world,
				PC_VecI pos) {
			TileEntity te = GameInfo.getTE(world, pos);

			if (te == null) {
				return null;
			}

			if (te instanceof PC_IInventoryWrapper) {
				return ((PC_IInventoryWrapper) te).getInventory();
			}

			if (!(te instanceof IInventory)) {
				return null;
			}

			IInventory inv = (IInventory) te;
			int id = GameInfo.getBID(world, pos);

			if (id == Block.chest.blockID) {
				if (GameInfo.getBID(world, pos.offset(-1, 0, 0)) == Block.chest.blockID) {
					inv = new InventoryLargeChest("Large chest",
							(IInventory) GameInfo.getTE(world,
									pos.offset(-1, 0, 0)), inv);
				}

				if (GameInfo.getBID(world, pos.offset(1, 0, 0)) == Block.chest.blockID) {
					inv = new InventoryLargeChest("Large chest", inv,
							(IInventory) GameInfo.getTE(world,
									pos.offset(1, 0, 0)));
				}

				if (GameInfo.getBID(world, pos.offset(0, 0, -1)) == Block.chest.blockID) {
					inv = new InventoryLargeChest("Large chest",
							(IInventory) GameInfo.getTE(world,
									pos.offset(0, 0, -1)), inv);
				}

				if (GameInfo.getBID(world, pos.offset(0, 0, 1)) == Block.chest.blockID) {
					inv = new InventoryLargeChest("Large chest", inv,
							(IInventory) GameInfo.getTE(world,
									pos.offset(0, 0, 1)));
				}
			}

			return inv;
		}

		public static boolean storeItemInSlot(IInventory inventory,
				ItemStack stackToStore, int slot) {
			if (stackToStore == null || stackToStore.stackSize == 0) {
				return false;
			}

			if (inventory instanceof PC_ISpecialAccessInventory
					&& !((PC_ISpecialAccessInventory) inventory)
							.canMachineInsertStackTo(slot, stackToStore)) {
				return false;
			}

			ItemStack destination = inventory.getStackInSlot(slot);

			if (destination == null) {
				int numStored = stackToStore.stackSize;
				numStored = Math.min(numStored, stackToStore.getMaxStackSize());
				numStored = Math.min(numStored,
						inventory.getInventoryStackLimit());
				destination = stackToStore.splitStack(numStored);
				inventory.setInventorySlotContents(slot, destination);
				return true;
			}

			if (destination.itemID == stackToStore.itemID
					&& destination.isStackable()
					&& (!destination.getHasSubtypes() || destination
							.getItemDamage() == stackToStore.getItemDamage())
					&& destination.stackSize < inventory
							.getInventoryStackLimit()) {
				int numStored = stackToStore.stackSize;
				numStored = Math.min(numStored, destination.getMaxStackSize()
						- destination.stackSize);
				numStored = Math.min(numStored,
						inventory.getInventoryStackLimit()
								- destination.stackSize);
				destination.stackSize += numStored;
				stackToStore.stackSize -= numStored;
				return (numStored > 0);
			}

			return false;
		}

		public static boolean addItemStackToInventory(IInventory inv,
				ItemStack itemstack) {
			if (!itemstack.isItemDamaged()) {
				int i;

				do {
					i = itemstack.stackSize;
					itemstack.stackSize = Inventory.storePartialItemStack(inv,
							itemstack);
				} while (itemstack.stackSize > 0 && itemstack.stackSize < i);

				return itemstack.stackSize < i;
			}

			int j = Inventory.getFirstEmptySlot(inv, itemstack);

			if (j >= 0) {
				inv.setInventorySlotContents(j,
						ItemStack.copyItemStack(itemstack));
				itemstack.stackSize = 0;
				return true;
			}

			return false;
		}

		public static boolean addWholeItemStackToInventory(IInventory inv,
				ItemStack itemstack) {
			if (!itemstack.isItemDamaged()) {
				int oldSize;

				do {
					oldSize = itemstack.stackSize;
					itemstack.stackSize = Inventory.storePartialItemStack(inv,
							itemstack);
				} while (itemstack.stackSize > 0
						&& itemstack.stackSize < oldSize);

				return itemstack.stackSize == 0;
			}

			int emptySlot = Inventory.getFirstEmptySlot(inv, itemstack);

			if (emptySlot >= 0) {
				inv.setInventorySlotContents(emptySlot,
						ItemStack.copyItemStack(itemstack));
				itemstack.stackSize = 0;
				return true;
			}

			return false;
		}

		private static int getStackWithFreeSpace(IInventory inv,
				ItemStack itemstack) {
			for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
				if (inv instanceof PC_ISpecialAccessInventory) {
					if (!((PC_ISpecialAccessInventory) inv)
							.canMachineInsertStackTo(slot, itemstack)) {
						continue;
					}
				}

				ItemStack stackAt = inv.getStackInSlot(slot);

				if (stackAt != null
						&& stackAt.itemID == itemstack.itemID
						&& stackAt.isStackable()
						&& stackAt.stackSize < stackAt.getMaxStackSize()
						&& stackAt.stackSize < inv.getInventoryStackLimit()
						&& (!stackAt.getHasSubtypes() || stackAt
								.getItemDamage() == itemstack.getItemDamage())) {
					return slot;
				}
			}

			return -1;
		}

		static int storePartialItemStack(IInventory inv, ItemStack itemstack) {
			int id = itemstack.itemID;
			int size = itemstack.stackSize;

			if (itemstack.getMaxStackSize() == 1) {
				int firstEmpty = Inventory.getFirstEmptySlot(inv, itemstack);

				if (firstEmpty < 0) {
					return size;
				}

				if (inv.getStackInSlot(firstEmpty) == null) {
					inv.setInventorySlotContents(firstEmpty,
							ItemStack.copyItemStack(itemstack));
				}

				return 0;
			}

			int targetSlot = getStackWithFreeSpace(inv, itemstack);

			if (targetSlot < 0) {
				targetSlot = Inventory.getFirstEmptySlot(inv, itemstack);
			}

			if (targetSlot < 0) {
				return size;
			}

			ItemStack is = inv.getStackInSlot(targetSlot);

			if (is == null) {
				is = new ItemStack(id, 0, itemstack.getItemDamage());
			}

			int canStore = size;

			if (canStore > is.getMaxStackSize() - is.stackSize) {
				canStore = is.getMaxStackSize() - is.stackSize;
			}

			if (canStore > inv.getInventoryStackLimit() - is.stackSize) {
				canStore = inv.getInventoryStackLimit() - is.stackSize;
			}

			if (canStore == 0) {
				return size;
			} else {
				size -= canStore;
				is.stackSize += canStore;
				inv.setInventorySlotContents(targetSlot, is);
				return size;
			}
		}

		private static int getFirstEmptySlot(IInventory inv,
				ItemStack stackInserted) {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) == null) {
					if (inv instanceof PC_ISpecialAccessInventory) {
						if (!((PC_ISpecialAccessInventory) inv)
								.canMachineInsertStackTo(i, stackInserted)) {
							continue;
						}
					}

					return i;
				}
			}

			return -1;
		}

		public static boolean storeItemInInventory(IInventory inventory,
				ItemStack stack) {
			if (inventory instanceof TileEntityFurnace) {
				if (PC_RecipeRegistry.isSmeltable(stack)) {
					return storeItemInSlot(inventory, stack, 0);
				} else if (PC_RecipeRegistry.isFuel(stack)) {
					return storeItemInSlot(inventory, stack, 1);
				} else {
					return false;
				}
			}

			if (inventory instanceof TileEntityBrewingStand) {
				if (stack.itemID == Item.potion.itemID) {
					if (storeItemInSlot(inventory, stack, 0)) {
						return true;
					}

					if (storeItemInSlot(inventory, stack, 1)) {
						return true;
					}

					if (storeItemInSlot(inventory, stack, 2)) {
						return true;
					}

					return false;
				} else {
					if (stack.getItem().isPotionIngredient()) {
						return storeItemInSlot(inventory, stack, 3);
					}

					return false;
				}
			}

			if (inventory instanceof PC_ISpecialAccessInventory) {
				boolean result = ((PC_ISpecialAccessInventory) inventory)
						.insertStackIntoInventory(stack);
				return result;
			}

			return addItemStackToInventory(inventory, stack);
		}

		public static boolean isInventoryFull(IInventory inv) {
			if (inv == null) {
				return false;
			}

			if (inv instanceof PC_IStateReportingInventory) {
				return ((PC_IStateReportingInventory) inv).isContainerFull();
			}

			if (inv instanceof TileEntityFurnace) {
				return inv.getStackInSlot(1) != null
						&& inv.getStackInSlot(1).stackSize == Math.min(inv
								.getInventoryStackLimit(), inv
								.getStackInSlot(1).getMaxStackSize());
			}

			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) == null
						|| inv.getStackInSlot(i).stackSize < Math.min(inv
								.getInventoryStackLimit(), inv
								.getStackInSlot(i).getMaxStackSize())) {
					return false;
				}
			}

			return true;
		}

		public static boolean hasInventoryNoFreeSlots(IInventory inv) {
			if (inv == null) {
				return false;
			}

			if (inv instanceof PC_IStateReportingInventory) {
				return ((PC_IStateReportingInventory) inv)
						.hasContainerNoFreeSlots();
			}

			if (inv instanceof TileEntityFurnace) {
				return inv.getStackInSlot(1) != null;
			}

			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) == null) {
					return false;
				}
			}

			return true;
		}

		public static boolean isInventoryEmpty(IInventory inv) {
			if (inv == null) {
				return true;
			}

			if (inv instanceof PC_IStateReportingInventory) {
				return ((PC_IStateReportingInventory) inv).isContainerEmpty();
			}

			if (inv instanceof TileEntityFurnace) {
				return inv.getStackInSlot(1) == null;
			}

			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) != null) {
					return false;
				}
			}

			return true;
		}

		public static boolean moveStacksForce(IInventory from, IInventory to) {
			int copied = Math.min(from.getSizeInventory(),
					to.getSizeInventory());

			for (int i = 0; i < copied; i++) {
				to.setInventorySlotContents(i, from.getStackInSlot(i));
				from.setInventorySlotContents(i, null);
			}

			return from.getSizeInventory() <= to.getSizeInventory();
		}

		public static void moveStacks(IInventory from, IInventory to) {
			for (int i = 0; i < from.getSizeInventory(); i++) {
				if (from.getStackInSlot(i) != null) {
					addItemStackToInventory(to, from.getStackInSlot(i));

					if (from.getStackInSlot(i) != null
							&& from.getStackInSlot(i).stackSize <= 0) {
						from.setInventorySlotContents(i, null);
					}
				}
			}
		}

		public static int getPlayerArmourValue(EntityPlayerSP player) {
			return player.inventory.getTotalArmorValue();
		}

		public static int countPowerCrystals(IInventory inventory) {
			boolean[] foundTable = { false, false, false, false, false, false,
					false, false };

			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				if (inventory.getStackInSlot(i) != null
						&& inventory.getStackInSlot(i).itemID == PC_BlockRegistry
								.getPCBlockByName("PCco_BlockPowerCrystal").blockID) {
					foundTable[PC_MathHelper.clamp_int(inventory
							.getStackInSlot(i).getItemDamage(), 0, 7)] = true;
				}
			}

			int cnt = 0;

			for (int i = 0; i < 8; i++) {
				if (foundTable[i]) {
					cnt++;
				}
			}

			return cnt;
		}

		public static ItemStack[] groupStacks(ItemStack[] input) {
			List<ItemStack> list = Inventory.stacksToList(input);
			Inventory.groupStacks(list);
			return Inventory.stacksToArray(list);
		}

		public static void groupStacks(List<ItemStack> input) {
			if (input == null) {
				return;
			}

			for (ItemStack st1 : input) {
				if (st1 != null) {
					for (ItemStack st2 : input) {
						if (st2 != null && st2.isItemEqual(st1)) {
							int movedToFirst = Math.min(st2.stackSize, st1
									.getItem().getItemStackLimit()
									- st1.stackSize);

							if (movedToFirst <= 0) {
								break;
							}

							st1.stackSize += movedToFirst;
							st2.stackSize -= movedToFirst;
						}
					}
				}
			}

			ArrayList<ItemStack> copy = new ArrayList<ItemStack>(input);

			for (int i = copy.size() - 1; i >= 0; i--) {
				if (copy.get(i) == null || copy.get(i).stackSize <= 0) {
					input.remove(i);
				}
			}
		}

		public static List<ItemStack> stacksToList(ItemStack[] stacks) {
			ArrayList<ItemStack> myList = new ArrayList<ItemStack>();
			Collections.addAll(myList, stacks);
			return myList;
		}

		public static ItemStack[] stacksToArray(List<ItemStack> stacks) {
			return stacks.toArray(new ItemStack[stacks.size()]);
		}

		public static void loadInventoryFromNBT(NBTTagCompound outerTag,
				String invTagName, IInventory inventory) {
			NBTTagList nbttaglist = outerTag.getTagList(invTagName);

			for (int i = 0; i < nbttaglist.tagCount(); i++) {
				NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
						.tagAt(i);
				NBTBase nbtTag = nbttagcompound1.getTag("Slot");
				int j = -1;
				if (nbtTag instanceof NBTTagByte) {
					j = ((NBTTagByte) nbtTag).data & 0xff;
				} else if (nbtTag instanceof NBTTagInt) {
					j = ((NBTTagInt) nbtTag).data;
				}

				if (j >= 0 && j < inventory.getSizeInventory()) {
					inventory.setInventorySlotContents(j,
							ItemStack.loadItemStackFromNBT(nbttagcompound1));
				}
			}
		}

		public static void saveInventoryToNBT(NBTTagCompound outerTag,
				String invTagName, IInventory inventory) {
			NBTTagList nbttaglist = new NBTTagList();

			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				if (inventory.getStackInSlot(i) != null) {
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setInteger("Slot", i);
					inventory.getStackInSlot(i).writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}

			outerTag.setTag(invTagName, nbttaglist);
		}

		public static void dropInventoryContents(IInventory inventory,
				World world, PC_VecI pos) {
			Random random = new Random();

			if (inventory != null) {
				for (int i = 0; i < inventory.getSizeInventory(); i++) {
					if (inventory instanceof PC_ISpecialAccessInventory) {
						if (!((PC_ISpecialAccessInventory) inventory)
								.canDropStackFrom(i))
							continue;
					}
					ItemStack itemstack = inventory.getStackInSlot(i);
					inventory.setInventorySlotContents(i, null);
					ValueWriting.dropItemStack(world, itemstack, pos);
				}
			}
		}

		public static boolean hasInventoryPlaceFor(IInventory inv,
				ItemStack itemStack) {
			if (inv == null) {
				return false;
			}

			if (inv instanceof PC_IStateReportingInventory) {
				return ((PC_IStateReportingInventory) inv)
						.hasInventoryPlaceFor(itemStack);
			}

			if (inv instanceof TileEntityFurnace) {
				return inv.getStackInSlot(1) == null
						|| (inv.getStackInSlot(1).isItemEqual(itemStack) && inv
								.getStackInSlot(1).stackSize == Math.min(inv
								.getInventoryStackLimit(), inv
								.getStackInSlot(1).getMaxStackSize()));
			}

			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) == null
						|| (inv.getStackInSlot(i).isItemEqual(itemStack) && inv
								.getStackInSlot(i).stackSize < Math.min(inv
								.getInventoryStackLimit(), inv
								.getStackInSlot(i).getMaxStackSize()))) {
					return true;
				}
			}

			return false;
		}

		public static boolean isInventoryEmptyOf(IInventory inv,
				ItemStack itemStack) {
			if (inv == null) {
				return true;
			}

			if (inv instanceof PC_IStateReportingInventory) {
				return ((PC_IStateReportingInventory) inv)
						.isContainerEmptyOf(itemStack);
			}

			if (inv instanceof TileEntityFurnace) {
				return inv.getStackInSlot(1) == null
						|| !inv.getStackInSlot(1).isItemEqual(itemStack);
			}

			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i) != null
						&& inv.getStackInSlot(i).isItemEqual(itemStack)) {
					return false;
				}
			}

			return true;
		}

		public static int insetItemTo(ItemStack itemstack, IInventory inv,
				int start, int end) {
			ItemStack is = itemstack.copy();
			for (int i = start; i < end; i++) {
				ItemStack isis = inv.getStackInSlot(i);
				if (isis == null) {
					inv.setInventorySlotContents(i, itemstack.copy());
					itemstack.stackSize = 0;
				} else if (isis.isItemEqual(itemstack)) {
					int maxToInsert = Math.min(isis.getMaxStackSize(),
							inv.getInventoryStackLimit())
							- isis.stackSize;
					if (maxToInsert > 0) {
						if (maxToInsert > itemstack.stackSize) {
							maxToInsert = itemstack.stackSize;
						}
						isis.stackSize += maxToInsert;
						itemstack.stackSize -= maxToInsert;
					}
				}
				if (itemstack.stackSize == 0)
					break;
			}
			return itemstack.stackSize;
		}

		public static int useFuel(IInventory inv, int start, int end,
				World world, PC_VecI pos) {
			for (int i = start; i < end; i++) {
				ItemStack is = inv.getStackInSlot(i);
				int fuel = PC_RecipeRegistry.getFuelValue(is);
				if (fuel > 0) {
					inv.decrStackSize(i, 1);
					ItemStack container = GameInfo.getContainerItemStack(is);
					if (container != null) {
						insetItemTo(container, inv, start, end);
						if (container.stackSize > 0) {
							ValueWriting.dropItemStack(world, container, pos);
						}
					}
					return fuel;
				}
			}
			return 0;
		}

	}*/

	public static boolean create() {
		if (instance == null) {
			instance = new PC_Utils();
			PC_RegistryServer.create();
			return true;
		}

		return false;
	}

	protected World iGetWorldForDimension(int dimension) {
		return GameInfo.mcs().worldServerForDimension(dimension);
	}

	protected boolean client() {
		return false;
	}

	protected EnumGameType iGetGameTypeFor(EntityPlayer player) {
		return ((EntityPlayerMP) player).theItemInWorldManager.getGameType();
	}

	protected void iChatMsg(String msg, boolean clear) {
	}

	protected File iGetMCDirectory() {
		return GameInfo.mcs().getFile("");
	}

	protected int iAddArmor(String name) {
		return 0;
	}

	protected boolean iIsEntityFX(Entity entity) {
		return false;
	}

	@Override
	public boolean handleIncomingPacket(EntityPlayer player, Object[] o) {
		switch ((Integer) o[0]) {
		case SPAWNPARTICLEONBLOCKS:
			List<PC_Struct2<PC_VecI, Float>> blocks = (List<PC_Struct2<PC_VecI, Float>>) o[1];

			for (PC_Struct2<PC_VecI, Float> block : blocks) {
				for (int i = 0; i < 100 * block.b; i++) {
					Block b = Block.blocksList[GameInfo.getBID(player.worldObj,
							block.a)];
					int side = rand.nextInt(6);
					PC_VecF pos;
					PC_VecF move;
					AxisAlignedBB aabb = b.getSelectedBoundingBoxFromPool(
							player.worldObj, block.a.x, block.a.y, block.a.z);

					switch (side) {
					case 0:
						pos = new PC_VecF((float) (aabb.minX - block.a.x),
								rand.nextFloat(), rand.nextFloat());
						move = new PC_VecF(-rand.nextFloat() * 0.04f,
								rand.nextFloat() * 0.08f - 0.04f,
								rand.nextFloat() * 0.08f - 0.04f);
						break;

					case 1:
						pos = new PC_VecF((float) (aabb.maxX - block.a.x),
								rand.nextFloat(), rand.nextFloat());
						move = new PC_VecF(rand.nextFloat() * 0.04f,
								rand.nextFloat() * 0.08f - 0.04f,
								rand.nextFloat() * 0.08f - 0.04f);
						break;

					case 2:
						pos = new PC_VecF(rand.nextFloat(),
								(float) (aabb.minY - block.a.y),
								rand.nextFloat());
						move = new PC_VecF(rand.nextFloat() * 0.08f - 0.04f,
								-rand.nextFloat() * 0.04f,
								rand.nextFloat() * 0.08f - 0.04f);
						break;

					case 3:
						pos = new PC_VecF(rand.nextFloat(),
								(float) (aabb.minY - block.a.y),
								rand.nextFloat());
						move = new PC_VecF(rand.nextFloat() * 0.08f - 0.04f,
								rand.nextFloat() * 0.04f,
								rand.nextFloat() * 0.08f - 0.04f);
						break;

					case 4:
						pos = new PC_VecF(rand.nextFloat(), rand.nextFloat(),
								(float) (aabb.minZ - block.a.z));
						move = new PC_VecF(rand.nextFloat() * 0.08f - 0.04f,
								rand.nextFloat() * 0.08f - 0.04f,
								-rand.nextFloat() * 0.04f);
						break;

					case 5:
						pos = new PC_VecF(rand.nextFloat(), rand.nextFloat(),
								(float) (aabb.minZ - block.a.z));
						move = new PC_VecF(rand.nextFloat() * 0.08f - 0.04f,
								rand.nextFloat() * 0.08f - 0.04f,
								rand.nextFloat() * 0.04f);
						break;

					default:
						pos = new PC_VecF(rand.nextFloat(), rand.nextFloat(),
								rand.nextFloat());
						move = new PC_VecF(rand.nextFloat() * 0.08f - 0.04f,
								rand.nextFloat() * 0.08f - 0.04f,
								rand.nextFloat() * 0.08f - 0.04f);
						break;
					}

					ValueWriting.spawnParticle("PC_EntityLaserParticleFX",
							player.worldObj, new PC_VecF(block.a).add(pos),
							new PC_Color(0.6f, 0.6f, 1.0f), move, 0);
				}
			}

			break;
		}

		return false;
	}

	protected void iSpawnParticle(String name, Object[] o) {
	}

}
