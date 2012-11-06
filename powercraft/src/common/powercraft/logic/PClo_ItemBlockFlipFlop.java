package powercraft.logic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import powercraft.core.PC_ItemBlock;
import powercraft.core.PC_MathHelper;
import powercraft.core.PC_Utils;

public class PClo_ItemBlockFlipFlop extends PC_ItemBlock {

	public PClo_ItemBlockFlipFlop(int id){
		super(id);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@Override
	public String[] getDefaultNames() {
		List<String> s =  new ArrayList<String>();
		for(int i=0; i<PClo_FlipFlopType.TOTAL_FLIPFLOP_COUNT; i++){
			s.add(getItemName()+".flipflop"+i);
			s.add("FlipFlop "+PClo_FlipFlopType.names[i]);
		};
		s.add(getItemName());
		s.add("FlipFlop");
		return s.toArray(new String[0]);
	}
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		for(int i=0; i<PClo_FlipFlopType.TOTAL_FLIPFLOP_COUNT; i++)
			arrayList.add(new ItemStack(this, 1, i));
		return arrayList;
	}
	
	@Override
	public int getIconFromDamage(int i) {
		return mod_PowerCraftLogic.flipFlop.getBlockTextureFromSideAndMetadata(1, 0);
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return getItemName() + ".flipflop" + itemstack.getItemDamage();
	}
	
	@Override
	public boolean isFull3D() {
		return false;
	}

	@Override
	public boolean shouldRotateAroundWhenRendering() {
		return false;
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b) {
		list.add(getDescriptionForGate(itemStack.getItemDamage()));
	}

	/**
	 * Get description bubble for gate
	 * 
	 * @param dmg gate item damage value
	 * @return the description string
	 */
	public static String getDescriptionForGate(int dmg) {
		return PC_Utils.tr("pc.flipflop." + PClo_FlipFlopType.names[PC_MathHelper.clamp_int(dmg, 0, PClo_FlipFlopType.TOTAL_FLIPFLOP_COUNT - 1)] + ".desc");
	}

}
