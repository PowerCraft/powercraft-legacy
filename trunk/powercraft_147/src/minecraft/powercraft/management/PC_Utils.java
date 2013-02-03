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
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.EntityRegistry;

public class PC_Utils implements PC_IPacketHandler
{
    protected static PC_Utils instance;
    public static final int BACK = 0, LEFT = 1, RIGHT = 2, FRONT = 3, BOTTOM = 4, TOP = 5;

    protected static HashMap<String, Class> guis = new HashMap<String, Class>();
    protected static final int KEYEVENT = 0;
    protected static final int SPAWNPARTICLEONBLOCKS = 1;
    
    protected static HashMap<EntityPlayer, List<String>> keyPressed = new HashMap<EntityPlayer, List<String>>();
    protected static int keyReverse;
    protected static HashMap<String, Object> objects = new HashMap<String, Object>();

    protected static HashMap<String, PC_IDataHandler> dataHandlers = new HashMap<String, PC_IDataHandler>();
    
    protected static List<PC_IMSG> msgObjects = new ArrayList<PC_IMSG>();
    
    private static Random rand = new Random();
    private static HashMap<String, PC_Struct2<PC_IModule, PC_Property>> modules = new HashMap<String, PC_Struct2<PC_IModule, PC_Property>>();
    
    public static String NO_HARVEST = "NO_HARVEST", HARVEST_STOP = "HARVEST_STOP", NO_BUILD = "NO_BUILD", SMOKE = "SMOKE",
    		NO_PICKUP = "NO_PICKUP", BEAMTRACER_STOP = "BEAMTRACER_STOP", PASSIVE = "PASSIVE";
    
    public static final int MSG_DEFAULT_NAME=1, MSG_BLOCK_FLAGS=2, MSG_ITEM_FLAGS=3, MSG_RENDER_INVENTORY_BLOCK=4, MSG_RENDER_WORLD_BLOCK=5,
    		MSG_SPAWNS_IN_CHUNK=6, MSG_BLOCKS_ON_SPAWN_POINT=7, MSG_SPAWN_POINT=8, MSG_SPAWN_POINT_METADATA=9, MSG_LOAD_FROM_CONFIG=10,
    		MSG_ON_HIT_BY_BEAM_TRACER=11, MSG_BURN_TIME=12, MSG_RECIVE_POWER=13, MSG_CAN_RECIVE_POWER=14, MSG_ON_ACTIVATOR_USED_ON_BLOCK = 15,
    		MSG_DONT_SHOW_IN_CRAFTING_TOOL=16, MSG_STR_MSG=17, MSG_RENDER_ITEM_HORIZONTAL=18, MSG_ROTATION=19, MSG_RATING=20, MSG_CONDUCTIVITY = 21,
    		MSG_TICK_EVENT = 22, MSG_LOAD_WORLD=23, MSG_GET_PROVIDET_GLOBAL_FUNCTIONS=24;
    
    protected PC_Utils(){
        PC_PacketHandler.registerPackethandler("PacketUtils", this);
    }

    public static class Lang{

		public static void registerLanguageForLang(PC_IModule module, String lang, PC_Struct3<String, String, String[]>... translations)
		{
		    PC_Utils.instance.iRegisterLanguage(module, lang, translations);
		}

		public static void registerLanguage(PC_IModule module, PC_Struct3<String, String, String[]>... translations)
		{
		    PC_Utils.Lang.registerLanguageForLang(module, "en_US", translations);
		}
		public static void loadLanguage(PC_IModule module)
		{
		    PC_Utils.instance.iLoadLanguage(module);
		}
		public static void saveLanguage(PC_IModule module)
		{
		    PC_Utils.instance.iSaveLanguage(module);
		}
		public static String tr(String identifier)
		{
		    return StringTranslate.getInstance().translateKey(identifier).trim();
		}
		public static String tr(String identifier, String... replacements)
		{
		    return StringTranslate.getInstance().translateKeyFormat(identifier, (Object[])replacements);
		}
    	
    }

    public static class ModuleLoader{
    	
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
		    String name = PC_Utils.ModuleLoader.isIDAvailable(id, c);
		
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
			PC_Property config = SaveHandler.getConfig(module);
		
