package powercraft.management;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.World;

public class PC_Utils implements PC_IPacketHandler
{
    protected static PC_Utils instance;
    public static final int BACK = 0, LEFT = 1, RIGHT = 2, FRONT = 3, BOTTOM = 4, TOP = 5;

    protected static HashMap<EntityPlayer, List<String>> keyPressed = new HashMap<EntityPlayer, List<String>>();
    protected static int keyReverse;
    protected static HashMap<String, Object> objects = new HashMap<String, Object>();

    private static Random rand = new Random();
    private static HashMap<String, PC_Struct2<PC_IModule, PC_Property>> modules = new HashMap<String, PC_Struct2<PC_IModule, PC_Property>>();
    
    protected static boolean isSoundEnabled = false;
    
    public static String NO_HARVEST = "NO_HARVEST", HARVEST_STOP = "HARVEST_STOP", NO_BUILD = "NO_BUILD", SMOKE = "SMOKE",
    		NO_PICKUP = "NO_PICKUP", BEAMTRACER_STOP = "BEAMTRACER_STOP", PASSIVE = "PASSIVE";
    
    public static final int MSG_DEFAULT_NAME=1, MSG_BLOCK_FLAGS=2, MSG_ITEM_FLAGS=3, MSG_RENDER_INVENTORY_BLOCK=4, MSG_RENDER_WORLD_BLOCK=5,
    		MSG_SPAWNS_IN_CHUNK=6, MSG_BLOCKS_ON_SPAWN_POINT=7, MSG_SPAWN_POINT=8, MSG_SPAWN_POINT_METADATA=9, MSG_LOAD_FROM_CONFIG=10,
    		MSG_ON_HIT_BY_BEAM_TRACER=11, MSG_BURN_TIME=12, MSG_RECIVE_POWER=13, MSG_CAN_RECIVE_POWER=14, MSG_ON_ACTIVATOR_USED_ON_BLOCK = 15,
    		MSG_DONT_SHOW_IN_CRAFTING_TOOL=16, MSG_STR_MSG=17;
    
    protected PC_Utils(){
        PC_PacketHandler.registerPackethandler("PacketUtils", this);
    }

    public static boolean create()
    {
        if (instance == null)
        {
        	instance = new PC_Utils();
            return true;
        }

        return false;
    }

    protected void iRegisterTextureFiles(String[] textureFiles) {}

    public static void registerTextureFiles(String... textureFiles)
    {
        instance.iRegisterTextureFiles(textureFiles);
    }

    protected boolean client()
    {
        return false;
    }

    public static boolean isClient()
    {
        return instance.client();
    }

    public static boolean isServer()
    {
        return !instance.client();
    }

    protected void iRegisterLanguage(PC_IModule module, String lang, PC_Struct3<String, String, String[]>[] translations)
    {
    }

    public static void registerLanguageForLang(PC_IModule module, String lang, PC_Struct3<String, String, String[]>... translations)
    {
        instance.iRegisterLanguage(module, lang, translations);
    }

    public static void registerLanguage(PC_IModule module, PC_Struct3<String, String, String[]>... translations)
    {
        instance.registerLanguageForLang(module, "en_US", translations);
    }

    protected void iLoadLanguage(PC_IModule module) {}

    public static void loadLanguage(PC_IModule module)
    {
        instance.iLoadLanguage(module);
    }

    protected void iSaveLanguage(PC_IModule module) {}

    public static void saveLanguage(PC_IModule module)
    {
        instance.iSaveLanguage(module);
    }

    public static String tr(String identifier)
    {
        return StringTranslate.getInstance().translateKey(identifier).trim();
    }

    public static String tr(String identifier, String... replacements)
    {
        return StringTranslate.getInstance().translateKeyFormat(identifier, (Object[])replacements);
    }

    public static String isIDAvailable(int id, Class c)
    {
        if (id < 0)
        {
            return "Out of bounds";
        }

        if (id < Block.blocksList.length)
        {
            if (Block.blocksList[id] != null)
            {
                return Block.blocksList[id].getBlockName();
            }
        }
        else if (Block.class.isAssignableFrom(c))
        {
            return "Out of bounds";
        }

        if (id < Item.itemsList.length)
        {
            if (Item.itemsList[id] != null)
            {
                return Item.itemsList[id].getItemName();
            }

            return null;
        }

        return "Out of bounds";
    }

    public static boolean isIDAvailable(int id, Class c, boolean throwError) throws Exception
    {
        String name = isIDAvailable(id, c);

        if (!throwError || name == null)
        {
            return name == null;
        }

        String error = "ID " + id + " for class \"" + c.getName() + "\" already used by \"" + name + "\"";
        PC_Logger.severe(error);
        throw new Exception(error);
    }
    
