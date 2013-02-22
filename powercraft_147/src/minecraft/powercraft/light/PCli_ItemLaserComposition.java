package powercraft.light;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import powercraft.management.PC_BeamTracer;
import powercraft.management.PC_Color;
import powercraft.management.PC_GlobalVariables;
import powercraft.management.PC_Item;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_Utils.ModuleInfo;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.PC_VecI;
import powercraft.management.registry.PC_LangRegistry.LangEntry;

public class PCli_ItemLaserComposition extends PC_Item
{
    public PCli_ItemLaserComposition(int id)
    {
    	super(id, 0);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public int getMetadata(int i)
    {
        return i;
    }
    
    @Override
	public boolean requiresMultipleRenderPasses() {
		return true;
    }

    /**
	 * Get texture from damage and pass
	 * 
	 * @param dmg damage
	 * @param pass pass 0-1
	 */
	@Override
	public int getIconFromDamageForRenderPass(int dmg, int pass) {
		return pass == 0 ? 0 : 1;
	}
    
	@Override
    public int getColorFromItemStack(ItemStack itemStack, int pass)
    {
		if(pass==0)
			return 0xFFFFFF;
        return getColorForItemStack(new PC_ItemStack(itemStack)).getHex();
    }

	
	
    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float par8, float par9, float par10)
    {
    	
    	TileEntity te = GameInfo.getTE(world, i, j, k);
    	
    	if(te instanceof PCli_TileEntityLaser){
    		
    		((PCli_TileEntityLaser)te).setItemStack(new PC_ItemStack(itemstack));
    		
    		itemstack.stackSize = 0;
    		
    		return true;
    		
    	}

        return false;
    }
    
