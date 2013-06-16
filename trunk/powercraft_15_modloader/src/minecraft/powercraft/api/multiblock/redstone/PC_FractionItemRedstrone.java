package powercraft.api.multiblock.redstone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemDye;
import net.minecraft.src.ItemStack;
import powercraft.api.multiblock.PC_FractionBlock;
import powercraft.api.multiblock.PC_FractionItem;
import powercraft.api.registry.PC_LangRegistry.LangEntry;

public class PC_FractionItemRedstrone extends PC_FractionItem {

	public PC_FractionItemRedstrone(int id) {
		super(id, "Cable");
		setCreativeTab(CreativeTabs.tabRedstone);
		setHasSubtypes(true);
	}

	@Override
	protected PC_FractionBlock getFractionBlock(ItemStack itemStack) {
		return new PC_FractionBlockRedstoneCable(itemStack.getItemDamage());
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return getUnlocalizedName()+".cable"+itemStack.getItemDamage();
	}

	@Override
	public List<LangEntry> getNames(ArrayList<LangEntry> names) {
		for(int i=0; i<16; i++){
			names.add(new LangEntry(getUnlocalizedName()+".cable"+i, "Cable "+ItemDye.dyeColorNames[i]));
		}
		return names;
	}

	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		for(int i=0; i<16; i++){
			arrayList.add(new ItemStack(this, 1, i));
		}
		return arrayList;
		
	}

	@Override
	public int getColorFromItemStack(ItemStack itemStack, int pass) {
		return ItemDye.dyeColors[itemStack.getItemDamage()];
	}
	
}
