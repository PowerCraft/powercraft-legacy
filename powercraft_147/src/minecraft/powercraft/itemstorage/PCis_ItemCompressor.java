package powercraft.itemstorage;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.management.PC_IPacketHandler;
import powercraft.management.PC_Item;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Struct3;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_VecI;

public class PCis_ItemCompressor extends PC_Item implements PC_IPacketHandler {
	
	public static final int NORMAL = 0, ENDERACCESS = 1, HIGHT = 2, BIG = 3;
	public static final String id2Name[] = {"normal", "enderaccess", "hight", "big"};
	public static final int id2Texture[] = {0, 2, 3, 1};
	
	public PCis_ItemCompressor(int id) {
		super(id, 0);
		setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!world.isRemote){
        	Gres.openGres("Compressor", entityplayer, null);
        }
        return itemstack;
    }
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		arrayList.add(new ItemStack(this, 1, NORMAL));
		arrayList.add(new ItemStack(this, 1, ENDERACCESS));
		arrayList.add(new ItemStack(this, 1, HIGHT));
		arrayList.add(new ItemStack(this, 1, BIG));
		return arrayList;
	}
	
	@Override
	public String getItemNameIS(ItemStack itemStack) {
		return super.getItemName() + "." + id2Name[itemStack.getItemDamage()];
	}
	
	@Override
	public int getIconFromDamage(int par1) {
		return id2Texture[par1];
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
		if(itemStack.hasTagCompound()){
			if(itemStack.getTagCompound().hasKey("label"))
				list.add(itemStack.getTagCompound().getString("label"));
		}
	}

	private int findSlotInPlayerInvFor(ItemStack itemStack, EntityPlayer player, int putStacks){
		for(int i=0; i<player.inventory.getSizeInventory(); i++){
			ItemStack is = player.inventory.getStackInSlot(i);
			if(is!=null && itemStack.isItemEqual(is)){
				if(putStacks==0){
					return i;
				}else{
					putStacks--;
				}
			}
		}
		return -1;
	}
	
	private int findSlotInPlayerInvForStore(ItemStack itemStack, EntityPlayer player, int putStacks, int startSlot){
		for(int i=startSlot; i<player.inventory.getSizeInventory(); i++){
			ItemStack is = player.inventory.getStackInSlot(i);
			if(is!=null && itemStack.isItemEqual(is) && is.stackSize<is.getMaxStackSize()){
				if(is.stackSize<is.getMaxStackSize()){
					if(putStacks>0){
						return i;
					}else{
						return -1;
					}
				}else{
					putStacks--;
				}
			}
		}
		return -1;
	}
	
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
		if(entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)entity;
			if(player.openContainer instanceof PCis_ContainerCompressor && par5)
				return;
			boolean takeStacks = isTakeStacks(itemStack);
			int putStacks = getPutStacks(itemStack);
			PCis_CompressorInventory compressorinv = getInventoryFor(player, par4);
			if(takeStacks){
				for(int i=0; i<compressorinv.getSizeInventory(); i++){
					ItemStack is = compressorinv.getStackInSlot(i);
					if(is!=null){
						int need = compressorinv.getSlotStackLimit(i)-is.stackSize;
						if(need>0){
							int playerSlot = findSlotInPlayerInvFor(is, player, putStacks);
							if(playerSlot!=-1){
								ItemStack isp = player.inventory.getStackInSlot(playerSlot);
								if(need>isp.stackSize){
									is.stackSize+=isp.stackSize;
									player.inventory.setInventorySlotContents(playerSlot, null);
								}else{
									is.stackSize+=need;
									isp.stackSize-=need;
									if(isp.stackSize==0)
										player.inventory.setInventorySlotContents(playerSlot, null);
								}
							}
						}
					}
				}
			}
			for(int n=0; n<putStacks; n++){
				for(int i=0; i<compressorinv.getSizeInventory(); i++){
					ItemStack is = compressorinv.getStackInSlot(i);
					if(is!=null && is.stackSize>1){
						int playerSlot = findSlotInPlayerInvForStore(is, player, putStacks, 0);
						while(playerSlot!=-1){
							ItemStack isp = player.inventory.getStackInSlot(playerSlot);
							int need = isp.getMaxStackSize()-isp.stackSize;
							if(need>is.stackSize){
								isp.stackSize += is.stackSize-1;
								is.stackSize = 1;
							}else{
								isp.stackSize+=need;
								is.stackSize-=need;
								if(is.stackSize==0){
									is.stackSize=1;
									isp.stackSize--;
								}
							}
							playerSlot = findSlotInPlayerInvForStore(is, player, putStacks, playerSlot+1);
						}
					}
				}
			}
			compressorinv.closeChest();
		}
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
		case PC_Utils.MSG_DEFAULT_NAME:
			List<PC_Struct3<String, String, String[]>> names = (List<PC_Struct3<String, String, String[]>>)obj[0];
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+"."+id2Name[NORMAL], "compressor", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+"."+id2Name[ENDERACCESS], "ender compressor", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+"."+id2Name[HIGHT], "high stack compressor", null));
			names.add(new PC_Struct3<String, String, String[]>(getItemName()+"."+id2Name[BIG], "big compressor", null));
			return names;
		}
		return null;
	}

	public static PCis_CompressorInventory getInventoryFor(EntityPlayer player, int equipment){
		if(equipment==-1)
			equipment=player.inventory.currentItem;
		ItemStack compressor = player.inventory.getStackInSlot(equipment);
		switch(compressor.getItemDamage()){
		case NORMAL:
			return new PCis_NormalCompressorInventory(player, equipment);
		case ENDERACCESS:
			return new PCis_EnderCompressorInventory(player, equipment);
		case HIGHT:
			return new PCis_HightCompressorInventory(player, equipment);
		case BIG:
			return new PCis_NormalCompressorInventory(player, equipment, new PC_VecI(9, 6));
		default:
			return null;
		}
	}
	
	public static String getName(ItemStack item) {
		if(item.hasTagCompound()){
			if(item.getTagCompound().hasKey("label"))
				return item.getTagCompound().getString("label");
		}
		return "";
	}
	
	public static void setName(EntityPlayer thePlayer, String name) {
		NBTTagCompound nbtTag = getItem(thePlayer).getTagCompound();
		if(nbtTag==null){
			nbtTag = new NBTTagCompound();
			getItem(thePlayer).setTagCompound(nbtTag);
		}
		if(name==null || name.equals(""))
			nbtTag.removeTag("label");
		else
			nbtTag.setString("label", name);
		if(thePlayer.worldObj.isRemote){
			PC_PacketHandler.sendToPacketHandler(true, thePlayer.worldObj, "ItemCompressor", "setName", name);
		}
	}

	public static int getPutStacks(ItemStack item) {
		if(item.hasTagCompound()){
			if(item.getTagCompound().hasKey("putStacks"))
				return item.getTagCompound().getInteger("putStacks");
		}
		return 0;
	}
	
	public static void setPutStacks(EntityPlayer thePlayer, int num) {
		NBTTagCompound nbtTag = getItem(thePlayer).getTagCompound();
		if(nbtTag==null){
			nbtTag = new NBTTagCompound();
			getItem(thePlayer).setTagCompound(nbtTag);
		}
		nbtTag.setInteger("putStacks", num);
		if(thePlayer.worldObj.isRemote){
			PC_PacketHandler.sendToPacketHandler(true, thePlayer.worldObj, "ItemCompressor", "setPutStacks", num);
		}
	}

	public static boolean isTakeStacks(ItemStack item) {
		if(item.hasTagCompound()){
			if(item.getTagCompound().hasKey("takeStacks"))
				return item.getTagCompound().getBoolean("takeStacks");
		}
		return false;
	}
	
	public static void setTakeStacks(EntityPlayer thePlayer, boolean checked) {
		NBTTagCompound nbtTag = getItem(thePlayer).getTagCompound();
		if(nbtTag==null){
			nbtTag = new NBTTagCompound();
			getItem(thePlayer).setTagCompound(nbtTag);
		}
		nbtTag.setBoolean("takeStacks", checked);
		if(thePlayer.worldObj.isRemote){
			PC_PacketHandler.sendToPacketHandler(true, thePlayer.worldObj, "ItemCompressor", "setTakeStacks", checked);
		}
	}

	public static ItemStack getItem(EntityPlayer player){
		return player.inventory.getCurrentItem();
	}
	
	@Override
	public boolean handleIncomingPacket(EntityPlayer player, Object[] o) {
		String key = (String)o[0];
		if(key.equals("setTakeStacks")){
			setTakeStacks(player, (Boolean)o[1]);
		}else if(key.equals("setPutStacks")){
			setPutStacks(player, (Integer)o[1]);
		}else if(key.equals("setName")){
			setName(player, (String)o[1]);
		}
		return false;
	}
	
}