    public static <t>t register(PC_IModule module, Class<t> c)
    {
    	PC_Property config = getConfig(module);

        if (PC_Block.class.isAssignableFrom(c))
        {
            return (t)register(module, (Class<PC_Block>)c, null, null);
        }
        else if (PC_Item.class.isAssignableFrom(c))
        {
            Class<PC_Item> itemClass = (Class<PC_Item>)c;

            try
            {
                PC_Item item = createClass(itemClass, new Class[] {}, new Object[] {});
                instance.objects.put(itemClass.getSimpleName(), item);
                item.setItemName(itemClass.getSimpleName());
                item.setModule(module);
                item.setTextureFile(getTerrainFile(module));

                item.msg(PC_Utils.MSG_LOAD_FROM_CONFIG, config.getProperty(itemClass.getSimpleName(), null, null));

                List<PC_Struct3<String, String, String[]>> l = (List<PC_Struct3<String, String, String[]>>)item.msg(MSG_DEFAULT_NAME, new ArrayList<PC_Struct3<String, String, String[]>>());
                if(l!=null){
                	registerLanguage(module, l.toArray(new PC_Struct3[0]));
                }
                return (t)item;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (PC_ItemArmor.class.isAssignableFrom(c))
        {
            Class<PC_ItemArmor> itemArmorClass = (Class<PC_ItemArmor>)c;

            try
            {
                PC_ItemArmor itemArmor = createClass(itemArmorClass, new Class[] {}, new Object[] {});
                instance.objects.put(itemArmorClass.getSimpleName(), itemArmor);
                itemArmor.setItemName(itemArmorClass.getSimpleName());
                itemArmor.setModule(module);

                itemArmor.msg(PC_Utils.MSG_LOAD_FROM_CONFIG, config.getProperty(itemArmorClass.getSimpleName(), null, null));
                
                List<PC_Struct3<String, String, String[]>> l = (List<PC_Struct3<String, String, String[]>>)itemArmor.msg(MSG_DEFAULT_NAME, new ArrayList<PC_Struct3<String, String, String[]>>());
                if(l!=null){
                	registerLanguage(module, l.toArray(new PC_Struct3[0]));
                }
                return (t)itemArmor;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        throw new IllegalArgumentException("3th parameter need to be a class witch extends PC_Block or PC_Item or PC_ItemArmor");
    }

    public static <t extends PC_Block>t register(PC_IModule module, Class<t> blockClass, Class c)
    {
        if (PC_ItemBlock.class.isAssignableFrom(c))
        {
            return register(module, blockClass, (Class<PC_ItemBlock>)c, null);
        }
        else if (PC_TileEntity.class.isAssignableFrom(c))
        {
            return register(module, blockClass, null, (Class<PC_TileEntity>)c);
        }

        throw new IllegalArgumentException("4th parameter need to be a class witch extends PC_ItemBlock or PC_TileEntity");
    }

    public static <t extends PC_Block>t register(PC_IModule module, Class<t> blockClass, Class <? extends PC_ItemBlock > itemBlockClass, Class <? extends PC_TileEntity > tileEntityClass)
    {
    	PC_Property config = getConfig(module);

        try
        {
            t block;
            t blockOff;

            if (blockClass.isAnnotationPresent(PC_Shining.class))
            {
                block = createClass(blockClass, new Class[] {boolean.class}, new Object[] {true});
                blockOff = createClass(blockClass, new Class[] {boolean.class}, new Object[] {false});
                setFieldsWithAnnotationTo(blockClass, PC_Shining.ON.class, blockClass, block);
                setFieldsWithAnnotationTo(blockClass, PC_Shining.OFF.class, blockClass, blockOff);
                blockOff.setBlockName(blockClass.getSimpleName());
                blockOff.setModule(module);
                blockOff.setTextureFile(getTerrainFile(module));
            }
            else
            {
                block = createClass(blockClass, new Class[] {}, new Object[] {});
            }

            instance.objects.put(blockClass.getSimpleName(), block);
            block.setBlockName(blockClass.getSimpleName());
            block.setModule(module);
            block.setTextureFile(getTerrainFile(module));

            block.msg(PC_Utils.MSG_LOAD_FROM_CONFIG, config.getProperty(blockClass.getSimpleName(), null, null));
            
            mod_PowerCraft.registerBlock(block, itemBlockClass);

            ItemBlock itemBlock = (ItemBlock)Item.itemsList[block.blockID];
            
            block.setItemBlock(itemBlock);
            
            if (itemBlockClass == null)
            {
                registerLanguage(module, new PC_Struct3<String, String, String[]>(block.getBlockName(), (String)block.msg(MSG_DEFAULT_NAME), null));
            }
            else
            {
                PC_ItemBlock ib = (PC_ItemBlock)itemBlock;
                ib.setModule(module);
                List<PC_Struct3<String, String, String[]>> l = (List<PC_Struct3<String, String, String[]>>)ib.msg(MSG_DEFAULT_NAME, new ArrayList<PC_Struct3<String, String, String[]>>());
                if(l!=null){
                	registerLanguage(module, l.toArray(new PC_Struct3[0]));
                }
            }
            
            if (tileEntityClass != null)
            {
                mod_PowerCraft.registerTileEntity(tileEntityClass);
                if(PC_ITileEntityRenderer.class.isAssignableFrom(tileEntityClass))
                	instance.iTileEntitySpecialRenderer(tileEntityClass);
            }

            return block;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

	protected void iTileEntitySpecialRenderer(Class <? extends TileEntity> tileEntityClass){
		
	}
    
    public static void setFieldsWithAnnotationTo(Class c, Class <? extends Annotation > annotationClass, Object obj, Object value)
    {
        Field fa[] = c.getDeclaredFields();

        for (Field f: fa)
        {
            if (f.isAnnotationPresent(annotationClass))
            {
                f.setAccessible(true);

                try
                {
                    f.set(obj, value);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object[] getFieldsWithAnnotationTo(Class c, Class <? extends Annotation > annotationClass, Object obj)
    {
        List<Object> l = new ArrayList<Object>();
        Field fa[] = c.getDeclaredFields();

        for (Field f: fa)
        {
            if (f.isAnnotationPresent(annotationClass))
            {
                f.setAccessible(true);

                try
                {
                    l.add(f.get(obj));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return l.toArray(new Object[0]);
    }

    public static void setBlockState(World world, int x, int y, int z, boolean on)
    {
        Block b = Block.blocksList[getBID(world, x, y, z)];

        if (b instanceof PC_Block)
        {
            int meta = getMD(world, x, y, z);
            PC_TileEntity te = getTE(world, x, y, z);
            Class c = b.getClass();

            if (c.isAnnotationPresent(PC_Shining.class))
            {
                Block bon = (Block)getFieldsWithAnnotationTo(c, PC_Shining.ON.class, c)[0];
                Block boff = (Block)getFieldsWithAnnotationTo(c, PC_Shining.OFF.class, c)[0];

                if ((b == bon && !on) || (b == boff && on))
                {
                    if (te != null)
                    {
                        te.lockInvalid(true);
                    }

                    if (on)
                    {
                        world.setBlockAndMetadataWithNotify(x, y, z, bon.blockID, meta);
                    }
                    else
                    {
                        world.setBlockAndMetadataWithNotify(x, y, z, boff.blockID, meta);
                    }

                    if (te != null)
                    {
                        world.setBlockTileEntity(x, y, z, te);
                        te.blockType = null;
                        te.getBlockType();
                        te.lockInvalid(false);
                    }
                    hugeUpdate(world, x, y, z, b.blockID);
                    
                }
            }
        }
    }

    public static <t>t createClass(Class<t> c, Class[] param, Object[] objects) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        return c.getConstructor(param).newInstance(objects);
    }

    public static boolean isSoundEnabled()
    {
        if (isServer())
        {
            return false;
        }

        return isSoundEnabled;
    }

    public static void setSoundEnabled(boolean enabled){
        if (isClient()){
        	isSoundEnabled = enabled;
        }
    }
    
    protected void iPlaySound(double x, double y, double z, String sound, float soundVolume, float pitch) {}

    public static void playSound(double x, double y, double z, String sound, float soundVolume, float pitch)
    {
        if (isSoundEnabled())
        {
            instance.iPlaySound(x, y, z, sound, soundVolume, pitch);
        }
    }

    protected static HashMap<String, Class> guis = new HashMap<String, Class>();

    public static void registerGres(String name, Class gui)
    {
        guis.put(name, gui);
    }

    public static void registerGresArray(Object[] o)
    {
        if (o == null)
        {
            return;
        }

        for (int i = 0; i < o.length; i += 2)
        {
            registerGres((String)o[i], (Class)o[i + 1]);
        }
    }

    protected void iOpenGres(String name, EntityPlayer player, Object[]o)
    {
        if (!(player instanceof EntityPlayerMP))
        {
            return;
        }

        int guiID = 0;

        try
        {
            Field var6 = EntityPlayerMP.class.getDeclaredFields()[16];
            var6.setAccessible(true);
            guiID = var6.getInt(player);
            guiID = guiID % 100 + 1;
            var6.setInt(player, guiID);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream sendData;

        try
        {
            sendData = new ObjectOutputStream(data);
            sendData.writeInt(PC_PacketHandler.PACKETGUI);
            sendData.writeObject(name);
            sendData.writeInt(guiID);
            sendData.writeObject(o);
            sendData.writeInt(PC_PacketHandler.PACKETGUI);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        PC_PacketHandler.sendToPlayer(player, data);

        if (guis.containsKey(name))
        {
            Class c = guis.get(name);

            if (PC_GresBaseWithInventory.class.isAssignableFrom(c))
            {
                try
                {
                    PC_GresBaseWithInventory bwi = createClass((Class<PC_GresBaseWithInventory>)c, new Class[] {EntityPlayer.class, Object[].class}, new Object[] {player, o});
                    player.openContainer = bwi;
                    player.openContainer.windowId = guiID;
                    player.openContainer.addCraftingToCrafters((EntityPlayerMP)player);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void openGres(String name, EntityPlayer player, Object...o)
    {
        instance.iOpenGres(name, player, o);
    }

    public static int getWorldDimension(World worldObj)
    {
        return worldObj.provider.dimensionId;
    }

    public static Object getPrivateValue(Class c, Object o, int i)
    {
        try
        {
            Field f = c.getDeclaredFields()[i];
            f.setAccessible(true);
            return f.get(o);
        }
        catch (IllegalAccessException e)
        {
            return null;
        }
    }

    public static boolean setPrivateValue(Class c, Object o, int i, Object v)
    {
        try
        {
            Field f = c.getDeclaredFields()[i];
            f.setAccessible(true);
            Field field_modifiers = Field.class.getDeclaredField("modifiers");
            field_modifiers.setAccessible(true);
            int modifier = field_modifiers.getInt(f);

            if ((modifier & Modifier.FINAL) != 0)
            {
                field_modifiers.setInt(f, modifier & ~Modifier.FINAL);
            }

            f.set(o, v);

            if ((modifier & Modifier.FINAL) != 0)
            {
                field_modifiers.setInt(f, modifier);
            }
            
            return true;
            
        }
        catch (Exception e)
        {
            PC_Logger.severe("setPrivateValue failed with " + c + ":" + o + ":" + i);
            return false;
        }
    }

    public static List<IRecipe> getRecipesForProduct(ItemStack prod)
    {
        List<IRecipe> recipes = new ArrayList<IRecipe>(CraftingManager.getInstance().getRecipeList());
        List<IRecipe> ret = new ArrayList<IRecipe>();

        for (IRecipe recipe: recipes)
        {
            try
            {
                if (recipe.getRecipeOutput().isItemEqual(prod) || (recipe.getRecipeOutput().itemID == prod.itemID && prod.getItemDamage() == -1))
                {
                    ret.add(recipe);
                }
            }
            catch (NullPointerException npe)
            {
                continue;
            }
        }

        return ret;
    }

    protected EnumGameType iGetGameTypeFor(EntityPlayer player)
    {
        return ((EntityPlayerMP)player).theItemInWorldManager.getGameType();
    }

    public static EnumGameType getGameTypeFor(EntityPlayer player)
    {
        return instance.iGetGameTypeFor(player);
    }

    public static boolean isCreative(EntityPlayer player)
    {
        return getGameTypeFor(player).isCreative();
    }

    public static boolean isFuel(ItemStack itemstack)
    {
        if (itemstack == null)
        {
            return false;
        }

        return PC_InvUtils.getFuelValue(itemstack, 1f) > 0;
    }

    public static boolean isSmeltable(ItemStack itemstack)
    {
        if (itemstack == null || FurnaceRecipes.smelting().getSmeltingResult(itemstack.getItem().shiftedIndex) == null)
        {
            return false;
        }

        return true;
    }

    protected void iChatMsg(String msg, boolean clear) {}

    public static void chatMsg(String msg, boolean clear)
    {
        instance.iChatMsg(msg, clear);
    }

    public static double ticksToSecs(int ticks)
    {
        return ticks * 0.05D;
    }

    public static int ticksToSecsInt(int ticks)
    {
        return Math.round(ticks * 0.05F);
    }

    public static int secsToTicks(double secs)
    {
        return (int)(secs * 20);
    }

    public static String doubleToString(double d)
    {
        return "" + d;
    }

    public static <t extends TileEntity>t getTE(IBlockAccess world, int x, int y, int z)
    {
        if (world != null)
        {
            TileEntity te = world.getBlockTileEntity(x, y, z);

            try
            {
                t tet = (t)te;
                return tet;
            }
            catch (ClassCastException e)
            {
                return null;
            }
        }

        return null;
    }

    public static <t extends TileEntity>t getTE(IBlockAccess world, PC_VecI vec)
    {
    	return getTE(world, vec.x, vec.y, vec.z);
    }
    
    public static <t extends TileEntity>t getTE(IBlockAccess world, int x, int y, int z, int blockID)
    {
        if (world != null)
        {
            if (getBID(world, x, y, z) == blockID)
            {
                TileEntity te = world.getBlockTileEntity(x, y, z);

                try
                {
                    t tet = (t)te;
                    return tet;
                }
                catch (ClassCastException e)
                {
                    return null;
                }
            }
        }

        return null;
    }

    public static int getBID(IBlockAccess world, int x, int y, int z)
    {
        if (world != null)
        {
            return world.getBlockId(x, y, z);
        }

        return 0;
    }

    public static int getBID(IBlockAccess world, PC_VecI vec)
    {
        return getBID(world, vec.x, vec.y, vec.z);
    }
    
    public static int getMD(IBlockAccess world, int x, int y, int z)
    {
        if (world != null)
        {
            return world.getBlockMetadata(x, y, z);
        }

        return 0;
    }

    public static int getMD(IBlockAccess world, PC_VecI vec)
    {
        return getMD(world, vec.x, vec.y, vec.z);
    }
    
    public static boolean getMD(World world, int x, int y, int z, int md)
    {
        if (world != null)
        {
            return world.setBlockMetadata(x, y, z, md);
        }

        return false;
    }
    
	public static boolean setMD(World world, PC_VecI pos, int md) {
		return getMD(world, pos.x, pos.y, pos.z, md);
	}
    
    protected boolean iIsPlacingReversed(EntityPlayer player)
    {
        return isKeyPressed(player, "keyReverse");
    }

    public static boolean isPlacingReversed(EntityPlayer player)
    {
        return instance.iIsPlacingReversed(player);
    }

    public static int reverseSide(int l)
    {
        if (l == 0)
        {
            l = 2;
        }
        else if (l == 2)
        {
            l = 0;
        }
        else if (l == 1)
        {
            l = 3;
        }
        else if (l == 3)
        {
            l = 1;
        }

        return l;
    }

    public static void hugeUpdate(World world, int x, int y, int z, int blockID)
    {
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
        notifyBlockOfNeighborChange(world, x, y + 1, z + 1, blockID);
        notifyBlockOfNeighborChange(world, x, y + 1, z - 1, blockID);
    }

    public static <t extends TileEntity>t setTE(World world, int x, int y, int z, t createTileEntity)
    {
        world.setBlockTileEntity(x, y, z, createTileEntity);
        return createTileEntity;
    }

    public static boolean poweredFromInput(World world, int x, int y, int z, int inp)
    {
        return poweredFromInput(world, x, y, z, inp, 0);
    }

    public static boolean poweredFromInput(World world, int x, int y, int z, int inp, int rotation)
    {
        if (world == null)
        {
            return false;
        }

        if (inp == 4)
        {
            boolean isProviding = (world.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0) || (world.getBlockId(x, y - 1, z) == Block.redstoneWire.blockID && world
                    .getBlockMetadata(x, y - 1, z) > 0));
            return isProviding;
        }

        if (inp == 5)
        {
            boolean isProviding = (world.isBlockIndirectlyProvidingPowerTo(x, y + 1, z, 1) || (world.getBlockId(x, y + 1, z) == Block.redstoneWire.blockID && world
                    .getBlockMetadata(x, y + 1, z) > 0));
            return isProviding;
        }

        int N0 = 0, N1 = 1, N2 = 2, N3 = 3;

        if (inp == 0)
        {
            N0 = 0;
            N1 = 1;
            N2 = 2;
            N3 = 3;
        }

        if (inp == 1)
        {
            N0 = 3;
            N1 = 0;
            N2 = 1;
            N3 = 2;
        }
        else if (inp == 2)
        {
            N0 = 1;
            N1 = 2;
            N2 = 3;
            N3 = 0;
        }
        else if (inp == 3)
        {
            N0 = 2;
            N1 = 3;
            N2 = 0;
            N3 = 1;
        }

        if (rotation == N0)
        {
            return (world.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3) || world.getBlockId(x, y, z + 1) == Block.redstoneWire.blockID
                    && world.getBlockMetadata(x, y, z + 1) > 0);
        }

        if (rotation == N1)
        {
            return (world.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4) || world.getBlockId(x - 1, y, z) == Block.redstoneWire.blockID
                    && world.getBlockMetadata(x - 1, y, z) > 0);
        }

        if (rotation == N2)
        {
            return (world.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2) || world.getBlockId(x, y, z - 1) == Block.redstoneWire.blockID
                    && world.getBlockMetadata(x, y, z - 1) > 0);
        }

        if (rotation == N3)
        {
            return (world.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5) || world.getBlockId(x + 1, y, z) == Block.redstoneWire.blockID
                    && world.getBlockMetadata(x + 1, y, z) > 0);
        }

        return false;
    }

    public static boolean isChestEmpty(World world, int x, int y, int z, ItemStack itemStack)
    {
        IInventory invAt = PC_InvUtils.getCompositeInventoryAt(world, new PC_VecI(x, y, z));

        if (invAt != null)
        {
            if (itemStack == null || itemStack.itemID == 0)
            {
                return PC_InvUtils.isInventoryEmpty(invAt);
            }
            else
            {
                return PC_InvUtils.isInventoryEmptyOf(invAt, itemStack);
            }
        }

        List<IInventory> list = world.getEntitiesWithinAABB(IInventory.class,
                AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(0.6D, 0.6D, 0.6D));

        if (list.size() >= 1)
        {
            if (itemStack == null || itemStack.itemID == 0)
            {
                return PC_InvUtils.isInventoryEmpty(list.get(0));
            }
            else
            {
                return PC_InvUtils.isInventoryEmptyOf(list.get(0), itemStack);
            }
        }

        List<PC_IInventoryWrapper> list2 = world.getEntitiesWithinAABB(PC_IInventoryWrapper.class,
                AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(0.6D, 0.6D, 0.6D));

        if (list2.size() >= 1)
        {
            if (list2.get(0).getInventory() != null)
            {
                if (itemStack == null || itemStack.itemID == 0)
                {
                    return PC_InvUtils.isInventoryEmpty(list2.get(0).getInventory());
                }
                else
                {
                    return PC_InvUtils.isInventoryEmptyOf(list2.get(0).getInventory(), itemStack);
                }
            }
        }

        return false;
    }

    public static boolean isChestFull(World world, int x, int y, int z, ItemStack itemStack)
    {
        IInventory invAt = PC_InvUtils.getCompositeInventoryAt(world, new PC_VecI(x, y, z));

        if (invAt != null)
        {
            if (itemStack == null || itemStack.itemID == 0)
            {
                return PC_InvUtils.hasInventoryNoFreeSlots(invAt);
            }
            else
            {
                return PC_InvUtils.hasInventoryPlaceFor(invAt, itemStack);
            }
        }

        List<IInventory> list = world.getEntitiesWithinAABB(IInventory.class,
                AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(0.6D, 0.6D, 0.6D));

        if (list.size() >= 1)
        {
            if (itemStack == null || itemStack.itemID == 0)
            {
                return PC_InvUtils.hasInventoryNoFreeSlots(list.get(0));
            }
            else
            {
                return PC_InvUtils.hasInventoryPlaceFor(list.get(0), itemStack);
            }
        }

        List<PC_IInventoryWrapper> list2 = world.getEntitiesWithinAABB(PC_IInventoryWrapper.class,
                AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(0.6D, 0.6D, 0.6D));

        if (list2.size() >= 1)
        {
            if (list2.get(0).getInventory() != null)
            {
                if (itemStack == null || itemStack.itemID == 0)
                {
                    return PC_InvUtils.hasInventoryNoFreeSlots(list2.get(0).getInventory());
                }
                else
                {
                    return PC_InvUtils.hasInventoryPlaceFor(list2.get(0).getInventory(), itemStack);
                }
            }
        }

        return false;
    }

    public static void spawnMobFromSpawner(World world, int x, int y, int z)
    {
        TileEntityMobSpawner te = PC_Utils.getTE(world, x, y, z, Block.mobSpawner.blockID);

        if (te != null)
        {
            spawnMobs(world, x, y, z, getMobID(te));
        }
    }

    public static String getMobID(TileEntityMobSpawner te){
    	return (String)getPrivateValue(TileEntityMobSpawner.class, te, 1);
    }
    
    public static void preventSpawnerSpawning(World world, int x, int y, int z)
    {
        TileEntityMobSpawner te = PC_Utils.getTE(world, x, y, z, Block.mobSpawner.blockID);

        if (te != null)
        {
            te.delay = 20;
        }
    }

    public static void spawnMobs(World world, int x, int y, int z, String type)
    {
        byte count = 5;
        boolean spawnParticles = world.getClosestPlayer(x + 0.5D, y + 0.5D, z + 0.5D, 16D) != null;

        for (int q = 0; q < count; q++)
        {
            EntityLiving entityliving = (EntityLiving) EntityList.createEntityByName(type, world);

            if (entityliving == null)
            {
                return;
            }

            int c = world.getEntitiesWithinAABB(entityliving.getClass(),
                    AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(8D, 4D, 8D)).size();

            if (c >= 6)
            {
                if (spawnParticles)
                {
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

            if (world.checkIfAABBIsClear(entityliving.boundingBox)
                    && world.getCollidingBoundingBoxes(entityliving, entityliving.boundingBox).size() == 0)
            {
                world.spawnEntityInWorld(entityliving);

                if (spawnParticles)
                {
                    world.playAuxSFX(2004, x, y, z, 0);
                    entityliving.spawnExplosionParticle();
                }

                return;
            }
        }
    }

    protected File iGetMCDirectory()
    {
        return mcs().getFile("");
    }

    public static MinecraftServer mcs()
    {
        return MinecraftServer.getServer();
    }

    public static File getMCDirectory()
    {
        return instance.iGetMCDirectory();
    }

    public static void notifyBlockOfNeighborChange(World world, int x, int y, int z, int blockId)
    {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];

        if (block != null)
        {
            block.onNeighborBlockChange(world, x, y, z, blockId);
        }
    }

    protected int iAddArmor(String name)
    {
        return 0;
    }

    public static int addArmor(String name)
    {
        return instance.iAddArmor(name);
    }

    protected boolean iIsEntityFX(Entity entity)
    {
        return false;
    }

    public static boolean isEntityFX(Entity entity)
    {
        return instance.iIsEntityFX(entity);
    }

    public static boolean isVersionNewer(String nVersion, String oVersion)
    {
        String saVersion[], saNewVersion[];
        saVersion = oVersion.split("\\.");
        saNewVersion = nVersion.split("\\.");

        for (int i = 0; i < saVersion.length; i++)
        {
            if (i >= saNewVersion.length)
            {
                return false;
            }

            int version = 0, newVersion = 0;
            String sVersion = "", sNewVersion = "";

            for (int j = 0; j < saVersion[i].length(); j++)
            {
                char c = saVersion[i].charAt(j);
                boolean num = true;

                if (Character.isDigit(c) && num)
                {
                    version *= 10;
                    version += c - '0';
                }
                else
                {
                    num = false;
                    sVersion += c;
                }
            }

            for (int j = 0; j < saNewVersion[i].length(); j++)
            {
                char c = saNewVersion[i].charAt(j);
                boolean num = true;

                if (Character.isDigit(c) && num)
                {
                    newVersion *= 10;
                    newVersion += c - '0';
                }
                else
                {
                    num = false;
                    sNewVersion += c;
                }
            }

            if (newVersion > version)
            {
                return true;
            }
            else if (newVersion < version)
            {
                return false;
            }
            else
            {
                int comp = sNewVersion.compareToIgnoreCase(sVersion);

                if (comp > 0)
                {
                    return false;
                }
                else if (comp < 0)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static void loadFromNBT(NBTTagCompound nbttagcompound, String string, PC_INBT nbt)
    {
        NBTTagCompound nbttag = nbttagcompound.getCompoundTag(string);
        nbt.readFromNBT(nbttag);
    }

    public static void saveToNBT(NBTTagCompound nbttagcompound, String string, PC_INBT nbt)
    {
        NBTTagCompound nbttag = new NBTTagCompound();
        nbt.writeToNBT(nbttag);
        nbttagcompound.setCompoundTag(string, nbttag);
    }

    protected boolean iIsKeyPressed(EntityPlayer player, String key)
    {
        if (!keyPressed.containsKey(player))
        {
            return false;
        }

        List<String> keyList = keyPressed.get(player);
        return keyList.contains(key);
    }

    public static boolean isKeyPressed(EntityPlayer player, String key)
    {
        return instance.iIsKeyPressed(player, key);
    }

    protected void iWatchForKey(String name, int key) {}

    public static void watchForKey(String name, int key)
    {
        instance.iWatchForKey(name, key);
    }

    public static int watchForKey(PC_Property config, String name, int key)
    {
    	key = config.getInt("key.name", key, new String[]{"Key for rotate placing"});
        watchForKey(name, key);
        return key;
    }

    public static void setReverseKey(PC_Property config)
    {
        instance.keyReverse = watchForKey(config, "keyReverse", 29);
    }

    public static PC_Block getPCBlockByName(String name)
    {
        if (instance.objects.containsKey(name))
        {
            Object o = instance.objects.get(name);

            if (o instanceof PC_Block)
            {
                return (PC_Block)o;
            }
        }

        return null;
    }

    public static PC_Item getPCItemByName(String name)
    {
        if (objects.containsKey(name))
        {
            Object o = objects.get(name);

            if (o instanceof PC_Item)
            {
                return (PC_Item)o;
            }
        }

        return null;
    }

    public static int getPCObjectIDByName(String name)
    {
        if (objects.containsKey(name))
        {
            Object o = objects.get(name);

            if (o instanceof Item)
            {
                return ((Item)o).shiftedIndex;
            }
            else if (o instanceof Block)
            {
                return ((Block)o).blockID;
            }
        }

        return 0;
    }
    
    protected static final int KEYEVENT = 0;
    protected static final int SPAWNPARTICLEONBLOCKS = 1;

    @Override
    public boolean handleIncomingPacket(EntityPlayer player, Object[] o)
    {
        switch ((Integer)o[0])
        {
            case KEYEVENT:
                List<String> keyList;

                if (keyPressed.containsKey(player))
                {
                    keyList = keyPressed.get(player);
                }
                else
                {
                    keyPressed.put(player, keyList = new ArrayList<String>());
                }

                String key = (String)o[2];

                if ((Boolean)o[1])
                {
                    if (!keyList.contains(key))
                    {
                        keyList.add(key);
                    }
                }
                else
                {
                    if (keyList.contains(key))
                    {
                        keyList.remove((Object)key);
                    }
                }

                break;

            case SPAWNPARTICLEONBLOCKS:
                List<PC_Struct2<PC_VecI, Float>> blocks = (List<PC_Struct2<PC_VecI, Float>>)o[1];

                for (PC_Struct2<PC_VecI, Float> block: blocks)
                {
                    for (int i = 0; i < 100 * block.b; i++)
                    {
                        Block b = Block.blocksList[getBID(player.worldObj, block.a)];
                        int side = rand.nextInt(6);
                        PC_VecF pos;
                        PC_VecF move;
                        AxisAlignedBB aabb = b.getSelectedBoundingBoxFromPool(player.worldObj, block.a.x, block.a.y, block.a.z);

                        switch (side)
                        {
                            case 0:
                                pos = new PC_VecF((float)(aabb.minX - block.a.x), rand.nextFloat(), rand.nextFloat());
                                move = new PC_VecF(-rand.nextFloat() * 0.04f, rand.nextFloat() * 0.08f - 0.04f, rand.nextFloat() * 0.08f - 0.04f);
                                break;

                            case 1:
                                pos = new PC_VecF((float)(aabb.maxX - block.a.x), rand.nextFloat(), rand.nextFloat());
                                move = new PC_VecF(rand.nextFloat() * 0.04f, rand.nextFloat() * 0.08f - 0.04f, rand.nextFloat() * 0.08f - 0.04f);
                                break;

                            case 2:
                                pos = new PC_VecF(rand.nextFloat(), (float)(aabb.minY - block.a.y), rand.nextFloat());
                                move = new PC_VecF(rand.nextFloat() * 0.08f - 0.04f, -rand.nextFloat() * 0.04f, rand.nextFloat() * 0.08f - 0.04f);
                                break;

                            case 3:
                                pos = new PC_VecF(rand.nextFloat(), (float)(aabb.minY - block.a.y), rand.nextFloat());
                                move = new PC_VecF(rand.nextFloat() * 0.08f - 0.04f, rand.nextFloat() * 0.04f, rand.nextFloat() * 0.08f - 0.04f);
                                break;

                            case 4:
                                pos = new PC_VecF(rand.nextFloat(), rand.nextFloat(), (float)(aabb.minZ - block.a.z));
                                move = new PC_VecF(rand.nextFloat() * 0.08f - 0.04f, rand.nextFloat() * 0.08f - 0.04f, -rand.nextFloat() * 0.04f);
                                break;

                            case 5:
                                pos = new PC_VecF(rand.nextFloat(), rand.nextFloat(), (float)(aabb.minZ - block.a.z));
                                move = new PC_VecF(rand.nextFloat() * 0.08f - 0.04f, rand.nextFloat() * 0.08f - 0.04f, rand.nextFloat() * 0.04f);
                                break;

                            default:
                                pos = new PC_VecF(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
                                move = new PC_VecF(rand.nextFloat() * 0.08f - 0.04f, rand.nextFloat() * 0.08f - 0.04f, rand.nextFloat() * 0.08f - 0.04f);
                                break;
                        }

                        PC_Utils.spawnParticle("PC_EntityLaserParticleFX", player.worldObj, new PC_VecF(block.a).add(pos), new PC_Color(0.6f, 0.6f, 1.0f), move, 0);
                    }
                }

                break;
        }

        return false;
    }

    public static List<Integer> parseIntList(String list)
    {
        if (list == null)
        {
            return null;
        }

        String[] parts = list.split(",");
        ArrayList<Integer> intList = new ArrayList<Integer>();

        for (String part : parts)
        {
            try
            {
                intList.add(Integer.parseInt(part));
            }
            catch (NumberFormatException e) {}
        }

        return intList;
    }

    public static boolean areObjectsEqual(Object a, Object b)
    {
        return a == null ? b == null : a.equals(b);
    }

    public static Block getBlock(World world, int x, int y, int z)
    {
        return Block.blocksList[PC_Utils.getBID(world, x, y, z)];
    }

    public static ItemStack extractAndRemoveChest(World world, PC_VecI pos)
    {
        if (hasFlag(world, pos, NO_HARVEST))
        {
            return null;
        }

        TileEntity tec = getTE(world, pos);

        if (tec == null)
        {
            return null;
        }

        ItemStack stack = new ItemStack(getPCObjectIDByName("PCco_ItemBlockSaver"), 1, 0);
        NBTTagCompound blocktag = new NBTTagCompound();
        getTE(world, pos).writeToNBT(blocktag);
        int dmg = getBID(world, pos);
        stack.setItemDamage(dmg);
        blocktag.setInteger("BlockMeta", getMD(world, pos));
        stack.setTagCompound(blocktag);

        if (tec instanceof IInventory)
        {
            IInventory ic = (IInventory) tec;

            for (int i = 0; i < ic.getSizeInventory(); i++)
            {
                ic.setInventorySlotContents(i, null);
            }
        }

        tec.invalidate();
        setBID(world, pos, 0, 0);
        return stack;
    }

    public static boolean setBID(World world, int x, int y, int z, int id, int meta) {
    	return world.setBlockAndMetadataWithNotify(x, y, z, id, meta);
	}
    
    public static boolean setBID(World world, PC_VecI pos, int id, int meta) {
    	return setBID(world, pos.x, pos.y, pos.z, id, meta);
	}

    public static void notifyNeighbour(World world, int x, int y, int z) {
    	world.notifyBlocksOfNeighborChange(x, y, z, getBID(world, x, y, z));
	}
    
    public static void notifyNeighbour(World world, PC_VecI pos) {
    	notifyNeighbour(world, pos.x, pos.y, pos.z);
	}
    
    public static boolean setBIDNoNotify(World world, int x, int y, int z, int id, int meta) {
    	return world.setBlockAndMetadata(x, y, z, id, meta);
	}
    
    public static boolean setBIDNoNotify(World world, PC_VecI pos, int id, int meta) {
    	return setBID(world, pos.x, pos.y, pos.z, id, meta);
	}
    
    public static boolean setMDNoNotify(World world, int x, int y, int z, int meta) {
    	return world.setBlockMetadata(x, y, z, meta);
	}
    
    public static boolean setMDNoNotify(World world, PC_VecI pos, int meta) {
    	return setMDNoNotify(world, pos.x, pos.y, pos.z, meta);
	}
    
	protected void iSpawnParticle(String name, Object[] o)
    {
    }

    public static void spawnParticle(String name, Object...o)
    {
        instance.iSpawnParticle(name, o);
    }

    public static Constructor findBestConstructor(Class c, Class[] cp)
    {
        Constructor[] constructors = c.getConstructors();

        for (Constructor constructor: constructors)
        {
            Class[] cep = constructor.getParameterTypes();

            if ((cep == null && cp == null) || (cep == null && cp.length == 0) || (cep.length == 0 && cp == null))
            {
                return constructor;
            }

            if (cep.length == cp.length)
            {
                boolean ok = true;

                for (int i = 0; i < cep.length; i++)
                {
                    if (cep[i].isPrimitive())
                    {
                        if (cep[i].equals(boolean.class))
                        {
                            cep[i] = Boolean.class;
                        }
                        else if (cep[i].equals(int.class))
                        {
                            cep[i] = Integer.class;
                        }
                        else if (cep[i].equals(float.class))
                        {
                            cep[i] = Float.class;
                        }
                        else if (cep[i].equals(double.class))
                        {
                            cep[i] = Double.class;
                        }
                        else if (cep[i].equals(long.class))
                        {
                            cep[i] = Long.class;
                        }
                        else if (cep[i].equals(char.class))
                        {
                            cep[i] = Integer.class;
                        }
                        else if (cep[i].equals(short.class))
                        {
                            cep[i] = Short.class;
                        }
                    }

                    if (!cep[i].isAssignableFrom(cp[i]))
                    {
                        ok = false;
                        break;
                    }
                }

                if (ok)
                {
                    return constructor;
                }
            }
        }

        return null;
    }

    public static float giveConductorValueFor(Block b)
    {
        if (b == null)
        {
            return 0.0f;
        }

        if (b.blockID == Block.blockSteel.blockID)
        {
            return 0.9f;
        }

        if (b.blockID == Block.blockGold.blockID)
        {
            return 0.7f;
        }

        return 0.0f;
    }

    private static void searchPowerReceiverConnectedTo(World world, int x, int y, int z, List<PC_Struct3<PC_VecI, PC_IMSG, Float>> receivers, List<PC_Struct2<PC_VecI, Float>> allpos, float power)
    {
        Block b = getBlock(world, x, y, z);
        PC_VecI pos = new PC_VecI(x, y, z);
        PC_Struct2<PC_VecI, Float> oldStruct = null;

        for (PC_Struct2<PC_VecI, Float> s: allpos)
        {
            if (s.a.equals(pos))
            {
                oldStruct = s;
                break;
            }
        }

        if (b instanceof PC_IMSG)
        {
        	Object o = ((PC_IMSG) b).msg(PC_Utils.MSG_CAN_RECIVE_POWER, b);
        	if(o instanceof Boolean && ((Boolean)o) == true){
	            if (oldStruct == null)
	            {
	                receivers.add(new PC_Struct3<PC_VecI, PC_IMSG, Float>(pos, (PC_IMSG)b, power));
	            }
        	}
            return;
        }

        float value = giveConductorValueFor(b);

        if (value < 0.01f)
        {
            return;
        }

        if (oldStruct == null)
        {
            oldStruct = new PC_Struct2<PC_VecI, Float>(pos, 0.0f);
            allpos.add(oldStruct);
        }

        if (power > oldStruct.b)
        {
            oldStruct.b = power;
            power *= value;

            if (power < 0.01f)
            {
                return;
            }

            searchPowerReceiverConnectedTo(world, x + 1, y, z, receivers, allpos, power);
            searchPowerReceiverConnectedTo(world, x - 1, y, z, receivers, allpos, power);
            searchPowerReceiverConnectedTo(world, x, y + 1, z, receivers, allpos, power);
            searchPowerReceiverConnectedTo(world, x, y - 1, z, receivers, allpos, power);
            searchPowerReceiverConnectedTo(world, x, y, z + 1, receivers, allpos, power);
            searchPowerReceiverConnectedTo(world, x, y, z - 1, receivers, allpos, power);
        }
    }

    public static List<PC_Struct3<PC_VecI, PC_IMSG, Float>> getPowerReceiverConnectedTo(World world, int x, int y, int z)
    {
        Random rand = new Random();
        List<PC_Struct3<PC_VecI, PC_IMSG, Float>> receivers = new ArrayList<PC_Struct3<PC_VecI, PC_IMSG, Float>>();
        List<PC_Struct2<PC_VecI, Float>> blocks = new ArrayList<PC_Struct2<PC_VecI, Float>>();
        searchPowerReceiverConnectedTo(world, x, y, z, receivers, blocks, 1.0f);
        PC_PacketHandler.sendToPacketHandler(true, world, "PacketUtils", SPAWNPARTICLEONBLOCKS, blocks);
        return receivers;
    }

    public static void givePowerToBlock(World world, int x, int y, int z, float power)
    {
        List<PC_Struct3<PC_VecI, PC_IMSG, Float>> powerReceivers = getPowerReceiverConnectedTo(world, x, y, z);
        float receivers = powerReceivers.size();

        for (PC_Struct3<PC_VecI, PC_IMSG, Float> receiver: powerReceivers)
        {
            receiver.b.msg(PC_Utils.MSG_RECIVE_POWER, world, receiver.a.x, receiver.a.y, receiver.a.z, power / receivers * receiver.c);
        }
    }

    public static void dropItemStack(World world, ItemStack itemstack, PC_VecI pos)
    {
        if (itemstack != null && !world.isRemote)
        {
            float f = rand.nextFloat() * 0.8F + 0.1F;
            float f1 = rand.nextFloat() * 0.8F + 0.1F;
            float f2 = rand.nextFloat() * 0.8F + 0.1F;

            while (itemstack.stackSize > 0)
            {
                int j = rand.nextInt(21) + 10;

                if (j > itemstack.stackSize)
                {
                    j = itemstack.stackSize;
                }

                itemstack.stackSize -= j;
                EntityItem entityitem = new EntityItem(world, pos.x + f, pos.y + f1, pos.z + f2, new ItemStack(itemstack.itemID, j,
                        itemstack.getItemDamage()));

                if (itemstack.hasTagCompound())
                {
                    entityitem.item.setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                }

                float f3 = 0.05F;
                entityitem.motionX = (float) rand.nextGaussian() * f3;
                entityitem.motionY = (float) rand.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float) rand.nextGaussian() * f3;
                world.spawnEntityInWorld(entityitem);
            }
        }
    }

	public static boolean isBlock(IBlockAccess world, PC_VecI pos, String...names) {
		int blockID = getBID(world, pos.x, pos.y, pos.z);
		for(String name:names)
			if(blockID == getPCObjectIDByName(name))
				return true;
		return false;
	}
	
	public static boolean hasFlag(World world, PC_VecI pos, String flag) {
		Block b = getBlock(world, pos.x, pos.y, pos.z);
		if(b instanceof PC_IMSG){
			List<String> list = (List<String>)((PC_IMSG)b).msg(MSG_BLOCK_FLAGS, world, pos, new ArrayList<String>());
			if(list != null){
				return list.contains(flag);
			}
		}
		return false;
	}
	
	public static boolean hasFlag(ItemStack is, String flag) {
		if(is.itemID<Block.blocksList.length){
			Block b = Block.blocksList[is.itemID];
			if(b instanceof PC_IMSG){
				List<String> list = (List<String>) ((PC_IMSG)b).msg(MSG_ITEM_FLAGS, is, new ArrayList<String>());
				if(list != null){
					return list.contains(flag);
				}
			}
		}

		Item i = is.getItem();
		if(i instanceof PC_IMSG){
			List<String> list = (List<String>) ((PC_IMSG) i).msg(MSG_ITEM_FLAGS, is, new ArrayList<String>());
			if(list != null){
				return list.contains(flag);
			}
		}

		return false;
	}

	public static Block getBlock(World world, PC_VecI pos) {
		return getBlock(world, pos.x, pos.y, pos.z);
	}

	public static PC_IModule getModule(Object o) {
		if(o instanceof PC_IItemInfo){
			return ((PC_IItemInfo) o).getModule();
		}
		return null;
	}
	
	public static File getPowerCraftFile() {
		return createFile(PC_Utils.getMCDirectory(), "PowerCraft");
    }

	public static int getValueNum(Class c, String n) {
		Field[] fields = c.getDeclaredFields();
		for(int i=0; i<fields.length; i++){
			if(fields[i].getName().equals(n)){
				return i;
			}
		}
		return -1;
	}
	
	public static boolean loadPCObjectsIDs(File worldDirectory){
		File file = new File(worldDirectory, "powercraft.dat");
		if(!file.exists())
			return false;
		try{
			 NBTTagCompound nbttag = CompressedStreamTools.readCompressed(new FileInputStream(file));
			 
			 for(Entry<String, Object>e: objects.entrySet()){
				 if(nbttag.hasKey(e.getKey())){
					 int id = nbttag.getInteger(e.getKey());
					 setPCObjectID(e.getValue(), id);
				 }
			 }
			 
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
		return true;
	}
	
	public static void savePCObjectsIDs(File worldDirectory){
        NBTTagCompound nbttag = new NBTTagCompound("PowerCraftIDs");

        for(String key:objects.keySet()){
        	nbttag.setInteger(key, getPCObjectIDByName(key));
        }
        	
        try
        {
            File file = new File(worldDirectory, "powercraft.dat");
            CompressedStreamTools.writeCompressed(nbttag, new FileOutputStream(file));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	
	private static void setPCObjectID(Object o, int id){
		if(o instanceof PC_Item){
			 ((PC_Item) o).setItemID(id);
		 }else if(o instanceof PC_Block){
			 ((PC_Block) o).setBlockID(id);
		 }
	}
	
	public static void resetPCObjectsIDs(){
		for(Object o: objects.values()){
			setPCObjectID(o, -1);
		}
		for(Object o: objects.values()){
			int id = -1;
			if(o instanceof PC_Item){
				id = getFreeItemID();
			}else if(o instanceof PC_Block){
				id = getFreeBlockID();
			}
			setPCObjectID(o, id);
		}
	}
	
	public static int getFreeBlockID() {
		for(int i=1; i<Block.blocksList.length; i++){
			if(Block.blocksList[i] == null)
				return i;
		}
		return -1;
	}
	
	public static int getFreeItemID() {
		for(int i=Block.blocksList.length; i<Item.itemsList.length; i++){
			if(Item.itemsList[i] == null)
				return i;
		}
		return -1;
	}

	public static File createFile(File pfile, String name) {
		File file = new File(pfile, name);
    	if(!file.exists())
    		file.mkdirs();
        return file;
	}
	
	public static void registerModule(PC_IModule module){
		PC_Property config = null;
		File f = new File(PC_Utils.getMCDirectory(), "config/PowerCraft-"+module.getName()+".cfg");
		if(f.exists()){
			try {
				InputStream is = new FileInputStream(f);
				config = PC_Property.loadFromFile(is);
			} catch (FileNotFoundException e) {
				PC_Logger.severe("Can't find File "+f);
			}
		}
		if(config==null){
			config = new PC_Property(null);
		}
		modules.put(module.getName(), new PC_Struct2<PC_IModule, PC_Property>(module, config));
	}
	
	public static String getTextureDirectory(PC_IModule module){
		return "/powercraft/" + module.getName().toLowerCase() + "/textures/";
	}

	public static String getTerrainFile(PC_IModule module){
		return getTextureDirectory(module) + "tiles.png";
	}
	
	public static PC_Property getConfig(PC_IModule module) {
		if(modules.containsKey(module.getName())){
			return modules.get(module.getName()).b;
		}
		return null;
	}
	
	public static void saveConfig(PC_IModule module) {
		PC_Property config = getConfig(module);
		File f = new File(PC_Utils.getMCDirectory(), "config/PowerCraft-"+module.getName()+".cfg");
		if(config!=null){
			try {
				OutputStream os = new FileOutputStream(f);
				config.save(os);
			} catch (FileNotFoundException e) {
				PC_Logger.severe("Can't find File "+f);
			}
		}
	}
	
	public static PC_IModule getModule(String name){
		if(modules.containsKey(name)){
			return modules.get(name).a;
		}
		return null;
	}
	
	public static List<PC_IModule> getModules(){
		List<PC_IModule> list = new ArrayList<PC_IModule>();
		for(PC_Struct2<PC_IModule, PC_Property> s:modules.values()){
			list.add(s.a);
		}
		return list;
	}

	public static String getPowerCraftLoaderImageDir(){
		return "/powercraft/management/textures/";
	}
	
	public static String getGresImgDir() {
		return getPowerCraftLoaderImageDir() + "gres/";
	}
	
	public static boolean usingForge() {
		return true;
	}
	
	public static List<Object> getRgisterdObjects() {
		return new ArrayList<Object>(objects.values());
	}
	
	public static boolean isPoweredDirectly(World world, int x, int y, int z){
		 return world.isBlockGettingPowered(x, y, z);
	}
	
	public static boolean isPoweredDirectly(World world, PC_VecI pos){
        return isPoweredDirectly(world, pos.x, pos.y, pos.z);
    }

	public static boolean isPoweredIndirectly(World world, int x, int y, int z){
        return world.isBlockIndirectlyGettingPowered(x, y, z);
    }
	
    public static boolean isPoweredIndirectly(World world, PC_VecI pos){
        return isPoweredIndirectly(world, pos.x, pos.y, pos.z);
    }
	
}
