// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst

package net.minecraft.src;

// Referenced classes of package net.minecraft.src:
// ItemBlock, Block, BlockCloth, ItemDye,
// ItemStack

public class PCtr_ItemBlockElevator extends ItemBlock {

	public PCtr_ItemBlockElevator(int i) {
		super(i);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getIconFromDamage(int i) {
		return 23;
	}

	@Override
	public int getMetadata(int i) {
		return i == 0 ? 0 : 1;
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return (new StringBuilder()).append(super.getItemName()).append(".").append(itemstack.getItemDamage() == 0 ? "up" : "down")
				.toString();
	}
}