		    if (PC_Block.class.isAssignableFrom(c))
		    {
		        return (t)ModuleLoader.register(module, (Class<PC_Block>)c, null, null);
		    }
		    else if (PC_Item.class.isAssignableFrom(c))
		    {
		        Class<PC_Item> itemClass = (Class<PC_Item>)c;
		
		        try
		        {
		        	config = config.getProperty(itemClass.getSimpleName(), null, null);
		        	int id = config.getInt("defaultID", 0);
		        	if(!ModuleLoader.isItemIDFree(id)){
		        		id = ModuleLoader.getFreeItemID();
		        		config.setInt("defaultID", id);
		        	}
		            PC_Item item = ValueWriting.createClass(itemClass, new Class[] {int.class}, new Object[] {id});
		            ModuleInfo.registerMSGObject(item);
		            PC_Utils.objects.put(itemClass.getSimpleName(), item);
		            item.setItemName(itemClass.getSimpleName());
		            item.setModule(module);
		            item.setTextureFile(ModuleInfo.getTerrainFile(module));
		
		            item.msg(PC_Utils.MSG_LOAD_FROM_CONFIG, config);
		
		            List<PC_Struct3<String, String, String[]>> l = (List<PC_Struct3<String, String, String[]>>)item.msg(PC_Utils.MSG_DEFAULT_NAME, new ArrayList<PC_Struct3<String, String, String[]>>());
		            if(l!=null){
		            	PC_Utils.Lang.registerLanguage(module, l.toArray(new PC_Struct3[0]));
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
		        	config = config.getProperty(itemArmorClass.getSimpleName(), null, null);
		        	int id = config.getInt("defaultID", 0);
		        	if(!ModuleLoader.isItemIDFree(id)){
		        		id = ModuleLoader.getFreeItemID();
		        		config.setInt("defaultID", id);
		        	}
		            PC_ItemArmor itemArmor = ValueWriting.createClass(itemArmorClass, new Class[] {int.class}, new Object[] {id});
		            ModuleInfo.registerMSGObject(itemArmor);
		            PC_Utils.objects.put(itemArmorClass.getSimpleName(), itemArmor);
		            itemArmor.setItemName(itemArmorClass.getSimpleName());
		            itemArmor.setModule(module);
		
		            itemArmor.msg(PC_Utils.MSG_LOAD_FROM_CONFIG, config);
		            
		            List<PC_Struct3<String, String, String[]>> l = (List<PC_Struct3<String, String, String[]>>)itemArmor.msg(PC_Utils.MSG_DEFAULT_NAME, new ArrayList<PC_Struct3<String, String, String[]>>());
		            if(l!=null){
		            	PC_Utils.Lang.registerLanguage(module, l.toArray(new PC_Struct3[0]));
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
		        return ModuleLoader.register(module, blockClass, (Class<PC_ItemBlock>)c, null);
		    }
		    else if (PC_TileEntity.class.isAssignableFrom(c))
		    {
		        return ModuleLoader.register(module, blockClass, null, (Class<PC_TileEntity>)c);
		    }
		
		    throw new IllegalArgumentException("4th parameter need to be a class witch extends PC_ItemBlock or PC_TileEntity");
		}
		public static <t extends PC_Block>t register(PC_IModule module, Class<t> blockClass, Class <? extends PC_ItemBlock > itemBlockClass, Class <? extends PC_TileEntity > tileEntityClass)
		{
			PC_Property config = SaveHandler.getConfig(module);
		
		    try
		    {
		    	config = config.getProperty(blockClass.getSimpleName(), null, null);
		    	
		        t block;
		        t blockOff;
		        
		        if (blockClass.isAnnotationPresent(PC_Shining.class))
		        {
		        	
		         	int idOn = config.getInt("defaultID.on", 0);
		         	if(!ModuleLoader.isBlockIDFree(idOn)){
		         		idOn = ModuleLoader.getFreeBlockID();
		         		config.setInt("defaultID.on", idOn);
		         	}
		        	
		            block = ValueWriting.createClass(blockClass, new Class[] {int.class, boolean.class}, new Object[] {idOn, true});
		            
		            int idOff = config.getInt("defaultID.off", 0);
		         	if(!ModuleLoader.isBlockIDFree(idOff)){
		         		idOff = ModuleLoader.getFreeBlockID();
		         		config.setInt("defaultID.off", idOff);
		         	}
		            
		            blockOff = ValueWriting.createClass(blockClass, new Class[] {int.class, boolean.class}, new Object[] {idOff, false});
		            ValueWriting.setFieldsWithAnnotationTo(blockClass, PC_Shining.ON.class, blockClass, block);
		            ValueWriting.setFieldsWithAnnotationTo(blockClass, PC_Shining.OFF.class, blockClass, blockOff);
		            blockOff.setBlockName(blockClass.getSimpleName());
		            blockOff.setModule(module);
		            blockOff.setTextureFile(ModuleInfo.getTerrainFile(module));
		            ModuleInfo.registerMSGObject(blockOff);
		            PC_Utils.objects.put(blockClass.getSimpleName()+".Off", blockOff);
		            mod_PowerCraft.registerBlock(blockOff, null);
		            ItemBlock itemBlock = (ItemBlock)Item.itemsList[blockOff.blockID];
		            
		            blockOff.setItemBlock(itemBlock);
		        }
		        else
		        {
		         	int id = config.getInt("defaultID", 0);
		         	if(!ModuleLoader.isBlockIDFree(id)){
		         		id = ModuleLoader.getFreeBlockID();
		         		config.setInt("defaultID", id);
		         	}
		            block = ValueWriting.createClass(blockClass, new Class[] {int.class}, new Object[] {id});
		        }
		
		        ModuleInfo.registerMSGObject(block);
		        PC_Utils.objects.put(blockClass.getSimpleName(), block);
		        block.setBlockName(blockClass.getSimpleName());
		        block.setModule(module);
		        block.setTextureFile(ModuleInfo.getTerrainFile(module));
		
		        block.msg(PC_Utils.MSG_LOAD_FROM_CONFIG, config);
		        
		        mod_PowerCraft.registerBlock(block, itemBlockClass);
		
		        ItemBlock itemBlock = (ItemBlock)Item.itemsList[block.blockID];
		        
		        block.setItemBlock(itemBlock);
		        
		        if (itemBlockClass == null)
		        {
		            PC_Utils.Lang.registerLanguage(module, new PC_Struct3<String, String, String[]>(block.getBlockName(), (String)block.msg(PC_Utils.MSG_DEFAULT_NAME), null));
		        }
		        else
		        {
		            PC_ItemBlock ib = (PC_ItemBlock)itemBlock;
		            ib.setModule(module);
		            List<PC_Struct3<String, String, String[]>> l = (List<PC_Struct3<String, String, String[]>>)ib.msg(PC_Utils.MSG_DEFAULT_NAME, new ArrayList<PC_Struct3<String, String, String[]>>());
		            if(l!=null){
		            	PC_Utils.Lang.registerLanguage(module, l.toArray(new PC_Struct3[0]));
		            }
		        }
		        
		        if (tileEntityClass != null)
		        {
		            mod_PowerCraft.registerTileEntity(tileEntityClass);
		            if(PC_ITileEntityRenderer.class.isAssignableFrom(tileEntityClass))
		            	PC_Utils.instance.iTileEntitySpecialRenderer(tileEntityClass);
		        }
		
		        return block;
		    }
		    catch (Exception e)
		    {
		        e.printStackTrace();
		    }
		
		    return null;
		}
		public static void registerTextureFiles(String... textureFiles)
		{
		    PC_Utils.instance.iRegisterTextureFiles(textureFiles);
		}
		public static int defaultID(Object o){
			int id = -1;
			if(o instanceof PC_IItemInfo){
				if(o.getClass().isAnnotationPresent(PC_Shining.class)){
					Object[] on = PC_Utils.ValueWriting.getFieldsWithAnnotation(o.getClass(), PC_Shining.ON.class, o);
					Object[] off = PC_Utils.ValueWriting.getFieldsWithAnnotation(o.getClass(), PC_Shining.OFF.class, o);
					if(on!=null && on.length>0 && on[0] == o){
						id = SaveHandler.getConfig(((PC_IItemInfo) o).getModule()).getInt(PC_Utils.objects.getClass().getSimpleName()+".defaultID.on", 0);
					}else{
						id = SaveHandler.getConfig(((PC_IItemInfo) o).getModule()).getInt(PC_Utils.objects.getClass().getSimpleName()+".defaultID.off", 0);
					}
					}else{
					id = SaveHandler.getConfig(((PC_IItemInfo) o).getModule()).getInt(PC_Utils.objects.getClass().getSimpleName()+".defaultID", 0);
				}
			}
			if(o instanceof PC_Item){
				if(!ModuleLoader.isItemIDFree(id))
					id = ModuleLoader.getFreeItemID();
			}else if(o instanceof PC_Block){
				if(!ModuleLoader.isBlockIDFree(id))
					id = ModuleLoader.getFreeBlockID();
			}else if(o instanceof PC_ItemArmor){
				if(!ModuleLoader.isItemIDFree(id))
					id = ModuleLoader.getFreeItemID();
			}
			return id;
		}
		public static void resetPCObjectsIDs(){
			if(!PC_GlobalVariables.idResolve)
				return;
			for(Object o: PC_Utils.objects.values()){
				ModuleLoader.setPCObjectID(o, -1);
			}
			for(Object o: PC_Utils.objects.values()){
				int id = PC_Utils.ModuleLoader.defaultID(o);
				ModuleLoader.setPCObjectID(o, id);
			}
		}
		public static void savePCObjectsIDs(File worldDirectory){
		    NBTTagCompound nbttag = SaveHandler.makeIDTagCompound();
		    	
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
		public static void setPCObjectID(Object o, int id){
			if(!PC_GlobalVariables.idResolve)
				return;
			if(o instanceof PC_Item){
				((PC_Item) o).setItemID(id);
			}else if(o instanceof PC_Block){
				((PC_Block) o).setBlockID(id);
			}else if(o instanceof PC_ItemArmor){
				((PC_ItemArmor) o).setItemID(id);
			}
		}
		public static int getFreeBlockID() {
			for(int i=PC_GlobalVariables.blockStartIndex; i<Block.blocksList.length; i++){
				if(Block.blocksList[i] == null)
					return i;
			}
			for(int i=1; i<PC_GlobalVariables.blockStartIndex && i<Block.blocksList.length; i++){
				if(Block.blocksList[i] == null)
					return i;
			}
			return -1;
		}
		public static int getFreeItemID() {
			if(PC_GlobalVariables.itemStartIndex>Block.blocksList.length){
				for(int i=PC_GlobalVariables.itemStartIndex; i<Item.itemsList.length; i++){
					if(Item.itemsList[i] == null)
						return i;
				}
				for(int i=Block.blocksList.length; i<PC_GlobalVariables.itemStartIndex && i<Item.itemsList.length; i++){
					if(Item.itemsList[i] == null)
						return i;
				}
			}else{
				for(int i=Block.blocksList.length; i<Item.itemsList.length; i++){
					if(Item.itemsList[i] == null)
						return i;
				}
			}
			return -1;
		}
		public static boolean isItemIDFree(int id){
			if(id<=0)
				return false;
			return Item.itemsList[id]==null;
		}
		public static void registerModule(PC_IModule module){
			PC_Property config = null;
			File f = new File(PC_Utils.GameInfo.getMCDirectory(), "config/PowerCraft-"+module.getName()+".cfg");
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
			}else{
				config = PC_ConfigConverter.convert(config, module.getName());
			}
			PC_Utils.modules.put(module.getName(), new PC_Struct2<PC_IModule, PC_Property>(module, config));
		}
		public static boolean isBlockIDFree(int id){
			if(id<=0)
				return false;
			return Block.blocksList[id]==null;
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
		public static int addArmor(String name)
		{
		    return PC_Utils.instance.iAddArmor(name);
		}
		public static File createFile(File pfile, String name) {
			File file = new File(pfile, name);
			if(!file.exists())
				file.mkdirs();
		    return file;
		}
    	
		public static void regsterDataHandler(String name, PC_IDataHandler dataHandler){
			if(!instance.dataHandlers.containsKey(name))
				instance.dataHandlers.put(name, dataHandler);
		}
		
		public static void registerEntity(Class<? extends Entity> c, int entityID){
			EntityRegistry.registerModEntity(c, c.getName(), entityID, mod_PowerCraft.getInstance(), 50, 5, false);
		}
		
    }
    
    public static class ValueWriting{

    	public static Object writeItemStack(ItemStack itemstack) {
    		if(itemstack==null)
    			return new Object[0];
    		Object o[] = new Object[4];
    		o[0] = itemstack.getItem().itemID;
    		o[1] = itemstack.stackSize;
    		o[2] = itemstack.getItemDamage();
    		if(itemstack.stackTagCompound!=null){
    			try {
    				o[3] = CompressedStreamTools.compress(itemstack.stackTagCompound);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		return o;
    	}
    	
    	public static ItemStack readItemStack(Object[] o) {
    		if(o.length!=4)
    			return null;
    		int itemID = (Integer)o[0];
    		int stackSize = (Integer)o[1];
    		int meta = (Integer)o[2];
    		byte[] nbt = (byte[])o[3];
    		ItemStack itemstack = new ItemStack(itemID, stackSize, meta);
    		if(nbt!=null){
    			try {
    				itemstack.stackTagCompound = CompressedStreamTools.decompress(nbt);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		return itemstack;
    	}
    	
		public static int getFieldIDByName(Class c, String name){
    		Field f[] = c.getDeclaredFields();
    		for(int i=0; i<f.length; i++){
    			if(f[i].getName().equals(name))
    				return i;
    		}
    		return -1;
    	}
		
    	public static void setBlockBounds(Block block, double x, double y, double z, double width, double height, double depht){
    		block.setBlockBounds((float)x, (float)y, (float)z, (float)width, (float)height, (float)depht);
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
		public static Object[] getFieldsWithAnnotation(Class c, Class <? extends Annotation > annotationClass, Object obj)
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
		    	e.printStackTrace();
		        PC_Logger.severe("setPrivateValue failed with " + c + ":" + o + ":" + i);
		        return false;
		    }
		}
		public static void setBlockState(World world, int x, int y, int z, boolean on)
		{
		    Block b = Block.blocksList[GameInfo.getBID(world, x, y, z)];
		
		    if (b instanceof PC_Block)
		    {
		        int meta = GameInfo.getMD(world, x, y, z);
		        PC_TileEntity te = GameInfo.getTE(world, x, y, z);
		        Class c = b.getClass();
		
		        if (c.isAnnotationPresent(PC_Shining.class))
		        {
		            Block bon = (Block)getFieldsWithAnnotation(c, PC_Shining.ON.class, c)[0];
		            Block boff = (Block)getFieldsWithAnnotation(c, PC_Shining.OFF.class, c)[0];
		
		            if ((b == bon && !on) || (b == boff && on))
		            {
		            	if (on)
		                {
		                	b = bon;
		                }
		                else
		                {
		                	b = boff;
		                }
		                
		              
		            	PC_GlobalVariables.tileEntity = te;
		                
		                world.setBlockAndMetadataWithNotify(x, y, z, b.blockID, meta);
		                
		                PC_GlobalVariables.tileEntity = null;
		                
		                if (te != null)
		                {
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
		public static boolean setMD(World world, int x, int y, int z, int md)
		{
		    if (world != null)
		    {
		        return world.setBlockMetadata(x, y, z, md);
		    }
		
		    return false;
		}
		public static <t extends TileEntity>t setTE(World world, int x, int y, int z, t createTileEntity)
		{
		    world.setBlockTileEntity(x, y, z, createTileEntity);
		    return createTileEntity;
		}
		public static void spawnMobFromSpawner(World world, int x, int y, int z)
		{
		    TileEntityMobSpawner te = PC_Utils.GameInfo.getTE(world, x, y, z, Block.mobSpawner.blockID);
		
		    if (te != null)
		    {
		        ValueWriting.spawnMobs(world, x, y, z, PC_Utils.GameInfo.getMobID(te));
		    }
		}
		public static void preventSpawnerSpawning(World world, int x, int y, int z)
		{
		    TileEntityMobSpawner te = PC_Utils.GameInfo.getTE(world, x, y, z, Block.mobSpawner.blockID);
		
		    if (te != null)
		    {
		        te.delay = 20;
		    }
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
		public static void hugeUpdate(World world, int x, int y, int z)
		{
			int blockID = PC_Utils.GameInfo.getBID(world, x, y, z);
		    ValueWriting.notifyBlockOfNeighborChange(world, x - 2, y, z, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y, z, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y, z, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x + 2, y, z, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x, y - 2, z, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x, y - 1, z, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x, y + 1, z, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x, y + 2, z, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x, y, z - 2, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x, y, z - 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x, y, z + 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x, y, z + 2, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y + 1, z - 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y + 1, z - 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y + 1, z + 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y + 1, z + 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y + 1, z, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y + 1, z, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x, y + 1, z + 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x, y + 1, z - 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y, z - 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y, z - 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y, z + 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y, z + 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y - 1, z - 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y - 1, z - 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y - 1, z + 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y - 1, z + 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x + 1, y - 1, z, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x - 1, y - 1, z, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x, y - 1, z + 1, blockID);
		    ValueWriting.notifyBlockOfNeighborChange(world, x, y - 1, z - 1, blockID);
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
		public static void setReverseKey(PC_Property config)
		{
		    PC_Utils.keyReverse = PC_Utils.Communication.watchForKey(config, "keyReverse", 29, "Key for rotate placing");
		}
		public static boolean setBID(World world, int x, int y, int z, int id, int meta) {
			return world.setBlockAndMetadataWithNotify(x, y, z, id, meta);
		}
		public static ItemStack extractAndRemoveChest(World world, PC_VecI pos)
		{
		    if (GameInfo.hasFlag(world, pos, PC_Utils.NO_HARVEST))
		    {
		        return null;
		    }
		
		    TileEntity tec = PC_Utils.GameInfo.getTE(world, pos);
		
		    if (tec == null)
		    {
		        return null;
		    }
		
		    ItemStack stack = new ItemStack(PC_Utils.ModuleInfo.getPCObjectIDByName("PCco_ItemBlockSaver"), 1, 0);
		    NBTTagCompound blocktag = new NBTTagCompound();
		    PC_Utils.GameInfo.getTE(world, pos).writeToNBT(blocktag);
		    int dmg = PC_Utils.GameInfo.getBID(world, pos);
		    stack.setItemDamage(dmg);
		    blocktag.setInteger("BlockMeta", PC_Utils.GameInfo.getMD(world, pos));
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
		    ValueWriting.setBID(world, pos, 0, 0);
		    return stack;
		}
		public static boolean setBID(World world, PC_VecI pos, int id, int meta) {
			return PC_Utils.ValueWriting.setBID(world, pos.x, pos.y, pos.z, id, meta);
		}
		public static void notifyNeighbour(World world, int x, int y, int z) {
			world.notifyBlocksOfNeighborChange(x, y, z, PC_Utils.GameInfo.getBID(world, x, y, z));
		}
		public static void notifyNeighbour(World world, PC_VecI pos) {
			PC_Utils.ValueWriting.notifyNeighbour(world, pos.x, pos.y, pos.z);
		}
		public static boolean setBIDNoNotify(World world, int x, int y, int z, int id, int meta) {
			return world.setBlockAndMetadata(x, y, z, id, meta);
		}
		public static boolean setBIDNoNotify(World world, PC_VecI pos, int id, int meta) {
			return PC_Utils.ValueWriting.setBID(world, pos.x, pos.y, pos.z, id, meta);
		}
		public static boolean setMDNoNotify(World world, int x, int y, int z, int meta) {
			return world.setBlockMetadata(x, y, z, meta);
		}
		public static boolean setMDNoNotify(World world, PC_VecI pos, int meta) {
			return PC_Utils.ValueWriting.setMDNoNotify(world, pos.x, pos.y, pos.z, meta);
		}
		public static void spawnParticle(String name, Object...o)
		{
		    PC_Utils.instance.iSpawnParticle(name, o);
		}
		public static <t>t createClass(Class<t> c, Class[] param, Object[] objects) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
		{
		    return c.getConstructor(param).newInstance(objects);
		}
		public static void playSound(double x, double y, double z, String sound, float soundVolume, float pitch)
		{
		    if (PC_Utils.GameInfo.isSoundEnabled())
		    {
		        PC_Utils.instance.iPlaySound(x, y, z, sound, soundVolume, pitch);
		    }
		}
		public static void notifyBlockOfNeighborChange(World world, int x, int y, int z, int blockId)
		{
		    Block block = Block.blocksList[world.getBlockId(x, y, z)];
		
		    if (block != null)
		    {
		        block.onNeighborBlockChange(world, x, y, z, blockId);
		    }
		}
		public static void givePowerToBlock(World world, int x, int y, int z, float power)
		{
		    List<PC_Struct3<PC_VecI, PC_IMSG, Float>> powerReceivers = GameInfo.getPowerReceiverConnectedTo(world, x, y, z);
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
		        float f = PC_Utils.rand.nextFloat() * 0.8F + 0.1F;
		        float f1 = PC_Utils.rand.nextFloat() * 0.8F + 0.1F;
		        float f2 = PC_Utils.rand.nextFloat() * 0.8F + 0.1F;
		
		        while (itemstack.stackSize > 0)
		        {
		            int j = PC_Utils.rand.nextInt(21) + 10;
		
		            if (j > itemstack.stackSize)
		            {
		                j = itemstack.stackSize;
		            }
		
		            itemstack.stackSize -= j;
		            EntityItem entityitem = new EntityItem(world, pos.x + f, pos.y + f1, pos.z + f2, new ItemStack(itemstack.itemID, j,
		                    itemstack.getItemDamage()));
		
		            if (itemstack.hasTagCompound())
		            {
		                entityitem.func_92014_d().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
		            }
		
		            float f3 = 0.05F;
		            entityitem.motionX = (float) PC_Utils.rand.nextGaussian() * f3;
		            entityitem.motionY = (float) PC_Utils.rand.nextGaussian() * f3 + 0.2F;
		            entityitem.motionZ = (float) PC_Utils.rand.nextGaussian() * f3;
		            entityitem.delayBeforeCanPickup = 10;
		            world.spawnEntityInWorld(entityitem);
		        }
		    }
		}
    	public static void removeSmeltingRecipes(ItemStack is){
    		FurnaceRecipes smlt = FurnaceRecipes.smelting();
    		smlt.getSmeltingList().remove(Integer.valueOf(is.itemID));
    		int idx = 3; //ValueWriting.getFieldIDByName(FurnaceRecipes.class, "metaSmeltingList");
    		if(idx!=-1){
    			Object o = ValueWriting.getPrivateValue(FurnaceRecipes.class, smlt, idx);
    			if(o instanceof HashMap){
    				HashMap<List<Integer>, ItemStack> map = (HashMap<List<Integer>, ItemStack>)o;
    				map.remove(Arrays.asList(Integer.valueOf(is.itemID), Integer.valueOf(is.getItemDamage())));
    			}
    		}
    		
    		idx = 2; //ValueWriting.getFieldIDByName(FurnaceRecipes.class, "experienceList");
    		if(idx!=-1){
    			Object o = ValueWriting.getPrivateValue(FurnaceRecipes.class, smlt, idx);
    			if(o instanceof Map){
    				Map map = (Map)o;
    				map.remove(Integer.valueOf(is.itemID));
    			}
    		}
    		
    		idx = 4; //ValueWriting.getFieldIDByName(FurnaceRecipes.class, "metaExperience");
    		if(idx!=-1){
    			Object o = ValueWriting.getPrivateValue(FurnaceRecipes.class, smlt, idx);
    			if(o instanceof HashMap){
    				HashMap<List<Integer>, Float> map = (HashMap<List<Integer>, Float>)o;
    				map.remove(Arrays.asList(Integer.valueOf(is.itemID), Integer.valueOf(is.getItemDamage())));
    			}
    		}
    	}
    	public static void addSmeltingRecipes(ItemStack isInput, ItemStack isOutput, float experience){
    		FurnaceRecipes.smelting().addSmelting(Integer.valueOf(isInput.itemID), Integer.valueOf(isInput.getItemDamage()), isOutput, experience);
    	}
    }
    
    public static class Gres{

		public static void registerGres(String name, Class gui)
		{
		    PC_Utils.guis.put(name, gui);
		}

		public static void registerGresArray(Object[] o)
		{
		    if (o == null)
		    {
		        return;
		    }
		
		    for (int i = 0; i < o.length; i += 2)
		    {
		        PC_Utils.Gres.registerGres((String)o[i], (Class)o[i + 1]);
		    }
		}

		public static void openGres(String name, EntityPlayer player, PC_TileEntity te, Object...o)
		{
		    PC_Utils.instance.iOpenGres(name, player, te, o);
		}
    	
    }
    
    public static class Converter{

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
    	
    }
    
    public static class GameInfo{

    	public static boolean isPlayerOPOrOwner(EntityPlayer player){
    		if(mcs().getConfigurationManager().getOps().contains(player))
    			return true;
    		return GameInfo.mcs().getServerOwner() == player.getEntityName();
    	}
    	
		public static boolean isClient()
		{
		    return PC_Utils.instance.client();
		}

		public static void markBlockForUpdate(World world, int x, int y, int z) {
			world.markBlockForUpdate(x, y, z);
		}

		public static void markBlockForUpdate(World world, PC_VecI pos) {
			world.markBlockForUpdate(pos.x, pos.y, pos.z);
		}
		
		public static boolean isServer()
		{
		    return !PC_Utils.instance.client();
		}

		public static boolean isSoundEnabled()
		{
		    if (PC_Utils.GameInfo.isServer())
		    {
		        return false;
		    }
		
		    return PC_GlobalVariables.soundEnabled;
		}

		public static int getWorldDimension(World worldObj)
		{
		    return worldObj.provider.dimensionId;
		}
		
		private static IRecipe resolveForgeRecipe(IRecipe recipe){
			Object[] o;
			boolean isShaped;
			if(recipe instanceof ShapedOreRecipe){
				o = (Object[])ValueWriting.getPrivateValue(ShapedOreRecipe.class, recipe, 3);
				int width = (Integer)ValueWriting.getPrivateValue(ShapedOreRecipe.class, recipe, 4);
				int height = (Integer)ValueWriting.getPrivateValue(ShapedOreRecipe.class, recipe, 5);
				List<PC_ItemStack> list[][] = new List[width][height];
				int i=0;
				for(int y=0; y<height; y++){
					for(int x=0; x<width; x++){
						if(i<o.length){
							list[x][y] = new ArrayList<PC_ItemStack>();
							if(o[i] instanceof ItemStack){
								list[x][y].add(new PC_ItemStack((ItemStack)o[i]));
							}else if(o[i] instanceof List){
								for(ItemStack is:(List<ItemStack>)o[i]){
									list[x][y].add(new PC_ItemStack(is));
								}
							}
						}
						i++;
					}
				}
				return new PC_ShapedRecipes(new PC_ItemStack(recipe.getRecipeOutput()), new PC_VecI(width, height), list);
			}else if(recipe instanceof ShapelessOreRecipe){
				o = ((List)ValueWriting.getPrivateValue(ShapelessOreRecipe.class, recipe, 1)).toArray(new Object[0]);
				List<PC_ItemStack> list[] = new List[o.length];
				for(int i=0; i<o.length; i++){
					list[i] = new ArrayList<PC_ItemStack>();
					if(o[i] instanceof ItemStack){
						list[i].add(new PC_ItemStack((ItemStack)o[i]));
					}else if(o[i] instanceof List){
						for(ItemStack is:(List<ItemStack>)o[i]){
							list[i].add(new PC_ItemStack(is));
						}
					}
				}
				return new PC_ShapelessRecipes(new PC_ItemStack(recipe.getRecipeOutput()), list);
			}else{
				return null;
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
		            	
		            	if(recipe instanceof ShapedOreRecipe||recipe instanceof ShapelessOreRecipe){
		            		ret.add(resolveForgeRecipe(recipe));
		            	}else{
		            		ret.add(recipe);
		            	}
		            }
		        }
		        catch (NullPointerException npe)
		        {
		            continue;
		        }
		    }
		
		    return ret;
		}

		public static EnumGameType getGameTypeFor(EntityPlayer player)
		{
		    return PC_Utils.instance.iGetGameTypeFor(player);
		}

		public static boolean isCreative(EntityPlayer player)
		{
		    return PC_Utils.GameInfo.getGameTypeFor(player).isCreative();
		}

		public static boolean isFuel(ItemStack itemstack)
		{
		    if (itemstack == null)
		    {
		        return false;
		    }
		
		    return GameInfo.getFuelValue(itemstack, 1f) > 0;
		}

		public static boolean isSmeltable(ItemStack itemstack)
		{
		    if (itemstack == null || FurnaceRecipes.smelting().getSmeltingResult(itemstack) == null)
		    {
		        return false;
		    }
		
		    return true;
		}

		public static List<ItemStack> getFeedstock(ItemStack itemstack){
			List<ItemStack> l = new ArrayList<ItemStack>();
			if (itemstack != null){
				Map<Integer, ItemStack> map = FurnaceRecipes.smelting().getSmeltingList();
				for(Entry<Integer, ItemStack> e:map.entrySet()){
					if(e.getValue().isItemEqual(itemstack)){
						l.add(new ItemStack((int)e.getKey(), 1, 0));
					}
				}
				Map<List<Integer>, ItemStack> map2 = (Map<List<Integer>, ItemStack>)ValueWriting.getPrivateValue(FurnaceRecipes.class, FurnaceRecipes.smelting(), 3);
				for(Entry<List<Integer>, ItemStack> e:map2.entrySet()){
					if(e.getValue().isItemEqual(itemstack)){
						l.add(new ItemStack((int)e.getKey().get(0), 1, e.getKey().get(1)));
					}
				}
			}
			return l;
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
			return PC_Utils.GameInfo.getTE(world, vec.x, vec.y, vec.z);
		}

		public static <t extends TileEntity>t getTE(IBlockAccess world, int x, int y, int z, int blockID)
		{
		    if (world != null)
		    {
		        if (GameInfo.getBID(world, x, y, z) == blockID)
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
		    return PC_Utils.GameInfo.getBID(world, vec.x, vec.y, vec.z);
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
		    return PC_Utils.GameInfo.getMD(world, vec.x, vec.y, vec.z);
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

		public static String getMobID(TileEntityMobSpawner te){
			return (String)PC_Utils.ValueWriting.getPrivateValue(TileEntityMobSpawner.class, te, 1);
		}

		public static File getMCDirectory()
		{
		    return PC_Utils.instance.iGetMCDirectory();
		}

		public static MinecraftServer mcs()
		{
		    return MinecraftServer.getServer();
		}

		public static boolean isEntityFX(Entity entity)
		{
		    return PC_Utils.instance.iIsEntityFX(entity);
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
		            intList.add(Integer.parseInt(part.trim()));
		        }
		        catch (NumberFormatException e) {}
		    }
		
		    return intList;
		}

		public static Block getBlock(World world, PC_VecI pos) {
			return GameInfo.getBlock(world, pos.x, pos.y, pos.z);
		}

		public static File getPowerCraftFile() {
			return ModuleLoader.createFile(PC_Utils.GameInfo.getMCDirectory(), "PowerCraft");
		}

		public static boolean isPoweredDirectly(World world, int x, int y, int z){
			 return world.isBlockGettingPowered(x, y, z);
		}

		public static boolean isPoweredIndirectly(World world, int x, int y, int z){
		    return world.isBlockIndirectlyGettingPowered(x, y, z);
		}

		public static boolean isPoweredIndirectly(World world, PC_VecI pos){
		    return PC_Utils.GameInfo.isPoweredIndirectly(world, pos.x, pos.y, pos.z);
		}

		public static boolean isPoweredDirectly(World world, PC_VecI pos){
		    return PC_Utils.GameInfo.isPoweredDirectly(world, pos.x, pos.y, pos.z);
		}

		public static boolean usingForge() {
			return true;
		}

		public static boolean isBlock(IBlockAccess world, PC_VecI pos, String...names) {
			int blockID = PC_Utils.GameInfo.getBID(world, pos.x, pos.y, pos.z);
			for(String name:names)
				if(blockID == ModuleInfo.getPCObjectIDByName(name))
					return true;
			return false;
		}

		public static float giveConductorValueFor(Block b)
		{
		    if (b == null)
		    {
		        return 0.0f;
		    }
		
		    if (b.blockID == Block.blockSteel.blockID)
		    {
		        return 0.8f;
		    }
		
		    if (b.blockID == Block.blockGold.blockID)
		    {
		        return 0.9f;
		    }
		
		    if(b instanceof PC_IMSG){
		    	Object o = ((PC_IMSG) b).msg(PC_Utils.MSG_CONDUCTIVITY);
		    	if(o instanceof Float)
		    		return (Float)o;
		    }
		    
		    return 0.0f;
		}

		public static Block getBlock(World world, int x, int y, int z)
		{
		    return Block.blocksList[PC_Utils.GameInfo.getBID(world, x, y, z)];
		}

		public static boolean isPlacingReversed(EntityPlayer player)
		{
		    return PC_Utils.instance.iIsPlacingReversed(player);
		}

		public static boolean poweredFromInput(World world, int x, int y, int z, int inp)
		{
		    return GameInfo.poweredFromInput(world, x, y, z, inp, 0);
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

		public static void searchPowerReceiverConnectedTo(World world, int x, int y, int z, List<PC_Struct3<PC_VecI, PC_IMSG, Float>> receivers, List<PC_Struct2<PC_VecI, Float>> allpos, float power)
		{
		    Block b = PC_Utils.GameInfo.getBlock(world, x, y, z);
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
		
		    float value = PC_Utils.GameInfo.giveConductorValueFor(b);
		
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
		    PC_Utils.GameInfo.searchPowerReceiverConnectedTo(world, x, y, z, receivers, blocks, 1.0f);
		    PC_PacketHandler.sendToPacketHandler(true, world, "PacketUtils", PC_Utils.SPAWNPARTICLEONBLOCKS, blocks);
		    return receivers;
		}

		public static boolean hasFlag(World world, PC_VecI pos, String flag) {
			Block b = PC_Utils.GameInfo.getBlock(world, pos.x, pos.y, pos.z);
			if(b instanceof PC_IMSG){
				List<String> list = (List<String>)((PC_IMSG)b).msg(PC_Utils.MSG_BLOCK_FLAGS, world, pos, new ArrayList<String>());
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
					List<String> list = (List<String>) ((PC_IMSG)b).msg(PC_Utils.MSG_ITEM_FLAGS, is, new ArrayList<String>());
					if(list != null){
						return list.contains(flag);
					}
				}
			}
		
			Item i = is.getItem();
			if(i instanceof PC_IMSG){
				List<String> list = (List<String>) ((PC_IMSG) i).msg(PC_Utils.MSG_ITEM_FLAGS, is, new ArrayList<String>());
				if(list != null){
					return list.contains(flag);
				}
			}
		
			return false;
		}

		public static void dropXP(World world, double x, double y, double z, int xp) {
			while (xp > 0)
            {
                int drop = EntityXPOrb.getXPSplit(xp);
                xp -= drop;
                world.spawnEntityInWorld(new EntityXPOrb(world, x, y, z, drop));
            }
		}
		
		public static void dropXP(World world, PC_VecF pos, int xp) {
			dropXP(world, pos, xp);
		}

		public static List<PC_ItemStack>[][] getExpectedInput(IRecipe recipe, int width, int hight) {
			List<PC_ItemStack>[][] list;
			if (recipe instanceof PC_IRecipeInfo){
				PC_IRecipeInfo ri = (PC_IRecipeInfo) recipe;
				PC_VecI size = ri.getSize();
				if(size!=null){
					if(width==-1)
						width = size.x;
					if(hight==-1)
						hight = size.y;
					if(size.x>width||size.y>hight)
						return null;
				}else{
					int rsize = recipe.getRecipeSize();
					if(width==-1)
						width = rsize;
					if(hight==-1)
						hight = 1;
					if(hight*width<rsize || rsize==0)
	            		return null;
					size = new PC_VecI(width, hight);
				}
				list = new List[width][hight];
				int i=0;
				for(int y=0; y<size.y; y++){
					for(int x=0; x<size.x; x++){
						if(i<ri.getRecipeSize()){
							list[x][y] = ri.getExpectedInputFor(i);
						}
						i++;
					}
				}
            }else if (recipe instanceof ShapedRecipes){
            	int sizeX = (Integer) ValueWriting.getPrivateValue(ShapedRecipes.class, recipe, 0);
            	int sizeY = (Integer) ValueWriting.getPrivateValue(ShapedRecipes.class, recipe, 1);
            	ItemStack[] stacks = (ItemStack[]) ValueWriting.getPrivateValue(ShapedRecipes.class, recipe, 2);
            	if(width==-1)
					width = sizeX;
				if(hight==-1)
					hight = sizeY;
            	if(sizeX>width||sizeY>hight)
					return null;
            	list = new List[width][hight];
            	int i=0;
				for(int y=0; y<sizeY; y++){
					for(int x=0; x<sizeX; x++){
						if(i<stacks.length){
							if(stacks[i]!=null){
								list[x][y] = new ArrayList<PC_ItemStack>();
								list[x][y].add(new PC_ItemStack(stacks[i]));
							}
						}
						i++;
					}
				}
            }else if (recipe instanceof ShapelessRecipes){
            	List<ItemStack> stacks = ((List<ItemStack>) ValueWriting.getPrivateValue(ShapelessRecipes.class, recipe, 1));
            	if(width==-1)
					width = stacks.size();
				if(hight==-1)
					hight = 1;
            	if(hight*width<stacks.size())
            		return null;
            	list = new List[width][hight];
            	int i=0;
            	for(int y=0; y<hight; y++){
					for(int x=0; x<width; x++){
						if(i<stacks.size()){
							list[x][y] = new ArrayList<PC_ItemStack>();
							list[x][y].add(new PC_ItemStack(stacks.get(i)));
						}
					}
            	}
            }else{
            	return null;
            }
			return list;
		}

		public static int getFuelValue(ItemStack itemstack, double strength)
		{
		    return (int)(TileEntityFurnace.getItemBurnTime(itemstack) * strength);
		}

		public static ItemStack getSmeltingResult(ItemStack item) {
			return FurnaceRecipes.smelting().getSmeltingResult(item);
		}
	   
   }
    
    public static class ModuleInfo{

		public static PC_Block getPCBlockByName(String name)
		{
		    if (PC_Utils.objects.containsKey(name))
		    {
		        Object o = PC_Utils.objects.get(name);
		
		        if (o instanceof PC_Block)
		        {
		            return (PC_Block)o;
		        }
		    }
		
		    return null;
		}

		public static PC_Item getPCItemByName(String name)
		{
		    if (PC_Utils.objects.containsKey(name))
		    {
		        Object o = PC_Utils.objects.get(name);
		
		        if (o instanceof PC_Item)
		        {
		            return (PC_Item)o;
		        }
		    }
		
		    return null;
		}

		public static int getPCObjectIDByName(String name)
		{
		    if (PC_Utils.objects.containsKey(name))
		    {
		        Object o = PC_Utils.objects.get(name);
		
		        if (o instanceof Item)
		        {
		            return ((Item)o).itemID;
		        }
		        else if (o instanceof Block)
		        {
		            return ((Block)o).blockID;
		        }
		    }
		
		    return 0;
		}

		public static PC_IModule getModule(Object o) {
			if(o instanceof PC_IItemInfo){
				return ((PC_IItemInfo) o).getModule();
			}
			return null;
		}

		public static List<PC_IModule> getModules(){
			List<PC_IModule> list = new ArrayList<PC_IModule>();
			for(PC_Struct2<PC_IModule, PC_Property> s:PC_Utils.modules.values()){
				list.add(s.a);
			}
			return list;
		}

		public static String getPowerCraftLoaderImageDir(){
			return "/powercraft/management/textures/";
		}

		public static PC_IModule getModule(String name){
			if(PC_Utils.modules.containsKey(name)){
				return PC_Utils.modules.get(name).a;
			}
			return null;
		}

		public static List<Object> getRegisterdObjects() {
			return new ArrayList<Object>(PC_Utils.objects.values());
		}

		public static String getGresImgDir() {
			return getPowerCraftLoaderImageDir() + "gres/";
		}

		public static String getTextureDirectory(PC_IModule module){
			return "/powercraft/" + module.getName().toLowerCase() + "/textures/";
		}

		public static String getTerrainFile(PC_IModule module){
			return PC_Utils.ModuleInfo.getTextureDirectory(module) + "tiles.png";
		}

		public static List<PC_IMSG> getMSGObjects() {
			return msgObjects;
		}
    	
		public static void registerMSGObject(PC_IMSG obj){
			msgObjects.add(obj);
		}
		
    }
    
    public static class SaveHandler{

		public static PC_INBT loadFromNBT(NBTTagCompound nbttagcompound, String string, PC_INBT nbt)
		{
		    NBTTagCompound nbttag = nbttagcompound.getCompoundTag(string);
		    return nbt.readFromNBT(nbttag);
		}

		public static void saveToNBT(NBTTagCompound nbttagcompound, String string, PC_INBT nbt)
		{
			NBTTagCompound nbttag = nbt.writeToNBT(new NBTTagCompound());
			if(nbttag!=null)
				nbttagcompound.setCompoundTag(string, nbttag);
		}

		public static boolean loadPCObjectsIDs(File worldDirectory){
			File file = new File(worldDirectory, "powercraft.dat");
			if(!file.exists())
				return false;
			try{
				SaveHandler.loadIDFromTagCompound(CompressedStreamTools.readCompressed(new FileInputStream(file)));
		    }catch (Exception e){
		        return false;
		    }
			return true;
		}

		public static NBTTagCompound makeIDTagCompound(){
			 NBTTagCompound nbttag = new NBTTagCompound("PowerCraftIDs");
		
		    for(String key:PC_Utils.objects.keySet()){
		    	nbttag.setInteger(key, ModuleInfo.getPCObjectIDByName(key));
		    }
		    
		    return nbttag;
		}

		public static void loadIDFromTagCompound(NBTTagCompound nbttag){
			if(!PC_GlobalVariables.idResolve)
				return;
			for(Entry<String, Object>e: PC_Utils.objects.entrySet()){
				PC_Utils.ModuleLoader.setPCObjectID(e.getValue(), -1);
			}
			for(Entry<String, Object>e: PC_Utils.objects.entrySet()){
				if(nbttag.hasKey(e.getKey())){
					PC_Utils.ModuleLoader.setPCObjectID(e.getValue(), nbttag.getInteger(e.getKey()));
				}
			}
			for(Entry<String, Object>e: PC_Utils.objects.entrySet()){
				if(!nbttag.hasKey(e.getKey())){
					PC_Utils.ModuleLoader.setPCObjectID(e.getValue(), PC_Utils.ModuleLoader.defaultID(e.getValue()));
				}
			}
		}

		public static void saveConfig(PC_IModule module) {
			PC_Property config = SaveHandler.getConfig(module);
			File f = new File(PC_Utils.GameInfo.getMCDirectory(), "config/PowerCraft-"+module.getName()+".cfg");
			if(config!=null){
				try {
					OutputStream os = new FileOutputStream(f);
					config.save(os);
				} catch (FileNotFoundException e) {
					PC_Logger.severe("Can't find File "+f);
				}
			}
		}

		public static PC_Property getConfig(PC_IModule module) {
			if(PC_Utils.modules.containsKey(module.getName())){
				return PC_Utils.modules.get(module.getName()).b;
			}
			return null;
		}

		public static void savePowerCraftData(WorldInfo worldInfo, File worldDirectory) {
			worldDirectory = new File(worldDirectory, "powercraft");
			if(!worldDirectory.exists())
				worldDirectory.mkdirs();
			for(Entry<String, PC_IDataHandler> dataHandler:instance.dataHandlers.entrySet()){
				if(dataHandler.getValue().needSave()){
					NBTTagCompound nbttag = dataHandler.getValue().save(new NBTTagCompound(dataHandler.getKey()));
					try
				    {
				        File file = new File(worldDirectory, dataHandler.getKey()+".dat");
				        CompressedStreamTools.writeCompressed(nbttag, new FileOutputStream(file));
				    }
				    catch (Exception e)
				    {
				        e.printStackTrace();
				    }
				}
			}
		}
		
		public static void loadPowerCraftData(WorldInfo worldInfo, File worldDirectory) {
			worldDirectory = new File(worldDirectory, "powercraft");
			for(PC_IDataHandler dh : dataHandlers.values()){
				dh.reset();
			}
			if(worldDirectory.exists()){
				File files[] = worldDirectory.listFiles();
				for(File file:files){
					String name = file.getName();
					if(name.endsWith(".dat")){
						name = name.substring(0, name.length()-4);
						if(dataHandlers.containsKey(name)){
							try{
								dataHandlers.get(name).load(CompressedStreamTools.readCompressed(new FileInputStream(file)));
						    }catch (Exception e){
						        e.printStackTrace();
						    }
						}
					}
				}
			}
			for(PC_IMSG msg : msgObjects){
				msg.msg(MSG_LOAD_WORLD, worldInfo, worldDirectory);
			}
		}

		public static void saveToNBT(NBTTagCompound nbtTag, String key, Object value) {
			if(value == null){
				return;
			}else if(value.getClass().isArray()){
				Object[] a = (Object[])value;
				NBTTagCompound nbtTag2 = new NBTTagCompound();
				int size = a.length;
				nbtTag2.setInteger("count", size);
				nbtTag2.setString("type", a.getClass().getName());
				for(int i=0; i<size; i++){
					saveToNBT(nbtTag2, "value["+i+"]", a[i]);
				}
				nbtTag.setCompoundTag(key, nbtTag2);
			}else if(value instanceof List){
				List l = (List)value;
				NBTTagCompound nbtTag2 = new NBTTagCompound();
				int size = l.size();
				nbtTag2.setInteger("count", size);
				nbtTag2.setString("type", l.getClass().getName());
				for(int i=0; i<size; i++){
					saveToNBT(nbtTag2, "value["+i+"]", l.get(i));
				}
				nbtTag.setCompoundTag(key, nbtTag2);
			}else if(value instanceof Byte){
				nbtTag.setByte(key, (Byte)value);
			}else if(value instanceof Short){
				nbtTag.setShort(key, (Short)value);
			}else if(value instanceof Integer){
				nbtTag.setInteger(key, (Integer)value);
			}else if(value instanceof Long){
				nbtTag.setLong(key, (Long)value);
			}else if(value instanceof Float){
				nbtTag.setFloat(key, (Float)value);
			}else if(value instanceof Double){
				nbtTag.setDouble(key, (Double)value);
			}else if(value instanceof Boolean){
				NBTTagCompound nbtTag2 = new NBTTagCompound();
				nbtTag2.setString("type", Boolean.class.getName());
				nbtTag2.setBoolean("value", (Boolean)value);
				nbtTag.setCompoundTag(key, nbtTag2);
			}else if(value instanceof String){
				nbtTag.setString(key, (String)value);
			}else if(value instanceof PC_INBT){
				NBTTagCompound nbtTag2 = new NBTTagCompound();
				nbtTag2.setString("type", value.getClass().getName());
				SaveHandler.saveToNBT(nbtTag2, "value", (PC_INBT)value);
				nbtTag.setCompoundTag(key, nbtTag2);
			}else if(value instanceof ItemStack){
				NBTTagCompound nbtTag2 = new NBTTagCompound();
				nbtTag2.setString("type", ItemStack.class.getName());
				((ItemStack) value).writeToNBT(nbtTag2);
				nbtTag.setCompoundTag(key, nbtTag2);
			}
		}
    	
		public static Object loadFromNBT(NBTTagCompound nbtTag, String key) {
			Object value = nbtTag.tagMap.get(key);
			if(value instanceof NBTTagCompound){
				NBTTagCompound nbtTag2 = nbtTag.getCompoundTag(key);
				try {
					Class c = Class.forName(nbtTag2.getString("type"));
					if(c.isArray()){
						int size = nbtTag2.getInteger("count");
						Object[] a = (Object[]) Array.newInstance(c, size);
						for(int i=0; i<size; i++){
							a[i] = loadFromNBT(nbtTag2, "value["+i+"]");
						}
					}else if(c == ItemStack.class){
						return ItemStack.loadItemStackFromNBT(nbtTag2);
					}else if(c == Boolean.class){
						return nbtTag2.getBoolean("value");
					}else{
						try {
							Object o = c.newInstance();
							if(o instanceof List){
								int size = nbtTag2.getInteger("count");
								for(int i=0; i<size; i++){
									((List)o).add(loadFromNBT(nbtTag2, "value["+i+"]"));
								}
							}else if(o instanceof PC_INBT){
								loadFromNBT(nbtTag2, "value", (PC_INBT)o);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}else if(value instanceof NBTTagByte){
				return ((NBTTagByte)value).data;
			}else if(value instanceof NBTTagShort){
				return ((NBTTagShort)value).data;
			}else if(value instanceof NBTTagInt){
				return ((NBTTagInt)value).data;
			}else if(value instanceof NBTTagLong){
				return ((NBTTagLong)value).data;
			}else if(value instanceof NBTTagFloat){
				return ((NBTTagFloat)value).data;
			}else if(value instanceof NBTTagDouble){
				return ((NBTTagDouble)value).data;
			}else if(value instanceof NBTTagString){
				return ((NBTTagString)value).data;
			}
			return null;
		}
		
    }
    
    public static class Communication{

		public static void chatMsg(String msg, boolean clear)
		{
		    PC_Utils.instance.iChatMsg(msg, clear);
		}

		public static boolean isKeyPressed(EntityPlayer player, String key)
		{
		    return PC_Utils.instance.iIsKeyPressed(player, key);
		}

		public static void watchForKey(String name, int key)
		{
		    PC_Utils.instance.iWatchForKey(name, key);
		}

		public static int watchForKey(PC_Property config, String name, int key, String...info)
		{
			key = config.getInt("key."+name, key, info);
		    watchForKey(name, key);
		    return key;
		}
    	
    }
    
    public static class Coding{

		public static boolean areObjectsEqual(Object a, Object b)
		{
		    return a == null ? b == null : a.equals(b);
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

		public static int getValueNum(Class c, String n) {
			Field[] fields = c.getDeclaredFields();
			for(int i=0; i<fields.length; i++){
				if(fields[i].getName().equals(n)){
					return i;
				}
			}
			return -1;
		}
    	
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

    protected boolean client()
    {
        return false;
    }

    protected void iRegisterLanguage(PC_IModule module, String lang, PC_Struct3<String, String, String[]>[] translations)
    {
    }

    protected void iLoadLanguage(PC_IModule module) {}

    protected void iSaveLanguage(PC_IModule module) {}

    protected void iTileEntitySpecialRenderer(Class <? extends TileEntity> tileEntityClass){
		
	}
        
    protected void iPlaySound(double x, double y, double z, String sound, float soundVolume, float pitch) {}

    protected void iOpenGres(String name, EntityPlayer player, PC_TileEntity te, Object[]o)
    {
        if (!(player instanceof EntityPlayerMP))
        {
            return;
        }

        int guiID = 0;

        guiID = ((EntityPlayerMP)player).currentWindowId;
        guiID = guiID % 100 + 1;
        ((EntityPlayerMP)player).currentWindowId = guiID;

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        ObjectOutputStream sendData;

        try
        {
            sendData = new ObjectOutputStream(data);
            sendData.writeInt(PC_PacketHandler.PACKETGUI);
            sendData.writeObject(name);
            sendData.writeInt(guiID);
            if(te==null){
            	sendData.writeObject(null);
            }else{
            	sendData.writeObject(te.getCoord());
            }
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
                    PC_GresBaseWithInventory bwi = ValueWriting.createClass((Class<PC_GresBaseWithInventory>)c, new Class[] {EntityPlayer.class, PC_TileEntity.class, Object[].class}, new Object[] {player, te, o});
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

    protected EnumGameType iGetGameTypeFor(EntityPlayer player)
    {
        return ((EntityPlayerMP)player).theItemInWorldManager.getGameType();
    }

    protected void iChatMsg(String msg, boolean clear) {}

    protected boolean iIsPlacingReversed(EntityPlayer player)
    {
        return Communication.isKeyPressed(player, "keyReverse");
    }

    protected File iGetMCDirectory()
    {
        return GameInfo.mcs().getFile("");
    }

    protected int iAddArmor(String name)
    {
        return 0;
    }

    protected boolean iIsEntityFX(Entity entity)
    {
        return false;
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

    protected void iWatchForKey(String name, int key) {}

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
                        Block b = Block.blocksList[GameInfo.getBID(player.worldObj, block.a)];
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

                        ValueWriting.spawnParticle("PC_EntityLaserParticleFX", player.worldObj, new PC_VecF(block.a).add(pos), new PC_Color(0.6f, 0.6f, 1.0f), move, 0);
                    }
                }

                break;
        }

        return false;
    }

    protected void iSpawnParticle(String name, Object[] o)
    {
    }
	
}
