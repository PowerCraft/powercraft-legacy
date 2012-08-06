package net.minecraft.src;


import java.util.List;


/**
 * Ore radar device.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCco_ItemOreSniffer extends Item {

	/**
	 * @param i ID
	 */
	public PCco_ItemOreSniffer(int i) {
		super(i);
		setMaxStackSize(1);
		setMaxDamage(500);
	}

	@Override
	public boolean tryPlaceIntoWorld(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float par8, float par9, float par10) {
		int[] offsetX = { 0, 0, 0, 0, 1, -1 };
		int[] offsetZ = { 0, 0, 1, -1, 0, 0 };
		int[] offsetY = { 1, -1, 0, 0, 0, 0 };
		int MoveX = offsetX[l];
		int MoveY = offsetY[l];
		int MoveZ = offsetZ[l];

		PC_Utils.openGres(entityplayer, new PCco_GuiOreSnifferResultScreen(entityplayer, world, new PC_CoordI(i, j, k), new PC_CoordI(MoveX, MoveY,
				MoveZ)));

		itemstack.damageItem(1, entityplayer);

		return false;
	}

	@Override
	public void addInformation(ItemStack itemstack, List list) {
		list.add(PC_Lang.tr("pc.sniffer.desc"));
	}
}
