package codechicken.core;

import codechicken.core.render.SpriteSheetManager;
import codechicken.core.render.SpriteSheetManager.SpriteSheet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemCustomTexture extends Item
{
    @SideOnly(Side.CLIENT)
    private int sprite;
    @SideOnly(Side.CLIENT)
    private SpriteSheet spriteSheet;
    
    public ItemCustomTexture(int itemID, int iconIndex, String texturefile)
    {
        super(itemID);
        sprite = iconIndex;
        spriteSheet = SpriteSheetManager.getSheet(texturefile);
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        spriteSheet.requestIndicies(sprite);
        spriteSheet.registerIcons(register);
        itemIcon = spriteSheet.getSprite(sprite);
    }
}
