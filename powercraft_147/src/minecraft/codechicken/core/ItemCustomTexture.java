package codechicken.core;

import net.minecraft.item.Item;

public class ItemCustomTexture extends Item
{
	public ItemCustomTexture(int itemID, int iconIndex, String texturefile)
	{
		super(itemID);
		this.iconIndex = iconIndex;
		this.texturefile = texturefile;
	}

	public String getTextureFile()
	{
		return texturefile;
	}
	
	String texturefile;
}
