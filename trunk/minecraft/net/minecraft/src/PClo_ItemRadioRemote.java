package net.minecraft.src;


import java.util.List;


/**
 * Radio remote controller
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_ItemRadioRemote extends Item {

	/**
	 * @param i ID
	 */
	public PClo_ItemRadioRemote(int i) {
		super(i);
		setMaxDamage(0);
		setMaxStackSize(1);
	}

	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
    	if (!itemstack.hasTagCompound()) {
			onCreated(itemstack, world, entityplayer);
		}

		mod_PClogic.DATA_BUS.anonymousTransmitterOn(itemstack.getTagCompound().getString("channel"));
		world.playSoundAtEntity(entityplayer, "random.click", (world.rand.nextFloat() + 0.7F) / 2.0F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
		
        entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        return itemstack;
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
    	return 0x11940; // dunno why, this is taken from bow item
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i) {
    	super.onPlayerStoppedUsing(itemstack, world, entityplayer, i);
    	mod_PClogic.DATA_BUS.anonymousTransmitterOff(itemstack.getTagCompound().getString("channel"));
		world.playSoundAtEntity(entityplayer, "random.click", (world.rand.nextFloat() + 0.7F) / 2.0F, 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
    }

	@Override
	public void onCreated(ItemStack itemstack, World world, EntityPlayer entityplayer) {

		NBTTagCompound tag = new NBTTagCompound();

		tag.setString("channel", "default");

		itemstack.setTagCompound(tag);
	}

	/**
	 * Set channel to this stack
	 * 
	 * @param itemstack stack
	 * @param channel new channel
	 */
	public static void setChannel(ItemStack itemstack, String channel) {

		NBTTagCompound tag = new NBTTagCompound();

		tag.setString("channel", channel == null ? "default" : channel);

		itemstack.setTagCompound(tag);
		PC_Utils.chatMsg(PC_Lang.tr("pc.radioRemote.connected", new String[] { channel }), true);
	}

	@Override
	public void addInformation(ItemStack itemstack, List list) {

		if (itemstack.hasTagCompound()) {
			list.add(PC_Lang.tr("pc.radioRemote.desc", new String[] { itemstack.getTagCompound().getString("channel") }));
		}

	}
}
