package powercraft.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.Entity;
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
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class PC_Utils implements PC_IPacketHandler {
	
	protected static PC_Utils instance;
	public static final int BACK = 0, LEFT = 1, RIGHT = 2, FRONT = 3, BOTTOM = 4, TOP = 5;
	
	protected HashMap<EntityPlayer, List<String>> keyPressed = new HashMap<EntityPlayer, List<String>>();
	protected int keyReverse;
	protected HashMap<String, Object> objects = new HashMap<String, Object>();
	
	public PC_Utils(){
		PC_PacketHandler.registerPackethandler("PacketUtils", this);
	}
	
	public static boolean setUtilsInstance(PC_Utils instance){
		if(PC_Utils.instance==null){
			PC_Utils.instance = instance;
			return true;
		}
		return false;
	}

	protected void iRegisterTextureFiles(String[] textureFiles) {}
	
	public static void registerTextureFiles(String... textureFiles) {
		instance.iRegisterTextureFiles(textureFiles);
	}
	
	protected boolean client(){return false;}
	
	public static boolean isClient(){
		return instance.client();
	}
	
	public static boolean isServer(){
		return !instance.client();
	}
	
	protected void iRegisterLanguage(PC_Module module, String lang, String[] translations) {
		for(int i=0; i<translations.length; i+=2)
			LanguageRegistry.instance().addStringLocalization(translations[i], lang, translations[i+1]);
	}
	
	public static void registerLanguageForLang(PC_Module module, String lang, String... translations){
		instance.iRegisterLanguage(module, lang, translations);
	}
	
	public static void registerLanguage(PC_Module module, String... translations){
		instance.registerLanguageForLang(module, "en_US", translations);
	}
	
	protected void iLoadLanguage(PC_Module module) {}
	
	public static void loadLanguage(PC_Module module){
		instance.iLoadLanguage(module);
	}
	
	protected void iSaveLanguage(PC_Module module) {}
	
	public static void saveLanguage(PC_Module module){
		instance.iSaveLanguage(module);
	}

	public static String tr(String identifier) {
		return StringTranslate.getInstance().translateKey(identifier).trim(); 
	}
	
	public static String tr(String identifier, String... replacements) {
		return StringTranslate.getInstance().translateKeyFormat(identifier, (Object[])replacements);
	}
	
	public static String isIDAvailable(int id, Class c){
		if(id<0)
			return "Out of bounds";
		if(id<Block.blocksList.length){
			if(Block.blocksList[id]!=null)
				return Block.blocksList[id].getBlockName();
		}else if(Block.class.isAssignableFrom(c))
			return "Out of bounds";
		if(id<Item.itemsList.length){
			if(Item.itemsList[id]!=null)
				return Item.itemsList[id].getItemName();
			return null;
		}
		return "Out of bounds";
	}
	
	public static boolean isIDAvailable(int id, Class c, boolean throwError) throws Exception{
		String name = isIDAvailable(id, c);
		if(!throwError || name==null)
			return name==null;
		String error = "ID "+id+" for class \""+c.getName()+"\" already used by \""+name+"\"";
		PC_Logger.severe(error);
		throw new Exception(error);
	}
	
	public static <t>t register(PC_Module module, int defaultID, Class<t> c){
		Configuration config = module.getConfig();
		if(PC_Block.class.isAssignableFrom(c)){
			return (t)register(module, defaultID, (Class<PC_Block>)c, null, null);
		}else if(PC_Item.class.isAssignableFrom(c)){
			Class<PC_Item> itemClass = (Class<PC_Item>)c;
			try {
				int itemID = getConfigInt(config, Configuration.CATEGORY_ITEM, itemClass.getName(), defaultID);
				isIDAvailable(itemID, itemClass, true);
				PC_Item item = createClass(itemClass, new Class[]{int.class}, new Object[]{itemID});
				instance.objects.put(itemClass.getSimpleName(), item);
				item.setItemName(itemClass.getSimpleName());
				item.setCraftingToolModule(module.getNameWithoutPowerCraft());
				item.setTextureFile(module.getTerrainFile());
				if(item instanceof PC_IConfigLoader)
					((PC_IConfigLoader) item).loadFromConfig(config);
				registerLanguage(module, item.getDefaultNames());
				return (t)item;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else if(PC_ItemArmor.class.isAssignableFrom(c)){
			Class<PC_ItemArmor> itemArmorClass = (Class<PC_ItemArmor>)c;
			try {
				int itemID = getConfigInt(config, Configuration.CATEGORY_ITEM, itemArmorClass.getName(), defaultID);
				isIDAvailable(itemID, itemArmorClass, true);
				PC_ItemArmor itemArmor = createClass(itemArmorClass, new Class[]{int.class}, new Object[]{itemID});
				instance.objects.put(itemArmorClass.getSimpleName(), itemArmor);
				itemArmor.setItemName(itemArmorClass.getSimpleName());
				itemArmor.setCraftingToolModule(module.getNameWithoutPowerCraft());
				if(itemArmor instanceof PC_IConfigLoader)
					((PC_IConfigLoader) itemArmor).loadFromConfig(config);
				registerLanguage(module, itemArmor.getItemName(), itemArmor.getDefaultName());
				return (t)itemArmor;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		throw new IllegalArgumentException("3th parameter need to be a class witch extends PC_Block or PC_Item or PC_ItemArmor");
	}
	
	public static <t extends PC_Block>t register(PC_Module module, int defaultID, Class<t> blockClass, Class c){
		if(PC_ItemBlock.class.isAssignableFrom(c)){
			return register(module, defaultID, blockClass, (Class<PC_ItemBlock>)c, null);
		}else if(PC_TileEntity.class.isAssignableFrom(c)){
			return register(module, defaultID, blockClass, null, (Class<PC_TileEntity>)c);
		}
		throw new IllegalArgumentException("4th parameter need to be a class witch extends PC_ItemBlock or PC_TileEntity");
	}
	
	public static <t extends PC_Block>t register(PC_Module module, int defaultID, Class<t> blockClass, Class<? extends PC_ItemBlock> itemBlockClass, Class<? extends PC_TileEntity> tileEntityClass){
		Configuration config = module.getConfig();
		try {
			int blockID = getConfigInt(config, Configuration.CATEGORY_BLOCK, blockClass.getName(), defaultID);
			isIDAvailable(blockID, blockClass, true);
			t block;
			t blockOff;
			if(blockClass.isAnnotationPresent(PC_Shining.class)){
				block = createClass(blockClass, new Class[]{int.class, boolean.class}, new Object[]{blockID, true});
				blockOff = createClass(blockClass, new Class[]{int.class, boolean.class}, new Object[]{blockID+1, false});
				setFieldsWithAnnotationTo(blockClass, PC_Shining.ON.class, blockClass, block);
				setFieldsWithAnnotationTo(blockClass, PC_Shining.OFF.class, blockClass, blockOff);
				blockOff.setBlockName(blockClass.getSimpleName());
				blockOff.setTextureFile(module.getTerrainFile());
			}else{
				block = createClass(blockClass, new Class[]{int.class}, new Object[]{blockID});
			}
			instance.objects.put(blockClass.getSimpleName(), block);
			block.setBlockName(blockClass.getSimpleName());
			block.setTextureFile(module.getTerrainFile());
			if(block instanceof PC_IConfigLoader)
				((PC_IConfigLoader) block).loadFromConfig(config);
			if(itemBlockClass==null){
				GameRegistry.registerBlock(block);
				registerLanguage(module, block.getBlockName(), block.getDefaultName());
			}else{
				GameRegistry.registerBlock(block, itemBlockClass);
				PC_ItemBlock itemBlock = (PC_ItemBlock)Item.itemsList[blockID];
				itemBlock.setCraftingToolModule(module.getNameWithoutPowerCraft());
				registerLanguage(module, itemBlock.getDefaultNames());
			}
			if(tileEntityClass!=null)
				GameRegistry.registerTileEntity(tileEntityClass, tileEntityClass.getName());
			return block;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setFieldsWithAnnotationTo(Class c, Class<? extends Annotation> annotationClass, Object obj, Object value){
		Field fa[] = c.getDeclaredFields();
		for(Field f:fa){
			if(f.isAnnotationPresent(annotationClass)){
				f.setAccessible(true);
				try {
					f.set(obj, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Object[] getFieldsWithAnnotationTo(Class c, Class<? extends Annotation> annotationClass, Object obj){
		List<Object> l = new ArrayList<Object>();
		Field fa[] = c.getDeclaredFields();
		for(Field f:fa){
			if(f.isAnnotationPresent(annotationClass)){
				f.setAccessible(true);
				try {
					l.add(f.get(obj));
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		return l.toArray(new Object[0]);
	}
	
	public static void setBlockState(World world, int x, int y, int z, boolean on){
		Block b = Block.blocksList[getBID(world, x, y, z)];
		if(b instanceof PC_Block){
			int meta = getMD(world, x, y, z);
			PC_TileEntity te = getTE(world, x, y, z);
			Class c = b.getClass();
			if(c.isAnnotationPresent(PC_Shining.class)){
				Block bon = (Block)getFieldsWithAnnotationTo(c, PC_Shining.ON.class, c)[0];
				Block boff = (Block)getFieldsWithAnnotationTo(c, PC_Shining.OFF.class, c)[0];
				if((b == bon && !on) || (b == boff && on)){
					if(te!=null)
						te.lockInvalid(true);
					if(on){
						world.setBlockAndMetadataWithNotify(x, y, z, bon.blockID, meta);
					}else{
						world.setBlockAndMetadataWithNotify(x, y, z, boff.blockID, meta);
					}
					if(te!=null){
						world.setBlockTileEntity(x, y, z, te);
						te.blockType = null;
						te.getBlockType();
						te.lockInvalid(false);
					}
				}
			}
		}
	}
	
	public static <t>t createClass(Class<t> c, Class[] param, Object[] objects) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		return c.getConstructor(param).newInstance(objects);
	}
	
	public static boolean getConfigBool(Configuration config, String category, String key, boolean defaultValue){
		return config.get(category, key, defaultValue).getBoolean(false);
	}
	
	public static boolean getConfigBool(Configuration config, String category, String key, boolean defaultValue, String description){
		config.get(category, key, defaultValue).comment = description;
		return config.get(category, key, defaultValue).getBoolean(false);
	}
	
	public static int getConfigInt(Configuration config, String category, String key, int defaultValue){
		return config.get(category, key, defaultValue).getInt(0);
	}
	
	public static int getConfigInt(Configuration config, String category, String key, int defaultValue, String description){
		config.get(category, key, defaultValue).comment = description;
		return config.get(category, key, defaultValue).getInt(0);
	}
	
	public static String getConfigString(Configuration config, String category, String key, String defaultValue){
		return config.get(category, key, defaultValue).value;
	}
	
	public static String getConfigString(Configuration config, String category, String key, String defaultValue, String description){
		config.get(category, key, defaultValue).comment = description;
		return config.get(category, key, defaultValue).value;
	}
	
	protected boolean soundEnabled = true; 
	
	public static boolean isSoundEnabled(){
		if(isServer())
			return false;
		return instance.soundEnabled;
	}
	
	public static void enableSound(boolean enable){
		instance.soundEnabled = enable;
	}
	
	protected void iPlaySound(double x, double y, double z, String sound, float soundVolume, float pitch){}
	
	public static void playSound(double x, double y, double z, String sound, float soundVolume, float pitch){
		if(isSoundEnabled()){
			instance.iPlaySound(x, y, z, sound, soundVolume, pitch);
		}
	}

	public static void addShapelessRecipe(PC_ItemStack itemStack, Object[] objects) {
		ArrayList<PC_ItemStack> var3 = new ArrayList<PC_ItemStack>();
        Object[] var4 = objects;
        int var5 = objects.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            Object var7 = var4[var6];

            if (var7 instanceof PC_ItemStack)
            {
                var3.add(((PC_ItemStack)var7).copy());
            }
            else if (var7 instanceof Item)
            {
                var3.add(new PC_ItemStack(var7));
            }
            else
            {
                if (!(var7 instanceof Block))
                {
                    throw new RuntimeException("Invalid shapeless recipy!");
                }

                var3.add(new PC_ItemStack(var7));
            }
        }

        GameRegistry.addRecipe(new PC_ShapelessRecipes(itemStack, var3));
	}
	
	public static void addRecipe(PC_ItemStack itemStack, Object[] objects) {
		String var3 = "";
        int var4 = 0;
        int var5 = 0;
        int var6 = 0;
        int var9;

        if (objects[var4] instanceof String[])
        {
            String[] var7 = (String[])((String[])objects[var4++]);
            String[] var8 = var7;
            var9 = var7.length;

            for (int var10 = 0; var10 < var9; ++var10)
            {
                String var11 = var8[var10];
                ++var6;
                var5 = var11.length();
                var3 = var3 + var11;
            }
        }
        else
        {
            while (objects[var4] instanceof String)
            {
                String var13 = (String)objects[var4++];
                ++var6;
                var5 = var13.length();
                var3 = var3 + var13;
            }
        }

        HashMap var14;

        for (var14 = new HashMap(); var4 < objects.length; var4 += 2)
        {
            Character var16 = (Character)objects[var4];
            PC_ItemStack var17 = null;

            if (objects[var4 + 1] instanceof Item)
            {
                var17 = new PC_ItemStack(objects[var4 + 1]);
            }
            else if (objects[var4 + 1] instanceof Block)
            {
                var17 = new PC_ItemStack(objects[var4 + 1], 1, -1);
            }
            else if (objects[var4 + 1] instanceof PC_ItemStack)
            {
                var17 = (PC_ItemStack)objects[var4 + 1];
            }

            var14.put(var16, var17);
        }

        PC_ItemStack[] var15 = new PC_ItemStack[var5 * var6];

        for (var9 = 0; var9 < var5 * var6; ++var9)
        {
            char var18 = var3.charAt(var9);

            if (var14.containsKey(Character.valueOf(var18)))
            {
                var15[var9] = ((PC_ItemStack)var14.get(Character.valueOf(var18))).copy();
            }
            else
            {
                var15[var9] = null;
            }
        }
		
		GameRegistry.addRecipe(new PC_ShapedRecipes(var5, var6, var15, itemStack));
	}
	
	protected static HashMap<String, Class> guis = new HashMap<String, Class>();
	
	public static void registerGres(String name, Class gui){
		guis.put(name, gui);
	}
	
	public static void registerGresArray(Object[] o){
		if(o==null)
			return;
		for(int i=0; i<o.length; i+=2){
			registerGres((String)o[i], (Class)o[i+1]);
		}
	}
	
	protected void iOpenGres(String name, EntityPlayer player, Object[]o){
		if(!(player instanceof EntityPlayerMP))
			return;
		int guiID = 0;
		try{
			Field var6 = EntityPlayerMP.class.getDeclaredFields()[16];
	        var6.setAccessible(true);
	        guiID = var6.getInt(player);
	        guiID = guiID % 100 + 1;
	        var6.setInt(player, guiID);
		} catch (Exception e){
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
		if(guis.containsKey(name)){
			Class c = guis.get(name);
			if(PC_GresBaseWithInventory.class.isAssignableFrom(c)){
				try {
					PC_GresBaseWithInventory bwi = createClass((Class<PC_GresBaseWithInventory>)c, new Class[]{EntityPlayer.class, Object[].class}, new Object[]{player, o});
					player.craftingInventory = bwi;
					player.craftingInventory.windowId = guiID;
					player.craftingInventory.addCraftingToCrafters((EntityPlayerMP)player);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void openGres(String name, EntityPlayer player, Object...o){
		instance.iOpenGres(name, player, o);
	}

	public static int getWorldDimension(World worldObj) {
		return worldObj.provider.dimensionId;
	}

	public static Object getPrivateValue(Class c, Object o, int i) {
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
	
	public static void setPrivateValue(Class c, Object o, int i, Object v) {
        try
        {
        	Field f = c.getDeclaredFields()[i];
            f.setAccessible(true);
            Field field_modifiers = Field.class.getDeclaredField("modifiers");
            field_modifiers.setAccessible(true);
            int modifier = field_modifiers.getInt(f);
            if((modifier & Modifier.FINAL)!=0){
            	field_modifiers.setInt(f, modifier & ~Modifier.FINAL);
            }
            f.set(o, v);
            if((modifier & Modifier.FINAL)!=0){
            	field_modifiers.setInt(f, modifier);
            }
        }
        catch (Exception e) {
            PC_Logger.severe("setPrivateValue failed with "+c+":"+o+":"+i);
        }
    }
	
	public static List<IRecipe> getRecipesForProduct(ItemStack prod) {
		List<IRecipe> recipes = new ArrayList<IRecipe>(CraftingManager.getInstance().getRecipeList());
		List<IRecipe> ret = new ArrayList<IRecipe>();
		
		for (IRecipe recipe:recipes) {
			try {
				if (recipe.getRecipeOutput().isItemEqual(prod) || (recipe.getRecipeOutput().itemID == prod.itemID && prod.getItemDamage() == -1)) {
					ret.add(recipe);
				}
			} catch (NullPointerException npe) {
				continue;
			}
		}

		return ret;
	}
	
	protected EnumGameType iGetGameTypeFor(EntityPlayer player){
		return ((EntityPlayerMP)player).theItemInWorldManager.getGameType();
	}
	
	public static EnumGameType getGameTypeFor(EntityPlayer player){
		return instance.iGetGameTypeFor(player);
	}
	
	public static boolean isCreative(EntityPlayer player){
		return getGameTypeFor(player).isCreative();
	}
	
	/**
	 * Tests if a given stack is a fuel
	 * 
	 * @param itemstack stack with item
	 * @return is fuel
	 */
	public static boolean isFuel(ItemStack itemstack) {
		if (itemstack == null) {
			return false;
		}

		return PC_InvUtils.getFuelValue(itemstack, 1f)>0;
		
	}
	
	/**
	 * Tests if a given stack can be smelted
	 * 
	 * @param itemstack stack with item
	 * @return is smeltable
	 */
	public static boolean isSmeltable(ItemStack itemstack) {
		if (itemstack == null || FurnaceRecipes.smelting().getSmeltingResult(itemstack.getItem().shiftedIndex) == null) {
			return false;
		}
		return true;
	}
	
	protected void iChatMsg(String msg, boolean clear){}
	
	/**
	 * Sends chat message onto the screen.
	 * 
	 * @param msg message
	 * @param clear clear screen before the messsage
	 */
	public static void chatMsg(String msg, boolean clear) {
		instance.iChatMsg(msg, clear);
	}

	/**
	 * Convert ticks to seconds
	 * 
	 * @param ticks ticks count
	 * @return seconds (double)
	 */
	public static double ticksToSecs(int ticks) {
		return ticks * 0.05D;
	}

	/**
	 * Convert ticks to seconds
	 * 
	 * @param ticks ticks count
	 * @return rounded seconds (int)
	 */
	public static int ticksToSecsInt(int ticks) {
		return Math.round(ticks * 0.05F);
	}

	/**
	 * Convert seconds to ticks
	 * 
	 * @param secs seconds
	 * @return ticks
	 */
	public static int secsToTicks(double secs) {
		return (int)(secs * 20);
	}
	
	public static String doubleToString(double d) {
		return ""+d;
	}
	
	public static <t extends TileEntity>t getTE(IBlockAccess world, int x, int y, int z){
		if(world!=null){
			TileEntity te = world.getBlockTileEntity(x, y, z);
			try{
				t tet = (t)te;
				return tet;
			}catch(ClassCastException e){
				return null;
			}
		}
		return null;
	}
	
	public static <t extends TileEntity>t getTE(IBlockAccess world, int x, int y, int z, int blockID){
		if(world!=null){
			if(getBID(world, x, y, z)==blockID){
				TileEntity te = world.getBlockTileEntity(x, y, z);
				try{
					t tet = (t)te;
					return tet;
				}catch(ClassCastException e){
					return null;
				}
			}
		}
		return null;
	}
	
	public static int getBID(IBlockAccess world, int x, int y, int z) {
		if(world!=null){
			return world.getBlockId(x, y, z);
		}
		return 0;
	}

	public static int getMD(IBlockAccess world, int x, int y, int z){
		if(world!=null){
			return world.getBlockMetadata(x, y, z);
		}
		return 0;
	}

	protected boolean iIsPlacingReversed(EntityPlayer player){
		return isKeyPressed(player, "keyReverse");
	}
	
	public static boolean isPlacingReversed(EntityPlayer player) {
		return instance.iIsPlacingReversed(player);
	}

	public static int reverseSide(int l) {
		if (l == 0) {
			l = 2;
		} else if (l == 2) {
			l = 0;
		} else if (l == 1) {
			l = 3;
		} else if (l == 3) {
			l = 1;
		}

		return l;
	}

	/**
	 * Perform hide redstone update around this gate.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param id
	 */
	public static void hugeUpdate(World world, int x, int y, int z, int blockID) {
		notifyBlockOfNeighborChange(world, x-2, y, z, blockID);
		notifyBlockOfNeighborChange(world, x-1, y, z, blockID);
		notifyBlockOfNeighborChange(world, x+1, y, z, blockID);
		notifyBlockOfNeighborChange(world, x+2, y, z, blockID);
		notifyBlockOfNeighborChange(world, x, y-2, z, blockID);
		notifyBlockOfNeighborChange(world, x, y-1, z, blockID);
		notifyBlockOfNeighborChange(world, x, y+1, z, blockID);
		notifyBlockOfNeighborChange(world, x, y+2, z, blockID);
		notifyBlockOfNeighborChange(world, x, y, z-2, blockID);
		notifyBlockOfNeighborChange(world, x, y, z-1, blockID);
		notifyBlockOfNeighborChange(world, x, y, z+1, blockID);
		notifyBlockOfNeighborChange(world, x, y, z+2, blockID);
		notifyBlockOfNeighborChange(world, x-1, y+1, z-1, blockID);
		notifyBlockOfNeighborChange(world, x+1, y+1, z-1, blockID);
		notifyBlockOfNeighborChange(world, x+1, y+1, z+1, blockID);
		notifyBlockOfNeighborChange(world, x-1, y+1, z+1, blockID);
		notifyBlockOfNeighborChange(world, x+1, y+1, z, blockID);
		notifyBlockOfNeighborChange(world, x-1, y+1, z, blockID);
		notifyBlockOfNeighborChange(world, x, y+1, z+1, blockID);
		notifyBlockOfNeighborChange(world, x, y+1, z-1, blockID);
		
		notifyBlockOfNeighborChange(world, x-1, y, z-1, blockID);
		notifyBlockOfNeighborChange(world, x+1, y, z-1, blockID);
		notifyBlockOfNeighborChange(world, x+1, y, z+1, blockID);
		notifyBlockOfNeighborChange(world, x-1, y, z+1, blockID);
		
		notifyBlockOfNeighborChange(world, x-1, y-1, z-1, blockID);
		notifyBlockOfNeighborChange(world, x+1, y-1, z-1, blockID);
		notifyBlockOfNeighborChange(world, x+1, y-1, z+1, blockID);
		notifyBlockOfNeighborChange(world, x-1, y-1, z+1, blockID);
		notifyBlockOfNeighborChange(world, x+1, y-1, z, blockID);
		notifyBlockOfNeighborChange(world, x-1, y-1, z, blockID);
		notifyBlockOfNeighborChange(world, x, y+1, z+1, blockID);
		notifyBlockOfNeighborChange(world, x, y+1, z-1, blockID);
		
	}
	
	public static <t extends TileEntity>t setTE(World world, int x, int y, int z, t createTileEntity) {
		world.setBlockTileEntity(x, y, z, createTileEntity);
		return createTileEntity;
	}
	
	/**
	 * Is the gate powered from given input? This method takes care of rotation
	 * for you. 0 BACK, 1 LEFT, 2 RIGHT, 3 FRONT, 4 BOTTOM, 5 TOP
	 * 
	 * @param world the World
	 * @param x
	 * @param y
	 * @param z
	 * @param inp the input number
	 * @return is powered
	 */
	
	public static boolean poweredFromInput(World world, int x, int y, int z, int inp) {
		return poweredFromInput(world, x, y, z, inp, 0);
	}
	
	/**
	 * Is the gate powered from given input? This method takes care of rotation
	 * for you. 0 BACK, 1 LEFT, 2 RIGHT, 3 FRONT, 4 BOTTOM, 5 TOP
	 * 
	 * @param world the World
	 * @param x
	 * @param y
	 * @param z
	 * @param inp the input number
	 * @param rotation the block rotation
	 * @return is powered
	 */
	public static boolean poweredFromInput(World world, int x, int y, int z, int inp, int rotation) {

		if(world==null)
			return false;
		
		if (inp == 4) {
			boolean isProviding = (world.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0) || (world.getBlockId(x, y - 1, z) == Block.redstoneWire.blockID && world
					.getBlockMetadata(x, y - 1, z) > 0));
			return isProviding;
		}
		if (inp == 5) {
			boolean isProviding = (world.isBlockIndirectlyProvidingPowerTo(x, y + 1, z, 1) || (world.getBlockId(x, y + 1, z) == Block.redstoneWire.blockID && world
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
			return (world.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3) || world.getBlockId(x, y, z + 1) == Block.redstoneWire.blockID
					&& world.getBlockMetadata(x, y, z + 1) > 0);
		}
		if (rotation == N1) {
			return (world.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4) || world.getBlockId(x - 1, y, z) == Block.redstoneWire.blockID
					&& world.getBlockMetadata(x - 1, y, z) > 0);
		}
		if (rotation == N2) {
			return (world.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2) || world.getBlockId(x, y, z - 1) == Block.redstoneWire.blockID
					&& world.getBlockMetadata(x, y, z - 1) > 0);
		}
		if (rotation == N3) {
			return (world.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5) || world.getBlockId(x + 1, y, z) == Block.redstoneWire.blockID
					&& world.getBlockMetadata(x + 1, y, z) > 0);
		}
		return false;
	}

	public static boolean isChestEmpty(World world, int x, int y, int z, ItemStack itemStack) {
		
		IInventory invAt = PC_InvUtils.getCompositeInventoryAt(world, new PC_CoordI(x, y, z));
		if (invAt != null){
			if (itemStack == null || itemStack.itemID == 0) {
				return PC_InvUtils.isInventoryEmpty(invAt);
			}else{
				return PC_InvUtils.isInventoryEmptyOf(invAt, itemStack);
			}
		}

		List<IInventory> list = world.getEntitiesWithinAABB(IInventory.class,
				AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(0.6D, 0.6D, 0.6D));

		if (list.size() >= 1) {
			if (itemStack == null || itemStack.itemID == 0) {
				return PC_InvUtils.isInventoryEmpty(list.get(0));
			}else{
				return PC_InvUtils.isInventoryEmptyOf(list.get(0), itemStack);
			}
		}
		
		List<PC_IInventoryWrapper> list2 = world.getEntitiesWithinAABB(PC_IInventoryWrapper.class,
				AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(0.6D, 0.6D, 0.6D));

		if (list2.size() >= 1) {
			if(list2.get(0).getInventory() != null) {
				if (itemStack == null || itemStack.itemID == 0) {
					return PC_InvUtils.isInventoryEmpty(list2.get(0).getInventory());
				}else{
					return PC_InvUtils.isInventoryEmptyOf(list2.get(0).getInventory(), itemStack);
				}
			}
		}

		return false;
		
	}

	public static boolean isChestFull(World world, int x, int y, int z, ItemStack itemStack) {
		
		IInventory invAt = PC_InvUtils.getCompositeInventoryAt(world, new PC_CoordI(x, y, z));
		if (invAt != null) {
			if (itemStack == null || itemStack.itemID == 0) {
				return PC_InvUtils.hasInventoryNoFreeSlots(invAt);
			} else {
				return PC_InvUtils.hasInventoryPlaceFor(invAt, itemStack);
			}
		}

		List<IInventory> list = world.getEntitiesWithinAABB(IInventory.class,
				AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(0.6D, 0.6D, 0.6D));

		if (list.size() >= 1) {
			if (itemStack == null || itemStack.itemID == 0) {
				return PC_InvUtils.hasInventoryNoFreeSlots(list.get(0));
			} else {
				return PC_InvUtils.hasInventoryPlaceFor(list.get(0), itemStack);
			}
		}

		List<PC_IInventoryWrapper> list2 = world.getEntitiesWithinAABB(PC_IInventoryWrapper.class,
				AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(0.6D, 0.6D, 0.6D));

		if (list2.size() >= 1) {
			if(list2.get(0).getInventory() != null) {
				if (itemStack == null || itemStack.itemID == 0) {
					return PC_InvUtils.hasInventoryNoFreeSlots(list2.get(0).getInventory());
				} else {
					return PC_InvUtils.hasInventoryPlaceFor(list2.get(0).getInventory(), itemStack);
				}
			}
		}

		return false;
		
	}

	public static void spawnMobFromSpawner(World world, int x, int y, int z) {
		TileEntityMobSpawner te = PC_Utils.getTE(world, x, y, z, Block.mobSpawner.blockID);
		if (te != null) {
			spawnMobs(world, x, y, z, te.getMobID());
		}
	}
	
	public static void preventSpawnerSpawning(World world, int x, int y, int z) {
		TileEntityMobSpawner te = PC_Utils.getTE(world, x, y, z, Block.mobSpawner.blockID);
		if (te != null) {
			te.delay = 20;
		}
	}
	
	/**
	 * Spawn blocks near this Special Controller
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param type Mob "name" string
	 */
	public static void spawnMobs(World world, int x, int y, int z, String type) {
		byte count = 5;

		boolean spawnParticles = world.getClosestPlayer(x + 0.5D, y + 0.5D, z + 0.5D, 16D) != null;

		for (int q = 0; q < count; q++) {
			EntityLiving entityliving = (EntityLiving) EntityList.createEntityByName(type, world);
			if (entityliving == null) {
				return;
			}
			int c = world.getEntitiesWithinAABB(entityliving.getClass(),
					AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(8D, 4D, 8D)).size();
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
			if (world.checkIfAABBIsClear(entityliving.boundingBox)
					&& world.getCollidingBoundingBoxes(entityliving, entityliving.boundingBox).size() == 0) {
				world.spawnEntityInWorld(entityliving);
				if (spawnParticles) {
					world.playAuxSFX(2004, x, y, z, 0);
					entityliving.spawnExplosionParticle();
				}
				return;
			}
		}
	}

	protected File iGetMCDirectory(){
		return mcs().getFile("");
	}
	
	public static MinecraftServer mcs(){
		return MinecraftServer.getServer();
	}
	
	public static File getMCDirectory() {
		return instance.iGetMCDirectory();
	}

	public static void notifyBlockOfNeighborChange(World world, int x, int y, int z, int blockId) {
		 Block block = Block.blocksList[world.getBlockId(x, y, z)];

         if (block != null)
         {
        	 block.onNeighborBlockChange(world, x, y, z, blockId);
         }
		
	}

	protected int iAddArmor(String name) {
		return 0;
	}
	
	public static int addArmor(String name) {
		return instance.iAddArmor(name);
	}

	protected boolean iIsEntityFX(Entity entity) {
		return false;
	}
	
	public static boolean isEntityFX(Entity entity) {
		return instance.iIsEntityFX(entity);
	}
	
	public static boolean isVersionNewer(String nVersion, String oVersion){
		String saVersion[], saNewVersion[];
		
		saVersion = oVersion.split("\\.");
		saNewVersion = nVersion.split("\\.");
		
		for(int i=0; i<saVersion.length; i++){
			if(i>=saNewVersion.length)
				return false;
			int version = 0, newVersion = 0;
			String sVersion = "", sNewVersion = "";
			for(int j=0; j<saVersion[i].length(); j++){
				char c = saVersion[i].charAt(j);
				boolean num=true;
				if(Character.isDigit(c) && num){
					version *= 10;
					version += c-'0';
				}else{
					num = false;
					sVersion += c;
				}
			}
			for(int j=0; j<saNewVersion[i].length(); j++){
				char c = saNewVersion[i].charAt(j);
				boolean num=true;
				if(Character.isDigit(c) && num){
					newVersion *= 10;
					newVersion += c-'0';
				}else{
					num = false;
					sNewVersion += c;
				}
			}
			if(newVersion>version){
				return true;
			}else if(newVersion<version){
				return false;
			}else{
				int comp = sNewVersion.compareToIgnoreCase(sVersion);
				if(comp>0){
					return false;
				}else if(comp<0){
					return true;
				}
			}
		}
		return false;
	}

	public static void loadFromNBT(NBTTagCompound nbttagcompound, String string, PC_INBT nbt) {
		NBTTagCompound nbttag = nbttagcompound.getCompoundTag(string);
		nbt.readFromNBT(nbttag);
	}

	public static void saveToNBT(NBTTagCompound nbttagcompound, String string, PC_INBT nbt) {
		NBTTagCompound nbttag = new NBTTagCompound();
		nbt.writeToNBT(nbttag);
		nbttagcompound.setCompoundTag(string, nbttag);
	}

	protected boolean iIsKeyPressed(EntityPlayer player, String key){
		if(!keyPressed.containsKey(player))
			return false;
		List<String> keyList = keyPressed.get(player);
		return keyList.contains(key);
	}
	
	public static boolean isKeyPressed(EntityPlayer player, String key){
		return instance.iIsKeyPressed(player, key);
	}
	
	protected void iWatchForKey(String name, int key){}
	
	public static void watchForKey(String name, int key){
		instance.iWatchForKey(name, key);
	}
	
	public static int watchForKey(Configuration config, String name, int key) {
		key = getConfigInt(config, Configuration.CATEGORY_GENERAL, name, key);
		watchForKey(name, key);
		return key;
	}
	
	public static void setReverseKey(Configuration config){
		instance.keyReverse = watchForKey(config, "keyReverse", 29);
	}
	
	public static PC_Block getPCBlockByName(String name){
		if(instance.objects.containsKey(name)){
			Object o = instance.objects.get(name);
			if(o instanceof PC_Block)
				return (PC_Block)o;
		}
		return null;
	}
	
	public static PC_Item getPCItemByName(String name){
		if(instance.objects.containsKey(name)){
			Object o = instance.objects.get(name);
			if(o instanceof PC_Item)
				return (PC_Item)o;
		}
		return null;
	}
	
	public static int getPCObjectIDByName(String name){
		if(instance.objects.containsKey(name)){
			Object o = instance.objects.get(name);
			if(o instanceof PC_Item)
				return ((PC_Item)o).shiftedIndex;
			else if(o instanceof PC_Block)
				return ((PC_Block)o).blockID;
		}
		return 0;
	}
	
	protected static final int KEYEVENT = 0;
	protected static final int SPAWNPARTICLEONBLOCKS = 1;
	
	@Override
	public boolean handleIncomingPacket(EntityPlayer player, Object[] o) {
		switch((Integer)o[0]){
		case KEYEVENT:
			List<String> keyList;
			if(keyPressed.containsKey(player))
				keyList = keyPressed.get(player);
			else
				keyPressed.put(player, keyList = new ArrayList<String>());
			String key = (String)o[2];
			if((Boolean)o[1]){
				if(!keyList.contains(key))
					keyList.add(key);
			}else{
				if(keyList.contains(key))
					keyList.remove((Object)key);
			}
			break;
		case SPAWNPARTICLEONBLOCKS:
			Random rand = new Random();
			List<PC_Struct2<PC_CoordI, Float>> blocks = (List<PC_Struct2<PC_CoordI, Float>>)o[1];
			for(PC_Struct2<PC_CoordI, Float> block:blocks){
				for(int i=0; i<100*block.b; i++){
					Block b = Block.blocksList[block.a.getId(player.worldObj)];
					int side = rand.nextInt(6);
					PC_CoordD pos;
					PC_CoordD move;
					AxisAlignedBB aabb = b.getSelectedBoundingBoxFromPool(player.worldObj, block.a.x, block.a.y, block.a.z);
					switch(side){
					case 0:
						pos = new PC_CoordD(aabb.minX-block.a.x, rand.nextFloat(), rand.nextFloat());
						move = new PC_CoordD(-rand.nextFloat()*0.04, rand.nextFloat()*0.08-0.04, rand.nextFloat()*0.08-0.04);
						break;
					case 1:
						pos = new PC_CoordD(aabb.maxX-block.a.x, rand.nextFloat(), rand.nextFloat());
						move = new PC_CoordD(rand.nextFloat()*0.04, rand.nextFloat()*0.08-0.04, rand.nextFloat()*0.08-0.04);
						break;
					case 2:
						pos = new PC_CoordD(rand.nextFloat(), aabb.minY-block.a.y, rand.nextFloat());
						move = new PC_CoordD(rand.nextFloat()*0.08-0.04, -rand.nextFloat()*0.04, rand.nextFloat()*0.08-0.04);
						break;
					case 3:
						pos = new PC_CoordD(rand.nextFloat(), aabb.maxY-block.a.y, rand.nextFloat());
						move = new PC_CoordD(rand.nextFloat()*0.08-0.04, rand.nextFloat()*0.04, rand.nextFloat()*0.08-0.04);
						break;
					case 4:
						pos = new PC_CoordD(rand.nextFloat(), rand.nextFloat(), aabb.minZ-block.a.z);
						move = new PC_CoordD(rand.nextFloat()*0.08-0.04, rand.nextFloat()*0.08-0.04, -rand.nextFloat()*0.04);
						break;
					case 5:
						pos = new PC_CoordD(rand.nextFloat(), rand.nextFloat(), aabb.maxZ-block.a.z);
						move = new PC_CoordD(rand.nextFloat()*0.08-0.04, rand.nextFloat()*0.08-0.04, rand.nextFloat()*0.04);
						break;
					default:
						pos = new PC_CoordD(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
						move = new PC_CoordD(rand.nextFloat()*0.08-0.04, rand.nextFloat()*0.08-0.04, rand.nextFloat()*0.08-0.04);
						break;
					}
					PC_Utils.spawnParticle("PC_EntityLaserParticleFX", player.worldObj, new PC_CoordD(block.a).offset(pos), new PC_Color(0.6,
							0.6, 1), move, 0);
				}
			}
			break;
		}
		return false;
	}

	/**
	 * Split comma separated list of integers.
	 * 
	 * @param list String containing the list.
	 * @return array of integers or null.
	 */
	public static List<Integer> parseIntList(String list) {
		if (list == null) {
			return null;
		}
		String[] parts = list.split(",");

		ArrayList<Integer> intList = new ArrayList<Integer>();

		for (String part : parts) {
			try {
				intList.add(Integer.parseInt(part));
			} catch (NumberFormatException e) {}
		}

		return intList;

	}
	
	/**
	 * Check if two objects are equal; null-safe test.
	 * 
	 * @param a first
	 * @param b second
	 * @return are equal
	 */
	public static boolean areObjectsEqual(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}
	
	public static Block getBlock(World world, int x, int y, int z){
		return Block.blocksList[PC_Utils.getBID(world, x, y, z)];
	}
	
	public static boolean canHarvest(Block b){
		if(b instanceof PC_Block){
			return ((PC_Block)b).canBeHarvest();
		}
		return true;
	}
	
	public static boolean canBuild(Item i){
		if(i instanceof PC_Item){
			return ((PC_Item)i).canBeBuild();
		}
		return true;
	}
	
	
	/**
	 * Extract and remove chest at given position.
	 * 
	 * @param world the world
	 * @param pos position of the chest
	 * @return itemstack with contents of the harvested chest
	 */
	public static ItemStack extractAndRemoveChest(World world, PC_CoordI pos) {

		if (canHarvest(getBlock(world, pos.x, pos.y, pos.z))) return null;

		TileEntity tec = pos.getTileEntity(world);

		if (tec == null) return null;

		ItemStack stack = new ItemStack(Block.lockedChest.blockID, 1, 0);

		NBTTagCompound blocktag = new NBTTagCompound();
		pos.getTileEntity(world).writeToNBT(blocktag);

		String name = "Unknown";
		int dmg = pos.getId(world);

		stack.setItemDamage(dmg);

		blocktag.setString("BlockName", name);
		blocktag.setInteger("BlockMeta", pos.getMeta(world));

		stack.setTagCompound(blocktag);
		if (tec instanceof IInventory) {

			IInventory ic = (IInventory) tec;

			for (int i = 0; i < ic.getSizeInventory(); i++) {
				ic.setInventorySlotContents(i, null);
			}
		}

		//Block.blocksList[pos.getId(world)].onBlockRemoval(world, pos.x, pos.y, pos.z);

		//if (tec instanceof PC_TileEntity) ((PC_TileEntity) tec).onBlockPickup();

		tec.invalidate();
		//world.removeBlockTileEntity(pos.x, pos.y, pos.z);

		pos.setBlock(world, 0, 0);


		return stack;

	}

	public static String getGresImageDir() {
		return PC_Module.getModule("Core").getTextureDirectory()+"gres/";
	}

	protected void iSpawnParticle(String name, Object[] o){
		
	}
	
	public static void spawnParticle(String name, Object...o) {
		instance.iSpawnParticle(name, o);
	}
	
	public static Constructor findBestConstructor(Class c, Class[] cp){
		Constructor[] constructors = c.getConstructors();
		for(Constructor constructor:constructors){
			Class[] cep = constructor.getParameterTypes();
			if((cep == null && cp == null) || (cep == null && cp.length==0) || (cep.length==0 && cp == null))
				return constructor;
			if(cep.length == cp.length){
				boolean ok = true;
				for(int i=0; i<cep.length; i++){
					if(cep[i].isPrimitive()){
						if(cep[i].equals(boolean.class)){
							cep[i] = Boolean.class;
						}else if(cep[i].equals(int.class)){
							cep[i] = Integer.class;
						}else if(cep[i].equals(Float.class)){
							cep[i] = Float.class;
						}else if(cep[i].equals(Double.class)){
							cep[i] = Double.class;
						}else if(cep[i].equals(long.class)){
							cep[i] = Long.class;
						}else if(cep[i].equals(char.class)){
							cep[i] = Integer.class;
						}else if(cep[i].equals(short.class)){
							cep[i] = Short.class;
						}
					}
					if(!cep[i].isAssignableFrom(cp[i])){
						ok = false;
						break;
					}
				}
				if(ok)
					return constructor;
			}
		}
		return null;
	}
	
	
	public static float giveConductorValueFor(Block b){
		if(b == null)
			return 0.0f;
		if(b.blockID == Block.blockSteel.blockID)
			return 0.9f;
		if(b.blockID == Block.blockGold.blockID)
			return 0.7f;
		return 0.0f;
	}
	
	private static void searchPowerReceiverConnectedTo(World world, int x, int y, int z, List<PC_Struct3<PC_CoordI, PC_IPowerReceiver, Float>> receivers, List<PC_Struct2<PC_CoordI, Float>> allpos, float power){
		Block b = getBlock(world, x, y, z);
		PC_CoordI pos = new PC_CoordI(x, y, z);
		PC_Struct2<PC_CoordI, Float> oldStruct = null;
		for(PC_Struct2<PC_CoordI, Float> s:allpos){
			if(s.a.equals(pos)){
				oldStruct = s;
				break;
			}
		}
		if(b instanceof PC_IPowerReceiver){
			if(oldStruct == null){
				receivers.add(new PC_Struct3<PC_CoordI, PC_IPowerReceiver, Float>(pos, (PC_IPowerReceiver)b, power));
			}
			return;
		}
		float value = giveConductorValueFor(b);
		if(value<0.01f)
			return;
		if(oldStruct == null){
			oldStruct = new PC_Struct2<PC_CoordI, Float>(pos, 0.0f);
			allpos.add(oldStruct);
		}
		if(power>oldStruct.b){
			oldStruct.b = power;
			power *= value;
			if(power<0.01f)
				return;
			searchPowerReceiverConnectedTo(world, x+1, y, z, receivers, allpos, power);
			searchPowerReceiverConnectedTo(world, x-1, y, z, receivers, allpos, power);
			searchPowerReceiverConnectedTo(world, x, y+1, z, receivers, allpos, power);
			searchPowerReceiverConnectedTo(world, x, y-1, z, receivers, allpos, power);
			searchPowerReceiverConnectedTo(world, x, y, z+1, receivers, allpos, power);
			searchPowerReceiverConnectedTo(world, x, y, z-1, receivers, allpos, power);
		}
	}
	
	public static List<PC_Struct3<PC_CoordI, PC_IPowerReceiver, Float>> getPowerReceiverConnectedTo(World world, int x, int y, int z){
		Random rand = new Random();
		List<PC_Struct3<PC_CoordI, PC_IPowerReceiver, Float>> receivers = new ArrayList<PC_Struct3<PC_CoordI,PC_IPowerReceiver,Float>>();
		List<PC_Struct2<PC_CoordI, Float>> blocks = new ArrayList<PC_Struct2<PC_CoordI, Float>>();
		searchPowerReceiverConnectedTo(world, x, y, z, receivers, blocks, 1.0f);
		PC_PacketHandler.sendToPacketHandler(true, world, "PacketUtils", SPAWNPARTICLEONBLOCKS, blocks);
		return receivers;
	}
	
	public static void givePowerToBlock(World world, int x, int y, int z, float power){
		List<PC_Struct3<PC_CoordI, PC_IPowerReceiver, Float>> powerReceivers = getPowerReceiverConnectedTo(world, x, y, z);
		float receivers = powerReceivers.size();
		for(PC_Struct3<PC_CoordI, PC_IPowerReceiver, Float> receiver:powerReceivers){
			receiver.b.receivePower(world, receiver.a.x, receiver.a.y, receiver.a.z, power/receivers * receiver.c);
		}
	}
	
}