	@Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b)
    {
		NBTTagCompound nbtTagCompound = itemStack.getTagCompound();
        if(nbtTagCompound==null){
        	nbtTagCompound = new NBTTagCompound();
        	itemStack.setTagCompound(nbtTagCompound);
        }
        int levelKill = nbtTagCompound.getInteger("level.kill");
        int levelDistance = nbtTagCompound.getInteger("level.distance");
        int levelSensor = nbtTagCompound.getInteger("level.sensor");
        
        if (levelKill > 0)
        {
        	list.add(Lang.tr(getItemName() + ".kill.name", ("" + levelKill)));
        }

        if (levelDistance > 0)
        {
        	list.add(Lang.tr(getItemName() + ".distance.name", ("" + levelDistance)));
        }
        
        if (levelSensor > 0)
        {
        	list.add(Lang.tr(getItemName() + ".sensor.name", ("" + levelSensor)));
        }
    }
	
    @Override
	public void doCrafting(ItemStack itemStack, InventoryCrafting inventoryCrafting) {
    	ItemStack is = inventoryCrafting.getStackInRowAndColumn(1, 1);
    	int levelKill = 0;
        int levelDistance = 0;
    	int levelSensor = 0;
        
    	if(is != null && is.itemID == ModuleInfo.getPCObjectIDByName("PCco_BlockPowerCrystal")){
    		switch(is.getItemDamage()){
    		case 0:
    			levelDistance = 1;
    			break;
    		case 1:
    			levelKill = 1;
    			break;
    		case 2:
    			levelSensor = 1;
    			break;
    		}
    	}else{
    		int size = inventoryCrafting.getSizeInventory();
    		for(int i=0; i<size; i++){
    			is = inventoryCrafting.getStackInSlot(i);
    			if(is!=null){
    				if(is.itemID == itemID){
    					if(is.stackTagCompound!=null){
    						levelDistance += is.stackTagCompound.getInteger("level.distance");
    						levelKill += is.stackTagCompound.getInteger("level.kill");
    						levelSensor += is.stackTagCompound.getInteger("level.sensor");
    					}
    				}
    			}
    		}
    	}
    	
    	itemStack.stackTagCompound = new NBTTagCompound();
		itemStack.stackTagCompound.setInteger("level.distance", levelDistance);
		itemStack.stackTagCompound.setInteger("level.kill", levelKill);
		itemStack.stackTagCompound.setInteger("level.sensor", levelSensor);
	}
    
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		ItemStack itemStack = new ItemStack(this);
		itemStack.stackTagCompound = new NBTTagCompound();
		itemStack.stackTagCompound.setInteger("level.distance", 1);
		arrayList.add(itemStack);
		itemStack = new ItemStack(this);
		itemStack.stackTagCompound = new NBTTagCompound();
		itemStack.stackTagCompound.setInteger("level.kill", 1);
		arrayList.add(itemStack);
		itemStack = new ItemStack(this);
		itemStack.stackTagCompound = new NBTTagCompound();
		itemStack.stackTagCompound.setInteger("level.sensor", 1);
		arrayList.add(itemStack);
		return arrayList;
	}

	public static PC_Color getColorForItemStack(PC_ItemStack itemstack)
    {
    	if(itemstack==null){
    		return new PC_Color(1.0f, 1.0f, 1.0f);
    	}

        NBTTagCompound nbtTagCompound = itemstack.getNBTTag();
        if(nbtTagCompound==null){
        	nbtTagCompound = new NBTTagCompound();
        	itemstack.setNBTTag(nbtTagCompound);
        }
        int levelKill = nbtTagCompound.getInteger("level.kill");
        int levelSensor = nbtTagCompound.getInteger("level.sensor");
        int levelDistance = nbtTagCompound.getInteger("level.distance");
        int maxLevel = levelKill;
        if(maxLevel<levelSensor)
        	maxLevel = levelSensor;
        if(maxLevel<levelDistance)
        	maxLevel = levelDistance;
        if(maxLevel==0){
        	return new PC_Color(1.0f, 1.0f, 1.0f);
        }
        PC_Color c = new PC_Color();
        c.x = levelKill / (float)maxLevel;
        c.y = levelSensor / (float)maxLevel;
        c.z = levelDistance / (float)maxLevel;
        return c;
    }
    
	public static int getLengthLimit(PC_ItemStack itemstack, boolean b) {
		if(itemstack==null)
			return b?40:20;
		NBTTagCompound nbtTagCompound = itemstack.getNBTTag();
		if(nbtTagCompound==null){
			nbtTagCompound = new NBTTagCompound();
	        itemstack.setNBTTag(nbtTagCompound);
	    }
		int levelDistance = nbtTagCompound.getInteger("level.distance");
		return (20+levelDistance*10)*(b?2:1);
	}

	public static boolean onBlockHit(PC_BeamTracer beamTracer, Block block, PC_VecI coord, PC_ItemStack itemstack, boolean isBurning) {
		if(block.isOpaqueCube() && !GameInfo.hasFlag(beamTracer.getWorld(), coord, PC_Utils.BEAMTRACER_STOP)){
			return true;
		}
		return false;
	}

	public static boolean handleItem(int levelSensor){
		return levelSensor == 0 || levelSensor == 1 || levelSensor == 2 || levelSensor == 4;
	}
	
	public static boolean handleMob(int levelSensor){
		return levelSensor == 0 || levelSensor == 2 || levelSensor == 3 || levelSensor == 6;
	}
	
	public static boolean handlePlayer(int levelSensor){
		return levelSensor == 0 || levelSensor == 1 || levelSensor == 3 || levelSensor == 5;
	}
	
	public static boolean onEntityHit(PC_BeamTracer beamTracer, Entity entity, PC_VecI coord, PC_ItemStack itemstack, boolean isBurning) {
		if(itemstack==null)
			return true;
		NBTTagCompound nbtTagCompound = itemstack.getNBTTag();
        if(nbtTagCompound==null){
        	nbtTagCompound = new NBTTagCompound();
        	itemstack.setNBTTag(nbtTagCompound);
        }
        int levelKill = nbtTagCompound.getInteger("level.kill");
        int levelSensor = nbtTagCompound.getInteger("level.sensor");
        int levelDistance = nbtTagCompound.getInteger("level.distance");
        if(isBurning)
        	levelKill *= 2;
        boolean shineTrough = levelDistance<=4;
        if(levelKill>0)
        	if(entity instanceof EntityItem){
        		if(handleItem(levelSensor))
        			entity.setDead();
        		else
        			shineTrough = false;
        	}else if(entity instanceof EntityPlayer){
        		if(handlePlayer(levelSensor)){
        			ValueWriting.setPrivateValue(EntityLiving.class, entity, PC_GlobalVariables.indexRecentlyHit, 60);
            		entity.attackEntityFrom(PCli_DamageSourceLaser.getDamageSource(), levelKill);
        		}else
        			shineTrough = false;
        	}else if(entity instanceof EntityLiving){
        		if(handleMob(levelSensor)){
        			ValueWriting.setPrivateValue(EntityLiving.class, entity, PC_GlobalVariables.indexRecentlyHit, 60);
            		entity.attackEntityFrom(PCli_DamageSourceLaser.getDamageSource(), levelKill);
        		}else
        			shineTrough = false;
        	}else{
        		shineTrough = false;
        	}
		return shineTrough;
	}

	public static boolean isSensor(PC_ItemStack itemstack) {
		if(itemstack==null)
			return false;
		NBTTagCompound nbtTagCompound = itemstack.getNBTTag();
        if(nbtTagCompound==null){
        	nbtTagCompound = new NBTTagCompound();
        	itemstack.setNBTTag(nbtTagCompound);
        }
        int levelSensor = nbtTagCompound.getInteger("level.sensor");
		return levelSensor>0;
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<LangEntry> names = (List<LangEntry>)obj[0];
			names.add(new LangEntry(getItemName(), "Laser Composition"));
			names.add(new LangEntry(getItemName() + ".kill", "Kill Level %s"));
			names.add(new LangEntry( getItemName() + ".distance", "Distance Level %s"));
			names.add(new LangEntry(getItemName() + ".sensor", "Sensor Level %s"));;
            return names;
		}
		return null;
	}
	
	public Object areItemsEqual(PC_ItemStack itemStack, int otherMeta, NBTTagCompound otherNbtTag) {
		NBTTagCompound nbtTag = itemStack.getNBTTag();
		if(nbtTag==null||otherNbtTag==null)
			return true;
		return null;
	}
	
}
