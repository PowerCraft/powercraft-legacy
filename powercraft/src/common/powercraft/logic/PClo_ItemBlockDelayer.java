package powercraft.logic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import powercraft.core.PC_ItemBlock;
import powercraft.core.PC_MathHelper;
import powercraft.core.PC_Utils;

public class PClo_ItemBlockDelayer extends PC_ItemBlock {

	public PClo_ItemBlockDelayer(int id){
		super(id);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@Override
	public String[] getDefaultNames() {
		List<String> s =  new ArrayList<String>();
		for(int i=0; i<PClo_DelayerType.TOTAL_DELAYER_COUNT; i++){
			s.add(getItemName()+".delayer"+i);
			s.add("Delayer "+PClo_DelayerType.names[i]);
		};
		s.add(getItemName());
		s.add("Delayer");
		return s.toArray(new String[0]);
	}
	
	@Override
	public List<ItemStack> getItemStacks(List<ItemStack> arrayList) {
		for(int i=0; i<PClo_DelayerType.TOTAL_DELAYER_COUNT; i++)
			arrayList.add(new ItemStack(this, 1, i));
		return arrayList;
	}
	
	@Override
	public int getIconFromDamage(int i) {
		return mod_PowerCraftLogic.delayer.getBlockTextureFromSideAndMetadata(1, 0);
	}

	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return getItemName() + ".delayer" + itemstack.getItemDamage();
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
		return PC_Utils.tr("pc.delayer." + PClo_DelayerType.names[PC_MathHelper.clamp_int(dmg, 0, PClo_DelayerType.TOTAL_DELAYER_COUNT - 1)] + ".desc");
	}

}
