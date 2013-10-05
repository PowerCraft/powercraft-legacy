package powercraft.core.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import powercraft.api.items.PC_Item;
import powercraft.api.items.PC_ItemInfo;
import powercraft.api.multiblocks.covers.PC_CoverRecipes;
import powercraft.api.registries.PC_RecipeRegistry;
import powercraft.api.registries.PC_RecipeRegistry.PC_RecipeTypes;
import powercraft.api.registries.PC_TextureRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@PC_ItemInfo(name="Saw", itemid="saw", defaultid=17005)
public class PC_ItemSaw extends PC_Item {

	public PC_ItemSaw(int id) {
		super(id);
		setContainerItem(this);
		setMaxDamage(1024);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public void registerRecipes() {
		PC_RecipeRegistry.addRecipe(PC_RecipeTypes.IRECIPE, new PC_CoverRecipes(this));
		PC_RecipeRegistry.addRecipe(PC_RecipeTypes.SHAPED, this, "sss", " ii", 's', Item.stick, 'i', Item.ingotIron);
	}

	@Override
	public void loadIcons() {
		itemIcon = PC_TextureRegistry.registerIcon("Icon");
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack) {
		return false;
	}

	@Override
	public ItemStack getContainerItemStack(ItemStack itemStack) {
		return new ItemStack(itemStack.itemID, 1, itemStack.getItemDamage()+1);
	}

	@Override
	@SideOnly(Side.CLIENT)
    public boolean isFull3D(){
        return true;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRotateAroundWhenRendering(){
		return true;
	}
	
}
