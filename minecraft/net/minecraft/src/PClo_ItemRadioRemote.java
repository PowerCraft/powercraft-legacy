package net.minecraft.src;

import java.util.List;


/**
 * Radio remote controller
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
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
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (!itemstack.hasTagCompound()) {
			onCreated(itemstack, world, entityplayer);
		}

		PClo_RadioManager.sendRemotePulse(itemstack.getTagCompound().getString("channel"));
		world.playSoundAtEntity(entityplayer, "random.click", (world.rand.nextFloat() + 0.7F) / 2.0F,
				1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);

		return itemstack;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack itemstack, List list) {

		if (itemstack.hasTagCompound()) {
			list.add(PC_Lang.tr("pc.radioRemote.desc", new String[] { itemstack.getTagCompound().getString("channel") }));
		}

	}
}
